package com.easy.tech.serialutil.engine;

import static com.easy.tech.serialutil.configs.LogConfig.DEBUG;
import static com.easy.tech.serialutil.configs.SerialConfig.FLAG_MASK_POWER;
import static com.easy.tech.serialutil.configs.SerialConfig.FLAG_POWER_OFF;
import static com.easy.tech.serialutil.configs.SerialConfig.FLAG_POWER_ON;

import static java.lang.Thread.sleep;

import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import com.easy.tech.serialutil.configs.LogConfig;
import com.easy.tech.serialutil.serialdevices.ISerialDeviceStrategy;
import com.easy.tech.serialutil.serialdevices.SerialDeviceFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SerialEngine {
    private static final String TAG = "SerialEngine";
    public static final int INVALID_FD = -1;

    private final ISerialDeviceStrategy mSerial;
    private final SerialEngineParameter mParameter;
    private int mFd;
    private Status status;
    private ThreadPoolExecutor threadPoolExecutor;
    private long mReadPeriod = 100;
    private byte[] mReadBuffer;
    private final Runnable readTask = new Runnable() {
        @Override
        public void run() {
            while (mReadPeriod > 0 && null != onDataReceiveListener) {
                try {
                    sleep(mReadPeriod);
                    int len = read(mReadBuffer, mReadBuffer.length);
                    if (0 >= len) continue;
                    onDataReceiveListener.onDataReceive(mReadBuffer, len);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    };

    private SerialEngine(ISerialDeviceStrategy serial, int baudrate, boolean writeOnly, int bits, int checkFlag, int stopBitCount) {
        this.mSerial = serial;
        if (DEBUG)
            Log.d(TAG, "power SerialEngine：name " + mSerial.getPath() + ",getSerialMask:" + mSerial.getSerialMask());
        this.mParameter = new SerialEngineParameter();
        this.mParameter.setBaudRate(baudrate);
        this.mParameter.setPowerFlag(mSerial.getSerialMask() << 4);
        this.mParameter.setWriteOnly(writeOnly);
        this.mParameter.setBits(bits);
        this.mParameter.setCheckFlag(checkFlag);
        this.mParameter.setStopBitCount(stopBitCount);
        this.status = Status.INITIALIZED;
        this.mFd = INVALID_FD;
        if (LogConfig.DEBUG)
            Log.d(TAG, "Serial engine is initialized! path: " + this.mSerial.getPath() + ", baud rate: " + this.mParameter.getBaudRate() + ", flag: " + this.mParameter.getPowerFlag());
    }

    public synchronized Status open() {
        if (LogConfig.DEBUG)
            Log.d(TAG, "open(), " + getStatus());
        if (status.equals(Status.INITIALIZED) || status.equals(Status.CLOSED)) {
            mFd = NativeSerialEngine.open(this.mSerial.getPath(), this.mParameter.getBaudRate(), this.mParameter.isWriteOnly(), this.mParameter.getBits(), this.mParameter.getCheckFlag(), this.mParameter.getStopBitCount());
            if (mFd > 0) {
                status = Status.OPENED;
            }
        }
        return status;
    }

    public Status openWithPower() {
        if (Status.OPENED == open()) status = setPower(true);
        return status;
    }

    public synchronized Status setPower(boolean on) {
        if (mFd <= 0) return status = Status.INITIALIZED;
        if (LogConfig.DEBUG)
            Log.d(TAG, "setPower(" + on + "), power flag 1: " + this.mParameter.getPowerFlag() + ", " + (this.mParameter.getPowerFlag() & FLAG_MASK_POWER));
        int flag = this.mParameter.getPowerFlag() & FLAG_MASK_POWER | (on ? FLAG_POWER_ON : FLAG_POWER_OFF);
        if (LogConfig.DEBUG)
            Log.d(TAG, "setPower(), power flag 2: " + flag);
        NativeSerialEngine.setPower(mFd, flag);
        if (LogConfig.DEBUG)
            Log.d(TAG, "Set serial(" + mFd + "), flag: " + this.mParameter.getPowerFlag());
        if (LogConfig.DEBUG)
            Log.d(TAG, "Set serial(" + mFd + "), power statues: " + flag + ", " + FLAG_POWER_ON);
        return status = (flag & FLAG_POWER_ON) == FLAG_POWER_ON ? Status.POWER_ON : Status.POWER_OFF;
    }

    public synchronized int read(byte[] buffer, int size) {
        Log.d(TAG, "Read from serial(" + mFd + ") start. Is power not on? " + (!status.equals(Status.POWER_ON)));
        if (mFd <= 0 || !status.equals(Status.POWER_ON)) return 0;
        int length = NativeSerialEngine.read(mFd, buffer, size);
        if (LogConfig.DEBUG && length > 0)
            Log.d(TAG, "Read from serial(" + mFd + "): " + new String(buffer, 0, length));
        return length;
    }

    public synchronized int write(byte[] buffer, int size) {
        if (mFd <= 0 || !status.equals(Status.POWER_ON)) return 0;
        if (LogConfig.DEBUG)
            Log.d(TAG, "Will write to serial(" + mFd + ") [size = " + size + "]: " + new String(buffer, 0, size, StandardCharsets.ISO_8859_1));
        int length = NativeSerialEngine.write(mFd, buffer, size);
        if (LogConfig.DEBUG && length > 0)
            Log.d(TAG, "Write to serial(" + mFd + "): " + new String(buffer, 0, length, StandardCharsets.ISO_8859_1));
        return length;
    }

    public synchronized Status close() {
        if (LogConfig.DEBUG) Log.d(TAG, "Close serial(" + mFd + ").");
        stopContinueRead(()->{});
        if (mFd > 0) NativeSerialEngine.close(mFd);
        if (LogConfig.DEBUG) Log.d(TAG, "Close serial status:");
        status = Status.CLOSED;
        return status;
    }

    public Status closeWithPower() {
        setPower(false);
        return close();
    }

    public boolean setBaudRate(int newBaudRate) {
        this.mParameter.setBaudRate(newBaudRate);
        reopen();
        if (LogConfig.DEBUG)
            Log.d(TAG, "Set baudrate, " + getStatus());
        return status.equals(Status.OPENED) || status.equals(Status.POWER_ON) || status.equals(Status.POWER_OFF);
    }

    public Status reopen() {
        boolean isPowerOn = status.equals(Status.POWER_ON);
        if (LogConfig.DEBUG)
            Log.d(TAG, "Will reopen serial(" + mFd + "). the current power is " + (isPowerOn ? "ON" : "OFF" + "!"));
        close();
        open();
        return setPower(isPowerOn);
    }

    public void startContinueRead(byte[] buffer,long period) throws Throwable {
        //if (!status.equals(Status.POWER_ON)) throw new Throwable("Please power the serial first!");
        mReadBuffer = buffer;
        mReadPeriod = period;
        stopContinueRead(()->{
            threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
            threadPoolExecutor.execute(readTask);
        });
    }

    public void stopContinueRead(@NonNull OnReadTaskStoppedListener listener) {
        if (null == threadPoolExecutor) {
            listener.onReadTaskStopped();
            return;
        }
        threadPoolExecutor.remove(readTask);
        threadPoolExecutor.purge();
        threadPoolExecutor.shutdownNow();
        long time = SystemClock.currentThreadTimeMillis();
        new Thread(()->{
            while (!threadPoolExecutor.isShutdown()){
                if (SystemClock.currentThreadTimeMillis() - time > 5000) break;
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            listener.onReadTaskStopped();
            threadPoolExecutor = null;
        }).start();
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        UNINITIALIZED,
        INITIALIZED,
        OPENED,
        POWER_ON,
        POWER_OFF,
        CLOSED
    }

    public static class Builder {

        ISerialDeviceStrategy strategy;
        SerialEngineParameter parameter;

        /***
         * Serial engine builder constructor
         * @param serialName Never open the same serial port more than once!!!
         */
        public Builder(String serialName) {
            if  (DEBUG) Log.d(TAG, "Builder() serialName: " + serialName);
            this.strategy = SerialDeviceFactory.getInstance().getSerialByName(serialName);
            Log.i(TAG, "Builder serial strategy: " + strategy.getPath());
            this.parameter = new SerialEngineParameter();
            if  (DEBUG) Log.d(TAG, "Builder() serialName END!");
        }

        /***
         * Serial engine builder constructor
         * @param serialName Name of serial. Never open the same serial port more than once!!!
         * @param serialMask Mark of serial.
         */
        public Builder(String serialName, int serialMask) {
            if (DEBUG) Log.d(TAG, "Builder() serialName: " + serialName + ", serialMask: " + serialMask);
            this.strategy = SerialDeviceFactory.getInstance().getNewSerial(serialName, serialMask);
            Log.i(TAG, "Builder serial strategy: " + strategy.getPath() + ", Serial mask: " + strategy.getSerialMask());
            this.parameter = new SerialEngineParameter();
            if  (DEBUG) Log.d(TAG, "Builder() new serialName END!");
        }

        public Builder setBaudRate(int baudrate) {
            this.parameter.setBaudRate(baudrate);
            return this;
        }

        public Builder setWriteOnly(boolean writeOnly) {
            this.parameter.setWriteOnly(writeOnly);
            return this;
        }

        public Builder setDataBitCount(int bits) {
            this.parameter.setBits(bits);
            return this;
        }

        public Builder setCheckFlag(int checkFlag) {
            this.parameter.setCheckFlag(checkFlag);
            return this;
        }

        public Builder setStopBitCount(int stopBitCount) {
            this.parameter.setStopBitCount(stopBitCount);
            return this;
        }

        public SerialEngine build() {
            return new SerialEngine(this.strategy, this.parameter.getBaudRate(),
                    this.parameter.isWriteOnly(), this.parameter.getBits(), this.parameter.getCheckFlag(),
                    this.parameter.getStopBitCount());
        }
    }

    private OnDataReceiveListener onDataReceiveListener = null;
    private volatile boolean isStop = false;

    public interface OnDataReceiveListener {
        void onDataReceive(byte[] buffer, int size);
    }

    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

    public interface OnReadTaskStoppedListener {
        void onReadTaskStopped();
    }

}

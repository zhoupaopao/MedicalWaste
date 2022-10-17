package com.easy.tech.serialutil.engine;

import android.util.Log;

import com.easy.tech.serialutil.configs.LogConfig;

import java.io.FileDescriptor;

class NativeSerialEngine {
    private static final String TAG = "NativeSerialEngine";

    static {
        try {
            if (LogConfig.DEBUG) Log.d(TAG, "load library serial");
            System.loadLibrary("serial");
        } catch (Exception e) {
            Log.e(TAG, "Load library serial_port error e:" + e.toString());
        }
    }

    protected static native int open(String path, int baud, boolean writeOnly, int bits, int checkFlag, int stop);

    protected static native void setPower(int fd, int flag);

    protected static native boolean switchBaudRate(int fd, int baud);

    protected static native int read(int fd, byte[] readBuffer, int size);

    protected static native int write(int fd, byte[] writeBuffer, int size);

    protected static native void close(int fd);
}

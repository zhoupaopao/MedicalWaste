package com.easy.tech.serialutil.serialdevices;

import static com.easy.tech.serialutil.configs.LogConfig.DEBUG;

import android.os.Build;
import android.util.Log;

import java.util.HashMap;

public class SerialDeviceFactory {
    private static final String TAG = "SerialDeviceFactory";
    private static final HashMap<String, ISerialDeviceStrategy> SERIAL_MAP = new HashMap<>();
    private static final String MODEL = Build.MODEL;
    private static final String SPLIT = "_";
    private static final String DEFAULT_SERIAL = "DEFAULT";
    private static final String TTYS0 = "ttyS0";
    private static final String TTYS2 = "ttyS2";

    static {
        SERIAL_MAP.put("uis7862s_1h10_Natv_ttyS0", new uis7862s_1h10_Natv_ttyS0());
        SERIAL_MAP.put("Alpha B_ttyS0", new AlphaB_ttyS0());
        SERIAL_MAP.put("Alpha B_ttyS2", new AlphaB_ttyS2());
        SERIAL_MAP.put("RF-New2_ttyS0", new RFNew2_ttyS0());
        SERIAL_MAP.put("RF-New2_ttyS2", new RFNew2_ttyS2());
        SERIAL_MAP.put(TTYS0, new SerialPort_ttyS0());
        SERIAL_MAP.put(TTYS2, new SerialPort_ttyS2());
        SERIAL_MAP.put("DEFAULT", new uis7862s_1h10_Natv_ttyS0());
    }

    private SerialDeviceFactory() {

    }

    public ISerialDeviceStrategy getSerialByName(String name) {
        if (DEBUG) Log.d(TAG, "getSerialByName: " + Build.MODEL + SPLIT + name);
        ISerialDeviceStrategy mSerial = SERIAL_MAP.get(Build.MODEL + SPLIT + name);
        if  (null == mSerial) {
            if (DEBUG) Log.d(TAG, "getSerialByName(by real name): " + SERIAL_MAP.get(name));
            mSerial = SERIAL_MAP.get(name);
        }
        if (null == mSerial) {
            mSerial = SERIAL_MAP.get(DEFAULT_SERIAL);
        }
        return mSerial;
    }

    public ISerialDeviceStrategy getNewSerial(final String path, final int serialMask) {
        return new SerialDevice(path, serialMask);
    }

    public static SerialDeviceFactory getInstance() {
        return HOLDER.BEAN;
    }

    private static final class HOLDER {
        private static final SerialDeviceFactory BEAN = new SerialDeviceFactory();
    }
}

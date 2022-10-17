package com.easy.tech.serialutil.serialdevices;
import com.easy.tech.serialutil.configs.SerialConfig;

public class UA570HP_ttyS0 implements ISerialDeviceStrategy {
    private final String path = "/dev/ttyS0";
    private final int serialMask = SerialConfig.SERIAL_MASK_F9P;

    @Override
    public final String getPath() {
        return path;
    }

    @Override
    public final int getSerialMask() {
        return serialMask;
    }
}

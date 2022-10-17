package com.easy.tech.serialutil.serialdevices;

import com.easy.tech.serialutil.configs.SerialConfig;

public class AlphaB_ttyS2 implements ISerialDeviceStrategy {
    private final String path = "/dev/ttyS2";
    private final int serialMask = SerialConfig.SERIAL_MASK_UART_S2;

    @Override
    public final String getPath() {
        return path;
    }

    @Override
    public final int getSerialMask() {
        return serialMask;
    }
}

package com.easy.tech.serialutil.serialdevices;

public class SerialDevice implements ISerialDeviceStrategy {
    private final String path;
    private final int serialMask;

    public SerialDevice(String path, int serialMask) {
        this.path = path;
        this.serialMask = serialMask;
    }

    @Override
    public final String getPath() {
        return path;
    }

    @Override
    public final int getSerialMask() {
        return serialMask;
    }
}

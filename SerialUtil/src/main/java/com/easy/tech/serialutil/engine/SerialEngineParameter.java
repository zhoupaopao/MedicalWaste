package com.easy.tech.serialutil.engine;

import androidx.annotation.NonNull;

import com.easy.tech.serialutil.configs.SerialConfig;

public class SerialEngineParameter {
    private int baudRate = SerialConfig.DEFAULT_BAUDRATE;
    private boolean writeOnly = false;
    private int bits = SerialConfig.DEFAULT_BITS;
    private int checkFlag = SerialConfig.DEFAULT_CHECK_FLAG;
    private int stopBitCount = SerialConfig.DEFAULT_STOP_BIT_COUNT;
    private int powerFlag = SerialConfig.DEFAULT_POWER_FLAG;

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public boolean isWriteOnly() {
        return writeOnly;
    }

    public void setWriteOnly(boolean writeOnly) {
        this.writeOnly = writeOnly;
    }

    public int getBits() {
        return bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
    }

    public int getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(int checkFlag) {
        this.checkFlag = checkFlag;
    }

    public int getStopBitCount() {
        return stopBitCount;
    }

    public void setStopBitCount(int stopBitCount) {
        this.stopBitCount = stopBitCount;
    }

    public int getPowerFlag() {
        return powerFlag;
    }

    public void setPowerFlag(int powerFlag) {
        this.powerFlag = powerFlag;
    }

    @NonNull
    @Override
    protected Object clone() {
        SerialEngineParameter parameter = new SerialEngineParameter();
        parameter.setBaudRate(getBaudRate());
        parameter.setBits(getBits());
        parameter.setCheckFlag(getCheckFlag());
        parameter.setStopBitCount(getStopBitCount());
        parameter.setWriteOnly(isWriteOnly());
        parameter.setPowerFlag(getPowerFlag());
        return parameter;
    }
}

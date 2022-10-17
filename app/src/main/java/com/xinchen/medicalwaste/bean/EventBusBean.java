package com.xinchen.medicalwaste.bean;

public class EventBusBean {

    public static final int DEVICE_SCAN = 51; //设备的扫描


    private int type;
    private Object object;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public EventBusBean(int type, Object object) {
        this.type = type;
        this.object = object;
    }
}

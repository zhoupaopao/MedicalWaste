package com.easy.tech.serialutil.configs;

public class SerialConfig {
    public static final int SERIAL_DATA_BITS_5 = 5;
    public static final int SERIAL_DATA_BITS_6 = 6;
    public static final int SERIAL_DATA_BITS_7 = 7;
    public static final int SERIAL_DATA_BITS_8 = 8;
    public static final int DEFAULT_BITS = SERIAL_DATA_BITS_8; //5,6,7,8
    public static final int SERIAL_CHECK_FLAG_NO = 0;
    public static final int SERIAL_CHECK_FLAG_ODD = 1;
    public static final int SERIAL_CHECK_FLAG_EVEN = 2;
    public static final int DEFAULT_CHECK_FLAG = SERIAL_CHECK_FLAG_NO; //0: no check; 1: odd check; 2: even check
    public static final int SERIAL_STOP_BITS_1 = 1;
    public static final int SERIAL_STOP_BITS_2 = 2;
    public static final int DEFAULT_STOP_BIT_COUNT = SERIAL_STOP_BITS_1; // 1; 2
    public static final int DEFAULT_BAUDRATE = 115200;
    public static final int SERIAL_MASK_F9P = 0;
    public static final int SERIAL_MASK_UART_S0 = 1;
    public static final int SERIAL_MASK_UART_S2 = 4;
    public static final int DEFAULT_SERIAL_MASK = SERIAL_MASK_F9P; //0: F9P ttyS0; 1: Serial1 ttyS0; 2: Serial2 ttyS2;
    public static final int FLAG_POWER_OFF = 0x00;
    public static final int FLAG_POWER_ON = 0x01;
    public static final int FLAG_MASK_POWER = 0xF1;
    public static final int DEFAULT_POWER_FLAG = FLAG_POWER_OFF;
}

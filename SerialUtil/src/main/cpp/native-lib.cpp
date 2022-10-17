#include <jni.h>

#include <android/log.h>
#include <fcntl.h>

#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>
#include <assert.h>
#include <errno.h>
#include <jni.h>
#include <jni.h>
#include <jni.h>
#include <jni.h>

#define LOG_TAG  "jni_log"
#define DEBUG_ENABLE 1
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##args)

#ifdef TIOCSERGSTRUCT
#define PWON_CTRL  TIOCSERGSTRUCT
#else
#define PWON_CTRL  0x5458
#endif

static speed_t getBaudrate(jint baudrate) {
    switch (baudrate) {
        case 0:
            return B0;
        case 50:
            return B50;
        case 75:
            return B75;
        case 110:
            return B110;
        case 134:
            return B134;
        case 150:
            return B150;
        case 200:
            return B200;
        case 300:
            return B300;
        case 600:
            return B600;
        case 1200:
            return B1200;
        case 1800:
            return B1800;
        case 2400:
            return B2400;
        case 4800:
            return B4800;
        case 9600:
            return B9600;
        case 19200:
            return B19200;
        case 38400:
            return B38400;
        case 57600:
            return B57600;
        case 115200:
        default:
            return B115200;
        case 230400:
            return B230400;
        case 460800:
            return B460800;
        case 500000:
            return B500000;
        case 576000:
            return B576000;
        case 921600:
            return B921600;
        case 1000000:
            return B1000000;
        case 1152000:
            return B1152000;
        case 1500000:
            return B1500000;
        case 2000000:
            return B2000000;
        case 2500000:
            return B2500000;
        case 3000000:
            return B3000000;
        case 3500000:
            return B3500000;
        case 4000000:
            return B4000000;
    }
}

static int set_baud_rate(int fd, int baudrate) {
    struct termios cfg;
    tcflush(fd, TCIOFLUSH);
    if (tcgetattr(fd, &cfg)) {
        LOGE("jni--set_baud_rate error tcgetattr failed!");
        close(fd);
        return -1;
    }
    //默认是B115200
    if (cfsetispeed(&cfg, getBaudrate(baudrate))) {
        LOGE("--JNI set input baudrate failed!");
        close(fd);
    }
    if (cfsetospeed(&cfg, getBaudrate(baudrate))) {
        LOGE("--JNI set output baudrate failed!");
        close(fd);
    }
    if (tcsetattr(fd, TCSANOW, &cfg)) {
        LOGE("--JNI tcsetattr failed!");
        close(fd);
        return -1;
    }
    return 1;
}

static int set_opt(int fd, int bits, int event, int stop) {
    struct termios cfg;
    tcflush(fd, TCIOFLUSH);
    if (tcgetattr(fd, &cfg)) {
        LOGE("jni--set-opt error tcgetattr failed!");
        close(fd);
        return -1;
    }
    cfg.c_iflag &= ~(IGNBRK | BRKINT | PARMRK | ISTRIP | INLCR | IGNCR | ICRNL | IXON);
    cfg.c_oflag &= ~OPOST;
    cfg.c_lflag &= ~(ECHO | ECHONL | ICANON | ISIG | IEXTEN);
    cfg.c_cflag &= ~CSIZE;
    //数据位
    switch (bits) {
        case 5:
            cfg.c_cflag |= CS5;
            break;
        case 6:
            cfg.c_cflag |= CS6;
            break;
        case 7:
            cfg.c_cflag |= CS7;
            break;
        case 8:
            cfg.c_cflag |= CS8;
            break;
        default:
            break;
    }
    //校验位
    switch (event) {
        case 1:                     //奇校验
            cfg.c_cflag |= PARENB;
            cfg.c_cflag |= PARODD;
            cfg.c_iflag |= (INPCK | ISTRIP);
            break;
        case 2:                     //偶校验
            cfg.c_iflag |= (INPCK | ISTRIP);
            cfg.c_cflag |= PARENB;
            cfg.c_cflag &= ~PARODD;
            break;
        case 0:                    //无校验
            cfg.c_cflag &= ~PARENB;
            break;
        default:
            break;
    }
    //停止位  原来的是1位停止位
    if (stop == 1) {
        cfg.c_cflag &= ~CSTOPB;
    } else if (stop == 2) {
        cfg.c_cflag |= CSTOPB;
    }

    cfg.c_cflag &= ~CRTSCTS;//no flow control
    tcsetattr(fd, TCSANOW, &cfg);
    tcflush(fd, TCIOFLUSH);

    return 1;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_easy_tech_serialutil_engine_NativeSerialEngine_open(JNIEnv *env, jclass clazz,
                                                             jstring path,
                                                             jint baud, jboolean write_only,
                                                             jint bits,
                                                             jint check_flag, jint stop) {
    const char *chr_path = (const char *) env->GetStringUTFChars(path, JNI_FALSE);
    int fd = -1;
    int openMode = (write_only ? O_WRONLY : O_RDWR) | O_NOCTTY | 0;

    fd = open(chr_path, openMode);
    if (DEBUG_ENABLE) LOGD("jni--open serial path:%s,return fd:%d", chr_path, fd);
    set_baud_rate(fd, baud);
    set_opt(fd, bits, check_flag, stop);
    env->ReleaseStringUTFChars(path, chr_path);
    return fd;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_easy_tech_serialutil_engine_NativeSerialEngine_setPower(JNIEnv *env, jclass clazz, jint fd,
                                                                 jint flag) {
    // TODO: implement setPower()
    ioctl(fd, PWON_CTRL, flag);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_easy_tech_serialutil_engine_NativeSerialEngine_switchBaudRate(JNIEnv *env, jclass clazz,
                                                                       jint fd,
                                                                       jint baud) {
    return set_baud_rate(fd, baud) == 1;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_easy_tech_serialutil_engine_NativeSerialEngine_read(JNIEnv *env, jclass clazz, jint fd,
                                                             jbyteArray read_buffer, jint size) {
    unsigned char *buf_char = (unsigned char *) (env->GetByteArrayElements(read_buffer, NULL));
    if (DEBUG_ENABLE) LOGD("---JNI---> readDev() fd:%d   size = %d\n", fd, size);

    int ret = 0;
    int length = 0;
    fd_set rd;
    struct timeval tv = {0, 200};

    FD_ZERO(&rd);
    FD_SET(fd, &rd);
    memset(buf_char, 0, size);
    ret = select(fd + 1, &rd, NULL, NULL, &tv);
    if (ret > 0 && FD_ISSET(fd, &rd)) {
        length = read(fd, buf_char, size);
    } else if (ret == 0) {
        LOGE("---JNI---> readDev()  time out!\n");
        //break;
    }

    if (DEBUG_ENABLE)
        LOGD("read_buffer(%d):\n%s", length, buf_char);
    env->ReleaseByteArrayElements(read_buffer, reinterpret_cast<jbyte *>(buf_char), 0);
    env->DeleteLocalRef(read_buffer);

    return length;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_easy_tech_serialutil_engine_NativeSerialEngine_write(JNIEnv *env, jclass clazz, jint fd,
                                                              jbyteArray write_buffer, jint size) {
    jbyte *cbuf = env->GetByteArrayElements(write_buffer, JNI_FALSE);
    int res = 0;
    LOGD("write_buffer after(%d):\n%s", size, cbuf);
    res = write(fd, cbuf, size);
    LOGD("write_buffer before(%d):\n%s", res, cbuf);
    env->ReleaseByteArrayElements(write_buffer, cbuf, 0);
    env->DeleteLocalRef(write_buffer);

    return res;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_easy_tech_serialutil_engine_NativeSerialEngine_close(JNIEnv *env, jclass clazz, jint fd) {
    if (DEBUG_ENABLE) LOGD("jni--close fd:%d\n", fd);
    if (fd < 0) {
        LOGE("fd < 0 ,close error fd:%d \n", fd);
    } else {
        close(fd);
    }
}
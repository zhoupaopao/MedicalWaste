package com.easy.tech.serialutil.engine;

import android.location.LocationManager;
import android.os.UserHandle;
import android.util.Log;

import java.io.FileDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SerialFileDescriptor {
    public int mfd;
    public FileDescriptor mFDescriptor;

    public void setFd(int fd) {
        mfd = fd;
        mFDescriptor = new FileDescriptor();
        try {
            Method method = FileDescriptor.class.getDeclaredMethod(
                    "setInt$",
                    int.class);
            method.invoke(mFDescriptor, fd);
        } catch (Exception e) {
            Log.d("SerialFileDescriptor:setFd", e.toString());
            return;
        }
        Log.d("SerialFileDescriptor", "setFd:fd=" + fd + ",mFDescriptor.fd = " + getFd());
    }

    public int getFd() {
        try {
            Method method = FileDescriptor.class.getDeclaredMethod(
                    "getInt$");
            return (int) method.invoke(mFDescriptor);
        } catch (Exception e) {
            Log.d("SerialFileDescriptor:getFd", e.toString());
        }
        return -1;
    }

    public SerialFileDescriptor(int fd, FileDescriptor fileDescriptor) {
        mfd = fd;
        mFDescriptor = fileDescriptor;
    }

    public SerialFileDescriptor(int fd) {
        mFDescriptor = new FileDescriptor();
        setFd(fd);
    }
}

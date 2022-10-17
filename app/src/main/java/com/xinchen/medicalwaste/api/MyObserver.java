package com.xinchen.medicalwaste.api;

import android.util.Log;

import com.arch.demo.network_api.errorhandler.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *
 * @author pqc
 */
public abstract class MyObserver<T> implements Observer<T> {
    private String TAG = getClass().getSimpleName();
    protected Disposable disposable;
    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }
    @Override
    public void onComplete() {
    }
    @Override
    public void onNext(T value) {
        onHandleSuccess(value);
    }
    @Override
    public void onError(Throwable e) {
//        Log.e("pqc", e.getMessage());
        // todo error somthing
        disposable.dispose();
        if(e instanceof ExceptionHandle.ResponeThrowable){
            onError((ExceptionHandle.ResponeThrowable)e);
        } else {
            onError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }


    protected abstract void onHandleSuccess(T t);


    protected void onHandleSuccess(T t, String msg) {

    }

    public abstract void onError(ExceptionHandle.ResponeThrowable e);

    public interface LogoutCallBack{
        void logout(String msg);
    }
}

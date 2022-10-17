package com.arch.demo.network_api.observer;

import android.util.Log;

import com.arch.demo.core.utils.EmptyUtil;
import com.arch.demo.core.utils.ToastUtil;
import com.arch.demo.network_api.beans.BaseResponse;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *
 * @author pqc
 */
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private String TAG = getClass().getSimpleName();
    private static LogoutCallBack logoutCallBack;
    protected Disposable disposable;

    public static void setLogoutCallBack(LogoutCallBack logoutCallBack){
        BaseObserver.logoutCallBack=logoutCallBack;
    }
    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }
    @Override
    public void onComplete() {
    }
    @Override
    public void onNext(BaseResponse<T> value) {
        if (value.isSuccess()) {
            if (value.hasData()) {
                T t = value.getData();
                onHandleSuccess(t);
            } else {
                T t = value.getData();
                onHandleSuccess(t);
//                onHandleSuccess(value.getData(),value.getMsg());
            }
        }else if (value.getErr().equals("403")){
            // 退出登录回调
            if (logoutCallBack==null){
                ExceptionHandle.ResponeThrowable exception=new ExceptionHandle.ResponeThrowable(new RuntimeException(), ExceptionHandle.ERROR.UNKNOWN);
                exception.message="退出登录回调未初始化";
                onError(exception);
            }else {
                logoutCallBack.logout(value.getMsg());
            }
        }else {
            ExceptionHandle.ResponeThrowable exception=new ExceptionHandle.ResponeThrowable(new RuntimeException(), ExceptionHandle.ERROR.UNKNOWN);
            exception.message=value.getMsg();
            onError(exception);
        }
    }
    @Override
    public void onError(Throwable e) {
        Log.e("pqc", e.getMessage());
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

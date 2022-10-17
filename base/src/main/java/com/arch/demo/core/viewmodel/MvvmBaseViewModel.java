package com.arch.demo.core.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

import com.arch.demo.core.model.SuperBaseModel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


/**
 * @author Administrator
 */
public class MvvmBaseViewModel<V> extends ViewModel implements IMvvmBaseViewModel<V> {
    private Reference<V> mUIRef;
//    protected M model;

    @Override
    public void attachUI(V ui) {
        mUIRef = new WeakReference<>(ui);
    }

    @Override
    @Nullable
    public V getPageView() {
        if (mUIRef == null) {
            return null;
        }
        return mUIRef.get();
    }

    @Override
    public boolean isUIAttached() {
        return mUIRef != null && mUIRef.get() != null;
    }

    @Override
    public void detachUI() {
        if (mUIRef != null) {
            mUIRef.clear();
            mUIRef = null;

        }
//        if(model != null) {
//            model.cancel();
//        }
    }
}

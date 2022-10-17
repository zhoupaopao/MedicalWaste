package com.xinchen.medicalwaste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.utils.ScanGunKeyEventHelper;
import com.arch.demo.core.utils.SharedPrefUtil;
import com.arch.demo.core.utils.ToastUtil;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;
import com.arch.demo.network_api.observer.BaseObserver;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.api.Api;
import com.xinchen.medicalwaste.api.MyObserver;
import com.xinchen.medicalwaste.app.MyApp;
import com.xinchen.medicalwaste.bean.EventBusBean;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.bean.WasteSourceBean;
import com.xinchen.medicalwaste.databinding.ActivityScanDepartmentsBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ScanDepartmentsActivity extends MvvmActivity<ActivityScanDepartmentsBinding, MvvmBaseViewModel> {
//    private String number="10001662022042900002";
    private String number="";
    private LabelBean labelBean;
    public static void launch(Context context) {
        Intent intent=new Intent(context,ScanDepartmentsActivity.class);
        context.startActivity(intent);
    }
    @Subscribe
    public void onEventMainThread(EventBusBean bean) {
        if (bean != null && EventBusBean.DEVICE_SCAN == bean.getType()) {
            number = (String) bean.getObject();
            labels(number);
        }
    }

    protected ScanGunKeyEventHelper mScanGunKeyEventHelper = new ScanGunKeyEventHelper(new ScanGunKeyEventHelper.OnScanSuccessListener() {
        @Override
        public void onScanSuccess(String barcode) {
            number = barcode;
            labels(number);
        }
    });
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //回到首页
            return super.onKeyDown(keyCode, event);
        } else if (mScanGunKeyEventHelper.isScanGunEvent(event)) {
            mScanGunKeyEventHelper.analysisKeyEvent(event);
            Log.i("performScanSuccess", event.getKeyCode()+"");
            return true;
        }

//        return false;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    @Override
    protected void initData() {
        MyApp.getSound().playShortResource("请扫科室二维码");
    }
    public void labels(String number){
        showLoadingDialog("加载中");
        Api.getInstance().labels(number, new MyObserver<LabelBean>() {
            @Override
            protected void onHandleSuccess(LabelBean labelBean) {
                hideLoadingDialog();
                try {
                    if (labelBean.getType().equals("thing") && labelBean.getObject().equals("department")) {
                        ScanDepartmentsActivity.this.labelBean=labelBean;
                        //扫到的是部门
//                        sourceNum(labelBean.getData().getNumber());
                        viewDataBinding.tvMsg.setText(labelBean.getData().getName());
                        SharedPrefUtil.putObjectT("department",labelBean);

                    }else {
                        ToastUtil.show("请扫描正确的二维码");
                    }


                } catch (Exception e) {

                }
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideLoadingDialog();
                ToastUtil.show("标签错误");
            }
        });
    }

    private void sourceNum(String number) {
        Api.getInstance().baseWasteSourcesNum(number,new MyObserver<WasteSourceBean>(){

            @Override
            protected void onHandleSuccess(WasteSourceBean wasteSourceBean) {
                ToastUtil.show(".........");
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                ToastUtil.show("请求错误");
            }
        });
    }


    @Override
    protected void initListener() {
        viewDataBinding.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewDataBinding.etScan.getVisibility()==View.GONE){
                    return;
                }
                number=viewDataBinding.etScan.getText().toString().trim();
                labels(number);
            }
        });

        viewDataBinding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (labelBean==null){
                    ToastUtil.show("请先扫描科室");
                    return;
                }
                CollectTypeActivity.launch(ScanDepartmentsActivity.this);
            }
        });

        viewDataBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected MvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_departments;
    }
}
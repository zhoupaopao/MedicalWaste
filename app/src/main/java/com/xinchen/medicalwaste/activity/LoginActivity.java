package com.xinchen.medicalwaste.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.model.UserBean;
import com.arch.demo.core.utils.ScanGunKeyEventHelper;
import com.arch.demo.core.utils.SharedPrefUtil;
import com.arch.demo.core.utils.ToastUtil;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;
import com.arch.demo.network_api.observer.BaseObserver;
import com.google.gson.Gson;
import com.hnj.dp_nusblist.USBFactory;
import com.xinchen.medicalwaste.MainActivity;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.api.Api;
import com.arch.demo.core.model.TokenBean;
import com.xinchen.medicalwaste.api.MyObserver;
import com.xinchen.medicalwaste.app.MyApp;
import com.xinchen.medicalwaste.bean.EventBusBean;
import com.xinchen.medicalwaste.bean.PlatformLoginBean;
import com.xinchen.medicalwaste.databinding.ActivityLoginBinding;
import com.xinchen.medicalwaste.utils.ScaleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Set;

import cn.wandersnail.bluetooth.BTManager;
import cn.wandersnail.bluetooth.ConnectCallback;
import cn.wandersnail.bluetooth.Connection;
import cn.wandersnail.bluetooth.EventObserver;

import static com.arch.demo.core.utils.SharedPrefUtil.EmployeeLabel;

public class LoginActivity extends MvvmActivity<ActivityLoginBinding, MvvmBaseViewModel>{

    private CountDownTimer timer;
    private UserBean nowUserBean;
    private PlatformLoginBean nowPlatformLoginBean;

    //    private String scanLabelNumber="10001662020060500113";//扫描到的标签号
//    private String scanLabelNumber="10001662020120200031";//扫描到的标签号
//    private String scanLabelNumber="10001662020060300022";//扫描到的标签号
    private String scanLabelNumber = "10001662020110500001";//扫描到的标签号

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    @Subscribe
    public void onEventMainThread(EventBusBean bean) {
        if (bean != null && EventBusBean.DEVICE_SCAN == bean.getType()) {
            scanLabelNumber = (String) bean.getObject();
            getToken();
        }
    }
    protected ScanGunKeyEventHelper mScanGunKeyEventHelper = new ScanGunKeyEventHelper(new ScanGunKeyEventHelper.OnScanSuccessListener() {
        @Override
        public void onScanSuccess(String barcode) {
            scanLabelNumber = barcode;
            getToken();
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


//        Api.getInstance().loginScan("10001662020060500113", new MyObserver<String>() {
//            @Override
//            protected void onHandleSuccess(String myObserver) {
//
//            }
//
//            @Override
//            public void onError(ExceptionHandle.ResponeThrowable e) {
//
//            }
//        });

//        if (!SharedPrefUtil.getString(SharedPrefUtil.EmployeeLabel).equals("")) {
//            MainActivity.launch(this);
//            finish();
//        }

        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewDataBinding.tvAgain.setText("即将重新登录验证" + (millisUntilFinished / 1000 + 1));
            }

            @Override
            public void onFinish() {
                viewDataBinding.viewError.setVisibility(View.GONE);
                viewDataBinding.viewLogin.setVisibility(View.VISIBLE);
            }
        };
        viewDataBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewDataBinding.etScan.getVisibility()!=View.GONE) {
                    scanLabelNumber = viewDataBinding.etScan.getText().toString().trim();
                    getToken();
                }
            }
        });
    }


    private void getToken() {
        showLoadingDialog("加载中...");
        Api.getInstance().tokenByNumber(scanLabelNumber, new MyObserver<TokenBean>() {
            @Override
            protected void onHandleSuccess(TokenBean tokenBean) {

                SharedPrefUtil.putObjectT(SharedPrefUtil.Token, tokenBean);
                accountsme();
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideLoadingDialog();
                ToastUtil.show("请求失败");
            }
        });
    }

    private void accountsme() {
        Api.getInstance().accountMe(new MyObserver<UserBean>() {
            @Override
            protected void onHandleSuccess(UserBean userBean) {
                List<UserBean.TenantsBean> tempTenants = userBean.getTenants();
                if (tempTenants != null && tempTenants.size() > 0) {
                    UserBean.TenantsBean trueTenant = null;
                    ps:
                    for (UserBean.TenantsBean tempTenant : tempTenants) {
                        if (tempTenant.isPrimary()) {
                            trueTenant = tempTenant;
                            break ps;
                        }
                    }

                    if (trueTenant != null) {
                        //获取登录权限，必须是收集人员
                        nowUserBean = userBean;
                        platformLogin(trueTenant.getOpenid());
                    } else {
                        ToastUtil.show("当前用户没有权限");
                        hideLoadingDialog();
                    }
                } else {
                    hideLoadingDialog();
                    ToastUtil.show("用户信息不正确");
                }

            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                ToastUtil.show("请求错误");
                hideLoadingDialog();
            }
        });
    }

    private void platformLogin(String openid) {

        Api.getInstance().platformLogin(openid, new MyObserver<PlatformLoginBean>() {
            @Override
            protected void onHandleSuccess(PlatformLoginBean platformLoginBean) {
                hideLoadingDialog();
                List<PlatformLoginBean.RolesBean> rolesBeans = platformLoginBean.getRoles();
                boolean isCollect = false;
                boolean isNurse = false;
                if (rolesBeans != null && rolesBeans.size() > 0) {
                    ps:
                    for (PlatformLoginBean.RolesBean rb : rolesBeans) {
                        if (rb.getType().equals("WASTE_COLLECTOR")) {
                            isCollect = true;
//                                    break ps;
                        }
                    }
                }
                if (isCollect) {//收集人员
                    nowPlatformLoginBean = platformLoginBean;
                    //登录成功了，需要去获取标签信息
//                    labels(scanLabelNumber);

//                    sendMessage("toHomeMain");
                    saveMsg();
                    MainActivity.launch(LoginActivity.this);

                } else {
                    ToastUtil.show("角色认证不通过");
                }
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideLoadingDialog();
                ToastUtil.show("请求错误");
            }
        });

    }

    private void saveMsg() {
        SharedPrefUtil.putUserBean(nowUserBean);

        SharedPrefUtil.putRoleType(new Gson().toJson(nowPlatformLoginBean.getRoles()));
        SharedPrefUtil.putString("organization", nowPlatformLoginBean.getOrganization().getName());
        SharedPrefUtil.putString("idCard", nowPlatformLoginBean.getEmployee().getIdCard());
        SharedPrefUtil.putString("EmployeeIdCard", nowPlatformLoginBean.getEmployee().getIdCard());
        SharedPrefUtil.putString("rolesId", nowPlatformLoginBean.getRoles().get(0).getId() + "");

        //把扫描到的工牌号也记录下来
        SharedPrefUtil.putString(SharedPrefUtil.EmployeeLabel, scanLabelNumber);
        MyApp.getSound().playShortResource("登录成功");
    }

    @Override
    protected void initListener() {

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
        return R.layout.activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
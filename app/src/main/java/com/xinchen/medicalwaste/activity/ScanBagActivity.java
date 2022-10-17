package com.xinchen.medicalwaste.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.utils.ScanGunKeyEventHelper;
import com.arch.demo.core.utils.SharedPrefUtil;
import com.arch.demo.core.utils.ToastUtil;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;
import com.google.gson.JsonObject;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.adapter.LabelListAdapter;
import com.xinchen.medicalwaste.api.Api;
import com.xinchen.medicalwaste.api.MyObserver;
import com.xinchen.medicalwaste.app.MyApp;
import com.xinchen.medicalwaste.bean.EventBusBean;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.bean.LableListBean;
import com.xinchen.medicalwaste.bean.WareHouseBean;
import com.xinchen.medicalwaste.bean.WasteSourceBean;
import com.xinchen.medicalwaste.databinding.ActivityScanBagBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ScanBagActivity extends MvvmActivity<ActivityScanBagBinding, MvvmBaseViewModel> {
    //    private String lable="10000032019100900027";
    private String lable = "";
    private LabelListAdapter adapter;
    private List<LabelBean> list;
    LableListBean lableListBean;
    WareHouseBean wareHouse;

    public static void launch(Context context) {
        Intent intent = new Intent(context, ScanBagActivity.class);
        context.startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(EventBusBean bean) {
        if (bean != null && EventBusBean.DEVICE_SCAN == bean.getType()) {
            lable = (String) bean.getObject();
            scan();
        }
    }
    protected ScanGunKeyEventHelper mScanGunKeyEventHelper = new ScanGunKeyEventHelper(new ScanGunKeyEventHelper.OnScanSuccessListener() {
        @Override
        public void onScanSuccess(String barcode) {
            lable = barcode;
            scan();
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
        MyApp.getSound().playShortResource("请扫描医废袋");
        list = new ArrayList<>();
        adapter = new LabelListAdapter(list);
        viewDataBinding.rvList.setAdapter(adapter);
        viewDataBinding.rvList.setLayoutManager(new LinearLayoutManager(this));


        viewDataBinding.cbWeight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    viewDataBinding.btNext.setText("复合称重");
                } else {
                    viewDataBinding.btNext.setText("收集完成");
                }
            }
        });
    }
    private void scan(){
        showLoadingDialog("加载中。。。");
        Api.getInstance().getBagList(lable, new MyObserver<LableListBean>() {
            @Override
            protected void onHandleSuccess(LableListBean wasteSourceBean) {
                getCangku();

//                ToastUtil.show("个数" + wasteSourceBean.getTotal());
                list.clear();
                for(LabelBean labelBean:wasteSourceBean.getList()){
                   LabelBean depart= SharedPrefUtil.getObjectT("department");
                    if(labelBean.getData().getDepartment().equals(depart.getData().getName())){
                        lableListBean = wasteSourceBean;
                        list.addAll(wasteSourceBean.getList());
                        adapter.notifyDataSetChanged();
                        break;
                    }else{
                        break;
                    }
                }

            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideLoadingDialog();
                ToastUtil.show(e.message);
            }
        });
    }

    @Override
    protected void initListener() {
        viewDataBinding.llScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewDataBinding.etScan.getVisibility()==View.GONE){
                    return;
                }
                lable=viewDataBinding.etScan.getText().toString().trim();
                scan();
            }
        });
        viewDataBinding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewDataBinding.cbWeight.isChecked()) {
                    LableListBean beanList=new LableListBean();
                    List<LabelBean> list=new ArrayList<>();

                    if (lableListBean!=null&&lableListBean.getList()!=null&&lableListBean.getList().size()>0){
                        for (int i = 0; i <lableListBean.getList().size() ; i++) {
                            if (lableListBean.getList().get(i).isSelect()){
                                list.add(lableListBean.getList().get(i));
                            }
                        }
                        beanList.setList(list);
                        beanList.setTotal(list.size());
                        if (list.size()>0){
                            WeighActivity.launch(ScanBagActivity.this,beanList);
                        }else {
                            ToastUtil.show("请选择标签");
                        }


                    }else {
                        ToastUtil.show("请扫描医废袋");
                    }
                } else {
                    collect();
                }

            }
        });
        viewDataBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getCangku() {
        Api.getInstance().warehouses(new MyObserver<WareHouseBean>() {
            @Override
            protected void onHandleSuccess(WareHouseBean wareHouseBean) {
                hideLoadingDialog();
                wareHouse = wareHouseBean;
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideLoadingDialog();
                ToastUtil.show("仓库获取失败，请返回重试");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode== Activity.RESULT_OK){
            finish();
        }
    }

    /***收集完成**/
    private void collect() {
        if (wareHouse == null || wareHouse.getList() == null || wareHouse.getList().size() <= 0) {
            ToastUtil.show("仓库获取失败");
            return;
        }
        LableListBean beanList=new LableListBean();
        List<LabelBean> list=new ArrayList<>();
        if (lableListBean!=null&&lableListBean.getList()!=null&&lableListBean.getList().size()>0) {
            for (int i = 0; i < lableListBean.getList().size(); i++) {
                if (lableListBean.getList().get(i).isSelect()) {
                    list.add(lableListBean.getList().get(i));
                }
            }
        }else{
            ToastUtil.show("请扫描医废袋");
            return;
        }
        beanList.setList(list);
        beanList.setTotal(list.size());
        if (list.size()>0){
            Api.getInstance().collectRecords(wareHouse, beanList, new MyObserver<JsonObject>() {
                @Override
                protected void onHandleSuccess(JsonObject jsonObject) {
                    ToastUtil.show(jsonObject.get("status").toString());
                    if (jsonObject.get("status").getAsInt() == 201) {
                        ToastUtil.show("收集成功");
                        MyApp.getSound().playShortResource("收集完成");
                        finish();
                    }
                }

                @Override
                public void onError(ExceptionHandle.ResponeThrowable e) {
                    ToastUtil.show("收集失败");
                }
            });
        }else {
            ToastUtil.show("请选择标签");
        }

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
        return R.layout.activity_scan_bag;
    }
}
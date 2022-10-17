package com.xinchen.medicalwaste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.utils.ToastUtil;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.arch.demo.network_api.beans.BaseListResponse;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.api.Api;
import com.xinchen.medicalwaste.api.MyObserver;
import com.xinchen.medicalwaste.app.MyApp;
import com.xinchen.medicalwaste.bean.WasteInventoryBean;
import com.xinchen.medicalwaste.databinding.ActivityWasteTypeBinding;

import java.util.ArrayList;

public class WasteTypeActivity extends MvvmActivity<ActivityWasteTypeBinding, MvvmBaseViewModel> implements View.OnClickListener {
    private ArrayList<WasteInventoryBean>allList=new ArrayList<>();//废物列表

    public static void launch(Context context) {
        Intent intent=new Intent(context,WasteTypeActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void initData() {
        MyApp.getSound().playShortResource("请选择医废类型");
        showLoadingDialog("加载中...");
        Api.getInstance().wasteProduceRecord("", new MyObserver<BaseListResponse<ArrayList<WasteInventoryBean>>>() {
            @Override
            protected void onHandleSuccess(BaseListResponse<ArrayList<WasteInventoryBean>> data) {
                hideLoadingDialog();
                allList.addAll(data.getList());

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
        viewDataBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        viewDataBinding.ivInfect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ScanBagActivity.launch(WasteTypeActivity.this);
//                WeighActivity.launch(WasteTypeActivity.this,null);
//            }
//        });
//        viewDataBinding.ivPrint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ScanBagActivity.launch(WasteTypeActivity.this);
//            }
//        });
        viewDataBinding.iv1.setOnClickListener(this);
        viewDataBinding.iv2.setOnClickListener(this);
        viewDataBinding.iv3.setOnClickListener(this);
        viewDataBinding.iv4.setOnClickListener(this);
        viewDataBinding.iv5.setOnClickListener(this);
        viewDataBinding.iv6.setOnClickListener(this);
    }
    private WasteInventoryBean nowWasteInventoryBean;//当前的危废类型
    //获取危废列表
    public void toWeight(String wasteName){

        for(WasteInventoryBean collectWasteBean:allList){
            if(collectWasteBean.getWaste().getName().equals(wasteName)){
                nowWasteInventoryBean=collectWasteBean;
//                Intent intent=new Intent(WasteTypeActivity.this, WeighActivity.class);
//                intent.putExtra("collectWasteBean",nowWasteInventoryBean);
//                startActivity(intent);
                WeighActivity.launch(this,nowWasteInventoryBean);
                break;
            }
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
        return R.layout.activity_waste_type;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_1:
                toWeight("感染性废物");
                break;
            case R.id.iv_2:
                toWeight("损伤性废物");
                break;
            case R.id.iv_3:
                toWeight("化学性废物");
                break;
            case R.id.iv_4:
                toWeight("病理性废物");
                break;
            case R.id.iv_5:
                toWeight("药物性废物");
                break;
            case R.id.iv_6:
//                toWeight("可回收性废物");
                break;
        }
    }
}
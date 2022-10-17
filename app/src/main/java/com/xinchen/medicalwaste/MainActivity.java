package com.xinchen.medicalwaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.utils.SharedPrefUtil;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.xinchen.medicalwaste.activity.CollectRecordActivity;
import com.xinchen.medicalwaste.activity.CollectTypeActivity;
import com.xinchen.medicalwaste.activity.LoginActivity;
import com.xinchen.medicalwaste.activity.PrintLabelActivity;
import com.xinchen.medicalwaste.activity.ScanDepartmentsActivity;
import com.xinchen.medicalwaste.databinding.ActivityMainBinding;

public class MainActivity extends MvvmActivity<ActivityMainBinding, MvvmBaseViewModel> implements View.OnClickListener {
    public static void launch(Context context) {
        Intent intent=new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        viewDataBinding.ivCollect.setOnClickListener(this);
        viewDataBinding.ivPrint.setOnClickListener(this);
        viewDataBinding.clExit.setOnClickListener(this);
        viewDataBinding.tvCollect.setOnClickListener(this);
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
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_collect:
//                CollectTypeActivity.launch(this);
                ScanDepartmentsActivity.launch(this);
                break;
            case R.id.iv_print:
                PrintLabelActivity.launch(this);
                break;
            case R.id.cl_exit:
                SharedPrefUtil.clear();
                LoginActivity.launch(this);
                finish();
                break;
            case R.id.tv_collect:
                //查看今日收集
                CollectRecordActivity.launch(this);
                break;
        }
    }
}
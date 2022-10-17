package com.xinchen.medicalwaste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.viewmodel.IMvvmBaseViewModel;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.app.MyApp;
import com.xinchen.medicalwaste.databinding.ActivityCollectTypeBinding;

public class CollectTypeActivity extends MvvmActivity<ActivityCollectTypeBinding, MvvmBaseViewModel> {

    public static void launch(Context context) {
        Intent intent=new Intent(context,CollectTypeActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void initData() {
        MyApp.getSound().playShortResource("请选择收集类型");
    }

    @Override
    protected void initListener() {
        viewDataBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewDataBinding.ivRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanBagActivity.launch(CollectTypeActivity.this);
            }
        });
        viewDataBinding.ivNotRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WasteTypeActivity.launch(CollectTypeActivity.this);
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
        return R.layout.activity_collect_type;
    }
}
package com.xinchen.medicalwaste.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.viewmodel.IMvvmBaseViewModel;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.adapter.OnItemClickListener;
import com.xinchen.medicalwaste.adapter.PrintLabelAdapter;
import com.xinchen.medicalwaste.api.Api;
import com.xinchen.medicalwaste.api.AppConstant;
import com.xinchen.medicalwaste.api.MyObserver;
import com.xinchen.medicalwaste.app.MyApp;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.bean.LableListBean;
import com.xinchen.medicalwaste.bean.LablePrintBean;
import com.xinchen.medicalwaste.databinding.ActivityPrintLabelBinding;
import com.xinchen.medicalwaste.utils.PrintUtils;

import java.util.ArrayList;

public class PrintLabelActivity extends MvvmActivity<ActivityPrintLabelBinding, MvvmBaseViewModel> {
    public static void launch(Context context) {
        Intent intent=new Intent(context,PrintLabelActivity.class);
        context.startActivity(intent);
    }
    private int offset=0;
    private int limit=100;
    ArrayList<LabelBean>mList;
    PrintLabelAdapter adapter;
    @Override
    protected void initData() {
        MyApp.getSound().playShortResource("请选择您要补打的标签");
        mList=new ArrayList<>();
        adapter=new PrintLabelAdapter(mList);
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
//
                if (AppConstant.isBijiguang){
                    PrintUtils.print(mList.get(position));
                }else {
                    PrintUtils.print1(mList.get(position), PrintLabelActivity.this);
                }
            }
        });
        viewDataBinding.rvList.setAdapter(adapter);
        viewDataBinding.rvList.setLayoutManager(new LinearLayoutManager(this));
        getLabelList(true);
    }
    //改用入库单列表
    public void getLabelList(Boolean isRefresh){
        if(isRefresh){
            offset=0;
            mList.clear();
        }else{
            offset=offset+limit;
        }
        showLoadingDialog("加载中...");
//        Api.getInstance().getProductionOrderList(offset, limit, new BaseListObserver<ArrayList<ProductionOrderDTO>>() {
        Api.getInstance().labelMy(offset,limit, new MyObserver<LablePrintBean>() {
            @Override
            protected void onHandleSuccess(LablePrintBean labelBeans) {
                hideLoadingDialog();
//                sendMessage("success");
                mList.addAll(labelBeans.getList());
//                sendMessage("adapter");
                adapter.notifyDataSetChanged();
                if(labelBeans.getList().size()<limit){
//                    sendMessage("noMoreData");
                }
                if(isRefresh){
//                    sendMessage("finishRefresh");
                }else{
//                    sendMessage("finishLoadMore");
                }
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
//                sendMessage("fail");
                hideLoadingDialog();
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
        return R.layout.activity_print_label;
    }
}
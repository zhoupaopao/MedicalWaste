package com.xinchen.medicalwaste.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.adapter.InventoryCollectRecordAdapter;
import com.xinchen.medicalwaste.adapter.OnItemClickListener;
import com.xinchen.medicalwaste.adapter.PrintLabelAdapter;
import com.xinchen.medicalwaste.api.Api;
import com.xinchen.medicalwaste.api.AppConstant;
import com.xinchen.medicalwaste.api.MyObserver;
import com.xinchen.medicalwaste.app.MyApp;
import com.xinchen.medicalwaste.bean.InventoryItem;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.bean.LablePrintBean;
import com.xinchen.medicalwaste.databinding.ActivityCollectRecordBinding;
import com.xinchen.medicalwaste.databinding.ActivityPrintLabelBinding;
import com.xinchen.medicalwaste.utils.BigUtil;
import com.xinchen.medicalwaste.utils.PrintUtils;

import java.util.ArrayList;

/**
 * 今日收集数据
 */
public class CollectRecordActivity extends MvvmActivity<ActivityCollectRecordBinding, MvvmBaseViewModel> {
    public static void launch(Context context) {
        Intent intent=new Intent(context, CollectRecordActivity.class);
        context.startActivity(intent);
    }
    private int offset=0;
    private int limit=100;
    ArrayList<InventoryItem>mList;
    InventoryCollectRecordAdapter adapter;
    @Override
    protected void initData() {
        mList=new ArrayList<>();
        adapter=new InventoryCollectRecordAdapter(mList);
        viewDataBinding.recycleView.setAdapter(adapter);
        viewDataBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        getLabelList(true);
    }
    //改用入库单列表
    public void getLabelList(Boolean isRefresh){
        showLoadingDialog("加载中...");
//        Api.getInstance().getProductionOrderList(offset, limit, new BaseListObserver<ArrayList<ProductionOrderDTO>>() {
        Api.getInstance().getwasteRecordTodayCollection( new MyObserver<ArrayList<InventoryItem>>() {
            @Override
            protected void onHandleSuccess(ArrayList<InventoryItem> inventoryItems) {
                hideLoadingDialog();
//                sendMessage("success");
                mList.addAll(inventoryItems);
//                sendMessage("adapter");
                Double totalWeight=0.0;
                for(InventoryItem inventoryItem:mList){
                    totalWeight= BigUtil.add(inventoryItem.getTotalWeight(),totalWeight);
                }
                viewDataBinding.tvTotal.setText("今日收集总重:"+totalWeight+"KG");
                adapter.notifyDataSetChanged();
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
        return R.layout.activity_collect_record;
    }
}
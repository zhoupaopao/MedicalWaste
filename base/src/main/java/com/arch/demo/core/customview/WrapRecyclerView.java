package com.arch.demo.core.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WrapRecyclerView extends RecyclerView {
    private ArrayList<View> mHeaderViewInfos = new ArrayList<View>();
    private ArrayList<View> mFooterViewInfos = new ArrayList<View>();
    private Adapter mAdapter;

    public WrapRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public  void addHeaderView(View view){
        mHeaderViewInfos.add(view);

        if (mAdapter!=null){
            if (!(mAdapter instanceof HeaderViewRecyclerAdapter)){
                mAdapter=new HeaderViewRecyclerAdapter(mHeaderViewInfos,mFooterViewInfos,mAdapter);
            }
        }

    }
    public void addFooterView(View view){
        mFooterViewInfos.add(view);
        if (mAdapter!=null){
            if (!(mAdapter instanceof HeaderViewRecyclerAdapter)){
                mAdapter=new HeaderViewRecyclerAdapter(mHeaderViewInfos,mFooterViewInfos,mAdapter);
            }
        }
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (mHeaderViewInfos.size() > 0|| mFooterViewInfos.size() > 0) {
            mAdapter = new HeaderViewRecyclerAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
        } else {
            mAdapter = adapter;
        }
        super.setAdapter(mAdapter);
    }
}
package com.xinchen.medicalwaste.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.bean.InventoryItem;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.databinding.ItemCollectRecordBinding;
import com.xinchen.medicalwaste.databinding.ItemPrintLabelBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InventoryCollectRecordAdapter extends RecyclerView.Adapter<InventoryCollectRecordAdapter.MyHolder> {
    ArrayList<InventoryItem> mList;

    public InventoryCollectRecordAdapter(ArrayList<InventoryItem> mList) {
        this.mList = mList;
    }


    @NonNull
    @Override
    public InventoryCollectRecordAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCollectRecordBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_collect_record,parent,false);

        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryCollectRecordAdapter.MyHolder holder, int position) {
        InventoryItem bean=mList.get(position);
        holder.binding.tvWeight.setText(bean.getTotalWeight()+"");
        holder.binding.tvQuantity.setText(bean.getTotalQuantity()+"");
        holder.binding.tvWasteType.setText(bean.getName());
        if (position%2!=0){
            holder.binding.getRoot().setBackgroundColor(Color.parseColor("#441779D4"));
        }else {
            holder.binding.getRoot().setBackgroundColor(0);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        ItemCollectRecordBinding binding;
        public MyHolder(@NonNull ItemCollectRecordBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

package com.xinchen.medicalwaste.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.bean.LableListBean;
import com.xinchen.medicalwaste.databinding.ItemLabelListBinding;
import com.xinchen.medicalwaste.databinding.ItemPrintLabelBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class LabelListAdapter extends RecyclerView.Adapter<LabelListAdapter.MyHolder> {
    private List<LabelBean> list;

    public LabelListAdapter(List<LabelBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public LabelListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLabelListBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_label_list,parent,false);

        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelListAdapter.MyHolder holder, int position) {
        LabelBean bean =list.get(position);
        holder.binding.tvLabelNum.setText(bean.getNumber());
        holder.binding.tvWeight.setText(bean.getData().getWeight());
        holder.binding.tvDepartments.setText(bean.getData().getDepartment());
        holder.binding.tvType.setText(bean.getData().getName());
        holder.binding.tvName.setText(bean.getData().getCollector());
        if (position%2!=0){
            holder.binding.getRoot().setBackgroundColor(Color.parseColor("#441779D4"));
        }else {
            holder.binding.getRoot().setBackgroundColor(0);
        }
        holder.binding.cbWeight.setChecked(bean.isSelect());
        holder.binding.cbWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setSelect(!bean.isSelect());
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        ItemLabelListBinding binding;
        public MyHolder(@NonNull ItemLabelListBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

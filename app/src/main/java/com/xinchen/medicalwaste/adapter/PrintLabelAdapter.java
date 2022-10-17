package com.xinchen.medicalwaste.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.databinding.ItemPrintLabelBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PrintLabelAdapter extends RecyclerView.Adapter<PrintLabelAdapter.MyHolder> {
    ArrayList<LabelBean> mList;
    private OnItemClickListener listener;

    public PrintLabelAdapter(ArrayList<LabelBean> mList) {
        this.mList = mList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PrintLabelAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPrintLabelBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_print_label,parent,false);

        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PrintLabelAdapter.MyHolder holder, int position) {
        LabelBean bean=mList.get(position);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data=sdf.format(Long.parseLong(bean.getCreatedAt()));
        holder.binding.tvTime.setText(data);
        holder.binding.tvLabelNum.setText(bean.getNumber());
        holder.binding.tvDepartments.setText(bean.getData().getDepartment());
        holder.binding.tvCollector.setText(bean.getData().getCollector());
        holder.binding.tvType.setText(bean.getData().getName());
        holder.binding.tvWeight.setText(bean.getData().getWeight());
        holder.binding.tvPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClickListener(v,position);
                }
            }
        });
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
        ItemPrintLabelBinding binding;
        public MyHolder(@NonNull ItemPrintLabelBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

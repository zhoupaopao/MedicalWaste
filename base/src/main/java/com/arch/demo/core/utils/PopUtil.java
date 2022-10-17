package com.arch.demo.core.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.arch.demo.core.R;
import com.dou361.dialogui.DialogUIUtils;

public class PopUtil {
    //有确定按钮的提示框
    public static void showAuto(Context context,String text,OnPopAutoClickListener onPopAutoClickListener){
        int layoutId = R.layout.pop_layout_common_text;   // 布局ID
        View contentView = LayoutInflater.from(context).inflate(layoutId, null);
        TextView tv_msg = contentView.findViewById(R.id.tv_msg);
        tv_msg.setText(text);
        final Dialog dialog = DialogUIUtils.showCustomAlert(context, contentView, Gravity.CENTER, false, false).show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.color_00FFFFFF);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    onPopAutoClickListener.onCancel();
                    DialogUIUtils.dismiss(dialog);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //没有确定按钮的提示框，自动关闭



    //有确定取消的提示框
    public void showAsk(Context context,String text,OnPopClickListener onPopClickListener){
        View view = getPopupWindowContentView(context,text);
        final Dialog dialog = DialogUIUtils.showCustomAlert(context, view, Gravity.CENTER, true, false).show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.color_00FFFFFF);
        TextView tv_sure=view.findViewById(R.id.tv_sure);
        TextView tv_cancel=view.findViewById(R.id.tv_cancel);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(dialog);
                onPopClickListener.onSure();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(dialog);
                onPopClickListener.onCancel();
            }
        });
    }
    private View getPopupWindowContentView(Context context,String text) {
        int layoutId = R.layout.pop_layout_common_ask;   // 布局ID
        View contentView = LayoutInflater.from(context).inflate(layoutId, null);

        //设置不同颜色的文字
        TextView tv_msg = contentView.findViewById(R.id.tv_msg);
//        SpannableStringBuilder style = new SpannableStringBuilder("未检测到本次拉运过程中已到达相关处置单位，若强制结束拉运可能会带来不必要负面影响");
//        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_FF4C21)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_FF4C21)), 16, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_FF4C21)), 36, 40, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_msg.setText(text);
        return contentView;
    }
}

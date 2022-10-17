package com.arch.demo.core.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.arch.demo.core.R;
import com.arch.demo.core.utils.AppManager;


public class WidgetArrowTitle extends LinearLayout {
    private String title;
    private TextView tv_title;
    private View view;
    private ImageView iv_back;

    public WidgetArrowTitle(Context context) {
        this(context, null);
    }

    public WidgetArrowTitle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WidgetArrowTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = LayoutInflater.from(context).inflate(R.layout.widget_layout_arrow_title, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WidgetArrowTitle, defStyleAttr, 0);
        title = array.getString(R.styleable.WidgetArrowTitle_wat_title);
        array.recycle();
        initView(context);
    }

    private void initView(Context context) {
        tv_title = view.findViewById(R.id.tv_title);
        iv_back=view.findViewById(R.id.iv_back);
        tv_title.setText(title);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getActivityContext(context).finish();
            }
        });
    }
    public void setOnBackClick(OnClickListener listener){
        iv_back.setOnClickListener(listener);
    }

}

package com.arch.demo.core.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.arch.demo.core.R;
import com.arch.demo.core.utils.AppManager;
import com.arch.demo.core.utils.AppUtils;


public class WidgetTextDetail extends LinearLayout {
    private String title;
    private TextView tv_text;
    private View view;
    private ImageView iv_back;
    private int height;
    private RelativeLayout ll_all;

    public WidgetTextDetail(Context context) {
        this(context, null);
    }

    public WidgetTextDetail(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WidgetTextDetail(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = LayoutInflater.from(context).inflate(R.layout.widget_text_detail, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WidgetTextDetail, defStyleAttr, 0);
        title = array.getString(R.styleable.WidgetTextDetail_wtd_text);
        height = array.getDimensionPixelSize(R.styleable.WidgetTextDetail_wtd_height, (int) AppUtils.dp2px(context,30));
        array.recycle();
        initView(context);
    }

    private void initView(Context context) {
        tv_text = view.findViewById(R.id.tv_text);
        tv_text.setText(title);
        ll_all=view.findViewById(R.id.ll_all);
        ViewGroup.LayoutParams lp;
        lp= ll_all.getLayoutParams();
        lp.width=LayoutParams.MATCH_PARENT;;
        lp.height=height;
        ll_all.setLayoutParams(lp);
    }

}

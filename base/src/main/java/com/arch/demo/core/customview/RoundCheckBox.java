package com.arch.demo.core.customview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.arch.demo.core.R;

public class RoundCheckBox extends AppCompatCheckBox {

    public  RoundCheckBox(Context context) {
        this(context, null);
    }

    public  RoundCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public  RoundCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

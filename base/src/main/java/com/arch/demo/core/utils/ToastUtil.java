package com.arch.demo.core.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.arch.demo.core.BaseApplication;

/**
 * @author pqc
 */
public class ToastUtil {
	private static Toast mToast;

	public static void show(Context context, String msg) {
		try {
			if (context != null && !TextUtils.isEmpty(msg)) {
				if(mToast != null){
					mToast.cancel();
				}
				mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
				mToast.setText(msg);
				mToast.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void show(String msg) {
		try {
			if (BaseApplication.sApplication != null && !TextUtils.isEmpty(msg)) {
				if(mToast != null){
					mToast.cancel();
				}
				mToast = Toast.makeText(BaseApplication.sApplication, "", Toast.LENGTH_LONG);
				mToast.setText(msg);
				mToast.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.xinchen.medicalwaste.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gprinter.command.CpclCommand;
import com.gprinter.command.LabelCommand;
import com.gprinter.utils.GpUtils;
import com.gprinter.utils.PDFUtils;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.bean.LabelBean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Copyright (C), 2012-2020, 珠海佳博科技股份有限公司
 * FileName: PrintConntent
 * Author: Circle
 * Date: 2020/7/20 10:04
 * Description: 打印内容
 */
public class PrintContent {
    /**
     * 标签打印测试页
     *
     * @return
     */
    public static Vector<Byte> getLabel(Context context, LabelBean labelBean) {
        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸宽高，按照实际尺寸设置 单位mm
        tsc.addUserCommand("\r\n");
        tsc.addSize(50, 80);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0 单位mm
        tsc.addGap(2);
        //设置纸张类型为黑标，发送BLINE 指令不能同时发送GAP指令
//        tsc.addBline(2);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        // 设置原点坐标
        tsc.addReference(0, 160);
        //设置浓度
        tsc.addDensity(LabelCommand.DENSITY.DNESITY4);
        // 撕纸模式开启
        tsc.addTear(LabelCommand.RESPONSE_MODE.ON);
        // 清除打印缓冲区
        tsc.addCls();

        tsc.addText(360,150, LabelCommand.FONTTYPE.SIMPLIFIED__32_CHINESE, LabelCommand.ROTATION.ROTATION_90,LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                labelBean.getData().getName());
        tsc.addText(290,5, LabelCommand.FONTTYPE.SIMPLIFIED_24_CHINESE, LabelCommand.ROTATION.ROTATION_90,LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "医院:"+labelBean.getData().getOrganization());
        tsc.addText(230,5, LabelCommand.FONTTYPE.SIMPLIFIED_24_CHINESE, LabelCommand.ROTATION.ROTATION_90,LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "医废种类:"+labelBean.getData().getName());
        tsc.addText(230,260, LabelCommand.FONTTYPE.SIMPLIFIED_24_CHINESE, LabelCommand.ROTATION.ROTATION_90,LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "科室:"+labelBean.getData().getDepartment());
        tsc.addText(180,140, LabelCommand.FONTTYPE.SIMPLIFIED_24_CHINESE, LabelCommand.ROTATION.ROTATION_90,LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "重量(KG):"+labelBean.getData().getWeight());
        tsc.addText(180,320, LabelCommand.FONTTYPE.SIMPLIFIED_24_CHINESE, LabelCommand.ROTATION.ROTATION_90,LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "收集人:"+labelBean.getData().getCollector());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String data=sdf.format(Long.parseLong(labelBean.getCreatedAt()));
        tsc.addText(120,140, LabelCommand.FONTTYPE.SIMPLIFIED_24_CHINESE, LabelCommand.ROTATION.ROTATION_90,LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "日期:"+data);
        tsc.addText(120,320, LabelCommand.FONTTYPE.SIMPLIFIED_24_CHINESE, LabelCommand.ROTATION.ROTATION_90,LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "交接人:"+labelBean.getData().getCollector());
//        二维码
        tsc.addQRCode(170,5, LabelCommand.EEC.LEVEL_M, 6, LabelCommand.ROTATION.ROTATION_90, labelBean.getNumber());
        tsc.addText(60,140, LabelCommand.FONTTYPE.SIMPLIFIED_24_CHINESE, LabelCommand.ROTATION.ROTATION_90,LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                labelBean.getNumber());
        // 打印标签
        tsc.addPrint(1, 1);
        // 打印标签后 蜂鸣器响
        tsc.addSound(2, 100);
        //开启钱箱
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        return datas;
    }
    public static Vector<Byte> getCpclCommand(Context context,int gap) {
        CpclCommand tsc = new CpclCommand();
        tsc.addInitializePrinter();
        // 设置标签尺寸宽高，按照实际尺寸设置 单位mm
        tsc.addUserCommand("\r\n");

        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        return datas;
    }


    /**
     * mxl转bitmap图片
     * @return
     */
    public static Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static TableRow ctv(Context context, String name, int k, int n){
        TableRow tb=new TableRow(context);
        tb.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT ,TableLayout.LayoutParams.WRAP_CONTENT));
        TextView tv1=new TextView(context);
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT ,TableRow.LayoutParams.WRAP_CONTENT));
        tv1.setText(name);
        tv1.setTextSize(15);
        tv1.setTextColor(Color.BLACK);
        tb.addView(tv1);
        TextView tv2=new TextView(context);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT ,TableRow.LayoutParams.WRAP_CONTENT));
        tv2.setText(k+"");
        tv2.setTextSize(15);
        tv2.setTextColor(Color.BLACK);
        tb.addView(tv2);
        TextView tv3=new TextView(context);
        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT ,TableRow.LayoutParams.WRAP_CONTENT));
        tv3.setText(n+"");
        tv3.setTextSize(15);
        tv3.setTextColor(Color.BLACK);
        tb.addView(tv3);
        return tb;
    }
    /**
     * 获取Assets文件
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context,String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                result += line+"\r\n";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

package com.xinchen.medicalwaste.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.arch.demo.core.utils.ToastUtil;
import com.gprinter.utils.Command;
import com.gprinter.utils.LogUtils;
import com.hnj.dp_nusblist.USBFactory;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.app.MyApp;
import com.xinchen.medicalwaste.bean.LabelBean;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.arch.demo.core.BaseApplication.getAppContext;

public class PrintUtils {
    public static void print(LabelBean labelBean) {
        USBFactory usbfactory = MyApp.usbfactory;
        if (usbfactory == null || !usbfactory.getconnectstate()) {
            ToastUtil.show("usb暂未连接，请稍后重试");
            return;
        }
        usbfactory.LabelBegin(600, 360);//先设置标签的宽度和高度
        /**
         * 1a 26 01 是画矩形指令
         * 55 00 是X轴起始位置（向左移动多少）前面是00是低位，后面00是高位 ，最大范围不能超过标签的宽度
         * 03 00 是Y轴起始位置（向下移动多少）前面是05是低位，后面00是高位，最大范围不能超过标签的高度
         * 30 02 是矩形的长度
         * 50 01 是矩形的高度
         * 03 00 是线条粗细
         * 01 固定，使用黑线打印，00为白线不打印
         * */
        String biankuang = "1a 26 01 45 00 03 00 30 02 50 01 03 00 01";
        byte[] bt = ScaleUtils.parseHexStr2Byte(biankuang.replace(" ", ""));
        usbfactory.Sendbyte(bt);

        usbfactory.Sendbyte(getLine("4600", "4000", "3002", "4000"));

        usbfactory.Sendbyte(getLine("46 00", "75 00", "30 02", "75 00"));

        usbfactory.Sendbyte(getLine("46 00", "B0 00", "30 02", "B0 00"));

        usbfactory.Sendbyte(getLine("E0 00", "E5 00", "30 02", "E5 00"));
        usbfactory.Sendbyte(getLine("E0 00", "1A 01", "30 02", "1A 01"));
        //竖线
        usbfactory.Sendbyte(getLine("E0 00", "B0 00", "E0 00", "50 01"));
        usbfactory.Sendbyte(getLine("68 01", "75 00", "68 01", "B0 00"));

//                usbfactory.Sendbyte(getLine("56 00","B0 00","30 02","B0 00"));
        usbfactory.LableText(200, 10, 2, 0, labelBean.getData().getName());
        usbfactory.LableText(80, 80, 1, 0, "医院:" + labelBean.getData().getOrganization());
//        usbfactory.LableText(80, 135, 1, 0,"医废种类:"+labelBean.getData().getName());
//        usbfactory.LableText(370, 135, 1, 0,"科室:"+labelBean.getData().getDepartment());
        usbfactory.LableText(80, 135, 1, 0, "科室:" + labelBean.getData().getDepartment());
        usbfactory.LableText(230, 190, 1, 0, "重量(KG):" + labelBean.getData().getWeight());
//        usbfactory.LableText(390, 190, 1, 0,"收集人:"+labelBean.getData().getCollector());
        usbfactory.LableText(230, 245, 1, 0, "收集人:" + labelBean.getData().getCollector());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String data = sdf.format(Long.parseLong(labelBean.getCreatedAt()));
//        usbfactory.LableText(230, 245 , 1, 0,"日期:"+ data);
        usbfactory.LableText(370, 135, 1, 0, "日期:" + data);
        usbfactory.LableText(400, 245, 1, 0, "交接人:" + labelBean.getData().getCollector());
        usbfactory.LableText(230, 295, 1, 0, labelBean.getNumber());

        usbfactory.LabelQRCode(80, 190, 4, labelBean.getNumber());

        usbfactory.Labelend();
        usbfactory.PaperCut();
    }

    public static byte[] getLine(String x1, String y1, String x2, String y2) {
        String henxian = ("1A 5C 01" + x1 + y1 + x2 + y2 + "01 00 01").replace(" ", "");
        Log.e("pqc", "getLine: " + henxian);
        return ScaleUtils.parseHexStr2Byte(henxian);
    }


    public static void print1(LabelBean labelBean, Context context) {
        Printer printer = Printer.getInstance();
        ThreadPoolManager.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if (printer.getPortManager() == null) {
                        tipsToast(getAppContext().getString(R.string.conn_first));
//                        ToastUtil.show("请先连接打印机");
                        return;
                    }
                    //打印前后查询打印机状态，部分老款打印机不支持查询请去除下面查询代码
                    //******************     查询状态     ***************************

                    Command command = printer.getPortManager().getCommand();
                    int status = printer.getPrinterState(command, 2000);
                    if (status != 0) {//打印机处于不正常状态,则不发送打印任务
                        Message msg = new Message();
                        msg.what = 0x01;
                        msg.arg1 = status;
                        handler.sendMessage(msg);
                        return;
                    }
                    //***************************************************************
                    boolean result = printer.getPortManager().writeDataImmediately(PrintContent.getLabel(context, labelBean));

                    if (result) {
                        tipsToast(context.getString(R.string.send_success));
                    } else {
                        tipsToast(context.getString(R.string.send_fail));
                    }
                    LogUtils.e("send result", result);
                } catch (IOException e) {
                    tipsToast(context.getString(R.string.send_fail) + e.getMessage());
                } catch (Exception e) {
                    tipsToast(context.getString(R.string.send_fail) + e.getMessage());
                } finally {
                    if (printer.getPortManager() == null) {
                        printer.close();
                    }
                }
            }
        });
    }
    /**
     * 提示弹框
     * @param message
     */
    private static void tipsToast(String message){
        Message msg =new Message();
        msg.what=0x00;
        msg.obj=message;
        handler.sendMessage(msg);
    }
     public static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x00:
                    String tip = (String) msg.obj;
                    Toast.makeText(getAppContext(), tip, Toast.LENGTH_SHORT).show();
                    break;
                case 0x01:
                    int status = msg.arg1;
                    if (status == -1) {//获取状态失败
//                        AlertDialog alertDialog = new AlertDialog.Builder(getAppContext())
//                                .setTitle("提示")
//                                .setMessage(getAppContext().getString(R.string.status_fail))
//                                .setIcon(R.mipmap.ic_launcher)
//                                .setPositiveButton(getAppContext().getString(R.string.ok), new DialogInterface.OnClickListener() {//添加"Yes"按钮
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    }
//                                })
//                                .create();
//                        alertDialog.show();
                        Toast.makeText(getAppContext(), getAppContext().getString(R.string.status_fail), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (status == 1) {
                        Toast.makeText(getAppContext(), getAppContext().getString(R.string.status_feed), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (status == 0) {//状态正常
                        Toast.makeText(getAppContext(), getAppContext().getString(R.string.status_normal), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (status == -2) {//状态缺纸
                        Toast.makeText(getAppContext(), getAppContext().getString(R.string.status_out_of_paper), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (status == -3) {//状态开盖
                        Toast.makeText(getAppContext(), getAppContext().getString(R.string.status_open), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (status == -4) {
                        Toast.makeText(getAppContext(), getAppContext().getString(R.string.status_overheated), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 0x02://关闭连接
                    ToastUtil.show(getAppContext().getString(R.string.not_connected));
                    break;
                case 0x03:
                    String message = (String) msg.obj;
                    AlertDialog alertDialog = new AlertDialog.Builder(getAppContext())
                            .setTitle(getAppContext().getString(R.string.tip))
                            .setMessage(message)
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton(getAppContext().getString(R.string.ok), new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create();
                    alertDialog.show();
                    break;
            }
        }
    };
}

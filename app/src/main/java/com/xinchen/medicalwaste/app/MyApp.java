package com.xinchen.medicalwaste.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arch.demo.core.BaseApplication;
import com.arch.demo.core.preference.PreferencesUtil;
import com.arch.demo.core.utils.PermissionUtils;
import com.arch.demo.core.utils.ToastUtil;
import com.arch.demo.network_api.ApiBase;
import com.gprinter.bean.PrinterDevices;
import com.gprinter.utils.CallbackListener;
import com.gprinter.utils.Command;
import com.gprinter.utils.ConnMethod;
import com.hnj.dp_nusblist.USBFactory;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.bean.EventBusBean;
import com.xinchen.medicalwaste.utils.Printer;
import com.xinchen.medicalwaste.utils.SoundPoolPlayer;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import cn.wandersnail.bluetooth.BTManager;
import cn.wandersnail.bluetooth.ConnectCallback;
import cn.wandersnail.bluetooth.Connection;
import cn.wandersnail.bluetooth.EventObserver;

public class MyApp extends BaseApplication implements EventObserver , CallbackListener {
    private String TAG = "InputSDK";
    private int blueToothConnectStatus = Connection.STATE_DISCONNECTED;
    public static String scan = "SCAN";

    public static USBFactory usbfactory;

    private static SoundPoolPlayer sound;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化sp存储
        PreferencesUtil.init(this);
        ApiBase.setNetworkRequestInfo(new NetworkRequestInfo());
//        initBle();//先注释

        printer=Printer.getInstance();//获取管理对象
        initUsb();

        sound = new SoundPoolPlayer(getAppContext());
    }

    public static SoundPoolPlayer getSound() {
        return sound;
    }


    //初始化蓝牙
    private void initBle() {
        connectBlueTooth();
    }


    //连接蓝牙（扫码枪和蓝牙秤）
    private void connectBlueTooth() {
        //实例化并初始化
        BTManager.getInstance().initialize(MyApp.sApplication);
        //获得已经配对过的蓝牙设备列表
        connPairedBlEs();
    }

    private void connPairedBlEs() {
        if (BluetoothAdapter.getDefaultAdapter().enable()) {
            Set<BluetoothDevice> pairedBlEs = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            if (pairedBlEs.isEmpty()) {
                Log.d(TAG, "请先进行蓝牙配对");
                ToastUtil.show("请先配对蓝牙");
            }
            for (BluetoothDevice bluetooth : pairedBlEs) {
                Log.d(
                        TAG,
                        "已配对的蓝牙设备名称：${bluetooth.name}," +
                                "已配对的蓝牙设备类型：${bluetooth.type}," +
                                "已配对的蓝牙设备地址：${bluetooth.address}," +
                                "已配对的蓝牙uuid：${bluetooth.uuids}"
                );
                //连接所有已经配对的
                if (blueToothConnectStatus != Connection.STATE_CONNECTED) {
                    connectBlueTooth(bluetooth);
                }
            }

        } else {
            Log.d(TAG, "蓝牙打开失败");
            ToastUtil.show("蓝牙未打开");

        }
    }

    private void connectBlueTooth(BluetoothDevice device) {
        //如果传null，默认使用{@link #SPP_UUID}连接
        BTManager.getInstance().createConnection(device, this) //观察者监听连接状态
                .connect(null, new ConnectCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "蓝牙连接成功");
                    }

                    @Override
                    public void onFail(@NonNull String errMsg, @Nullable Throwable e) {
                        Log.d(TAG, errMsg);
                    }
                });

    }

    //监听蓝牙状态改变
    @Override
    public void onConnectionStateChanged(BluetoothDevice device, int state) {
        Log.d(
                TAG,
                "蓝牙连接状态：" + state + "," +
                        "蓝牙设备名称：" + device.getName() + "," +
                        "蓝牙设备类型：" + device.getType() + "," +
                        "蓝牙设备地址：" + device.getAddress() + "," +
                        "uuid：" + device.getUuids().toString()
        );
        //修改蓝牙状态
        blueToothConnectStatus = state;

        switch (state) {
            case Connection.STATE_CONNECTING:

                break;
            case Connection.STATE_PAIRING:

                break;
            case Connection.STATE_PAIRED:
                //配对完成之后，连接蓝牙
                connectBlueTooth(device);

                break;
            case Connection.STATE_CONNECTED:

                break;
            case Connection.STATE_DISCONNECTED:
                //断开重连
                connectBlueTooth(device);

                break;

            case Connection.STATE_RELEASED:
                break;
        }
    }

    //接收蓝牙设备数据
    StringBuffer barCode = new StringBuffer();

    @Override
    public void onRead(BluetoothDevice device, byte[] value) {
//        super.onRead(device, value);
        if (device.getName().contains("BarCode Scanner")) {
            barCode.append(new String(value));
            if (value[value.length - 1] == 13)//以回车结束
            {
                Log.d(
                        TAG, "barCode=" + barCode + "}"
                );
//                ToastUtil.show(barCode.toString().trim());
                EventBus.getDefault().post(new EventBusBean(EventBusBean.DEVICE_SCAN, barCode.toString().trim()));
                //清空，准备接收下一次扫码
                barCode.setLength(0);
            }
        }


    }

    private Handler mHandler;
    private UsbManager mUsbManager;
    private HashMap<String, UsbDevice> deviceList;
    private Iterator<UsbDevice> deviceIterator;
    private final int CONNECTRESULT = 0x05;
    private final int CONNECT = 0x26;

    private void initUsb() {
        mHandler = new MHandler();
        usbfactory = USBFactory.getUsbFactory(mHandler);
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        AutomaticConnection();
    }

    //    //搜索USB设备search USB
//    @SuppressLint("RtlHardcoded")
//    private void SearchUSB() {
//        deviceList = mUsbManager.getDeviceList();
//        deviceIterator = deviceList.values().iterator();
//        usbfactory.CloseUSB();
//        if (deviceList.size() > 0) {
//            // 初始化选择对话框布局，并添加按钮和事件
//            while (deviceIterator.hasNext()) { // 这里是if不是while，说明我只想支持一种device
//                final UsbDevice device = deviceIterator.next();
//                device.getInterfaceCount();
//                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(getApplicationInfo().packageName), 0);
//                if (!mUsbManager.hasPermission(device)) {
//                    mUsbManager.requestPermission(device, mPermissionIntent);
//                } else {
//                    boolean connectUsb = usbfactory.connectUsb(mUsbManager, device);
//                    if (connectUsb) {
//                        Log.e(TAG, "USB连接成功!!");
//                        mHandler.removeMessages(CONNECTRESULT);
//                        return;
//                    } else {
//                        Log.e(TAG, "USB连接失败!!");
//                    }
//                    mHandler.removeMessages(CONNECTRESULT);
//                    mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
//                }
//            }
//        }
//    }
    boolean isLink=false;
//自动搜索并连接USB设备
    @SuppressLint("RtlHardcoded")
    private void AutomaticConnection() {
        deviceList = mUsbManager.getDeviceList();
        deviceIterator = deviceList.values().iterator();
        if (deviceList.size() > 0) {
            isLink=false;
            // 初始化选择对话框布局，并添加按钮和事件
            while (deviceIterator.hasNext()) { // 这里是if不是while，说明我只想支持一种device
                final UsbDevice device = deviceIterator.next();
                device.getInterfaceCount();
                int vid = device.getVendorId();
                int pid = device.getProductId();

                if ((vid == 1155 && pid == 22336) || (vid == 1157 && pid == 22337))//判断是不是打印机编号
                {
                    PendingIntent mPermissionIntent;

                    mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(getApplicationInfo().packageName), 0);

                    if (!mUsbManager.hasPermission(device)) {
                        mUsbManager.requestPermission(device, mPermissionIntent);
                        mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
                    } else {
                        boolean connectUsb = usbfactory.connectUsb(mUsbManager, device);

                        if (connectUsb) {
//                            mHandler.removeMessages(CONNECTRESULT);
                            //连接成功在这里调用打印的方法
                            isLink=true;
                        } else {
                            Log.e(TAG, "USB连接失败!!");
                            mHandler.removeMessages(CONNECTRESULT);
                            mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
                        }

                    }
                }else if (vid==1137&&pid==85){
                    PendingIntent mPermissionIntent;

                    mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(getApplicationInfo().packageName), 0);

                    if (!mUsbManager.hasPermission(device)) {
                        mUsbManager.requestPermission(device, mPermissionIntent);
                        mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
                    } else {
                        PrinterDevices usb = new PrinterDevices.Build()
                                .setContext(this)
                                .setConnMethod(ConnMethod.USB)
                                .setUsbDevice(device)
                                .setCommand(Command.TSC)
                                .setCallbackListener(this)
                                .build();
                        printer.connect(usb);
                        isLink=true;
                    }
                }
            }
            if (!isLink){
                mHandler.removeMessages(CONNECT);
                mHandler.sendEmptyMessageDelayed(CONNECT, 1000);
            }

        } else {
            mHandler.removeMessages(CONNECT);
            mHandler.sendEmptyMessageDelayed(CONNECT, 1000);
        }
    }
    Printer printer=null;
//    PermissionUtils permissionUtils;
//    Handler handler=new Handler(Looper.getMainLooper()){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            switch (msg.what){
//                case 0x00:
//                    String tip=(String)msg.obj;
//                    Toast.makeText(getAppContext(),tip,Toast.LENGTH_SHORT).show();
//                    break;
//                case 0x01:
//                    int status=msg.arg1;
//                    if (status==-1){//获取状态失败
//                        AlertDialog alertDialog = new AlertDialog.Builder(getAppContext())
//                                .setTitle(getString(R.string.tip))
//                                .setMessage(getString(R.string.status_fail))
//                                .setIcon(R.mipmap.ic_launcher)
//                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {//添加"Yes"按钮
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    }
//                                })
//                                .create();
//                        alertDialog.show();
//                        return;
//                    }else if (status==1){
//                        Toast.makeText(getAppContext(),getString(R.string.status_feed),Toast.LENGTH_SHORT).show();
//                        return;
//                    }else if (status==0){//状态正常
//                        Toast.makeText(getAppContext(),getString(R.string.status_normal),Toast.LENGTH_SHORT).show();
//                        return;
//                    }else if (status==-2){//状态缺纸
//                        Toast.makeText(getAppContext(),getString(R.string.status_out_of_paper),Toast.LENGTH_SHORT).show();
//                        return;
//                    }else if (status==-3){//状态开盖
//                        Toast.makeText(getAppContext(),getString(R.string.status_open),Toast.LENGTH_SHORT).show();
//                        return;
//                    }else if (status==-4){
//                        Toast.makeText(getAppContext(),getString(R.string.status_overheated),Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    break;
//                case 0x02://关闭连接
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (printer.getPortManager()!=null){
//                                printer.close();
//                            }
//                        }
//                    }).start();
//
////                    tvState.setText();
//                    ToastUtil.show(getString(R.string.not_connected));
//                    break;
//                case 0x03:
//                    String message=(String)msg.obj;
//                    AlertDialog alertDialog = new AlertDialog.Builder(getAppContext())
//                            .setTitle(getString(R.string.tip))
//                            .setMessage(message)
//                            .setIcon(R.mipmap.ic_launcher)
//                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {//添加"Yes"按钮
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                }
//                            })
//                            .create();
//                    alertDialog.show();
//                    break;
//            }
//        }
//    };
    @Override
    public void onConnecting() {//连接打印机中
//        tvState.setText(getString(R.string.conning));
    }

    @Override
    public void onCheckCommand() {//查询打印机指令
//        tvState.setText(getString(R.string.checking));
    }

    @Override
    public void onSuccess(PrinterDevices devices) {//连接成功
        Toast.makeText(getAppContext(),getString(R.string.conn_success),Toast.LENGTH_SHORT).show();
//        tvState.setText(devices.toString());
    }

    @Override
    public void onReceive(byte[] bytes) {

    }

    @Override
    public void onFailure() {//连接失败
        Toast.makeText(getAppContext(),getString(R.string.conn_fail),Toast.LENGTH_SHORT).show();
//        handler.obtainMessage(0x02).sendToTarget();
        mHandler.removeMessages(CONNECTRESULT);
        mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
    }

    @Override
    public void onDisconnect() {//断开连接
        Toast.makeText(getAppContext(),"打印机断开",Toast.LENGTH_SHORT).show();
//        handler.obtainMessage(0x02).sendToTarget();
        mHandler.removeMessages(CONNECTRESULT);
        mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
    }

    @SuppressLint("HandlerLeak")
    class MHandler extends Handler {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECTRESULT: {
                    if (usbfactory != null) {
                        if (usbfactory.getconnectstate()) {
                            usbfactory.is_connectdevice();
                            mHandler.removeMessages(CONNECTRESULT);
                            mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
//							 Log.e("---------", "==============连接成功=======1111");
                        } else {
                            mHandler.removeMessages(CONNECT);
                            mHandler.sendEmptyMessageDelayed(CONNECT, 1000);
                            Log.e("---------", "==============连接失败=======000");
                        }
                    }

                    break;
                }
                case CONNECT: {
                    AutomaticConnection();//自动连接
                    break;
                }
                case USBFactory.CHECKPAGE_RESULT: {
                    if (msg.getData().getString("state").equals("1")) {

                    } else {
                    }
                    break;
                }
                case USBFactory.PRINTSTATE: {
                    boolean printstate = msg.getData().getBoolean("printstate");
                    if (printstate) {
                        ToastUtil.show("打印完成");//打印完成
                    } else {
                        ToastUtil.show("打印失败");//打印失败
                    }

                    break;
                }

            }
        }
    }
}

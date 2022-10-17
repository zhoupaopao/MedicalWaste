package com.xinchen.medicalwaste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.arch.demo.core.activity.MvvmActivity;
import com.arch.demo.core.utils.SharedPrefUtil;
import com.arch.demo.core.utils.ToastUtil;
import com.arch.demo.core.viewmodel.MvvmBaseViewModel;
import com.arch.demo.network_api.errorhandler.ExceptionHandle;
import com.arch.demo.network_api.observer.BaseObserver;
import com.easy.tech.serialutil.engine.SerialEngine;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xinchen.medicalwaste.R;
import com.xinchen.medicalwaste.api.Api;
import com.xinchen.medicalwaste.api.AppConstant;
import com.xinchen.medicalwaste.api.MyObserver;
import com.xinchen.medicalwaste.app.MyApp;
import com.xinchen.medicalwaste.bean.AcheveReturnLabelBean;
import com.xinchen.medicalwaste.bean.AchieveLabelBean;
import com.xinchen.medicalwaste.bean.ApiStringBean;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.bean.LableListBean;
import com.xinchen.medicalwaste.bean.LablePrintBean;
import com.xinchen.medicalwaste.bean.SelectedRegistBean;
import com.xinchen.medicalwaste.bean.SubmitResultBean;
import com.xinchen.medicalwaste.bean.WareHouseBean;
import com.xinchen.medicalwaste.bean.WasteInventoryBean;
import com.xinchen.medicalwaste.databinding.ActivityWeighBinding;
import com.xinchen.medicalwaste.utils.PrintUtils;
import com.xinchen.medicalwaste.utils.UnDoubleClickListener;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.f1reking.serialportlib.SerialPortHelper;
import me.f1reking.serialportlib.entity.DATAB;
import me.f1reking.serialportlib.entity.FLOWCON;
import me.f1reking.serialportlib.entity.PARITY;
import me.f1reking.serialportlib.entity.STOPB;
import me.f1reking.serialportlib.listener.IOpenSerialPortListener;
import me.f1reking.serialportlib.listener.ISerialPortDataListener;
import me.f1reking.serialportlib.listener.Status;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.arch.demo.core.utils.EncryptDecodeUtils.bytesToHexString;
import static com.xinchen.medicalwaste.utils.ScaleUtils.COMMAND_SCALE;
import static com.xinchen.medicalwaste.utils.ScaleUtils.parseScale;

public class WeighActivity extends MvvmActivity<ActivityWeighBinding, MvvmBaseViewModel> {
    private SerialPortHelper scaleSerialPortHelper;
    private final String SP_NAME_SCALE= AppConstant.isBijiguang? "ttyS3":"ttyS0";
    private final int SP_RATE_SCALE= 19200;
    private Timer timer;
    public static void launch(Activity context, LableListBean lableListBean) {
        Intent intent=new Intent(context,WeighActivity.class);
        intent.putExtra("lableListBean",lableListBean);
        intent.putExtra("type",true);
        context.startActivityForResult(intent,100);
    }
    public static void launch(Context context, WasteInventoryBean collectWasteBean) {
        Intent intent=new Intent(context,WeighActivity.class);
        intent.putExtra("collectWasteBean",collectWasteBean);
        intent.putExtra("type",false);
        context.startActivity(intent);
    }
    class TaskSerialPortSendCommand extends TimerTask{
        @Override
        public void run() {
            if (AppConstant.isBijiguang){
                byte[] read_weight_current = new byte[]{(byte) 0x11, (byte) 0x42, (byte) 0x3f, (byte) 0x12, (byte) 0x0d};
                scaleSerialPortHelper.sendBytes(read_weight_current);
            }else
                scaleSerialPortHelper.sendHex(COMMAND_SCALE);

        }
    }
    String weight="";
    //称重串口
    private void openScaleSerialPort() {
        scaleSerialPortHelper = new  SerialPortHelper();
        scaleSerialPortHelper.setPort("/dev/"+SP_NAME_SCALE);
        scaleSerialPortHelper.setBaudRate(SP_RATE_SCALE);
        scaleSerialPortHelper.setStopBits(STOPB.getStopBit(STOPB.B2));
        scaleSerialPortHelper.setDataBits(DATAB.getDataBit(DATAB.CS8));
        scaleSerialPortHelper.setParity(PARITY.getParity(PARITY.NONE));
        scaleSerialPortHelper.setFlowCon(FLOWCON.getFlowCon(FLOWCON.NONE));
        scaleSerialPortHelper.setIOpenSerialPortListener(new IOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {
                Log.d("pqc", "称重传感器串口:串口打开成功");
                openScaleSerialPortPower();
//                timer?.schedule(TaskSerialPortSendCommand(), 0, 500);
                if (timer!=null)
                timer.schedule(new TaskSerialPortSendCommand(),0,500);
            }

            @Override
            public void onFail(File device, Status status) {
                if (status==Status.NO_READ_WRITE_PERMISSION){
                    Log.d("TAG", "称重传感器串口：没有读写权限");
                }else if (status==Status.OPEN_FAIL){
                    Log.d("TAG", "称重传感器串口：串口打开失败");
                }else {
                    Log.d("TAG", "称重传感器串口：串口打开失败");
                }
            }

        });

        scaleSerialPortHelper.setISerialPortDataListener(new ISerialPortDataListener() {

            @Override
            public void onDataReceived(byte[] bytes) {
                String hex = bytesToHexString(bytes);
                if (AppConstant.isBijiguang){
                    weight=parseScale(bytes);
                }else {
                    weight = parseScale(hex);
                }
                Log.d("TAG", "称重传感器串口：接收----"+hex+",size="+hex.length()+"},解析结果:"+weight);

                //重量大小0再显示
                if (Float.parseFloat(weight)>= 0) {
//                    findViewById<TextView>(R.id.tv_weight).text = weight
//                    ToastUtil.show(weight);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewDataBinding.tvWeight.setText(weight);
                        }
                    });

                }

//                findViewById<TextView>(R.id.tv_currentWeight).text = weight

            }

            @Override
            public void onDataSend(byte[] bytes) {
                String hex = bytesToHexString(bytes);
                Log.d("TAG", "称重传感器串口：发送----"+hex+"},size="+hex.length());
                /*  //发送之后间隔100毫秒再发送
                  if (hex == COMMAND_SCALE) {
                      timerSendHex()
                  }*/

            }

        });

        //打开
        scaleSerialPortHelper.open();


    }
    //打开秤串口电源
    private void openScaleSerialPortPower() {
        try {
            SerialEngine serialEngine = new SerialEngine.Builder(SP_NAME_SCALE)
                    .setBaudRate(SP_RATE_SCALE).setWriteOnly(false).setDataBitCount(8)
                    .setCheckFlag(0).setStopBitCount(1).build();
            serialEngine.openWithPower();
            Log.d("pqc", "称重传感器串口电源打开成功");
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d("pqc", "称重传感器串口电源打开失败，发了异常:"+e.getMessage());
        }
    }


    LableListBean lableListBean;
    WasteInventoryBean collectWasteBean;
    boolean type=false;//判断是否是复核称重
    @Override
    protected void initData() {
        MyApp.getSound().playShortResource("请进行称重");
        getCangku();
        timer = new Timer();
        openScaleSerialPort();
        type=getIntent().getBooleanExtra("type",false);
        if (type){
            lableListBean=(LableListBean) getIntent().getSerializableExtra("lableListBean");
            viewDataBinding.tvTitle.setText("复核称重");
        }else {
            collectWasteBean= (WasteInventoryBean) getIntent().getSerializableExtra("collectWasteBean");
            viewDataBinding.tvTitle.setText("称重");
            viewDataBinding.llNum.setVisibility(View.GONE);
            viewDataBinding.llWeightSum.setVisibility(View.GONE);
        }

        if (lableListBean!=null){

            LabelBean.ListBean bean=lableListBean.getList().get(0).getData();
            viewDataBinding.tvType.setText(bean.getName());
            viewDataBinding.tvDepartments.setText(bean.getDepartment());
            viewDataBinding.tvCollector.setText(bean.getCollector());
            viewDataBinding.tvLabelNum.setText(lableListBean.getTotal()+"");
            BigDecimal sum=new BigDecimal(0);
            for (int i = 0; i < lableListBean.getList().size(); i++) {
                sum=sum.add(new BigDecimal(lableListBean.getList().get(i).getData().getWeight()));
            }
            viewDataBinding.tvWeightSum.setText(sum.toString()+"kg");


        }else if (collectWasteBean!=null){
            viewDataBinding.tvType.setText(collectWasteBean.getWaste().getName());
            LabelBean labelBean= (LabelBean) SharedPrefUtil.getObject("department");
            viewDataBinding.tvDepartments.setText(labelBean.getData().getName());
            viewDataBinding.tvCollector.setText(SharedPrefUtil.getUser().getNickname());
        }
    }

    @Override
    protected void initListener() {
        viewDataBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewDataBinding.btOk.setOnClickListener(new UnDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (type){
                    submitSumWeight();
                }else {
                    submitRegist();
                }

            }
        });
    }

    private void submitSumWeight() {
        showLoadingDialog("加载中...");
        Api.getInstance().packingWeights(viewDataBinding.tvWeight.getText().toString().trim(),lableListBean, new MyObserver<SubmitResultBean>() {
            @Override
            protected void onHandleSuccess(SubmitResultBean bean) {

                if (bean.getCode()==200){
                    collect();
                }else {
                    hideLoadingDialog();
                    ToastUtil.show(bean.getMsg());
                }
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideLoadingDialog();
                ToastUtil.show(e.message);
            }
        });
    }

    private void submitRegist() {
        achieveLabel(viewDataBinding.tvWeight.getText().toString());

    }


    public void achieveLabel(String weight){
        showLoadingDialog("加载中...");
        AchieveLabelBean.Organization organization = new AchieveLabelBean.Organization(SharedPrefUtil.getUser().getTenants().get(0).getOpenid());
//        String id,String number,String name,String type,String code,String department,String organization

        LabelBean departmentBean=SharedPrefUtil.getObjectT("department");
        AchieveLabelBean.SkuBeanMsg skuBeanMsg = new AchieveLabelBean.SkuBeanMsg(collectWasteBean.getWaste().getId()+"", collectWasteBean.getWaste().getNumber(), collectWasteBean.getWaste().getName(), collectWasteBean.getWaste().getType(), "",departmentBean.getData().getName(), SharedPrefUtil.getUser().getTenants().get(0).getName(),weight,SharedPrefUtil.getUser().getNickname(),SharedPrefUtil.getString("EmployeeIdCard"),SharedPrefUtil.getUser().getNickname());
        AchieveLabelBean achieveLabelBean = new AchieveLabelBean("thing", "medicalWaste", "created", null, null, organization, skuBeanMsg);
        RequestBody requestBody= RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(achieveLabelBean));
        Api.getInstance().achieveLabel(requestBody, new MyObserver<AcheveReturnLabelBean>() {
            @Override
            protected void onHandleSuccess(AcheveReturnLabelBean acheveReturnLabelBean) {
//                sendMessage("success");
                //提交登记
                submitRegister( acheveReturnLabelBean,weight);

            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideLoadingDialog();
                ToastUtil.show("标签创建失败");
            }
        });
    }

    private void submitRegister(AcheveReturnLabelBean acheveReturnLabelBean,String weight) {

        ArrayList<SelectedRegistBean> mList=new ArrayList<>();
        SelectedRegistBean selectedRegistBean=new SelectedRegistBean();
        LabelBean departmentBean=SharedPrefUtil.getObjectT("department");
        selectedRegistBean.setSource(departmentBean.getData().getName());
        selectedRegistBean.setWeight(Double.parseDouble(weight));
        selectedRegistBean.setWom("KG");
        selectedRegistBean.setQuantity(1);
        SelectedRegistBean.weasteBean weasteBean=new SelectedRegistBean.weasteBean(collectWasteBean.getWaste().getId()+"",collectWasteBean.getWaste().getName(),collectWasteBean.getWaste().getNumber());
        selectedRegistBean.setWaste(weasteBean);
        SelectedRegistBean.ChildRegistBean childRegistBean=new SelectedRegistBean.ChildRegistBean();
        childRegistBean.setLabel(acheveReturnLabelBean.getLabel().getNumber());
        childRegistBean.setUom("KG");
        childRegistBean.setQuantity(Double.parseDouble(weight));
        ArrayList<SelectedRegistBean.ChildRegistBean>childRegistBeans=new ArrayList<>();
        childRegistBeans.add(childRegistBean);
        selectedRegistBean.setDetails(childRegistBeans);
        mList.add(selectedRegistBean);
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(mList));
        Api.getInstance().batchSave(requestBody, new MyObserver<ApiStringBean>() {
            @Override
            protected void onHandleSuccess(ApiStringBean apiStringBean) {

                //执行打印
                if(apiStringBean.getStatus()==201){
//                    ToastUtil.show("登记完成");
                    //获取这个标签信息
                    getLabelBean(acheveReturnLabelBean.getLabel().getNumber());

                }else{
//                    Tips.show("登记异常，请重试");
                    hideLoadingDialog();
                    ToastUtil.show("");
                }

            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
//                sendMessage("fail");
                ToastUtil.show("登记失败");
                hideLoadingDialog();
            }
        });

    }

    //获取label用于打印
    private void getLabelBean(String number) {
        Api.getInstance().labels(number, new MyObserver<LabelBean>() {
            @Override
            protected void onHandleSuccess(LabelBean labelBean) {
                lableListBean =new LableListBean();
                lableListBean.setTotal(1);
                List<LabelBean>bean=new ArrayList<>();
                bean.add(labelBean);
                lableListBean.setList(bean);
                collect();
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {

            }
        });
    }
    WareHouseBean wareHouse;
    private void getCangku() {
        showLoadingDialog("");
        Api.getInstance().warehouses(new MyObserver<WareHouseBean>() {
            @Override
            protected void onHandleSuccess(WareHouseBean wareHouseBean) {
                hideLoadingDialog();
                wareHouse = wareHouseBean;
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideLoadingDialog();
                ToastUtil.show("仓库获取失败，请返回重试");
            }
        });
    }
    /***收集完成**/
    private void collect() {
        if (wareHouse == null || wareHouse.getList() == null || wareHouse.getList().size() <= 0) {
            hideLoadingDialog();
            ToastUtil.show("仓库获取失败");
            return;
        }
        Api.getInstance().collectRecords(wareHouse, lableListBean, new MyObserver<JsonObject>() {
            @Override
            protected void onHandleSuccess(JsonObject jsonObject) {
                hideLoadingDialog();
                ToastUtil.show(jsonObject.get("status").toString());
                if (jsonObject.get("status").getAsInt() == 201) {
                    ToastUtil.show("收集成功");
                    MyApp.getSound().playShortResource("收集完成");
                    if (type){
                        setResult(Activity.RESULT_OK);
                    }else {
                        if (AppConstant.isBijiguang){
                            PrintUtils.print(lableListBean.getList().get(0));
                        }else {
                            PrintUtils.print1(lableListBean.getList().get(0), WeighActivity.this);
                        }
                    }
                    finish();
                }
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideLoadingDialog();
                ToastUtil.show("收集失败");
            }
        });
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected MvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_weigh;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
        }
    }
}
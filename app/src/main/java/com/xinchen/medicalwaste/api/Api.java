package com.xinchen.medicalwaste.api;

import com.arch.demo.core.model.TokenBean;
import com.arch.demo.core.model.UserBean;
import com.arch.demo.core.utils.AppUtils;
import com.arch.demo.core.utils.SharedPrefUtil;
import com.arch.demo.network_api.ApiBase;
import com.arch.demo.network_api.beans.BaseListResponse;
import com.arch.demo.network_api.beans.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xinchen.medicalwaste.bean.AcheveReturnLabelBean;
import com.xinchen.medicalwaste.bean.ApiStringBean;
import com.xinchen.medicalwaste.bean.CollectSubmitBean;
import com.xinchen.medicalwaste.bean.InventoryItem;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.bean.LableListBean;
import com.xinchen.medicalwaste.bean.LablePrintBean;
import com.xinchen.medicalwaste.bean.PlatformLoginBean;
import com.xinchen.medicalwaste.bean.SubmitBean;
import com.xinchen.medicalwaste.bean.SubmitResultBean;
import com.xinchen.medicalwaste.bean.WareHouseBean;
import com.xinchen.medicalwaste.bean.WasteInventoryBean;
import com.xinchen.medicalwaste.bean.WasteSourceBean;
import com.xinchen.medicalwaste.utils.BigUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Api extends ApiBase {
    private static volatile Api instance = null;
    private ApiInterface apiInterface;
    protected Api() {
        super(AppConstant.baseUrl);
        apiInterface = retrofit.create(ApiInterface.class);
    }
    public static Api getInstance() {
        if (instance == null) {
            synchronized (Api.class) {
                if (instance == null) {
                    instance = new Api();
                }
            }
        }
        return instance;
    }
    public void tokenByNumber(String number, Observer<TokenBean> observer){
//        RequestBody requestBody = new FormBody.Builder()
//                .add("number", number)
//                .build();
        ApiSubscribe(apiInterface.tokenByNumber(number), observer);
    }
    public void accountMe(Observer<UserBean> observer){
        ApiSubscribe(apiInterface.accountsMe(), observer);
    }

    public void platformLogin( String openId,Observer<PlatformLoginBean> observer){
        RequestBody requestBody = new FormBody.Builder()
                .add("openid", openId)
                .build();
        ApiSubscribe(apiInterface.platformLogin(requestBody), observer);
    }

    public void labels(String number,Observer<LabelBean> observer){
        ApiSubscribe(apiInterface.labels(number), observer);
    }

    public void baseWasteSourcesNum(String number,Observer<WasteSourceBean>observer){
        ApiSubscribe(apiInterface.baseWasteSourcesNum(number),observer);
    }
    public void getBagList(String number,Observer<LableListBean>observer){
        ApiSubscribe(apiInterface.getBagList(number),observer);
    }
    public void collect(Observer<LableListBean>observer){

//        ApiSubscribe(apiInterface.CollectbatchSave(),observer);
    }
    /**
     * 获取仓库列表
     * */
    public void warehouses(Observer<WareHouseBean>observer){
        ApiSubscribe(apiInterface.warehouses(),observer);
    }

    public void collectRecords(WareHouseBean houseBean, LableListBean lableListBean,Observer<JsonObject>observer) {
        ArrayList<CollectSubmitBean> submitList = new ArrayList<>();
//        for (LabelBean selectedRegistBean : lableListBean.getList()) {
        LabelBean selectedRegistBean =lableListBean.getList().get(0);
            CollectSubmitBean.WasteBean waste = new CollectSubmitBean.WasteBean();
            waste.setId(selectedRegistBean.getData().getId());
            waste.setNumber(selectedRegistBean.getData().getNumber());
            waste.setName(selectedRegistBean.getData().getName());
            CollectSubmitBean collectSubmitBean = new CollectSubmitBean();
            collectSubmitBean.setWaste(waste);
            WasteSourceBean wasteSourceBean=new WasteSourceBean();
            wasteSourceBean.setName(selectedRegistBean.getData().getDepartment());
            wasteSourceBean.setType("department");
            collectSubmitBean.setFrom(wasteSourceBean);
            if (houseBean.getTotal()!=0) {
                collectSubmitBean.setTo(houseBean.getList().get(0));
            }
            collectSubmitBean.setLot(selectedRegistBean.getLot());

            collectSubmitBean.setCount(lableListBean.getTotal());
            collectSubmitBean.setUom("KG");
            collectSubmitBean.setStepone("true");
//            collectSubmitBean.setExtention(wslBean.getExtention());
            collectSubmitBean.setCollector(new CollectSubmitBean.CollecterBean(SharedPrefUtil.getUser().getNickname(), SharedPrefUtil.getString("EmployeeIdCard")));
            collectSubmitBean.setTransactor(new CollectSubmitBean.TransactorBean(selectedRegistBean.getData().getTransactor(), selectedRegistBean.getData().getTransactorIdCard()));
            ArrayList<CollectSubmitBean.DetailBean> list = new ArrayList<>();
            if (selectedRegistBean.getLot() == null || selectedRegistBean.getLot().equals("")) {
                collectSubmitBean.setExited(true);//以后改成false，现在默认true
            } else {
                collectSubmitBean.setExited(true);
            }
            double weight=0;
            if (lableListBean != null) {
                for (LabelBean childRegistBean : lableListBean.getList()) {
                    weight= BigUtil.add(Double.parseDouble(childRegistBean.getData().getWeight()),weight);
//                    weight+=Double.parseDouble(childRegistBean.getData().getWeight());
                    list.add(new CollectSubmitBean.DetailBean(childRegistBean.getNumber(), Double.parseDouble(childRegistBean.getData().getWeight()), "KG"));
                }
            }
        collectSubmitBean.setQuantity(weight);
            collectSubmitBean.setDetails(list);
            submitList.add(collectSubmitBean);
//        }
        ApiSubscribe(apiInterface.CollectbatchSave(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(submitList))),observer);
    }

    //只搜索袋子，同时
    public void labelMy( int offset, int limit,Observer<LablePrintBean> observer){
        ApiSubscribe(apiInterface.labelMy("thing","",offset,limit), observer);
    }

    public void wasteProduceRecord( String source,MyObserver<BaseListResponse<ArrayList<WasteInventoryBean>>> observer){
        ApiSubscribe(apiInterface.wasteProduceRecord(source,"",0,20), observer);
    }
    public void getwasteRecordTodayCollection( MyObserver<ArrayList<InventoryItem>> observer){
        ApiSubscribe(apiInterface.getwasteRecordTodayCollection(), observer);
    }


    /**复核称重**/
    public void packingWeights(String weight,LableListBean lableListBean,Observer<SubmitResultBean> observer){
        SubmitBean submitBean =new SubmitBean();
        submitBean.setWasteType(lableListBean.getList().get(0).getData().getName());
        submitBean.setOfficeName(lableListBean.getList().get(0).getData().getDepartment());
        submitBean.setTotalPackageNumber(lableListBean.getTotal());
        submitBean.setReviewWeight(weight);

        submitBean.setCollectPerson(lableListBean.getList().get(0).getData().getCollector());
        submitBean.setUom("KG");

        List<SubmitBean.WeightDetail>list=new ArrayList<>();
        BigDecimal sum=new BigDecimal(0);
        for (int i = 0; i < lableListBean.getList().size(); i++) {
            sum=sum.add(new BigDecimal(lableListBean.getList().get(i).getData().getWeight()));
            LabelBean listBean = lableListBean.getList().get(i);
            SubmitBean.WeightDetail bean=new SubmitBean.WeightDetail(listBean.getNumber(),listBean.getData().getWeight(),"KG");
            list.add(bean);
        }
        submitBean.setTotalWeight(sum.toString());
        submitBean.setDetails(list);
        ApiSubscribe(apiInterface.packingWeights(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(submitBean))), observer);
    }

    public void achieveLabel(RequestBody requestBody,Observer<AcheveReturnLabelBean> observer){
        ApiSubscribe(apiInterface.achieveLabel(requestBody), observer);
    }
    public void batchSave(RequestBody requestBody,Observer<ApiStringBean> observer){
        ApiSubscribe(apiInterface.batchSave(requestBody), observer);
    }
}

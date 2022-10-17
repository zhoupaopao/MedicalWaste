package com.xinchen.medicalwaste.api;

import com.arch.demo.core.model.TokenBean;
import com.arch.demo.core.model.UserBean;
import com.arch.demo.network_api.beans.BaseListResponse;
import com.arch.demo.network_api.beans.BaseResponse;
import com.google.gson.JsonObject;
import com.xinchen.medicalwaste.bean.AcheveReturnLabelBean;
import com.xinchen.medicalwaste.bean.ApiStringBean;
import com.xinchen.medicalwaste.bean.InventoryItem;
import com.xinchen.medicalwaste.bean.LabelBean;
import com.xinchen.medicalwaste.bean.LableListBean;
import com.xinchen.medicalwaste.bean.LablePrintBean;
import com.xinchen.medicalwaste.bean.PlatformLoginBean;
import com.xinchen.medicalwaste.bean.SubmitResultBean;
import com.xinchen.medicalwaste.bean.WareHouseBean;
import com.xinchen.medicalwaste.bean.WasteInventoryBean;
import com.xinchen.medicalwaste.bean.WasteSourceBean;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("oauth/token?client_id=web&grant_type=numbers&scope=read")
    Observable<TokenBean> tokenByNumber(@Query("number") String number);

    @GET("/accounts/me")
    Observable<UserBean> accountsMe();

    //1.3，登录租户，获取用户角色，判断是否有权限

    @POST("platform/login")
    Observable<PlatformLoginBean> platformLogin(@Body RequestBody requestBody);

    @GET("label/labels/{number}")
    Observable<LabelBean> labels(@Path("number") String number);

    @GET("waste/base/wasteSources/num/{number}")
    Observable<WasteSourceBean> baseWasteSourcesNum(@Path("number") String number);
    @GET("label/labels/limitNumber/{number}")
    Observable<LableListBean> getBagList(@Path("number") String number);
//    @POST("waste/weight/packingWeights")
//    Observable<LableListBean> packingWeights();

    @POST("waste/record/wasteCollectionRecords/batchSave")
    Observable<JsonObject> CollectbatchSave(@Body RequestBody requestBody);


    @GET("wms/warehouse/warehouses")
    Observable<WareHouseBean> warehouses();
    @GET("label/labels/my")
    Observable<LablePrintBean> labelMy(@Query("params[type]") String batch,//周转箱
                                       @Query("params[keywords]") String search,//模糊搜索
                                       @Query("offset") int offset,
                                       @Query("limit") int limit
    );

    @GET("waste/record/wasteRecordToday/collection")
    Observable<ArrayList<InventoryItem>> getwasteRecordTodayCollection();

    //搜索危废类型列表
    @GET("waste/record/wasteInventories/findByWasteName")
    Observable<BaseListResponse<ArrayList<WasteInventoryBean>>> wasteProduceRecord(@Query("params[source]") String source,
                                                                                   @Query("params[keywords]") String wasteName,
                                                                                   @Query("offset") int offset,
                                                                                   @Query("limit") int limit
    );

    /**上传收集记录*/
    @POST("waste/weight/packingWeights")
    Observable<SubmitResultBean> packingWeights(@Body RequestBody requestBody);

    // 创建单个标签
    @POST("label/labels")
    Observable<AcheveReturnLabelBean> achieveLabel(@Body RequestBody requestBody);

    //保存登记
    @POST("waste/record/wasteProduceRecord/batchSave")
    Observable<ApiStringBean> batchSave(@Body RequestBody requestBody);

}

package com.arch.demo.network_api.beans;


import com.arch.demo.core.utils.EmptyUtil;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author pqc
 */
public class BaseResponse<T> {
    private String code;
    private String msg;
    @SerializedName(value="body", alternate={"data"})
    private T body;
    public boolean isSuccess(){
        return code.equals("0")||code.equals("200");
    }

    public String getErr() {
        return code;
    }

    public void setErr(String err) {
        this.code = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return body;
    }

    public void setData(T data) {
        this.body = data;
    }

    public boolean hasData() {
        return !EmptyUtil.isEmpty(body);
    }
}

package com.xinchen.medicalwaste.bean;

import java.io.Serializable;
import java.util.List;

public class LableListBean implements Serializable {

    private int total;
    private List<LabelBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<LabelBean> getList() {
        return list;
    }

    public void setList(List<LabelBean> list) {
        this.list = list;
    }


}

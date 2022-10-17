package com.xinchen.medicalwaste.bean;

import java.util.List;

/**
 * Created by zcx on 2018/6/12.
 */

public class CollectSubmitBean {
    WasteBean waste;
    WasteSourceBean from;
    WareHouseBean.ListBean to;
    double weight;
    String uow;
    String stepone;
    String wasteHouseId;
    List<DetailBean> details;
    String lot="";
    TransactorBean  transactor;
    CollecterBean collector;
    int quantity;//数量
    Boolean isExited=false;

//    WasteSourcesBean.WasteSourcesLabelBean.ExtentionBean extention;
//
//    public WasteSourcesBean.WasteSourcesLabelBean.ExtentionBean getExtention() {
//        return extention;
//    }
//
//    public void setExtention(WasteSourcesBean.WasteSourcesLabelBean.ExtentionBean extention) {
//        this.extention = extention;
//    }

    public TransactorBean getTransactor() {
        return transactor;
    }

    public void setTransactor(TransactorBean transactor) {
        this.transactor = transactor;
    }

    public CollecterBean getCollector() {
        return collector;
    }

    public void setCollector(CollecterBean collector) {
        this.collector = collector;
    }

    public WasteBean getWaste() {
        return waste;
    }

    public void setWaste(WasteBean waste) {
        this.waste = waste;
    }

    public WasteSourceBean getFrom() {
        return from;
    }

    public void setFrom(WasteSourceBean from) {
        this.from = from;
    }
    public int getCount() {
        return quantity;
    }

    public void setCount(int count) {
        this.quantity = count;
    }
    public String getWasteHouseId() {
        return wasteHouseId;
    }

    public void setWasteHouseId(String wasteHouseId) {
        this.wasteHouseId = wasteHouseId;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getExited() {
        return isExited;
    }

    public void setExited(Boolean exited) {
        isExited = exited;
    }

    public WareHouseBean.ListBean getTo() {
        return to;
    }

    public void setTo(WareHouseBean.ListBean to) {
        this.to = to;
    }

    public double getQuantity() {
        return weight;
    }

    public void setQuantity(double quantity) {
        this.weight = quantity;
    }

    public String getUom() {
        return uow;
    }

    public void setUom(String uom) {
        this.uow = uom;
    }

    public String getStepone() {
        return stepone;
    }

    public void setStepone(String stepone) {
        this.stepone = stepone;
    }

    public List<DetailBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailBean> details) {
        this.details = details;
    }

    public static  class  TransactorBean{
        String name;
        String idCard;
        public TransactorBean(String name,String idCard){
            this.name=name;
            this.idCard=idCard;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId_card() {
            return idCard;
        }

        public void setId_card(String idCard) {
            this.idCard = idCard;
        }
    }

    public static class DetailBean {
        String label;
        double weight;
        String uow;
        boolean isRegister;

        public DetailBean(String label, double quantity, String uom) {
            this.label = label;
            this.weight = quantity;
            this.uow = uom;
        }
        public DetailBean(String label, double quantity, String uow,boolean isRegister) {
            this.label = label;
            this.weight = quantity;
            this.uow = uow;
            this.isRegister=isRegister;
        }

        public boolean isRegister() {
            return isRegister;
        }

        public void setRegister(boolean register) {
            isRegister = register;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public double getQuantity() {
            return weight;
        }

        public void setQuantity(double quantity) {
            this.weight = quantity;
        }

        public String getUom() {
            return uow;
        }

        public void setUom(String uom) {
            this.uow = uom;
        }
    }
    public static  class  CollecterBean{
        String name;
        String idCard;

        public CollecterBean(String name){
            this.name=name;
        }

        public CollecterBean(String name,String idCard){
            this.name=name;
            this.idCard=idCard;
        }

        public String getId_card() {
            return idCard;
        }

        public void setId_card(String idCard) {
            this.idCard = idCard;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class WasteBean {
        String id;
        String number;
        String name;

        public WasteBean() {

        }

        public WasteBean(String id, String number, String name) {
            this.id = id;
            this.number = number;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

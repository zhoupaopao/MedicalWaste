package com.xinchen.medicalwaste.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcx on 2018/6/11.
 */

public class WareHouseBean implements Parcelable {


    /**
     * total : 1
     * list : [{"id":"183350917305730001","name":"暂存库","number":"001","type":"waste","isDefault":1,"administrator":"张三","capacity":100000,"uom":"KG","description":"危废暂存库","locations":null}]
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }


    public static class ListBean implements Parcelable {
        /**
         * id : 183350917305730001
         * name : 暂存库
         * number : 001
         * type : waste
         * isDefault : 1
         * administrator : 张三
         * capacity : 100000.0
         * uom : KG
         * description : 危废暂存库
         * locations : null
         */

        private String id;
        private String name;
        private String number;
        private String type;
        private int isDefault;
        private String administrator;
        private double capacity;
        private String uow;
        private String description;
        private Object locations;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public String getAdministrator() {
            return administrator;
        }

        public void setAdministrator(String administrator) {
            this.administrator = administrator;
        }

        public double getCapacity() {
            return capacity;
        }

        public void setCapacity(double capacity) {
            this.capacity = capacity;
        }

        public String getUom() {
            return uow;
        }

        public void setUom(String uom) {
            this.uow = uom;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Object getLocations() {
            return locations;
        }

        public void setLocations(Object locations) {
            this.locations = locations;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", number='" + number + '\'' +
                    ", type='" + type + '\'' +
                    ", isDefault=" + isDefault +
                    ", administrator='" + administrator + '\'' +
                    ", capacity=" + capacity +
                    ", uom='" + uow + '\'' +
                    ", description='" + description + '\'' +
                    ", locations=" + locations +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.number);
            dest.writeString(this.type);
            dest.writeInt(this.isDefault);
            dest.writeString(this.administrator);
            dest.writeDouble(this.capacity);
            dest.writeString(this.uow);
            dest.writeString(this.description);
            dest.writeParcelable((Parcelable) this.locations, flags);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.number = in.readString();
            this.type = in.readString();
            this.isDefault = in.readInt();
            this.administrator = in.readString();
            this.capacity = in.readDouble();
            this.uow = in.readString();
            this.description = in.readString();
            this.locations = in.readParcelable(Object.class.getClassLoader());
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

    @Override
    public String toString() {
        return "WareHouseBean{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeList(this.list);
    }

    public WareHouseBean() {
    }

    protected WareHouseBean(Parcel in) {
        this.total = in.readInt();
        this.list = new ArrayList<ListBean>();
        in.readList(this.list, ListBean.class.getClassLoader());
    }

    public static final Creator<WareHouseBean> CREATOR = new Creator<WareHouseBean>() {
        @Override
        public WareHouseBean createFromParcel(Parcel source) {
            return new WareHouseBean(source);
        }

        @Override
        public WareHouseBean[] newArray(int size) {
            return new WareHouseBean[size];
        }
    };
}

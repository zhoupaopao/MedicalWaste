package com.xinchen.medicalwaste.bean;

import java.util.List;

public class SubmitResultBean {

    private int code;
    private String msg;
    private BodyBean body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private Object id;
        private String wasteType;
        private String officeName;
        private int totalPackageNumber;
        private double totalWeight;
        private String reviewWeight;
        private String collectPerson;
        private Object createdTime;
        private Object updateTime;
        private String uom;
        private Object tenantId;
        private List<DetailsBean> details;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getWasteType() {
            return wasteType;
        }

        public void setWasteType(String wasteType) {
            this.wasteType = wasteType;
        }

        public String getOfficeName() {
            return officeName;
        }

        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }

        public int getTotalPackageNumber() {
            return totalPackageNumber;
        }

        public void setTotalPackageNumber(int totalPackageNumber) {
            this.totalPackageNumber = totalPackageNumber;
        }

        public double getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(double totalWeight) {
            this.totalWeight = totalWeight;
        }

        public String getReviewWeight() {
            return reviewWeight;
        }

        public void setReviewWeight(String reviewWeight) {
            this.reviewWeight = reviewWeight;
        }

        public String getCollectPerson() {
            return collectPerson;
        }

        public void setCollectPerson(String collectPerson) {
            this.collectPerson = collectPerson;
        }

        public Object getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(Object createdTime) {
            this.createdTime = createdTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getUom() {
            return uom;
        }

        public void setUom(String uom) {
            this.uom = uom;
        }

        public Object getTenantId() {
            return tenantId;
        }

        public void setTenantId(Object tenantId) {
            this.tenantId = tenantId;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public static class DetailsBean {
            private Object id;
            private Object wasteWeightId;
            private double weight;
            private Object wasteType;
            private String tagNumber;
            private String uom;
            private Object tenantId;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public Object getWasteWeightId() {
                return wasteWeightId;
            }

            public void setWasteWeightId(Object wasteWeightId) {
                this.wasteWeightId = wasteWeightId;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public Object getWasteType() {
                return wasteType;
            }

            public void setWasteType(Object wasteType) {
                this.wasteType = wasteType;
            }

            public String getTagNumber() {
                return tagNumber;
            }

            public void setTagNumber(String tagNumber) {
                this.tagNumber = tagNumber;
            }

            public String getUom() {
                return uom;
            }

            public void setUom(String uom) {
                this.uom = uom;
            }

            public Object getTenantId() {
                return tenantId;
            }

            public void setTenantId(Object tenantId) {
                this.tenantId = tenantId;
            }
        }
    }
}

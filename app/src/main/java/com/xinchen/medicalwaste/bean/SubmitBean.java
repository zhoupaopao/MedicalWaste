package com.xinchen.medicalwaste.bean;

import java.io.Serializable;
import java.util.List;

public class SubmitBean implements Serializable {
    private String wasteType;
    private String officeName;
    private int totalPackageNumber;
    private String totalWeight;
    private String reviewWeight;
    private String collectPerson;
    private String uom;
    private List<WeightDetail> details;

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

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public List<WeightDetail> getDetails() {
        return details;
    }

    public void setDetails(List<WeightDetail> details) {
        this.details = details;
    }

    public static class WeightDetail implements Serializable{
        private String tagNumber;
        private String weight;
        private String uom;

        public WeightDetail(String tagNumber, String weight, String uom) {
            this.tagNumber = tagNumber;
            this.weight = weight;
            this.uom = uom;
        }

        public String getTagNumber() {
            return tagNumber;
        }

        public void setTagNumber(String tagNumber) {
            this.tagNumber = tagNumber;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getUom() {
            return uom;
        }

        public void setUom(String uom) {
            this.uom = uom;
        }
    }
}

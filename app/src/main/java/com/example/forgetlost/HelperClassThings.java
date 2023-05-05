package com.example.forgetlost;

public class HelperClassThings {
    String name, describing, conditions, area, imgPath;

    public HelperClassThings(String name, String describing, String conditions, String area, String data, String imgPath) {
        this.name = name;
        this.describing = describing;
        this.conditions = conditions;
        this.area = area;
        this.imgPath = imgPath;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDescribing() {
        return describing;
    }

    public void setDescribing(String describing) {
        this.describing = describing;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}

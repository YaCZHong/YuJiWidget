package com.czh.yujiwidget.bean;

public class City {
    private int id;
    private String cityName;
    private String cityParent;
    private String cityLocation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityParent() {
        return cityParent;
    }

    public void setCityParent(String cityParent) {
        this.cityParent = cityParent;
    }

    public String getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
    }
}

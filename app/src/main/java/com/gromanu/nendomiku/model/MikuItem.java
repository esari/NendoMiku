package com.gromanu.nendomiku.model;

import com.google.gson.annotations.SerializedName;



public class MikuItem {

    @SerializedName("num")
    private int number;

    @SerializedName("name")
    private String name;

    @SerializedName("date")
    private String releaseDate;

    @SerializedName("price")
    private String price;

    @SerializedName("ex")
    private boolean exclusive;

    @SerializedName("pic")
    private String pictureURL;

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public boolean getExclusive() { return exclusive;}
}

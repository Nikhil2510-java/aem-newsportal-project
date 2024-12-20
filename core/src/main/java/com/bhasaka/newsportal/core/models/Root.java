package com.bhasaka.newsportal.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class Root {
    @JsonProperty("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

class Data {
    @JsonProperty("newsinfoList")
    private NewsinfoList newsinfoList;

    public NewsinfoList getNewsinfoList() {
        return newsinfoList;
    }

    public void setNewsinfoList(NewsinfoList newsinfoList) {
        this.newsinfoList = newsinfoList;
    }
}

class NewsinfoList {
    @JsonProperty("items")
    private ArrayList<Item> items;

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}

class Item {
    @JsonProperty("_path")
    private String path;

    @JsonProperty("agencyName")
    private String agencyName;

    @JsonProperty("mobileNo")
    private String mobileNo;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}

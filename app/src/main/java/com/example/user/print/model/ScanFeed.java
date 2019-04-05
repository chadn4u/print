package com.example.user.print.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanFeed {

    @SerializedName("data")
    @Expose
    private ScanDetail data;

    @SerializedName("status")
    @Expose
    private Boolean status;
    public ScanDetail getData() {
        return data;
    }

    public void setData(ScanDetail data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


}

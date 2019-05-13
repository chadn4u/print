package com.example.user.print.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespondProfile {

    @SerializedName("data")
    @Expose
    private RespondProfileDetail data;

    @SerializedName("status")
    @Expose
    private Boolean status;

    public RespondProfileDetail getData() {
        return data;
    }

    public void setData(RespondProfileDetail data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

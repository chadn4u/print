package com.example.user.print.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("EMP_NO")
    @Expose
    private String EMP_NO;

    @SerializedName("EMP_NM")
    @Expose
    private String EMP_NM;

    @SerializedName("STR_CD")
    @Expose
    private String STR_CD;

    @SerializedName("CORP_FG")
    @Expose
    private String CORP_FG;

    public String getEMP_NO() {
        return EMP_NO;
    }

    public void setEMP_NO(String EMP_NO) {
        this.EMP_NO = EMP_NO;
    }

    public String getEMP_NM() {
        return EMP_NM;
    }

    public void setEMP_NM(String EMP_NM) {
        this.EMP_NM = EMP_NM;
    }

    public String getSTR_CD() {
        return STR_CD;
    }

    public void setSTR_CD(String STR_CD) {
        this.STR_CD = STR_CD;
    }

    public String getCORP_FG() {
        return CORP_FG;
    }

    public void setCORP_FG(String CORP_FG) {
        this.CORP_FG = CORP_FG;
    }
}

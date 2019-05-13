package com.example.user.print.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespondProfileDetail {

    @SerializedName("EMP_NO")
    @Expose
    private String emp_no;

    @SerializedName("EMP_NM")
    @Expose
    private String emp_nm;

    @SerializedName("STR_CD")
    @Expose
    private String str_cd;

    @SerializedName("CORP_FG")
    @Expose
    private String corp_fg;

    @SerializedName("JOB_CD")
    @Expose
    private String job_cd;

    @SerializedName("JOB_NM")
    @Expose
    private String job_nm;

    public String getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
    }

    public String getEmp_nm() {
        return emp_nm;
    }

    public void setEmp_nm(String emp_nm) {
        this.emp_nm = emp_nm;
    }

    public String getStr_cd() {
        return str_cd;
    }

    public void setStr_cd(String str_cd) {
        this.str_cd = str_cd;
    }

    public String getCorp_fg() {
        return corp_fg;
    }

    public void setCorp_fg(String corp_fg) {
        this.corp_fg = corp_fg;
    }

    public String getJob_cd() {
        return job_cd;
    }

    public void setJob_cd(String job_cd) {
        this.job_cd = job_cd;
    }

    public String getJob_nm() {
        return job_nm;
    }

    public void setJob_nm(String job_nm) {
        this.job_nm = job_nm;
    }
}

package com.example.user.print.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuFeed {
    @SerializedName("data")
    @Expose
    private List<MenuDetail> data;

    @SerializedName("status")
    @Expose
    private boolean status;

    public List<MenuDetail> getData() {
        return data;
    }

    public void setData(List<MenuDetail> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public class MenuDetail{
        @SerializedName("PARENT_ID")
        @Expose
        private String PARENT_ID;
        @SerializedName("PARENT_NM")
        @Expose
        private String PARENT_NM;

        @SerializedName("CHILD")
        @Expose
        private List<MenuChild> CHILD;

        public MenuDetail(String PARENT_ID, String PARENT_NM, List<MenuChild> CHILD) {
            this.PARENT_ID = PARENT_ID;
            this.PARENT_NM = PARENT_NM;
            this.CHILD = CHILD;
        }

        public String getPARENT_ID() {
            return PARENT_ID;
        }

        public void setPARENT_ID(String PARENT_ID) {
            this.PARENT_ID = PARENT_ID;
        }

        public String getPARENT_NM() {
            return PARENT_NM;
        }

        public void setPARENT_NM(String PARENT_NM) {
            this.PARENT_NM = PARENT_NM;
        }

        public List<MenuChild> getCHILD() {
            return CHILD;
        }

        public void setCHILD(List<MenuChild> CHILD) {
            this.CHILD = CHILD;
        }
    }
    public  class MenuChild{
        @SerializedName("CHILD_ID")
        @Expose
        private String CHILD_ID;

        @SerializedName("CHILD_NM")
        @Expose
        private String CHILD_NM;

        @SerializedName("CHILD_ORD")
        @Expose
        private String CHILD_ORD;

        public String getCHILD_ID() {
            return CHILD_ID;
        }

        public void setCHILD_ID(String CHILD_ID) {
            this.CHILD_ID = CHILD_ID;
        }

        public String getCHILD_NM() {
            return CHILD_NM;
        }

        public void setCHILD_NM(String CHILD_NM) {
            this.CHILD_NM = CHILD_NM;
        }

        public String getCHILD_ORD() {
            return CHILD_ORD;
        }

        public void setCHILD_ORD(String CHILD_ORD) {
            this.CHILD_ORD = CHILD_ORD;
        }
    }

}

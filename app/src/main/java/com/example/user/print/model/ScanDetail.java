package com.example.user.print.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanDetail {

    @SerializedName("PROD_CD")
    @Expose
    private String prod_cd;

    @SerializedName("PROD_NM")
    @Expose
    private String prod_nm;

    @SerializedName("VEN_CD")
    @Expose
    private String ven_cd;

    @SerializedName("VEN_NM")
    @Expose
    private String ven_nm;

    @SerializedName("SALE_PRC")
    @Expose
    private String sale_prc;

    @SerializedName("DEAL_STOP_FG")
    @Expose
    private String deal_stop_fg;

    @SerializedName("DEL_STOP_FG")
    @Expose
    private String del_stop_fg;

    @SerializedName("SALE_STOCK")
    @Expose
    private String sale_stock;

    @SerializedName("BOOK_STOCK")
    @Expose
    private String book_stock;

    @SerializedName("STATUS_PRODUCT")
    @Expose
    private String status_product;

    @SerializedName("STATUS_PRINT")
    @Expose
    private String status_print;


    public String getProd_cd() {
        return prod_cd;
    }

    public void setProd_cd(String prod_cd) {
        this.prod_cd = prod_cd;
    }

    public String getProd_nm() {
        return prod_nm;
    }

    public void setProd_nm(String prod_nm) {
        this.prod_nm = prod_nm;
    }

    public String getVen_cd() {
        return ven_cd;
    }

    public void setVen_cd(String ven_cd) {
        this.ven_cd = ven_cd;
    }

    public String getVen_nm() {
        return ven_nm;
    }

    public void setVen_nm(String ven_nm) {
        this.ven_nm = ven_nm;
    }

    public String getSale_prc() {
        return sale_prc;
    }

    public void setSale_prc(String sale_prc) {
        this.sale_prc = sale_prc;
    }

    public String getDeal_stop_fg() {
        return deal_stop_fg;
    }

    public void setDeal_stop_fg(String deal_stop_fg) {
        this.deal_stop_fg = deal_stop_fg;
    }

    public String getDel_stop_fg() {
        return del_stop_fg;
    }

    public void setDel_stop_fg(String del_stop_fg) {
        this.del_stop_fg = del_stop_fg;
    }

    public String getSale_stock() {
        return sale_stock;
    }

    public void setSale_stock(String sale_stock) {
        this.sale_stock = sale_stock;
    }

    public String getBook_stock() {
        return book_stock;
    }

    public void setBook_stock(String book_stock) {
        this.book_stock = book_stock;
    }

    public String getStatus_product() {
        return status_product;
    }

    public void setStatus_product(String status_product) {
        this.status_product = status_product;
    }

    public String getStatus_print() {
        return status_print;
    }

    public void setStatus_print(String status_print) {
        this.status_print = status_print;
    }

}

package com.example.user.print.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanDetail implements Parcelable {

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

    @SerializedName("POS_PRC")
    @Expose
    private String pos_prc;

    @SerializedName("ON_ORD_QTY")
    @Expose
    private String on_ord_qty;

    @SerializedName("PACKS")
    @Expose
    private String packs;

    @SerializedName("PROTECT_TAG")
    @Expose
    private String protect_tag;

    @SerializedName("CONDI_SRCMK")
    @Expose
    private String condi_srcmk;

    protected ScanDetail(Parcel in) {
        prod_cd = in.readString();
        prod_nm = in.readString();
        ven_cd = in.readString();
        ven_nm = in.readString();
        sale_prc = in.readString();
        deal_stop_fg = in.readString();
        del_stop_fg = in.readString();
        sale_stock = in.readString();
        book_stock = in.readString();
        status_product = in.readString();
        status_print = in.readString();
        pos_prc = in.readString();
        on_ord_qty = in.readString();
        packs = in.readString();
        protect_tag = in.readString();
        condi_srcmk = in.readString();
    }

    public static final Creator<ScanDetail> CREATOR = new Creator<ScanDetail>() {
        @Override
        public ScanDetail createFromParcel(Parcel in) {
            return new ScanDetail(in);
        }

        @Override
        public ScanDetail[] newArray(int size) {
            return new ScanDetail[size];
        }
    };

    public String getPos_prc() {
        return pos_prc;
    }

    public void setPos_prc(String pos_prc) {
        this.pos_prc = pos_prc;
    }

    public String getOn_ord_qty() {
        return on_ord_qty;
    }

    public void setOn_ord_qty(String on_ord_qty) {
        this.on_ord_qty = on_ord_qty;
    }

    public String getPacks() {
        return packs;
    }

    public void setPacks(String packs) {
        this.packs = packs;
    }

    public String getProtect_tag() {
        return protect_tag;
    }

    public void setProtect_tag(String protect_tag) {
        this.protect_tag = protect_tag;
    }

    public String getCondi_srcmk() {
        return condi_srcmk;
    }

    public void setCondi_srcmk(String condi_srcmk) {
        this.condi_srcmk = condi_srcmk;
    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prod_cd);
        dest.writeString(prod_nm);
        dest.writeString(ven_cd);
        dest.writeString(ven_nm);
        dest.writeString(sale_prc);
        dest.writeString(deal_stop_fg);
        dest.writeString(del_stop_fg);
        dest.writeString(sale_stock);
        dest.writeString(book_stock);
        dest.writeString(status_product);
        dest.writeString(status_print);
        dest.writeString(pos_prc);
        dest.writeString(on_ord_qty);
        dest.writeString(packs);
        dest.writeString(protect_tag);
        dest.writeString(condi_srcmk);
    }
}

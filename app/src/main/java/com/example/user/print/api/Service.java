package com.example.user.print.api;

import com.example.user.print.model.AuthorizeToken;
import com.example.user.print.model.LoginFeed;
import com.example.user.print.model.RespondStatus;
import com.example.user.print.model.ScanFeed;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Service {

    //X-API-KEY : 026dabaf69302a0b44b14978b16b4fd3
    @Headers("X-API-KEY: 026dabaf69302a0b44b14978b16b4fd3")
    @POST("api/gmd/user")
    @FormUrlEncoded
    Call<LoginFeed> getLogin(@Field("username") String username,@Field("password") String Password);

    @POST("password_credentials")
    @FormUrlEncoded
    Call<AuthorizeToken> getToken(@FieldMap HashMap<String,String> data);

    @POST("refresh_token")
    @FormUrlEncoded
    Call<AuthorizeToken> getRefreshToken(@Body HashMap<String,String> data);

    @Headers("X-API-KEY: 026dabaf69302a0b44b14978b16b4fd3")
    @POST("api/gmd/scan")
    @FormUrlEncoded
    Call<ScanFeed> getScanDetail(@Field("barcode") String barcode
            , @Field("corp") String corp
            , @Field("store") String store);

    @Headers("X-API-KEY: 026dabaf69302a0b44b14978b16b4fd3")
    @POST("api/gmd/print")
    @FormUrlEncoded
    Call<RespondStatus> savePrint(@Field("store") String storeCD
            , @Field("barcode") String barcode
            , @Field("qty") String qty
            , @Field("emp_no") String emp_no);

}

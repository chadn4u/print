package com.example.user.print.api;

import android.content.Context;

import com.example.user.print.util.InterceptorAuth;
import com.example.user.print.util.SessionManagement;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientWithToken {
    private Service service;
    private String baseUrl;
    private SessionManagement sessionManagement;

    public ClientWithToken(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Service getClientWithToken(Context mContext){
        sessionManagement = new SessionManagement(mContext);
        if(service == null){
            Client client = new Client("http://frontier.lottemart.co.id/authorize/");
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.connectTimeout(30, TimeUnit.SECONDS);
            clientBuilder.readTimeout(30,TimeUnit.SECONDS);
            clientBuilder.writeTimeout(30,TimeUnit.SECONDS);
            clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization","Bearer "+ sessionManagement.getTokenAccess())
                            .addHeader("Accept","application/json")
                            .build();

                    return chain.proceed(newRequest);
                }
            });
            clientBuilder.addInterceptor(new InterceptorAuth(client.getClient(),mContext));
            clientBuilder.addInterceptor(loggingInterceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(clientBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(Service.class);
        }
        return service;
    }

}

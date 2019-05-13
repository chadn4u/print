package com.example.user.print.util;

import android.content.Context;

import com.example.user.print.api.Client;
import com.example.user.print.api.Service;
import com.example.user.print.model.AuthorizeToken;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class InterceptorAuth implements Interceptor {
    private Service apiService;
    private Client client;
    private Context mContext;
    private SessionManagement sessionManagement;

    public InterceptorAuth(Service apiService,Context mContext) {
        this.apiService = apiService;
        this.mContext = mContext;
        sessionManagement = new SessionManagement(mContext);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response mainResponse = chain.proceed(chain.request());
        Request mainRequest = chain.request();

        if (sessionManagement.checkSharedPreferences("Accesstoken")) {
            if (mainResponse.code() == 401 || mainResponse.code() == 403) {

                HashMap<String,String> authorizeData = new HashMap<>();
                authorizeData.put("grant_type","refresh_token");
                authorizeData.put("refresh_token",sessionManagement.getTokenRefresh());
                authorizeData.put("client_id","testclient");
                authorizeData.put("client_secret","testpass");

                Call<AuthorizeToken> refreshTOken = apiService.getRefreshToken(authorizeData);

                refreshTOken.enqueue(new Callback<AuthorizeToken>() {
                    @Override
                    public void onResponse(Call<AuthorizeToken> call, retrofit2.Response<AuthorizeToken> response) {
                        if (response.isSuccessful()) {
                            sessionManagement.saveTokenRefresh(response.body().getRefresh_token());
                            sessionManagement.saveTokenAccess(response.body().getAccess_token());


                        }
                    }

                    @Override
                    public void onFailure(Call<AuthorizeToken> call, Throwable t) {

                    }
                });

                Request.Builder builder = mainRequest.newBuilder()
                                            .header("Authorization","Bearer "+ sessionManagement.getTokenAccess())
                                            .method(mainRequest.method(),mainRequest.body());
                mainResponse = chain.proceed(builder.build());

            }
        }


        return mainResponse;
    }
}

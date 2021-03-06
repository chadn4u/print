package com.example.user.print;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.print.api.ClientWithToken;
import com.example.user.print.api.Service;
import com.example.user.print.model.AuthorizeToken;
import com.example.user.print.model.LoginFeed;
import com.example.user.print.model.RespondProfile;
import com.example.user.print.util.NumericKeyboardTransformationMethod;
import com.example.user.print.util.SessionManagement;
import com.example.user.print.util.SetupUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Button buttonLogin;
    private EditText userId;
    private EditText passwd;
    private Context mContext;
    private SessionManagement sessionManagement;
    private ProgressDialog pd;
    private ClientWithToken clientWithToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout_login);
        mContext = getApplicationContext();
        sessionManagement = new SessionManagement(mContext);

        if(sessionManagement.checkSharedPreferences("EMP_NM")){
            Intent intent=new Intent(getApplicationContext(), Main2Activity.class);
            startActivity(intent);
            finish();
        }
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Please Wait...");

        buttonLogin = findViewById(R.id.buttonLogin);
        userId = findViewById(R.id.editUserID);
        passwd = findViewById(R.id.editUserPasswd);

        userId.setTransformationMethod(new NumericKeyboardTransformationMethod());



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateLogin()){
                    pd.show();
                    loadJson();
                }

            }
        });

    }

    private void loadJson(){

        HashMap<String,String> mapData = new HashMap<>();
        mapData.put("grant_type","password");
        mapData.put("username",userId.getText().toString());
        mapData.put("password",passwd.getText().toString());
        mapData.put("client_secret","testpass");
        mapData.put("client_id","testclient");

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
//        application/x-www-form-urlencoded
        //http://frontier.lottemart.co.id/authorize/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frontier.lottemart.co.id/authorize/")
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service serviceAPI = retrofit.create(Service.class);
        Call<AuthorizeToken> call = serviceAPI.getToken(mapData);

        call.enqueue(new Callback<AuthorizeToken>() {
            @Override
            public void onResponse(Call<AuthorizeToken> call, Response<AuthorizeToken> response) {



                if(!response.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this,"Terjadi kesalahan pada server",Toast.LENGTH_SHORT).show();
                }
                else{
//                    Log.d(TAG, "onResponse: STATUS"+response.body().getStatus());
                    if(response.isSuccessful()){
//                        Log.d(TAG, "onResponse: EMP_NO"+response.body().getData().getEMP_NO());
//
//
                        HashMap<String,String> sessionValue = new HashMap<>();
//                        sessionValue.put("EMP_NM",response.body().getData().getEMP_NM());
//                        sessionValue.put("EMP_NO",response.body().getData().getEMP_NO());
//                        sessionValue.put("STR_CD",response.body().getData().getSTR_CD());
//                        sessionValue.put("CORP_FG",response.body().getData().getCORP_FG());

//                        sessionManagement.saveSharedPreferencesString(sessionValue);
                        sessionManagement.saveTokenAccess(response.body().getAccess_token());
                        sessionManagement.saveTokenRefresh(response.body().getRefresh_token());
                        Log.i(TAG, "loadJsonProfile: "+ sessionManagement.getTokenAccess());
                        Log.i(TAG, "loadJsonProfile2: "+sessionManagement.checkSharedPreferences("Accesstoken"));
//                        SetupUtil setupUtil= new SetupUtil();
//                        setupUtil.setIntent(getApplicationContext(),Main2Activity.class,setupUtil.getActivity(getApplicationContext()));

                        loadJsonProfile();
//                        Intent intent=new Intent(getApplicationContext(), Main2Activity.class);
//                        startActivity(intent);
//                        finish();
                    }else
                    {   pd.dismiss();
                        Toast.makeText(LoginActivity.this,"Username / Password Salah",Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(Call<AuthorizeToken> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadJsonProfile(){
        clientWithToken = new ClientWithToken("http://frontier.lottemart.co.id/code/V2/");
        Service serviceProfile = clientWithToken.getClientWithToken(this);

        Call<RespondProfile> call = serviceProfile.getProfile();

        call.enqueue(new Callback<RespondProfile>() {
            @Override
            public void onResponse(Call<RespondProfile> call, Response<RespondProfile> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"E101: Login Failed",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                else{
                    if (response.body().getStatus()) {
                        HashMap<String,String> sessionValue = new HashMap<>();
                        sessionValue.put("EMP_NM",response.body().getData().getEmp_nm());
                        sessionValue.put("EMP_NO",response.body().getData().getEmp_no());
                        sessionValue.put("STR_CD",response.body().getData().getStr_cd());
                        sessionValue.put("CORP_FG",response.body().getData().getCorp_fg());

                        sessionManagement.saveSharedPreferencesString(sessionValue);

                        pd.dismiss();
                        Intent intent=new Intent(getApplicationContext(), Main2Activity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"E102: Login Failed",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<RespondProfile> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"E103: Login Failed",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

     private boolean validateLogin(){
        if(userId.getText().equals("") || userId.getText().toString().isEmpty() ){
            Toast.makeText(getApplicationContext(),"User ID tidak boleh kosong!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(passwd.getText().equals("") || passwd.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Password tidak boleh kosong",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

}

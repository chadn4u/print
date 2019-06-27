package com.example.user.print;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.print.api.Client;
import com.example.user.print.api.ClientWithToken;
import com.example.user.print.api.Service;
import com.example.user.print.model.LoginDtl;
import com.example.user.print.model.LoginFeed;
import com.example.user.print.model.RespondStatus;
import com.example.user.print.model.ScanDetail;
import com.example.user.print.model.ScanFeed;
import com.example.user.print.util.SessionManagement;
import com.example.user.print.util.SetupUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Etc_Management_Activity extends AppCompatActivity {
    private static final String TAG = "Etc_Management_Activity";
    private FloatingActionButton floatingActionButton;
    private static Button buttonSave,buttonWaste,buttonShortage;
    private static Context mContext;
    TextView productCode,productName,vendorName,posPrice,requestQty,salesStock,bookStock,itemCode;
    String username;//i.getStringExtra("EMP_NM");
    String userID ;
    String strCd ;
    String corpFG;
    String result;
    boolean flagToast = false;
    String statusPrint;
    ClientWithToken client;
    private SetupUtil setupUtil;
    private ImageButton incrementButton,decrementButton;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etc_man_layout1);

        mContext = Etc_Management_Activity.this;
        setupUtil = new SetupUtil();
/**
 * findViewByID
  */
//        floatingActionButton = findViewById(R.id.scan);
        buttonSave = findViewById(R.id.buttonTagPrint);
        buttonShortage = findViewById(R.id.buttonShortage);
        buttonWaste = findViewById(R.id.buttonWaste);
        itemCode = findViewById(R.id.barcode);
        productCode  = findViewById(R.id.productCode);
        productName  = findViewById(R.id.productName);
        vendorName = findViewById(R.id.vendorName);
        posPrice = findViewById(R.id.posPrice);
        requestQty= findViewById(R.id.requestQty);
        salesStock = findViewById(R.id.salesStock);
        bookStock = findViewById(R.id.bookStock);
        incrementButton = findViewById(R.id.btnPlus);
        decrementButton = findViewById(R.id.btnMinus);

        enableDisableButton(buttonShortage,false);
        enableDisableButton(buttonWaste,false);
/**
 * End of findViewByID
 */
        incrementButton.setEnabled(false);
        decrementButton.setEnabled(false);

/**
 * get SharedPreferences
 */
        sessionManagement = new SessionManagement(getApplicationContext());
        username = sessionManagement.getSharedPreferences("EMP_NM","");//i.getStringExtra("EMP_NM");//i.getStringExtra("EMP_NM");
        userID = sessionManagement.getSharedPreferences("EMP_NO","");//i.getStringExtra("EMP_NO");
        strCd = sessionManagement.getSharedPreferences("STR_CD","");//.getStringExtra("STR_CD");
        corpFG = sessionManagement.getSharedPreferences("CORP_FG","");//.getStringExtra("CORP_FG");

        result = getIntent().getStringExtra("result");
        Log.d(TAG, "onCreate: "+result);
        itemCode.setText(result);
/**
 * end of Get Intent
 */
        loadJson();
/**
 *  itemCode oNTextChangeListener
 */
        itemCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!itemCode.getText().toString().isEmpty() || !itemCode.getText().toString().equals(""))
                {
                    client = new ClientWithToken("http://frontier.lottemart.co.id/code/V2/");
                    Service serviceAPI = client.getClientWithToken(Etc_Management_Activity.this);
                    Call<ScanFeed> call = serviceAPI.getScanDetail(itemCode.getText().toString(),corpFG,strCd);

                    call.enqueue(new Callback<ScanFeed>() {
                        @Override
                        public void onResponse(Call<ScanFeed> call, Response<ScanFeed> response) {

                            if(!response.isSuccessful()){
                                setupUtil.showToast(mContext,"Terjadi Kesalahan pada server",0);
                                //Toast.makeText(mContext,"Terjadi Kesalahan pada server",Toast.LENGTH_SHORT).show();
                            }
                            else{

                                if(response.body().getStatus()) {

                                    productCode.setText(response.body().getData().getProd_cd());
                                    productName.setText(response.body().getData().getProd_nm());
                                    vendorName.setText(response.body().getData().getVen_nm());
                                    posPrice.setText(response.body().getData().getSale_prc());
                                    requestQty.setText("0");
                                    bookStock.setText(response.body().getData().getBook_stock());
                                    salesStock.setText(response.body().getData().getSale_stock());
                                    statusPrint = response.body().getData().getStatus_print();
//                                    buttonSave.setEnabled(true);
//                                    enableDisableEditText(requestQty,true);

                                    enableDisableButton(buttonSave,true);
                                    enableDisableButton(incrementButton,true);
                                    enableDisableButton(decrementButton,true);

//                                    requestQty.setEnabled(true);
                                }
                                else
                                {
                                    setupUtil.showToast(mContext,"Product tidak terdaftar",0);
//                                    Toast.makeText(mContext,"Product tidak terdaftar",Toast.LENGTH_SHORT).show();
                                    clearEditText();

//                                    requestQty.setEnabled(false);
//                                    enableDisableEditText(requestQty,false);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<ScanFeed> call, Throwable t) {
                            setupUtil.showToast(mContext,"Terjadi Kesalahan pada server",0);
//                            Toast.makeText(mContext,"Terjadi Kesalahan pada server",Toast.LENGTH_SHORT).show();
                        }

                    });
                }

            }
        });
/**
 * end of onKeyChangeListener
 */

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setupUtil.setIntentWithoutFinish(mContext,ScannerActivity.class,setupUtil.getActivity(mContext));
////                startActivity(new Intent(getApplicationContext(),ScannerActivity.class));
//            }
//        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                if(Integer.parseInt(requestQty.getText().toString().trim()) >=  99999){
                    i = 99999;
                }
                else{
                    i = Integer.parseInt(requestQty.getText().toString().trim()) + 1;
                }
                requestQty.setText(Integer.toString(i));
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                if(Integer.parseInt(requestQty.getText().toString().trim()) <= 0 ){
                    i = 0;
                }
                else{
                    i = Integer.parseInt(requestQty.getText().toString().trim()) - 1;
                }
                requestQty.setText(Integer.toString(i));
            }
        });
        buttonShortage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Fungsi belum tersedia",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        });
        buttonWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Fungsi belum tersedia",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusPrint.equals("NOT")){
                    setupUtil.showToast(mContext,"Product tidak bisa di print",0);
//                    Toast.makeText(Etc_Management_Activity.this,"Product tidak bisa di print",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(requestQty.getText().toString().equals("")||requestQty.getText().toString().isEmpty()){
                        setupUtil.showToast(mContext,"Qty belum di input!",0);
//                        Toast.makeText(Etc_Management_Activity.this,"Qty belum di input!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Integer.parseInt(requestQty.getText().toString()) == 0){
                        setupUtil.showToast(mContext,"Wrong qty!",0);
                        return;
                    }else if(Integer.parseInt(requestQty.getText().toString())>= 9999){
                        setupUtil.showToast(mContext,"Too many qty!",0);
                        return;
                    }

                    client = new ClientWithToken("http://frontier.lottemart.co.id/purchase/V2/");

                    Service serviceAPI = client.getClientWithToken(Etc_Management_Activity.this);
                    Call<RespondStatus> call = serviceAPI.savePrint(strCd
                                            ,itemCode.getText().toString()
                                            ,requestQty.getText().toString()
                                            ,userID);

                    call.enqueue(new Callback<RespondStatus>() {
                        @Override
                        public void onResponse(Call<RespondStatus> call, Response<RespondStatus> response) {
                            if(!response.isSuccessful()){
                                setupUtil.showToast(mContext,"Terjadi Kesalahan pada server",0);
//                                Toast.makeText(mContext,"Terjadi Kesalahan pada server",Toast.LENGTH_SHORT).show();
                            }
                            else{

                                if(response.body().getStatus()) {
                                    clearEditText();

//                                    enableDisableEditText(requestQty,false);
                                    enableDisableButton(buttonSave,false);
                                    enableDisableButton(incrementButton,false);
                                    enableDisableButton(decrementButton,false);
//                                    Toast.makeText(mContext,"Sukses di simpan!!",Toast.LENGTH_SHORT).show();
                                    setupUtil.showToast(mContext,"Sukses di simpan!!",0);
                                    finish();
                                }
                                else
                                {
                                    clearEditText();

//                                    enableDisableEditText(requestQty,false);
                                    enableDisableButton(buttonSave,false);
                                    enableDisableButton(incrementButton,false);
                                    enableDisableButton(decrementButton,false);
//                                    requestQty.setEnabled(false);
//                                    buttonSave.setEnabled(false);
//                                    Toast.makeText(mContext,"Gagal di simpan!!",Toast.LENGTH_SHORT).show();
                                    setupUtil.showToast(mContext,"Gagal di simpan!!",0);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RespondStatus> call, Throwable t) {
//                           Toast.makeText(mContext,"Terjadi Kesalahan pada server",Toast.LENGTH_SHORT).show();
                            setupUtil.showToast(mContext,"Terjadi Kesalahan pada server",0);

                        }
                    });


                }
            }
        });
    }
    private void clearEditText(){
        itemCode.setText("");
        productCode.setText("");
        productName.setText("");
        vendorName.setText("");
        posPrice.setText("");
        requestQty.setText("");
        salesStock.setText("");
        bookStock.setText("");
    }
    private void enableDisableButton(ImageButton buttonId,boolean status){
        if (buttonId instanceof ImageButton){
            buttonId.setEnabled(status);
        }
    }
    private void enableDisableButton(Button buttonId,boolean status){
        if (buttonId instanceof Button){
            buttonId.setEnabled(status);
            if (!status)
            buttonId.setTextColor(getResources().getColor(android.R.color.black));
        }
    }
    private void enableDisableEditText(TextView etId,boolean status){
        if (etId instanceof TextView){
            etId.setEnabled(status);
        }
    }
    private void loadJson(){
        if(!itemCode.getText().toString().isEmpty() || !itemCode.getText().toString().equals(""))
        {
            client = new ClientWithToken("http://frontier.lottemart.co.id/code/V2/");
            Service serviceAPI = client.getClientWithToken(Etc_Management_Activity.this);
            Call<ScanFeed> call = serviceAPI.getScanDetail(itemCode.getText().toString(),corpFG,strCd);

            call.enqueue(new Callback<ScanFeed>() {
                @Override
                public void onResponse(Call<ScanFeed> call, Response<ScanFeed> response) {

                    if(!response.isSuccessful()){
                        setupUtil.showToast(mContext,"Terjadi Kesalahan pada server",0);
                        //Toast.makeText(mContext,"Terjadi Kesalahan pada server",Toast.LENGTH_SHORT).show();
                    }
                    else{

                        if(response.body().getStatus()) {

                            productCode.setText(response.body().getData().getProd_cd());
                            productName.setText(response.body().getData().getProd_nm());
                            vendorName.setText(response.body().getData().getVen_nm());
                            posPrice.setText(response.body().getData().getSale_prc());
                            requestQty.setText("0");
                            bookStock.setText(response.body().getData().getBook_stock());
                            salesStock.setText(response.body().getData().getSale_stock());
                            statusPrint = response.body().getData().getStatus_print();
//                                    buttonSave.setEnabled(true);
//                            enableDisableEditText(requestQty,true);

                            enableDisableButton(buttonSave,true);
                            enableDisableButton(incrementButton,true);
                            enableDisableButton(decrementButton,true);

//                                    requestQty.setEnabled(true);
                        }
                        else
                        {
                            setupUtil.showToast(mContext,"Product tidak terdaftar",0);
//                                    Toast.makeText(mContext,"Product tidak terdaftar",Toast.LENGTH_SHORT).show();
                            clearEditText();

//                                    requestQty.setEnabled(false);
//                            enableDisableEditText(requestQty,false);
                        }
                    }

                }

                @Override
                public void onFailure(Call<ScanFeed> call, Throwable t) {
                    setupUtil.showToast(mContext,"Terjadi Kesalahan pada server",0);
//                            Toast.makeText(mContext,"Terjadi Kesalahan pada server",Toast.LENGTH_SHORT).show();
                }

            });
        }
    }
}

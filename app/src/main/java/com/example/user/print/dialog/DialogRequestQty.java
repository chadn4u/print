package com.example.user.print.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.print.R;
import com.example.user.print.api.ClientWithToken;
import com.example.user.print.api.Service;
import com.example.user.print.model.RespondStatus;
import com.example.user.print.model.ScanDetail;
import com.example.user.print.util.SessionManagement;
import com.example.user.print.util.SetupUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class DialogRequestQty extends DialogFragment {
    private Context mContext;

    private TextView requestQtyDialog;
    private ImageButton imgBtnPlus,imgBtnMinus;
    private Button btnSave;
    private SetupUtil setupUtil= new SetupUtil();
    private ScanDetail scanDetail;

    private String strCd,corpFG,barcodeCd,userID;
    private SessionManagement sessionManagement;

    ClientWithToken client;
    public interface OnRequestQty{
            void requestQty(Integer requestQty);
    }

    public OnRequestQty onRequestQty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_request_qty,container,false);
        requestQtyDialog = view.findViewById(R.id.requestQtyDialog);
        imgBtnPlus = view.findViewById(R.id.imgBtnPlus);
        imgBtnMinus = view.findViewById(R.id.imgBtnMinus);
        btnSave = view.findViewById(R.id.btnSave);

        sessionManagement = new SessionManagement(getContext());

        scanDetail = getArguments().getParcelable("data");
        barcodeCd = getArguments().getString("barcode");

        imgBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                if(Integer.parseInt(requestQtyDialog.getText().toString().trim()) >=  99999){
                    i = 99999;
                }
                else{
                    i = Integer.parseInt(requestQtyDialog.getText().toString().trim()) + 1;
                }
                requestQtyDialog.setText(Integer.toString(i));
            }
        });

        imgBtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                if(Integer.parseInt(requestQtyDialog.getText().toString().trim()) <= 0 ){
                    i = 0;
                }
                else{
                    i = Integer.parseInt(requestQtyDialog.getText().toString().trim()) - 1;
                }
                requestQtyDialog.setText(Integer.toString(i));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scanDetail.getStatus_print().equals("NOT")){
                    setupUtil.showToast(getContext(),"Product tidak bisa di print",0);
                }
                else{
                    if(requestQtyDialog.getText().toString().equals("")||requestQtyDialog.getText().toString().isEmpty()){
                        setupUtil.showToast(getContext(),"Qty belum di input!",0);
                        return;
                    }
                    if (Integer.parseInt(requestQtyDialog.getText().toString()) == 0){
                        setupUtil.showToast(getContext(),"Wrong qty!",0);
                        return;
                    }else if(Integer.parseInt(requestQtyDialog.getText().toString())>= 9999){
                        setupUtil.showToast(getContext(),"Too many qty!",0);
                        return;
                    }
                    else if(Integer.parseInt(requestQtyDialog.getText().toString())< 0){
                        setupUtil.showToast(getContext(),"Wrong qty!",0);
                        return;
                    }
                    save(requestQtyDialog.getText().toString());

                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onRequestQty = (DialogRequestQty.OnRequestQty) getActivity();
            mContext = context;
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: "+ e.getMessage() );
        }
    }

    private void save(String requestQty){
        strCd = sessionManagement.getSharedPreferences("STR_CD","");//.getStringExtra("STR_CD");
        corpFG = sessionManagement.getSharedPreferences("CORP_FG","");//.getStringExtra("CORP_FG");
        userID = sessionManagement.getSharedPreferences("EMP_NO","");//i.getStringExtra("EMP_NO");

        client = new ClientWithToken("http://frontier.lottemart.co.id/purchase/V2/");

        Service serviceAPI = client.getClientWithToken(getActivity());
        Call<RespondStatus> call = serviceAPI.savePrint(strCd
                ,barcodeCd
                ,requestQty
                ,userID);

        call.enqueue(new Callback<RespondStatus>() {
            @Override
            public void onResponse(Call<RespondStatus> call, Response<RespondStatus> response) {
                if(!response.isSuccessful()){
                    if (response.code() == 401){
                        setupUtil.showToast(getContext(),"Token Expired, silahkan save ulang",0);
                    }else{
                        setupUtil.showToast(getContext(),"Server response : "+response.code(),0);
                    }
                }
                else{

                    if(response.body().getStatus()) {

                        setupUtil.showToast(getContext(),"Sukses di simpan!!",0);
                    }
                    else
                    {
                        setupUtil.showToast(getContext(),"Gagal di simpan!!",0);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespondStatus> call, Throwable t) {
//                           Toast.makeText(mContext,"Terjadi Kesalahan pada server",Toast.LENGTH_SHORT).show();
                setupUtil.showToast(getContext(),t.getMessage(),0);

            }
        });
    }
}

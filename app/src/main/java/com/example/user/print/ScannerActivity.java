package com.example.user.print;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.user.print.util.SetupUtil;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "ScannerActivity";
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    ZXingScannerView zXingScannerView;

    private static String strCd ;
    private static String corpFG;
    private SetupUtil setupUtil = new SetupUtil();
    @Override
    public void handleResult(Result result) {
//        Intent i = getIntent();
//        strCd = i.getStringExtra("STR_CD");
//        corpFG = i.getStringExtra("CORP_FG");
        HashMap<String,String> mapResult = new HashMap<>();
        mapResult.put("result",result.getText());
        setupUtil.setIntentStr(this,Etc_Management_Activity.class,this,mapResult);
//        Etc_Management_Activity.itemCode.setText(result.getText());
//        ((TagPrintRequestFragment) tagPrintRequestFragment).setItemCode(result.getText());
//        onFragmentInteraction(result.getText());
//        Etc_Management_Activity.callAPI(result.getText());
//        onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zXingScannerView = new ZXingScannerView(this);
        // Camera Permission
        int permissionCamera = ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA);

        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            checkPermissions();
        }

        setContentView(zXingScannerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(ScannerActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissions) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;

                        }

                    }
                    // Show permissionsDenied
//                    onBackPressed();
                }
                return;
            }
        }
    }



}

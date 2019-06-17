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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.print.util.SetupUtil;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler,BarcodeCallback {
    private static final String TAG = "ScannerActivity";
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    ZXingScannerView zXingScannerView;

    private static String strCd ;
    private static String corpFG;
    private SetupUtil setupUtil = new SetupUtil();
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private MaterialSearchView searchView;


    @Override
    public void handleResult(Result result) {
//        Intent i = getIntent();
//        strCd = i.getStringExtra("STR_CD");
//        corpFG = i.getStringExtra("CORP_FG");
        HashMap<String,String> mapResult = new HashMap<>();
        mapResult.put("result",result.getText());
        Log.d(TAG, "handleResult: "+result.getText());
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

//        setContentView(zXingScannerView);
        setContentView(R.layout.scan_layout);
        Toolbar toolbar = findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Scan Barcode");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                HashMap<String,String> mapResult = new HashMap<>();
                mapResult.put("result",query);
                setupUtil.setIntentStr(ScannerActivity.this,Etc_Management_Activity.class,ScannerActivity.this,mapResult);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);


        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
        barcodeScannerView.decodeSingle(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        zXingScannerView.stopCamera();
        capture.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();

//        zXingScannerView.setResultHandler(this);
//        zXingScannerView.startCamera();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        HashMap<String,String> mapResult = new HashMap<>();
        mapResult.put("result",result.getText());
        Log.d(TAG, "handleResult: "+result.getText());
        setupUtil.setIntentStr(ScannerActivity.this,Etc_Management_Activity.class,ScannerActivity.this,mapResult);
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {

    }
}

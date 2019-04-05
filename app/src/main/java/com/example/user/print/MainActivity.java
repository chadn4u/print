package com.example.user.print;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
   private CardView cardView;
    private Context mContext;
    private TextView userText;
    private String username;//i.getStringExtra("EMP_NM");
    private String userID ;
    private String strCd ;
    private String corpFG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        Intent i = getIntent();
        username = i.getStringExtra("EMP_NM");//i.getStringExtra("EMP_NM");
         userID = i.getStringExtra("EMP_NO");
         strCd = i.getStringExtra("STR_CD");
        corpFG = i.getStringExtra("CORP_FG");

        userText = findViewById(R.id.userNameTextView);
        userText.setText(username);
//        Log.d(TAG, "initContent: "+username);
        initContent();
    }

    @SuppressLint("WrongViewCast")
    private void initContent(){

        cardView = findViewById(R.id.cardViewETCmanagement);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mContext, Etc_Management_Activity.class);
                intent.putExtra("menu","ETC_MANAGEMENT");
                intent.putExtra("EMP_NM",username);
                intent.putExtra("EMP_NO",userID);
                intent.putExtra("STR_CD",strCd);
                intent.putExtra("CORP_FG",corpFG);
                startActivity(intent);
            }
        });
    }
}

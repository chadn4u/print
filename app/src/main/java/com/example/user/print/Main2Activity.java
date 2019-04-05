package com.example.user.print;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.user.print.util.SessionManagement;
import com.example.user.print.util.SetupUtil;

import java.util.HashMap;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Main2Activity";
    private String username;//i.getStringExtra("EMP_NM");
    private String userID ;
    private String strCd ;
    private String corpFG;
    private SetupUtil setupUtil;
    private TextView userIDTextView;
    private TextView strCodeTextView;
    private ImageView imageViewProfile;
    private HashMap<String,String> intentMap = new HashMap<>();
    private SessionManagement sessionManagement;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        sessionManagement = new SessionManagement(getApplicationContext());


//        Intent i = getIntent();
        username = sessionManagement.getSharedPreferences("EMP_NM","");//i.getStringExtra("EMP_NM");//i.getStringExtra("EMP_NM");
        userID = sessionManagement.getSharedPreferences("EMP_NO","");//i.getStringExtra("EMP_NO");
        strCd = sessionManagement.getSharedPreferences("STR_CD","");//.getStringExtra("STR_CD");
        corpFG = sessionManagement.getSharedPreferences("CORP_FG","");//.getStringExtra("CORP_FG");

//        Log.d(TAG, "onCreate: " + username);

        setProfile();

//        intentMap.put("EMP_NO",userID);
//        intentMap.put("EMP_NM",username);
//        intentMap.put("STR_CD",strCd);
//        intentMap.put("CORP_FG",corpFG);

        setupUtil = new SetupUtil();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        displayMenu();
    }

    private void displayMenu(){

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Menu menu = navigationView.getMenu();
        SubMenu subMenu = menu.addSubMenu("ETC MANAGEMENT");
        subMenu.add("   - Tag Print Request").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                setupUtil.setIntentWithoutFinishStr(Main2Activity.this,Etc_Management_Activity.class,
//                        setupUtil.getActivity(Main2Activity.this),intentMap);
//                setupUtil.setIntent(getApplicationContext(),Etc_Management_Activity.class,setupUtil.getActivity(getApplicationContext()));
                Intent intent=new Intent(getApplicationContext(), Etc_Management_Activity.class);
                startActivity(intent);
                return false;
            }
        });

        SubMenu subLogout = menu.addSubMenu("Logout");
        subLogout.add("Logout").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                sessionManagement.clearSharedPreferences();
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

                return false;
            }
        });
        drawerLayout.closeDrawers();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            sessionManagement.clearSharedPreferences();
            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return true;
    }
    private void setProfile(){
        View vHeader = navigationView.getHeaderView(0);
        userIDTextView = vHeader.findViewById(R.id.userId);
        strCodeTextView = vHeader.findViewById(R.id.strCd);
        imageViewProfile = vHeader.findViewById(R.id.imageViewProfile);
        setImageDrawable(imageViewProfile);

        userIDTextView.setText(username);
        strCodeTextView.setText(strCd);
    }
    private void setImageDrawable(ImageView imageDrawable){
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(4) /* thickness in px */
                .endConfig()
                .buildRoundRect(username.substring(0,1), Color.RED, 10);

        imageDrawable.setImageDrawable(drawable);
    }
}

package com.example.user.print;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.user.print.Fragment.HomeFragment;
import com.example.user.print.Fragment.PrintRequestContainerFragment;
import com.example.user.print.Interface.ScanResultReceiver;
import com.example.user.print.util.NoScanResultException;
import com.example.user.print.util.SessionManagement;
import com.example.user.print.util.SetupUtil;

import java.util.HashMap;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomeFragment.OnFragmentInteractionListener,
        PrintRequestContainerFragment.OnFragmentInteractionListener,
        ScanResultReceiver {
    private static final String TAG = "Main2Activity";
    private String userID,strCd,corpFG,username,setTitle ;
    private SetupUtil setupUtil;
    private TextView userIDTextView,strCodeTextView;
    private ImageView imageViewProfile;
    private HashMap<String,String> intentMap = new HashMap<>();
    private SessionManagement sessionManagement;
    private NavigationView navigationView;
    private Fragment fragment;

    private Boolean doubleBackExit = false;
    private Handler mHandles = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackExit = false;
        }
    };
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
        fragment = new HomeFragment();
        fragmentShow(fragment,"Home");

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

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment tagFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                String tagName = tagFragment.getTag();
                if(tagFragment instanceof HomeFragment){
//                    navigationView.setCheckedItem(010);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }

//                else if(tagFragment instanceof TagPrintRequestFragment)
//                {
//                    navigationView.setCheckedItem(navigationView.getMenu().findItem(100));
//                }
//                else if(tagFragment instanceof HomeFragment){
//                    for (int i = 0; i < navigationView.getMenu().size(); i++){
//                        if(navigationView.getMenu().getItem(i).getSubMenu().){
//                            navigationView.getMenu().getItem(i).setChecked(false);
//                        }
//                    }
//
//                }
                setTitle(tagName);
            }
        });

        displayMenu();
    }

    private void displayMenu(){
        fragment = null;
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        final Menu menu = navigationView.getMenu();

        menu.add(0,010,0,"Home").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                setCheckableFalse();
                menu.getItem(0).setChecked(true);
                fragmentShow(new HomeFragment(),"Home");
                return false;
            }
        });
        SubMenu subMenu = menu.addSubMenu("ETC MANAGEMENT");
        subMenu.add(1,100,0,"   - Tag Print Request").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent=new Intent(getApplicationContext(), Etc_Management_Activity.class);
//                startActivity(intent);
                setCheckableFalse();
                menu.getItem(1).getSubMenu().getItem(0).setChecked(true);

                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                navigationView.setCheckedItem(navigationView.getMenu().findItem(100));
                    fragmentShow(new PrintRequestContainerFragment(),"Tag Print Request");

                return false;
            }
        });

        SubMenu subLogout = menu.addSubMenu("Logout");
        subLogout.add(2,200,0,"Logout").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
    }
    private void setCheckableFalse(){
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++){
            if (menu.getItem(i).getSubMenu()!=null){
                for(int x = 0; x < menu.getItem(i).getSubMenu().size(); x++){
                        menu.getItem(i).getSubMenu().getItem(x).setChecked(false);
                    }
            }
            else
            {
                menu.getItem(i).setChecked(false);
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            setCheckableFalse();

            Fragment tagFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            String tagName = tagFragment.getTag();

            if(tagName == "Home"){
                if (doubleBackExit){
                    finish();
                }

                this.doubleBackExit = true;
                Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();
                mHandles.postDelayed(mRunnable,2000);
            }
            else{
                setTitle(tagName);
                getSupportFragmentManager().popBackStack();

            }
//
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
    private void fragmentShow(Fragment fragmentParam,String title){
        setTitle(title);
        if (fragmentParam != null ){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.replace(R.id.fragmentContainer,fragmentParam,title);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandles != null){
            mHandles.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void scanResultData(String codeFormat, String codeContent) {

    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {

    }
}

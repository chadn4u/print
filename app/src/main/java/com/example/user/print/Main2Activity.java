package com.example.user.print;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.user.print.Fragment.HomeFragment;
import com.example.user.print.Fragment.PrintRequestContainerFragment;
import com.example.user.print.Fragment.ScanResultFragment;
import com.example.user.print.Interface.FragmentDestroy;
import com.example.user.print.Interface.ScanResultReceiver;
import com.example.user.print.adapter.ExpandableListAdapter;
import com.example.user.print.api.ClientWithToken;
import com.example.user.print.api.Service;
import com.example.user.print.model.MenuFeed;
import com.example.user.print.model.MenuModel;
import com.example.user.print.model.ScanFeed;
import com.example.user.print.util.CustomTypeFaceSpan;
import com.example.user.print.util.NoScanResultException;
import com.example.user.print.util.SessionManagement;
import com.example.user.print.util.SetupUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomeFragment.OnFragmentInteractionListener,
        PrintRequestContainerFragment.OnFragmentInteractionListener,
        ScanResultFragment.OnFragmentInteractionListener,
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
    private ExpandableListView expandableListView;
    private ClientWithToken client;
    private Boolean doubleBackExit = false;
    private Handler mHandles = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackExit = false;
        }
    };
    private String barcodes;
    private FragmentDestroy fragmentDestroy;
    private Fragment lastFragment;
    private List<MenuFeed.MenuDetail> lstMenu = new ArrayList<>();
    private List<MenuFeed.MenuChild> lstChild = new ArrayList<>();

    HashMap<MenuModel, List<MenuModel>> menuMap = new HashMap<>();
    ExpandableListAdapter expandableListAdapter;
    List<MenuModel> headerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        expandableListView = findViewById(R.id.expandableListView);
        sessionManagement = new SessionManagement(getApplicationContext());


//        Intent i = getIntent();
        username = sessionManagement.getSharedPreferences("EMP_NM","");//i.getStringExtra("EMP_NM");//i.getStringExtra("EMP_NM");
        userID = sessionManagement.getSharedPreferences("EMP_NO","");//i.getStringExtra("EMP_NO");
        strCd = sessionManagement.getSharedPreferences("STR_CD","");//.getStringExtra("STR_CD");
        corpFG = sessionManagement.getSharedPreferences("CORP_FG","");//.getStringExtra("CORP_FG");

//        Log.d(TAG, "onCreate: " + username);

        setProfile();
        if (savedInstanceState == null){
            fragment = new HomeFragment();
            Bundle bundleDummy = new Bundle();
            fragmentShow(fragment,"Home");
        }

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
//                    navigationView.getMenu().getItem(0).setChecked(true);
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
        populateMenu();
//        displayMenu();
    }
    private void displayMenu(){
        fragment = null;
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        final Menu menu = navigationView.getMenu();
//        generateMenuData();
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
        Typeface font = Typeface.createFromAsset(getAssets(), "lottegroup.ttf");
        SpannableString s = new SpannableString("ETC MANAGEMENT");
        s.setSpan(new CustomTypeFaceSpan("",font), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SubMenu subMenu = menu.addSubMenu(s);

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
        SpannableString s1 = new SpannableString("Logout");
        s1.setSpan(new CustomTypeFaceSpan("",font), 0, s1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SubMenu subLogout = menu.addSubMenu(s1);
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
                Fragment fragmentChild = tagFragment.getChildFragmentManager().findFragmentById(tagFragment.getView().findViewById(R.id.fragmentPrintRequest).getId());
                if(fragmentChild != null){
                    Log.d(TAG, "onBackPressed: "+tagFragment.getChildFragmentManager().getBackStackEntryCount());
                    tagFragment.getChildFragmentManager().beginTransaction().remove(fragmentChild).commit();
                    tagFragment.getChildFragmentManager().popBackStackImmediate();
                    fragmentDestroy = (FragmentDestroy) tagFragment;
                    fragmentDestroy.onChildDestroy(true);
                } else{
                    Log.d(TAG, "onBackPressed: udah gk ada child");
                    if (doubleBackExit){
                        finish();
                    }else{
                        this.doubleBackExit = true;
                        Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();
                        mHandles.postDelayed(mRunnable,2000);
                    }
                }



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
        Typeface font = Typeface.createFromAsset(getAssets(), "lottegroup.ttf");
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.RED)
                .useFont(font)
                .withBorder(4) /* thickness in px */
                .endConfig()
                .buildRound(username.substring(0,1), Color.WHITE);
//                .buildRoundRect(username.substring(0,1), Color.WHITE, 90);

        imageDrawable.setImageDrawable(drawable);
    }
    private void fragmentShow(Fragment fragmentParam,String title){
        setTitle(title);
        if (fragmentParam != null ){
            lastFragment = fragmentParam;
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
    public void onBarcodeDetach(String barcode) {
        barcodes = barcode;
    }

    @Override
    public void scanResultData(String codeFormat, String codeContent) {

    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {

    }

    public void generateMenuData(){
        client = new ClientWithToken("http://frontier.lottemart.co.id/code/V2/");
        Service serviceAPI = client.getClientWithToken(Main2Activity.this);
        Call<MenuFeed> call = serviceAPI.getMenu();

        call.enqueue(new Callback<MenuFeed>() {
            @Override
            public void onResponse(Call<MenuFeed> call, Response<MenuFeed> response) {

                if(!response.isSuccessful()){
                    setupUtil.showToast(Main2Activity.this,"Terjadi Kesalahan pada server 1",0);
            }
                else{

                    if(response.body().isStatus()) {
                       lstMenu.addAll(response.body().getData());
                       populateMenu();
                    }
                    else
                    {
                        setupUtil.showToast(Main2Activity.this,"Terjadi Kesalahan pada server 2",0);

                    }
                }

            }

            @Override
            public void onFailure(Call<MenuFeed> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                setupUtil.showToast(Main2Activity.this,t.getLocalizedMessage(),0);
                //debug only
                populateMenu();
     }

        });
    }
    public void populateMenu(){
        MenuModel menuModel;
        boolean hasChild = false;
//        menuModel = new MenuModel("Home",true,hasChild,"Home");
//        menuMap.put(menuModel,null);
//        headerList.add(menuModel);

        menuModel = new MenuModel("Tag Print Request",true,hasChild,"Tag Print Request");
        menuMap.put(menuModel,null);
        headerList.add(menuModel);
        menuModel = new MenuModel("Logout",true,hasChild,"Logout");
        menuMap.put(menuModel,null);
        headerList.add(menuModel);
        if (lstMenu != null){
            for (int i = 0; i < lstMenu.size(); i++){
                List<MenuModel> childModelsList = new ArrayList<>();
                if (lstMenu.get(i).getCHILD().size() > 0 ){
                    hasChild = true;
                    for (int x = 0; x < lstMenu.get(i).getCHILD().size(); x++){
                        childModelsList.add(new MenuModel(
                                lstMenu.get(i).getCHILD().get(x).getCHILD_NM(),
                                false,
                                false,
                                lstMenu.get(i).getCHILD().get(x).getCHILD_ORD())
                        );
                    }
                }
                else{
                    hasChild = false;
                }
                menuModel = new MenuModel(lstMenu.get(i).getPARENT_NM(),true,hasChild,Integer.toString(i));
                headerList.add(menuModel);
                if (menuModel.hasChildren){
                    menuMap.put(menuModel,childModelsList);
                }else{menuMap.put(menuModel,null);}
            }
        }

        expandableListAdapter = new ExpandableListAdapter(this, headerList,menuMap);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (headerList.get(groupPosition).isGroup){
                    if (!headerList.get(groupPosition).hasChildren){
                        fragment = null;
                        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

                        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        switch (headerList.get(groupPosition).fragmentName){
                            case "Tag Print Request":
                                if (!(lastFragment instanceof HomeFragment))
                                    fragmentShow(new HomeFragment(),"Home");
                                //fragmentShow(new PrintRequestContainerFragment(),"Tag Print Request");
                                break;
                            case "Home":
                                if (!(lastFragment instanceof HomeFragment))
                                    fragmentShow(new HomeFragment(),"Home");
                                break;
                            case "Logout":
                                sessionManagement.clearSharedPreferences();
                                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                        }
                          return false;
                    }
                }

                return false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("barcode",barcodes);
    }
}

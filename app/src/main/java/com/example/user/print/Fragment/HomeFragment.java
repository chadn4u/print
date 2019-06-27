package com.example.user.print.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.print.Interface.FragmentDestroy;
import com.example.user.print.Main2Activity;
import com.example.user.print.R;
import com.example.user.print.ScannerActivity;
import com.example.user.print.api.ClientWithToken;
import com.example.user.print.api.Service;
import com.example.user.print.model.ScanFeed;
import com.example.user.print.util.SessionManagement;
import com.example.user.print.util.SetupUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ScanResultFragment.OnFragmentInteractionListener, FragmentDestroy {
    private static final String TAG = "HomeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String strCd,corpFG ;

    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;

    private ImageView startScan;
    private SetupUtil setupUtil = new SetupUtil();
    private  ClientWithToken client;
    private SessionManagement sessionManagement;
    private SearchView searchView;

    private Boolean onDestroy = false;

    private OnFragmentInteractionListener mListener;

    private String barcode;

    private Fragment fragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tag_print_request, container, false);

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startScan = view.findViewById(R.id.scanStart);
        searchView = view.findViewById(R.id.searchView);

        sessionManagement = new SessionManagement(getContext());

        if (savedInstanceState != null){
            loadJson(savedInstanceState.getString("barcode"));
            barcode = savedInstanceState.getString("barcode");
        }



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                barcode = query;
                loadJson(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new IntentIntegrator(setupUtil.getActivity(getContext())).
//                        setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES)
//                        .setCaptureActivity(ScannerActivity.class).initiateScan();
                new com.example.user.print.util.IntentIntegrator(HomeFragment.this)
                        .setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES)
                        .setCaptureActivity(ScannerActivity.class).initiateScan();
//                IntentIntegrator.forSupportFragment(HomeFragment.this).initiateScan();

            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onChildDestroy(Boolean destroy) {
          onDestroy = destroy;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onBarcodeDetach(String barcode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (requestCode) {
            case CUSTOMIZED_REQUEST_CODE: {
                Toast.makeText(getContext(), "REQUEST_CODE = " + requestCode, Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        barcode = result.getContents();
        //Toast.makeText(getContext(),"Selamat "+result.getContents(),Toast.LENGTH_SHORT).show();
        if(result.getContents() == null) {
            Log.d("MainActivity", "Cancelled scan");
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Log.d("MainActivity", "Scanned");
            Toast.makeText(getContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

            loadJson(result.getContents());

        }
    }

    private void fragmentShow(Fragment fragmentParam, String title,Bundle bundle){

        if (fragmentParam != null ){
            fragmentParam.setArguments(bundle);

            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();


            ft.replace(R.id.fragmentPrintRequest,fragmentParam,title);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft.addToBackStack(null);
            ft.commit();
        }
    }
    private void loadJson(final String result){
        if ( result == null){
            return; //Skip Call Json
        }
            strCd = sessionManagement.getSharedPreferences("STR_CD","");//.getStringExtra("STR_CD");
            corpFG = sessionManagement.getSharedPreferences("CORP_FG","");//.getStringExtra("CORP_FG");

            client = new ClientWithToken("http://frontier.lottemart.co.id/code/V2/");
            Service serviceAPI = client.getClientWithToken(getContext());
            Fragment fragmentExisting = getChildFragmentManager().findFragmentById(R.id.fragmentPrintRequest);
            if(fragmentExisting != null)
                getChildFragmentManager().beginTransaction().remove(fragmentExisting).commit();

        Call<ScanFeed> call = serviceAPI.getScanDetail(result,corpFG,strCd);

            call.enqueue(new Callback<ScanFeed>() {
                @Override
                public void onResponse(Call<ScanFeed> call, Response<ScanFeed> response) {

                    if(response.code() != 200){
                        if (response.code() == 401){
                            setupUtil.showToast(getContext(),"Token Expired, silahkan scan ulang",0);
                            Snackbar.make(getView(),"Token Expired, silahkan scan ulang",Snackbar.LENGTH_LONG).show();
                        }else{
                            Snackbar.make(getView(),"Server response : "+response.code(),Snackbar.LENGTH_LONG).show();
                        }

                        //Toast.makeText(mContext,"Terjadi Kesalahan pada server",Toast.LENGTH_SHORT).show();
                    }
                    else{

                        if(response.body().getStatus()) {
                            fragment = new ScanResultFragment();

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", response.body().getData());
                            bundle.putString("barcodeCd",result);

                            fragmentShow(fragment,"Result",bundle);

                        }
                        else
                        {
                            if (response.body().getMessage().equals("No Data were found")){
                                Snackbar.make(getView(),"Product tidak terdaftar",Snackbar.LENGTH_SHORT).show();
                            }
                            else{
                                Snackbar.make(getView(),response.body().getMessage(),Snackbar.LENGTH_LONG).show();
                            }


                        }
                    }

                }

                @Override
                public void onFailure(Call<ScanFeed> call, Throwable t) {
                    Snackbar.make(getView(),t.getMessage(),Snackbar.LENGTH_SHORT).show();
                }

            });
        }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!onDestroy){
            outState.putString("barcode",barcode);
            mListener.onBarcodeDetach(barcode);
        }else{
            outState.clear();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

package com.example.user.print.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

public class SetupUtil {
    //    private SectionStatePagerAdapter sectionStatePagerAdapter;
    private HashMap<String,Integer> hashMap = new HashMap<>();
    public SetupUtil() {
    }

    /**
     * Method untuk menge set ViewPager
     * @param viewPager
     * @param fragmentManager
     * @param listFragment
     * @param listPageTitle
     */
//    public void setupViewPager(ViewPager viewPager, FragmentManager fragmentManager, ArrayList<Fragment> listFragment, ArrayList<String> listPageTitle){
//        sectionStatePagerAdapter = new SectionStatePagerAdapter(fragmentManager);
//        for(int i = 0; i < listFragment.size(); i++){
//
//            sectionStatePagerAdapter.addFragment(listFragment.get(i),listPageTitle.get(i));
//        }
//
//        viewPager.setAdapter(sectionStatePagerAdapter);
//    }

    /**
     * Methor untuk menge set Intent
     * @param mContextHome Context Awal
     * @param mClassDest Class tujuan
     * @param mActivity Nama Activity yang memanggil method setIntent
     */
    public void setIntentInt(Context mContextHome, Class<?> mClassDest, Activity mActivity, HashMap<String,Integer> extra){
        Intent intent = new Intent(mContextHome,mClassDest);
        for(HashMap.Entry<String,Integer> entry : extra.entrySet()){

            intent.putExtra(entry.getKey(),entry.getValue());
        }
        mActivity.startActivity(intent);
        mActivity.finish();
    }
    public void setIntentStr(Context mContextHome,Class<?> mClassDest,Activity mActivity,HashMap<String,String> extra){
        Intent intent = new Intent(mContextHome,mClassDest);
        for(HashMap.Entry<String,String> entry : extra.entrySet()){

            intent.putExtra(entry.getKey(),entry.getValue());
        }
        mActivity.startActivity(intent);
        mActivity.finish();
    }
    public void setIntent(Context mContextHome,Class<?> mClassDest,Activity mActivity){
        Intent intent = new Intent(mContextHome,mClassDest);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    public void setIntentWithoutFinishInt(Context mContextHome,Class<?> mClassDest,Activity mActivity,HashMap<String,Integer> extra){
        Intent intent = new Intent(mContextHome,mClassDest);
        for(HashMap.Entry<String,Integer> entry : extra.entrySet()){

            intent.putExtra(entry.getKey(),entry.getValue());
        }
        mActivity.startActivity(intent);
    }

    public void setIntentWithoutFinishObj(Context mContextHome,Class<?> mClassDest,Activity mActivity,HashMap<String,Object> extra){
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundleObj",extra);
        Intent intent = new Intent(mContextHome,mClassDest);

        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    public void setIntentWithoutFinishStr(Context mContextHome,Class<?> mClassDest,Activity mActivity,HashMap<String,String> extra){
        Intent intent = new Intent(mContextHome,mClassDest);
        for(HashMap.Entry<String,String> entry : extra.entrySet()){

            intent.putExtra(entry.getKey(),entry.getValue());
        }
        mActivity.startActivity(intent);
    }
    public void setIntentWithoutFinish(Context mContextHome,Class<?> mClassDest,Activity mActivity){
        Intent intent = new Intent(mContextHome,mClassDest);
        mActivity.startActivity(intent);
//        mActivity.finish();
    }

    /**
     * Method untuk mendapatkan activity dari context
     * @param mContext
     * @return
     */
    public Activity getActivity(Context mContext){
        if(mContext == null){
            return null;
        }
        else if(mContext instanceof ContextWrapper){
            if(mContext instanceof Activity){
                return (Activity) mContext;
            }
            else {
                return getActivity(((ContextWrapper) mContext).getBaseContext());
            }
        }
        return null;
    }

    public void mapPut(String key,int value){
        hashMap.put(key,value);
    }
    public int mapGet(String key){
        return hashMap.get(key);
    }

    /**
     *
     * @param mContext
     * @param textToast
     * @param lengthToast lama toast muncul 0:Short || 1:Long
     */
    public void showToast(Context mContext,String textToast,int lengthToast){
        if(mContext == null ){
            return;
        }
        if (lengthToast > 1){
            lengthToast = 1;
        }
        if(lengthToast < 0 ){
            lengthToast = 0;
        }
        Toast.makeText(mContext,textToast,lengthToast).show();
    }
}

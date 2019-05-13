package com.example.user.print.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManagement {
    Context mContext;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManagement(Context mContext) {
        this.mContext = mContext;
        sharedPreferences =  mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSharedPreferencesString(HashMap<String,String> value){
        for(HashMap.Entry<String,String> entry : value.entrySet()){
            editor.putString(entry.getKey(),entry.getValue());
        }
        editor.commit();
    }
    public void saveSharedPreferencesInt(HashMap<String,Integer> value){
        for(HashMap.Entry<String,Integer> entry : value.entrySet()){
            editor.putInt(entry.getKey(),entry.getValue());
        }
        editor.commit();
    }

    public String getSharedPreferences(String key,String defaultValue){
        return sharedPreferences.getString(key,defaultValue);
    }
    public Integer getSharedPreferences(String key,Integer defaultValue){
        return sharedPreferences.getInt(key,defaultValue);
    }
    public boolean checkSharedPreferences(String key){
        if(sharedPreferences.contains(key)){
            return true;
        }
        else{
            return false;
        }
    }
    public void saveTokenAccess(String token){
        editor.putString("Accesstoken",token);
        editor.commit();
    }
    public void saveTokenRefresh(String token){
        editor.putString("Refreshtoken",token);
        editor.commit();
    }
    public String getTokenAccess(){
        return sharedPreferences.getString("Accesstoken","");
    }
    public String getTokenRefresh(){
        return sharedPreferences.getString("Refreshtoken","");
    }
    public void clearSharedPreferences(){
        editor.clear();
        editor.commit();
    }
}

package com.example.manish.wpaper.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**********************************
 * Created by Manish on 02-Jan-19
 ***********************************/
public class SessionUtility {

    private static final String PREF_NAME = "INEED-PREF";
    private static final String PREF_KEY_USERNAME = "userName";
    private static final String PREF_KEY_CARD_USERNAME = "cardUserName";

    private static final String PREF_KEY_PHONE_NUMBER = "phone_number";
    private static final String PREF_KEY_EMAIL = "email";

    private static final String PREF_KEY_ACCESS_TOKEN = "access_token";

    private static final String PREF_KEY_USER_ID = "user_id";
    private static final String PREF_KEY_USER_TYPE_ID = "user_type_id";
    private static final String PREF_KEY_APP_CHANNEL_ID = "app_chanel_id";

    private static final String PREF_KEY_SF_USER_ID = "sf_user_id";

    private static final String PREF_KEY_CARD_ID = "card_id";

    private static final String PREF_KEY_IS_LOGGED_IN = "is_logged_in";

    private static SharedPreferences mPrefs;
    private static SessionUtility appPreferencesHelper;

    public static SessionUtility getInstance(Context context){
        if(null == appPreferencesHelper ){
            appPreferencesHelper = new SessionUtility();
            if(null != context){
                mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            }
        }
        return appPreferencesHelper;
    }

    public boolean isLoggedIn() {
        return mPrefs.getBoolean(PREF_KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(Boolean isLoggedIn) {
        if (null != mPrefs) {
            mPrefs.edit().putBoolean(PREF_KEY_IS_LOGGED_IN, isLoggedIn).apply();
        }
    }

//    public void setUser(UserLoginResult user){
//        if(null != mPrefs){
//            SharedPreferences.Editor editor = mPrefs.edit();
//            editor.putString(PREF_KEY_ACCESS_TOKEN, user.getAccessToken());
//            editor.putInt(PREF_KEY_USER_ID, user.getUserId());
//            editor.putInt(PREF_KEY_USER_TYPE_ID, user.getUserTypeId());
//            editor.putString(PREF_KEY_PHONE_NUMBER, user.getMobileNumber());
//            editor.putBoolean(PREF_KEY_IS_LOGGED_IN, true);
//            editor.putInt(PREF_KEY_APP_CHANNEL_ID, user.getAppChannelId());
//            editor.putString(PREF_KEY_USERNAME, user.getUserName());
//            editor.putString(PREF_KEY_SF_USER_ID, user.getSFUserId());
//            editor.apply();
//        }
//    }

    public int getUserId() {
        int userId = 0;
        if(null != mPrefs) {
            userId = mPrefs.getInt(PREF_KEY_USER_ID,0);
        }
        return userId;
    }

    public String getSFUserID(){
        String SFuserID = null;
        if(null != mPrefs){
            SFuserID = mPrefs.getString(PREF_KEY_SF_USER_ID, null);
        }
        return SFuserID;
    }

    public String getAccessToken(){
        String token = null;
        if(null != mPrefs){
            token = mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
        }
        return token;
    }

    public int getUserTypeId(){
        int userTypeId = 0;
        if(null != mPrefs){
            userTypeId = mPrefs.getInt(PREF_KEY_USER_TYPE_ID, 0);
        }
        return userTypeId;
    }

    public String getUserName(){
        String username = null;
        if(null != mPrefs){
            username = mPrefs.getString(PREF_KEY_USERNAME, null);
        }
        return username;
    }

    public int getAppChannelId(){
        int appChannelId = 0;
        if( null != mPrefs){
            appChannelId = mPrefs.getInt(PREF_KEY_APP_CHANNEL_ID, 0);
        }
        return appChannelId;
    }

    public String getUserMobileNo(){
        String mobile = null;
        if(null != mPrefs){
            mobile = mPrefs.getString(PREF_KEY_PHONE_NUMBER, null);
        }
        return mobile;
    }

    public void setPrefValueEmail(String email){
        if (null != email){
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_KEY_EMAIL, email);
            editor.apply();
        }
    }
    public String getEmail(){
        String email = null;
        if(null != mPrefs){
            email = mPrefs.getString(PREF_KEY_EMAIL, null);
        }
        return  email;
    }


    public void setPrefValueCardUserName(String name){
        if (null != name){
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_KEY_CARD_USERNAME, name);
            editor.apply();
        }
    }
    public String getCardUserName(){
        String name = null;
        if(null != mPrefs){
            name = mPrefs.getString(PREF_KEY_CARD_USERNAME, null);
        }
        return  name;
    }
    public void resetSession() {
        if (mPrefs != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.remove(PREF_KEY_USER_ID);
            editor.remove(PREF_KEY_USER_TYPE_ID);
            editor.remove(PREF_KEY_PHONE_NUMBER);
            editor.remove(PREF_KEY_IS_LOGGED_IN);
            editor.remove(PREF_KEY_ACCESS_TOKEN);
            editor.remove(PREF_KEY_APP_CHANNEL_ID);
            editor.remove(PREF_KEY_USERNAME);
            editor.apply();
        }
    }
}

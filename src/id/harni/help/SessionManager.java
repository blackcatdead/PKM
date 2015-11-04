package id.harni.help;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import id.harni.pkm.LoginActivity;

import java.util.HashMap;



/**
 * Created by Ikhsanudin on 6/5/2015.
 */
public class SessionManager {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "PKM";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_NAME = "name";
    public static final String KEY_IP = "ip";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String name, String username){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_ID, id);
        // commit changes
        editor.commit();
    }

    public void setIP(String ip){

        editor.putString(KEY_IP, ip);

        editor.commit();
    }

    public String getIP(){

        String ipz = pref.getString(KEY_ID, "");
        return ipz;
    }


    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
        //if (this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            //Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

        }
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_IP, pref.getString(KEY_IP, null));
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        //editor.clear();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_ID);
        editor.remove(KEY_NAME);
        editor.remove(KEY_EMAIL);
        editor.remove(IS_LOGIN);
        editor.commit();

        // After logout redirect user to Loing Activity
        //Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
       // _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}

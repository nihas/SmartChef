package com.nihas.smart.chef.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.nihas.smart.chef.Constants;
import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.QuickstartPreferences;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.RegistrationIntentService;
import com.nihas.smart.chef.activities.MainActivity;
import com.nihas.smart.chef.app.SmartChefApp;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by snyxius on 10/23/2015.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;
    String SENDER_ID = "1013702867679";

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private String fbId = null,emailid,uid,access_token;
    GoogleCloudMessaging gcm;
    String regid;

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_splash);
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            regid = getRegistrationId(getApplicationContext());
            Log.d("DeviceId", regid);
            if (regid.isEmpty()) {
                registerInBackground(false, "");


            } else if (!SmartChefApp.readFromPreferences(getApplicationContext(), Keys.isSent, false)) {
                registerInBackground(true,
                        SmartChefApp.readFromPreferences(getApplicationContext(), Keys.key, Constants.DEFAULT_STRING));
            }
        } else {

            Log.i("Debug", "No valid Google Play Services APK found.");
        }
//
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
////                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
//                SharedPreferences sharedPreferences =
//                        PreferenceManager.getDefaultSharedPreferences(context);
//                boolean sentToken = sharedPreferences
//                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
//                if (sentToken) {
//                    SmartChefApp.showAToast(getString(R.string.gcm_send_message));
//                } else {
//                    SmartChefApp.showAToast(getString(R.string.token_error_message));
//                }
//            }
//        };
//
//        // Registering BroadcastReceiver
//        registerReceiver();
//
//        if (checkPlayServices()) {
//            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                doCheck();
            }
        }, SPLASH_TIME_OUT);

    }

    private void doCheck() {

        Profile profile = Profile.getCurrentProfile();
        AccessToken token = AccessToken.getCurrentAccessToken();


        if (profile != null) {
            uid = profile.getId();

        }

        if (token != null) {
            uid = token.getUserId();
            access_token = token.getToken();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            finish();
        }else if(SmartChefApp.readFromPreferences(SplashActivity.this,"isGplusLogin",false)) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            finish();
        }else{
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            finish();
        }


        Log.d("FacebookId", " " + fbId + " email" + SmartChefApp.readFromPreferences(SplashActivity.this, Keys.email, Constants.DEFAULT_STRING) + "uid " + uid + "access_token " + access_token);



    }


    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("SPLASH", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("Debug", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("Debug", "App version changed.");
            return "";
        }
        return registrationId;
    }


    private SharedPreferences getGCMPreferences(Context context) {
        return getApplicationContext().getSharedPreferences(SplashActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    private void registerInBackground(boolean flag, String value) {
        try {
            if (flag) {
                new KeyAsyncTask().execute(value);
            } else {
                new KeyAsyncTask().execute("");
            }

        } catch (Exception e) {
        }
    }

    private class KeyAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject obj = null;
            try {
                if (params[0] != null
                        && params[0].toString().trim().length() > 0) {
                    regid = params[0];

                } else {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(SENDER_ID);

                }

                // You should send the registration ID to your server over
                // HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your
                // app.
                // The request to your server should be authenticated if
                // your app
                // is using accounts.
                obj = sendRegistrationIdToBackend();

                // For this demo: we don't need to send it because the
                // device
                // will send upstream messages to a server that echo back
                // the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                storeRegistrationId(getApplicationContext(), regid);
            } catch (IOException ex) {
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if (result == null) {
                final SharedPreferences prefs = getGCMPreferences(getApplicationContext());
                int appVersion = getAppVersion(getApplicationContext());
                Log.i("Debug", "Saving regId on app version " + appVersion);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(PROPERTY_APP_VERSION, appVersion);
                editor.commit();
                SmartChefApp.saveToPreferences(getApplicationContext(), Keys.key, regid);
                SmartChefApp.saveToPreferences(getApplicationContext(), Keys.isSent, false);
            }

        }



        private JSONObject sendRegistrationIdToBackend() {
            JSONObject data = null;
            try {
                SmartChefApp.saveToPreferences(getApplicationContext(), Keys.token, regid);
                Log.d("RegidterationId", regid.toString());
                //      String response = WebServices.postData(sendObj.toString(),3);
                // data = new JSONObject(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }


        private void storeRegistrationId(Context context, String regId) {
            final SharedPreferences prefs = getGCMPreferences(context);
            int appVersion = getAppVersion(context);
            Log.i("Debug", "Saving regId on app version " + appVersion);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PROPERTY_REG_ID, regId);
            editor.putInt(PROPERTY_APP_VERSION, appVersion);
            editor.putBoolean("isSent", true);
            editor.commit();
        }
    }
}

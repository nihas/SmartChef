package com.nihas.smart.chef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.nihas.smart.chef.Constants;
import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.MainActivity;
import com.nihas.smart.chef.app.SmartChefApp;

/**
 * Created by snyxius on 10/23/2015.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private String fbId = null,emailid,uid,access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_splash);

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
        }else{
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            finish();
        }


        Log.d("FacebookId", " " + fbId + " email" + SmartChefApp.readFromPreferences(SplashActivity.this, Keys.email, Constants.DEFAULT_STRING) + "uid " + uid + "access_token " + access_token);



    }
}

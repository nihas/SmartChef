package com.nihas.smart.chef.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.nihas.smart.chef.Constants;
import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.utils.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by snyxius on 4/2/16.
 */
public class PreferActivity extends AppCompatActivity {

    LinearLayout veg,nonVeg;
    TextView both;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.veg_non_veg);
        initialise();

        Animation anim1=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.push_up_in);
        Animation anim2=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.push_down_in);
        Animation anim3=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.to_middle);
        nonVeg.setAnimation(anim2);
        veg.setAnimation(anim1);
        both.setAnimation(anim3);
    }


    private void initialise(){
        veg=(LinearLayout)findViewById(R.id.veg);
        nonVeg=(LinearLayout)findViewById(R.id.non_veg);
        both=(TextView)findViewById(R.id.both);
    }


}

package com.nihas.smart.chef.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

private LoginButton loginButton;
private CallbackManager callbackManager;
private AccessTokenTracker accessTokenTracker;
private ProfileTracker profileTracker;
private String fbId = null,emailid,uid,access_token;
boolean loggedIn;
private SignInButton signinButton;
private static GoogleApiClient mGoogleApiClient;
private boolean mIntentInProgress;
//    VideoView videoView;

private boolean mSignInClicked;

private ConnectionResult mConnectionResult;

    private static final int PICK_MEDIA_REQUEST_CODE = 8;
    private static final int SHARE_MEDIA_REQUEST_CODE = 9;
    private static final int SIGN_IN_REQUEST_CODE = 10;
    private static final int ERROR_DIALOG_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        initialise();
        initializeFacebook();
        initializeGooglePlus();
    }


    private void initialise(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        loginButton = (LoginButton) findViewById(R.id.fblogin);
//        loginButton.setFragment(this);
//        videoView=(VideoView)findViewById(R.id.video_view);
//        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
//                + R.raw.sample);
//        videoView.setVideoURI(video);
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.start();
//            }
//        });
//        videoView.start();

        signinButton = (SignInButton)findViewById(R.id.signin);
        signinButton.setOnClickListener(this);
        findViewById(R.id.fb).setOnClickListener(this);
        findViewById(R.id.googlesignin).setOnClickListener(this);
        viewPager.setAdapter(new ViewPagerAdapter(R.array.icons, R.array.titles, R.array.hints));
        CirclePageIndicator mIndicator  = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
    }

    private void initializeGooglePlus() {
        mGoogleApiClient = buildGoogleAPIClient();
    }



    public boolean isFacebookLoggedIn(){
            return AccessToken.getCurrentAccessToken() != null;
        }


    /**
     * API to return GoogleApiClient Make sure to create new after revoking
     * access or for first time sign in
     *
     * @return
     */
    private GoogleApiClient buildGoogleAPIClient() {
        return new GoogleApiClient.Builder(LoginActivity.this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        // make sure to initiate connection
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        // disconnect api if it is connected
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    /**
     * API to handler sign in of user If error occurs while connecting process
     * it in processSignInError() api
     */
    private void processSignIn() {

        if (!mGoogleApiClient.isConnecting()) {
            processSignInError();
            mSignInClicked = true;
        }

    }

    /**
     * API to process sign in error Handle error based on ConnectionResult
     */
    private void processSignInError() {
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(LoginActivity.this,
                        SIGN_IN_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Callback for GoogleApiClient connection failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), LoginActivity.this,
                    ERROR_DIALOG_REQUEST_CODE).show();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                processSignInError();
            }
        }

    }

    /**
     * Callback for GoogleApiClient connection success
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;
        Toast.makeText(LoginActivity.this, "Signed In Successfully",
                Toast.LENGTH_LONG).show();

        processUserInfoAndUpdateUI();



    }

    /**
     * Callback for suspension of current connection
     */
    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();

    }

    /**
     * API to update signed in user information
     */
    private void processUserInfoAndUpdateUI() {
        JSONObject jobj=new JSONObject();
        Person signedInUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (signedInUser != null) {





            SmartChefApp.saveToPreferences(LoginActivity.this, "login_type","google");

            if (signedInUser.hasId()) {
                String id = signedInUser.getId();
                Log.d("id",id);
                SmartChefApp.saveToPreferences(LoginActivity.this, "user_id", id);
            }

            if (signedInUser.hasDisplayName()) {
                String userName = signedInUser.getDisplayName();
                Log.d("UserName",userName);
                SmartChefApp.saveToPreferences(LoginActivity.this, "user_name", userName);
            }

            if(signedInUser.hasGender()){
                int gendr = signedInUser.getGender();
                Log.d("gender",gendr+"");
                if(gendr==0)
                    SmartChefApp.saveToPreferences(LoginActivity.this, "gender", "male");
                else
                    SmartChefApp.saveToPreferences(LoginActivity.this, "gender", "female");
            }

            if (signedInUser.hasTagline()) {
                String tagLine = signedInUser.getTagline();
                Log.d("tagLine",tagLine);
            }

            if (signedInUser.hasAboutMe()) {
                String aboutMe = signedInUser.getAboutMe();
                Log.d("aboutMe",aboutMe);
            }

            if (signedInUser.hasBirthday()) {
                String birthday = signedInUser.getBirthday();
                Log.d("birthday",birthday);
            }

            if (signedInUser.hasCurrentLocation()) {
                String userLocation = signedInUser.getCurrentLocation();
                Log.d("userLocation",userLocation);
            }

            String userEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Log.d("userEmail",userEmail);
            SmartChefApp.saveToPreferences(LoginActivity.this, Keys.email, userEmail);


            if (signedInUser.hasImage()) {
                String userProfilePicUrl = signedInUser.getImage().getUrl();
                // default size is 50x50 in pixels.changes it to desired size
                int profilePicRequestSize = 250;

                userProfilePicUrl = userProfilePicUrl.substring(0,
                        userProfilePicUrl.length() - 2) + profilePicRequestSize;
                Log.d("userProfilePicUrl",userProfilePicUrl);
                SmartChefApp.saveToPreferences(LoginActivity.this, "profile_pic", userProfilePicUrl);
            }

            try {
                jobj.accumulate("id", SmartChefApp.readFromPreferences(LoginActivity.this, "user_id", ""));
                jobj.accumulate("name", SmartChefApp.readFromPreferences(LoginActivity.this, "user_name", ""));
                jobj.accumulate("type", "fb");
                jobj.accumulate("gender", SmartChefApp.readFromPreferences(LoginActivity.this, "gender", ""));
                jobj.accumulate("email", SmartChefApp.readFromPreferences(LoginActivity.this, Keys.email, ""));
                jobj.accumulate("profile_pic", SmartChefApp.readFromPreferences(LoginActivity.this, "profile_pic", ""));
                SmartChefApp.saveToPreferences(LoginActivity.this, "isGplusLogin", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(SmartChefApp.isNetworkAvailable()){
                new postReview().execute(String.valueOf(jobj));
            }else{
                SmartChefApp.showAToast("Internet Unavailable");
            }


        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else if (requestCode == PICK_MEDIA_REQUEST_CODE) {
            // If picking media is success, create share post using
            // PlusShare.Builder
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                ContentResolver cr = getApplicationContext().getContentResolver();
                String mime = cr.getType(selectedImage);

                PlusShare.Builder share = new PlusShare.Builder(LoginActivity.this);
                share.setText("Hello from AndroidSRC.net");
                share.addStream(selectedImage);
                share.setType(mime);
                startActivityForResult(share.getIntent(),
                        SHARE_MEDIA_REQUEST_CODE);
            }
        }
    }


    private void initializeFacebook(){

        callbackManager = CallbackManager.Factory.create();


        loggedIn = isFacebookLoggedIn();

        if(loggedIn){
            doCheck();
            Log.d("Instance","sdfa");
        }

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Log.d("oldToken", " "+oldToken);
                Log.d("newToken", " "+newToken);
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.d("oldProfile", " "+oldProfile);
                Log.d("newProfile", " "+newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.e("Facebook", object.toString());

                                try {
                                    fbId = object.getString("id");

                                    URL profile_pic = new URL("https://graph.facebook.com/" + fbId + "/picture?width=200&height=150");
                                    Log.i("profile_pic", profile_pic + "");
                                    SmartChefApp.saveToPreferences(LoginActivity.this, "user_id", fbId);
                                    SmartChefApp.saveToPreferences(LoginActivity.this, "profile_pic", profile_pic.toString());
                                    SmartChefApp.saveToPreferences(LoginActivity.this, "user_name",object.getString("first_name")+" "+object.getString("last_name"));
                                    SmartChefApp.saveToPreferences(LoginActivity.this, "login_type","fb");
                                    SmartChefApp.saveToPreferences(LoginActivity.this, "gender",object.getString("gender"));

                                    if (object.has("email")) {
                                        emailid = object.getString("email");
                                        SmartChefApp.saveToPreferences(LoginActivity.this, Keys.email, emailid);
                                    }


                                    doCheck();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,birthday,gender,email,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();

            }


            @Override
            public void onCancel() {
                Log.d("print", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });

    }



    private void doCheck() {

        Profile profile = Profile.getCurrentProfile();
        AccessToken  token = AccessToken.getCurrentAccessToken();


        if (profile != null) {
            uid = profile.getId();

        }

        if (token != null) {
            uid = token.getUserId();
            access_token = token.getToken();

            /*{"id":"1234271433254379",
                    "name":"Nihas Nizar",
                    "type":"fb/google",
                    "gender":"male",
                    "email":"nihasnizar@gmail.com",
                    "profile_pic": "https://graph.facebook.com/1234271433254379/picture?width=200&height=150"
            }*/

            JSONObject jsonPost = new JSONObject();
            try {
                jsonPost.accumulate("id", SmartChefApp.readFromPreferences(LoginActivity.this, "user_id", ""));
            jsonPost.accumulate("name", SmartChefApp.readFromPreferences(LoginActivity.this, "user_name", ""));
            jsonPost.accumulate("type", "fb");
            jsonPost.accumulate("gender", SmartChefApp.readFromPreferences(LoginActivity.this, "gender", ""));
                jsonPost.accumulate("email", SmartChefApp.readFromPreferences(LoginActivity.this, Keys.email, ""));
                jsonPost.accumulate("profile_pic", SmartChefApp.readFromPreferences(LoginActivity.this, "profile_pic", ""));
            if(SmartChefApp.isNetworkAvailable()){
                new postReview().execute(String.valueOf(jsonPost));
            }else{
                SmartChefApp.showAToast("Internet Unavailable");
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        Log.d("FacebookId", " " + fbId + " email" + SmartChefApp.readFromPreferences(LoginActivity.this, Keys.email, Constants.DEFAULT_STRING) + "uid " + uid + "access_token " + access_token);



    }

    private class postReview extends AsyncTask<String, Void, JSONObject> {


        ProgressDialog pdialog=new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage("Verifying User");
            pdialog.setCancelable(false);
            pdialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {
                return WebRequest.postData(params[0], WebServices.registerUser);

            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jobj) {
            super.onPostExecute(jobj);
            pdialog.dismiss();
            Log.e("REVIEW",jobj+"");
            try {
                if(jobj.getString("status").equals("true") || jobj.getString("status").equals("exists")){
                    SmartChefApp.showAToast("Success");
//                    if(SmartChefApp.readFromPreferences(LoginActivity.this,"preference",false)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
//                    }else{
//                        Intent intent = new Intent(LoginActivity.this, PreferActivity.class);
//                        startActivity(intent);
//                    }

                    overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
                    finish();
                }else{
                    SmartChefApp.showAToast("Problem with login Please try again");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.fb:
                loginButton.performClick();
                break;
            case R.id.googlesignin:
                processSignIn();
                break;

        }
    }

    public static void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private int iconResId, titleArrayResId, hintArrayResId;

        public ViewPagerAdapter(int iconResId, int titleArrayResId, int hintArrayResId) {

            this.iconResId = iconResId;
            this.titleArrayResId = titleArrayResId;
            this.hintArrayResId = hintArrayResId;
        }

        @Override
        public int getCount() {
            return getResources().getIntArray(iconResId).length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Drawable icon = getResources().obtainTypedArray(iconResId).getDrawable(position);
            String title = getResources().getStringArray(titleArrayResId)[position];
            String hint = getResources().getStringArray(hintArrayResId)[position];


            View itemView = getLayoutInflater().inflate(R.layout.viewpager_item, container, false);


            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            TextView titleView = (TextView) itemView.findViewById(R.id.landing_txt_title);
            TextView hintView = (TextView) itemView.findViewById(R.id.landing_txt_hint);


            iconView.setImageDrawable(icon);
            titleView.setText(title);
            hintView.setText(hint);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }
    }


}

package com.nihas.smart.chef.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.RecipeActivity;
import com.nihas.smart.chef.activities.RecipeDetailsActivity;
import com.nihas.smart.chef.adapters.ReviewAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.customui.CircleImageView;
import com.nihas.smart.chef.pojos.ReviewPojo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nihas on 21-02-2016.
 */

public class ReviewActivity extends AppCompatActivity {

    Toolbar toolbar;
//    CircleImageView pro_pic;
//    RatingBar ratingBar;
//    EditText review;
    Button post;
    RecyclerView recyclerView;
//    ProgressBar pBar;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    TextView emptyView;
    String review_detail;
    float rating_value;
    JSONObject jsonPost;
    Bundle bundle;

    public static final ArrayList<ReviewPojo> rvwList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_dialog);
        overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        bundle=getIntent().getExtras();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Reviews");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        pro_pic=(CircleImageView)findViewById(R.id.profile);
//        title=(TextView)view.findViewById(R.id.title);
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        emptyView=(TextView)findViewById(R.id.empty_view);

        emptyView.setVisibility(View.GONE);
        if(SmartChefApp.isNetworkAvailable()){
            new getReviews().execute(bundle.getString("rid"));
        }else{
            SmartChefApp.showAToast("Internet Unavailable");
        }
        if(!RecipeDetailsActivity.rvwList.isEmpty()) {


        }else{
            emptyView.setVisibility(View.VISIBLE);
        }

//        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
//        review=(EditText)findViewById(R.id.review);
        post=(Button)findViewById(R.id.post_button);
//        pBar=(ProgressBar)findViewById(R.id.progressBar);
//        review_detail=review.getText().toString().trim();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)

                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();


//        imageLoader.displayImage(SmartChefApp.readFromPreferences(ReviewActivity.this, "profile_pic", ""), pro_pic, options);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ReviewDialog dialogFragment = new ReviewDialog();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, "Sample Fragment");
            }
        });

    }


    private class getReviews extends AsyncTask<String, Void, JSONArray> {

        ProgressDialog pDialog=new ProgressDialog(ReviewActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Fetching Reviews");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonObject = null;
            try {

                return WebRequest.getDataJSONArray(WebServices.getRecipeReviews(params[0]));
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONArray jArray) {
            super.onPostExecute(jArray);
pDialog.dismiss();

          try{

                    rvwList.clear();
                    for (int j = 0; j < jArray.length(); j++) {
                        JSONObject jobjrev = jArray.getJSONObject(j);
                        ReviewPojo rwpojo = new ReviewPojo();
                        rwpojo.setRid(jobjrev.getString("rid"));
                        rwpojo.setUser(jobjrev.getString("user"));
                        if(!jobjrev.isNull("pic"))
                            rwpojo.setPic(jobjrev.getString("pic"));
                        else
                            rwpojo.setPic("http://collegemix.ca/img/placeholder.png");
                        rwpojo.setRating(jobjrev.getString("rating"));
                        rwpojo.setReview(jobjrev.getString("review"));
                        rvwList.add(rwpojo);

                    }




              ReviewAdapter rAdapter=new ReviewAdapter(ReviewActivity.this, rvwList);
              recyclerView.setAdapter(rAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
//            progressBar.setVisibility(View.GONE);
//            onDone(jArray);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

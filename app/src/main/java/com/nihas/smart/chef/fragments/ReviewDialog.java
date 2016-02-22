package com.nihas.smart.chef.fragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
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

/**
 * Created by Nihas on 21-02-2016.
 */

public class ReviewDialog extends DialogFragment {

    CircleImageView pro_pic;
    RatingBar ratingBar;
    EditText review;
    Button submitButton;
    RecyclerView recyclerView;
    ProgressBar pBar;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    TextView emptyView;
    String review_detail;
    float rating_value;
    JSONObject jsonPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.review_dialog, container, false);
        imageLoader = ImageLoader.getInstance();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pro_pic=(CircleImageView)view.findViewById(R.id.profile);
//        title=(TextView)view.findViewById(R.id.title);
        recyclerView=(RecyclerView)view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        emptyView=(TextView)view.findViewById(R.id.empty_view);

        emptyView.setVisibility(View.GONE);
        if(!RecipeDetailsActivity.rvwList.isEmpty()) {

            ReviewAdapter rAdapter=new ReviewAdapter(getContext(), RecipeDetailsActivity.rvwList);
            recyclerView.setAdapter(rAdapter);
        }else{
            emptyView.setVisibility(View.VISIBLE);
        }

        ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);
        review=(EditText)view.findViewById(R.id.review);
        submitButton=(Button)view.findViewById(R.id.submit_button);
        pBar=(ProgressBar)view.findViewById(R.id.progressBar);
        review_detail=review.getText().toString().trim();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jsonPost = new JSONObject();
                    jsonPost.accumulate("rid", RecipeDetailsActivity.rvwList.get(0).getRid());
                    jsonPost.accumulate("user",SmartChefApp.readFromPreferences(getActivity(), "user_id", ""));
                    jsonPost.accumulate("review",review_detail);
                    jsonPost.accumulate("rating",String.valueOf(ratingBar.getRating()));
                    if(SmartChefApp.isNetworkAvailable()){
                        new postReview().execute(String.valueOf(jsonPost));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
//                .displayer(new BitmapDisplayer() {
//            @Override
//            public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
//                int gradientStartColor = Color.parseColor("#00000000");//argb(0, 0, 0, 0);
//                int gradientEndColor = Color.parseColor("#88000000");//argb(255, 0, 0, 0);
//                GradientHalfoverImageDrawable gradientDrawable = new GradientHalfoverImageDrawable(activity.getResources(), bitmap);
//                gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);
//                imageAware.setImageDrawable(gradientDrawable);
//            }
//        })
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();


        imageLoader.displayImage(SmartChefApp.readFromPreferences(getContext(), "profile_pic", ""), pro_pic, options);
    }

    private class postReview extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String jsonObject = null;
            try {
                Log.e("data",String.valueOf(jsonPost));
                return WebRequest.postDataParams("data", params[0],WebServices.postReview);

            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(String jArray) {
            super.onPostExecute(jArray);
            Log.e("REVIEW",jArray);

        }
    }

}

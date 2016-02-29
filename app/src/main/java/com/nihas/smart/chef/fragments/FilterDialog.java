package com.nihas.smart.chef.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.RecipeDetailsActivity;
import com.nihas.smart.chef.adapters.ReviewAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.customui.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nihas on 21-02-2016.
 */

public class FilterDialog extends AppCompatActivity {

    Toolbar toolbar;
    TextView veg,nonVeg;
    Boolean isVeg,isNonVeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_dialog);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Filter");
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

        isVeg=false;
        isNonVeg=false;

        veg=(TextView)findViewById(R.id.veg);
        nonVeg=(TextView)findViewById(R.id.non_veg);
        Button apply=(Button)findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_VEG",false))
            veg.setBackgroundColor(Color.GREEN);
        else
            veg.setBackgroundColor(Color.TRANSPARENT);

        if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_NON_VEG",false))
            nonVeg.setBackgroundColor(Color.GREEN);
        else
            nonVeg.setBackgroundColor(Color.TRANSPARENT);

        veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_VEG",false)){
                    veg.setBackgroundColor(Color.TRANSPARENT);
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_VEG",false);
                }else {
                    veg.setBackgroundColor(Color.GREEN);
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_VEG",true);
                }
            }
        });
        nonVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
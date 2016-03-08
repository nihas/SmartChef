package com.nihas.smart.chef.fragments;

import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nihas.smart.chef.Constants;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.RecipeActivity;
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
    Bundle bundle;
    CheckBox breakfast,snack,lunch,dinner,desert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_dialog);
        overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        bundle=getIntent().getExtras();

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

        breakfast=(CheckBox)findViewById(R.id.breakfast);
        snack=(CheckBox)findViewById(R.id.snack);
        lunch=(CheckBox)findViewById(R.id.lunch);
        dinner=(CheckBox)findViewById(R.id.dinner);
        desert=(CheckBox)findViewById(R.id.desert);

        breakfast.setChecked(false);
        snack.setChecked(false);
        lunch.setChecked(false);
        dinner.setChecked(false);
        desert.setChecked(false);

        if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_BREAKFAST, false)){
            breakfast.setChecked(true);
        }
        if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_SNACK, false)){
            snack.setChecked(true);
        }
        if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_LUNCH, false)){
            lunch.setChecked(true);
        }
        if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_DINNER, false)){
            dinner.setChecked(true);
        }
        if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_DESERT, false)){
            desert.setChecked(true);
        }

        Button apply=(Button)findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isVeg)
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_VEG",true);
                else
                    SmartChefApp.saveToPreferences(getApplicationContext(), "FILTER_VEG", false);

                if(isNonVeg)
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_NON_VEG",true);
                else
                    SmartChefApp.saveToPreferences(getApplicationContext(), "FILTER_NON_VEG", false);

                if(breakfast.isChecked()){
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_BREAKFAST",true);
                }else{
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_BREAKFAST",false);
                }

                if(snack.isChecked()){
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_SNACK",true);
                }else{
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_SNACK",false);
                }

                if(lunch.isChecked()){
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_LUNCH",true);
                }else{
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_LUNCH",false);
                }

                if(dinner.isChecked()){
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_DINNER",true);
                }else{
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_DINNER",false);
                }

                if(desert.isChecked()){
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_DESERT",true);
                }else{
                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_DESERT",false);
                }



                onBackPressed();
            }
        });

        if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_VEG",false)) {
            veg.setBackgroundColor(Color.GREEN);
            isVeg=true;
        }
        else {
            veg.setBackgroundColor(Color.TRANSPARENT);
            isVeg=false;
        }

        if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_NON_VEG",false)) {
            nonVeg.setBackgroundColor(Color.GREEN);
            isNonVeg=true;
        }
        else {
            nonVeg.setBackgroundColor(Color.TRANSPARENT);
            isNonVeg=false;
        }

        veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isVeg){
                    veg.setBackgroundColor(Color.TRANSPARENT);
                    isVeg=false;
                }else {
                    veg.setBackgroundColor(Color.GREEN);
                    isVeg=true;
                    nonVeg.setBackgroundColor(Color.TRANSPARENT);
                    isNonVeg=false;
                }
            }
        });
        nonVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNonVeg){
                    nonVeg.setBackgroundColor(Color.TRANSPARENT);
                    isNonVeg=false;
                }else {
                    nonVeg.setBackgroundColor(Color.GREEN);
                    isNonVeg=true;
                    veg.setBackgroundColor(Color.TRANSPARENT);
                    isVeg=false;
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent=new Intent(FilterDialog.this, RecipeActivity.class);
        backIntent.putExtra("ingredients", bundle.getString("ingredients"));
        startActivity(backIntent);
        finish();
    }
}

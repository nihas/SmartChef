package com.nihas.smart.chef.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nihas.smart.chef.Constants;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.RecipeActivity;
import com.nihas.smart.chef.app.SmartChefApp;

import org.w3c.dom.Text;

/**
 * Created by Nihas on 21-02-2016.
 */

public class FilterLayout extends AppCompatActivity {

//    Toolbar toolbar;
    RadioButton veg,nonVeg,radioRating,radioPopular;
    RadioGroup rGroup,rSortGroup;
    Boolean isVeg,isNonVeg;
    Bundle bundle;
    TextView reset;
    CheckBox breakfast,snack,lunch,dinner,desert,rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_layout);
        overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);
        reset=(TextView)findViewById(R.id.reset);
        ImageView close=(ImageView)findViewById(R.id.wrong);
//        toolbar=(Toolbar)findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
//        setSupportActionBar(toolbar);

        bundle=getIntent().getExtras();

//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setTitle("Filter");
//        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartChefApp.saveToPreferences(getApplicationContext(), "FILTER_VEG", false);
                SmartChefApp.saveToPreferences(getApplicationContext(), "FILTER_NON_VEG", false);

                SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_BREAKFAST",false);
                SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_SNACK",false);
                SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_LUNCH",false);
                SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_DINNER",false);
                SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_DESERT",false);

                SmartChefApp.saveToPreferences(getApplicationContext(),"SORT_RATING",false);
                onBackPressed();
            }
        });

        isVeg=false;
        isNonVeg=false;
        rGroup=(RadioGroup)findViewById(R.id.radioGroupVeg);
        veg=(RadioButton)findViewById(R.id.veg);
        nonVeg=(RadioButton)findViewById(R.id.non_veg);

        rSortGroup=(RadioGroup)findViewById(R.id.radioGroupSort);
        radioRating=(RadioButton)findViewById(R.id.sortRadioRating);
        radioPopular=(RadioButton)findViewById(R.id.sortRadioPopularity);

        breakfast=(CheckBox)findViewById(R.id.breakfast);
        snack=(CheckBox)findViewById(R.id.snack);
        lunch=(CheckBox)findViewById(R.id.lunch);
        dinner=(CheckBox)findViewById(R.id.dinner);
        desert=(CheckBox)findViewById(R.id.desert);
//        rating=(CheckBox)findViewById(R.id.rating);

        breakfast.setChecked(false);
        snack.setChecked(false);
        lunch.setChecked(false);
        dinner.setChecked(false);
        desert.setChecked(false);
//        rating.setChecked(false);
        if(SmartChefApp.readFromPreferences(getApplicationContext(),"SORT_RATING",false))
            radioRating.toggle();
        else
            radioPopular.toggle();
        if(SmartChefApp.readFromPreferences(getApplicationContext(), "FILTER_VEG", false))
            veg.toggle();
        else
            nonVeg.toggle();

        breakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    snack.setChecked(false);
                    lunch.setChecked(false);
                    dinner.setChecked(false);
                    desert.setChecked(false);
                }
            }
        });
        snack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    breakfast.setChecked(false);
                    lunch.setChecked(false);
                    dinner.setChecked(false);
                    desert.setChecked(false);
                }
            }
        });
        lunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    snack.setChecked(false);
                    breakfast.setChecked(false);
                    dinner.setChecked(false);
                    desert.setChecked(false);
                }
            }
        });
        dinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    snack.setChecked(false);
                    lunch.setChecked(false);
                    breakfast.setChecked(false);
                    desert.setChecked(false);
                }
            }
        });
        desert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    snack.setChecked(false);
                    lunch.setChecked(false);
                    dinner.setChecked(false);
                    breakfast.setChecked(false);
                }
            }
        });

        if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_BREAKFAST, false)){
            breakfast.setChecked(true);
            snack.setChecked(false);
            lunch.setChecked(false);
            dinner.setChecked(false);
            desert.setChecked(false);
//            rating.setChecked(false);
        }else if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_SNACK, false)){
            breakfast.setChecked(false);
            snack.setChecked(true);
            lunch.setChecked(false);
            dinner.setChecked(false);
            desert.setChecked(false);
//            rating.setChecked(false);
        }else if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_LUNCH, false)){
            breakfast.setChecked(false);
            snack.setChecked(false);
            lunch.setChecked(true);
            dinner.setChecked(false);
            desert.setChecked(false);
//            rating.setChecked(false);
        }else if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_DINNER, false)){
            breakfast.setChecked(false);
            snack.setChecked(false);
            lunch.setChecked(false);
            dinner.setChecked(true);
            desert.setChecked(false);
//            rating.setChecked(false);
        }else if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_DESERT, false)){
            breakfast.setChecked(false);
            snack.setChecked(false);
            lunch.setChecked(false);
            dinner.setChecked(false);
            desert.setChecked(true);
//            rating.setChecked(false);
        }

//        else if(SmartChefApp.readFromPreferences(getApplicationContext(),"SORT_RATING", false)){
//            breakfast.setChecked(false);
//            snack.setChecked(false);
//            lunch.setChecked(false);
//            dinner.setChecked(false);
//            desert.setChecked(false);
//            rating.setChecked(true);
//        }

        Button apply=(Button)findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = rGroup.getCheckedRadioButtonId();
                if (id == -1){
                    //no item selected
                }
                else{
                    if (id == R.id.veg){
                        //Do something with the button
                        SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_VEG",true);
                        SmartChefApp.saveToPreferences(getApplicationContext(), "FILTER_NON_VEG", false);
                    }else if(id==R.id.non_veg){
                        SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_NON_VEG",true);
                        SmartChefApp.saveToPreferences(getApplicationContext(), "FILTER_VEG", false);
                    }
                }

                int sortid = rGroup.getCheckedRadioButtonId();
                if (sortid == -1){
                    //no item selected
                }
                else{
                    if (sortid == R.id.sortRadioRating){
                        //Do something with the button
                        SmartChefApp.saveToPreferences(getApplicationContext(),"SORT_RATING",true);
                    }else if(sortid==R.id.sortRadioPopularity){
                        SmartChefApp.saveToPreferences(getApplicationContext(),"SORT_RATING",false);
                    }
                }
//                if(isVeg)
//                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_VEG",true);
//                else
//                    SmartChefApp.saveToPreferences(getApplicationContext(), "FILTER_VEG", false);
//
//                if(isNonVeg)
//                    SmartChefApp.saveToPreferences(getApplicationContext(),"FILTER_NON_VEG",true);
//                else
//                    SmartChefApp.saveToPreferences(getApplicationContext(), "FILTER_NON_VEG", false);

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

//                if(rating.isChecked()){
//                    SmartChefApp.saveToPreferences(getApplicationContext(),"SORT_RATING",true);
//                }else{
//                    SmartChefApp.saveToPreferences(getApplicationContext(),"SORT_RATING",false);
//                }



                onBackPressed();
            }
        });

//        if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_VEG",false)) {
//            veg.setBackgroundColor(Color.GREEN);
//            isVeg=true;
//        }
//        else {
//            veg.setBackgroundColor(Color.TRANSPARENT);
//            isVeg=false;
//        }

//        if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_NON_VEG",false)) {
//            nonVeg.setBackgroundColor(Color.GREEN);
//            isNonVeg=true;
//        }
//        else {
//            nonVeg.setBackgroundColor(Color.TRANSPARENT);
//            isNonVeg=false;
//        }

//        veg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isVeg) {
//                    veg.setBackgroundColor(Color.TRANSPARENT);
//                    isVeg = false;
//                } else {
//                    veg.setBackgroundColor(Color.GREEN);
//                    isVeg = true;
//                    nonVeg.setBackgroundColor(Color.TRANSPARENT);
//                    isNonVeg = false;
//                }
//            }
//        });
//        nonVeg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isNonVeg){
//                    nonVeg.setBackgroundColor(Color.TRANSPARENT);
//                    isNonVeg=false;
//                }else {
//                    nonVeg.setBackgroundColor(Color.GREEN);
//                    isNonVeg=true;
//                    veg.setBackgroundColor(Color.TRANSPARENT);
//                    isVeg=false;
//                }
//            }
//        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent=new Intent(FilterLayout.this, RecipeActivity.class);
        backIntent.putExtra("ingredients", bundle.getString("ingredients"));
        startActivity(backIntent);
        finish();
    }
}

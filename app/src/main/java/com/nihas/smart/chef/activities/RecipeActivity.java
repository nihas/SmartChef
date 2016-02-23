package com.nihas.smart.chef.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.IngredientsAdapter;
import com.nihas.smart.chef.adapters.RecipesAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Nihas on 08-11-2015.
 */
public class RecipeActivity extends AppCompatActivity {

    ArrayList<RecipesPojo> listRecipes;
    RecipesAdapter recipAdapter;
    RecyclerView mRecyclerView;
    TextView cupQty;
    ProgressBar progressBar;
    Toolbar toolbar;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Recipes");
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

        progressBar=(ProgressBar)findViewById(R.id.pBar);
        if (SmartChefApp.isNetworkAvailable()) {
            new getRecipe().execute();
        } else {
            SmartChefApp.showAToast("Internet not Connected");
        }


        mRecyclerView=(RecyclerView)findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bundle=getIntent().getExtras();
    }






    private class getRecipe extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {
                String param=bundle.getString("ingredients");
                String encodedpara = URLEncoder.encode(param, "UTF-8");
                return WebRequest.getData(WebServices.searchRecipe(encodedpara, 1));
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jobj) {
            super.onPostExecute(jobj);
            progressBar.setVisibility(View.GONE);
//            onDone(jArray);
//            SmartChefApp.showAToast(jobj+"");
            try {
                if(!jobj.isNull("results")) {
                    if (Integer.parseInt(jobj.getString("results")) > 0) {
                        listRecipes = new ArrayList<>();
                        for (int i = 0; i < Integer.parseInt(jobj.getString("results")); i++) {
//                    for(int i=0;i<10;i++){
                            JSONObject innerjobj = new JSONObject(jobj.getString(String.valueOf(i)));
                            RecipesPojo pojo = new RecipesPojo();

                            pojo.setId(innerjobj.getString(Keys.id));
                            pojo.setName(innerjobj.getString(Keys.name));
                            pojo.setVeg(innerjobj.getString(Keys.veg));
                            pojo.setServes(innerjobj.getString(Keys.serves));
                            if (!innerjobj.isNull(Keys.food_kind))
                                pojo.setFood_kind(innerjobj.getString(Keys.food_kind));
                            if (!innerjobj.isNull(Keys.cuisine))
                                pojo.setCuisine(innerjobj.getString(Keys.cuisine));
                            if (!innerjobj.isNull(Keys.preparation_time))
                                pojo.setPreparation_time(innerjobj.getString(Keys.preparation_time));
                            if (!innerjobj.isNull(Keys.media_url))
                                pojo.setMedia_url(innerjobj.getString(Keys.media_url));
                            else
                                pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                            if (!innerjobj.isNull(Keys.media_type))
                                pojo.setMedia_type(innerjobj.getString(Keys.media_type));

                            listRecipes.add(pojo);
                        }


                        recipAdapter = new RecipesAdapter(RecipeActivity.this, listRecipes);
                        mRecyclerView.setAdapter(recipAdapter);
                    } else {
                        SmartChefApp.showAToast("No Data Available");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                SmartChefApp.showAToast("SOMETHNG WRONG");
            }
        }
    }


    @Override
    public void onBackPressed() {
//        Intent intent=new Intent(RecipeActivity.this,MainActivity.class);
//        intent.putExtra("MESSAGE","msg");
//        setResult(1, intent);
//        startActivity(intent);
        finish();

    }
}

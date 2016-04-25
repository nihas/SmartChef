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
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nihas.smart.chef.Constants;
import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.IngredientsAdapter;
import com.nihas.smart.chef.adapters.RecipesAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.fragments.DrawerFragment;
import com.nihas.smart.chef.fragments.FilterDialog;
import com.nihas.smart.chef.fragments.FilterLayout;
import com.nihas.smart.chef.fragments.ReviewDialog;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.utils.EndlessRecyclerOnScrollListener;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Nihas on 08-11-2015.
 */
public class RecipeActivity extends AppCompatActivity {

    ArrayList<RecipesPojo> listRecipes;
    RecipesAdapter recipAdapter;
    RecyclerView mRecyclerView;
    TextView cupQty;
    static TextView emptyView;
    ProgressBar progressBar;
    Toolbar toolbar;
    Bundle bundle;
    ArrayList<RecipesPojo> filterList;
    ArrayList<RecipesPojo> filterTypeList;
    int page=1;

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
        bundle=getIntent().getExtras();
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

        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_drawer,new DrawerFragment().newInstance(drawerLayout), "CupFragment")
                .commit();

        try{
            if (SmartChefApp.isNetworkAvailable()) {
                String param=bundle.getString("ingredients");
                String encodedpara = URLEncoder.encode(param, "UTF-8");

                new getRecipe().execute(WebServices.searchRecipe(encodedpara, 1)+getFilteredUrl());
            } else {
                SmartChefApp.showAToast("Internet not Connected");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        emptyView=(TextView)findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);
        mRecyclerView=(RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int current_page) {
                Log.d("POSITION"," "+current_page);
                if(!recipAdapter.isFooterEnabled() && recipAdapter.getItemCount() > 0) {

                }else{
                    ScrollApiCall(current_page);
                }
            }
        });



//        if (SmartChefApp.isNetworkAvailable()) {
//            new getRecipe().execute();
//        } else {
//            SmartChefApp.showAToast("Internet not Connected");
//        }
    }


    private void ScrollApiCall(int current_page) {

        try{

        if (SmartChefApp.isNetworkAvailable()) {


            String param=bundle.getString("ingredients");
            String encodedpara = URLEncoder.encode(param, "UTF-8");

            new getRecipeScroll().execute(WebServices.searchRecipe(encodedpara, current_page)+getFilteredUrl());
        } else {
            SmartChefApp.showAToast("Internet not Connected");
        }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        try{
//        if (SmartChefApp.isNetworkAvailable()) {
//            String param=bundle.getString("ingredients");
//            String encodedpara = URLEncoder.encode(param, "UTF-8");
//
//            new getRecipe().execute(WebServices.searchRecipe(encodedpara, 1)+getFilteredUrl());
//        } else {
//            SmartChefApp.showAToast("Internet not Connected");
//        }
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            Intent filterIntent=new Intent(RecipeActivity.this,FilterLayout.class);
            filterIntent.putExtra("ingredients", bundle.getString("ingredients"));
            startActivity(filterIntent);
            finish();
//            FragmentManager fm = getSupportFragmentManager();
//            FilterDialog dialogFragment = new FilterDialog();
//            dialogFragment.show(fm, "Sample Fragment");
        }
        return super.onOptionsItemSelected(item);
    }


    private class getRecipe extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {
//                String param=bundle.getString("ingredients");
//                String encodedpara = URLEncoder.encode(param, "UTF-8");
//                if(SmartChefApp.readFromPreferences(getApplicationContext(),"SORT_RATING", false)) {
//                    return WebRequest.getData(WebServices.searchRecipeSortRating(encodedpara, 1));
//                }else{
                    return WebRequest.getData(params[0]);
//                }
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
            if (jobj != null) {
                try {
                    if (jobj.length() != 0) {
                        if (!jobj.isNull("results")) {

                            page=Integer.parseInt(jobj.getString("pages"));

                            JSONObject logJson=new JSONObject();
                            logJson.accumulate("id", SmartChefApp.readFromPreferences(RecipeActivity.this, "user_id", ""));
                            logJson.accumulate("ingredients", bundle.getString("ingredients"));
                            logJson.accumulate("recipes",jobj.getString("results"));
                            if(SmartChefApp.isNetworkAvailable()){
                                new LogDetails().execute(logJson.toString());
                            }

                            if (Integer.parseInt(jobj.getString("results")) > 0) {
                                Iterator<String> keys = jobj.keys();
                                listRecipes = new ArrayList<>();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    if(!key.equals("results") && !key.equals("pages")) {
                                        JSONObject innerjobj = jobj.getJSONObject(key);
//                                    for (int i = 0; i < Integer.parseInt(jobj.getString("results")); i++) {
//                    for(int i=0;i<10;i++){
//                                        JSONObject innerjobj = new JSONObject(jobj.getString(String.valueOf(i)));
                                        RecipesPojo pojo = new RecipesPojo();

                                        pojo.setId(innerjobj.getString(Keys.id));
                                        pojo.setName(innerjobj.getString(Keys.name));
                                        pojo.setVeg(innerjobj.getString(Keys.veg));
                                        pojo.setServes(innerjobj.getString(Keys.serves));
                                        if (!innerjobj.isNull(Keys.reference))
                                            pojo.setReference(innerjobj.getString(Keys.reference));
                                        if (!innerjobj.isNull(Keys.food_kind))
                                            pojo.setFood_kind(innerjobj.getString(Keys.food_kind));
                                        else
                                            pojo.setFood_kind("");
                                        if (!innerjobj.isNull(Keys.cuisine))
                                            pojo.setCuisine(innerjobj.getString(Keys.cuisine));
                                        else
                                            pojo.setCuisine("");
                                        if (!innerjobj.isNull(Keys.preparation_time))
                                            pojo.setPreparation_time(innerjobj.getString(Keys.preparation_time));
                                        else
                                            pojo.setPreparation_time("");
                                        if (!innerjobj.isNull(Keys.media_url))
                                            pojo.setMedia_url(innerjobj.getString(Keys.media_url));
                                        else
                                            pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                                        if (!innerjobj.isNull(Keys.media_type))
                                            pojo.setMedia_type(innerjobj.getString(Keys.media_type));
                                        else
                                            pojo.setMedia_type("");
                                        if (!innerjobj.isNull("rating")) {
                                            String s = String.format("%.2f", Float.parseFloat(innerjobj.getString("rating")));
                                            pojo.setRating(s);
                                        }else
                                            pojo.setRating("0");
                                        listRecipes.add(pojo);
                                    }
                                }


//                                if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_VEG",false)) {
//                                    RecipesAdapter recipAdapter = new RecipesAdapter(RecipeActivity.this, getfilterList( //filterng for veg nonveg
//                                            getfilterTypeList(// filtering for brakfast snack lunch dinner desert
//                                            listRecipes,SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_BREAKFAST,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_SNACK,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_LUNCH,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DINNER,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DESERT,false)),true));
//
//                                    mRecyclerView.setAdapter(recipAdapter);
//                                }else if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_NON_VEG",false)) {
//                                    RecipesAdapter recipAdapter = new RecipesAdapter(RecipeActivity.this, getfilterList( //filterng for veg nonveg
//                                            getfilterTypeList(// filtering for brakfast snack lunch dinner desert
//                                                    listRecipes,SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_BREAKFAST,false),
//                                                    SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_SNACK,false),
//                                                    SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_LUNCH,false),
//                                                    SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DINNER,false),
//                                                    SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DESERT,false)),false));
//                                    mRecyclerView.setAdapter(recipAdapter);
//                                }
//                                else{
                                    recipAdapter = new RecipesAdapter(RecipeActivity.this,listRecipes);
                                    mRecyclerView.setAdapter(recipAdapter);
//                                }

                            } else {
                                SmartChefApp.showAToast("No Data Available");
                                showEmptyView();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    SmartChefApp.showAToast("SOMETHNG WRONG");
                }
            }else{
                showEmptyView();
            }
        }
    }



    private class getRecipeScroll extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonObject = null;
            try {
//                String param=bundle.getString("ingredients");
//                String encodedpara = URLEncoder.encode(param, "UTF-8");
//                if(SmartChefApp.readFromPreferences(getApplicationContext(),"SORT_RATING", false)) {
//                    return WebRequest.getData(WebServices.searchRecipeSortRating(encodedpara, 1));
//                }else{
                return WebRequest.getDataJSONArray(params[0]);
//                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONArray jobj) {
            super.onPostExecute(jobj);
            progressBar.setVisibility(View.GONE);
//            onDone(jArray);
//            SmartChefApp.showAToast(jobj+"");
            if (jobj != null) {
                try {
//                    if (jobj.length() != 0) {
                        if (jobj.length()>0) {


//                            page=Integer.parseInt(jobj.getString("pages"));
                        for(int i=0;i<jobj.length();i++){
//                            if (Integer.parseInt(jobj.getString("results")) > 0) {
//                                Iterator<String> keys = jobj.keys();
//                                listRecipes = new ArrayList<>();
//                                while (keys.hasNext()) {
//                                    String key = keys.next();
//                                    if (!key.equals("results") && !key.equals("pages")) {
                                        JSONObject innerjobj = jobj.getJSONObject(i);
//                                    for (int i = 0; i < Integer.parseInt(jobj.getString("results")); i++) {
//                    for(int i=0;i<10;i++){
//                                        JSONObject innerjobj = new JSONObject(jobj.getString(String.valueOf(i)));
                                        RecipesPojo pojo = new RecipesPojo();

                                        pojo.setId(innerjobj.getString(Keys.id));
                                        pojo.setName(innerjobj.getString(Keys.name));
                                        pojo.setVeg(innerjobj.getString(Keys.veg));
                                        pojo.setServes(innerjobj.getString(Keys.serves));
                                        if (!innerjobj.isNull(Keys.reference))
                                            pojo.setReference(innerjobj.getString(Keys.reference));
                                        if (!innerjobj.isNull(Keys.food_kind))
                                            pojo.setFood_kind(innerjobj.getString(Keys.food_kind));
                                        else
                                            pojo.setFood_kind("");
                                        if (!innerjobj.isNull(Keys.cuisine))
                                            pojo.setCuisine(innerjobj.getString(Keys.cuisine));
                                        else
                                            pojo.setCuisine("");
                                        if (!innerjobj.isNull(Keys.preparation_time))
                                            pojo.setPreparation_time(innerjobj.getString(Keys.preparation_time));
                                        else
                                            pojo.setPreparation_time("");
                                        if (!innerjobj.isNull(Keys.media_url))
                                            pojo.setMedia_url(innerjobj.getString(Keys.media_url));
                                        else
                                            pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                                        if (!innerjobj.isNull(Keys.media_type))
                                            pojo.setMedia_type(innerjobj.getString(Keys.media_type));
                                        else
                                            pojo.setMedia_type("");
                                        if (!innerjobj.isNull("rating")) {
                                            String s = String.format("%.2f", Float.parseFloat(innerjobj.getString("rating")));
                                            pojo.setRating(s);
                                        } else
                                            pojo.setRating("0");
                                        listRecipes.add(pojo);


                                    }
                                }else {
//                            SmartChefApp.showAToast("No Data Available");
                            recipAdapter.enableFooter(false);
//                            showEmptyView();
                        }




//                                if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_VEG",false)) {
//                                    RecipesAdapter recipAdapter = new RecipesAdapter(RecipeActivity.this, getfilterList( //filterng for veg nonveg
//                                            getfilterTypeList(// filtering for brakfast snack lunch dinner desert
//                                            listRecipes,SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_BREAKFAST,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_SNACK,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_LUNCH,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DINNER,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DESERT,false)),true));
//
//                                    mRecyclerView.setAdapter(recipAdapter);
//                                }else if(SmartChefApp.readFromPreferences(getApplicationContext(),"FILTER_NON_VEG",false)) {
//                                    RecipesAdapter recipAdapter = new RecipesAdapter(RecipeActivity.this, getfilterList( //filterng for veg nonveg
//                                            getfilterTypeList(// filtering for brakfast snack lunch dinner desert
//                                                    listRecipes,SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_BREAKFAST,false),
//                                                    SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_SNACK,false),
//                                                    SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_LUNCH,false),
//                                                    SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DINNER,false),
//                                                    SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DESERT,false)),false));
//                                    mRecyclerView.setAdapter(recipAdapter);
//                                }
//                                else{
//                                recipAdapter = new RecipesAdapter(RecipeActivity.this,listRecipes);
//                                mRecyclerView.setAdapter(recipAdapter);
//                                int curSize = recipAdapter.getItemCount();
                                recipAdapter.notifyDataSetChanged();//(curSize, listRecipes.size() - 1);
//                                }




//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    SmartChefApp.showAToast("SOMETHNG WRONG");
                }
            }else{
                showEmptyView();
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

    public ArrayList<RecipesPojo> getfilterList(ArrayList<RecipesPojo> original,boolean veg){
        filterList=new ArrayList<>();
        if(veg) {
            for (int i = 0; i < original.size(); i++) {
                if(original.get(i).getVeg().equals("1")){
                    RecipesPojo pojo=new RecipesPojo();
                    pojo.setId(original.get(i).getId());
                    pojo.setName(original.get(i).getName());
                    pojo.setVeg(original.get(i).getVeg());
                    pojo.setServes(original.get(i).getServes());
                    if (original.get(i).getFood_kind()!=null)
                        pojo.setFood_kind(original.get(i).getFood_kind());
                    else
                        pojo.setFood_kind("");
                    if (original.get(i).getCuisine()!=null)
                        pojo.setCuisine(original.get(i).getCuisine());
                    else
                        pojo.setCuisine("");
                    if (original.get(i).getPreparation_time()!=null)
                        pojo.setPreparation_time(original.get(i).getPreparation_time());
                    else
                        pojo.setPreparation_time("");
                    if (original.get(i).getMedia_url()!=null)
                        pojo.setMedia_url(original.get(i).getMedia_url());
                    else
                        pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                    if (original.get(i).getMedia_type()!=null)
                        pojo.setMedia_type(original.get(i).getMedia_type());
                    else
                        pojo.setMedia_type("");

                    filterList.add(pojo);
                }
            }
        }else{
            for (int i = 0; i < original.size(); i++) {
                if(original.get(i).getVeg().equals("0")){
                    RecipesPojo pojo=new RecipesPojo();
                    pojo.setId(original.get(i).getId());
                    pojo.setName(original.get(i).getName());
                    pojo.setVeg(original.get(i).getVeg());
                    pojo.setServes(original.get(i).getServes());
                    if (original.get(i).getFood_kind()!=null)
                        pojo.setFood_kind(original.get(i).getFood_kind());
                    else
                        pojo.setFood_kind("");
                    if (original.get(i).getCuisine()!=null)
                        pojo.setCuisine(original.get(i).getCuisine());
                    else
                        pojo.setCuisine("");
                    if (original.get(i).getPreparation_time()!=null)
                        pojo.setPreparation_time(original.get(i).getPreparation_time());
                    else
                        pojo.setPreparation_time("");
                    if (original.get(i).getMedia_url()!=null)
                        pojo.setMedia_url(original.get(i).getMedia_url());
                    else
                        pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                    if (original.get(i).getMedia_type()!=null)
                        pojo.setMedia_type(original.get(i).getMedia_type());
                    else
                        pojo.setMedia_type("");

                    filterList.add(pojo);
                }
            }

        }
        if(filterList.size()>0)
        return filterList;
        else
            return original;
    }

//    SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_BREAKFAST,false),
    //                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_SNACK,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_LUNCH,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DINNER,false),
//                                            SmartChefApp.readFromPreferences(getApplicationContext(),Constants.FILTER_DESERT,false)
    public String getFilteredUrl(){
        String param="";
        if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_BREAKFAST,false)){
            param=param+"&kind=breakfast";
        }else if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_SNACK,false)){
            param=param+"&kind=snack";
        }else if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_LUNCH,false)){
            param=param+"&kind=lunch";
        }else if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_DINNER,false)){
            param=param+"&kind=dinner";
        }else if(SmartChefApp.readFromPreferences(getApplicationContext(), Constants.FILTER_DESERT,false)){
            param=param+"&kind=dessert";
        }else{
            param=param+"";
        }

        if (SmartChefApp.readFromPreferences(getApplicationContext(), "TIMING", "").equals("15")) {
            param=param+"&time=15";
        }else if (SmartChefApp.readFromPreferences(getApplicationContext(), "TIMING", "").equals("30")) {
            param=param+"&time=30";
        }else if (SmartChefApp.readFromPreferences(getApplicationContext(), "TIMING", "").equals("40")) {
            param=param+"&time=40";
        }else{
            param=param+"";
        }

        if (SmartChefApp.readFromPreferences(getApplicationContext(), "SORT_RATING", false)) {
            param=param+"&sort=rating";
        }else{
            param=param+"";
        }

        if (SmartChefApp.readFromPreferences(getApplicationContext(), "FILTER_VEG", false)){
            param=param+"&veg=1";
        }else if(SmartChefApp.readFromPreferences(getApplicationContext(), "FILTER_NON_VEG", false)){
            param=param+"&veg=0";
        }else{
            param=param+"";
        }

        return param;
    }

    public ArrayList<RecipesPojo> getfilterTypeList(ArrayList<RecipesPojo> original,boolean breakfast,boolean snack,boolean lunch,boolean dinner,boolean dessert){
        if(breakfast || snack || lunch || dinner || dessert) {
            filterTypeList = new ArrayList<>();
            if (breakfast) {
                for (int i = 0; i < original.size(); i++) {
                    if (original.get(i).getFood_kind().equals("breakfast")) {
                        RecipesPojo pojo = new RecipesPojo();
                        pojo.setId(original.get(i).getId());
                        pojo.setName(original.get(i).getName());
                        pojo.setVeg(original.get(i).getVeg());
                        pojo.setServes(original.get(i).getServes());
                        if (original.get(i).getFood_kind() != null)
                            pojo.setFood_kind(original.get(i).getFood_kind());
                        else
                            pojo.setFood_kind("");
                        if (original.get(i).getCuisine() != null)
                            pojo.setCuisine(original.get(i).getCuisine());
                        else
                            pojo.setCuisine("");
                        if (original.get(i).getPreparation_time() != null)
                            pojo.setPreparation_time(original.get(i).getPreparation_time());
                        else
                            pojo.setPreparation_time("");
                        if (original.get(i).getMedia_url() != null)
                            pojo.setMedia_url(original.get(i).getMedia_url());
                        else
                            pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                        if (original.get(i).getMedia_type() != null)
                            pojo.setMedia_type(original.get(i).getMedia_type());
                        else
                            pojo.setMedia_type("");

                        filterTypeList.add(pojo);
                    }
                }
            }
            if (snack) {
                for (int i = 0; i < original.size(); i++) {
                    if (original.get(i).getFood_kind().equals("snack")) {
                        RecipesPojo pojo = new RecipesPojo();
                        pojo.setId(original.get(i).getId());
                        pojo.setName(original.get(i).getName());
                        pojo.setVeg(original.get(i).getVeg());
                        pojo.setServes(original.get(i).getServes());
                        if (original.get(i).getFood_kind() != null)
                            pojo.setFood_kind(original.get(i).getFood_kind());
                        else
                            pojo.setFood_kind("");
                        if (original.get(i).getCuisine() != null)
                            pojo.setCuisine(original.get(i).getCuisine());
                        else
                            pojo.setCuisine("");
                        if (original.get(i).getPreparation_time() != null)
                            pojo.setPreparation_time(original.get(i).getPreparation_time());
                        else
                            pojo.setPreparation_time("");
                        if (original.get(i).getMedia_url() != null)
                            pojo.setMedia_url(original.get(i).getMedia_url());
                        else
                            pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                        if (original.get(i).getMedia_type() != null)
                            pojo.setMedia_type(original.get(i).getMedia_type());
                        else
                            pojo.setMedia_type("");

                        filterTypeList.add(pojo);
                    }
                }
            }
            if (lunch) {
                for (int i = 0; i < original.size(); i++) {
                    if (original.get(i).getFood_kind().equals("lunch")) {
                        RecipesPojo pojo = new RecipesPojo();
                        pojo.setId(original.get(i).getId());
                        pojo.setName(original.get(i).getName());
                        pojo.setVeg(original.get(i).getVeg());
                        pojo.setServes(original.get(i).getServes());
                        if (original.get(i).getFood_kind() != null)
                            pojo.setFood_kind(original.get(i).getFood_kind());
                        else
                            pojo.setFood_kind("");
                        if (original.get(i).getCuisine() != null)
                            pojo.setCuisine(original.get(i).getCuisine());
                        else
                            pojo.setCuisine("");
                        if (original.get(i).getPreparation_time() != null)
                            pojo.setPreparation_time(original.get(i).getPreparation_time());
                        else
                            pojo.setPreparation_time("");
                        if (original.get(i).getMedia_url() != null)
                            pojo.setMedia_url(original.get(i).getMedia_url());
                        else
                            pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                        if (original.get(i).getMedia_type() != null)
                            pojo.setMedia_type(original.get(i).getMedia_type());
                        else
                            pojo.setMedia_type("");

                        filterTypeList.add(pojo);
                    }
                }
            }
            if (dinner) {
                for (int i = 0; i < original.size(); i++) {
                    if (original.get(i).getFood_kind().equals("dinner")) {
                        RecipesPojo pojo = new RecipesPojo();
                        pojo.setId(original.get(i).getId());
                        pojo.setName(original.get(i).getName());
                        pojo.setVeg(original.get(i).getVeg());
                        pojo.setServes(original.get(i).getServes());
                        if (original.get(i).getFood_kind() != null)
                            pojo.setFood_kind(original.get(i).getFood_kind());
                        else
                            pojo.setFood_kind("");
                        if (original.get(i).getCuisine() != null)
                            pojo.setCuisine(original.get(i).getCuisine());
                        else
                            pojo.setCuisine("");
                        if (original.get(i).getPreparation_time() != null)
                            pojo.setPreparation_time(original.get(i).getPreparation_time());
                        else
                            pojo.setPreparation_time("");
                        if (original.get(i).getMedia_url() != null)
                            pojo.setMedia_url(original.get(i).getMedia_url());
                        else
                            pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                        if (original.get(i).getMedia_type() != null)
                            pojo.setMedia_type(original.get(i).getMedia_type());
                        else
                            pojo.setMedia_type("");

                        filterTypeList.add(pojo);
                    }
                }
            }
            if (dessert) {
                for (int i = 0; i < original.size(); i++) {
                    if (original.get(i).getFood_kind().equals("dessert")) {
                        RecipesPojo pojo = new RecipesPojo();
                        pojo.setId(original.get(i).getId());
                        pojo.setName(original.get(i).getName());
                        pojo.setVeg(original.get(i).getVeg());
                        pojo.setServes(original.get(i).getServes());
                        if (original.get(i).getFood_kind() != null)
                            pojo.setFood_kind(original.get(i).getFood_kind());
                        else
                            pojo.setFood_kind("");
                        if (original.get(i).getCuisine() != null)
                            pojo.setCuisine(original.get(i).getCuisine());
                        else
                            pojo.setCuisine("");
                        if (original.get(i).getPreparation_time() != null)
                            pojo.setPreparation_time(original.get(i).getPreparation_time());
                        else
                            pojo.setPreparation_time("");
                        if (original.get(i).getMedia_url() != null)
                            pojo.setMedia_url(original.get(i).getMedia_url());
                        else
                            pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                        if (original.get(i).getMedia_type() != null)
                            pojo.setMedia_type(original.get(i).getMedia_type());
                        else
                            pojo.setMedia_type("");

                        filterTypeList.add(pojo);
                    }
                }
            }


            return filterTypeList;
        }else{
            return original;
        }
    }


    private class LogDetails extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {
//                String param=bundle.getString("ingredients");
//                String encodedpara = URLEncoder.encode(param, "UTF-8");
//                if(SmartChefApp.readFromPreferences(getApplicationContext(),"SORT_RATING", false)) {
//                    return WebRequest.getData(WebServices.searchRecipeSortRating(encodedpara, 1));
//                }else{
                return WebRequest.postData(params[0], WebServices.logging);
//                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jobj) {
            super.onPostExecute(jobj);
        }
    }

    public static void showEmptyView(){
        emptyView.setVisibility(View.VISIBLE);
    }

    public static void hideEmptyView(){
        emptyView.setVisibility(View.GONE);
    }




}

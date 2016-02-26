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
import android.support.v7.widget.GridLayoutManager;
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
import com.nihas.smart.chef.adapters.CategoryAdapter;
import com.nihas.smart.chef.adapters.IngredientsAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by snyxius on 10/23/2015.
 */
public class IngredientsActivity extends AppCompatActivity{

    RecyclerView mRecyclerView;
    ArrayList<IngredientsPojo> listIngredients;
    IngredientsAdapter ingAdapter;
    ProgressBar progressBar;
    static TextView cupQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("COMNG");
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
            new getAllCategories().execute();
        } else {
            SmartChefApp.showAToast("Network Unavailable");
        }

        mRecyclerView=(RecyclerView)findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    private class getAllCategories extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonObject = null;
            try {

                return WebRequest.getDataJSONArray(WebServices.getIngredients(SmartChefApp.readFromPreferences(IngredientsActivity.this, "ID", 1)));
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONArray jArray) {
            super.onPostExecute(jArray);
            progressBar.setVisibility(View.GONE);
            onDone(jArray);
        }
    }


    private void onDone(JSONArray jArray){
        try {
            if(jArray != null) {
                listIngredients = new ArrayList<>();
                if (jArray.length() > 0) {
                    for (int i = 0; i < jArray.length(); i++) {
//                            AllPojo cp = new AllPojo();
////                            cp.setName(jArray.getString(i));
                        listIngredients.add(new IngredientsPojo(jArray.getJSONObject(i).getString(Keys.name),
                                jArray.getJSONObject(i).getString(Keys.image)));
                    }
                } else {
                    SmartChefApp.showAToast("Something Went Wrong.");
                }

//                    final EstablishmentTypeAdapter adapter = new EstablishmentTypeAdapter(getContext(), estTypeListArray);
//                    typeList.setAdapter(adapter);
                ingAdapter=new IngredientsAdapter(IngredientsActivity.this,listIngredients);
                mRecyclerView.setAdapter(ingAdapter);





            }else{
                SmartChefApp.showAToast("Something Went Wrong.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_cup);
        MenuItemCompat.setActionView(item, R.layout.cup_icon);
        View view = MenuItemCompat.getActionView(item);
        cupQty=(TextView)view.findViewById(R.id.cup_qty);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //do stuff here

                Intent intent = new Intent(IngredientsActivity.this, CupActivity.class);
//                startActivity(intent);
                startActivity(intent);
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.container_drawer,new CupFragment().newInstance(drawerLayout), "CupFragment")
//                        .commit();
//                if(isOpen) {
//                    drawerLayout.closeDrawer(Gravity.RIGHT);
//                    isOpen=false;
//                } else {
//                    drawerLayout.openDrawer(Gravity.RIGHT);
//                    isOpen=true;
//                }

//                getSupportFragmentManager().beginTransaction().replace(R.id.container,new CupFragment()).addToBackStack(null).commit();

//                getSupportFragmentManager().beginTransaction().replace(R.id.container,new CupFragment()).addToBackStack(null).commit();
//                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        });

        MyDbHandler dbHandler = new MyDbHandler(this, null, null, 1);
        Cursor c=dbHandler.getAllCup();
        CupPojo pojo=new CupPojo();
        if(c==null) {
            pojo.setCup_count(0);
        } else {
            pojo.setCup_count(c.getCount());
        }
        cupQty.setText(String.valueOf(pojo.getCup_count()));


        return true;
    }



    public static void updateCupValue(int size) {
        cupQty.setText(String.valueOf(size));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cup) {

            return true;
        }else if(id==R.id.action_search){
//            showSearchView();
//            Intent searchInten=new Intent(MainActivity.this,SearchActivity.class);
//            startActivity(searchInten);
//            return true;

        }


        return super.onOptionsItemSelected(item);
    }







}

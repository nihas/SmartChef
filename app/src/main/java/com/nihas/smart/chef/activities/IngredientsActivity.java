package com.nihas.smart.chef.activities;

import android.content.Intent;
import android.database.Cursor;
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

//    RecyclerView mRecyclerView;
//    ArrayList<IngredientsPojo> listIngredients;
//    IngredientsAdapter ingAdapter;
    static TextView cupQty;
//    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        progressBar=(ProgressBar)findViewById(R.id.pBar);
//        if (SmartChefApp.isNetworkAvailable()) {
//            new getAllCategories().execute();
//        } else {
//
//        }
//
//        mRecyclerView=(RecyclerView)findViewById(R.id.rv);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        ingAdapter=new IngredientsAdapter(this,getIngredients());
//        mRecyclerView.setAdapter(ingAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//        ingAdapter.setOnDataChangeListener(new IngredientsAdapter.OnDataChangeListener() {
//            @Override
//            public void onDataChanged(int size) {
//                CupPojo pojo=new CupPojo();
//                cupQty.setText(String.valueOf(pojo.getCup_count()));
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_cup);
        MenuItemCompat.setActionView(item, R.layout.cup_icon);
        View view = MenuItemCompat.getActionView(item);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //do stuff here
                Intent intent = new Intent(IngredientsActivity.this, CupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        });



        cupQty=(TextView)view.findViewById(R.id.cup_qty);
        MyDbHandler dbHandler = new MyDbHandler(this, null, null, 1);
        Cursor c=dbHandler.getAllCup();
        CupPojo pojo=new CupPojo();
        if(c==null)
            pojo.setCup_count(0);
        else
            pojo.setCup_count(c.getCount());

        cupQty.setText(String.valueOf(pojo.getCup_count()));
        return true;
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
            Intent searchInten=new Intent(IngredientsActivity.this,SearchActivity.class);
            startActivity(searchInten);
//            return true;

        }

        return super.onOptionsItemSelected(item);
    }







}

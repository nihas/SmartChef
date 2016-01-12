package com.nihas.smart.chef.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.CategoryAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.fragments.CategoryFragment;
import com.nihas.smart.chef.fragments.CupFragment;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    static TextView cupQty;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    public static boolean isOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Smart Chef");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        initDrawer();

        getSupportFragmentManager().beginTransaction().add(R.id.container,new CategoryFragment()).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void initDrawer() {
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_drawer,new CupFragment().newInstance(drawerLayout), "CupFragment")
                .commit();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerToggle.syncState();
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerToggle.syncState();

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }else
            super.onBackPressed();

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
//                Intent intent = new Intent(MainActivity.this, CupActivity.class);
//                startActivity(intent);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_drawer,new CupFragment().newInstance(drawerLayout), "CupFragment")
                        .commit();
                if(isOpen) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    isOpen=false;
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                    isOpen=true;
                }
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
            Intent searchInten=new Intent(MainActivity.this,SearchActivity.class);
            startActivity(searchInten);
//            return true;

        }

        return super.onOptionsItemSelected(item);
    }




}

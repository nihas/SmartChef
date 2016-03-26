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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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
import com.nihas.smart.chef.fragments.DrawerFragment;
import com.nihas.smart.chef.fragments.IngredientsFragment;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by snyxius on 10/23/2015.
 */
public class IngredientsActivity extends AppCompatActivity{

//    RecyclerView mRecyclerView;
    ArrayList<IngredientsPojo> listIngredients;
    static ArrayList<AllPojo> listCats;
    IngredientsAdapter ingAdapter;
//    ProgressBar progressBar;
    static TextView cupQty;
    Bundle extras;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
        SmartChefApp.saveToPreferences(getApplicationContext(),"ING_ACT",true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SmartChefApp.saveToPreferences(getApplicationContext(), "ING_ACT", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("SmartChef");
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

        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_drawer,new DrawerFragment().newInstance(drawerLayout), "CupFragment")
                .commit();


        viewPager=(ViewPager)findViewById(R.id.viewpager);
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        extras=getIntent().getExtras();
        viewPager.setCurrentItem(extras.getInt("POSITION"));

//        progressBar=(ProgressBar)findViewById(R.id.pBar);
//        if (SmartChefApp.isNetworkAvailable()) {
//            new getAllCategories().execute();
//        } else {
//            SmartChefApp.showAToast("Network Unavailable");
//        }

//        mRecyclerView=(RecyclerView)findViewById(R.id.rv);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }








    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return IngredientsFragment.newInstance(IngredientsActivity.this,position,listIngredients,listCats.get(position).getId());
        }

        @Override
        public int getCount() {
            return listCats.size();
        }

//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listCats.get(position).getTitle();
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
            Intent searchInten=new Intent(IngredientsActivity.this,SearchActivity.class);
            startActivity(searchInten);
//            return true;

        }


        return super.onOptionsItemSelected(item);
    }



public static void equateCats(ArrayList<AllPojo> listCa){
    listCats=listCa;
}



}

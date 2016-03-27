package com.nihas.smart.chef.activities;

import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;
import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.CategoryAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.fragments.CategoryFragment;
import com.nihas.smart.chef.fragments.DrawerFragment;
import com.nihas.smart.chef.fragments.IngredientsFragment;
import com.nihas.smart.chef.fragments.RecipeFragment;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    static TextView cupQty;
    static Toolbar toolbar;
    DrawerLayout drawerLayout;
    static ActionBarDrawerToggle drawerToggle;
    public static boolean isOpen=false;
    RecyclerView mRecyclerView;
    ArrayList<AllPojo> listCuisines;
    ProgressBar progressBar;
//    private static TabLayout tabLayout;
//    private static ViewPager viewPager;
//    ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Smart Chef");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        initDrawer();
        initialise();

//        getSupportFragmentManager().beginTransaction().add(R.id.container,new CategoryFragment()).commit();










    }


    private void initialise() {
        try {
            progressBar=(ProgressBar)findViewById(R.id.pBar);
//            if(SmartChefApp.readFromPreferences(getApplicationContext(),"CATEGORIES","").equals("")) {
                if (SmartChefApp.isNetworkAvailable()) {
                    new getAllCategories().execute();
                } else {
                    SmartChefApp.showAToast("Internet Unavailable");
                }
//            }else{
//                progressBar.setVisibility(View.GONE);
//                JSONArray jarray=new JSONArray(SmartChefApp.readFromPreferences(getApplicationContext(),"CATEGORIES",""));
//                Log.e("hhghghg",jarray.toString());
//                onDone(jarray);
//            }



//            viewPager = (ViewPager) findViewById(R.id.pager);
//            setupViewPager(viewPager);


            // Calling the RecyclerView
            mRecyclerView = (RecyclerView)findViewById(R.id.rv);
            mRecyclerView.setHasFixedSize(true);

            // The number of Columns
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);



            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                SmartChefApp.showAToast(String.valueOf(listCuisines.get(position).getId()));
                    SmartChefApp.saveToPreferences(getApplicationContext(), "ID", listCuisines.get(position).getId());
                    SmartChefApp.saveToPreferences(getApplicationContext(), "CAT", listCuisines.get(position).getTitle());
//                    getFragmentManager().beginTransaction().replace(R.id.container, new IngredientsFragment()).addToBackStack(null).commit();
                    Intent intent = new Intent(MainActivity.this, IngredientsActivity.class);
                    intent.putExtra("POSITION",position);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
                }
            }));


        }catch (Exception e){

        }

    }

    private void setupViewPager(ViewPager viewPager) {


    }


//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private  List<AllPojo> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return new IngredientsFragment(MainActivity.this,position,listCuisines);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentTitleList.size();
//        }
//
//        public void addFragment( List<AllPojo> mFragmentList) {
////            mFragmentList.add(fragment);
////            AllPojo pojo=new AllPojo(id,title);
//            this.mFragmentTitleList=mFragmentList;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position).getTitle();
//        }
//
//    }

    private class getAllCategories extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonObject = null;
            try {
                return WebRequest.getDataJSONArray(WebServices.getCategories());
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONArray jArray) {
            super.onPostExecute(jArray);
            progressBar.setVisibility(View.GONE);
//            SmartChefApp.saveToPreferences(getApplicationContext(), "CATEGORIES", jArray.toString());
            onDone(jArray);
        }
    }


    private void onDone(JSONArray jArray){
        try {
            if(jArray != null) {
                listCuisines = new ArrayList<>();
//                adapter = new ViewPagerAdapter(getSupportFragmentManager());

                if (jArray.length() > 0) {
                    SmartChefApp.saveToPreferences(getApplicationContext(),"LIST_CUISINES",jArray.toString());
                    for (int i = 0; i < jArray.length(); i++) {
//                            AllPojo cp = new AllPojo();
////                            cp.setName(jArray.getString(i));
                        listCuisines.add(new AllPojo(jArray.getJSONObject(i).getInt(Keys.id),
                                jArray.getJSONObject(i).getString(Keys.name),
                                jArray.getJSONObject(i).getString(Keys.image)));


                    }
//                    IngredientsActivity.equateCats(listCuisines);
//                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//                    adapter.addFragment(listCuisines);
//                    viewPager.setAdapter(adapter);
//                    tabLayout = (TabLayout) findViewById(R.id.tabs);
//                    tabLayout.setupWithViewPager(viewPager);
                } else {
                    SmartChefApp.showAToast("Something Went Wrong.");
                }

//                    final EstablishmentTypeAdapter adapter = new EstablishmentTypeAdapter(getContext(), estTypeListArray);
//                    typeList.setAdapter(adapter);

                CategoryAdapter mAdapter = new CategoryAdapter(getApplicationContext(),listCuisines);
                mRecyclerView.setAdapter(mAdapter);





            }else{
                SmartChefApp.showAToast("Something Went Wrong.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void animate() {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                drawerToggle.onDrawerSlide(drawerLayout, slideOffset);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        anim.start();
    }





    private void initDrawer() {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_drawer,new DrawerFragment().newInstance(drawerLayout), "CupFragment")
                .commit();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
//        animate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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

                Intent intent = new Intent(MainActivity.this, CupActivity.class);
//                startActivity(intent);
                startActivity(intent);
                SmartChefApp.saveToPreferences(getApplicationContext(), "TO_CUP", "main");
                finish();
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
            Intent searchInten=new Intent(MainActivity.this,SearchActivity.class);
            startActivity(searchInten);
//            return true;

        }else if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }


        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }


public static void showSnak(String msg,View v) {
    Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
}


    public static void setTitle(String msg) {
        toolbar.setTitle(msg);
    }




}

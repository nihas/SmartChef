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
//    RecyclerView mRecyclerView;
    ArrayList<AllPojo> listCuisines;
    ProgressBar progressBar;
    private static TabLayout tabLayout;
    private static ViewPager viewPager;


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
            if (SmartChefApp.isNetworkAvailable()) {
                new getAllCategories().execute();
            } else {
                SmartChefApp.showAToast("Internet Unavailable");
            }



            viewPager = (ViewPager) findViewById(R.id.pager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            // Calling the RecyclerView
//            mRecyclerView = (RecyclerView)findViewById(R.id.rv);
//            mRecyclerView.setHasFixedSize(true);

            // The number of Columns
//            GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
//            mRecyclerView.setLayoutManager(mLayoutManager);



//            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
////                SmartChefApp.showAToast(String.valueOf(listCuisines.get(position).getId()));
//                    SmartChefApp.saveToPreferences(getApplicationContext(), "ID", listCuisines.get(position).getId());
//                    SmartChefApp.saveToPreferences(getApplicationContext(), "CAT", listCuisines.get(position).getTitle());
////                    getFragmentManager().beginTransaction().replace(R.id.container, new IngredientsFragment()).addToBackStack(null).commit();
//                    Intent intent = new Intent(MainActivity.this, IngredientsActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
//                }
//            }));


        }catch (Exception e){

        }

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new MyRewardFragment(), "MY REWARDS");
//        adapter.addFragment(new WalletFragment(), "WALLET");
//        adapter.addFragment(new OffersFragment(), "OFFERS");
//        adapter.addFragment(new SwapFragments(), "SWAP");

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

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
            onDone(jArray);
        }
    }


    private void onDone(JSONArray jArray){
        try {
            if(jArray != null) {
                listCuisines = new ArrayList<>();
                if (jArray.length() > 0) {
                    for (int i = 0; i < jArray.length(); i++) {
//                            AllPojo cp = new AllPojo();
////                            cp.setName(jArray.getString(i));
//                        listCuisines.add(new AllPojo(jArray.getJSONObject(i).getInt(Keys.id),
//                                jArray.getJSONObject(i).getString(Keys.name),
//                                jArray.getJSONObject(i).getString(Keys.image)));

                    }
                } else {
                    SmartChefApp.showAToast("Something Went Wrong.");
                }

//                    final EstablishmentTypeAdapter adapter = new EstablishmentTypeAdapter(getContext(), estTypeListArray);
//                    typeList.setAdapter(adapter);

//                CategoryAdapter mAdapter = new CategoryAdapter(getApplicationContext(),listCuisines);
//                mRecyclerView.setAdapter(mAdapter);





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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        // check if the request code is same as what is passed  here it is 2
//        if(resultCode==2)
//        {
////            getSupportFragmentManager().beginTransaction().replace(R.id.container,new RecipeFragment()).addToBackStack(null).commit();
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            Fragment fragment = RecipeFragment.newInstance(data.getExtras().getString("ING"));
//            transaction.replace(R.id.container, fragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }else{
////            getSupportFragmentManager().beginTransaction().replace(R.id.container,new CategoryFragment()).addToBackStack(null).commit();
//        }
//    }


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




    private class SearchRecipe extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {

                return WebRequest.getData(WebServices.searchIngRecipe(params[0]));
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jobj) {
            super.onPostExecute(jobj);
            try {
//                JSONObject jobj2=jobj.getJSONObject("ingredients");
                JSONArray jarray=jobj.getJSONArray("ingredients");
                JSONArray jarray2=jobj.getJSONArray("recipes");
//                mSuggestionsList.clear();
//                mSuggestionsRecipe.clear();
                for(int i=0;i<jarray.length();i++){
//                    mSuggestionsList.add(new SearchItem(jarray.getString(i)));
                }
                for (int j=0;j<jarray2.length();j++){
                    JSONObject job=jarray2.getJSONObject(j);
                    RecipesPojo pojo=new RecipesPojo();
                    pojo.setId(job.getString("id"));
                    pojo.setName(job.getString("name"));
                    pojo.setMedia_type("media_type");
                    if(pojo.getMedia_type().equals("image"))
                        pojo.setMedia_url("media_url");
                    else
                        pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
//                    mSuggestionsRecipe.add(pojo);
                }
                List<SearchItem> mResultsList = new ArrayList<>();
//                mSearchAdapter = new SearchAdapter(MainActivity.this, mResultsList, mSuggestionsList, SearchCodes.THEME_LIGHT);
//                mSearchView.setAdapter(mSearchAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
//
//            progressBar.setVisibility(View.GONE);
//            onDone(jArray);
        }
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

//    public static void replaceFrag(){
//        getSupportFragmentManager().beginTransaction().replace(R.id.container,new CategoryFragment()).commit();
//    }

public static void showSnak(String msg,View v) {
    Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
}


    public static void setTitle(String msg) {
        toolbar.setTitle(msg);
    }




}

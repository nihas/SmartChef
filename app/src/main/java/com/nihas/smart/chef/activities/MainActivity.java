package com.nihas.smart.chef.activities;

import android.animation.ValueAnimator;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
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
import com.nihas.smart.chef.fragments.CupFragment;
import com.nihas.smart.chef.fragments.RecipeFragment;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;


public class MainActivity extends AppCompatActivity {


    static TextView cupQty;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    public static boolean isOpen=false;
    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private SearchView mSearchView;
    private int mVersion = SearchCodes.VERSION_MENU_ITEM;
    private int mStyle = SearchCodes.STYLE_MENU_ITEM_CLASSIC;
    private int mTheme = SearchCodes.THEME_LIGHT;
    SearchAdapter mSearchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Smart Chef");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        initDrawer();

        getSupportFragmentManager().beginTransaction().add(R.id.container,new CategoryFragment()).commit();



        mHistoryDatabase = new SearchHistoryTable(this);
        mSuggestionsList = new ArrayList<>();

        mSearchView = (SearchView) findViewById(R.id.searchView);
        // important -------------------------------------------------------------------------------
        mSearchView.setVersion(mVersion);
        mSearchView.setStyle(mStyle);
        mSearchView.setTheme(mTheme);
        // -----------------------------------------------------------------------------------------
        mSearchView.setDivider(false);
        mSearchView.setHint("Search");
        mSearchView.setHint("Search");
        mSearchView.setHintSize(getResources().getDimension(R.dimen.search_text_medium));
        mSearchView.setVoice(false);
        mSearchView.setVoiceText("Voice");
        mSearchView.setAnimationDuration(360);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.hide(false);
                mHistoryDatabase.addItem(new SearchItem(query));
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0)
                    new SearchRecipe().execute(newText);
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                // mFab.hide();
            }

            @Override
            public void onSearchViewClosed() {
                // mFab.show();
            }
        });






    }

//        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed);
//        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
//            @Override
//            public boolean onMenuItemSelected(MenuItem menuItem) {
//                //TODO: Start some activity
//                int id = menuItem.getItemId();
//
//                //noinspection SimplifiableIfStatement
//                if (id == R.id.action_logout) {
//                    LoginManager.getInstance().logOut();
//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
//                    SmartChefApp.clearSharedPrefData(getApplicationContext());
//                    finish();
//                }
//                return false;
//            }
//        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
                .add(R.id.container_drawer,new CupFragment().newInstance(drawerLayout), "CupFragment")
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
                startActivityForResult(intent,2);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(resultCode==2)
        {
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new RecipeFragment()).addToBackStack(null).commit();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment = RecipeFragment.newInstance(data.getExtras().getString("ING"));
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new CategoryFragment()).addToBackStack(null).commit();
        }
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
            showSearchView();
//            Intent searchInten=new Intent(MainActivity.this,SearchActivity.class);
//            startActivity(searchInten);
//            return true;

        }else if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void showSearchView() {
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());

        mSearchView.show(true);
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
                mSuggestionsList.clear();
                for(int i=0;i<jarray.length();i++){
                    mSuggestionsList.add(new SearchItem(jarray.getString(i)));
                }
                List<SearchItem> mResultsList = new ArrayList<>();
                mSearchAdapter = new SearchAdapter(MainActivity.this, mResultsList, mSuggestionsList, SearchCodes.THEME_LIGHT);
                mSearchView.setAdapter(mSearchAdapter);

                mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mSearchView.hide(false);
                        TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                        CharSequence text = textView.getText();
                        mHistoryDatabase.addItem(new SearchItem(text));
//                        Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();

                        MyDbHandler dbHandler = new MyDbHandler(MainActivity.this, null, null, 1);

                        CupPojo product =
                                new CupPojo(text.toString(), "http://collegemix.ca/img/placeholder.png");
                        if (!dbHandler.isIngredients(text.toString())) {
                            if (dbHandler.addProduct(product)) {
                                Toast.makeText(MainActivity.this, "Added" + position, Toast.LENGTH_SHORT).show();
//                           innerHolder3.quantity_text.setText(ingQty.getText().toString().trim());
//                                       innerHolder3.plusMinusLayout.setVisibility(View.VISIBLE);
//                                       innerHolder3.addLayout.setVisibility(View.GONE);
//                           innerHolder3.ingMeasure.setVisibility(View.VISIBLE);
                                Cursor c=dbHandler.getAllCup();
                                CupPojo pojo=new CupPojo();
                                if(c==null)
                                    pojo.setCup_count(0);
                                else
                                    pojo.setCup_count(c.getCount());
                                MainActivity.updateCupValue(pojo.getCup_count());
                            }
                            else
                                Toast.makeText(MainActivity.this, "FAILED", Toast.LENGTH_SHORT).show();

//                       dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
//
                        }
                    }
                });
//                SmartChefApp.showAToast(""+jobj2);
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


}

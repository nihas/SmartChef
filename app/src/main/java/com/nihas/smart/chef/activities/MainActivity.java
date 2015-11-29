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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.CategoryAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    static TextView cupQty;
    RecyclerView mRecyclerView;
    ArrayList<AllPojo> listCuisines;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar=(ProgressBar)findViewById(R.id.pBar);
        if (SmartChefApp.isNetworkAvailable()) {
            new getAllCategories().execute();
        } else {

        }
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
                SmartChefApp.saveToPreferences(getApplicationContext(),"ID",listCuisines.get(position).getId());
                Intent intent = new Intent(MainActivity.this, IngredientsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
                Intent intent = new Intent(MainActivity.this, CupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        });
        cupQty=(TextView)view.findViewById(R.id.cup_qty);
        MyDbHandler dbHandler = new MyDbHandler(this, null, null, 1);
        Cursor c=dbHandler.getAllCup();
        CupPojo pojo=new CupPojo();
        if(c==null) {
//            pojo.setCup_count(0);
            doButtonOneClickActions(0);
        }
        else {
//            pojo.setCup_count(c.getCount());
            doButtonOneClickActions(c.getCount());
        }


        return true;
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
                            listCuisines.add(new AllPojo(jArray.getJSONObject(i).getInt(Keys.id),
                                    jArray.getJSONObject(i).getString(Keys.name),
                                        jArray.getJSONObject(i).getString(Keys.image)));
                        }
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



    public void UpdateQty(){
        CupPojo pojo=new CupPojo();
        cupQty.setText(pojo.getCup_count());
    }

    public interface OnDataChangeListener{
        public void onDataChanged(int size);
    }

 static OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public static void doButtonOneClickActions(int size) {

        if(mOnDataChangeListener != null){
            mOnDataChangeListener.onDataChanged(size);
            cupQty.setText(String.valueOf(size));
        }
    }


}

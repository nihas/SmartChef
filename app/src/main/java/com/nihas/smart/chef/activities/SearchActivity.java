package com.nihas.smart.chef.activities;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.CupAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snyxius on 27/11/15.
 */
public class SearchActivity extends AppCompatActivity {
//FloatingSearchView mSearchView;

    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText searchQuery;
//    private SearchHistoryTable mHistoryDatabase;
    private List<String> mIngList;
    private List<RecipesPojo> mRecipeList;
//    private SearchView mSearchView;
//    private int mVersion = SearchCodes.VERSION_MENU_ITEM;
//    private int mStyle = SearchCodes.STYLE_MENU_ITEM_CLASSIC;
//    private int mTheme = SearchCodes.THEME_LIGHT;
//    SearchAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("CUP");
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

        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        searchQuery=(EditText)findViewById(R.id.search_query);
        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(SmartChefApp.isNetworkAvailable()){
                    if(charSequence.length()>0)
                    new SearchRecipe().execute(charSequence.toString());
                }else{
                    SmartChefApp.showAToast("Network Unavailable");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    /*    mHistoryDatabase = new SearchHistoryTable(this);
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
        mSearchView.setAnimationDuration(200);
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

        showSearchView();*/

    }


//    private void showSearchView() {
//        mSuggestionsList.clear();
//        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
//
//        mSearchView.show(true);
//    }


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
                mIngList=new ArrayList<>();
                for(int i=0;i<jarray.length();i++){
                    mIngList.add(jarray.getString(i));
                }
                JSONArray jarrayrec=jobj.getJSONArray("recipes");
                mRecipeList = new ArrayList<>();
                for(int j=0;j<jarrayrec.length();j++){
                    RecipesPojo pojo=new RecipesPojo();
                    pojo.setId(jarrayrec.getJSONObject(j).getString("id"));
                    pojo.setName(jarrayrec.getJSONObject(j).getString("name"));
                    pojo.setMedia_type(jarrayrec.getJSONObject(j).getString("media_type"));
                    pojo.setMedia_url(jarrayrec.getJSONObject(j).getString("media_url"));
                    mRecipeList.add(pojo);
                }
                com.nihas.smart.chef.adapters.SearchAdapter searchAdapter=new com.nihas.smart.chef.adapters.SearchAdapter(getApplicationContext(),mIngList,mRecipeList);
                recyclerView.setAdapter(searchAdapter);
//                mSearchAdapter = new SearchAdapter(SearchActivity.this, mResultsList, mSuggestionsList, mSuggestionsRecipe, SearchCodes.THEME_LIGHT);
//                mSearchView.setAdapter(mSearchAdapter);

              /*  mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        mSearchView.hide(false);
                        TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                        CharSequence text = textView.getText();
//                        mHistoryDatabase.addItem(new SearchItem(text));
//                        Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();

                        MyDbHandler dbHandler = new MyDbHandler(SearchActivity.this, null, null, 1);

                        CupPojo product =
                                new CupPojo(text.toString(), "http://collegemix.ca/img/placeholder.png");
                        if (!dbHandler.isIngredients(text.toString())) {
                            if (dbHandler.addProduct(product)) {
                                Toast.makeText(SearchActivity.this, "Added" + position, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(SearchActivity.this, "FAILED", Toast.LENGTH_SHORT).show();

//                       dialog.dismiss();
                        } else {
                            Toast.makeText(SearchActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
//
                        }
                    }
                });*/
//                SmartChefApp.showAToast(""+jobj2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//
//            progressBar.setVisibility(View.GONE);
//            onDone(jArray);
        }
    }



}

package com.nihas.smart.chef.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;

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
        setContentView(R.layout.search_layout);

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

        showSearchView();

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
//                mSearchAdapter = new SearchAdapter(SearchActivity.this, mResultsList, mSuggestionsList, mSuggestionsRecipe, SearchCodes.THEME_LIGHT);
                mSearchView.setAdapter(mSearchAdapter);

                mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mSearchView.hide(false);
                        TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                        CharSequence text = textView.getText();
                        mHistoryDatabase.addItem(new SearchItem(text));
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
        mSearchView.hide(false);
        finish();
    }
}

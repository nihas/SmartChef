package com.nihas.smart.chef.utils;


import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Filter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataHelper {

        private static final String COLORS_FILE_NAME = "colors.json";

        private static List<ColorWrapper> sColorWrappers = new ArrayList<>();

        public interface OnFindResultsListener{

                void onResults(List<ColorSuggestion> results);
        }

        public static List<ColorSuggestion> getHistory(Context context, int count){

                initColorWrapperList(context);

                List<ColorSuggestion> suggestionList = new ArrayList<>();

                ColorSuggestion colorSuggestion;
                for(int i=0; i<count; i++){
                        colorSuggestion = new ColorSuggestion(sColorWrappers.get(i));
                        colorSuggestion.setIsHistory(true);
                        suggestionList.add(colorSuggestion);
                }

                return suggestionList;
        }

        public static void find(Context context, String query, final OnFindResultsListener listener){

//                initColorWrapperList(context);


                new getSearchQuery().execute(query);


               /* new Filter(){

                        @Override
                        protected FilterResults performFiltering(CharSequence constraint) {


                                List<ColorSuggestion> suggestionList = new ArrayList<>();

                                if (!(constraint == null || constraint.length() == 0)) {

                                        for(ColorWrapper color: sColorWrappers){

                                                if(color.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                                                        suggestionList.add(new ColorSuggestion(color));
                                        }

                                }

                                FilterResults results = new FilterResults();
                                results.values = suggestionList;
                                results.count = suggestionList.size();

                                return results;
                        }

                        @Override
                        protected void publishResults(CharSequence constraint, FilterResults results) {

                                if(listener!=null)
                                        listener.onResults((List<ColorSuggestion>)results.values);
                        }
                }.filter(query);*/

        }

        private static void initColorWrapperList(Context context){

                if(sColorWrappers.isEmpty()) {

                        String jsonString = loadJson(context);
                        sColorWrappers = deserializeColors(jsonString);
                }
        }

        private static class getSearchQuery extends AsyncTask<String, Void, JSONObject> {

                @Override
                protected JSONObject doInBackground(String... params) {
                        JSONObject jsonObject = null;
                        try {
                                return WebRequest.getData(WebServices.searchRecipeQuick(params[0]));
                        } catch (Exception e) {

                                e.printStackTrace();
                        }
                        return jsonObject;
                }

                @Override
                protected void onPostExecute(JSONObject jobj) {
                        super.onPostExecute(jobj);
                        SmartChefApp.showAToast(jobj + "");
                        List<String> suggestionList = new ArrayList<>();
                        try {
                                JSONArray jarray=jobj.getJSONArray("products");
                                for(int i=0;i<jarray.length();i++){
                                        JSONObject jobj2=jarray.getJSONObject(i);
                                        suggestionList.add(jobj2.getString("product_name"));
                                }


                        } catch (JSONException e) {
                                e.printStackTrace();
                        }

//                        progressBar.setVisibility(View.GONE);
//                        onDone(jArray);
                }
        }

        private static String loadJson(Context context) {

                String jsonString;

                try {
                        InputStream is = context.getAssets().open(COLORS_FILE_NAME);
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        jsonString = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                        ex.printStackTrace();
                        return null;
                }

                return jsonString;
        }

        private static List<ColorWrapper> deserializeColors(String jsonString){

                Gson gson = new Gson();

                Type collectionType = new TypeToken<List<ColorWrapper>>() {}.getType();
                return gson.fromJson(jsonString, collectionType);
        }

}
package com.nihas.smart.chef.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.IngredientsActivity;
import com.nihas.smart.chef.adapters.CategoryAdapter;
import com.nihas.smart.chef.adapters.IngredientsAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Nihas on 29-11-2015.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {


    RecyclerView mRecyclerView;
    ArrayList<AllPojo> listCuisines;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialise(view);
    }
    private void initialise(View rootView) {
        try {
            progressBar=(ProgressBar)rootView.findViewById(R.id.pBar);
            if (SmartChefApp.isNetworkAvailable()) {
                new getAllCategories().execute();
            } else {

            }
            // Calling the RecyclerView
            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.rv);
            mRecyclerView.setHasFixedSize(true);

            // The number of Columns
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(mLayoutManager);



            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                SmartChefApp.showAToast(String.valueOf(listCuisines.get(position).getId()));
                    SmartChefApp.saveToPreferences(getActivity().getApplicationContext(), "ID", listCuisines.get(position).getId());
                    getFragmentManager().beginTransaction().replace(R.id.container,new IngredientsFragment()).addToBackStack(null).commit();
//                    Intent intent = new Intent(MainActivity.this, IngredientsActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
                }
            }));


        }catch (Exception e){

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

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
                        listCuisines.add(new AllPojo(jArray.getJSONObject(i).getInt(Keys.id),
                                jArray.getJSONObject(i).getString(Keys.name),
                                jArray.getJSONObject(i).getString(Keys.image)));
                    }
                } else {
                    SmartChefApp.showAToast("Something Went Wrong.");
                }

//                    final EstablishmentTypeAdapter adapter = new EstablishmentTypeAdapter(getContext(), estTypeListArray);
//                    typeList.setAdapter(adapter);

                CategoryAdapter mAdapter = new CategoryAdapter(getActivity().getApplicationContext(),listCuisines);
                mRecyclerView.setAdapter(mAdapter);





            }else{
                SmartChefApp.showAToast("Something Went Wrong.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
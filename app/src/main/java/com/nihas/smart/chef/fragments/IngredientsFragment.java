package com.nihas.smart.chef.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.nihas.smart.chef.activities.MainActivity;
import com.nihas.smart.chef.adapters.IngredientsAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;
import com.nihas.smart.chef.utils.GridSpacingItemDecoration;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Nihas on 29-11-2015.
 */
public class IngredientsFragment extends Fragment implements View.OnClickListener {

    RecyclerView mRecyclerView;
    ArrayList<IngredientsPojo> mlistIngredients;
    IngredientsAdapter ingAdapter;
    ProgressBar progressBar;
    Context mcontext;
    int position;
    int ID;



    public IngredientsFragment(Context context, int position, ArrayList<IngredientsPojo> listIng, int id) {
        super();
        this.mcontext = context;
        this.position = position;
        this.mlistIngredients = listIng;
        this.ID=id;
    }


    public static IngredientsFragment newInstance(Context context, int position, ArrayList<IngredientsPojo> listIng, int id) {
        IngredientsFragment f = new IngredientsFragment(context,position,listIng,id);
        Bundle args = new Bundle();
//        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }



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
        MainActivity.setTitle("SmartChef");
        if (SmartChefApp.isNetworkAvailable()) {
            new getAllCategories().execute(String.valueOf(ID));
        } else {
            SmartChefApp.showAToast("Network Unavailable");
        }


    }



    private void initialise(View rootView) {
        try {
            progressBar=(ProgressBar)rootView.findViewById(R.id.pBar);
            progressBar.setVisibility(View.VISIBLE);
            if (SmartChefApp.isNetworkAvailable()) {
                new getAllCategories().execute();
            } else {
                SmartChefApp.showAToast("Network Unavailable");
            }

            mRecyclerView=(RecyclerView)rootView.findViewById(R.id.rv);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            int spanCount = 3;
            int spacing = 5;
            boolean includeEdge = true;
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


        }catch (Exception e){

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }


    private class getAllCategories extends AsyncTask<String, Void, JSONArray> {
ProgressDialog dial=new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dial.setMessage("Loading..");
            dial.setCancelable(false);
            dial.show();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonObject = null;
            try {

                return WebRequest.getDataJSONArray(WebServices.getIngredients(Integer.parseInt(params[0])));
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONArray jArray) {
            super.onPostExecute(jArray);
            progressBar.setVisibility(View.GONE);
            dial.dismiss();
            onDone(jArray);
        }
    }


    private void onDone(JSONArray jArray){
        try {
            if(jArray != null) {
                mlistIngredients = new ArrayList<>();
                if (jArray.length() > 0) {
                    for (int i = 0; i < jArray.length(); i++) {
//                            AllPojo cp = new AllPojo();
////                            cp.setName(jArray.getString(i));
                        mlistIngredients.add(new IngredientsPojo(jArray.getJSONObject(i).getString(Keys.name),
                                jArray.getJSONObject(i).getString(Keys.image)));
                    }
                } else {
                    SmartChefApp.showAToast("Something Went Wrong.");
                }

//                    final EstablishmentTypeAdapter adapter = new EstablishmentTypeAdapter(getContext(), estTypeListArray);
//                    typeList.setAdapter(adapter);
                ingAdapter=new IngredientsAdapter(getContext(),mlistIngredients);
                mRecyclerView.setAdapter(ingAdapter);





            }else{
//                SmartChefApp.showAToast("Something Went Wrong.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
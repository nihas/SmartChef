package com.nihas.smart.chef.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.RecipeActivity;
import com.nihas.smart.chef.adapters.CategoryAdapter;
import com.nihas.smart.chef.adapters.CupAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Nihas on 29-11-2015.
 */
public class CupFragment extends Fragment implements View.OnClickListener {


    RecyclerView mRecyclerView;
    ArrayList<CupPojo> listIngredients;
    CupAdapter cupAdapter;
    static TextView emptyView;
    Button cookButton;
    ProgressBar progressBar;
    static DrawerLayout drawerLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_cup, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialise(view);
    }


    public static  CupFragment newInstance(DrawerLayout drawer) {
        drawerLayout = drawer;
        CupFragment f = new CupFragment();
        return f;
    }

    private void initialise(View rootView) {
        try {
//            progressBar=(ProgressBar)rootView.findViewById(R.id.pBar);
//            progressBar.setVisibility(View.GONE);

            cookButton=(Button)rootView.findViewById(R.id.cook_button);
            emptyView=(TextView)rootView.findViewById(R.id.empty_view);
            mRecyclerView=(RecyclerView)rootView.findViewById(R.id.rv);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            cupAdapter=new CupAdapter(getActivity(),getIngredients());
            mRecyclerView.setAdapter(cupAdapter);

            if (getIngredients().isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }

            cookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cook_ing="";
                    MyDbHandler dbHandler = new MyDbHandler(getActivity(), null, null, 1);
                    Cursor c=dbHandler.getAllCup();
//        Toast.makeText(this, String.valueOf(c.getCount()), Toast.LENGTH_LONG).show();
                    CupPojo pojo= new CupPojo();
                    pojo.setCup_count(c.getCount());
                    if (c.moveToFirst()) {
                        do {
//                DisplayContact(c);
                          cook_ing=cook_ing+c.getString(1)+",";
                        } while (c.moveToNext());
                    }
                    SmartChefApp.showAToast(cook_ing);


                    getFragmentManager().beginTransaction().replace(R.id.container,new RecipeFragment()).addToBackStack(null).commit();

//                    Intent newInten = new Intent(getActivity(), RecipeActivity.class);
//                    startActivity(newInten);
//                    getActivity().finish();
                }
            });


        }catch (Exception e){

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }


    public ArrayList<CupPojo> getIngredients(){
        listIngredients=new ArrayList<>();
        MyDbHandler dbHandler = new MyDbHandler(getActivity(), null, null, 1);
        Cursor c=dbHandler.getAllCup();
//        Toast.makeText(this, String.valueOf(c.getCount()), Toast.LENGTH_LONG).show();
        CupPojo pojo= new CupPojo();
        pojo.setCup_count(c.getCount());
        if (c.moveToFirst()) {
            do {
//                DisplayContact(c);
                listIngredients.add(new CupPojo(Integer.parseInt(c.getString(0)),c.getString(1),c.getString(3)));
            } while (c.moveToNext());
        }
//        String[] array=getResources().getStringArray(R.array.fruits);
//        listIngredients=new ArrayList<>(Arrays.asList(array));
//        listIngredients.add("Banana");
//        listIngredients.add("Apple");
//        listIngredients.add("Onion");
//        listIngredients.add("Ginger");
//        listIngredients.add("Garlic");
//        listIngredients.add("Tamarind");
//        listIngredients.add("Cucumber");
//        listIngredients.add("Tomato");
//        listIngredients.add("Chilly");
//        listIngredients.add("Chicken");
//        listIngredients.add("Brinjal");
//        listIngredients.add("Elephant Yam");
        return listIngredients;


    }


    public static void updateView(){
        emptyView.setVisibility(View.VISIBLE);
    }


}
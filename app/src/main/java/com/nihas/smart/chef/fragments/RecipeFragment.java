package com.nihas.smart.chef.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.RecipeActivity;
import com.nihas.smart.chef.activities.RecipeDetailsActivity;
import com.nihas.smart.chef.adapters.CupAdapter;
import com.nihas.smart.chef.adapters.RecipesAdapter;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by Nihas on 29-11-2015.
 */
public class RecipeFragment extends Fragment implements View.OnClickListener {

    ArrayList<RecipesPojo> listRecipes;
    RecipesAdapter recipAdapter;
    RecyclerView mRecyclerView;
    TextView cupQty;


    public static RecipeFragment newInstance(String ings) {
        RecipeFragment f = new RecipeFragment();
        Bundle args = new Bundle();
        args.putString("ingredients", ings);
        f.setArguments(args);
        return f;
    }

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

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        }));
    }




    private void initialise(View rootView) {
        try {
//            progressBar=(ProgressBar)rootView.findViewById(R.id.pBar);
//            progressBar.setVisibility(View.GONE);

            mRecyclerView=(RecyclerView)rootView.findViewById(R.id.rv);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//            recipAdapter=new RecipesAdapter(this,getIngredients());
            mRecyclerView.setAdapter(recipAdapter);



        }catch (Exception e){

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }




}
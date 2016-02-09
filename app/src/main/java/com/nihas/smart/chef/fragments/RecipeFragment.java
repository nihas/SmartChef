package com.nihas.smart.chef.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nihas on 29-11-2015.
 */
public class RecipeFragment extends Fragment implements View.OnClickListener {

    ArrayList<RecipesPojo> listRecipes;
    RecipesAdapter recipAdapter;
    RecyclerView mRecyclerView;
    TextView cupQty;
    ProgressBar progressBar;


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
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialise(view);

        progressBar=(ProgressBar)view.findViewById(R.id.pBar);
        if (SmartChefApp.isNetworkAvailable()) {
            new getRecipe().execute();
        } else {

        }


        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recipAdapter=new RecipesAdapter(getActivity(),getIngredients());
        mRecyclerView.setAdapter(recipAdapter);

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

    private class getRecipe extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {

                return WebRequest.getData(WebServices.searchRecipe(getArguments().getString("ingredients"), 1));
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jobj) {
            super.onPostExecute(jobj);
            progressBar.setVisibility(View.GONE);
//            onDone(jArray);
            SmartChefApp.showAToast(jobj+"");
        }
    }


    public ArrayList<RecipesPojo> getIngredients(){
        listRecipes=new ArrayList<>();
//        String[] array=getResources().getStringArray(R.array.fruits);
//        listIngredients=new ArrayList<>(Arrays.asList(array));
        listRecipes.add(new RecipesPojo("Ramen Noodles","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/8505482256_18e865c985_z.jpg","2-3","4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Vegetable","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/Ramen-Vegetable-Beef-Skillet-Recipe-Taste-and-Tell.jpg","1-5","4 Minutes"));
        listRecipes.add(new RecipesPojo("Pasta Brocali","http://www.dishbase.com/recipe_images/large/pasta-with-broccoli-and-italian-sausage-12512110711.jpg","8-10","4 Minutes"));
        listRecipes.add(new RecipesPojo("Shirley Leyung","http://www.restaurantmagazine.com/wp-content/uploads/2012/10/Popeyes-4th-Annual-Crawfish-Festival-is-the-Perfect-Recipe-for-Rich-Louisiana-Flavor.jpg","2-6","4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Noodles","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/8505482256_18e865c985_z.jpg","2-6","4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Vegetable","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/Ramen-Vegetable-Beef-Skillet-Recipe-Taste-and-Tell.jpg","1-9","4 Minutes"));
        listRecipes.add(new RecipesPojo("Pasta Brocali","http://www.dishbase.com/recipe_images/large/pasta-with-broccoli-and-italian-sausage-12512110711.jpg","8-10","4 Minutes"));
        listRecipes.add(new RecipesPojo("Shirley Leyung","http://www.restaurantmagazine.com/wp-content/uploads/2012/10/Popeyes-4th-Annual-Crawfish-Festival-is-the-Perfect-Recipe-for-Rich-Louisiana-Flavor.jpg","2-9","4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Noodles","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/8505482256_18e865c985_z.jpg","2-3","4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Vegetable","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/Ramen-Vegetable-Beef-Skillet-Recipe-Taste-and-Tell.jpg","1-5","4 Minutes"));
        listRecipes.add(new RecipesPojo("Pasta Brocali","http://www.dishbase.com/recipe_images/large/pasta-with-broccoli-and-italian-sausage-12512110711.jpg","8-11","4 Minutes"));
        listRecipes.add(new RecipesPojo("Shirley Leyung","http://www.restaurantmagazine.com/wp-content/uploads/2012/10/Popeyes-4th-Annual-Crawfish-Festival-is-the-Perfect-Recipe-for-Rich-Louisiana-Flavor.jpg","2-5","4 Minutes"));

        return listRecipes;


    }

}
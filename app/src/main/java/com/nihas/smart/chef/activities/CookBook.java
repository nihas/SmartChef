package com.nihas.smart.chef.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.CupAdapter;
import com.nihas.smart.chef.adapters.RecipesAdapter;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;

import java.util.ArrayList;

/**
 * Created by snyxius on 10/23/2015.
 */
public class CookBook extends AppCompatActivity {
    RecyclerView mRecyclerView;
    ArrayList<RecipesPojo> listIngredients;
    RecipesAdapter recipeAdapter;
    static LinearLayout emptyView;
//    static Button cookButton,addIng;
    ProgressBar progressBar;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_book);
initialise();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("COOK BOOK");
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



    }

    public ArrayList<RecipesPojo> getIngredients(){
        listIngredients=new ArrayList<>();
        MyDbHandler dbHandler = new MyDbHandler(CookBook.this, null, null, 1);
        Cursor c=dbHandler.getAllFav();
//        Toast.makeText(this, String.valueOf(c.getCount()), Toast.LENGTH_LONG).show();

        if (c.moveToFirst()) {
            do {
//                DisplayContact(c);
                RecipesPojo pojo= new RecipesPojo();
                pojo.setId(c.getString(0));
                pojo.setName(c.getString(1));
                pojo.setVeg(c.getString(2));
                pojo.setServes(c.getString(3));
                pojo.setFood_kind(c.getString(4));
                pojo.setCuisine(c.getString(5));
                pojo.setPreparation_time(c.getString(6));
                pojo.setMedia_url(c.getString(7));
                listIngredients.add(pojo);
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




    private void initialise() {
        try {
//            progressBar=(ProgressBar)rootView.findViewById(R.id.pBar);
//            progressBar.setVisibility(View.GONE);

//            cookButton=(Button)findViewById(R.id.cook_button);
//            addIng=(Button)findViewById(R.id.add_ing);
//            addIng.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
            emptyView=(LinearLayout)findViewById(R.id.empty_view);
            mRecyclerView=(RecyclerView)findViewById(R.id.rv);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            recipeAdapter=new RecipesAdapter(CookBook.this,getIngredients());
            mRecyclerView.setAdapter(recipeAdapter);

            if (getIngredients().isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
//                cookButton.setVisibility(View.GONE);
            }
            else {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
//                cookButton.setVisibility(View.VISIBLE);
            }




        }catch (Exception e){

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("MESSAGE","msg");
        setResult(1, intent);
        finish();
    }

    public static void updateView(){
        emptyView.setVisibility(View.VISIBLE);
//        cookButton.setVisibility(View.GONE);
    }
}



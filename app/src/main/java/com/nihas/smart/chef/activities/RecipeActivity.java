package com.nihas.smart.chef.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.IngredientsAdapter;
import com.nihas.smart.chef.adapters.RecipesAdapter;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by Nihas on 08-11-2015.
 */
public class RecipeActivity extends AppCompatActivity {

    ArrayList<RecipesPojo> listRecipes;
    RecipesAdapter recipAdapter;
    RecyclerView mRecyclerView;
    TextView cupQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView=(RecyclerView)findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipAdapter=new RecipesAdapter(this,getIngredients());
        mRecyclerView.setAdapter(recipAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        recipAdapter.setOnDataChangeListener(new RecipesAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(int size) {
                CupPojo pojo = new CupPojo();
                cupQty.setText(String.valueOf(pojo.getCup_count()));
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(RecipeActivity.this, RecipeDetailsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        }));
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
                Intent intent = new Intent(RecipeActivity.this, CupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        });

        cupQty=(TextView)view.findViewById(R.id.cup_qty);
        MyDbHandler dbHandler = new MyDbHandler(this, null, null, 1);
        Cursor c=dbHandler.getAllCup();
        CupPojo pojo=new CupPojo();
        if(c==null)
            pojo.setCup_count(0);
        else
            pojo.setCup_count(c.getCount());
        cupQty.setText(String.valueOf(pojo.getCup_count()));
        return true;
    }


    public ArrayList<RecipesPojo> getIngredients(){
        listRecipes=new ArrayList<>();
//        String[] array=getResources().getStringArray(R.array.fruits);
//        listIngredients=new ArrayList<>(Arrays.asList(array));
        listRecipes.add(new RecipesPojo("Ramen Noodles","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/8505482256_18e865c985_z.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Vegetable","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/Ramen-Vegetable-Beef-Skillet-Recipe-Taste-and-Tell.jpg",1,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Pasta Brocali","http://www.dishbase.com/recipe_images/large/pasta-with-broccoli-and-italian-sausage-12512110711.jpg",8,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Shirley Leyung","http://www.restaurantmagazine.com/wp-content/uploads/2012/10/Popeyes-4th-Annual-Crawfish-Festival-is-the-Perfect-Recipe-for-Rich-Louisiana-Flavor.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Noodles","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/8505482256_18e865c985_z.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Vegetable","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/Ramen-Vegetable-Beef-Skillet-Recipe-Taste-and-Tell.jpg",1,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Pasta Brocali","http://www.dishbase.com/recipe_images/large/pasta-with-broccoli-and-italian-sausage-12512110711.jpg",8,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Shirley Leyung","http://www.restaurantmagazine.com/wp-content/uploads/2012/10/Popeyes-4th-Annual-Crawfish-Festival-is-the-Perfect-Recipe-for-Rich-Louisiana-Flavor.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Noodles","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/8505482256_18e865c985_z.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Ramen Vegetable","https://dz2k5jx87b7zc.cloudfront.net/wp-content/uploads/2013/03/Ramen-Vegetable-Beef-Skillet-Recipe-Taste-and-Tell.jpg",1,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Pasta Brocali","http://www.dishbase.com/recipe_images/large/pasta-with-broccoli-and-italian-sausage-12512110711.jpg",8,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Shirley Leyung","http://www.restaurantmagazine.com/wp-content/uploads/2012/10/Popeyes-4th-Annual-Crawfish-Festival-is-the-Perfect-Recipe-for-Rich-Louisiana-Flavor.jpg",2,"4 Minutes"));

        return listRecipes;


    }
}

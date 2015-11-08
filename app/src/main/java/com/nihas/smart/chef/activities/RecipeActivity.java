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
        listRecipes.add(new RecipesPojo("Apple","http://img1.exportersindia.com/product_images/bc-small/dir_100/2970062/fresh-apple-1062283.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Apricot","http://www.foodallergens.info/foodimages/apricot.jpg",1,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Avocado","http://content.everydayhealth.com/sbd2/cms/how-to-enjoy-avocado-200x200.jpg",8,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Banana - ripe","https://southernmarylandvapes.com/wp-content/uploads/2013/10/Ripe-Banana.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Banana - raw","http://bakalu.in/wp-content/uploads/2015/09/Raw-Banana.jpg",3,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Black Berry","http://3.imimg.com/data3/JD/YL/MY-2762220/blackberry-250x250.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Chickoo","http://imghost1.indiamart.com/data2/VM/SK/IMFCP-3283736/fresh-fruits-828505-250x250.jpg",5,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Cucumber","http://static.wixstatic.com/media/7a456c_8f59c685d9b746a89746f48511388cbb.jpg_256",1,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Custard apple","http://img1.exportersindia.com/product_images/bc-small/dir_108/3225894/custard-apple-1368732.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Dates","http://ghk.h-cdn.co/assets/cm/15/11/54fdcfd529790-dates-rf-200.jpg",5,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Figs","http://www.dansessentials.com/wp-content/uploads/2011/11/fruits_0000s_0000s_0018_figs.png",9,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Grapes","http://static.caloriecount.about.com/images/medium/grapes-157546.jpg",12,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Grapefruit - Pomelo","http://www.frutasiru.com/uploads/catalogo/productos/thumb/pomelo.png",6,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Gauvas","http://catalog.wlimg.com/1/997490/small-images/fresh-red-guava-910893.jpg",9,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Jackfruit","http://img1.exportersindia.com/product_images/bc-small/dir_74/2208948/jack-fruits-1266061.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Lychee - Litchi","http://www.pakissan.com/english/advisory/images/dat.lychee05.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Mango Ripe","http://1.bp.blogspot.com/-bAJap1YWaf8/UBHmM7yagFI/AAAAAAAAABk/u840qocUyZM/s200/mango+2.jpg",8,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Mango Green/Raw","http://loyalbazaar.com/wp-content/uploads/2015/04/raw-mango-200x200.jpg",7,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Muskmelon/Cantalope","http://g03.s.alicdn.com/kf/UT8Sc5GXD0aXXagOFbXL/MUSK-MELON-OIL-WHOLESALE-MUSK-MELON-OIL.jpg_200x200.jpg",1,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Olive","http://www.gourmetsleuth.com/images/default-source/dictionary/gaeta-olives-jpg.jpg?sfvrsn=4",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Orange","http://earthsciencenaturals.com/images/uploads/2013042515541099356_big.jpg",6,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Papaya Raw","http://rakeshfruits.com/image/cache/catalog/Veg/raw-papaya-200x200.jpg",2,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Papaya Ripe","http://images.mathrubhumi.com/english_images/2013/Jul/11/03082_191611.jpg",4,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Pear","http://www.godist.net/img/pears_packham.jpg",5,"4 Minutes"));
        listRecipes.add(new RecipesPojo("Plum","http://www.whatsfresh.co.nz/images/produce/plums-200x200.jpg",4,"4 Minutes"));
        return listRecipes;


    }
}

package com.nihas.smart.chef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.IngredientsAdapter;
import com.nihas.smart.chef.pojos.IngredientsPojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by snyxius on 10/23/2015.
 */
public class IngredientsActivity extends AppCompatActivity{

    RecyclerView mRecyclerView;
    ArrayList<IngredientsPojo> listIngredients;
    IngredientsAdapter ingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView=(RecyclerView)findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        ingAdapter=new IngredientsAdapter(this,getIngredients());
        mRecyclerView.setAdapter(ingAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                Intent intent = new Intent(IngredientsActivity.this, CupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cup) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<IngredientsPojo> getIngredients(){
        listIngredients=new ArrayList<>();
//        String[] array=getResources().getStringArray(R.array.fruits);
//        listIngredients=new ArrayList<>(Arrays.asList(array));
        listIngredients.add(new IngredientsPojo("Apple","http://img1.exportersindia.com/product_images/bc-small/dir_100/2970062/fresh-apple-1062283.jpg"));
        listIngredients.add(new IngredientsPojo("Apricot","http://www.foodallergens.info/foodimages/apricot.jpg"));
        listIngredients.add(new IngredientsPojo("Avocado","http://content.everydayhealth.com/sbd2/cms/how-to-enjoy-avocado-200x200.jpg"));
        listIngredients.add(new IngredientsPojo("Banana - ripe","https://southernmarylandvapes.com/wp-content/uploads/2013/10/Ripe-Banana.jpg"));
        listIngredients.add(new IngredientsPojo("Banana - raw","http://bakalu.in/wp-content/uploads/2015/09/Raw-Banana.jpg"));
        listIngredients.add(new IngredientsPojo("Black Berry","http://3.imimg.com/data3/JD/YL/MY-2762220/blackberry-250x250.jpg"));
        listIngredients.add(new IngredientsPojo("Chickoo","http://imghost1.indiamart.com/data2/VM/SK/IMFCP-3283736/fresh-fruits-828505-250x250.jpg"));
        listIngredients.add(new IngredientsPojo("Cucumber","http://static.wixstatic.com/media/7a456c_8f59c685d9b746a89746f48511388cbb.jpg_256"));
        listIngredients.add(new IngredientsPojo("Custard apple","http://img1.exportersindia.com/product_images/bc-small/dir_108/3225894/custard-apple-1368732.jpg"));
        listIngredients.add(new IngredientsPojo("Dates","http://ghk.h-cdn.co/assets/cm/15/11/54fdcfd529790-dates-rf-200.jpg"));
        listIngredients.add(new IngredientsPojo("Figs","http://www.dansessentials.com/wp-content/uploads/2011/11/fruits_0000s_0000s_0018_figs.png"));
        listIngredients.add(new IngredientsPojo("Grapes","http://static.caloriecount.about.com/images/medium/grapes-157546.jpg"));
        listIngredients.add(new IngredientsPojo("Grapefruit - Pomelo","http://www.frutasiru.com/uploads/catalogo/productos/thumb/pomelo.png"));
        listIngredients.add(new IngredientsPojo("Gauvas","http://catalog.wlimg.com/1/997490/small-images/fresh-red-guava-910893.jpg"));
        listIngredients.add(new IngredientsPojo("Jackfruit","http://img1.exportersindia.com/product_images/bc-small/dir_74/2208948/jack-fruits-1266061.jpg"));
        listIngredients.add(new IngredientsPojo("Lychee - Litchi","http://www.pakissan.com/english/advisory/images/dat.lychee05.jpg"));
        listIngredients.add(new IngredientsPojo("Mango Ripe","http://1.bp.blogspot.com/-bAJap1YWaf8/UBHmM7yagFI/AAAAAAAAABk/u840qocUyZM/s200/mango+2.jpg"));
        listIngredients.add(new IngredientsPojo("Mango Green/Raw","http://loyalbazaar.com/wp-content/uploads/2015/04/raw-mango-200x200.jpg"));
        listIngredients.add(new IngredientsPojo("Muskmelon/Cantalope","http://g03.s.alicdn.com/kf/UT8Sc5GXD0aXXagOFbXL/MUSK-MELON-OIL-WHOLESALE-MUSK-MELON-OIL.jpg_200x200.jpg"));
        listIngredients.add(new IngredientsPojo("Olive","http://www.gourmetsleuth.com/images/default-source/dictionary/gaeta-olives-jpg.jpg?sfvrsn=4"));
        listIngredients.add(new IngredientsPojo("Orange","http://earthsciencenaturals.com/images/uploads/2013042515541099356_big.jpg"));
        listIngredients.add(new IngredientsPojo("Papaya Raw","http://rakeshfruits.com/image/cache/catalog/Veg/raw-papaya-200x200.jpg"));
        listIngredients.add(new IngredientsPojo("Papaya Ripe","http://images.mathrubhumi.com/english_images/2013/Jul/11/03082_191611.jpg"));
        listIngredients.add(new IngredientsPojo("Pear","http://www.godist.net/img/pears_packham.jpg"));
        listIngredients.add(new IngredientsPojo("Plum","http://www.whatsfresh.co.nz/images/produce/plums-200x200.jpg"));
        listIngredients.add(new IngredientsPojo("Pineapple","http://www.tropicalpopsicles.com/image/Pineapple.png"));
        listIngredients.add(new IngredientsPojo("Pomogranate","http://tajagroproducts.com/pomegranates/tajagro_dry-pomegranates.jpg"));
        listIngredients.add(new IngredientsPojo("Sapodilla","http://3.imimg.com/data3/FK/AU/MY-8574577/sapodilla-250x250.jpg"));
        listIngredients.add(new IngredientsPojo("Watermelon","http://www.azspagirls.com/files/2013/08/watermelon-200.jpg"));
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
}

package com.nihas.smart.chef.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.RecipesAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.customui.GradientHalfoverImageDrawable;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nihas on 10-11-2015.
 */
public class RecipeDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    ArrayList<RecipesPojo> listRecipes;
    RecipesAdapter recipAdapter;
    ImageView thumb;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
//        initializeRecylceView();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        extras=getIntent().getExtras();

//        getSupportActionBar().setTitle("Rasperry Ice");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thumb=(ImageView)findViewById(R.id.thumb);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(SmartChefApp.isNetworkAvailable()){
            new getRecipe().execute(extras.getString("RECIPE_ID"));
            Log.e("IID",extras.getString("RECIPE_ID"));
        }

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .displayer(new BitmapDisplayer() {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        int gradientStartColor = Color.parseColor("#00000000");//argb(0, 0, 0, 0);
                        int gradientEndColor = Color.parseColor("#88000000");//argb(255, 0, 0, 0);
                        GradientHalfoverImageDrawable gradientDrawable = new GradientHalfoverImageDrawable(getResources(), bitmap);
                        gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);
                        imageAware.setImageDrawable(gradientDrawable);
                    }
                })
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.destroy();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        imageLoader.displayImage("http://www.twopeasandtheirpod.com/wp-content/uploads/2013/07/Vegan-Coconut-Raspberry-Ice-Cream-7.jpg", thumb, options);
    }

//    private void initializeRecylceView() {
////        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
////        initializeData();
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setHasFixedSize(false);
//        recipAdapter=new RecipesAdapter(this,getIngredients());
//        mRecyclerView.setAdapter(recipAdapter);
//
//    }

//    public ArrayList<RecipesPojo> getIngredients(){
//        listRecipes=new ArrayList<>();
////        String[] array=getResources().getStringArray(R.array.fruits);
////        listIngredients=new ArrayList<>(Arrays.asList(array));
//        listRecipes.add(new RecipesPojo("Apple","http://img1.exportersindia.com/product_images/bc-small/dir_100/2970062/fresh-apple-1062283.jpg",2,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Apricot","http://www.foodallergens.info/foodimages/apricot.jpg",1,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Avocado","http://content.everydayhealth.com/sbd2/cms/how-to-enjoy-avocado-200x200.jpg",8,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Banana - ripe","https://southernmarylandvapes.com/wp-content/uploads/2013/10/Ripe-Banana.jpg",2,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Banana - raw","http://bakalu.in/wp-content/uploads/2015/09/Raw-Banana.jpg",3,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Black Berry","http://3.imimg.com/data3/JD/YL/MY-2762220/blackberry-250x250.jpg",2,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Chickoo","http://imghost1.indiamart.com/data2/VM/SK/IMFCP-3283736/fresh-fruits-828505-250x250.jpg",5,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Cucumber","http://static.wixstatic.com/media/7a456c_8f59c685d9b746a89746f48511388cbb.jpg_256",1,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Custard apple","http://img1.exportersindia.com/product_images/bc-small/dir_108/3225894/custard-apple-1368732.jpg",2,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Dates","http://ghk.h-cdn.co/assets/cm/15/11/54fdcfd529790-dates-rf-200.jpg",5,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Figs","http://www.dansessentials.com/wp-content/uploads/2011/11/fruits_0000s_0000s_0018_figs.png",9,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Grapes","http://static.caloriecount.about.com/images/medium/grapes-157546.jpg",12,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Grapefruit - Pomelo","http://www.frutasiru.com/uploads/catalogo/productos/thumb/pomelo.png",6,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Gauvas","http://catalog.wlimg.com/1/997490/small-images/fresh-red-guava-910893.jpg",9,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Jackfruit","http://img1.exportersindia.com/product_images/bc-small/dir_74/2208948/jack-fruits-1266061.jpg",2,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Lychee - Litchi","http://www.pakissan.com/english/advisory/images/dat.lychee05.jpg",2,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Mango Ripe","http://1.bp.blogspot.com/-bAJap1YWaf8/UBHmM7yagFI/AAAAAAAAABk/u840qocUyZM/s200/mango+2.jpg",8,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Mango Green/Raw","http://loyalbazaar.com/wp-content/uploads/2015/04/raw-mango-200x200.jpg",7,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Muskmelon/Cantalope","http://g03.s.alicdn.com/kf/UT8Sc5GXD0aXXagOFbXL/MUSK-MELON-OIL-WHOLESALE-MUSK-MELON-OIL.jpg_200x200.jpg",1,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Olive","http://www.gourmetsleuth.com/images/default-source/dictionary/gaeta-olives-jpg.jpg?sfvrsn=4",2,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Orange","http://earthsciencenaturals.com/images/uploads/2013042515541099356_big.jpg",6,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Papaya Raw","http://rakeshfruits.com/image/cache/catalog/Veg/raw-papaya-200x200.jpg",2,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Papaya Ripe","http://images.mathrubhumi.com/english_images/2013/Jul/11/03082_191611.jpg",4,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Pear","http://www.godist.net/img/pears_packham.jpg",5,"4 Minutes"));
//        listRecipes.add(new RecipesPojo("Plum","http://www.whatsfresh.co.nz/images/produce/plums-200x200.jpg",4,"4 Minutes"));
//        return listRecipes;
//
//
//    }



    private class getRecipe extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonObject = null;
            try {

                return WebRequest.getDataJSONArray(WebServices.getRecipeById(Integer.parseInt(params[0])));
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONArray jArray) {
            super.onPostExecute(jArray);
            SmartChefApp.showAToast(jArray+"");

            for(int i=0;i<jArray.length();i++){

            }
//            progressBar.setVisibility(View.GONE);
//            onDone(jArray);
        }
    }
}

package com.nihas.smart.chef.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nihas.smart.chef.Constants;
import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.RecipesAdapter;
import com.nihas.smart.chef.adapters.ReviewAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.customui.CircleImageView;
import com.nihas.smart.chef.customui.GradientHalfoverImageDrawable;
import com.nihas.smart.chef.customui.GradientHalfoverImageDrawableTop;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.fragments.ReviewActivity;
import com.nihas.smart.chef.fragments.ReviewDialog;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.pojos.ReviewPojo;
import com.nihas.smart.chef.utils.AutoScrollViewPager;
import com.nihas.smart.chef.utils.CirclePageIndicator;
import com.nihas.smart.chef.utils.PicassoImageLoader;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;
import com.veinhorn.scrollgalleryview.loader.DefaultVideoLoader;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nihas on 10-11-2015.
 */
public class RecipeDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    ArrayList<RecipesPojo> listRecipes;
    RecipesAdapter recipAdapter;
    ImageView thumb,isVeg;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Bundle extras;
    LinearLayout IngView,fullView;
    TextView recipeName,cusineType,howToCook,timeTaken,serves,food_kind,refer,prepTime,cookTime,rate_text;
    ProgressBar pBar;
    Drawable favIcon;
    LinearLayout reviewLayout;
    RatingBar ratingBar;
    Boolean isFav;
    AutoScrollViewPager viewPager;
    CircleImageView pro_pic;
    private ImageLoadingListener imageListener;
    boolean isVideo;
    String Videoid="";
    MenuItem videoitem;
//    private ScrollGalleryView scrollGalleryView;
    public static final ArrayList<ReviewPojo> rvwList=new ArrayList<>();
    private static final ArrayList<String> images = new ArrayList<>(Arrays.asList(
            "http://img1.goodfon.ru/original/1920x1080/d/f5/aircraft-jet-su-47-berkut.jpg",
            "http://www.dishmodels.ru/picture/glr/13/13312/g13312_7657277.jpg",
            "http://img2.goodfon.ru/original/1920x1080/b/c9/su-47-berkut-c-37-firkin.jpg",
            "http://www.avsimrus.com/file_images/15/img4951_1.jpg",
            "http://www.avsimrus.com/file_images/15/img4951_3.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/0/07/Sukhoi_Su-47_in_2008.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/b/b4/Sukhoi_Su-47_Berkut_%28S-37%29_in_2001.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37333-4.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-0.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-6.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-9.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-2.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-1.jpg"
    ));

    private Bitmap convertDrawableToBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        isVideo=false;
//        initializeRecylceView();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        extras=getIntent().getExtras();
        pro_pic=(CircleImageView)findViewById(R.id.pro_pic);


        fullView=(LinearLayout)findViewById(R.id.full_layout);
        fullView.setVisibility(View.GONE);
        rate_text=(TextView)findViewById(R.id.review_text);
        String rate=SmartChefApp.readFromPreferences(getApplicationContext(), "R_RATING", "");
//        String s = String.format("%.2f", 1.2975118);
        rate_text.setText(String.valueOf(rate));
        final FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);

        MyDbHandler dbHandler = new MyDbHandler(RecipeDetailsActivity.this, null, null, 1);
        if(dbHandler.isFav(SmartChefApp.readFromPreferences(getApplicationContext(), "RID", ""))){
            fab.setImageDrawable(getResources().getDrawable(R.drawable.heart));
            isFav=true;
        }else{
            fab.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            isFav=false;
        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFav) {


                    MyDbHandler dbHandler = new MyDbHandler(getApplicationContext(), null, null, 1);

                    RecipesPojo recipesPojo = new RecipesPojo();
                    recipesPojo.setId(SmartChefApp.readFromPreferences(getApplicationContext(), "RID", ""));
                    recipesPojo.setName(SmartChefApp.readFromPreferences(getApplicationContext(), "RNAME", ""));
                    recipesPojo.setVeg(SmartChefApp.readFromPreferences(getApplicationContext(), "RVEG", ""));
                    recipesPojo.setServes(SmartChefApp.readFromPreferences(getApplicationContext(), "RSERVE", ""));
                    recipesPojo.setFood_kind(SmartChefApp.readFromPreferences(getApplicationContext(), "RFOOD_KIND", ""));
                    recipesPojo.setCuisine(SmartChefApp.readFromPreferences(getApplicationContext(), "RCUISINE", ""));
                    recipesPojo.setPreparation_time(SmartChefApp.readFromPreferences(getApplicationContext(), "RPREP_TIME", ""));
                    recipesPojo.setMedia_url(SmartChefApp.readFromPreferences(getApplicationContext(), "RMEDIA_URL", ""));


                    if (dbHandler.addtoFav(recipesPojo)) {
                        Toast.makeText(getApplicationContext(), SmartChefApp.readFromPreferences(getApplicationContext(), "RNAME", "")+ "Added to fav", Toast.LENGTH_SHORT).show();
//                    MainActivity.showSnak(SmartChefApp.readFromPreferences(getApplicationContext(), "RNAME", "") + " Added to Fav", item.getActionView());
                        fab.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.heart));
                        isFav=true;
                    } else
                        Toast.makeText(getApplicationContext(), "FAILED ADD", Toast.LENGTH_SHORT).show();

                }else{
                    MyDbHandler dbHandler = new MyDbHandler(getApplicationContext(), null, null, 1);
                    if (dbHandler.deletefromFav(SmartChefApp.readFromPreferences(getApplicationContext(), "RID", ""))) {
                        Toast.makeText(getApplicationContext(), SmartChefApp.readFromPreferences(getApplicationContext(), "RNAME", "")+ "Removed from favourites", Toast.LENGTH_SHORT).show();
                        fab.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.fav));
                        isFav=false;

                    } else
                        Toast.makeText(getApplicationContext(), "FAILED REMOV", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        fullView.setVisibility(View.GONE);



//        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
//        Bitmap bitmap = convertDrawableToBitmap(R.drawable.wallpaper7);
//        scrollGalleryView
//                .setThumbnailSize(100)
//                .setZoom(false)
//                .setFragmentManager(getSupportFragmentManager());

        imageListener = new ImageDisplayListener();


        reviewLayout=(LinearLayout)findViewById(R.id.review_layout);
        thumb=(ImageView)findViewById(R.id.thumb);
        thumb.setVisibility(View.GONE);
        pBar=(ProgressBar)findViewById(R.id.progressBar);
        IngView=(LinearLayout)findViewById(R.id.ing_view);
        recipeName=(TextView)findViewById(R.id.recipe_name);
        cusineType=(TextView)findViewById(R.id.cuisine_type);
        howToCook=(TextView)findViewById(R.id.how_to_cook);
        timeTaken=(TextView)findViewById(R.id.time_taken);
        isVeg=(ImageView)findViewById(R.id.is_veg);
        serves=(TextView)findViewById(R.id.serves);
        food_kind=(TextView)findViewById(R.id.food_kind);
        refer=(TextView)findViewById(R.id.reference);
        prepTime=(TextView)findViewById(R.id.prep_time);
        cookTime=(TextView)findViewById(R.id.cook_time);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
//        getSupportActionBar().setTitle("Rasperry Ice");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecipeDetailsActivity.this, WebViewActivity.class));

            }
        });

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .displayer(new BitmapDisplayer() {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        int gradientStartColor = Color.parseColor("#88000000");//argb(0, 0, 0, 0);
                        int gradientEndColor = Color.parseColor("#00000000");//argb(255, 0, 0, 0);
                        GradientHalfoverImageDrawableTop gradientDrawable = new GradientHalfoverImageDrawableTop(getResources(), bitmap);
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
        imageLoader.displayImage(SmartChefApp.readFromPreferences(getApplicationContext(), "profile_pic", ""), pro_pic);


        reviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fm = getSupportFragmentManager();
//                ReviewDialog dialogFragment = new ReviewDialog();
//                dialogFragment.show(fm, "Sample Fragment");

                Intent review = new Intent(RecipeDetailsActivity.this, ReviewActivity.class);
                review.putExtra("rid", extras.getString("RECIPE_ID"));
                startActivity(review);
            }
        });
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.color7), PorterDuff.Mode.SRC_ATOP);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                FragmentManager fm = getSupportFragmentManager();
                ReviewDialog dialogFragment = new ReviewDialog();
                Bundle bund=new Bundle();
                bund.putFloat("RatingStar",rating);
                bund.putString("rid", extras.getString("RECIPE_ID"));
                dialogFragment.setArguments(bund);
                dialogFragment.show(fm, "Sample Fragment");

            }
        });

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

            pBar.setVisibility(View.GONE);
            fullView.setVisibility(View.VISIBLE);
            thumb.setVisibility(View.VISIBLE);

            ArrayList<RecipesPojo> attachArray=new ArrayList<>();
//            for(int i=0;i<jArray.length();i++){

                try {
                    JSONObject jobj=jArray.getJSONObject(0);
                    if(!jobj.isNull(Keys.name))
                    recipeName.setText(jobj.getString("name"));


                    if(!jobj.isNull("how_to_cook"))
                    howToCook.setText(jobj.getString("how_to_cook"));
                    if(!jobj.isNull("cuisine"))
                        cusineType.setText(jobj.getString("cuisine") +" CUISINE");
                    if(!jobj.isNull("serves"))
                        serves.setText(jobj.getString("serves"));
                    if(!jobj.isNull("food_kind"))
                        food_kind.setText(", "+jobj.getString("food_kind"));
                    if(!jobj.isNull("reference"))
                        refer.setText("By: "+jobj.getString("reference"));
                    if(!jobj.isNull("preparation_time"))
                        prepTime.setText(jobj.getString("preparation_time"));
                    if(!jobj.isNull("cook_time"))
                        cookTime.setText(jobj.getString("cook_time"));
                    if(!jobj.isNull("total_time"))
                        timeTaken.setText(jobj.getString("total_time"));
                    if(!jobj.isNull("veg")) {
                        if(Integer.parseInt(jobj.getString("veg"))==1){
                            isVeg.setImageResource(R.drawable.veg_icon);
                        }else{
                            isVeg.setImageResource(R.drawable.non_veg_icon);
                        }
                    }
                    JSONArray attachmentarr=jobj.getJSONArray("attachments");
                    SmartChefApp.saveToPreferences(getApplicationContext(),"ATTACHMENTS",attachmentarr+"");
                    List<MediaInfo> infos = new ArrayList<>(images.size());
                    for(int j=0;j<attachmentarr.length();j++) {
                        JSONObject jobj2=attachmentarr.getJSONObject(j);
                        RecipesPojo pojo=new RecipesPojo();
                        if((!jobj2.isNull("media_type"))&&(!jobj2.isNull("media_url"))){

                            if(jobj2.getString("media_type").equals("video")){
                                isVideo=true;
                                Videoid=jobj2.getString("media_url");
//                                pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
//                                scrollGalleryView.addMedia(MediaInfo.mediaLoader(
//                                        new DefaultVideoLoader(jobj2.getString("media_url"), R.drawable.default_video)));
//                                imageLoader.displayImage("http://collegemix.ca/img/placeholder.png", thumb, options);
                            }else{
                                pojo.setMedia_url(jobj2.getString("media_url"));
//                                for (String url : images) {
//                                    infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(jobj2.getString("media_url"))));
//                                scrollGalleryView.addMedia(infos);
//                                }
                                imageLoader.displayImage(jobj2.getString("media_url"), thumb, options);
                            }
                        }
                        attachArray.add(pojo);
                    }


                        JSONArray reviewarr = jobj.getJSONArray("reviews");
                        rvwList.clear();
                        for (int j = 0; j < reviewarr.length(); j++) {
                            JSONObject jobjrev = reviewarr.getJSONObject(j);
                            ReviewPojo rwpojo = new ReviewPojo();
                            rwpojo.setRid(jobjrev.getString("rid"));
                            rwpojo.setUser(jobjrev.getString("user"));
                            rwpojo.setRating(jobjrev.getString("rating"));
                            rwpojo.setReview(jobjrev.getString("review"));
                            rvwList.add(rwpojo);

                        }

                    JSONArray jarr2=jobj.getJSONArray("ingredients");
                    for(int j=0;j<jarr2.length();j++) {

                        JSONObject jobj2=jarr2.getJSONObject(j);

                        LinearLayout childLayout = new LinearLayout(
                                RecipeDetailsActivity.this);

                        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        childLayout.setLayoutParams(linearParams);

                        TextView mType = new TextView(RecipeDetailsActivity.this);
                        TextView mValue = new TextView(RecipeDetailsActivity.this);

//                        mType.setLayoutParams(new TableLayout.LayoutParams(
//                                0,
//                                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        mType.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        mValue.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

//                        mType.setTextSize(17);
                        mType.setPadding(5, 8, 0, 8);
                        mType.setTypeface(Typeface.DEFAULT);

//                        mValue.setTextSize(16);
                        mValue.setPadding(0, 10, 0, 10);
                        mValue.setTypeface(Typeface.DEFAULT);

                        mType.setText(jobj2.getString("name"));
                        if(!jobj2.isNull("qty"))
                            mValue.setText(jobj2.getString("qty"));
                        else
                            mValue.setText("0 "+jobj2.getString("qty_type"));


                        childLayout.addView(mValue);
                        childLayout.addView(mType);

                        IngView.addView(childLayout);




//                        LinearLayout inside=new LinearLayout(RecipeDetailsActivity.this);
//                        inside.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                        inside.setOrientation(LinearLayout.HORIZONTAL);
//
//
//                        TextView ing=new TextView(RecipeDetailsActivity.this);
////                        ing.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1));
//                        ing.setText(jobj2.getString("name"));
//                        inside.addView(ing);
//
//                        TextView qty=new TextView(RecipeDetailsActivity.this);
////                        qty.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1));
//                        qty.setText(jobj2.getString("qty_type"));
//                        inside.addView(ing);
//
//                        IngView.addView(inside);




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//            }

            if(isVideo)
                videoitem.setVisible(true);
            else
                videoitem.setVisible(false);

            viewPager = (AutoScrollViewPager) findViewById(R.id.pager);

            viewPager.setInterval(Constants.SPLASH_TIME_OUT);
            viewPager.startAutoScroll();
            viewPager.setAdapter(new ViewPagerAdapter(attachArray));
            CirclePageIndicator mIndicator  = (CirclePageIndicator) findViewById(R.id.indicator);
            mIndicator.setViewPager(viewPager);
//            progressBar.setVisibility(View.GONE);
//            onDone(jArray);
        }
    }

    private static class ImageDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fab, menu);
        videoitem = menu.findItem(R.id.action_video);

//        MyDbHandler dbHandler = new MyDbHandler(RecipeDetailsActivity.this, null, null, 1);
//        if(dbHandler.isFav(SmartChefApp.readFromPreferences(getApplicationContext(), "RID", ""))){
//            item.setIcon(getResources().getDrawable(R.drawable.heart_white));
//            isFav=true;
//        }else{
//            item.setIcon(getResources().getDrawable(R.drawable.fav_white));
//            isFav=false;
//        }
        return true;
    }

    public void onShareItem(View v) {
        // Get access to bitmap image from view
//        ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(thumb);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Smart Chef to view recipe: " + recipeName.getText());
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {

            onShareItem(thumb);
        }else if(id==R.id.action_video){
            if(!Videoid.equals(""))
            startVideo(Videoid);
        }

//            String image_url = "http://images.cartradeexchange.com//img//800//vehicle//Honda_Brio_562672_5995_6_1438153637072.jpg";
//            Uri uri =  Uri.parse( "http://images.cartradeexchange.com//img//800//vehicle//Honda_Brio_562672_5995_6_1438153637072.jpg" );
//
//            Intent shareIntent = new Intent();
//            shareIntent.setType("image/*");
//            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            shareIntent.setAction(Intent.ACTION_SEND);
//
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Smart Chef to view recipe: "+recipeName.getText());
//            shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
//            // Target whatsapp:
//            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            try {
//                startActivity(shareIntent);
//            } catch (android.content.ActivityNotFoundException ex) {
//                ex.printStackTrace();
//            }
//        }
//        else if(id== R.id.action_like){
//
//
//            if (!isFav) {
//
//
//                MyDbHandler dbHandler = new MyDbHandler(getApplicationContext(), null, null, 1);
//
//                RecipesPojo recipesPojo = new RecipesPojo();
//                recipesPojo.setId(SmartChefApp.readFromPreferences(getApplicationContext(), "RID", ""));
//                recipesPojo.setName(SmartChefApp.readFromPreferences(getApplicationContext(), "RNAME", ""));
//                recipesPojo.setVeg(SmartChefApp.readFromPreferences(getApplicationContext(), "RVEG", ""));
//                recipesPojo.setServes(SmartChefApp.readFromPreferences(getApplicationContext(), "RSERVE", ""));
//                recipesPojo.setFood_kind(SmartChefApp.readFromPreferences(getApplicationContext(), "RFOOD_KIND", ""));
//                recipesPojo.setCuisine(SmartChefApp.readFromPreferences(getApplicationContext(), "RCUISINE", ""));
//                recipesPojo.setPreparation_time(SmartChefApp.readFromPreferences(getApplicationContext(), "RPREP_TIME", ""));
//                recipesPojo.setMedia_url(SmartChefApp.readFromPreferences(getApplicationContext(), "RMEDIA_URL", ""));
//
//
//                if (dbHandler.addtoFav(recipesPojo)) {
//                    Toast.makeText(getApplicationContext(), SmartChefApp.readFromPreferences(getApplicationContext(), "RNAME", "")+ "Added to fav", Toast.LENGTH_SHORT).show();
////                    MainActivity.showSnak(SmartChefApp.readFromPreferences(getApplicationContext(), "RNAME", "") + " Added to Fav", item.getActionView());
//                    item.setIcon(getApplicationContext().getResources().getDrawable(R.drawable.heart_white));
//                    isFav=true;
//                } else
//                    Toast.makeText(getApplicationContext(), "FAILED ADD", Toast.LENGTH_SHORT).show();
//
//            }else{
//                MyDbHandler dbHandler = new MyDbHandler(getApplicationContext(), null, null, 1);
//                if (dbHandler.deletefromFav(SmartChefApp.readFromPreferences(getApplicationContext(), "RID", ""))) {
//                    Toast.makeText(getApplicationContext(), SmartChefApp.readFromPreferences(getApplicationContext(), "RNAME", "")+ "Removed from favourites", Toast.LENGTH_SHORT).show();
//                    item.setIcon(getApplicationContext().getResources().getDrawable(R.drawable.fav_white));
//                    isFav=false;
//
//                } else
//                    Toast.makeText(getApplicationContext(), "FAILED REMOV", Toast.LENGTH_SHORT).show();
//            }

//        }
        return super.onOptionsItemSelected(item);
    }


    private void startVideo(String videoID)
    { // default youtube app
     Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoID));
//     List<ResolveInfo> list = getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
//     if (list.size() == 0) {
    // default youtube app not present or doesn't conform to the standard we know
    // use our own activity
//     i = new Intent(getApplicationContext(), YouTube.class);
//     i.putExtra("VIDEO_ID", videoID);
//     }
     startActivity(i);
     }



    public class ViewPagerAdapter extends PagerAdapter {

        private ArrayList<RecipesPojo> iconResId;

        public ViewPagerAdapter(ArrayList<RecipesPojo> iconResId) {

            this.iconResId = iconResId;
//            this.hintArrayResId = hintArrayResId;
        }


        @Override
        public int getCount() {
            return iconResId.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

//            Drawable icon = getResources().obtainTypedArray(iconResId).getDrawable(position);
//            String hint = getResources().getStringArray(hintArrayResId)[position];



            View itemView = getLayoutInflater().inflate(R.layout.gallery_item, container, false);

            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
//            TextView hintView = (TextView)itemView.findViewById(R.id.landing_txt_hint);

            imageLoader.displayImage(iconResId.get(position).getMedia_url(), iconView, options,imageListener);
//            iconView.setImageDrawable(icon);
//            hintView.setText(hint);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(RecipeDetailsActivity.this, ActivityGallery.class);
                    startActivity(galleryIntent);

                }
            });

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }
    }
}



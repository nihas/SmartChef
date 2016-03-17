package com.nihas.smart.chef.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.CookBook;
import com.nihas.smart.chef.activities.MainActivity;
import com.nihas.smart.chef.activities.RecipeActivity;
import com.nihas.smart.chef.activities.RecipeDetailsActivity;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.customui.GradientHalfoverImageDrawable;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snyxius on 10/14/2015.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<RecipesPojo> mDataset;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Activity activity;
    Dialog dialog;

    static String measurement;
    private static int colorCode=0;
    static int[] colorArray={R.color.color1,R.color.color2, R.color.color3,R.color.color4,
            R.color.color5,R.color.color6,R.color.color7,R.color.color8};
    String[] values = new String[] { "--- SELECT UNIT ---" ,"Gram",
            "Milli Litre",
            "Cup",
            "Tea Spoon",
            "Pieces"
    };

    public RecipesAdapter(Activity activity, ArrayList<RecipesPojo> ingredients) {
        mDataset = ingredients;
        this.activity=activity;
        imageLoader = ImageLoader.getInstance();

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title,serveTo,timeTaken,cusine;
        ImageView thumbnail,foodType,fav;
        CardView recipeView;
        RatingBar ratingBar;
//        LinearLayout addLayout,plusMinusLayout;
        public ViewHolder(View v) {
            super(v);
            title=(TextView)v.findViewById(R.id.recipe_title);
//            mTitleLetter=(TextView)v.findViewById(R.id.titleLetter);
            serveTo=(TextView)v.findViewById(R.id.serve_to);
            timeTaken=(TextView)v.findViewById(R.id.time_taken);
            cusine=(TextView)v.findViewById(R.id.cusine);
//            minusToCup=(TextView)v.findViewById(R.id.minusToCup);
            foodType=(ImageView)v.findViewById(R.id.food_type);
            fav=(ImageView)v.findViewById(R.id.fav);
            recipeView=(CardView)v.findViewById(R.id.recipe_card);
            ratingBar=(RatingBar)v.findViewById(R.id.ratingBar);
//            addPlus=(TextView)v.findViewById(R.id.addPlus);
//            addLayout=(LinearLayout)v.findViewById(R.id.add_layout);
//            addLayout.setVisibility(View.VISIBLE);
//            plusMinusLayout=(LinearLayout)v.findViewById(R.id.plus_minus_layout);
//            plusMinusLayout.setVisibility(View.GONE);
//            ingMeasure=(TextView)v.findViewById(R.id.ingredientMeasure);
//            ingMeasure.setVisibility(View.GONE);
            thumbnail=(ImageView)v.findViewById(R.id.thumb_image);
//            quantity_text.setText("0");

//            mTextView = v;
        }
    }





    // Create new views (invoked by the layout manager)
//    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item_new, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
//                .displayer(new BitmapDisplayer() {
//                    @Override
//                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
//                        int gradientStartColor = Color.parseColor("#00000000");//argb(0, 0, 0, 0);
//                        int gradientEndColor = Color.parseColor("#88000000");//argb(255, 0, 0, 0);
//                        GradientHalfoverImageDrawable gradientDrawable = new GradientHalfoverImageDrawable(activity.getResources(), bitmap);
//                        gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);
//                        imageAware.setImageDrawable(gradientDrawable);
//                    }
//                })
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();





        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mDataset.isEmpty()){
            RecipeActivity.showEmptyView();
        }else {
            RecipeActivity.hideEmptyView();
        }

        holder.title.setText(mDataset.get(position).getName());
        imageLoader.displayImage(mDataset.get(position).getMedia_url(), holder.thumbnail, options);
//        mImageFetcher.loadImage(mDataset.get(position).getUrl(), holder.mRimageView);
    holder.serveTo.setText(mDataset.get(position).getServes() + "");
        holder.cusine.setText(mDataset.get(position).getCuisine());
        holder.timeTaken.setText(mDataset.get(position).getPreparation_time());
        if(Integer.parseInt(mDataset.get(position).getVeg())==1){
            holder.foodType.setImageResource(R.drawable.veg_icon);
        }else{
            holder.foodType.setImageResource(R.drawable.non_veg_icon);
        }
        holder.ratingBar.setRating(Float.parseFloat(mDataset.get(position).getRating()));



        holder.recipeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RecipeDetailsActivity.class);
                intent.putExtra("RECIPE_ID",mDataset.get(position).getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);

                SmartChefApp.saveToPreferences(activity, "RID", mDataset.get(position).getId());
                SmartChefApp.saveToPreferences(activity,"RNAME",mDataset.get(position).getName());
                SmartChefApp.saveToPreferences(activity,"RVEG",mDataset.get(position).getVeg());
                SmartChefApp.saveToPreferences(activity,"RSERVE",mDataset.get(position).getServes());
                SmartChefApp.saveToPreferences(activity,"RFOOD_KIND",mDataset.get(position).getFood_kind());
                SmartChefApp.saveToPreferences(activity, "RCUISINE", mDataset.get(position).getCuisine());
                SmartChefApp.saveToPreferences(activity, "RPREP_TIME", mDataset.get(position).getPreparation_time());
                SmartChefApp.saveToPreferences(activity, "RMEDIA_URL", mDataset.get(position).getMedia_url());
                SmartChefApp.saveToPreferences(activity,"R_RATING",String.valueOf(mDataset.get(position).getRating()));
            }
        });

        MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
        if(dbHandler.isFav(mDataset.get(position).getId())){
            holder.fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart));
        }else{
            holder.fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.fav));
        }

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = (ImageView) view;
                if (img.getDrawable().getConstantState().equals
                        (activity.getResources().getDrawable(R.drawable.fav).getConstantState())) {


                    MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);

                    RecipesPojo recipesPojo = new RecipesPojo();
                    recipesPojo.setId(mDataset.get(position).getId());
                    recipesPojo.setName(mDataset.get(position).getName());
                    recipesPojo.setVeg(mDataset.get(position).getVeg());
                    recipesPojo.setServes(mDataset.get(position).getServes());
                    recipesPojo.setFood_kind(mDataset.get(position).getFood_kind());
                    recipesPojo.setCuisine(mDataset.get(position).getCuisine());
                    recipesPojo.setPreparation_time(mDataset.get(position).getPreparation_time());
                    recipesPojo.setMedia_url(mDataset.get(position).getMedia_url());
                    recipesPojo.setRating(mDataset.get(position).getRating());


                    if (dbHandler.addtoFav(recipesPojo)) {

                        SmartChefApp.saveToPreferences(activity, "RID", mDataset.get(position).getId());
                        SmartChefApp.saveToPreferences(activity,"RNAME",mDataset.get(position).getName());
                        SmartChefApp.saveToPreferences(activity,"RVEG",mDataset.get(position).getVeg());
                        SmartChefApp.saveToPreferences(activity,"RSERVE",mDataset.get(position).getServes());
                        SmartChefApp.saveToPreferences(activity,"RFOOD_KIND",mDataset.get(position).getFood_kind());
                        SmartChefApp.saveToPreferences(activity,"RCUISINE",mDataset.get(position).getCuisine());
                        SmartChefApp.saveToPreferences(activity,"RPREP_TIME",mDataset.get(position).getPreparation_time());
                        SmartChefApp.saveToPreferences(activity,"RMEDIA_URL",mDataset.get(position).getMedia_url());
                        SmartChefApp.saveToPreferences(activity,"R_RATING",String.valueOf(mDataset.get(position).getRating()));


                        MainActivity.showSnak(mDataset.get(position).getName() + " Added to Fav", view);
                        holder.fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart));
                    } else
                        Toast.makeText(activity, "FAILED ADD", Toast.LENGTH_SHORT).show();

                }else{
                    MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                    if (dbHandler.deletefromFav(mDataset.get(position).getId())) {
                        MainActivity.showSnak(mDataset.get(position).getName() + "Remov frm Fav", view);
                        holder.fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.fav));

                        if(mDataset.isEmpty())
                            CookBook.updateView();
                    } else
                        Toast.makeText(activity, "FAILED REMOV", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnDataChangeListener{
        public void onDataChanged(int size);
    }

    OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    private void doButtonOneClickActions(int size) {

        if(mOnDataChangeListener != null){
            mOnDataChangeListener.onDataChanged(size);
        }
    }

}
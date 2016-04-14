package com.nihas.smart.chef.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.ProgressBar;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by snyxius on 10/14/2015.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecipesPojo> mDataset;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Activity activity;
    Dialog dialog;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

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


    public boolean isFooterEnabled() {
        return isFooterEnabled;
    }

    public void setIsFooterEnabled(boolean isFooterEnabled) {
        this.isFooterEnabled = isFooterEnabled;
    }

    private boolean isFooterEnabled = true;

    public RecipesAdapter(Activity activity, ArrayList<RecipesPojo> ingredients) {
        mDataset = ingredients;
        this.activity=activity;
        imageLoader = ImageLoader.getInstance();


    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title,serveTo,timeTaken,cusine,refer;
        ImageView thumbnail,foodType,fav;
        CardView recipeView;
        RatingBar ratingBar;
        ImageView share;
//        LinearLayout addLayout,plusMinusLayout;
        public MyViewHolder(View v) {
            super(v);
            share=(ImageView)v.findViewById(R.id.share_action);
            title=(TextView)v.findViewById(R.id.recipe_title);
//            mTitleLetter=(TextView)v.findViewById(R.id.titleLetter);
            serveTo=(TextView)v.findViewById(R.id.serve_to);
            timeTaken=(TextView)v.findViewById(R.id.time_taken);
            cusine=(TextView)v.findViewById(R.id.cusine);
            refer=(TextView)v.findViewById(R.id.reference);
//            minusToCup=(TextView)v.findViewById(R.id.minusToCup);
            foodType=(ImageView)v.findViewById(R.id.food_type);
            fav=(ImageView)v.findViewById(R.id.fav);
            recipeView=(CardView)v.findViewById(R.id.recipe_card);
            ratingBar=(RatingBar)v.findViewById(R.id.ratingBar);
            Drawable drawable = ratingBar.getProgressDrawable();
            Drawable drawable2 = ratingBar.getIndeterminateDrawable();



//            drawable.setColorFilter(Color.parseColor("#efcb07"), PorterDuff.Mode.SRC_ATOP);
//            drawable2.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_ATOP);
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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        }
    }





    // Create new views (invoked by the layout manager)
//    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        RecyclerView.ViewHolder vh;

        if(viewType==VIEW_ITEM) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_item_new, parent, false);
            // set the view's size, margins, paddings and layout parameters


            //MyViewHolder
             vh = new MyViewHolder(v);
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
        }else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }




        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder myViewHolder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(myViewHolder instanceof ProgressViewHolder){

            ((ProgressViewHolder)myViewHolder).progressBar.setIndeterminate(true);

        }else if (mDataset.size() > 0 && position < mDataset.size()) {
            if(myViewHolder instanceof MyViewHolder) {


        if(mDataset.isEmpty()){
            RecipeActivity.showEmptyView();
        }else {
            RecipeActivity.hideEmptyView();
        }

                ((MyViewHolder) myViewHolder).title.setText(mDataset.get(position).getName());
        imageLoader.displayImage(mDataset.get(position).getMedia_url(),  ((MyViewHolder) myViewHolder).thumbnail, options);
                ((MyViewHolder) myViewHolder).thumbnail.setTag( ((MyViewHolder) myViewHolder).title.getText());
//        mImageFetcher.loadImage(mDataset.get(position).getUrl(), holder.mRimageView);
                ((MyViewHolder) myViewHolder).serveTo.setText(mDataset.get(position).getServes() + "");
                ((MyViewHolder) myViewHolder).refer.setText("by "+mDataset.get(position).getReference());
                ((MyViewHolder) myViewHolder).cusine.setText(mDataset.get(position).getCuisine());
                ((MyViewHolder) myViewHolder).timeTaken.setText(mDataset.get(position).getPreparation_time());
        if(Integer.parseInt(mDataset.get(position).getVeg())==1){
            ((MyViewHolder) myViewHolder).foodType.setImageResource(R.drawable.veg_icon);
        }else{
            ((MyViewHolder) myViewHolder).foodType.setImageResource(R.drawable.non_veg_icon);
        }
                ((MyViewHolder) myViewHolder).ratingBar.setRating(Float.parseFloat(mDataset.get(position).getRating()));

                ((MyViewHolder) myViewHolder).share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareItem( ((MyViewHolder) myViewHolder).thumbnail);
            }
        });

                ((MyViewHolder) myViewHolder).recipeView.setOnClickListener(new View.OnClickListener() {
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
            ((MyViewHolder) myViewHolder).fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart));
        }else{
            ((MyViewHolder) myViewHolder).fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.fav));
        }

                ((MyViewHolder) myViewHolder).fav.setOnClickListener(new View.OnClickListener() {
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
                        SmartChefApp.saveToPreferences(activity, "RNAME", mDataset.get(position).getName());
                        SmartChefApp.saveToPreferences(activity, "RVEG", mDataset.get(position).getVeg());
                        SmartChefApp.saveToPreferences(activity, "RSERVE", mDataset.get(position).getServes());
                        SmartChefApp.saveToPreferences(activity, "RFOOD_KIND", mDataset.get(position).getFood_kind());
                        SmartChefApp.saveToPreferences(activity, "RCUISINE", mDataset.get(position).getCuisine());
                        SmartChefApp.saveToPreferences(activity, "RPREP_TIME", mDataset.get(position).getPreparation_time());
                        SmartChefApp.saveToPreferences(activity, "RMEDIA_URL", mDataset.get(position).getMedia_url());
                        SmartChefApp.saveToPreferences(activity, "R_RATING", String.valueOf(mDataset.get(position).getRating()));


                        MainActivity.showSnak(mDataset.get(position).getName() + " Added to CookBook", view);
                        ((MyViewHolder) myViewHolder).fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart));
                    } else
                        Toast.makeText(activity, "FAILED ADD", Toast.LENGTH_SHORT).show();

                } else {
                    MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                    if (dbHandler.deletefromFav(mDataset.get(position).getId())) {
                        MainActivity.showSnak(mDataset.get(position).getName() + " Deleted from CookBook", view);
                        ((MyViewHolder) myViewHolder).fav.setImageDrawable(activity.getResources().getDrawable(R.drawable.fav));

                        if (mDataset.isEmpty())
                            CookBook.updateView();
                    } else
                        Toast.makeText(activity, "FAILED REMOV", Toast.LENGTH_SHORT).show();
                }
            }
        });


        LayerDrawable stars = (LayerDrawable) ((MyViewHolder) myViewHolder).ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(activity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);//FULLY SELECT
        stars.getDrawable(1).setColorFilter(activity.getResources().getColor(R.color.color7), PorterDuff.Mode.SRC_ATOP);//PARTIALLY SELECT
        stars.getDrawable(0).setColorFilter(activity.getResources().getColor(R.color.color7), PorterDuff.Mode.SRC_ATOP);//NOT SELECTED

            }

        }

    }

    public void onShareItem(ImageView v) {
        // Get access to bitmap image from view
//        ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(v);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Smart Chef to view recipe: " + v.getTag());
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            activity.startActivity(Intent.createChooser(shareIntent, "Share Image"));
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
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return (isFooterEnabled && position >= mDataset.size() ) ? VIEW_PROG : VIEW_ITEM;

    }

    public void enableFooter(boolean isEnabled){
        this.isFooterEnabled = isEnabled;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
//        return mDataset.size();
        return  (isFooterEnabled) ? mDataset.size() + 1 : mDataset.size();
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
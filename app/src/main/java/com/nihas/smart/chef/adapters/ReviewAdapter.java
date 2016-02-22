package com.nihas.smart.chef.adapters;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.MainActivity;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;
import com.nihas.smart.chef.pojos.ReviewPojo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snyxius on 10/14/2015.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<ReviewPojo> mDataset;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Context activity;

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

    public ReviewAdapter(Context activity, ArrayList<ReviewPojo> reviews) {
        mDataset = reviews;
        this.activity=activity;
        imageLoader = ImageLoader.getInstance();

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView user,review;
        RatingBar rBar;
        public ViewHolder(View v) {
            super(v);


            user=(TextView)v.findViewById(R.id.userTitle);
            review=(TextView)v.findViewById(R.id.review);
            rBar=(RatingBar)v.findViewById(R.id.ratingBar);
        }
    }





    // Create new views (invoked by the layout manager)
//    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
//        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        SmartChefApp.initImageLoader(activity);
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
//                .displayer(new BitmapDisplayer() {
//            @Override
//            public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
//                int gradientStartColor = Color.parseColor("#00000000");//argb(0, 0, 0, 0);
//                int gradientEndColor = Color.parseColor("#88000000");//argb(255, 0, 0, 0);
//                GradientHalfoverImageDrawable gradientDrawable = new GradientHalfoverImageDrawable(activity.getResources(), bitmap);
//                gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);
//                imageAware.setImageDrawable(gradientDrawable);
//            }
//        })
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

        holder.user.setText(mDataset.get(position).getUser());
        holder.review.setText(mDataset.get(position).getReview());
        holder.rBar.setRating(Float.parseFloat(mDataset.get(position).getRating()));


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}


package com.nihas.smart.chef.adapters;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.CupActivity;
import com.nihas.smart.chef.activities.MainActivity;
import com.nihas.smart.chef.customui.GradientoverImageDrawable;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.fragments.CupFragment;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snyxius on 10/26/2015.
 */
public class CupAdapter extends RecyclerView.Adapter<CupAdapter.ViewHolder> {

    private List<CupPojo> mDataset;
    Activity activity;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public CupAdapter(Activity activity, ArrayList<CupPojo> ingredients) {
        this.activity=activity;
        this.mDataset=new ArrayList<>();
        this.mDataset=ingredients;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
//                .displayer(new BitmapDisplayer() {
//            @Override
//            public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
//                int gradientStartColor = Color.parseColor("#55000000");//argb(0, 0, 0, 0);
//                int gradientEndColor = Color.parseColor("#55000000");//argb(255, 0, 0, 0);
//                GradientoverImageDrawable gradientDrawable = new GradientoverImageDrawable(activity.getResources(), bitmap);
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mIngredient.setText(mDataset.get(position).getIngredientName());
        holder.ingMeasure.setText(mDataset.get(position).getIngredienMeasurement());
        holder.quantity_text.setText(mDataset.get(position).getIngredientQty().toString());
        holder.quantity_text.setVisibility(View.GONE);
        imageLoader.displayImage(mDataset.get(position).getImageUrl(), holder.thumbnail, options);


        holder.minusToCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
//                int quantity = Integer.parseInt(holder.quantity_text.getText().toString());
//                if (quantity > 0) {
//                    quantity--;
//                    holder.quantity_text.setText(String.valueOf(quantity));
//                    if (quantity == 0) {
                        if (dbHandler.deleteProduct(holder.mIngredient.getText().toString())) {
                            Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            notifyItemChanged(position);
                            notifyItemRemoved(position);
                            mDataset.remove(position);
                            Cursor c=dbHandler.getAllCup();
                            CupPojo pojo=new CupPojo();
                            if(c==null) {
                                pojo.setCup_count(0);
                            }
                            else {
                                if(c.getCount()==0)
                                    CupActivity.updateView();
                                else
                                    pojo.setCup_count(c.getCount());
                            }
                            MainActivity.updateCupValue(pojo.getCup_count());


                        }
                        else
                            Toast.makeText(activity, "FAILED Delete", Toast.LENGTH_SHORT).show();
//                    } else {
//                        CupPojo product =
//                                new CupPojo(holder.mIngredient.getText().toString().toString(),mDataset.get(position).getImageUrl());
//
//                        if (dbHandler.updateQty(product))
//                            Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(activity, "FAILED Update", Toast.LENGTH_SHORT).show();
//                    }
                }

        });

        holder.addToCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=Integer.parseInt(holder.quantity_text.getText().toString());

                MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                quantity++;
                holder.quantity_text.setText(String.valueOf(quantity));
                CupPojo product =
                        new CupPojo(holder.mIngredient.getText().toString().toString(),mDataset.get(position).getImageUrl());

                if(dbHandler.updateQty(product))
                    Toast.makeText(activity,"Updated",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity,"FAILED Update",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView addToCup,minusToCup,mIngredient,quantity_text,ingMeasure;
        ImageView thumbnail;
        public ViewHolder(View v) {
            super(v);
            mIngredient=(TextView)v.findViewById(R.id.ingredientTitle);
//            mTitleLetter=(TextView)v.findViewById(R.id.titleLetter);
            ingMeasure=(TextView)v.findViewById(R.id.ingredientMeasure);
            quantity_text=(TextView)v.findViewById(R.id.quantity);
            addToCup=(TextView)v.findViewById(R.id.addToCup);
            minusToCup=(TextView)v.findViewById(R.id.minusToCup);
            thumbnail=(ImageView)v.findViewById(R.id.maskImage);

//            mTextView = v;
        }
    }
}

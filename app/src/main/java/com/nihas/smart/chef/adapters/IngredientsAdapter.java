package com.nihas.smart.chef.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.CupActivity;
import com.nihas.smart.chef.activities.IngredientsActivity;
import com.nihas.smart.chef.activities.MainActivity;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.customui.GradientHalfoverImageDrawable;
import com.nihas.smart.chef.customui.GradientoverImageDrawable;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by snyxius on 10/14/2015.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<IngredientsPojo> mDataset;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Context activity;
    Dialog dialog;
    String ingMeasure_text;

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

    public IngredientsAdapter(Context activity, ArrayList<IngredientsPojo> ingredients) {
        mDataset = ingredients;
        this.activity=activity;
        imageLoader = ImageLoader.getInstance();

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView addToCup,minusToCup,mIngredient,quantity_text,ingMeasure;
        ImageView thumbnail,addPlus;
        LinearLayout addLayout,plusMinusLayout;
        public ViewHolder(View v) {
            super(v);
            mIngredient=(TextView)v.findViewById(R.id.ingredientTitle);
//            mTitleLetter=(TextView)v.findViewById(R.id.titleLetter);
            quantity_text=(TextView)v.findViewById(R.id.quantity);
            addToCup=(TextView)v.findViewById(R.id.addToCup);
            minusToCup=(TextView)v.findViewById(R.id.minusToCup);
            addPlus=(ImageView)v.findViewById(R.id.addPlus);
            addLayout=(LinearLayout)v.findViewById(R.id.add_layout);
            addLayout.setVisibility(View.VISIBLE);
            plusMinusLayout=(LinearLayout)v.findViewById(R.id.plus_minus_layout);
            plusMinusLayout.setVisibility(View.GONE);
            ingMeasure=(TextView)v.findViewById(R.id.ingredientMeasure);
            ingMeasure.setVisibility(View.GONE);
            thumbnail=(ImageView)v.findViewById(R.id.maskImage);
            quantity_text.setText("0");


//            mTextView = v;
        }
    }


    public interface CartChanged{
        public void updateCup(String text);
    }


    // Create new views (invoked by the layout manager)
//    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_list_item3, parent, false);
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
        MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
        if (dbHandler.isIngredients(mDataset.get(position).getName())) {
            holder.addPlus.setImageDrawable(activity.getResources().getDrawable(R.drawable.minus));
        }else {
            holder.addPlus.setImageDrawable(activity.getResources().getDrawable(R.drawable.plus));
        }

        holder.mIngredient.setText(mDataset.get(position).getName());
        imageLoader.displayImage(WebServices.getImagePath(mDataset.get(position).getImage_url()), holder.thumbnail, options);
//        mImageFetcher.loadImage(mDataset.get(position).getUrl(), holder.mRimageView);
//        final ViewHolder innerHolder=holder;
//        holder.minusToCup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
//                int quantity=Integer.parseInt(innerHolder.quantity_text.getText().toString());
//                if(quantity>0) {
//                    quantity--;
//                    innerHolder.quantity_text.setText(String.valueOf(quantity));
//                    if(quantity==0){
//                        if(dbHandler.deleteProduct(innerHolder.mIngredient.getText().toString())) {
//                            Toast.makeText(activity, "Deleted" + position, Toast.LENGTH_SHORT).show();
//                            innerHolder.plusMinusLayout.setVisibility(View.GONE);
//                            innerHolder.addLayout.setVisibility(View.VISIBLE);
//                            innerHolder.ingMeasure.setVisibility(View.GONE);
//                        }
//                        else
//                            Toast.makeText(activity,"FAILED Delete",Toast.LENGTH_SHORT).show();
//                    }else {
//                        CupPojo product =
//                                new CupPojo(innerHolder.mIngredient.getText().toString(), holder.ingMeasure.getText().toString().trim(),mDataset.get(position).getImage_url(), quantity);
//
//                        if (dbHandler.updateQty(product))
//                            Toast.makeText(activity, "Updated"+position, Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(activity, "FAILED Update", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//        final ViewHolder innerHolder2=holder;
//        holder.addToCup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
//                int quantity=Integer.parseInt(innerHolder2.quantity_text.getText().toString().trim());
//                quantity++;
//                innerHolder2.quantity_text.setText(String.valueOf(quantity));
//                CupPojo product =
//                        new CupPojo(innerHolder2.mIngredient.getText().toString(),innerHolder.ingMeasure.getText().toString(),
//                                mDataset.get(position).getImage_url(),quantity);
//
//                if(dbHandler.updateQty(product))
//                    Toast.makeText(activity,"Updated"+position,Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(activity,"FAILED Update",Toast.LENGTH_SHORT).show();
//            }
//        });
       holder.addPlus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

//               int fromLoc[] = new int[2];
//               ImageView img=(ImageView)v.getTag();
//               v.getLocationOnScreen(fromLoc);
//               float startX = fromLoc[0];
//               float startY = fromLoc[1];

//               int toLoc[] = new int[2];
//               v.getRootView().getRight();
//               float destX = toLoc[0];
//               float destY = toLoc[1];
//               Display mdisp = activity.getWindowManager().getDefaultDisplay();
//               Point mdispSize = new Point();
//               mdisp.getSize(mdispSize);
//               float destX = mdispSize.x;
//               float destY = mdispSize.y;
//               Log.e("njaaana",startX+" "+startY+" "+destX+" "+destY);

//               Animation fromAtoB = new TranslateAnimation(
//                       Animation.RELATIVE_TO_PARENT, //from xType
//                       startX,
//                       Animation.RELATIVE_TO_PARENT, //to xType
//                       destX,
//                       Animation.RELATIVE_TO_PARENT, //from yType
//                       startY,
//                       Animation.RELATIVE_TO_PARENT, //to yType
//                       122
//               );
//
//               fromAtoB.setDuration(3000);
////               fromAtoB.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
//               v.setAnimation(fromAtoB);

               ImageView img = (ImageView) v;
               if (img.getDrawable().getConstantState().equals
                       (activity.getResources().getDrawable(R.drawable.plus).getConstantState())){

//                   Animation anim= AnimationUtils.loadAnimation(activity,R.anim.wibble);
//                   img.setAnimation(anim);
//                   anim.start();

                   MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);

               CupPojo product =
                       new CupPojo(mDataset.get(position).getName(),
                               WebServices.getImagePath(mDataset.get(position).getImage_url()));
               if (!dbHandler.isIngredients(mDataset.get(position).getName())) {
                   if (dbHandler.addProduct(product)) {
                       MainActivity.showSnak(mDataset.get(position).getName() + " Added to Cup", v);
                       Cursor c = dbHandler.getAllCup();
                       CupPojo pojo = new CupPojo();
                       if (c == null)
                           pojo.setCup_count(0);
                       else
                           pojo.setCup_count(c.getCount());
                       MainActivity.updateCupValue(pojo.getCup_count());
                       IngredientsActivity.updateCupValue(pojo.getCup_count());
                       holder.addPlus.setImageResource(R.drawable.minus);
                   } else
                       Toast.makeText(activity, "FAILED", Toast.LENGTH_SHORT).show();

               } else {
                   MainActivity.showSnak(mDataset.get(position).getName() + " Already in Cup", v);

               }

           }else{
                   MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                   if (dbHandler.deleteProduct(holder.mIngredient.getText().toString())) {
                       Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                       notifyDataSetChanged();
                       Cursor c=dbHandler.getAllCup();
                       CupPojo pojo=new CupPojo();
                       if(c==null) {
                           pojo.setCup_count(0);
                       }
                       else {

                               pojo.setCup_count(c.getCount());
                       }
                       MainActivity.updateCupValue(pojo.getCup_count());
                       IngredientsActivity.updateCupValue(pojo.getCup_count());
                       holder.addPlus.setImageDrawable(activity.getResources().getDrawable(R.drawable.plus));

                   }
                   else
                       Toast.makeText(activity, "FAILED Delete", Toast.LENGTH_SHORT).show();
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


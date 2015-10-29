package com.nihas.smart.chef.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.nihas.smart.chef.R;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snyxius on 10/14/2015.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<IngredientsPojo> mDataset;
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

    public IngredientsAdapter(Activity activity, ArrayList<IngredientsPojo> ingredients) {
        mDataset = ingredients;
        this.activity=activity;
        imageLoader = ImageLoader.getInstance();

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView addToCup,minusToCup,mIngredient,quantity_text,ingMeasure,addPlus;
        ImageView thumbnail;
        LinearLayout addLayout,plusMinusLayout;
        public ViewHolder(View v) {
            super(v);
            mIngredient=(TextView)v.findViewById(R.id.ingredientTitle);
//            mTitleLetter=(TextView)v.findViewById(R.id.titleLetter);
            quantity_text=(TextView)v.findViewById(R.id.quantity);
            addToCup=(TextView)v.findViewById(R.id.addToCup);
            minusToCup=(TextView)v.findViewById(R.id.minusToCup);
            addPlus=(TextView)v.findViewById(R.id.addPlus);
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





    // Create new views (invoked by the layout manager)
//    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_list_item2, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .displayer(new BitmapDisplayer() {
            @Override
            public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                int gradientStartColor = Color.parseColor("#00000000");//argb(0, 0, 0, 0);
                int gradientEndColor = Color.parseColor("#88000000");//argb(255, 0, 0, 0);
                GradientHalfoverImageDrawable gradientDrawable = new GradientHalfoverImageDrawable(activity.getResources(), bitmap);
                gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);
                imageAware.setImageDrawable(gradientDrawable);
            }
        })
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
        holder.mIngredient.setText(mDataset.get(position).getName());
        imageLoader.displayImage(mDataset.get(position).getImage_url(), holder.thumbnail, options);
//        mImageFetcher.loadImage(mDataset.get(position).getUrl(), holder.mRimageView);
        final ViewHolder innerHolder=holder;
        holder.minusToCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                int quantity=Integer.parseInt(innerHolder.quantity_text.getText().toString());
                if(quantity>0) {
                    quantity--;
                    innerHolder.quantity_text.setText(String.valueOf(quantity));
                    if(quantity==0){
                        if(dbHandler.deleteProduct(innerHolder.mIngredient.getText().toString())) {
                            Toast.makeText(activity, "Deleted" + position, Toast.LENGTH_SHORT).show();
                            innerHolder.plusMinusLayout.setVisibility(View.GONE);
                            innerHolder.addLayout.setVisibility(View.VISIBLE);
                            innerHolder.ingMeasure.setVisibility(View.GONE);
                        }
                        else
                            Toast.makeText(activity,"FAILED Delete",Toast.LENGTH_SHORT).show();
                    }else {
                        CupPojo product =
                                new CupPojo(innerHolder.mIngredient.getText().toString(), holder.ingMeasure.getText().toString().trim(),mDataset.get(position).getImage_url(), quantity);

                        if (dbHandler.updateQty(product))
                            Toast.makeText(activity, "Updated"+position, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(activity, "FAILED Update", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        final ViewHolder innerHolder2=holder;
        holder.addToCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                int quantity=Integer.parseInt(innerHolder2.quantity_text.getText().toString().trim());
                quantity++;
                innerHolder2.quantity_text.setText(String.valueOf(quantity));
                CupPojo product =
                        new CupPojo(innerHolder2.mIngredient.getText().toString(),innerHolder.ingMeasure.getText().toString(),
                                mDataset.get(position).getImage_url(),quantity);

                if(dbHandler.updateQty(product))
                    Toast.makeText(activity,"Updated"+position,Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity,"FAILED Update",Toast.LENGTH_SHORT).show();
            }
        });
        final ViewHolder innerHolder3=holder;
       holder.addPlus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int quantity=Integer.parseInt(innerHolder3.quantity_text.getText().toString());

               if (quantity>=0) {
                   dialog = new Dialog(activity);
                   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                   dialog.setContentView(R.layout.select_qty_dialog2);
                   dialog.setCanceledOnTouchOutside(true);

                   WindowManager.LayoutParams lp = new WindowManager.LayoutParams();//dialog.getWindow().getAttributes();
                   lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                   lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                   lp.dimAmount=0.60f;
                   dialog.getWindow().setAttributes(lp);
                   dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

//                window.setAttributes(lp);
//                   lp.dimAmount = 0.6f;
                   dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                   dialog.getWindow().setGravity(Gravity.CENTER);
//                   lp.gravity = Gravity.CENTER;

//                lp.x = -100;   //x position
//                lp.y = -100;   //y position
                   dialog.show();

                   ImageView ingImage=(ImageView)dialog.findViewById(R.id.ing_thumbnail);
                   final TextView ingTitle=(TextView)dialog.findViewById(R.id.ing_name);
                   TextView ingAdd=(TextView)dialog.findViewById(R.id.ing_add);
                   TextView ingMinus=(TextView)dialog.findViewById(R.id.ing_minus);
                   final TextView ingQty=(TextView)dialog.findViewById(R.id.ing_quantity);
                   final Spinner ingMeasure=(Spinner)dialog.findViewById(R.id.ing_measure);
                   Button ingAddtoCup=(Button)dialog.findViewById(R.id.ing_addtoocup);

                   imageLoader.displayImage(mDataset.get(position).getImage_url(), ingImage, options);
                   ingTitle.setText(mDataset.get(position).getName().toString());
                   ingQty.setText("0");
                   ingAdd.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           int qty = Integer.parseInt(ingQty.getText().toString().trim());
                           qty++;
                           ingQty.setText(String.valueOf(qty));
                       }
                   });
                   ingMinus.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           int qty = Integer.parseInt(ingQty.getText().toString().trim());
                           if(qty>0) {
                               qty--;
                               ingQty.setText(String.valueOf(qty));
                           }
                       }
                   });
                   ingAddtoCup.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if (ingQty.getText().toString().equals("0")) {
                               Snackbar.make(v, "Quantity is 0", Snackbar.LENGTH_LONG)
                                       .setAction("Action", null).show();
                           }else if(ingMeasure.getSelectedItemId()==0){
                               Snackbar.make(v, "Measurement not selected", Snackbar.LENGTH_LONG)
                                       .setAction("Action", null).show();
                           } else {
                               MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);


                               CupPojo product =
                                       new CupPojo(ingTitle.getText().toString().trim(), ingMeasure.getSelectedItem().toString(),
                                               mDataset.get(position).getImage_url(), Integer.parseInt(ingQty.getText().toString().trim()));
                               if (!dbHandler.isIngredients(ingTitle.getText().toString())) {
                                   if (dbHandler.addProduct(product)) {
                                       Toast.makeText(activity, "Added" + position, Toast.LENGTH_SHORT).show();
                                       innerHolder3.quantity_text.setText(ingQty.getText().toString().trim());
                                       innerHolder3.plusMinusLayout.setVisibility(View.VISIBLE);
                                       innerHolder3.addLayout.setVisibility(View.GONE);
                                       innerHolder3.ingMeasure.setVisibility(View.VISIBLE);
                                   }
                                   else
                                       Toast.makeText(activity, "FAILED", Toast.LENGTH_SHORT).show();

                                   dialog.dismiss();
                               } else {
                                   Toast.makeText(activity, "Already Exists", Toast.LENGTH_SHORT).show();
                                   Snackbar.make(v, "Already Exists", Snackbar.LENGTH_LONG)
                                           .setAction("Action", null).show();
//                                   holder.quantity_text.setText("0");
//                                   dialog.dismiss();
                               }
                           }
                       }
                   });


//                   final Spinner spinner = (Spinner) dialog.findViewById(R.id.ing_measure);
                   ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                           android.R.layout.simple_list_item_1, android.R.id.text1, values);
                   ingMeasure.setAdapter(adapter);
                   ingMeasure
                           .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                               @Override
                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                   String imc_met=ingMeasure.getSelectedItem().toString();
                                   ingMeasure.setSelection(position,true);

                               }

                               @Override
                               public void onNothingSelected(AdapterView<?> parent) {

                               }
                           });

//                   spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                       @Override
//                       public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//                           measurement = listView.getItemAtPosition(pos).toString();
////                           int quantity;
////                           quantity=1;
////                           holder.ingMeasure.setVisibility(View.VISIBLE);
//                           holder.ingMeasure.setText(listView.getItemAtPosition(pos).toString());
//                           holder.quantity_text.setText("1");
//
//                           MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
//
//
//                           CupPojo product =
//                                   new CupPojo(holder.mIngredient.getText().toString(), holder.ingMeasure.getText().toString(),
//                                           mDataset.get(position).getImage_url(),1);
//                           if (!dbHandler.isIngredients(holder.mIngredient.getText().toString())) {
//                               if (dbHandler.addProduct(product))
//                                   Toast.makeText(activity, "Added"+position, Toast.LENGTH_SHORT).show();
//                               else
//                                   Toast.makeText(activity, "FAILED", Toast.LENGTH_SHORT).show();
//
//                               dialog.dismiss();
//                           }else{
//                               Toast.makeText(activity, "Already Exists", Toast.LENGTH_SHORT).show();
//                               holder.quantity_text.setText("0");
//                               dialog.dismiss();
//                           }
//                       }
//                   });

               }else{
                   MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                   quantity++;
                   innerHolder3.quantity_text.setText(String.valueOf(quantity));
                   CupPojo product =
                           new CupPojo(innerHolder3.mIngredient.getText().toString(),innerHolder3.ingMeasure.getText().toString(),
                                   mDataset.get(position).getImage_url(),quantity);

                   if(dbHandler.updateQty(product))
                        Toast.makeText(activity,"Updated"+position,Toast.LENGTH_SHORT).show();
                   else
                        Toast.makeText(activity,"FAILED Update",Toast.LENGTH_SHORT).show();
               }
           }
       });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
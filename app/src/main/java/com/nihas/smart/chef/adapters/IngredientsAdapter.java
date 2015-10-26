package com.nihas.smart.chef.adapters;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.nihas.smart.chef.R;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snyxius on 10/14/2015.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<String> mDataset;
//    private ImageFetcher mImageFetcher;
    Activity activity;
    Dialog dialog;

    static String measurement;
    private static int colorCode=0;
    static int[] colorArray={R.color.color1,R.color.color2, R.color.color3,R.color.color4,
            R.color.color5,R.color.color6,R.color.color7,R.color.color8};
    String[] values = new String[] { "Gram",
            "Milli Litre",
            "Cup",
            "Tea Spoon",
            "Pieces"
    };

    public IngredientsAdapter(Activity activity, ArrayList<String> ingredients) {
        mDataset = ingredients;
        this.activity=activity;
//        this.mImageFetcher=mImageFetcher;

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView addToCup,minusToCup,mIngredient,quantity_text,ingMeasure;
        ImageView thumbnail;
        public ViewHolder(View v) {
            super(v);
            mIngredient=(TextView)v.findViewById(R.id.ingredientTitle);
//            mTitleLetter=(TextView)v.findViewById(R.id.titleLetter);
            quantity_text=(TextView)v.findViewById(R.id.quantity);
            addToCup=(TextView)v.findViewById(R.id.addToCup);
            minusToCup=(TextView)v.findViewById(R.id.minusToCup);
            ingMeasure=(TextView)v.findViewById(R.id.ingredientMeasure);
            ingMeasure.setVisibility(View.INVISIBLE);
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
                .inflate(R.layout.ingredients_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mIngredient.setText(mDataset.get(position));
//        mImageFetcher.loadImage(mDataset.get(position).getUrl(), holder.mRimageView);
        holder.minusToCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                int quantity=Integer.parseInt(holder.quantity_text.getText().toString());
                if(quantity>0) {
                    quantity--;
                    holder.quantity_text.setText(String.valueOf(quantity));
                    CupPojo product =
                            new CupPojo(holder.mIngredient.getText().toString(),measurement, quantity);

                    if(dbHandler.updateQty(product))
                        Toast.makeText(activity,"Updated",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(activity,"FAILED Update",Toast.LENGTH_SHORT).show();
                }
            }
        });
       holder.addToCup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int quantity=Integer.parseInt(holder.quantity_text.getText().toString());

               if (quantity==0) {
                   dialog = new Dialog(activity);
                   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                   dialog.setContentView(R.layout.select_qty_dialog);
                   dialog.setCanceledOnTouchOutside(true);

                   WindowManager.LayoutParams lp = new WindowManager.LayoutParams();//dialog.getWindow().getAttributes();
                   lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                   lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                   dialog.getWindow().setAttributes(lp);
                   dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//                window.setAttributes(lp);
                   lp.dimAmount = 0.50f;
                   dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                   dialog.getWindow().setGravity(Gravity.BOTTOM);
                   lp.gravity = Gravity.BOTTOM;
//                lp.x = -100;   //x position
//                lp.y = -100;   //y position
                   dialog.show();

                   final ListView listView = (ListView) dialog.findViewById(R.id.qtyList);
                   ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                           android.R.layout.simple_list_item_1, android.R.id.text1, values);
                   listView.setAdapter(adapter);
                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           measurement=listView.getItemAtPosition(position).toString();
//                           int quantity;
//                           quantity=1;
                           holder.ingMeasure.setVisibility(View.VISIBLE);
                           holder.ingMeasure.setText(listView.getItemAtPosition(position).toString());
                           holder.quantity_text.setText("1");

                           MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);


                           CupPojo product =
                                   new CupPojo(holder.mIngredient.getText().toString(), holder.ingMeasure.getText().toString(),1);

                           if(dbHandler.addProduct(product))
                               Toast.makeText(activity,"Added",Toast.LENGTH_SHORT).show();
                           else
                               Toast.makeText(activity,"FAILED",Toast.LENGTH_SHORT).show();

                           dialog.dismiss();
                       }
                   });
               }else{
                   MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                   quantity++;
                   holder.quantity_text.setText(String.valueOf(quantity));
                   CupPojo product =
                           new CupPojo(holder.mIngredient.getText().toString(),holder.ingMeasure.getText().toString(), quantity);

                   if(dbHandler.updateQty(product))
                        Toast.makeText(activity,"Updated",Toast.LENGTH_SHORT).show();
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
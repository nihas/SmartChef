package com.nihas.smart.chef.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.CupActivity;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snyxius on 10/26/2015.
 */
public class CupAdapter extends RecyclerView.Adapter<CupAdapter.ViewHolder> {

    private List<CupPojo> mDataset;
    Activity activity;

    public CupAdapter(Activity activity, ArrayList<CupPojo> ingredients) {
        this.activity=activity;
        this.mDataset=ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mIngredient.setText(mDataset.get(position).getIngredientName());
        holder.ingMeasure.setText(mDataset.get(position).getIngredienMeasurement());
        holder.quantity_text.setText(mDataset.get(position).getIngredientQty().toString());


        holder.minusToCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbHandler dbHandler = new MyDbHandler(activity, null, null, 1);
                int quantity = Integer.parseInt(holder.quantity_text.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    holder.quantity_text.setText(String.valueOf(quantity));
                    if (quantity == 0) {
                        if (dbHandler.deleteProduct(holder.mIngredient.getText().toString())) {
                            Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            notifyItemChanged(position);
                            notifyItemRemoved(position);
                            mDataset.remove(position);

                        }
                        else
                            Toast.makeText(activity, "FAILED Delete", Toast.LENGTH_SHORT).show();
                    } else {
                        CupPojo product =
                                new CupPojo(holder.mIngredient.getText().toString(), holder.ingMeasure.getText().toString(), quantity);

                        if (dbHandler.updateQty(product))
                            Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(activity, "FAILED Update", Toast.LENGTH_SHORT).show();
                    }
                }
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
                        new CupPojo(holder.mIngredient.getText().toString(),holder.ingMeasure.getText().toString(), quantity);

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

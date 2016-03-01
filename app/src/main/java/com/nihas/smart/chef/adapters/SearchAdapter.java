package com.nihas.smart.chef.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.IngredientsActivity;
import com.nihas.smart.chef.activities.MainActivity;
import com.nihas.smart.chef.activities.RecipeDetailsActivity;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.IngredientsPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AMAN on 11-09-2015.
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<IngredientsPojo> ingData;
    List<RecipesPojo> recipeData;
    private static final int TYPE_ITEM_ING=1;
    private static final int TYPE_ITEM_REC=2;
    private static final int TYPE_ITEM_HEAD1=3;
    private static final int TYPE_ITEM_HEAD2=4;
    private LayoutInflater inflater;
    private Activity context;
    public Boolean isAllAmenitiesShowing=false;
    static Double Latitude,Longitude;

    ImageLoader imageLoader;
    DisplayImageOptions options;



    public SearchAdapter(Activity context,  List<IngredientsPojo> INGData, List<RecipesPojo> RecipeData) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.ingData=INGData;
        this.recipeData=RecipeData;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SmartChefApp.initImageLoader(context);
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();



        if(viewType==TYPE_ITEM_ING){
            View view=inflater.inflate(R.layout.search_item, parent,false);
            HeaderHolder holder=new HeaderHolder(view);
            return holder;
        }
        else if(viewType==TYPE_ITEM_REC){
            View view=inflater.inflate(R.layout.search_item_recipe, parent,false);
            ItemHolder holder=new ItemHolder(view);
            return holder;
        }else if(viewType==TYPE_ITEM_HEAD1){
            View view=inflater.inflate(R.layout.search_item_header, parent,false);
            HeadHolderIng holder=new HeadHolderIng(view);
            return holder;
        }else if(viewType==TYPE_ITEM_HEAD2){
            View view=inflater.inflate(R.layout.search_item_header_rec, parent,false);
            HeadHolderRec holder=new HeadHolderRec(view);
            return holder;
        }
        else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int pos) {
        if (pos > 0){
            final int position=pos-1;
            if (holder instanceof HeaderHolder) {
                final HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.textView_Ing.setText(String.valueOf(ingData.get(position).getName()));
                MyDbHandler dbHandler = new MyDbHandler(context, null, null, 1);
                if (dbHandler.isIngredients(ingData.get(position).getName())) {
                    headerHolder.addPlus.setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
                } else {
                    headerHolder.addPlus.setImageDrawable(context.getResources().getDrawable(R.drawable.plus));
                }
                headerHolder.addPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView img = (ImageView) v;
                        if (img.getDrawable().getConstantState().equals
                                (context.getResources().getDrawable(R.drawable.plus).getConstantState())) {

//                   Animation anim= AnimationUtils.loadAnimation(activity,R.anim.wibble);
//                   img.setAnimation(anim);
//                   anim.start();

                            MyDbHandler dbHandler = new MyDbHandler(context, null, null, 1);

                            CupPojo product =
                                    new CupPojo(ingData.get(position).getName(), "http://collegemix.ca/img/placeholder.png");
                            if (!dbHandler.isIngredients(ingData.get(position).getName())) {
                                if (dbHandler.addProduct(product)) {
                                    MainActivity.showSnak(ingData.get(position).getName() + " Added to Cup", v);
                                    Cursor c = dbHandler.getAllCup();
                                    CupPojo pojo = new CupPojo();
                                    if (c == null)
                                        pojo.setCup_count(0);
                                    else
                                        pojo.setCup_count(c.getCount());
                                    MainActivity.updateCupValue(pojo.getCup_count());
//                                IngredientsActivity.updateCupValue(pojo.getCup_count());
                                    headerHolder.addPlus.setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
                                } else
                                    Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();

                            } else {
                                MainActivity.showSnak(ingData.get(position).getName() + " Already in Cup", v);

                            }

                        } else {
                            MyDbHandler dbHandler = new MyDbHandler(context, null, null, 1);
                            if (dbHandler.deleteProduct(headerHolder.textView_Ing.getText().toString())) {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                Cursor c = dbHandler.getAllCup();
                                CupPojo pojo = new CupPojo();
                                if (c == null) {
                                    pojo.setCup_count(0);
                                } else {

                                    pojo.setCup_count(c.getCount());
                                }
                                MainActivity.updateCupValue(pojo.getCup_count());
                                IngredientsActivity.updateCupValue(pojo.getCup_count());
                                headerHolder.addPlus.setImageDrawable(context.getResources().getDrawable(R.drawable.plus));

                            } else
                                Toast.makeText(context, "FAILED Delete", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


            } else if (holder instanceof ItemHolder) {
                ItemHolder itemHolder = (ItemHolder) holder;
                final int posi = pos - (ingData.size() + 2);
                if (posi < recipeData.size() && posi > -1) {
                    itemHolder.text_rec.setText(String.valueOf(recipeData.get(posi).getName()));
                    imageLoader.displayImage(recipeData.get(posi).getMedia_url(), ((ItemHolder) holder).imageView_rec, options);
                    itemHolder.searchViewItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, RecipeDetailsActivity.class);
                            intent.putExtra("RECIPE_ID", recipeData.get(posi).getId());
                            context.startActivity(intent);
                            context.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
                            context.finish();

                            SmartChefApp.saveToPreferences(context, "RID", recipeData.get(posi).getId());
                            SmartChefApp.saveToPreferences(context,"RNAME",recipeData.get(posi).getName());
                            SmartChefApp.saveToPreferences(context,"RVEG",recipeData.get(posi).getVeg());
                            SmartChefApp.saveToPreferences(context,"RSERVE",recipeData.get(posi).getServes());
                            SmartChefApp.saveToPreferences(context,"RFOOD_KIND",recipeData.get(posi).getFood_kind());
                            SmartChefApp.saveToPreferences(context, "RCUISINE", recipeData.get(posi).getCuisine());
                            SmartChefApp.saveToPreferences(context, "RPREP_TIME", recipeData.get(posi).getPreparation_time());
                            SmartChefApp.saveToPreferences(context, "RMEDIA_URL", recipeData.get(posi).getMedia_url());
                        }
                    });
                }


            } else if (holder instanceof HeadHolderIng) {
                HeadHolderIng headHolderIng = (HeadHolderIng) holder;

            } else if (holder instanceof HeadHolderRec) {
                HeadHolderRec headHolderRec = (HeadHolderRec) holder;
            }

    }

    }



    @Override
    public int getItemCount() {
        return  ingData.size()+recipeData.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_ITEM_HEAD1;
        }else if(position==ingData.size()+1) {
            return TYPE_ITEM_HEAD2;
        }else if(position > ingData.size()){
                return TYPE_ITEM_REC;
            }else{
                return TYPE_ITEM_ING;
            }

    }

    class HeadHolderIng extends RecyclerView.ViewHolder{
        TextView header;

        public HeadHolderIng(View itemView) {
            super(itemView);
//            mapView = (MapView) itemView.findViewById(R.id.map);
            header=(TextView)itemView.findViewById(R.id.header);




        }


    }

    class HeadHolderRec extends RecyclerView.ViewHolder{
        TextView header;

        public HeadHolderRec(View itemView) {
            super(itemView);
//            mapView = (MapView) itemView.findViewById(R.id.map);
            header=(TextView)itemView.findViewById(R.id.header);




        }


    }


    class ItemHolder extends RecyclerView.ViewHolder {


        TextView text_rec;
        ImageView imageView_rec;
        LinearLayout searchViewItem;

        public ItemHolder(View itemView) {
            super(itemView);

            text_rec = (TextView) itemView.findViewById(R.id.textView_item_text);
            imageView_rec = (ImageView) itemView.findViewById(R.id.imageView_item_icon_left);
            searchViewItem=(LinearLayout)itemView.findViewById(R.id.search_view_item);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView textView_Ing;
        ImageView addPlus;



        public HeaderHolder(View itemView) {
            super(itemView);

            textView_Ing = (TextView) itemView.findViewById(R.id.textView_item_text);
            addPlus = (ImageView) itemView.findViewById(R.id.addPlus);



        }
    }
}

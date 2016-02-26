package com.nihas.smart.chef.adapters;

import android.content.Context;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.pojos.RecipesPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AMAN on 11-09-2015.
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> ingData;
    List<RecipesPojo> recipeData;
    private static final int TYPE_ITEM_ING=1;
    private static final int TYPE_ITEM_REC=2;
    private LayoutInflater inflater;
    private Context context;
    public Boolean isAllAmenitiesShowing=false;
    static Double Latitude,Longitude;



    public SearchAdapter(Context context,  List<String> INGData, List<RecipesPojo> RecipeData) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.ingData=INGData;
        this.recipeData=RecipeData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM_ING){
            View view=inflater.inflate(R.layout.search_item, parent,false);
            HeaderHolder holder=new HeaderHolder(view);
            return holder;
        }
        else if(viewType==TYPE_ITEM_REC){
            View view=inflater.inflate(R.layout.search_item_recipe, parent,false);
            ItemHolder holder=new ItemHolder(view);
            return holder;
        }
        else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderHolder ){
            HeaderHolder headerHolder= (HeaderHolder) holder;
            for (int i=0;i<ingData.size();i++){
                headerHolder.textView_Ing.setText(String.valueOf(ingData.get(i)));
            }
        }
        else if(holder instanceof ItemHolder ){
            ItemHolder itemHolder= (ItemHolder) holder;
            for (int j=0;j<recipeData.size();j++){
                itemHolder.text_rec.setText(String.valueOf(recipeData.get(j).getName()));
            }
        }

    }



    @Override
    public int getItemCount() {
        return  ingData.size()+recipeData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < ingData.size()){
            return TYPE_ITEM_ING;
        }else{
             return TYPE_ITEM_REC;
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder{
        MapView mapView;
        TextView noMap;

        public FooterHolder(View itemView) {
            super(itemView);
//            mapView = (MapView) itemView.findViewById(R.id.map);
//            noMap=(TextView)itemView.findViewById(R.id.no_map);




        }


    }


    class ItemHolder extends RecyclerView.ViewHolder {


        TextView text_rec;
        ImageView imageView_rec;

        public ItemHolder(View itemView) {
            super(itemView);

            text_rec = (TextView) itemView.findViewById(R.id.textView_item_text);
            imageView_rec = (ImageView) itemView.findViewById(R.id.imageView_item_icon_left);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView textView_Ing;



        public HeaderHolder(View itemView) {
            super(itemView);

            textView_Ing = (TextView) itemView.findViewById(R.id.textView_item_text);



        }
    }
}

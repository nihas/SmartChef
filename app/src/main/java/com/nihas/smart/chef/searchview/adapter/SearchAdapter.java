package com.nihas.smart.chef.searchview.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.MainActivity;
import com.nihas.smart.chef.customui.GradientHalfoverImageDrawable;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.searchview.view.SearchCodes;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final List<Integer> mStartList = new ArrayList<>();
    private final Context mContext;
    private final int mTheme;
    private List<SearchItem> mSearchList = new ArrayList<>();
    private List<RecipesPojo> mSearchRecipe = new ArrayList<>();
    private List<SearchItem> mDataList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private int mKeyLength = 0;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    private static final int ITEM_VIEW_TYPE_ING = 0;
    private static final int ITEM_VIEW_TYPE_REC = 1;

    public SearchAdapter(Context context, List<SearchItem> searchList, List<SearchItem> dataList, List<RecipesPojo> mSuggestionsRecipe, int theme) {
        this.mContext = context;
        this.mSearchList = searchList;
        this.mDataList = dataList;
        this.mTheme = theme;
        this.mSearchRecipe=mSuggestionsRecipe;
        imageLoader = ImageLoader.getInstance();
    }



    @Override
    public int getItemViewType(int position) {
        if(position>mSearchList.size())
            return ITEM_VIEW_TYPE_REC;
        else
            return ITEM_VIEW_TYPE_ING;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {
                    List<SearchItem> searchData = new ArrayList<>();

                    mStartList.clear();
                    String key = constraint.toString().toLowerCase(Locale.getDefault());

                    for (SearchItem str : mDataList) {
                        String string = str.get_text().toString().toLowerCase(Locale.getDefault());
                        if (string.contains(key)) {
                            searchData.add(str);
                            mStartList.add(string.indexOf(key));
                            mKeyLength = key.length();
                        }
                    }

                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    mSearchList.clear();
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof SearchItem) {
                            mSearchList.add((SearchItem) object);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder().cacheInMemory(true)

                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();


        if(viewType==ITEM_VIEW_TYPE_ING){
            final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
            final View sView = mInflater.inflate(R.layout.search_item, parent, false);
            return new ResultViewHolder(sView);

        }else if(viewType==ITEM_VIEW_TYPE_REC){
            final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
            final View sView = mInflater.inflate(R.layout.search_item_recipe, parent, false);
            return new ResultViewHolder(sView);
        }else
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if(viewHolder instanceof IngHolder ){

            SearchItem item = mSearchList.get(position);

            int start = mStartList.get(position);  // TODO - CANT BE NULL
            int end = start + mKeyLength;

            ((IngHolder) viewHolder).icon_left.setImageResource(item.get_icon());

            if (mTheme == SearchCodes.THEME_LIGHT) {
                ((IngHolder) viewHolder).icon_left.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
                ((IngHolder) viewHolder).text.setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));

                ((IngHolder) viewHolder).text.setText(item.get_text(), TextView.BufferType.SPANNABLE);
                Spannable s = (Spannable) ((IngHolder) viewHolder).text.getText();
                s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.search_light_text_highlight)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (mTheme == SearchCodes.THEME_DARK) {
                ((IngHolder) viewHolder).icon_left.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                ((IngHolder) viewHolder).text.setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));

                ((IngHolder) viewHolder).text.setText(item.get_text(), TextView.BufferType.SPANNABLE);
                Spannable s = (Spannable) ((IngHolder) viewHolder).text.getText();
                s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.search_dark_text_highlight)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            ((IngHolder)viewHolder).addIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                    CharSequence text = textView.getText();
                    MainActivity.hideSearch(text);
//                        Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();

                    MyDbHandler dbHandler = new MyDbHandler(mContext, null, null, 1);

                    CupPojo product =
                            new CupPojo(text.toString(), "http://collegemix.ca/img/placeholder.png");
                    if (!dbHandler.isIngredients(text.toString())) {
                        if (dbHandler.addProduct(product)) {
                            Toast.makeText(mContext, "Added" + position, Toast.LENGTH_SHORT).show();
//                           innerHolder3.quantity_text.setText(ingQty.getText().toString().trim());
//                                       innerHolder3.plusMinusLayout.setVisibility(View.VISIBLE);
//                                       innerHolder3.addLayout.setVisibility(View.GONE);
//                           innerHolder3.ingMeasure.setVisibility(View.VISIBLE);
                            Cursor c=dbHandler.getAllCup();
                            CupPojo pojo=new CupPojo();
                            if(c==null)
                                pojo.setCup_count(0);
                            else
                                pojo.setCup_count(c.getCount());
                            MainActivity.updateCupValue(pojo.getCup_count());
                        }
                        else
                            Toast.makeText(mContext, "FAILED", Toast.LENGTH_SHORT).show();

//                       dialog.dismiss();
                    } else {
                        Toast.makeText(mContext, "Already Exists", Toast.LENGTH_SHORT).show();
//
                    }
                }
            });

        }else if(viewHolder instanceof RecHolder ){
            imageLoader.displayImage(mSearchRecipe.get(position).getMedia_url(), ((RecHolder) viewHolder).icon_left, options);
            ((RecHolder) viewHolder).text.setText(mSearchRecipe.get(position).getName());
            ((RecHolder) viewHolder).searchViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.showSnak("Recipe :" +mSearchRecipe.get(position).getId(),view);
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return mSearchList.size()+mSearchRecipe.size();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView icon_left;
        public final TextView text;
        public final TextView addIcon;

        public ResultViewHolder(View view) {
            super(view);
            icon_left = (ImageView) view.findViewById(R.id.imageView_item_icon_left);
            text = (TextView) view.findViewById(R.id.textView_item_text);
            addIcon = (TextView) view.findViewById(R.id.addPlus);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition()); // getAdapterPosition()
            }
        }
    }

    class IngHolder extends RecyclerView.ViewHolder{
        public final ImageView icon_left;
        public final TextView text;
        public final TextView addIcon;

        public IngHolder(View view) {
            super(view);
            icon_left = (ImageView) view.findViewById(R.id.imageView_item_icon_left);
            text = (TextView) view.findViewById(R.id.textView_item_text);
            addIcon = (TextView) view.findViewById(R.id.addPlus);
        }


    }

    class RecHolder extends RecyclerView.ViewHolder {
        public final ImageView icon_left;
        public final TextView text;
        public final LinearLayout searchViewItem;

        public RecHolder(View view) {
            super(view);
            icon_left = (ImageView) view.findViewById(R.id.imageView_item_icon_left);
            text = (TextView) view.findViewById(R.id.textView_item_text);
            searchViewItem=(LinearLayout)view.findViewById(R.id.search_view_item);
        }
    }

}
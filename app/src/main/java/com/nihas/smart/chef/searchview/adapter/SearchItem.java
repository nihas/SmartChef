package com.nihas.smart.chef.searchview.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.lapism.searchview.R;


public class SearchItem implements Parcelable {

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        public SearchItem createFromParcel(Parcel source) {
            return new SearchItem(source);
        }

        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };
    private int icon;
    private CharSequence text;
    private String ingredients;
    private String recipe_id;
    private String recipe_name;

    public SearchItem() {
    }

    public SearchItem(CharSequence text) {
        this.icon = R.drawable.search_ic_search_black_24dp;
        this.text = text;
    }

    private SearchItem(Parcel in) {
        this.icon = in.readInt();
        this.text = in.readParcelable(CharSequence.class.getClassLoader());
    }

    public int get_icon() {
        return this.icon;
    }

    public void set_icon(int icon) {
        this.icon = icon;
    }

    public CharSequence get_text() {
        return this.text;
    }

    public void set_text(CharSequence text) {
        this.text = text;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.icon);
        TextUtils.writeToParcel(this.text, dest, flags); // dest.writeValue(this.text);
    }

}
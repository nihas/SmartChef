package com.nihas.smart.chef.utils;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;


public  class SearchPojo {

    String product_id;
    String product_name;
    String price;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
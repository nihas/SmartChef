package com.nihas.smart.chef.pojos;

/**
 * Created by snyxius on 10/27/2015.
 */
public class RecipesPojo {

//    String name;
//    String image_url;
//    String serve_to;
//    String time_taken;

    String id;
    String name;
    String veg;
    String serves;
    String food_kind;
    String cuisine;
    String preparation_time;
    String media_url;
    String media_type;
    float rating;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVeg() {
        return veg;
    }

    public void setVeg(String veg) {
        this.veg = veg;
    }

    public String getServes() {
        return serves;
    }

    public void setServes(String serves) {
        this.serves = serves;
    }

    public String getFood_kind() {
        return food_kind;
    }

    public void setFood_kind(String food_kind) {
        this.food_kind = food_kind;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(String preparation_time) {
        this.preparation_time = preparation_time;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }
}

package com.nihas.smart.chef.pojos;

/**
 * Created by snyxius on 10/27/2015.
 */
public class IngredientsPojo {

    String name;
    String image_url;

    public IngredientsPojo(String Name, String Image_url){
        this.name=Name;
        this.image_url=Image_url;
    }

    public IngredientsPojo(){
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }
}

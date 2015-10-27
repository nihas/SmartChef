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

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }
}

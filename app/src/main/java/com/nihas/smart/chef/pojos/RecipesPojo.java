package com.nihas.smart.chef.pojos;

/**
 * Created by snyxius on 10/27/2015.
 */
public class RecipesPojo {

    String name;
    String image_url;
    String serve_to;
    String time_taken;

    public RecipesPojo(String Name, String Image_url, String serve, String time){
        this.name=Name;
        this.image_url=Image_url;
        this.serve_to=serve;
        this.time_taken=time;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getServe_to() {
        return serve_to;
    }

    public String getTime_taken() {
        return time_taken;
    }
}

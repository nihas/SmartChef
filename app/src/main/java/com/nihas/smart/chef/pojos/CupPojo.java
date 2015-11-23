package com.nihas.smart.chef.pojos;

/**
 * Created by snyxius on 10/26/2015.
 */
public class CupPojo {

    private int id;
    private String ingredientName;
    private int ingredientQty;
    private String ingredienMeasurement;
    private String imageUrl;
    public int cup_count;

    public CupPojo(int id,String IngredientName,String ImageUrl){
        this.id=id;
        this.ingredientName=IngredientName;
//        this.ingredienMeasurement=IngredienMeasurement;
//        this.ingredientQty=IngredientQty;
        this.imageUrl=ImageUrl;
    }

    public CupPojo(String IngredientName,String ImageUrl){
        this.id=id;
        this.ingredientName=IngredientName;
//        this.ingredienMeasurement=IngredienMeasurement;
//        this.ingredientQty=IngredientQty;
        this.imageUrl=ImageUrl;
    }

    public CupPojo(){

    }

    public Integer getId() {
        return id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public Integer getIngredientQty() {
        return ingredientQty;
    }

    public String getIngredienMeasurement() {
        return ingredienMeasurement;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setIngredientQty(int ingredientQty) {
        this.ingredientQty = ingredientQty;
    }

    public void setIngredienMeasurement(String ingredienMeasurement) {
        this.ingredienMeasurement = ingredienMeasurement;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public int getCup_count() {
        return cup_count;
    }



    public void setCup_count(int cup_count) {
        this.cup_count = cup_count;
    }
}

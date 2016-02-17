package com.nihas.smart.chef.api;

/**
 * Created by AMAN on 25-10-2015.
 */
public class WebServices {

    public static final String SECRET_KEY="recipeSecretkey";

    public static final String baseUrl = "http://creatingfutures.in/sarath/recipe/apis/";

    public static final String baseUrl2 = "http://airesearch.xyz/recipe/apis/";
    public static final String imageUrl = "http://airesearch.xyz/recipe/";

    public static final String signUp = baseUrl +  "merchants/signUp";

    /*
    Request
    {
        "firstName":"aa",
            "lastName" :"aa",
            "mobile":"1231234",
            "email":"aa@aa.com",
            "establishmentName":"test",
            "password":"aa"
    }*/
/*
Response
          {
            "notice": "SignUp Completed",
            "status": "Success",
            "id": "563bb5e21d76b3ac42d36fd8"
            }
  */

    public static final String signIn = baseUrl  + "merchants/signIn";
    /*
    Request
    {
            "Email":"aa@aa.com",
            "Password":"aa"
    }*/

    /*
    Response
    {
            "status": "Success",
            "id": "563bb5e21d76b3ac42d36fd8",
            "notice": "SignIn Completed"
    }*/

    public static final String forgotPass = baseUrl  + "merchants/forgotPass";
    /*{
        "email":"aa@aa.com"
    }*/

    public static final String category = baseUrl  + "establishmenttype/category";

    public static final String typeDetails = baseUrl  + "establishmenttype/typeDetails";

    /*{
     "type":
                [
                      "Restaurant",
                       "Halls"
                ]

}
*/
    public static final String searchRecipeQuick(String query) {
        return "http://quickshop.ae/beta/api/shops/getProductSearchResultByKeyword?shop_id=1&query="+query;
    }
    public static final String searchIngRecipe(String query) {
        return baseUrl2+"ingnrec.php?ins="+query+"&key="+SECRET_KEY;
    }

    public static final String searchRecipe(String ingredients,int page){
        return baseUrl2 + "searchit.php?ins="+ingredients+"&key="+SECRET_KEY+"&page="+page;
    }

    public static String getImagePath(String path){
        return imageUrl+path;
    }

    public static final String getCategories(){
        return baseUrl2 + "cats.php?key="+SECRET_KEY;
    }

    public static final String getIngredients(int cat_id){
        return baseUrl2 + "cat_ingredients.php?cat="+cat_id+"&key="+SECRET_KEY;
    }

    public static final String getRecipeById(int recipe_id){
        return baseUrl2 + "one_recipe.php?rid="+recipe_id+"&key="+SECRET_KEY;
    }

    public static final String cuisine = baseUrl  + "establishmenttype/cuisine";

    public static final String ambiance = baseUrl  + "establishmenttype/ambiance";

    public static final String allBuisnessProf = baseUrl  + "merchants/allBuisnessProf";


 /*   {
        "notice":{
        "allProfiles":[{
            "Business_Name":"Sample",
                    "Cover_Image":"/FFFABRRRQAUUUUAFFFFAB\nRRRQAUUUUAFFFFABRRRQAUUUUAFFFFAH/9k=\n",
                    "Location_Name":"Domlur Bangalore",
                    "category":"Restaurant",
                    "profileId":"SampleDomlur BangaloreRestaurant"
        }
        ]
    }
    }*/




        public static final String saveDeal = baseUrl  + "merchants/saveDeal";

    public static final String saveBuisnessProf = baseUrl  + "merchants/saveBuisnessProf";

}

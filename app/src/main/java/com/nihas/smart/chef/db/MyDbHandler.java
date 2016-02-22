package com.nihas.smart.chef.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.pojos.RecipesPojo;

/**
 * Created by snyxius on 10/26/2015.
 */
public class MyDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ingredientsDB.db";
    public static final String TABLE_PRODUCTS = "ingredients";
    public static final String TABLE_FAV = "favourites";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "ingredient_name";
    public static final String COLUMN_QUANTITY = "ingredient_quantity";
    public static final String COLUMN_MEASUREMENT = "ingredient_measurement";
    public static final String COLUMN_IMAGE_URL = "image_url";

    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String name="name";
    public static final String veg="veg";
    public static final String serves="serves";
    public static final String food_kind="food_kind";
    public static final String cuisine="cuisine";
    public static final String preparation_time="preparation_time";

    public MyDbHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public synchronized void close() {
        super.close();

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " integer primary key autoincrement," + COLUMN_PRODUCTNAME
                + " TEXT NOT NULL," + COLUMN_MEASUREMENT + " TEXT,"+COLUMN_IMAGE_URL + " TEXT NOT NULL,"+ COLUMN_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        String CREATE_FAV_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_FAV + "("
                + COLUMN_ID + " integer primary key autoincrement," + COLUMN_RECIPE_ID
                + " TEXT NOT NULL," + name + " TEXT,"+veg + " TEXT NOT NULL,"+ serves + " TEXT NOT NULL,"+food_kind+" TEXT NOT NULL,"+cuisine+" TEXT NOT NULL,"
                +preparation_time+  " TEXT NOT NULL,"+COLUMN_IMAGE_URL+" TEXT NOT NULL"  + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_FAV_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        onCreate(db);

    }


    public boolean addProduct(CupPojo cupPojo) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, cupPojo.getIngredientName());
        values.put(COLUMN_MEASUREMENT, cupPojo.getIngredienMeasurement());
        values.put(COLUMN_IMAGE_URL, cupPojo.getImageUrl());
        values.put(COLUMN_QUANTITY, cupPojo.getIngredientQty());


        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return id > 0;
    }



    public CupPojo findIngredients(String productname) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        CupPojo product = new CupPojo();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setId(Integer.parseInt(cursor.getString(0)));
            product.setIngredientName(cursor.getString(1));
            product.setIngredientQty(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }

    public boolean isIngredients(String productname) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";
Boolean hiii;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        CupPojo product = new CupPojo();

        if (cursor.moveToFirst()) {

            cursor.moveToFirst();
//            product.setId(Integer.parseInt(cursor.getString(0)));
//            product.setIngredientName(cursor.getString(1));
//            product.setIngredientQty(Integer.parseInt(cursor.getString(2)));
            cursor.close();
            hiii =true;
        } else {
            product = null;
            hiii=false;
        }
        db.close();
        return hiii;
    }


    public boolean deleteProduct(String productname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        CupPojo product = new CupPojo();

        if (cursor.moveToFirst()) {
            product.setId(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(product.getId()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public boolean updateQty(CupPojo cupPojo) {

        // get current Unix epoc time in milliseconds // helper is MyDatabaseHelper, a subclass database control class in which this updateTime method is resides
        ContentValues values = new ContentValues();
//        values.put(COLUMN_PRODUCTNAME, cupPojo.getIngredientName());
        values.put(COLUMN_MEASUREMENT, cupPojo.getIngredienMeasurement());
        values.put(COLUMN_IMAGE_URL, cupPojo.getImageUrl());
        values.put(COLUMN_QUANTITY, cupPojo.getIngredientQty());

        String selection =COLUMN_PRODUCTNAME+"=?"; // where ID column = rowId (that is, selectionArgs)
        String[] selectionArgs = { cupPojo.getIngredientName() };

        SQLiteDatabase db = this.getWritableDatabase();

//        sqliteDatabase.update(AndroidOpenDbHelper.TABLE_NAME_GPA,
//                contentValues, AndroidOpenDbHelper.COLUMN_NAME_UNDERGRADUATE_NAME+"=?", whereClauseArgument);

        long id = db.update(TABLE_PRODUCTS, values,selection ,
                selectionArgs);
        db.close();
        return id > 0;
    }

    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_PRODUCTS, null, null);
    }


    public Cursor getAllCup() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_PRODUCTS, new String[] { COLUMN_ID, COLUMN_PRODUCTNAME,
                COLUMN_MEASUREMENT,COLUMN_IMAGE_URL,COLUMN_QUANTITY }, null, null, null, null, null);
    }


    public boolean addtoFav(RecipesPojo rpojo) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID, rpojo.getId());
        values.put(name, rpojo.getName());
        values.put(veg, rpojo.getVeg());
        values.put(serves, rpojo.getServes());
        values.put(food_kind, rpojo.getFood_kind());
        values.put(cuisine, rpojo.getCuisine());
        values.put(preparation_time, rpojo.getPreparation_time());
        values.put(COLUMN_IMAGE_URL, rpojo.getMedia_url());


        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(TABLE_FAV, null, values);
        db.close();
        return id > 0;
    }

    public boolean deletefromFav(String id) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_FAV + " WHERE " + COLUMN_RECIPE_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        if (cursor.moveToFirst()) {
            db.delete(TABLE_FAV, COLUMN_RECIPE_ID + " = ?",
                    new String[] { String.valueOf(id) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }


    public Cursor getAllFav() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_FAV, new String[] { COLUMN_RECIPE_ID, name,
                veg,serves,food_kind,cuisine,preparation_time,COLUMN_IMAGE_URL }, null, null, null, null, null);
    }


    public boolean isFav(String id) {
        String query = "Select * FROM " + TABLE_FAV + " WHERE " + COLUMN_RECIPE_ID + " =  \"" + id + "\"";
        Boolean hiii;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        RecipesPojo recipesPojo = new RecipesPojo();

        if (cursor.moveToFirst()) {

            cursor.moveToFirst();
//            product.setId(Integer.parseInt(cursor.getString(0)));
//            product.setIngredientName(cursor.getString(1));
//            product.setIngredientQty(Integer.parseInt(cursor.getString(2)));
            cursor.close();
            hiii =true;
        } else {
            recipesPojo = null;
            hiii=false;
        }
        db.close();
        return hiii;
    }
}
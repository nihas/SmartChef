package com.nihas.smart.chef.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nihas.smart.chef.pojos.CupPojo;

/**
 * Created by snyxius on 10/26/2015.
 */
public class MyDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ingredientsDB.db";
    public static final String TABLE_PRODUCTS = "ingredients";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "ingredient_name";
    public static final String COLUMN_QUANTITY = "ingredient_quantity";
    public static final String COLUMN_MEASUREMENT = "ingredient_measurement";
    public static final String COLUMN_IMAGE_URL = "image_url";

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
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " integer primary key autoincrement," + COLUMN_PRODUCTNAME
                + " TEXT NOT NULL," + COLUMN_MEASUREMENT + " TEXT,"+COLUMN_IMAGE_URL + " TEXT NOT NULL,"+ COLUMN_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
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


    public Cursor getAllCup() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_PRODUCTS, new String[] { COLUMN_ID, COLUMN_PRODUCTNAME,
                COLUMN_MEASUREMENT,COLUMN_IMAGE_URL,COLUMN_QUANTITY }, null, null, null, null, null);
    }


}
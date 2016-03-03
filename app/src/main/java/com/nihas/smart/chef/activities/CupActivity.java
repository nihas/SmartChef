package com.nihas.smart.chef.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.CupAdapter;
import com.nihas.smart.chef.adapters.IngredientsAdapter;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.fragments.RecipeFragment;
import com.nihas.smart.chef.pojos.CupPojo;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by snyxius on 10/23/2015.
 */
public class CupActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    ArrayList<CupPojo> listIngredients;
    CupAdapter cupAdapter;
    static LinearLayout emptyView;
    static Button cookButton,addIng;
    ProgressBar progressBar;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cup);
initialise();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("CUP");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    public ArrayList<CupPojo> getIngredients(){
        listIngredients=new ArrayList<>();
        MyDbHandler dbHandler = new MyDbHandler(CupActivity.this, null, null, 1);
        Cursor c=dbHandler.getAllCup();
//        Toast.makeText(this, String.valueOf(c.getCount()), Toast.LENGTH_LONG).show();
        CupPojo pojo= new CupPojo();
        pojo.setCup_count(c.getCount());
        if (c.moveToFirst()) {
            do {
//                DisplayContact(c);
                listIngredients.add(new CupPojo(Integer.parseInt(c.getString(0)),c.getString(1),c.getString(3)));
            } while (c.moveToNext());
        }
//        String[] array=getResources().getStringArray(R.array.fruits);
//        listIngredients=new ArrayList<>(Arrays.asList(array));
//        listIngredients.add("Banana");
//        listIngredients.add("Apple");
//        listIngredients.add("Onion");
//        listIngredients.add("Ginger");
//        listIngredients.add("Garlic");
//        listIngredients.add("Tamarind");
//        listIngredients.add("Cucumber");
//        listIngredients.add("Tomato");
//        listIngredients.add("Chilly");
//        listIngredients.add("Chicken");
//        listIngredients.add("Brinjal");
//        listIngredients.add("Elephant Yam");
        return listIngredients;


    }




    private void initialise() {
        try {
//            progressBar=(ProgressBar)rootView.findViewById(R.id.pBar);
//            progressBar.setVisibility(View.GONE);

            cookButton=(Button)findViewById(R.id.cook_button);
            addIng=(Button)findViewById(R.id.add_ing);
            addIng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            emptyView=(LinearLayout)findViewById(R.id.empty_view);
            mRecyclerView=(RecyclerView)findViewById(R.id.rv);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            cupAdapter=new CupAdapter(CupActivity.this,getIngredients());
            mRecyclerView.setAdapter(cupAdapter);

            if (getIngredients().isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                cookButton.setVisibility(View.GONE);
            }
            else {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                cookButton.setVisibility(View.VISIBLE);
            }


            cookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cook_ing="";
                    MyDbHandler dbHandler = new MyDbHandler(CupActivity.this, null, null, 1);
                    Cursor c=dbHandler.getAllCup();
//        Toast.makeText(this, String.valueOf(c.getCount()), Toast.LENGTH_LONG).show();
                    CupPojo pojo= new CupPojo();
                    pojo.setCup_count(c.getCount());
                    if (c.moveToFirst()) {
                        do {
//                DisplayContact(c);
                            cook_ing=cook_ing+c.getString(1)+",";
                        } while (c.moveToNext());
                    }
//                    SmartChefApp.showAToast(cook_ing);
//                    dbHandler.removeAll();
//                    pojo.setCup_count(0);
//                    MainActivity.updateCupValue(pojo.getCup_count());
                    Intent intent=new Intent(CupActivity.this,RecipeActivity.class);
                    intent.putExtra("ingredients", cook_ing);
//                    setResult(2,intent);
                    startActivity(intent);
//                    finish();//finishing activity
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new RecipeFragment()).addToBackStack(null).commit();
//                    finish();

//                    Intent newInten = new Intent(getActivity(), RecipeActivity.class);
//                    startActivity(newInten);
//                    getActivity().finish();
                }
            });


        }catch (Exception e){

        }

    }

    @Override
    public void onBackPressed() {
//        Intent intent=new Intent(CupActivity.this,MainActivity.class);
//        intent.putExtra("MESSAGE","msg");
//        setResult(1, intent);
//        startActivity(intent);
        finish();

    }

    public static void updateView(){
        emptyView.setVisibility(View.VISIBLE);
        cookButton.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
            builder.setTitle("Delete");
            builder.setMessage("Are you sure want to clear cup?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MyDbHandler dbHandler = new MyDbHandler(CupActivity.this, null, null, 1);
                    dbHandler.removeAll();
                    dialogInterface.dismiss();
                    Intent inten=new Intent(CupActivity.this,CupActivity.class);
                    startActivity(inten);
                    finish();
                    MainActivity.updateCupValue(0);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();



        }
        return super.onOptionsItemSelected(item);
    }
}



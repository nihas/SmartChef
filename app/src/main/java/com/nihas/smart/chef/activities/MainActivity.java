package com.nihas.smart.chef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.CategoryAdapter;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ArrayList<AllPojo> listCuisines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        CategoryAdapter mAdapter = new CategoryAdapter(getApplicationContext(),getCuisines());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, IngredientsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_cup);
        MenuItemCompat.setActionView(item, R.layout.cup_icon);
        View view = MenuItemCompat.getActionView(item);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //do stuff here
                Intent intent = new Intent(MainActivity.this, CupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cup) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<AllPojo> getCuisines(){
        listCuisines=new ArrayList<>();
        listCuisines.add(new AllPojo("FRUITS","http://salemcart.com/media/shopshark/blog/BestFruits_article.jpg","2 Ingredients"));
        listCuisines.add(new AllPojo("VEGETABLES","http://cms.bbcomcdn.com/fun/images/2007/sclark78b.jpg","5 Ingredients"));
        listCuisines.add(new AllPojo("SPICES/MASALA","http://www.stemkart.com/media/resized/l/blog/blog-3.jpg","5 Ingredients"));
        listCuisines.add(new AllPojo("GRAINS","http://www.in.all.biz/img/in/catalog/404097.jpeg","5 Ingredients"));
        listCuisines.add(new AllPojo("LENTILS - PULSES","http://matirkatha.gov.in/wp-content/uploads/2013/11/pulses.jpg","5 Ingredients"));
        listCuisines.add(new AllPojo("SEEDS","http://www.fitho.in/wp-content/uploads/2011/02/Nuts-and-Seeds.jpg","5 Ingredients"));
//        listCuisines.add(new AllPojo("CHINESE","https://lh3.googleusercontent.com/-Tt9oNXXrAzo/ViYJSNptqLI/AAAAAAAAAoU/P3rVzaRPIPM/s240-Ic42/italy.png","5 Recipes"));
//        listCuisines.add(new AllPojo("PIZZA","https://lh3.googleusercontent.com/-Tt9oNXXrAzo/ViYJSNptqLI/AAAAAAAAAoU/P3rVzaRPIPM/s240-Ic42/italy.png","5 Recipes"));
//        listCuisines.add(new AllPojo("SEA FOOD","https://lh3.googleusercontent.com/-Tt9oNXXrAzo/ViYJSNptqLI/AAAAAAAAAoU/P3rVzaRPIPM/s240-Ic42/italy.png","5 Recipes"));
//        listCuisines.add(new AllPojo("AMERICAN","https://lh3.googleusercontent.com/-Tt9oNXXrAzo/ViYJSNptqLI/AAAAAAAAAoU/P3rVzaRPIPM/s240-Ic42/italy.png","5 Recipes"));
        return listCuisines;


    }
}

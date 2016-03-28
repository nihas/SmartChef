package com.nihas.smart.chef.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.adapters.CupAdapter;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.customui.CircleImageView;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by snyxius on 10/23/2015.
 */
public class ProfileActivity extends AppCompatActivity {
    TextView userFullName,userFirstName,userLastName,userEmail,email;
    Toolbar toolbar;
    CircleImageView proPic;
    ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
initialise();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        userFullName=(TextView)findViewById(R.id.user_full_name);
        userFirstName=(TextView)findViewById(R.id.user_first_name);
        userLastName=(TextView)findViewById(R.id.user_last_name);
        userEmail=(TextView)findViewById(R.id.user_email);
        email=(TextView)findViewById(R.id.email);

        proPic=(CircleImageView)findViewById(R.id.user_pro_pic);

        imageLoader = ImageLoader.getInstance();
        userFullName.setText(SmartChefApp.readFromPreferences(getApplicationContext(), "user_name", ""));
        userFirstName.setText(SmartChefApp.readFromPreferences(getApplicationContext(), "user_name", ""));
        userEmail.setText(SmartChefApp.readFromPreferences(getApplicationContext(),"email",""));
        email.setText(SmartChefApp.readFromPreferences(getApplicationContext(),"email",""));
        imageLoader.displayImage(SmartChefApp.readFromPreferences(getApplicationContext(),"profile_pic",""),proPic);



        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Profile");
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






    private void initialise() {


    }

    @Override
    public void onBackPressed() {

        finish();

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
                    MyDbHandler dbHandler = new MyDbHandler(ProfileActivity.this, null, null, 1);
                    dbHandler.removeAll();
                    dialogInterface.dismiss();
                    Intent inten=new Intent(ProfileActivity.this,ProfileActivity.class);
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



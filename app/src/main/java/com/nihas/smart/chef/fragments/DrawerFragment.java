package com.nihas.smart.chef.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nihas.smart.chef.Keys;
import com.nihas.smart.chef.R;
import com.nihas.smart.chef.activities.CookBook;
import com.nihas.smart.chef.activities.LoginActivity;
import com.nihas.smart.chef.activities.RecipeActivity;
import com.nihas.smart.chef.adapters.CategoryAdapter;
import com.nihas.smart.chef.adapters.CupAdapter;
import com.nihas.smart.chef.api.WebRequest;
import com.nihas.smart.chef.api.WebServices;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.customui.CircleImageView;
import com.nihas.smart.chef.db.MyDbHandler;
import com.nihas.smart.chef.pojos.AllPojo;
import com.nihas.smart.chef.pojos.CupPojo;
import com.nihas.smart.chef.utils.RecyclerItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Nihas on 29-11-2015.
 */
public class DrawerFragment extends Fragment implements View.OnClickListener {


    TextView logOut,userName,eMail,myCookBook;
    CircleImageView proPic;
    ImageLoader imageLoader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.drawer_header, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logOut=(TextView)view.findViewById(R.id.log_out);
        logOut.setOnClickListener(this);
        myCookBook=(TextView)view.findViewById(R.id.my_cook_book);
        myCookBook.setOnClickListener(this);
        proPic=(CircleImageView)view.findViewById(R.id.profile);
        userName=(TextView)view.findViewById(R.id.username);
        eMail=(TextView)view.findViewById(R.id.email);
        imageLoader = ImageLoader.getInstance();
        userName.setText(SmartChefApp.readFromPreferences(getContext(), "user_name", ""));
        eMail.setText(SmartChefApp.readFromPreferences(getContext(),"email",""));
        imageLoader.displayImage(SmartChefApp.readFromPreferences(getContext(),"profile_pic",""),proPic);
    }


    public static  DrawerFragment newInstance(DrawerLayout drawer) {

        DrawerFragment f = new DrawerFragment();
        return f;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_out:
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_Alert);
                builder.setTitle("LOG OUT");
                builder.setMessage("Are you sure want to Log Out?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SmartChefApp.saveToPreferences(getActivity(), "isGplusLogin", false);
                        LoginActivity.signOutFromGplus();

                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
                        SmartChefApp.clearSharedPrefData(getActivity());
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();


                break;
            case R.id.my_cook_book:
                Intent cookIntent=new Intent(getActivity(), CookBook.class);
                startActivity(cookIntent);
                break;
            default:
                SmartChefApp.showAToast("Under Construction");
        }

    }
}
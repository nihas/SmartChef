package com.nihas.smart.chef.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.nihas.smart.chef.R;
import com.nihas.smart.chef.app.SmartChefApp;
import com.nihas.smart.chef.pojos.RecipesPojo;
import com.nihas.smart.chef.utils.PicassoImageLoader;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;
import com.veinhorn.scrollgalleryview.loader.DefaultVideoLoader;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by snyxius on 3/3/16.
 */
public class ActivityGallery extends AppCompatActivity {


    Toolbar toolbar;

    private static final ArrayList<String> images = new ArrayList<>(Arrays.asList(
            "http://img1.goodfon.ru/original/1920x1080/d/f5/aircraft-jet-su-47-berkut.jpg",
            "http://www.dishmodels.ru/picture/glr/13/13312/g13312_7657277.jpg",
            "http://img2.goodfon.ru/original/1920x1080/b/c9/su-47-berkut-c-37-firkin.jpg",
            "http://www.avsimrus.com/file_images/15/img4951_1.jpg",
            "http://www.avsimrus.com/file_images/15/img4951_3.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/0/07/Sukhoi_Su-47_in_2008.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/b/b4/Sukhoi_Su-47_Berkut_%28S-37%29_in_2001.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37333-4.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-0.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-6.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-9.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-2.jpg",
            "http://testpilot.ru/russia/sukhoi/s/37/images/s37-1.jpg"
    ));
    private ScrollGalleryView scrollGalleryView;
    JSONArray attachmentarr= null;
    List<MediaInfo> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Gallery");
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

        Bitmap bitmap = convertDrawableToBitmap(R.drawable.wallpaper7);


        try {
            attachmentarr = new JSONArray(SmartChefApp.readFromPreferences(getApplicationContext(), "ATTACHMENTS", ""));
            infos = new ArrayList<>(attachmentarr.length());
        for(int j=0;j<attachmentarr.length();j++) {
            JSONObject jobj2=attachmentarr.getJSONObject(j);
            RecipesPojo pojo=new RecipesPojo();
            if((!jobj2.isNull("media_type"))&&(!jobj2.isNull("media_url"))){

                if(jobj2.getString("media_type").equals("video")){
//                    pojo.setMedia_url("http://collegemix.ca/img/placeholder.png");
                    infos.add(MediaInfo.mediaLoader(new PicassoImageLoader("http://collegemix.ca/img/placeholder.png")));
//                                scrollGalleryView.addMedia(MediaInfo.mediaLoader(
//                                        new DefaultVideoLoader(jobj2.getString("media_url"), R.drawable.default_video)));
//                                imageLoader.displayImage("http://collegemix.ca/img/placeholder.png", thumb, options);
                }else{
//                    pojo.setMedia_url(jobj2.getString("media_url"));
                    infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(jobj2.getString("media_url"))));
//                                for (String url : images) {
//                                    infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(jobj2.getString("media_url"))));
//                                scrollGalleryView.addMedia(infos);
//                                }
//                                imageLoader.displayImage(jobj2.getString("media_url"), thumb, options);
                }
            }
//            attachArray.add(pojo);
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }




        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
//                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.wallpaper1)))
//                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(bitmap)))
//                .addMedia(MediaInfo.mediaLoader(new MediaLoader() {
//                    @Override
//                    public boolean isImage() {
//                        return true;
//                    }
//
//                    @Override
//                    public void loadMedia(Context context, ImageView imageView, MediaLoader.SuccessCallback callback) {
//                        Bitmap bitmap = convertDrawableToBitmap(R.drawable.wallpaper3);
//                        imageView.setImageBitmap(bitmap);
//                        callback.onSuccess();
//                    }
//
//                    @Override
//                    public void loadThumbnail(Context context, ImageView thumbnailView, MediaLoader.SuccessCallback callback) {
//                        Bitmap bitmap = convertDrawableToBitmap(R.drawable.wallpaper3);
//                        thumbnailView.setImageBitmap(bitmap);
//                        callback.onSuccess();
//                    }
//                }))
//                .addMedia(MediaInfo.mediaLoader(
//                        new DefaultVideoLoader("http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4", R.drawable.default_video)))
                .addMedia(infos);
    }

    private Bitmap convertDrawableToBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }
}

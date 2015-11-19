package com.nihas.smart.chef.pojos;

/**
 * Created by snyxius on 10/14/2015.
 */
public class AllPojo {

    private String title;
    private String subTitle;
    private String url;
    int img_drawable;

    public AllPojo(String titl, String urll, String subTitl,int ImgDraw){
        this.title=titl;
        this.url=urll;
        this.subTitle=subTitl;
        this.img_drawable=ImgDraw;
    }

    public AllPojo(String titl, String urll){
        this.title=titl;
        this.url=urll;
    }

    public AllPojo(){
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getImg_drawable() {
        return img_drawable;
    }
}

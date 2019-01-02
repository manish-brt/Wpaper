package com.example.manish.wpaper.model;

/**
 * Created by Manish on 06-Oct-17.
 */

public class PostModel {
    private String title, desc,image, location, postedBy;

    public PostModel(){

    }

    public PostModel(String title, String desc, String image, String location) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.location = location;
    }

    public PostModel(String title, String desc, String image, String location, String postedBy) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.location = location;
        this.postedBy = postedBy;
    }

    public String getLocation() {return location;    }

    public void setLocation(String location) {this.location = location; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
}

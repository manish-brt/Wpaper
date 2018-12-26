package com.example.manish.wpaper;

/**
 * Created by Manish on 06-Oct-17.
 */

public class ModelClass {
    private String title, desc,image, location;

    public ModelClass(){

    }
    public ModelClass(String title, String desc, String image, String location) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.location = location;
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
}

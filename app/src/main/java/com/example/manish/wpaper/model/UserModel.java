package com.example.manish.wpaper.model;

/**********************************
 * Created by Manish on 02-Jan-19
 ***********************************/
public class UserModel {

    public String userName;
    public String email;
    public String mobile;

    public UserModel(){}


    public UserModel(String userName, String userEmail, String userMobile) {
        this.userName = userName;
        this.email = userEmail;
        this.mobile = userMobile;
    }
}

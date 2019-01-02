package com.example.manish.wpaper.view;

/**********************************
 * Created by Manish on 02-Jan-19
 ***********************************/
public interface BaseView {

    void showLoading();

    void hideLoading();

    void showSnackBar(String message);

    void showToastMessage(String message);

    void setSupportToolbar();

    void setToolbarLeftIconClickListener();
}

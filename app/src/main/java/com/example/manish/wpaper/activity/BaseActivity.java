package com.example.manish.wpaper.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.manish.wpaper.R;
import com.example.manish.wpaper.utility.CommonUtils;
import com.example.manish.wpaper.view.BaseView;

/**********************************
 * Created by Manish on 02-Jan-19
 ***********************************/
public class BaseActivity extends AppCompatActivity implements BaseView {
    private final static String TAG = "BaseAct";

    Toolbar toolbar;
    Dialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void showLoading() {
        if(isFinishing()) return;
        progressDialog = CommonUtils.showLoadingBar(this);

    }

    @Override
    public void hideLoading() {
        CommonUtils.hideLoadingBar(progressDialog);
    }

    @Override
    public void showSnackBar(String message) {
        if(null != message){
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    message, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    @Override
    public void showToastMessage(String message) {
        if(message != null){
            Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setSupportToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (null != toolbar) {
            setSupportActionBar(toolbar);
        }

    }

    @Override
    public void setToolbarLeftIconClickListener() {
        LinearLayout navLL = findViewById(R.id.navigation_ll);
//        LinearLayout backLL = findViewById(R.id.toolbar_ll);
//        if (null != backLL) {
//            backLL.setVisibility(View.GONE);
//        }
        if (null != navLL) {
            navLL.setVisibility(View.VISIBLE);
            navLL.setOnClickListener(onToolbarLeftLayerClickListener);
        }
    }

    View.OnClickListener onToolbarLeftLayerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.navigation_ll:
                    showToastMessage("Feature In Progress..");
                    break;
            }
        }
    };

}

package com.example.manish.wpaper;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class BlogSingleActivity extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mdb;

    private ImageView mBlogSingleImage;
    String post_image;

    private GyroscopeObserver gyroscopeObserver;
    WallpaperManager wallpaperManager;


    private String Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);

        Button setWall = (Button) findViewById(R.id.setWpaper);
        Button remove_btn  = (Button) findViewById(R.id.remove);


        mdb = FirebaseDatabase.getInstance().getReference().child("Blog");

        mPost_key = getIntent().getExtras().getString("blog_id");

        //mBlogSingleImage = (ImageView) findViewById(R.id.blogsingleimageview);
        gyroscopeObserver = new GyroscopeObserver();
        gyroscopeObserver.setMaxRotateRadian(Math.PI/9);

        final PanoramaImageView panoramaImageView = (PanoramaImageView) findViewById(R.id.blogsingleimageview);
        // Set GyroscopeObserver for PanoramaImageView.


        mdb.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                post_image = (String) dataSnapshot.child("image").getValue();
                Log.d(Tag, "Value is: " + post_image);

                Picasso.with(BlogSingleActivity.this).load(post_image).into(panoramaImageView);
                panoramaImageView.setGyroscopeObserver(gyroscopeObserver);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        remove_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdb.child(mPost_key).removeValue();
                Toast.makeText(getApplicationContext(), "Post Removed",
                        Toast.LENGTH_SHORT).show();
                Intent main_intent = new Intent(BlogSingleActivity.this,MainActivity.class);
                main_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main_intent);

            }
        });

        setWall.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v1) {

                Toast.makeText(getApplicationContext(), " Wallpaper Changed",
                        Toast.LENGTH_SHORT).show();


                wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                Picasso.with(getApplicationContext()).load(post_image).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        //Log.i(TAG, "The image was obtained correctly, now you can do your canvas operation!");
                        try {
                            wallpaperManager.setBitmap(bitmap);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        //Log.e(TAG, "The image was not obtained");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        //Log.(TAG, "Getting ready to get the image");
                        //Here you should place a loading gif in the ImageView to
                        //while image is being obtained.
                    }
                });


            }
        });


        //new DownloadImage().execute(post_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register GyroscopeObserver.
        gyroscopeObserver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister GyroscopeObserver.
        gyroscopeObserver.unregister();
    }

    public static Bitmap viewToBitmap(View view,int width,int height){

        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;

    }


}

package com.example.manish.wpaper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.manish.wpaper.R;
import com.example.manish.wpaper.model.PostModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UpdatePostActivity extends BaseActivity {

    private EditText postedBy, location, postTitle, postDesc;
    private TextView updatePostTV;

    private String imagePath;

    private String mPost_key = null;
    private DatabaseReference mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        postedBy = findViewById(R.id.posted_by_et);
        location = findViewById(R.id.location_et);
        postTitle = findViewById(R.id.post_title_et);
        postDesc = findViewById(R.id.post_desc_et);
        updatePostTV = findViewById(R.id.update_post_tv);

        mdb = FirebaseDatabase.getInstance().getReference().child("Blog");

        mPost_key = getIntent().getExtras().getString("blog_id");

        mdb.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PostModel postModel = dataSnapshot.getValue(PostModel.class);

                if (postModel != null) {
                    postedBy.setText(postModel.getPostedBy());
                    location.setText(postModel.getLocation());
                    postTitle.setText(postModel.getTitle());
                    postDesc.setText(postModel.getDesc());
                    imagePath = postModel.getImage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        updatePostTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePost();
            }
        });

    }

    private void updatePost() {

        PostModel postModel = new PostModel();
        postModel.setPostedBy(postedBy.getText().toString());
        postModel.setLocation(location.getText().toString());
        postModel.setTitle(postTitle.getText().toString());
        postModel.setDesc(postDesc.getText().toString());
        postModel.setImage(imagePath);

        mdb.child(mPost_key).setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showSnackBar("Updated");
                finish();
            }
        });
    }
}

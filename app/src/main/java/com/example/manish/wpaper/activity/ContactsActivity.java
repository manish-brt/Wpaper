package com.example.manish.wpaper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.manish.wpaper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ContactsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView contact_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mAuth = FirebaseAuth.getInstance();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        //mLayoutManager.setStackFromEnd(true);

        contact_recyclerView = (RecyclerView)findViewById(R.id.contact_list);
        contact_recyclerView.setHasFixedSize(true);
        contact_recyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

        }
    }
}

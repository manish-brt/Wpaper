package com.example.manish.wpaper.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.manish.wpaper.R;
import com.example.manish.wpaper.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private EditText userName;
    private EditText userEmail;
    private EditText userMobile;

    private TextView updateProfile;

    private FirebaseDatabase mydb;
    private DatabaseReference myDbRef;

    private View mView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = mView.findViewById(R.id.userName_et);
        userEmail = mView.findViewById(R.id.email_et);
        userMobile = mView.findViewById(R.id.mobile_et);
        updateProfile = mView.findViewById(R.id.update_tv);

        mydb = FirebaseDatabase.getInstance();
        myDbRef = mydb.getReference().child("Users");

        getProfileDate();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });


        return mView;
    }

    private void updateProfile() {

//        getA.setMessage("Uploading....");

        String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        String mobile = userMobile.getText().toString();

        UserModel user = new UserModel(name, email, mobile);

        myDbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

    }

    private void getProfileDate(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                userName.setText(user.userName);
                userEmail.setText(user.email);
                userMobile.setText(user.mobile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        };
        myDbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(listener);

    }

}

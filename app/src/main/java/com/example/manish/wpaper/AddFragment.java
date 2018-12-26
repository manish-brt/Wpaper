package com.example.manish.wpaper;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    private ImageButton mSelectImg;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mUpload;

    private Uri mImageUri ;
    private static final int GALLERY_REQUEST = 100;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    private FusedLocationProviderClient mFusedLocationClient;
    int REQUEST_LOCATION = 121;

    private View mFragView;
    private Uri Cropped_ImageURI;

    GPSTracker gps;
    List<Address> addresses;
    Address address;
    //String string_location;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragView = inflater.inflate(R.layout.fragment_add, container, false);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mSelectImg = (ImageButton) mFragView.findViewById(R.id.img_select);
        mPostTitle = (EditText) mFragView.findViewById(R.id.title_field);
        mPostDesc = (EditText) mFragView.findViewById(R.id.decs_field);
        mUpload = (Button) mFragView.findViewById(R.id.upload_button);

        mProgress = new ProgressDialog(getContext());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        gps = new GPSTracker(getActivity());

        mSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               CropImage.activity().start(getContext(),AddFragment.this);

               /* Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Pick A Picture"),GALLERY_REQUEST);*/
            }
        });

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                }else {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if(location != null){
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                try {

                                    Geocoder gCoder = new Geocoder(getContext());
                                    addresses = gCoder.getFromLocation(latitude, longitude, 1);

                                }catch(IOException e) {
                                    // Catch network or other I/O problems.
                                }catch (IllegalArgumentException ee){
                                    // Catch invalid latitude or longitude values.
                                }
                                if (addresses != null && addresses.size() > 0) {

                                    address  = addresses.get(0);
                                    ArrayList<String> addressList = new ArrayList<String>();
                                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                                        addressList.add(address.getAddressLine(i));
                                    }

                                    final String string_location = addresses.get(0).getLocality();
                                    startPosting(string_location);

                                    //Toast.makeText(getContext(),address.getLocality(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }

            }
        });

        return mFragView;
    }

    private void startPosting(String loc){

        //Uri uploadUri = Uri.fromFile(new File(fileUri.toString()));
        mProgress.setMessage("Uploading....");


        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();
        final String loc_val = loc;

        if((!TextUtils.isEmpty(title_val)) && (!TextUtils.isEmpty(desc_val)) && (Cropped_ImageURI != null)){

            StorageReference filepath = mStorage.child("UploadedImages").child(Cropped_ImageURI.getLastPathSegment());
            mProgress.show();
            filepath.putFile(Cropped_ImageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            @SuppressWarnings("VisibleForTests")Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            DatabaseReference newPost = mDatabase.push();
                            newPost.child("title").setValue(title_val);
                            newPost.child("desc").setValue(desc_val);
                            newPost.child("location").setValue(loc_val);
                            newPost.child("image").setValue(downloadUrl.toString());

                            Toast.makeText(getContext(), " Uploaded "+loc_val,
                                    Toast.LENGTH_SHORT).show();

                            mProgress.dismiss();

                            Intent main_intent = new Intent(getContext(),MainActivity.class);
                            main_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(main_intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads

                            mProgress.dismiss();
                            Toast.makeText(getContext(), "Error: upload failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Cropped_ImageURI = result.getUri();
                mSelectImg.setImageURI(Cropped_ImageURI);

            }

        }

       /* switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(),"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                    mImageUri = data.getData();
                    mSelectImg.setImageURI(mImageUri);


                }
        }*/
    }


}

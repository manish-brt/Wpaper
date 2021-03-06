package com.example.manish.wpaper.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.manish.wpaper.activity.UpdatePostActivity;
import com.example.manish.wpaper.model.PostModel;
import com.example.manish.wpaper.R;
import com.example.manish.wpaper.activity.BlogSingleActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView mBlogList;
    private FirebaseDatabase mydb;
    private DatabaseReference myRef;

    private View mView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        //Recycler View
        mBlogList = (RecyclerView) mView.findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(mLayoutManager);

        //Sending a Query to the Database
        mydb = FirebaseDatabase.getInstance();
        myRef = mydb.getReference().child("Blog");

        return mView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<PostModel, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<PostModel, BlogViewHolder>(
                        PostModel.class,
                        R.layout.design_row,
                        BlogViewHolder.class,
                        myRef) {

                    @Override
                    protected void populateViewHolder(final BlogViewHolder viewHolder, final PostModel model, int pos) {

                        final String post_key = getRef(pos).getKey();

                        viewHolder.setTile(model.getTitle());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setImage(model.getImage());
                        viewHolder.setUserNAme(model.getPostedBy());
                        try {
                            viewHolder.setLocation(model.getLocation());
                        }catch (NullPointerException e){
                        }

                        viewHolder.more_vert.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopupMenu mpopupMenu = new PopupMenu(getContext(),viewHolder.more_vert);
                                mpopupMenu.getMenuInflater().inflate(R.menu.post_more_menu,mpopupMenu.getMenu());

                                mpopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {

                                        switch (item.getItemId()) {
                                            case R.id.update_post:
                                                Intent updatePost = new Intent(getContext(),UpdatePostActivity.class);
                                                updatePost.putExtra("blog_id",post_key);
                                                startActivity(updatePost);
                                                return true;
                                            case R.id.view_post:

                                                Intent singleBlogIntent = new Intent(getContext(),BlogSingleActivity.class);
                                                singleBlogIntent.putExtra("blog_id",post_key);
                                                startActivity(singleBlogIntent);
                                                //Toast.makeText(getContext(),model.getTitle().toString(),Toast.LENGTH_LONG).show();
                                                return true;

                                            case R.id.delete_post:

                                                myRef.child(post_key).removeValue();
                                                return true;

                                            case R.id.share_post:

                                                Intent share_intent = new Intent(Intent.ACTION_SEND);
                                                share_intent.setType("text/plain");

                                                share_intent.putExtra(Intent.EXTRA_SUBJECT, model.getTitle());
                                                share_intent.putExtra(Intent.EXTRA_TEXT, model.getImage());
                                                startActivity(Intent.createChooser(share_intent, "Share Image"));                                            default:
                                                return true;

                                        }
                                    }
                                });
                                mpopupMenu.show();
                            }
                        });
                        viewHolder.post_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent singleBlogIntent = new Intent(getContext(),BlogSingleActivity.class);
                                singleBlogIntent.putExtra("blog_id",post_key);
                                startActivity(singleBlogIntent);

                            }
                        });


                    }
                };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }


    //View Holder For Recycler View
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        ImageButton more_vert;
        ImageView post_img;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            /*itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.androidsquad.space/"));

                    Intent browserChooserIntent = Intent.createChooser(browserIntent, "Choose Browser of ur Choice");
                    v.getContext().startActivity(browserChooserIntent);
                }
            });*/
            more_vert = (ImageButton)mView.findViewById(R.id.more_vert);
            post_img =  mView.findViewById(R.id.post_img_fresco);

        }

        public void setTile(String tile) {
            TextView post_title = (TextView) mView.findViewById(R.id.Text_title);
            post_title.setText(tile);
        }

        public void setDesc(String desc) {
            TextView post_title = (TextView) mView.findViewById(R.id.Text_desc);
            post_title.setText(desc);
        }

        public void setUserNAme(String name){
            TextView user_name = mView.findViewById(R.id.post_username);
            user_name.setText(name);
        }
        public void setLocation(String loc){
            TextView post_loc = (TextView) mView.findViewById(R.id.post_location);
            post_loc.setText(loc);
        }
        public void setImage(String image) {
            Glide.with(mView).load(image).apply(new RequestOptions()
                    .placeholder(R.drawable.ic_add_circle_white_24dp)
                    .centerCrop()).into(post_img);
        }
    }

}

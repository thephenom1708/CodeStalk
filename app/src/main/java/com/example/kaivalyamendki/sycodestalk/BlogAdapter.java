package com.example.kaivalyamendki.sycodestalk;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Kaivalya Mendki on 05-04-2018.
 */

public class BlogAdapter extends RecyclerView.Adapter <BlogAdapter.BlogViewHolder> {

    public List<Blog> blogList;

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView blogTitle, blogDescription, blogUsername, blogLikes;
        public ImageView blogImage;
        public Context ctx;
        public CardView blogCard;
        public ImageButton likeButton;

        public BlogViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            mView = itemView;
            blogCard = (CardView) mView.findViewById(R.id.blog_cardView);
            blogTitle = (TextView) mView.findViewById(R.id.blogTitle);
            blogDescription = (TextView) mView.findViewById(R.id.blogDescription);
            blogUsername = (TextView) mView.findViewById(R.id.blogUsername);
            blogLikes = (TextView) mView.findViewById(R.id.blogLikes);
            blogImage = (ImageView) mView.findViewById(R.id.blogImage);
            likeButton = (ImageButton)mView.findViewById(R.id.likeButton);
        }

        public void setDetails(String title, String desc, String username, String likes, String image) {

            blogTitle.setText(title);
            blogDescription.setText(desc);
            blogUsername.setText(username);
            blogLikes.setText(likes);
            Glide.with(ctx).load(image).into(blogImage);
        }
    }

    public BlogAdapter(List<Blog> blogList) {
        this.blogList = blogList;
    }

    @Override
    public BlogAdapter.BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_view, parent, false);

        final BlogAdapter.BlogViewHolder blogHolder = new BlogAdapter.BlogViewHolder(itemView, parent.getContext());

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference forumDatabase = FirebaseDatabase.getInstance().getReference("TechForum/");

       blogHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String blogId = blogList.get(blogHolder.getAdapterPosition()).getBlogId();
                forumDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot walker : dataSnapshot.getChildren()){
                            if(walker.getKey().equals(blogId))
                            {
                                Blog blog = walker.getValue(Blog.class);
                                if(!blog.likeArray.contains(user.getUid())){
                                    blog.likeArray.add(user.getUid());
                                    blog.setLikes(String.valueOf(blog.likeArray.size()));
                                    forumDatabase.child(blogId).setValue(blog);
                                }
                                forumDatabase.removeEventListener(this);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        return blogHolder;
    }

    @Override
    public void onBindViewHolder(BlogAdapter.BlogViewHolder holder, int position) {
        Blog newBlog = blogList.get(position);

        holder.setDetails(newBlog.getTitle(), newBlog.getDescription(),
                newBlog.getUsername(), newBlog.getLikes(), newBlog.getImage());
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }
}

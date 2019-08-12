package com.example.kaivalyamendki.sycodestalk;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Kaivalya Mendki on 05-04-2018.
 */

public class FollowManageAdapter extends RecyclerView.Adapter <FollowManageAdapter.UsersViewHolder> {

        public List<CodeStalk_users> userList;
        Dialog profileDialog;

        String reqToId, reqFromId;
        Follow_Request fReq;
        int flag = 0;
        String count;
        CodeStalk_users cUser, tempUser;

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView user_name, user_email;
        public ImageView user_image;
        public Context ctx;
        public RelativeLayout userSelection;

    public UsersViewHolder(View itemView, Context ctx) {
        super(itemView);
        this.ctx = ctx;
        mView = itemView;
        userSelection = (RelativeLayout) mView.findViewById(R.id.userSelection);
        user_name = (TextView) mView.findViewById(R.id.name_text);
        user_email = (TextView) mView.findViewById(R.id.email_text);
        user_image = (ImageView) mView.findViewById(R.id.profile_image);
    }

    public void setDetails(final String userName, String userEmail, String userImage) {

            /*TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_email = (TextView) mView.findViewById(R.id.email_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);*/

        user_name.setText(userName);
        user_email.setText(userEmail);

        Glide.with(ctx).load(userImage).into(user_image);
    }
}

    public FollowManageAdapter(List<CodeStalk_users> userList) {
        this.userList = userList;
    }

    @Override
    public FollowManageAdapter.UsersViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);

        final FollowManageAdapter.UsersViewHolder uHolder = new FollowManageAdapter.UsersViewHolder(itemView, parent.getContext());

        profileDialog = new Dialog(parent.getContext());
        profileDialog.setContentView(R.layout.profile_popup);
        profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //pop_image.setImageResource(Integer.parseInt(userList.get(uHolder.getAdapterPosition()).getPicUrl()));




        uHolder.userSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView pop_image = (ImageView)profileDialog.findViewById(R.id.pop_image);
                TextView pop_username = (TextView)profileDialog.findViewById(R.id.pop_username);
                TextView pop_email = (TextView)profileDialog.findViewById(R.id.pop_email);
                TextView pop_followers = (TextView)profileDialog.findViewById(R.id.pop_followers);
                TextView pop_following = (TextView)profileDialog.findViewById(R.id.pop_following);
                TextView pop_rating = (TextView)profileDialog.findViewById(R.id.pop_rating);
                TextView pop_status = (TextView)profileDialog.findViewById(R.id.pop_status);
                TextView pop_txtclose = (TextView)profileDialog.findViewById(R.id.pop_txtclose);
                Button pop_btnfollow = (Button)profileDialog.findViewById(R.id.pop_btnfollow);


                Glide.with(parent.getContext())
                        .load(userList.get(uHolder.getAdapterPosition()).getPicUrl())
                        .into(pop_image);

                pop_username.setText(userList.get(uHolder.getAdapterPosition()).getUsername());
                pop_email.setText(userList.get(uHolder.getAdapterPosition()).getEmail());
                pop_followers.setText(String.valueOf(userList.get(uHolder.getAdapterPosition()).getFollowers()));
                pop_following.setText(String.valueOf(userList.get(uHolder.getAdapterPosition()).getFollowing()));
                pop_rating.setText(String.valueOf(userList.get(uHolder.getAdapterPosition()).getRating()));
                pop_status.setText(userList.get(uHolder.getAdapterPosition()).getStatus());

                pop_txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profileDialog.dismiss();
                    }
                });

                //Toast.makeText(parent.getContext(), userList.get(uHolder.getAdapterPosition()).getUsername(), Toast.LENGTH_SHORT).show();
                profileDialog.show();
            }
        });

        return uHolder;
    }

    @Override
    public void onBindViewHolder(FollowManageAdapter.UsersViewHolder holder, int position) {
        CodeStalk_users cUser = userList.get(position);

        holder.setDetails(cUser.getUsername(), cUser.getEmail(), cUser.getPicUrl());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

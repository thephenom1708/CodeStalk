package com.example.kaivalyamendki.sycodestalk;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaivalya Mendki on 03-04-2018.
 */

public class FollowAdapter extends RecyclerView.Adapter <FollowAdapter.UsersViewHolder> {

    public List<CodeStalk_users> userList;
    Dialog profileDialog;
    String reqFromId, reqToId;
    int flagFrom = 0, flagTo = 0;

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView user_name, user_email;
        public ImageView user_image;
        public Context ctx;
        public RelativeLayout followReqSelection;
        public Button acceptBtn, declineBtn;

        public UsersViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            mView = itemView;
            followReqSelection = (RelativeLayout) mView.findViewById(R.id.followReqSelection);
            user_name = (TextView) mView.findViewById(R.id.followName_text);
            user_email = (TextView) mView.findViewById(R.id.followEmail_text);
            user_image = (ImageView) mView.findViewById(R.id.followProfile_image);
            acceptBtn = (Button)mView.findViewById(R.id.followReq_acceptBtn);
            declineBtn = (Button)mView.findViewById(R.id.followReq_declineBtn);
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

    public FollowAdapter(List<CodeStalk_users> userList) {
        this.userList = userList;
    }

    @Override
    public FollowAdapter.UsersViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.followreq_recycle, parent, false);

        final FollowAdapter.UsersViewHolder uHolder = new FollowAdapter.UsersViewHolder(itemView, parent.getContext());

        profileDialog = new Dialog(parent.getContext());
        profileDialog.setContentView(R.layout.profile_popup);
        profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //pop_image.setImageResource(Integer.parseInt(userList.get(uHolder.getAdapterPosition()).getPicUrl()));

        uHolder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                reqToId =  user.getUid();
                reqFromId = userList.get(uHolder.getAdapterPosition()).getId();

               final DatabaseReference userDatabaseFrom = FirebaseDatabase.getInstance().getReference("CodeStalk_users/"+reqFromId);
               final DatabaseReference userDatabaseTo = FirebaseDatabase.getInstance().getReference("CodeStalk_users/"+reqToId);
               final DatabaseReference followReqDatabase = FirebaseDatabase.getInstance().getReference("Follow_Requests");
               final DatabaseReference followersDatabase = FirebaseDatabase.getInstance().getReference("Follow_Management");
                final DatabaseReference tempDatabse = FirebaseDatabase.getInstance().getReference("Follow_Requests/"+ user.getUid());
                if(user != null)
                {
                    tempDatabse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Follow_Request fReq = dataSnapshot.getValue(Follow_Request.class);
                            List<String>list = fReq.getRequestFromId();
                            for(int i = 0; i < list.size(); i++){
                                if(list.get(i).equals(reqFromId)){
                                    list.remove(i);
                                    break;
                                }
                            }
                            fReq = new Follow_Request(list);
                            tempDatabse.setValue(fReq);
                            //notifyDataSetChanged();
                            tempDatabse.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    userDatabaseFrom.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CodeStalk_users userFrom = dataSnapshot.getValue(CodeStalk_users.class);
                            String count = String.valueOf(Integer.parseInt(userFrom.getFollowing()) + 1);
                            userFrom.setFollowing(count);
                            userDatabaseFrom.setValue(userFrom);
                            userDatabaseFrom.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    userDatabaseTo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CodeStalk_users userTo = dataSnapshot.getValue(CodeStalk_users.class);
                            String count = String.valueOf(Integer.parseInt(userTo.getFollowers()) + 1);
                            userTo.setFollower(count);
                            userDatabaseTo.setValue(userTo);
                            userDatabaseTo.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    followersDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot walkerFrom : dataSnapshot.getChildren()){
                                flagFrom = 0;
                                if(walkerFrom.getKey().equals(reqFromId)){
                                    flagFrom = 1;
                                    Follow follow = walkerFrom.getValue(Follow.class);
                                    follow.addFollowing(reqToId);
                                    followersDatabase.child(reqFromId).setValue(follow);
                                    break;
                                }
                            }
                            if(flagFrom == 0){
                                List<String>list1 = new ArrayList<>();
                                List<String>list2 = new ArrayList<>();
                                list2.add(reqToId);
                                Follow flw = new Follow(list1, list2);
                                followersDatabase.child(reqFromId).setValue(flw);
                            }
                            for(DataSnapshot walkerTo : dataSnapshot.getChildren()){
                                flagTo = 0;
                                if(walkerTo.getKey().equals(reqToId)){
                                    flagTo = 1;
                                    Follow follow = walkerTo.getValue(Follow.class);
                                    follow.addFollower(reqFromId);
                                    followersDatabase.child(reqToId).setValue(follow);
                                    followersDatabase.removeEventListener(this);
                                    break;
                                }
                            }
                            if(flagTo == 0){
                                List<String>list1 = new ArrayList<>();
                                List<String>list2 = new ArrayList<>();
                                list1.add(reqFromId);
                                Follow flw = new Follow(list1, list2);
                                followersDatabase.child(reqToId).setValue(flw);
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
                dashboard dash = new dashboard();
                android.support.v4.app.FragmentManager manager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment, dash, dash.getTag()).commit();

            }

        });

        uHolder.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                reqFromId =  userList.get(uHolder.getAdapterPosition()).getId();
                reqToId = user.getUid();
                final DatabaseReference followReqDatabase = FirebaseDatabase.getInstance().getReference("Follow_Requests/"+ user.getUid());

                followReqDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Follow_Request fReq = dataSnapshot.getValue(Follow_Request.class);
                        List<String>list = fReq.getRequestFromId();
                        for(int i = 0; i < list.size(); i++){
                            if(list.get(i).equals(reqFromId)){
                                list.remove(i);
                                break;
                            }
                        }
                        fReq = new Follow_Request(list);
                        followReqDatabase.setValue(fReq);
                        notifyDataSetChanged();
                        Toast.makeText(parent.getContext(), "Follow Request declined...", Toast.LENGTH_SHORT).show();
                        followReqDatabase.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

       /* uHolder.followReqSelection.setOnClickListener(new View.OnClickListener() {
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

                pop_btnfollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //userList.get(uHolder.getAdapterPosition()).setFollower();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Follow_Requests");
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();

                        FirebaseUser user = mAuth.getCurrentUser();

                        if(user != null)
                        {
                            String reqToId =  userList.get(uHolder.getAdapterPosition()).getId();
                            databaseReference.child(reqToId).setValue(new Follow_Request(user.getUid()));

                        }
                    }
                });

                //Toast.makeText(parent.getContext(), userList.get(uHolder.getAdapterPosition()).getUsername(), Toast.LENGTH_SHORT).show();
                profileDialog.show();
            }
        });*/

        return uHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        CodeStalk_users cUser = userList.get(position);

        holder.setDetails(cUser.getUsername(), cUser.getEmail(), cUser.getPicUrl());
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}

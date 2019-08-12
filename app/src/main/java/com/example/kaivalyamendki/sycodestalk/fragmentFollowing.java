package com.example.kaivalyamendki.sycodestalk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Created by Kaivalya Mendki on 02-04-2018.
 */

public class fragmentFollowing extends Fragment {

    View view;
    private RecyclerView mResultList;
    FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase, followDatabase;
    private FollowManageAdapter followAdapter;
    private List<CodeStalk_users> userList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_following, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("CodeStalk_users");
        followDatabase = FirebaseDatabase.getInstance().getReference("Follow_Management");

        mAuth = FirebaseAuth.getInstance();

        mResultList = (RecyclerView)getView().findViewById(R.id.following_recyclerView);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        followAdapter = new FollowManageAdapter(userList);

        mResultList.setAdapter(followAdapter);
        //userList.clear();
        followAdapter.notifyDataSetChanged();

         firebaseFollowerSearch();
    }


    public void firebaseFollowerSearch()
    {
        final FirebaseUser user = mAuth.getCurrentUser();
        followDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot walker : dataSnapshot.getChildren())
                {
                    if(walker.getKey().equals(user.getUid()))
                    {
                        final Follow follow = walker.getValue(Follow.class);

                        mUserDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<String>list = follow.getFollowing();
                                for(String following : list) {
                                    CodeStalk_users cUser = dataSnapshot.child(following).getValue(CodeStalk_users.class);
                                    userList.add(cUser);
                                    //Toast.makeText(getContext(), cUser.getUsername(), Toast.LENGTH_SHORT).show();
                                }
                                followAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                followAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

package com.example.kaivalyamendki.sycodestalk;

import android.content.Context;
import android.net.Uri;
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

public class fragmentFollowRequests extends Fragment {

    private search_user.OnFragmentInteractionListener mListener;

    View view;

    private RecyclerView mResultList;
    FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase, followReqDatabase;
    private FollowAdapter followAdapter;
    private List<CodeStalk_users> userList;


    public fragmentFollowRequests(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_follow_requests, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("CodeStalk_users");
        followReqDatabase = FirebaseDatabase.getInstance().getReference("Follow_Requests");

        mAuth = FirebaseAuth.getInstance();

        mResultList = (RecyclerView)getView().findViewById(R.id.followReq_recyclerView);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        followAdapter = new FollowAdapter(userList);

        mResultList.setAdapter(followAdapter);
        //userList.clear();
        followAdapter.notifyDataSetChanged();

        firebaseFollowerSearch();
    }


    public void firebaseFollowerSearch()
    {
       final FirebaseUser user = mAuth.getCurrentUser();
        followReqDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot walker : dataSnapshot.getChildren())
                {
                    if(walker.getKey().equals(user.getUid()))
                    {
                        final Follow_Request fReq = walker.getValue(Follow_Request.class);

                        mUserDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(String reqFromId : fReq.requestFromId) {
                                    CodeStalk_users cUser = dataSnapshot.child(reqFromId).getValue(CodeStalk_users.class);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof search_user.OnFragmentInteractionListener) {
            mListener = (search_user.OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}



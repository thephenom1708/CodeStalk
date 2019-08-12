package com.example.kaivalyamendki.sycodestalk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link profile_show.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profile_show#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_show extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public profile_show() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_show.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_show newInstance(String param1, String param2) {
        profile_show fragment = new profile_show();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView profilePic;
    TextView username, csRating, followers, following, status, email, mobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_show, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        profilePic = (ImageView)getView().findViewById(R.id.profile_pic);
        username = (TextView)getView().findViewById(R.id.username);
        csRating = (TextView)getView().findViewById(R.id.cs_rating);
        followers = (TextView)getView().findViewById(R.id.followers);
        following = (TextView)getView().findViewById(R.id.following);
        email = (TextView)getView().findViewById(R.id.email);
        status = (TextView)getView().findViewById(R.id.status);
        mobile = (TextView)getView().findViewById(R.id.mobile);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("CodeStalk_users/"+ user.getUid());

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CodeStalk_users cUser = dataSnapshot.getValue(CodeStalk_users.class);

                username.setText(cUser.getUsername());
                csRating.setText(cUser.getRating());
                followers.setText(cUser.getFollowers());
                following.setText(cUser.getFollowing());
                email.setText(cUser.getEmail());
                status.setText(cUser.getStatus());
                mobile.setText(cUser.getMobile());

                Glide.with(getContext()).load(cUser.getPicUrl()).into(profilePic);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }/* else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

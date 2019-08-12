package com.example.kaivalyamendki.sycodestalk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link search_user.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link search_user#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search_user extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public search_user() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static search_user newInstance(String param1, String param2) {
        search_user fragment = new search_user();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;
    private UserAdapter userAdapter;
    private List <CodeStalk_users> userList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_user, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("CodeStalk_users");
        mSearchField = (EditText) getView().findViewById(R.id.search_field);
        mSearchBtn = (ImageButton)getView().findViewById(R.id.search_btn);

        mResultList = (RecyclerView)getView().findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);

        mResultList.setAdapter(userAdapter);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();
                //Toast.makeText(getContext(), "trip", Toast.LENGTH_SHORT).show();
                userList.clear();
                userAdapter.notifyDataSetChanged();
                firebaseUserSearch(searchText);

            }
        });

    }

    public void firebaseUserSearch(final String searchText) {

        Toast.makeText(getContext(), "Started Search for  " + searchText , Toast.LENGTH_LONG).show();

       /* Query firebaseSearchQuery = mUserDatabase.orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<CodeStalk_users> options = new FirebaseRecyclerOptions.Builder<CodeStalk_users>()
                .setQuery(firebaseSearchQuery, CodeStalk_users.class).build();

        final FirebaseRecyclerAdapter<CodeStalk_users, UsersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CodeStalk_users, UsersViewHolder>(options) {
            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_layout, parent, false);

                return new UsersViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder viewHolder, int position, @NonNull CodeStalk_users model) {
                viewHolder.setDetails(getContext(), model.getUsername(), model.getEmail(), model.getPicUrl());

            }
            @Override
            public int getItemCount() {
                return firebaseRecyclerAdapter.size();
            }

        };

        mResultList.setAdapter(firebaseRecyclerAdapter);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));*/

       mUserDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               int flag = 0;
               for(DataSnapshot walker : dataSnapshot.getChildren())
               {
                    CodeStalk_users cUser =  walker.getValue(CodeStalk_users.class);
                    /*for(Iterator<CodeStalk_users> itr = userList.iterator(); itr.hasNext(); )
                    {
                        CodeStalk_users userWalker = itr.next();
                        if(cUser.getEmail().equals(userWalker.getEmail()))
                        {
                            if(!cUser.getUsername().toLowerCase().startsWith(searchText.toLowerCase()))
                                itr.remove();
                            //Toast.makeText(getContext(), "chip", Toast.LENGTH_SHORT).show();
                        }
                    }*/
                   if(cUser.getUsername().toLowerCase().startsWith(searchText.toLowerCase())) {
                       flag = 1;
                       userList.add(cUser);
                       //Toast.makeText(getContext(), cUser.getUsername(), Toast.LENGTH_SHORT).show();
                   }
               }

               if(flag == 0){
                   Toast.makeText(getContext(), "No user found mathcing with this keyword...", Toast.LENGTH_SHORT).show();
               }
               userAdapter.notifyDataSetChanged();
               //userList.removeAll(userList);
               //Toast.makeText(getContext(), "Search Ended", Toast.LENGTH_SHORT).show();
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


/*class UsersViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public UsersViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

    }

    public void setDetails(Context ctx, String userName, String userEmail, String userImage) {

        TextView user_name = (TextView) mView.findViewById(R.id.name_text);
        TextView user_email = (TextView) mView.findViewById(R.id.email_text);
        ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


        user_name.setText(userName);
        user_email.setText(userEmail);

        Glide.with(ctx).load(userImage).into(user_image);


    }
}*/





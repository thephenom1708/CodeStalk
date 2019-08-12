package com.example.kaivalyamendki.sycodestalk;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBlog extends Fragment {


    public AddBlog() {
        // Required empty public constructor
    }

    private static final int CHOOSE_IMAGE = 101;

    ImageView blogImage;
    EditText title, description;
    Button blogSubmit;

    Uri uriBlogImage;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    String blogImageUrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_add_blog, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title = (EditText)getView().findViewById(R.id.addTitle);
        description = (EditText)getView().findViewById(R.id.addDescription);
        blogSubmit = (Button)getView().findViewById(R.id.blogSubmit);
        blogImage = (ImageView)getView().findViewById(R.id.imageSelect);
        progressBar = (ProgressBar)getView().findViewById(R.id.blogProgressbar);

        blogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        blogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBlogInformation();
            }
        });
    }

    public void saveBlogInformation()
    {
        String blogTitle = title.getText().toString();
        String blogDescription = description.getText().toString();

        if (blogTitle.isEmpty()) {
            title.setError("Blog Title required");
            title.requestFocus();
            return;
        }
        else if(blogDescription.isEmpty())
        {
            description.setError("Blog Description required");
            description.requestFocus();
            return;
        }
        else if(blogImageUrl.isEmpty()){
            Toast.makeText(getContext(), "Blog Image required...", Toast.LENGTH_SHORT).show();
            blogImage.requestFocus();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String likes = "0";

        if(user != null && blogImageUrl != null){

            DatabaseReference forumDatabase = FirebaseDatabase.getInstance().getReference("TechForum");

            String blogKey = forumDatabase.push().getKey();
            Blog blog = new Blog(blogKey, blogTitle, blogDescription, user.getDisplayName(), blogImageUrl, likes);
            forumDatabase.child(blogKey).setValue(blog);
            Toast.makeText(getContext(), "Blog added to TechForum Successfuly...", Toast.LENGTH_SHORT).show();
            TechForum techForum = new TechForum();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, techForum, techForum.getTag()).commit();
        }
        else{
            Toast.makeText(getContext(), "Some Error Occurred!!! Try again...", Toast.LENGTH_SHORT).show();
        }

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            uriBlogImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriBlogImage);
                blogImage.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("BlogPics/" + System.currentTimeMillis() + ".jpg");

        if (uriBlogImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriBlogImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            blogImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}

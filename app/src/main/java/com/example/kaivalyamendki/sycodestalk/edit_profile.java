package com.example.kaivalyamendki.sycodestalk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class edit_profile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;

    TextView textView;
    ImageView imageView;
    EditText username, mobile, status;

    Uri uriProfileImage;
    ProgressBar progressBar;

    String profileImageUrl;
    CheckBox hackerrank,codechef,hackerearth;
    EditText hackerrank_user, codechef_user, hackerearth_user;
    FirebaseAuth mAuth;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();

        /*Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        username = (EditText) findViewById(R.id.editTextDisplayName);
        mobile = (EditText) findViewById(R.id.editTextDisplayMobile);
        status = (EditText) findViewById(R.id.editTextDisplayBio);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        textView = (TextView) findViewById(R.id.textViewVerified);
        hackerrank = (CheckBox)findViewById(R.id.hackerrank);
        hackerearth = (CheckBox)findViewById(R.id.hackerearth);
        codechef = (CheckBox)findViewById(R.id.codechef);
        hackerrank_user = (EditText) findViewById(R.id.hackerrank_user);
        codechef_user = (EditText)findViewById(R.id.codechef_user);
        hackerearth_user = (EditText)findViewById(R.id.hackerearth_user);

        databaseReference = FirebaseDatabase.getInstance().getReference("CodeStalk_users");

        hackerrank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked != true)
                    hackerrank_user.setVisibility(View.INVISIBLE);
                else
                    hackerrank_user.setVisibility(View.VISIBLE);
            }
        });
        codechef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked != true)
                    codechef_user.setVisibility(View.INVISIBLE);
                else
                    codechef_user.setVisibility(View.VISIBLE);
            }
        });
        hackerearth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked != true)
                    hackerearth_user.setVisibility(View.INVISIBLE);
                else
                    hackerearth_user.setVisibility(View.VISIBLE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        loadUserInformation();

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, login_page.class));
        }
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot walker : dataSnapshot.getChildren()) {
                        if(walker.getKey().equals(user.getUid())) {
                            CodeStalk_users currentUser = walker.getValue(CodeStalk_users.class);

                            mobile.setText(currentUser.getMobile());
                            status.setText(currentUser.getStatus());
                            if(!currentUser.getHackerrank_user().isEmpty()){
                                hackerrank.setChecked(true);
                                hackerrank_user.setText(currentUser.getHackerrank_user());
                            }
                            if(!currentUser.getHackerearth_user().isEmpty()){
                                hackerearth.setChecked(true);
                                hackerearth_user.setText(currentUser.getHackerearth_user());
                            }
                            if(!currentUser.getCodechef_user().isEmpty()){
                                codechef.setChecked(true);
                                codechef_user.setText(currentUser.getCodechef_user());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }

            if (user.getDisplayName() != null) {
                username.setText(user.getDisplayName());
            }

            if (user.isEmailVerified()) {
                textView.setText("Email Verified");
            } else {
                textView.setText("Email Not Verified (Click to Verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(edit_profile.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }


    private void saveUserInformation() {

        String displayName = username.getText().toString();
        String displayMobile = mobile.getText().toString();
        String displayStatus = status.getText().toString();
        String hackerrankUser = hackerrank_user.getText().toString();
        String codechefUser = codechef_user.getText().toString();
        String hackerearthUser = hackerearth_user.getText().toString();


        if (displayName.isEmpty()) {
            username.setError("Username required");
            username.requestFocus();
            return;
        }
        else if(displayMobile.isEmpty())
        {
            mobile.setError("Mobile Number required");
            mobile.requestFocus();
            return;
        }
        else if(displayStatus.isEmpty())
        {
            status.setError("Bio required");
            status.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        String picUrl = profileImageUrl;
       // Toast.makeText(getApplicationContext(), picUrl, Toast.LENGTH_SHORT).show();
        String uid = user.getUid();
        String emailId = user.getEmail();

        CodeStalk_users newUser = new CodeStalk_users(uid, emailId, displayName, displayMobile, displayStatus,
                picUrl, hackerrankUser, codechefUser, hackerearthUser);


        //Toast.makeText(edit_profile.this,"Added", Toast.LENGTH_SHORT).show();

        if (user != null && profileImageUrl != null) {

            databaseReference.child(uid).setValue(newUser);

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(edit_profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(edit_profile.this, profile_view.class));
                            }
                            else{
                                Toast.makeText(edit_profile.this, "Profile Update Failed !!! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            newUser.picUrl = user.getPhotoUrl().toString();
            //Toast.makeText(getApplicationContext(), newUser.getPicUrl(), Toast.LENGTH_SHORT).show();
            databaseReference.child(uid).setValue(newUser);

            Toast.makeText(edit_profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(edit_profile.this, profile_view.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(edit_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, login_page.class));

                break;
        }

        return true;
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

}

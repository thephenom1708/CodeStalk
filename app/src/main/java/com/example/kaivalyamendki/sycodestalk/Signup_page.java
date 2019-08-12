package com.example.kaivalyamendki.sycodestalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Signup_page extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText rpass, rcpass, remail;
    Button register;
    TextView loginLink;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        loginLink = (TextView)findViewById(R.id.login_link);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup_page.this, login_page.class));
            }
        });
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        remail = (EditText) findViewById(R.id.remail);
        rpass = (EditText) findViewById(R.id.rpassword);
        rcpass = (EditText) findViewById(R.id.rcpassword);

        register = (Button)findViewById(R.id.register_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSignup();
            }
        });
    }

    private void UserSignup()
    {
        String email, pass, cpass;

        email = remail.getText().toString();
        pass = rpass.getText().toString();
        cpass = rcpass.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(Signup_page.this, "Please enter the Email !!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(Signup_page.this, "Password field cannot be kept blank!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cpass))
        {
            Toast.makeText(Signup_page.this, "Confirm password field cannot be kept blank!!!", Toast.LENGTH_SHORT).show();
        }

         else if(pass.equals(cpass))
         {
             progressDialog.setMessage("Signing up...");
             progressDialog.show();

             firebaseAuth.createUserWithEmailAndPassword(email, cpass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful())
                      {
                          FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                          if(user != null) {
                              DatabaseReference reqDatabase = FirebaseDatabase.getInstance().getReference("Follow_Requests");
                              List<String> list = new ArrayList<>();
                              list.add(user.getUid());
                              reqDatabase.child(user.getUid()).setValue(new Follow_Request(list));
                          }
                          else {
                              Toast.makeText(Signup_page.this, "trip", Toast.LENGTH_SHORT).show();
                          }

                          Toast.makeText(Signup_page.this, "Registration Successful !!!", Toast.LENGTH_SHORT).show();
                          progressDialog.hide();
                          startActivity(new Intent(Signup_page.this, edit_profile.class));
                      }
                      else {
                          FirebaseAuthException e = (FirebaseAuthException)task.getException();
                          Toast.makeText(Signup_page.this, "Registration Failed !!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                          progressDialog.hide();
                      }
                 }
             });
         }
         else{
             Toast.makeText(Signup_page.this, "Password match failed !!!", Toast.LENGTH_SHORT).show();
         }
    }
}

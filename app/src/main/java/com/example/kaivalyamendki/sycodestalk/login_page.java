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


public class login_page extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Button login;
    EditText email_field, pass_field;
    ProgressDialog progressDialog;
    FirebaseUser user;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        /*System.setProperty("webdriver.chrome.driver", "G:\\Selenium\\Selenium_JAVA/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.hackerrank.com/The_PHENOM");
        String s = driver.getTitle().toString();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();*/

        progressDialog = new ProgressDialog(this);

        register = (TextView) findViewById(R.id.signup_button);
        login = (Button)findViewById(R.id.login_button);

        firebaseAuth = FirebaseAuth.getInstance();
        email_field = (EditText)findViewById(R.id.email);
        pass_field = (EditText)findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_page.this, Signup_page.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin();
            }
        });
    }

    private void UserLogin()
    {
        String email, password;

        email = email_field.getText().toString();
        password = pass_field.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(login_page.this, "Please enter the Email !!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(login_page.this, "Password field cannot be kept blank!!!", Toast.LENGTH_SHORT).show();
        }

        else {

            progressDialog.setMessage("Signing in...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                         user = firebaseAuth.getCurrentUser();
                        Toast.makeText(login_page.this, "Login Successful !!!", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                        startActivity(new Intent(login_page.this, edit_profile.class));
                    }
                    else {
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Toast.makeText(login_page.this, "Login Failed !!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }
            });
        }
    }
}

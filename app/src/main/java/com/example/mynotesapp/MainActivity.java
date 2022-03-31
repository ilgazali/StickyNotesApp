package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText loginemail, loginpassword;
    private RelativeLayout login,gotosignup;
    private TextView gotoforgotpassword;

    private FirebaseAuth firebaseAuth;

    private ProgressBar mProgressBarOfMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        loginemail = findViewById(R.id.login_email);
        loginpassword = findViewById(R.id.login_password);
        login = findViewById(R.id.login);
        gotosignup = findViewById(R.id.gotosignup);
        gotoforgotpassword = findViewById(R.id.gotoforgotpassword);

        mProgressBarOfMainActivity = findViewById(R.id.progressBarOfMainActivity);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){

            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));

        }

        gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
        gotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPasswordActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = loginemail.getText().toString().trim();
                String password = loginpassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }else{
                    //Login the user

                    mProgressBarOfMainActivity.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                checkMailVerification();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Account does not exist!", Toast.LENGTH_SHORT).show();

                                mProgressBarOfMainActivity.setVisibility(View.INVISIBLE);
                            }

                        }
                    });

                }
            }
        });
    }

    private void checkMailVerification(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser.isEmailVerified() == true)
        {
            Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));

        }
        else
        {
            mProgressBarOfMainActivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify your mail first!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }




    }
}
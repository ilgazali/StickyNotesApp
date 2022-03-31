package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText signinemail, signipassword;
    private RelativeLayout signin;
    private TextView gotologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        signinemail = findViewById(R.id.signin_email);
        signipassword = findViewById(R.id.signin_password);
        signin = findViewById(R.id.signin);
        gotologin = findViewById(R.id.gotologin);

        firebaseAuth = FirebaseAuth.getInstance();


        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = signinemail.getText().toString().trim();
                String password = signipassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){

                    Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();

                }else if (password.length()<7){

                    Toast.makeText(getApplicationContext(), "Password should be greater than 7 character!", Toast.LENGTH_SHORT).show();

                }else{
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Registration Successfull!",Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Failed to register!",Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });
    }
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Verification email is sent, verify and log in again!",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));

                }
            });
        }
    }
}
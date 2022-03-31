package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forgotpasswordEditText;
    private RelativeLayout passwordRecoverButton;
    private TextView gobacktologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();

        forgotpasswordEditText = findViewById(R.id.forgotpasswordEditText);
        passwordRecoverButton = findViewById(R.id.passwordRecoverButton);
        gobacktologin = findViewById(R.id.gobacktologin);

        firebaseAuth = FirebaseAuth.getInstance();

        gobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgotPasswordActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

         passwordRecoverButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String mail = forgotpasswordEditText.getText().toString().trim();
                 if (mail.isEmpty()){
                     Toast.makeText(getApplicationContext(), "Please enter your mail!", Toast.LENGTH_SHORT).show();
                 }else{
                     //recover password stuff

                     firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {

                             if (task.isSuccessful()){

                                 Toast.makeText(getApplicationContext(),"Mail is sent, you can recover your password using email!",Toast.LENGTH_SHORT).show();
                                 finish();
                                 startActivity(new Intent(ForgotPasswordActivity.this,MainActivity.class));

                             }
                             else
                             {
                                 Toast.makeText(getApplicationContext(),"Email is wrong or account does not exist!",Toast.LENGTH_SHORT).show();

                             }


                         }
                     });
                 }
             }
         });



    }
}
package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText mCreateTitleOfNote, mCreateContentOfNote;
    private FloatingActionButton mSaveNote;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar mProgressBarOfCreateNoteActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mCreateTitleOfNote = findViewById(R.id.createTitleOfNote);
        mCreateContentOfNote = findViewById(R.id.createContentOfNote);
        mSaveNote = findViewById(R.id.saveNote);

        Toolbar toolbar = findViewById(R.id.toolbarCreateNote);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mProgressBarOfCreateNoteActivity = findViewById(R.id.progressBarOfCreateNoteActivity);

        mSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mCreateTitleOfNote.getText().toString();
                String content = mCreateContentOfNote.getText().toString();

                if (title.isEmpty() || content.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgressBarOfCreateNoteActivity.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = firebaseFirestore.collection("notes")
                            .document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Note is created successfuly!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNoteActivity.this,NotesActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create note!", Toast.LENGTH_SHORT).show();

                            mProgressBarOfCreateNoteActivity.setVisibility(View.INVISIBLE);
                        }
                    });





                }


            }
        });








    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
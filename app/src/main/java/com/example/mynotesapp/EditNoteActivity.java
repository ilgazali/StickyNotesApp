package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class EditNoteActivity extends AppCompatActivity {

   private  Intent data;
   private  EditText mEditTitleOfNote, mEditContentOfNote;
   private FloatingActionButton editNote;

   private FirebaseAuth firebaseAuth;
   private FirebaseFirestore firebaseFirestore;
   private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mEditContentOfNote = findViewById(R.id.editContentOfNote);
        mEditTitleOfNote = findViewById(R.id.editTitleOfNote);
        editNote = findViewById(R.id.editNote);

        data = getIntent();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        Toolbar toolbar = findViewById(R.id.toolbarOfEditNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");

        mEditTitleOfNote.setText(noteTitle);
        mEditContentOfNote.setText(noteContent);

        editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();

                String newTitle = mEditTitleOfNote.getText().toString();
                String newContent = mEditContentOfNote.getText().toString();

                if (newTitle.isEmpty() || newContent.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Something is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    DocumentReference documentReference = firebaseFirestore.collection("notes")
                            .document(firebaseUser.getUid()).collection("myNotes")
                            .document(data.getStringExtra("noteId"));
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",newTitle);
                    note.put("content",newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Note is updated!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditNoteActivity.this,NotesActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to update!", Toast.LENGTH_SHORT).show();

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
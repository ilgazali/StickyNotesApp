package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetailsActivity extends AppCompatActivity {

    private TextView mDetailTitleOfNote,mDetailContentOfNote;
    private FloatingActionButton mGoToEditNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);


        mDetailContentOfNote = findViewById(R.id.detailContentOfNote);
        mDetailTitleOfNote = findViewById(R.id.detailTitleOfNote);
        mGoToEditNote = findViewById(R.id.gotoeditnote);

        Toolbar toolbar = findViewById(R.id.toolbarNoteDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        mGoToEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),EditNoteActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                v.getContext().startActivity(intent);

            }
        });

        mDetailContentOfNote.setText(data.getStringExtra("content"));
        mDetailTitleOfNote.setText(data.getStringExtra("title"));

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
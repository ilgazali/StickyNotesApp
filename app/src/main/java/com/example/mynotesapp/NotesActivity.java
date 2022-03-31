package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Random;

public class NotesActivity extends AppCompatActivity {

    private FloatingActionButton mCreateNoteFab;
    private FirebaseAuth firebaseAuth;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mCreateNoteFab = findViewById(R.id.createNoteFab);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("All Notes");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF000000"));

        // Set BackgroundDrawable
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        mCreateNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NotesActivity.this,CreateNoteActivity.class));
            }
        });


        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid())
                .collection("myNotes").orderBy("title",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<FirebaseModel> allUserNotes = new FirestoreRecyclerOptions.Builder<FirebaseModel>()
                .setQuery(query,FirebaseModel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allUserNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull FirebaseModel firebaseModel) {

                ImageView popupButton = noteViewHolder.itemView.findViewById(R.id.menuPopButton);



                int colorCode = getRandomColor();
                noteViewHolder.mNote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colorCode,null));


                noteViewHolder.noteTitle.setText(firebaseModel.getTitle());
                noteViewHolder.noteContent.setText(firebaseModel.getContent());

                String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();


                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //we have to open note detail activity
                        Intent intent = new Intent(v.getContext(),NoteDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("title",firebaseModel.getTitle());
                        intent.putExtra("content",firebaseModel.getContent());
                        intent.putExtra("noteId",docId);

                        v.getContext().startActivity(intent);

                    }
                });




                popupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent = new Intent(v.getContext(),EditNoteActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("title",firebaseModel.getTitle());
                                intent.putExtra("content",firebaseModel.getContent());
                                intent.putExtra("noteId",docId);

                                v.getContext().startActivity(intent);


                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                DocumentReference documentReference = firebaseFirestore.collection("notes")
                                        .document(firebaseUser.getUid()).collection("myNotes").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v.getContext(),"This note is deleted!",Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(),"Failed to delete!",Toast.LENGTH_LONG).show();
                                    }
                                });
                                return false;
                            }
                        });

                        popupMenu.show();
                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.notes_layout,parent,false);

                return new NoteViewHolder(view);



            }
        };

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(noteAdapter);



    }


    public class NoteViewHolder extends RecyclerView.ViewHolder
    {

        private TextView noteTitle;
        private TextView noteContent;
        LinearLayout mNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            mNote = itemView.findViewById(R.id.note);

        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(NotesActivity.this,MainActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null)
        {
            noteAdapter.stopListening();
        }
    }

    private int getRandomColor()
    {
        ArrayList<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.purple);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.teal);
        colorCode.add(R.color.cyan);
        colorCode.add(R.color.indigo);
        colorCode.add(R.color.amber);
        colorCode.add(R.color.red);
        colorCode.add(R.color.deep_purple);
        colorCode.add(R.color.light_blue);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.green);

        Random random = new Random();
        int number = random.nextInt(colorCode.size());
        return colorCode.get(number);

    }

}
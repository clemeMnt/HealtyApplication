package com.exemple.healtyapplication.ui.notes;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.healtyapplication.R;

import com.exemple.healtyapplication.model.Note;
import com.exemple.healtyapplication.model.NoteAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NotesFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private FloatingActionButton fab;
    private Button buttonSave;
    private EditText title, descriptions;
    private TextView titleCard, descriptionsCard;
    private ListView view_list;
    NoteAdapter adap;
    ArrayList<Note> notesList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        titleCard = (TextView) root.findViewById(R.id.text_notes_title);
        descriptionsCard = (TextView) root.findViewById(R.id.text_notes_desc);
        view_list = root.findViewById(R.id.listNote);
        loadData();

        fab = (FloatingActionButton) root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.popup_notes, null);
                mBuilder.setView(mView);
                AlertDialog dialog =  mBuilder.create();

                title = (EditText) mView.findViewById(R.id.input_notes_title);
                buttonSave = (Button) mView.findViewById(R.id.button_notes_save);
                descriptions = (EditText) mView.findViewById(R.id.input_notes_desc);

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveNote();
                        dialog.dismiss();
                        loadData();
                    }
                });
                dialog.show();
            }
        });


        view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note _note = (Note) adap.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle( _note.getTitle() + " - Notes");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         deleteNote(dialog, position, _note.getDocumentId());
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                builder.show();
            }
        });
        return root;
    }

    private void loadData() {
        db.collection("notes").document(mAuth.getUid()).collection("notes").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        notesList.clear();
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        Note n= doc.toObject(Note.class);
                        n.setDocumentId(doc.getId());
                        notesList.add(n);
                    }
                    adap = new NoteAdapter(getActivity(), notesList);
                    view_list.setAdapter(adap);
                }
            });
    }


    public void saveNote(){
        String _title = title.getText().toString();
        String _description = descriptions.getText().toString();

        if(_title.isEmpty()){
            Toast.makeText(getActivity(), "Some fiels are empty, try again", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> notes = new HashMap<>();
            notes.put("title", _title);
            notes.put("description", _description);

            DocumentReference docRef = db.collection("notes").document(mAuth.getUid()).collection("notes").document();
            docRef.set(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Succed", "ok");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error to request notes, try again", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


    public void deleteNote(DialogInterface dialog, int postiton, String id){
        DocumentReference docRef = db.collection("notes").document(mAuth.getUid()).collection("notes").document(id);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               notesList.remove(postiton);
               dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Error to delete note, try again", Toast.LENGTH_SHORT).show();

            }
        });

        adap.notifyDataSetChanged();

    }


}
package com.exemple.healtyapplication.ui.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.exemple.healtyapplication.MainActivity;
import com.exemple.healtyapplication.R;

import com.exemple.healtyapplication.ui.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedHashMap;
import java.util.Map;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    EditText displayName, phone, prefix, age, bth, allergies, blod, weight, height, cal, addresses;
    Button bottom;
    FirebaseFirestore db;
    private boolean _mode = false;
    View root;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        root = inflater.inflate(R.layout.fragment_account, container, false);
       

        displayName = (EditText) root.findViewById(R.id.input_acc_name);
        phone = (EditText) root.findViewById(R.id.input_acc_phone);
        addresses = (EditText) root.findViewById(R.id.input_acc_addresses);
        prefix = (EditText) root.findViewById(R.id.input_acc_prefix);
        age = (EditText) root.findViewById(R.id.input_acc_age);
        bth = (EditText) root.findViewById(R.id.input_acc_brth);
        allergies = (EditText) root.findViewById(R.id.input_acc_allergies);
        blod = (EditText) root.findViewById(R.id.input_acc_blodtype);
        weight = (EditText) root.findViewById(R.id.input_acc_weight);
        height = (EditText) root.findViewById(R.id.input_acc_height);
        cal = (EditText) root.findViewById(R.id.input_acc_minCal);
        bottom = (Button) root.findViewById(R.id.button_acc);
        
        enabledInput(false);
        updateProfiles();


        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!_mode){
                    enabledInput(true);
                   _mode = true;
                }else if(_mode){
                    saveData();
                    enabledInput(false);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateProfiles();
                }
            }
        });

        return root;
    }

    private void saveData() {
        final boolean[] b = {false};
        Map<String, Object> attent = new LinkedHashMap<>();

        attent.put("addresses", addresses.getText().toString());
        attent.put("phone", phone.getText().toString());
        attent.put("prefix", prefix.getText().toString());
        attent.put("age", age.getText().toString());
        attent.put("bth", bth.getText().toString());
        attent.put("allergies", allergies.getText().toString());
        attent.put("blodType", blod.getText().toString());
        attent.put("height", height.getText().toString());
        attent.put("weight", weight.getText().toString());
        attent.put("calPerDay", cal.getText().toString());
        attent.put("modify", true);

         db.collection("users").whereEqualTo("uid", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                // Add a new document with a generated ID
                                db.collection("users").document(doc.getId()).update(attent);
                            }
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void enabledInput(boolean v){
        displayName.setEnabled(false);
        addresses.setEnabled(v);
        phone.setEnabled(v);
        prefix.setEnabled(v);
        age.setEnabled(v);
        bth.setEnabled(v);
        allergies.setEnabled(v);
        blod.setEnabled(v);
        weight.setEnabled(v);
        height.setEnabled(v);
        cal.setEnabled(false);

        if(!v){
            bottom.setText("Modify");
        }else if (v){
            bottom.setText("Save ");
        }
    }

    private void updateProfiles() {

        db.collection("users").whereEqualTo("uid", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Log.d("OK", doc.getId() + " => " + doc.getData());
                                 String _displayName = doc.getString("name") + " " + doc.getString("surname");
                                displayName.setText(_displayName);
                                phone.setText(doc.getString("phone"));
                                prefix.setText(doc.getString("prefix"));

                                //Alllow to set variable if data are add, if not that show classique placeholder
                                boolean check = doc.getBoolean("modify");
                                Log.e("Verif", String.valueOf(check));
                                if(check){
                                    age.setText(doc.getString("age"));
                                    bth.setText(doc.getString("bth"));
                                    allergies.setText(doc.getString("allergies"));
                                    blod.setText(doc.getString("blodType"));
                                    weight.setText(doc.getString("weight"));
                                    height.setText(doc.getString("height"));
                                    cal.setText(doc.getString("calPerDay"));
                                    addresses.setText(doc.getString("addresses"));
                                }
                            }
                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
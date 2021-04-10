package com.exemple.healtyapplication.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.exemple.healtyapplication.HomeActivity;
import com.exemple.healtyapplication.MainActivity;
import com.exemple.healtyapplication.R;
import com.exemple.healtyapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Multiset;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends Activity {

    private EditText inputPwd, inputEmail, inputName, inputSurname, inputPrefix, inputPhone;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        inputEmail = (EditText) findViewById(R.id.input_regis_email);
        inputName = (EditText) findViewById(R.id.input_regis_name);
        inputSurname = (EditText) findViewById(R.id.input_regis_surname);
        inputPhone = (EditText) findViewById(R.id.input_regis_phone);
        inputPrefix = (EditText) findViewById(R.id.input_regis_prefix);
        inputPwd = (EditText) findViewById(R.id.input_regis_pwd);
    }

    public void signUpUser(View v){
        Bundle mBundle = checkData();
        boolean verif = mBundle.getBoolean("verif");
        User uI  = (User) mBundle.getSerializable("user");

        if (verif) {
            mAuth.createUserWithEmailAndPassword(uI.getEmail(), uI.getPwd())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign up success
                                Toast.makeText(RegistrationActivity.this, "Subscription succed.", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                addUsers(uI, user.getUid());

                                Intent login = new Intent(RegistrationActivity.this, SignActivity.class);
                                startActivity(login);

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegistrationActivity.this, "Subscription failed, try again.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    private Bundle checkData(){

        Bundle mBundle = new Bundle();

        String email = inputEmail.getText().toString();
        String pwd  = inputPwd.getText().toString();
        String prefix = inputPrefix.getText().toString();
        String phone = inputPhone.getText().toString();
        String name = inputName.getText().toString();
        String surname = inputSurname.getText().toString();


        if (email.length() == 0 || pwd.length() == 0){
            Toast.makeText(RegistrationActivity.this, "Some fields are empty.", Toast.LENGTH_SHORT).show();
            mBundle.putBoolean("verif", false);
            mBundle.putParcelable("user", null);
            return mBundle;
        }

        if (prefix.length() == 0 || phone.length() == 0){
            Toast.makeText(RegistrationActivity.this, "Some fields are empty.", Toast.LENGTH_SHORT).show();
            mBundle.putBoolean("verif", false);
            mBundle.putParcelable("user", null);
            return mBundle;
        }

        if (name.length() == 0 || surname.length() == 0){
            Toast.makeText(RegistrationActivity.this, "Some fields are empty.", Toast.LENGTH_SHORT).show();
            mBundle.putBoolean("verif", false);
            mBundle.putParcelable("user", null);
            return mBundle;
        }

        User u = new User(email, pwd,name, surname, prefix, phone);
        mBundle.putBoolean("verif",true);
        mBundle.putSerializable("user", (Serializable) u);
        return mBundle;
    }

    private void addUsers(User u, String uid){

        Map<String, Object> user = new HashMap<>();
        user.put("uid", uid);
        user.put("email", u.getEmail());
        user.put("name", u.getName());
        user.put("surname", u.getSurname());
        user.put("prefix", u.getPrefix());
        user.put("phone", u.getPhone());

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("succed", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failled", "Error adding document", e);
                    }
                });
    }

}
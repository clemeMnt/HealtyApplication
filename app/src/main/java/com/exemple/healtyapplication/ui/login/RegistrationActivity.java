package com.exemple.healtyapplication.ui.login;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.exemple.healtyapplication.MainActivity;
import com.exemple.healtyapplication.R;
import com.exemple.healtyapplication.model.User;
import com.exemple.healtyapplication.ui.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText inputPwd, inputEmail, inputName, inputSurname, inputPrefix, inputPhone;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Registration");
            actionBar.setDisplayHomeAsUpEnabled(true);
            ColorDrawable c = new ColorDrawable(Color.parseColor("#3A647F"));
            actionBar.setBackgroundDrawable(c);
            actionBar.show();
        }

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
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegistrationActivity.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                            }

                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Subscription succed.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegistrationActivity.this, "Check your email, you need to verifiy your email first.", Toast.LENGTH_SHORT).show();

                                FirebaseUser uA = mAuth.getCurrentUser();
                                addUsers(uI, uA);
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

    private void addUsers(User u, FirebaseUser uA){

        Map<String, Object> user = new HashMap<String, Object>();
        user.put("uid", uA.getUid());
        user.put("email", u.getEmail());
        user.put("name", u.getName());
        user.put("surname", u.getSurname());
        user.put("prefix", u.getPrefix());
        user.put("phone", u.getPhone());
        user.put("modify", false);
        user.put("age", "");
        user.put("bth", "");
        user.put("allergies", "");
        user.put("blodType", "");
        user.put("weight", "");
        user.put("height", "");
        user.put("calPerDay", "");
        user.put("addresses", "");

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

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(u.getName() + u.getSurname())
                .build();

        uA.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Message", "User profile updated.");
                        }
                    }
                });

        uA.sendEmailVerification();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent home = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(home);
        finish();

        return super.onOptionsItemSelected(item);
    }

}
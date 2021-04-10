package com.exemple.healtyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.exemple.healtyapplication.login.SignActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(HomeActivity.this, currentUser.getEmail(), Toast.LENGTH_SHORT).show();

            currentUser.reload();
        }else {
            Toast.makeText(HomeActivity.this, "fail", Toast.LENGTH_SHORT).show();
        }
    }


    public void logout (View v){
        FirebaseAuth.getInstance().signOut();
        Intent home = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(home);
        finish();
    }
}
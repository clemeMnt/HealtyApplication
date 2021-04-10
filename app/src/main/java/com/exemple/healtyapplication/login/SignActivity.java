package com.exemple.healtyapplication.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exemple.healtyapplication.HomeActivity;
import com.exemple.healtyapplication.MainActivity;
import com.exemple.healtyapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignActivity extends AppCompatActivity {

    private EditText inputPwd, inputEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        mAuth = FirebaseAuth.getInstance();

        inputPwd = (EditText) findViewById(R.id.input_login_pwd);
        inputEmail = (EditText) findViewById(R.id.input_login_email);


    }



    public void signInUser(View v){
        mAuth.signInWithEmailAndPassword(inputEmail.getText().toString(), inputPwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Toast.makeText(SignActivity.this, "Authentication succed.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent home = new Intent(SignActivity.this, HomeActivity.class);
                            startActivity(home);

                            Log.d("user", String.valueOf(user));

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignActivity.this, "Authentication failed, try again.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}
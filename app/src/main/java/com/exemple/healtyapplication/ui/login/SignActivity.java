package com.exemple.healtyapplication.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.exemple.healtyapplication.ui.home.HomeActivity;
import com.exemple.healtyapplication.R;
import com.exemple.healtyapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

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
        Bundle mBundle = checkData();
        boolean verif = mBundle.getBoolean("verif");
        User uI  = (User) mBundle.getSerializable("user");

        if (verif) {
            mAuth.signInWithEmailAndPassword(inputEmail.getText().toString(), inputPwd.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();

                                if (!user.isEmailVerified()) {
                                    Toast.makeText(SignActivity.this, "Authentication failed, you need to verifiy your email first.", Toast.LENGTH_SHORT).show();
                                    user.reload();
                                } else {
                                    Intent home = new Intent(SignActivity.this, HomeActivity.class);
                                    startActivity(home);
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignActivity.this, "Authentication failed, try again.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    private Bundle checkData(){

        Bundle mBundle = new Bundle();

        String email = inputEmail.getText().toString();
        String pwd  = inputPwd.getText().toString();


        if (email.length() == 0 || pwd.length() == 0){
            Toast.makeText(SignActivity.this, "Some fields are empty.", Toast.LENGTH_SHORT).show();
            mBundle.putBoolean("verif", false);
            mBundle.putParcelable("user", null);
            return mBundle;
        }

        User u = new User(email, pwd);
        mBundle.putBoolean("verif",true);
        mBundle.putSerializable("user", (Serializable) u);
        return mBundle;
    }

}
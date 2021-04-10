package com.exemple.healtyapplication.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.exemple.healtyapplication.HomeActivity;
import com.exemple.healtyapplication.MainActivity;
import com.exemple.healtyapplication.R;

public class SignActivity extends AppCompatActivity {

    TextView header, textPwd, textEmail;
    EditText inputPwd, inputEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        textEmail = (TextView) findViewById(R.id.text_login_email);
        textPwd = (TextView) findViewById(R.id.text_login_pwd);

        inputPwd = (EditText) findViewById(R.id.input_login_pwd);
        inputEmail = (EditText) findViewById(R.id.input_login_email);

    }


    public void HomePage(View v){
        Intent home = new Intent(SignActivity.this, HomeActivity.class);
        startActivity(home);
    }
}
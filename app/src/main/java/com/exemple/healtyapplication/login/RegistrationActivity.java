package com.exemple.healtyapplication.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.exemple.healtyapplication.HomeActivity;
import com.exemple.healtyapplication.MainActivity;
import com.exemple.healtyapplication.R;

public class RegistrationActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }


    public void LoginPage(View v){
        Intent log = new Intent(RegistrationActivity.this, SignActivity.class);
        startActivity(log);
    }
}
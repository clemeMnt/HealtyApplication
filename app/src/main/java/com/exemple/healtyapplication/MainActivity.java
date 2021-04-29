package com.exemple.healtyapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.widget.ImageView;


import com.exemple.healtyapplication.ui.home.HomeActivity;
import com.exemple.healtyapplication.ui.login.RegistrationActivity;
import com.exemple.healtyapplication.ui.login.SignActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 1;
    private CarouselView carouselView;
    int[] sampleImages = {R.drawable.undraw_healthy, R.drawable.undraw_personalization, R.drawable.undraw_workout};
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent home = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(home);
            finish();
        }
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    public void RegistrationPage(View v){
        Intent regis = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(regis);
        finish();
    }

    public void LoginPage(View v){
        Intent log = new Intent(MainActivity.this, SignActivity.class);
        startActivity(log);
        finish();
    }

}
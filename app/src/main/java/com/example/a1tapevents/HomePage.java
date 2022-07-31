package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class HomePage extends AppCompatActivity {
    private ImageView venues;
    private ImageView videography;
    private ImageView caterings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        init();
        onClickFunctions();
    }
    private void init() {
        venues = findViewById(R.id.venue_homepage);
        videography = findViewById(R.id.videography_homepage);
        caterings = findViewById(R.id.cateringservice_homepage);
    }

    private void onClickFunctions() {
        Log.v("init","1");
        venues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        videography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        caterings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePage.this,Food.class);
                startActivity(intent);
            }
        });

}}

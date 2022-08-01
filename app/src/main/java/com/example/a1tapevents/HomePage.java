package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class HomePage extends AppCompatActivity {

    public static final String TEXT_TO_SEND = "com.example.a1tapevents.TEXT_TO_SEND";
    private ImageView venues;
    private ImageView videography;
    private ImageView caterings;

    String textToSend;



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

                textToSend = "Venues";
                gotoActivity();
            }
        });

        videography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend = "Photography & Videography";
                gotoActivity();
            }
        });

        caterings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend = "Catering";
                gotoActivity();
            }
        });

}

    private void gotoActivity() {
        Intent intent = new Intent(this,AllServices.class);
        intent.putExtra(TEXT_TO_SEND,textToSend);
        startActivity(intent);
    }
}

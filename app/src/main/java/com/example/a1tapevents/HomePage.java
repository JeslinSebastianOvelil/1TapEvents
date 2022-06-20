package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class HomePage extends AppCompatActivity {
    private ImageView logo3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

     init();
    }
    private void init(){
         logo3 = findViewById(R.id.logo3_home);}
}

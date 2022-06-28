package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.content.Intent;

public class Food extends AppCompatActivity {

    ImageButton logo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        ImageButton logo1= (ImageButton) findViewById(R.id.imageButton);
        logo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity = new Intent(Food.this,catering1.class);
                startActivity(intentLoadNewActivity);
            }
        });

    }
}







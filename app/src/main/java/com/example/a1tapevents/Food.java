package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Food extends AppCompatActivity {

    public static final String TEXT_TO_SEND = "com.example.a1tapevents.TEXT_TO_SEND";

    ImageButton logo1;
    TextView caterer;
    String textToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        caterer = findViewById(R.id.caterer_name);
        caterer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend = caterer.getText().toString();
                gotoActivity();
            }
        });

        ImageButton logo1= (ImageButton) findViewById(R.id.imageButton);
        logo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity = new Intent(Food.this,Service.class);
                startActivity(intentLoadNewActivity);
            }
        });

    }

    private void gotoActivity() {
        Intent intent = new Intent(this,Service.class);
        intent.putExtra(TEXT_TO_SEND,textToSend);
        startActivity(intent);
    }
}







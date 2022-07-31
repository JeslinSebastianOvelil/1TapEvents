package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class catering1 extends AppCompatActivity {

    Button addToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering1);
        init();
        onClickFunctions();
    }

    private void onClickFunctions() {
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(catering1.this,MyCart.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        addToCart = findViewById(R.id.button_addtocart);
    }
}
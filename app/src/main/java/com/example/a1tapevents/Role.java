package com.example.a1tapevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Role extends AppCompatActivity {

    private Button user;
    private Button organizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        init();
        onClickFunctions();
    }
    private void init(){

        user = findViewById(R.id.user_role);
        organizer = findViewById(R.id.organizer_role);

    }
    private void onClickFunctions() {


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login Function
                Intent intent = new Intent(Role.this,MainActivity.class);
                startActivity(intent);
            }
        });

        organizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login Function
                Intent intent = new Intent(Role.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView login;
    private EditText email;
    private EditText password;
    private Button login_btn;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        onClickFunctions();
    }

    private void init(){
        logo = findViewById(R.id.imageview1_login);
        login = findViewById(R.id.textView1_login);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        login_btn = findViewById(R.id.button_login);
        signup = findViewById(R.id.signup_login);
    }

    private void onClickFunctions() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login Function
                Intent intent = new Intent(MainActivity.this,HomePage.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Signup Function
                Intent intent = new Intent(MainActivity.this,SignupPage.class);
                startActivity(intent);
            }
        });
    }

    private void ApiCalls() {}
}
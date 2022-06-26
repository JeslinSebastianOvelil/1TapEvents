package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private TextView sign_up;
    private TextView forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        onClickFunctions();
    }
    private void init(){
        Log.v("init","1");
        logo = findViewById(R.id.imageview1_login);
        login = findViewById(R.id.textView1_login);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        login_btn = findViewById(R.id.button_login);
        sign_up = findViewById(R.id.signup_login);
        forgotpassword = findViewById(R.id.forgotpassword_login);
        Log.v("init","1");
    }


    private void onClickFunctions() {
        Log.v("init","1");
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login Function
                Intent intent = new Intent(MainActivity.this,HomePage.class);
                startActivity(intent);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Signup Function
                Intent intent = new Intent(MainActivity.this,SignupPage.class);
                startActivity(intent);
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login Function
                Intent intent = new Intent(MainActivity.this,HomePage.class);
                startActivity(intent);
            }
        });
        Log.v("init","1");
    }

    private void ApiCalls() {}
}
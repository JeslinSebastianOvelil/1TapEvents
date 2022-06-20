package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SignupPage extends AppCompatActivity {
    private ImageView logo1;
    private TextView name;
    private TextView contact;
    private TextView email;
    private TextView password;
    private TextView newpassword;
    private Button signupbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        init();
        onClickFunctions();
    }

    private void init() {
        logo1 = findViewById(R.id.logo_signup);
        name = findViewById(R.id.name_signup);
        contact = findViewById(R.id.contactnumber_signup);
        email = findViewById(R.id.email_signup);
        password = findViewById(R.id.password_signup);
        newpassword = findViewById(R.id.newpassword_signup);
        signupbtn = findViewById(R.id.signup_signup);
    }

    private void onClickFunctions() {
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Signup Function
                Intent intent = new Intent(SignupPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
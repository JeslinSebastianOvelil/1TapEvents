package com.example.a1tapevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupPage extends AppCompatActivity {

    private static final String TAG= "MainActivity";
    private ImageView logo1;
    private EditText name;
    private EditText contact;
    private EditText email;
    private EditText password;
    private EditText newpassword;
    private Button signupbtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        mAuth = FirebaseAuth.getInstance();
    }

    private void onClickFunctions() {
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Signup Function
                String name_d = name.getText().toString();
                String contact_string = contact.getText().toString();
                long contact_d;
                if(contact_string.equals(""))
                    contact_d = 0;
                else
                    contact_d = Long.parseLong(contact_string);
                String email_d = email.getText().toString();
                String password_d = password.getText().toString();
                String confirmpassword_d = newpassword.getText().toString();
                if(!password_d.equals(confirmpassword_d)){
                    Toast.makeText(SignupPage.this,"Please check both paasword",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(email_d) || TextUtils.isEmpty(password_d) || TextUtils.isEmpty(confirmpassword_d)  || TextUtils.isEmpty(name_d) || contact_d ==0){
                    Toast.makeText(SignupPage.this,"Please add your credentials",Toast.LENGTH_SHORT).show();
                }
                else {
                    //Creating user
                    mAuth.createUserWithEmailAndPassword(email_d, password_d).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Writing to database
                                writedb(name_d,contact_d,email_d,password_d);
                                Toast.makeText(SignupPage.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupPage.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupPage.this, "Signup failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void writedb(String name_d,long contact_d, String email_d, String password_d){

        //Writing to database
        Map<String,Object> user = new HashMap<>();
        user.put("name",name_d);
        user.put("contact",contact_d);
        user.put("email",email_d);
        user.put("password",password_d);

        db.collection("users").document().set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Data written successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,e.toString());
                    }
                });
    }

}
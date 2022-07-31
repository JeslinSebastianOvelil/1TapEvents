package com.example.a1tapevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class OrganizerLogin extends AppCompatActivity {
    private TextView login;
    private EditText email;
    private EditText password;
    private Button login_btn;
    private TextView forgotpassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_login);

        init();
        onClickFunctions();
    }
    private void init(){
        login = findViewById(R.id.title_login);
        email = findViewById(R.id.email_ologin);
        password = findViewById(R.id.password_ologin);
        login_btn = findViewById(R.id.button_ologin);
        forgotpassword = findViewById(R.id.forgot_ologin);
        mAuth = FirebaseAuth.getInstance();
    }


    private void onClickFunctions() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_d = email.getText().toString();
                String password_d = password.getText().toString();
                if(TextUtils.isEmpty(email_d) && TextUtils.isEmpty(password_d)){
                    Toast.makeText(OrganizerLogin.this,"Please add your credentials",Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email_d, password_d).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(OrganizerLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OrganizerLogin.this, HomePage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(OrganizerLogin.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter email ID to receive reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(OrganizerLogin.this, "Reset link sent to your email",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(OrganizerLogin.this,"Error! Failure in sending reset link",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }

    /*
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(OrganizerLogin.this, HomePage.class);
            startActivity(i);
            this.finish();
        }
    }
    */

}




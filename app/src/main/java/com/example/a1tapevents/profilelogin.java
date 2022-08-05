package com.example.a1tapevents;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1tapevents.models.CartModel;
import com.example.a1tapevents.models.OrganizerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class profilelogin extends AppCompatActivity {

    private static final String TAG = "profilelogin";
    TextView org_name,org_desc,org_address,org_contact,org_email;
    TextView logout;

    OrganizerModel organizerModel;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    FirebaseFirestore db;
    FirebaseUser currUser;
    ImageView org_photo;

    String orgname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilelogin);

        init();
        retrieverFirestore();
        onClick();
    }

    private void onClick() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

    private void retrieverFirestore() {
        String user_email = currUser.getEmail();

        db.collection("organizer").
                whereEqualTo("email",user_email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            //retrieveDocument(documentID);
                            db.collection("organizer").document(documentID)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            organizerModel = documentSnapshot.toObject(OrganizerModel.class);
                                            org_name.setText(organizerModel.getName());
                                            org_desc.setText(organizerModel.getAbout());
                                            org_address.setText(organizerModel.getAddress());
                                            org_contact.setText(organizerModel.getContact());
                                            org_email.setText(organizerModel.getEmail());
                                            orgname = organizerModel.getUrl();
                                            //getting image
                                            storageReference = storage.getReference().child(orgname);
                                            try {
                                                File localfile = File.createTempFile("images","jpg");
                                                storageReference.getFile(localfile)
                                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                                                ((ImageView)findViewById(R.id.organizer_photo)).setImageBitmap(bitmap);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(TAG, "onFailure: Failed to retrieve image ");
                                                            }
                                                        });
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                        }

                        else{
                            Log.d(TAG, "onComplete: Error.....");
                        }
                    }
                });
    }

    private void init(){
        org_name = findViewById(R.id.organizer_name);
        org_desc = findViewById(R.id.organizer_desc);
        org_address = findViewById(R.id.organizer_address);
        org_contact = findViewById(R.id.organizer_contact);
        org_email = findViewById(R.id.organizer_email);
        logout = findViewById(R.id.organizer_logout);
        org_photo = findViewById(R.id.organizer_photo);

        orgname = new String();

        storage = FirebaseStorage.getInstance();
        db= FirebaseFirestore.getInstance();
        currUser = FirebaseAuth.getInstance().getCurrentUser();


    }
}
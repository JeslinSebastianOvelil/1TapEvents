package com.example.a1tapevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1tapevents.models.BookingModel;
import com.example.a1tapevents.models.CartModel;
import com.example.a1tapevents.models.OrganizerModel;
import com.example.a1tapevents.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;

public class Service extends AppCompatActivity {

    private static final String TAG ="Service";
    private ImageView photo;
    private TextView name,contact,address,about,price;
    private Button addToCart;

    private OrganizerModel organizerModel;
    private CartModel cartModel;
    private BookingModel bookingModel;
    UserModel userModel;

    private FirebaseFirestore db;
    private FirebaseUser currUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    String organizer;
    String documentID;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = getIntent();
        organizer = intent.getStringExtra(AllServices.TEXT_TO_SEND);

        init();
        onClickFunctions();
        retrieverFirestore();

    }

    private void retrieverFirestore() {
        db.collection("organizer").
                whereEqualTo("name",organizer)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            documentID = documentSnapshot.getId();

                            db.collection("organizer").document(documentID)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            organizerModel = documentSnapshot.toObject(OrganizerModel.class);
                                            name.setText(organizerModel.getName());
                                            about.setText(organizerModel.getAbout());
                                            address.setText(organizerModel.getAddress());
                                            contact.setText(organizerModel.getContact());
                                            price.setText(new StringBuilder("Rs").append(organizerModel.getPrice()));
                                            String photourl = organizerModel.getUrl();
                                            //getting image
                                            storageReference = storage.getReference().child(photourl);
                                            try {
                                                File localfile = File.createTempFile("images","jpg");
                                                storageReference.getFile(localfile)
                                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                                                photo.setImageBitmap(bitmap);
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
        photo = findViewById(R.id.service_pic);
        name = findViewById(R.id.service_name);
        about = findViewById(R.id.service_desc);
        contact = findViewById(R.id.service_contact);
        price = findViewById(R.id.service_price);
        address = findViewById(R.id.service_address);
        addToCart = findViewById(R.id.service_addtocart);
        db = FirebaseFirestore.getInstance();
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        storage = FirebaseStorage.getInstance();

        name.setText(organizer);
    }

    private void onClickFunctions() {
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder addToCartDialog = new AlertDialog.Builder(Service.this);
                addToCartDialog.setTitle("Add to cart");
                View view = getLayoutInflater().inflate(R.layout.add_to_cart,null);

                EditText date = view.findViewById(R.id.addcart_date);
                EditText time = view.findViewById(R.id.addcart_time);
                EditText location = view.findViewById(R.id.addcart_location);
                Button submit = view.findViewById(R.id.addcart_submit);
                Button cancel = view.findViewById(R.id.addcart_cancel);



                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date_string =date.getText().toString();
                        String time_string =time.getText().toString();
                        String location_string = location.getText().toString();
                        addToFirestore(date_string, time_string, location_string);
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                addToCartDialog.setView(view);
                dialog = addToCartDialog.create();
                dialog.show();
            }
        });

    }

    private void addToFirestore(String date, String time, String location) {
        bookingModel = new BookingModel();
        bookingModel.setDate(date);
        bookingModel.setTime(time);
        bookingModel.setLocation(location);
        bookingModel.setStatus("Pending");

        cartModel = new CartModel();
        cartModel.setOrganizer(organizerModel.getName());
        cartModel.setContact(organizerModel.getContact());
        cartModel.setDate(date);
        cartModel.setTime(time);
        cartModel.setPrice(organizerModel.getPrice());
        String uid = currUser.getUid();
        db.collection("users").document(uid).collection("cart").document().set(cartModel);

        db.collection("users").document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);


                bookingModel.setUsername(userModel.getName());
                bookingModel.setContact(userModel.getContact());
                bookingModel.setEmail(userModel.getEmail());
                db.collection("organizer").document(documentID).collection("bookings").document().set(bookingModel);


            }
        });

    }

}
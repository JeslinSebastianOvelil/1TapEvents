package com.example.a1tapevents;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a1tapevents.Adaptors.AllServicesAdapter;
import com.example.a1tapevents.Adaptors.OrgHomeAdapter;
import com.example.a1tapevents.Fragments.HomeFragment;
import com.example.a1tapevents.models.BookingModel;
import com.example.a1tapevents.models.OrganizerModel;
import com.example.a1tapevents.utils.SpaceItemDecoration;
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

import java.util.ArrayList;
import java.util.List;

public class OrganizerHome extends AppCompatActivity {

    RecyclerView recyclerview_orghome;
    RelativeLayout mainLayout;
    ImageView profile;

    FirebaseFirestore db;
    OrgHomeAdapter adapter;
    List<BookingModel> bookingModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home2);

        init();
        onClickFunctions();
        retrieverFirebase();
    }

    private void onClickFunctions() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerHome.this,profilelogin.class);
                startActivity(intent);
            }
        });
    }

    private void retrieverFirebase() {

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currUser.getEmail();

        db.collection("organizer")
                .whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();

                            //Setting SnapshotListener
                            db.collection("organizer").document(documentID).collection("bookings")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if(error != null){
                                                Snackbar.make(mainLayout,error.getMessage(),Snackbar.LENGTH_LONG).show();
                                                return;
                                            }

                                            for(DocumentChange dc : value.getDocumentChanges()){
                                                if(dc.getType() == DocumentChange.Type.ADDED){

                                                    bookingModels.add(dc.getDocument().toObject(BookingModel.class));
                                                }
                                                if (dc.getType() == DocumentChange.Type.REMOVED) {
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                                if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                                adapter.notifyDataSetChanged();
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

        //ButterKnife.bind(this);
        recyclerview_orghome =findViewById(R.id.orghome_recycler);
        mainLayout = findViewById(R.id.orghome_layout);
        profile = findViewById(R.id.orgprofile);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview_orghome.setLayoutManager(linearLayoutManager);
        recyclerview_orghome.setHasFixedSize(true);
        recyclerview_orghome.addItemDecoration(new SpaceItemDecoration());
        bookingModels = new ArrayList<>();
        adapter = new OrgHomeAdapter(this,bookingModels);
        recyclerview_orghome.setAdapter(adapter);
        db= FirebaseFirestore.getInstance();

    }


}
package com.example.a1tapevents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a1tapevents.Adaptors.AllServicesAdapter;
import com.example.a1tapevents.Adaptors.CartAdapter;
import com.example.a1tapevents.Fragments.HomeFragment;
import com.example.a1tapevents.models.CartModel;
import com.example.a1tapevents.models.OrganizerModel;
import com.example.a1tapevents.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllServices extends AppCompatActivity implements AllServicesAdapter.OnItemClickListener {
    public static final String TEXT_TO_SEND = "com.example.a1tapevents.TEXT_TO_SEND";

    RecyclerView recyclerview_service;
    RelativeLayout mainLayout;
    TextView title;

    FirebaseFirestore db;
    AllServicesAdapter adapter;
    List<OrganizerModel> organizerModelList;

    String string_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services);

        init();
        retrieverFirebase();
    }

    private void retrieverFirebase() {

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currUser.getUid();

        db.collection("organizer")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Snackbar.make(mainLayout,error.getMessage(),Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                OrganizerModel om = dc.getDocument().toObject(OrganizerModel.class);
                                if(om.getCategory().equals(string_title))

                                organizerModelList.add(dc.getDocument().toObject(OrganizerModel.class));
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

    private void init(){

        //ButterKnife.bind(this);
        Intent intent = getIntent();
        string_title = intent.getStringExtra(HomeFragment.TEXT_TO_SEND);
        recyclerview_service =findViewById(R.id.allservices_recycler);
        mainLayout = findViewById(R.id.allservices_layout);
        title = findViewById(R.id.allservices_title);
        title.setText(string_title);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview_service.setLayoutManager(linearLayoutManager);
        recyclerview_service.setHasFixedSize(true);
        recyclerview_service.addItemDecoration(new SpaceItemDecoration());
        organizerModelList = new ArrayList<>();
        adapter = new AllServicesAdapter(string_title,this,organizerModelList,this);
        recyclerview_service.setAdapter(adapter);
        db= FirebaseFirestore.getInstance();

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,Service.class);
        intent.putExtra(TEXT_TO_SEND,organizerModelList.get(position).getName());
        startActivity(intent);
    }
}
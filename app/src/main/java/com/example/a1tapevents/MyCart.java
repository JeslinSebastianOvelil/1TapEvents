package com.example.a1tapevents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.a1tapevents.Adaptors.CartAdapter;
import com.example.a1tapevents.listeners.ICartLoadListener;
import com.example.a1tapevents.models.CartModel;
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

public class MyCart extends AppCompatActivity implements ICartLoadListener {
    @BindView(R.id.mycart_recycler)
    RecyclerView recyclerview_cart;
    @BindView(R.id.mycart_layout)
    RelativeLayout mainLayout;

    FirebaseFirestore db;
    CartAdapter adapter;
    List<CartModel> cartmodels;
    ICartLoadListener cartLoadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        init();
        loadItemFromFirebase();
    }

    private void loadItemFromFirebase() {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currUser.getUid();

        db.collection("users").document(uid).collection("cart")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error != null){
                                    Snackbar.make(mainLayout,error.getMessage(),Snackbar.LENGTH_LONG).show();
                                    return;
                                }

                                for(DocumentChange dc : value.getDocumentChanges()){
                                    if(dc.getType() == DocumentChange.Type.ADDED){
                                        cartmodels.add(dc.getDocument().toObject(CartModel.class));
                                    }
                                    if (dc.getType() == DocumentChange.Type.REMOVED) {
                                        finish();
                                        startActivity(getIntent());
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                cartLoadListener.onCartLoadSuccess(cartmodels);
                            }
                        });
    }

    private void init(){
        ButterKnife.bind(this);
        cartLoadListener = this;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview_cart.setLayoutManager(linearLayoutManager);
        recyclerview_cart.setHasFixedSize(true);
        recyclerview_cart.addItemDecoration(new SpaceItemDecoration());
        cartmodels = new ArrayList<>();
        adapter = new CartAdapter(this, cartmodels);
        recyclerview_cart.setAdapter(adapter);
        db= FirebaseFirestore.getInstance();

    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
    }

    @Override
    public void onCartLoadFailure(String message) {
    }

}
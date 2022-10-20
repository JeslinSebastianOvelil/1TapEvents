package com.example.a1tapevents.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a1tapevents.Adaptors.BookingAdapter;
import com.example.a1tapevents.Adaptors.CartAdapter;
import com.example.a1tapevents.R;
import com.example.a1tapevents.models.BookingModel;
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

import butterknife.BindView;

public class BookingsFragment extends Fragment {

    private static final String TAG = "BookingsFragment";
    Context context;
    private ArrayList<CartModel> bookingModelList;
    private FirebaseFirestore db;
    private BookingAdapter adapter;

    public BookingsFragment(){}
    public BookingsFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        // Inflate the layout for this fragment


        RecyclerView recyclerView = view.findViewById(R.id.mybooking_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpaceItemDecoration());
        bookingModelList = new ArrayList<>();
        adapter = new BookingAdapter(this.getContext(), bookingModelList);
        recyclerView.setAdapter(adapter);
        db= FirebaseFirestore.getInstance();

        retrievefromFirestore();

        return view;
    }

    private void retrievefromFirestore() {

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currUser.getUid();

        db.collection("users").document(uid).collection("bookings")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.d(TAG, "onEvent: Error in Listening......");
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                bookingModelList.add(dc.getDocument().toObject(CartModel.class));
                            }
                            if (dc.getType() == DocumentChange.Type.REMOVED) {

                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }


}
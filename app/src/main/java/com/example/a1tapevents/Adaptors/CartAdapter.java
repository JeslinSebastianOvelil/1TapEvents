package com.example.a1tapevents.Adaptors;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1tapevents.R;
import com.example.a1tapevents.models.CartModel;
import com.example.a1tapevents.models.OrganizerModel;
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

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemViewHolder> {

    private Context context;
    private List<CartModel> cartModelList;
    FirebaseFirestore db;
    FirebaseUser currUser;
    String uid;

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //Glide.with(context)
                //.load(itemModelList.get(position).getImage())
               // .into(holder.itemImageView);
        holder.itemOrganizer.setText(new StringBuilder().append(cartModelList.get(position).getOrganizer()));
        holder.itemContact.setText(new StringBuilder().append(cartModelList.get(position).getContact()));
        holder.itemDate.setText(new StringBuilder().append(cartModelList.get(position).getDate()));
        holder.itemTime.setText(new StringBuilder().append(cartModelList.get(position).getTime()));
        holder.itemPrice.setText(new StringBuilder("Rs").append(String.valueOf(cartModelList.get(position).getPrice())));
        holder.itemDelete.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Delete item")
                    .setMessage("Do you want to delete this item from cart?")
                    .setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss())
                    .setPositiveButton("OK", (dialog12, which) -> {

                        notifyItemRemoved(position);
                        deletefromFirestore(cartModelList.get(position));
                        dialog12.dismiss();
                    }).create();
            dialog.show();

        });
    }

    private void deletefromFirestore(CartModel cartModel) {
        db = FirebaseFirestore.getInstance();
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currUser.getUid();

        db.collection("users").document(uid).collection("cart").
                whereEqualTo("organizer",cartModel.getOrganizer())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("users").document(uid).collection("cart").document(documentID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: Item Deleted......");

                                            updateFirebase(cartModel);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: "+e);
                                        }
                                    });
                        }
                        else{
                            Log.d(TAG, "onComplete: Error.....");
                        }
                    }
                });

    }

    private void updateFirebase(CartModel cartModel) {
        db.collection("organizer").document(cartModel.getOrganizer()).collection("bookings")
                .whereEqualTo("email",currUser.getEmail()).whereEqualTo("date",cartModel.getDate())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();

                            db.collection("organizer").document(cartModel.getOrganizer()).collection("bookings").document(documentID)
                                    .update("status", "Deleted")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        }

                        else{
                            Log.d(TAG, "onComplete: Error.....");
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_organizer)
        TextView itemOrganizer;

        @BindView(R.id.item_delete)
        ImageView itemDelete;

        @BindView(R.id.item_whatsapp)
        ImageView itemWhatsapp;

        @BindView(R.id.item_contact)
        TextView itemContact;

        @BindView(R.id.item_date)
        TextView itemDate;

        @BindView(R.id.item_time)
        TextView itemTime;

        @BindView(R.id.item_price)
        TextView itemPrice;

        private Unbinder unbinder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
        }
    }
}

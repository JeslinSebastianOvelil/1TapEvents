package com.example.a1tapevents.Adaptors;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ItemViewHolder> {

    private Context context;
    private List<CartModel> cartModelList;
    FirebaseFirestore db;
    FirebaseUser currUser;

    public BookingAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.booking_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        holder.Organizer.setText(new StringBuilder().append(cartModelList.get(position).getOrganizer()));
        holder.Contact.setText(new StringBuilder().append(cartModelList.get(position).getContact()));
        holder.Date.setText(new StringBuilder().append(cartModelList.get(position).getDate()));
        holder.Time.setText(new StringBuilder().append(cartModelList.get(position).getTime()));
        holder.Price.setText(new StringBuilder("Rs").append(String.valueOf(cartModelList.get(position).getPrice())));
        holder.Delete.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Delete item")
                    .setMessage("Do you want to cancel your booking? ")
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
        String uid = currUser.getUid();

        db.collection("users").document(uid).collection("bookings").
                whereEqualTo("organizer",cartModel.getOrganizer())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("users").document(uid).collection("bookings").document(documentID)
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
                                    .update("status", "Cancelled")
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

        @BindView(R.id.booking_organizer)
        TextView Organizer;

        @BindView(R.id.booking_delete)
        ImageView Delete;

        @BindView(R.id.booking_googlepay)
        ImageView gpay_logo;

        @BindView(R.id.booking_contact)
        TextView Contact;

        @BindView(R.id.booking_date)
        TextView Date;

        @BindView(R.id.booking_time)
        TextView Time;

        @BindView(R.id.booking_price)
        TextView Price;

        private Unbinder unbinder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
        }
    }
}

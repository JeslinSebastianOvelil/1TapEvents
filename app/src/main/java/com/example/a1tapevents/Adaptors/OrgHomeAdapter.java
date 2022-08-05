package com.example.a1tapevents.Adaptors;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1tapevents.R;
import com.example.a1tapevents.models.BookingModel;
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

public class OrgHomeAdapter extends RecyclerView.Adapter<OrgHomeAdapter.ItemViewHolder> {

    private Context context;
    private List<BookingModel> bookingModelList;
    FirebaseFirestore db;
    FirebaseUser currUser;

    public OrgHomeAdapter(Context context, List<BookingModel> bookingModelList) {
            this.context = context;
            this.bookingModelList = bookingModelList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.org_bookings_layout,parent,false));
            }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

            holder.username.setText(new StringBuilder().append(bookingModelList.get(position).getUsername()));
            holder.contact.setText(new StringBuilder().append(bookingModelList.get(position).getContact()));
            holder.email.setText(new StringBuilder().append(bookingModelList.get(position).getEmail()));
            holder.date.setText(new StringBuilder().append(bookingModelList.get(position).getDate()));
            holder.time.setText(new StringBuilder().append(bookingModelList.get(position).getTime()));
            holder.location.setText(new StringBuilder().append(bookingModelList.get(position).getLocation()));
            holder.status.setText(new StringBuilder().append(bookingModelList.get(position).getStatus()));
            holder.confirm.setOnClickListener(v -> {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Confirm order?")
                        .setMessage("Do you want to confirm this order?")
                        .setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss())
                        .setPositiveButton("OK", (dialog12, which) -> {

                            updateFirestore(bookingModelList.get(position),"Confirmed");
                            dialog12.dismiss();
                        }).create();
                dialog.show();

            });
            holder.decline.setOnClickListener(v -> {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Decline order")
                        .setMessage("Do you want to decline this order?")
                        .setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss())
                        .setPositiveButton("OK", (dialog12, which) -> {

                            updateFirestore(bookingModelList.get(position),"Declined");
                            dialog12.dismiss();
                        }).create();
                dialog.show();

            });
    }

    private void updateFirestore(BookingModel bookingModel,String action) {
            db = FirebaseFirestore.getInstance();
            currUser = FirebaseAuth.getInstance().getCurrentUser();


            db.collection("organizer").document(bookingModel.getOrganizer()).collection("bookings")
                    .whereEqualTo("username",bookingModel.getUsername())
                    .whereEqualTo("date",bookingModel.getDate())
                    .whereEqualTo("time",bookingModel.getTime())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && !task.getResult().isEmpty()){
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String documentID = documentSnapshot.getId();

                                //updating the booking status
                                db.collection("organizer").document(bookingModel.getOrganizer()).collection("bookings").document(documentID)
                                        .update("status", action)
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

            final String[] documentID = new String[1];

            //Moving the order from user's cart to bookings
            if(action.equals("Confirmed")){
                db.collection("users").document(bookingModel.getUserid()).collection("cart")
                        .whereEqualTo("organizer",bookingModel.getOrganizer())
                        .whereEqualTo("date",bookingModel.getDate())
                        .whereEqualTo("time",bookingModel.getTime())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && !task.getResult().isEmpty()){
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    documentID[0] = documentSnapshot.getId();
                                    CartModel cartModel = documentSnapshot.toObject(CartModel.class);
                                    //Writing to bookings
                                    db.collection("users").document(bookingModel.getUserid()).collection("bookings").document().set(cartModel);
                                    //deleting the order from user's cart
                                    db.collection("users").document(bookingModel.getUserid()).collection("cart").document(documentID[0])
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "onSuccess: Item Deleted......");

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

    }

    @Override
    public int getItemCount() {
            return bookingModelList.size();
            }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.orghome_name)
        TextView username;

        @BindView(R.id.orghome_contact)
        TextView contact;

        @BindView(R.id.orghome_email)
        TextView email;

        @BindView(R.id.orghome_date)
        TextView date;

        @BindView(R.id.orghome_time)
        TextView time;

        @BindView(R.id.orghome_location)
        TextView location;

        @BindView(R.id.orghome_status)
        TextView status;
        @BindView(R.id.orghome_confirm)
        Button confirm;

        @BindView(R.id.orghome_decline)
        Button decline;

        private Unbinder unbinder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
        }
    }
}

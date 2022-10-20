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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1tapevents.AllServices;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllServicesAdapter extends RecyclerView.Adapter<AllServicesAdapter.ItemViewHolder>{

    public String category;

    private Context context;
    private List<OrganizerModel> organizerModels;

    OnItemClickListener mitemClickListener;

    public AllServicesAdapter(String category, Context context, List<OrganizerModel> organizerModels,OnItemClickListener itemClickListener) {
        this.category = category;
        this.context = context;
        this.organizerModels = organizerModels;
        this.mitemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public AllServicesAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllServicesAdapter.ItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.service_layout,parent,false),mitemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AllServicesAdapter.ItemViewHolder holder, int position) {


            holder.name.setText(new StringBuilder().append(organizerModels.get(position).getName()));
            holder.price.setText(new StringBuilder("Rs").append(String.valueOf(organizerModels.get(position).getPrice())));

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference;
            String photourl = organizerModels.get(position).getUrl();

            storageReference = storage.getReference().child(photourl);
            try {
                File localfile = File.createTempFile("images","jpeg");
                storageReference.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                holder.photo.setImageBitmap(bitmap);
                                holder.progressBar.setVisibility(View.GONE);
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

    @Override
    public int getItemCount() {
        return organizerModels.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.allservice_pb)
        public ProgressBar progressBar;

        @BindView(R.id.allservices_pic)
        ImageView photo;

        @BindView(R.id.allservices_name)
        TextView name;

        @BindView(R.id.allservices_price)
        TextView price;

        OnItemClickListener itemClickListener;

        private Unbinder unbinder;

        public ItemViewHolder(@NonNull View itemView,OnItemClickListener itemClickListener) {
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(getAdapterPosition());
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}

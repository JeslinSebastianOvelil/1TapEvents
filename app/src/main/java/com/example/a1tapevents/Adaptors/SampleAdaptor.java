package com.example.a1tapevents.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1tapevents.R;

public class SampleAdaptor extends RecyclerView.Adapter<SampleAdaptor.ViewHoder> {
    @NonNull
    @Override
    public SampleAdaptor.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_card_layout,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SampleAdaptor.ViewHoder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return
                5;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

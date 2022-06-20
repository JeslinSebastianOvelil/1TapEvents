package com.example.a1tapevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.a1tapevents.Adaptors.SampleAdaptor;

public class catering_service extends AppCompatActivity {

    private RecyclerView listVal;
    private RecyclerView.LayoutManager linearLayoutManager;
    private SampleAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering_service);

        init();
    }

    private void init() {
        listVal = findViewById(R.id.sample_recycler);
        linearLayoutManager = new LinearLayoutManager(catering_service.this,RecyclerView.HORIZONTAL,false);
        adaptor = new SampleAdaptor();

        listVal.setLayoutManager(linearLayoutManager);
        listVal.setAdapter(adaptor);
    }
}
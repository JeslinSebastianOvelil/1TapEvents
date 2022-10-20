package com.example.a1tapevents.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a1tapevents.AllServices;
import com.example.a1tapevents.R;
import com.example.a1tapevents.Service;

public class HomeFragment extends Fragment {

    public static final String TEXT_TO_SEND = "com.example.a1tapevents.TEXT_TO_SEND";
    String textToSend;

    Context context;
    private ImageView venues;
    private ImageView videography;
    private ImageView caterings;
    private ImageView transport;
    private ImageView cultureprog;
    private ImageView soundlight;
    private ImageView decoration;

    public HomeFragment(){
    }
    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        venues = (ImageView) view.findViewById(R.id.venue_homepage);
        videography = (ImageView) view.findViewById(R.id.videography_homepage);
        caterings = (ImageView) view.findViewById(R.id.cateringservice_homepage);
        transport = (ImageView) view.findViewById(R.id.transport_homepage);
        cultureprog = (ImageView) view.findViewById(R.id.culture_homepage);
        decoration = (ImageView) view.findViewById(R.id.decorations_homepage);
        soundlight = (ImageView)  view.findViewById(R.id.soundlight_homepage);

        venues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend ="Venues";
                gotoActivity();
            }
        });
        videography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend ="Photography & Videography";
                gotoActivity();
            }
        });
        caterings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend ="Catering";
                gotoActivity();
            }
        });
        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend ="Transportation";
                gotoActivity();
            }
        });
        soundlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend ="Light & Sound";
                gotoActivity();
            }
        });
        cultureprog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend ="Cultural Programs";
                gotoActivity();
            }
        });
        decoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSend ="Decoration";
                gotoActivity();
            }
        });
        // Inflate the layout for this fragment
        return view;

    }

    private void gotoActivity() {
        Intent intent = new Intent(getActivity(), AllServices.class);
        intent.putExtra(TEXT_TO_SEND,textToSend);
        startActivity(intent);
    }
}
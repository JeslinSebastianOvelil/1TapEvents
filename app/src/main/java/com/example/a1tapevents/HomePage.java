package com.example.a1tapevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.a1tapevents.Fragments.AboutUsFragment;
import com.example.a1tapevents.Fragments.BookingsFragment;
import com.example.a1tapevents.Fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private FloatingActionButton cart;
    private BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment;
    BookingsFragment bookingsFragment;
    AboutUsFragment aboutUsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        init();
        onClickFunctions();
    }
    private void init() {
//

        cart = findViewById(R.id.btn_home_cart);
        bottomNavigationView = findViewById(R.id.bottom_nav_home);

        homeFragment = new HomeFragment(getApplicationContext());
        bookingsFragment = new BookingsFragment(getApplicationContext());
        aboutUsFragment = new AboutUsFragment(getApplicationContext());
    }

    private void onClickFunctions() {
        Log.v("init","1");

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, MyCart.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(HomePage.this);
        bottomNavigationView.setSelectedItemId(R.id.home);
}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, homeFragment).commit();
                return true;
            case R.id.bookings:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, bookingsFragment).commit();
                return true;
            case R.id.about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, aboutUsFragment).commit();
                return true;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, homeFragment).commit();
                return true;
        }
    }
}

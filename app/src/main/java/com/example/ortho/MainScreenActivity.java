package com.example.ortho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainScreenActivity extends AppCompatActivity implements ExpenseFragment.OnFragmentInteractionListener,BankFragment.OnFragmentInteractionListener,DebtsFragment.OnFragmentInteractionListener, DatePickerDialog.OnDateSetListener {

    private BottomNavigationView bottomNavigationView;

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drawerLayout = findViewById(R.id.drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.light_green));

        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.drawer_account){

                    Intent intent = new Intent(MainScreenActivity.this,AccountActivity.class);

                    startActivity(intent);
                }

                if (menuItem.getItemId()==R.id.drawer_feedback){

                    Intent intent = new Intent(MainScreenActivity.this,FeedBackActivity.class);

                    startActivity(intent);
                }

                if(menuItem.getItemId()==R.id.signout){

                    FirebaseAuth.getInstance().signOut();

                    finish();

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                    startActivity(intent);
                }

                if(menuItem.getItemId()==R.id.drawer_about_us){

                    Intent intent = new Intent(MainScreenActivity.this,AboutThisAppActivity.class);

                    startActivity(intent);
                }


                return true;
            }
        });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);








        bottomNavigationView = findViewById(R.id.bottom_navigationId);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,

                selectedFragment).commit();



    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    BottomNavigationView.OnNavigationItemSelectedListener  navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;

                    if(menuItem.getItemId()==R.id.navigation_home){

                        selectedFragment = new HomeFragment();

                    }else if(menuItem.getItemId()==R.id.navigation_calender){

                        selectedFragment = new CalenderFragment();

                    }else if(menuItem.getItemId()==R.id.navigation_wishList){

                        selectedFragment = new WishListFragment();

                    }else if(menuItem.getItemId()==R.id.navigation_tourPlan){

                        selectedFragment = new TourFragment();

                    }else if(menuItem.getItemId()==R.id.navigation_analytics){

                        selectedFragment = new AnalyticsFragment();

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,

                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainScreenActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}

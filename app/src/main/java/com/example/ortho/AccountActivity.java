package com.example.ortho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;

    private Button changePasswordButton,saveButton;

    private Spinner genderSpinner;

    private TextView emailTextview,genderView;

    private ImageView accountImageview;

    AccountInfo accountInfo = new AccountInfo();

    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String []genders = {"Male", "Female"};

    private int [] images = {R.drawable.boy,R.drawable.girl};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();

        changePasswordButton = findViewById(R.id.change_Password_button);

        genderSpinner  = findViewById(R.id.genderSpinnerId);

        emailTextview = findViewById(R.id.show_email_id);

        accountImageview = findViewById(R.id.account_image_id);

        saveButton  = findViewById(R.id.save_account_button);

        genderView = findViewById(R.id.show_gender);

        emailTextview.setText("Email: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("accountInfo");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AccountInfo accountInfo = dataSnapshot.getValue(AccountInfo.class);

                try {
                    if(accountInfo.getGender().equals("Male")){
                        accountImageview.setImageResource(R.drawable.boy);
                        genderView.setText("Gender: Male");
                    }else{

                        accountImageview.setImageResource(R.drawable.girl);
                        genderView.setText("Gender: Female");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.addbalance_spinner,R.id.add_balance_spinner_layout_textview,genders);

        genderSpinner.setAdapter(arrayAdapter);

        genderSpinner.setOnItemSelectedListener(this);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this,ChangePasswordActivity.class);

                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("accountInfo");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AccountInfo accountInfo1 = dataSnapshot.getValue(AccountInfo.class);

                        accountInfo.setEmail(accountInfo1.getEmail());

                        databaseReference.setValue(accountInfo);

                        Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_SHORT).show();

                        finish();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                try {
                    accountInfo.setGender(genders[position]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {



    }


}

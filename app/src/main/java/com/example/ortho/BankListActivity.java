package com.example.ortho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class BankListActivity extends AppCompatActivity {


    private ListView bankList;

    private DatabaseReference databaseReference;

    private FirebaseUser user;

    private String uid;

    private List<BankAccountInfo> bankAccountInfoList;

    private BankListAdapter bankListAdapter;

    private TextView textView;

    private GifImageView gifImageView;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);

        bankList = findViewById(R.id.bank_list_listview);

        bankAccountInfoList = new ArrayList<BankAccountInfo>();

        bankListAdapter = new BankListAdapter(this,bankAccountInfoList,getSupportFragmentManager());

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        textView = findViewById(R.id.no_bank_id);

        gifImageView = findViewById(R.id.no_bank_image);

        progressBar = findViewById(R.id.bank_loading_progressbar);

        progressBar.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("Bank Accounts");
    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bankAccountInfoList.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    BankAccountInfo bankAccountInfo1 = dataSnapshot1.getValue(BankAccountInfo.class);
                    bankAccountInfoList.add(bankAccountInfo1);
                }

                progressBar.setVisibility(View.GONE);

                if(bankAccountInfoList.size()==0){

                    textView.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.VISIBLE);
                }else {

                    textView.setVisibility(View.GONE);
                    gifImageView.setVisibility(View.GONE);
                }

                bankList.setAdapter(bankListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        super.onStart();
    }
}

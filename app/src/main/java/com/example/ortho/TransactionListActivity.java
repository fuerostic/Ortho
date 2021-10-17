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
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class TransactionListActivity extends AppCompatActivity {

    private ListView transactionList;

    private DatabaseReference databaseReference;

    private FirebaseUser user;

    private String uid;

    private List<TransactionData> transactionDataList;

    private TransactionActivityAdapter transactionActivityAdapter;

    private TextView textView;

    private GifImageView gifImageView;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        transactionList = findViewById(R.id.transaction_activity_listview);

        transactionDataList = new ArrayList<TransactionData>();

        transactionActivityAdapter = new TransactionActivityAdapter(this,transactionDataList);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        textView = findViewById(R.id.no_transaction_text_id);

        gifImageView = findViewById(R.id.no_transaction_image);

        progressBar = findViewById(R.id.transaction_loading_progressbar);

        progressBar.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("transactions");


    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                transactionDataList.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    TransactionData transactionData = dataSnapshot1.getValue(TransactionData.class);
                    transactionDataList.add(transactionData);
                }

                if(transactionDataList.size()==0){

                    textView.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                    gifImageView.setVisibility(View.GONE);

                }
                progressBar.setVisibility(View.GONE);

                Collections.sort(transactionDataList);

                transactionList.setAdapter(transactionActivityAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }
}

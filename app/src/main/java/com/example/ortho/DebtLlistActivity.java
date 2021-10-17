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

public class DebtLlistActivity extends AppCompatActivity {

    private ListView debtsListView;

    private DatabaseReference databaseReference;

    private List<DebtsInfo> debtsInfoList;

    private DebtListAdapter debtListAdapter ;

    private FirebaseUser user;

    private TextView textView;

    private ProgressBar progressBar;

    private GifImageView gifImageView;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_llist);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();


        debtsListView = findViewById(R.id.debtListviewId);

        textView = findViewById(R.id.no_debts_id);

        progressBar = findViewById(R.id.debts_loading_progressbar);

        progressBar.setVisibility(View.VISIBLE);

        gifImageView = findViewById(R.id.no_debts_image);

        debtsInfoList = new ArrayList<DebtsInfo>();

        debtListAdapter = new DebtListAdapter(this,debtsInfoList,getSupportFragmentManager());

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("Debts");
    }

    @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                debtsInfoList.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    DebtsInfo debtsInfo1 = dataSnapshot1.getValue(DebtsInfo.class);
                    debtsInfoList.add(debtsInfo1);
                }

                progressBar.setVisibility(View.GONE);

                if(debtsInfoList.size()==0){

                    textView.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                    gifImageView.setVisibility(View.GONE);

                }

                debtsListView.setAdapter(debtListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }
}

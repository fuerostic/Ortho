package com.example.ortho;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TourAddSpendDialog extends AppCompatDialogFragment {

    private EditText spendAmountEditext;

    private TourInfo tourInfo;

    private FirebaseUser user;

    private String uid;

    public TourAddSpendDialog(TourInfo tourInfo) {
        this.tourInfo = tourInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.tour_add_spend_dialog_layout,null);

        spendAmountEditext = view.findViewById(R.id.tour_add_spend_edittext);


        builder.setView(view)
                .setTitle("Spend Amount")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user = FirebaseAuth.getInstance().getCurrentUser();

                        uid = user.getUid();

                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Tour Plan");

                        final double spendAmount;

                        if(spendAmountEditext.getText().toString().trim().equals("")){

                            spendAmountEditext.setError("Amount Required");

                            spendAmountEditext.requestFocus();

                            return;
                        }

                        spendAmount = Double.valueOf(spendAmountEditext.getText().toString());

                        if(spendAmount+tourInfo.getBudgetSpend()<=tourInfo.getBudget()){

                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    db.child(tourInfo.getKey().toString()).child("budgetSpend").setValue(spendAmount + tourInfo.getBudgetSpend());

                                    Toast.makeText(getContext(),"Saved Successfully",Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else{
                            Toast.makeText(getContext(),"Can't Spend more than budget",Toast.LENGTH_SHORT).show();

                        }

                    }
                });



        return builder.create();
    }
}

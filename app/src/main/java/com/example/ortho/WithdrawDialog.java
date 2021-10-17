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

import java.util.Calendar;

public class WithdrawDialog extends AppCompatDialogFragment {

    private EditText withdrawEdittext;

    private BankAccountInfo bankAccountInfo;

    private FirebaseUser user;

    private String uid;

    private DailyData dailyData;

    public WithdrawDialog(BankAccountInfo bankAccountInfo) {
        this.bankAccountInfo = bankAccountInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.withdraw_dialog_layout,null);

        withdrawEdittext = view.findViewById(R.id.withdraw_edittext);

        builder.setView(view)
                .setTitle("Withdraw")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setPositiveButton("Withdraw", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user = FirebaseAuth.getInstance().getCurrentUser();

                        uid  = user.getUid();

                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Bank Accounts");

                        final String key = bankAccountInfo.getKey();

                        if(withdrawEdittext.getText().toString().trim().equals("")){

                            withdrawEdittext.setError("Please Enter Amount");
                            withdrawEdittext.requestFocus();

                            return;
                        }

                        final double amount = Double.valueOf(withdrawEdittext.getText().toString());

                        if(amount>bankAccountInfo.getBankBalance()){

                            withdrawEdittext.setError("Amount is more than balance");
                            withdrawEdittext.requestFocus();

                            return;


                        }else {


                            db.keepSynced(true);

                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    db.child(bankAccountInfo.getKey().toString()).child("bankBalance").setValue(bankAccountInfo.getBankBalance()-amount);

                                    Toast.makeText(getContext(),"Amount Withdrawn Successfully",Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            dailyData = new DailyData(Calendar.getInstance().getTime());

                            final DatabaseReference dateReference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(dailyData.getYear()).child(dailyData.getMonth()).child(dailyData.getDay());

                            dateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild("bankWithdrawn")){

                                        DateInfo dateInfo1 = dataSnapshot.getValue(DateInfo.class);

                                        dateInfo1.setBankWithdrawn(dateInfo1.getBankWithdrawn() + amount);

                                        dateReference.setValue(dateInfo1);
                                    }else {

                                        DateInfo dateInfo1 = new DateInfo();

                                        dateInfo1.setBankWithdrawn(amount);

                                        dateReference.setValue(dateInfo1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                });

        return builder.create();
    }
}

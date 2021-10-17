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

public class DepositDialog extends AppCompatDialogFragment {

    private EditText depositAmountEdittext;

    private BankAccountInfo bankAccountInfo;
    private FirebaseUser user;

    private String uid;

    private DailyData dailyData;

    public DepositDialog(BankAccountInfo bankAccountInfo) {
        this.bankAccountInfo = bankAccountInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.deposit_dialog_layout,null);

        depositAmountEdittext = view.findViewById(R.id.deposit_amount_edittext);

        builder.setView(view)
                .setTitle("Deposit")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Deposit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user = FirebaseAuth.getInstance().getCurrentUser();

                        uid = user.getUid();

                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Bank Accounts");

                        final String key = bankAccountInfo.getKey();

                        if(depositAmountEdittext.getText().toString().trim().equals("")){

                            depositAmountEdittext.setError("Amount Required");
                            depositAmountEdittext.requestFocus();

                            return;
                        }

                        final double amount = Double.valueOf(depositAmountEdittext.getText().toString());

                        if(amount<0){
                            Toast.makeText(getContext(),"Cant Deposit negative amount",Toast.LENGTH_SHORT).show();


                        }else {

                            db.keepSynced(true);

                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    db.child(bankAccountInfo.getKey().toString()).child("bankBalance").setValue(amount + bankAccountInfo.getBankBalance());


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
                                    if(dataSnapshot.hasChild("bankDeposit")){

                                        DateInfo dateInfo1 = dataSnapshot.getValue(DateInfo.class);

                                        dateInfo1.setBankDeposit(dateInfo1.getBankDeposit() + amount);

                                        dateReference.setValue(dateInfo1);
                                    }else {

                                        DateInfo dateInfo1 = new DateInfo();

                                        dateInfo1.setBankDeposit(amount);

                                        dateReference.setValue(dateInfo1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            Toast.makeText(getContext(),"Amount Deposited",Toast.LENGTH_SHORT).show();

                        }



                    }
                });






        return builder.create();
    }
}

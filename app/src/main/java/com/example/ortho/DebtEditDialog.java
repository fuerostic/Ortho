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

public class DebtEditDialog extends AppCompatDialogFragment {

    private EditText debtAmountEdittext;

    private DebtsInfo debtsInfo;

    private DailyData dailyData;

    public DebtEditDialog(DebtsInfo debtsInfo) {
        this.debtsInfo = debtsInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.debt_edit_dialog,null);

        final String title, buttonText,child;

        dailyData = new DailyData(Calendar.getInstance().getTime());

        debtAmountEdittext = view.findViewById(R.id.debt_edit_dialog_amount_editext);

        if(debtsInfo.getDebtType().toString().equals("Given")){
            title = "Receive Debt";
            buttonText = "Receive";
            child = "debtReceived";
        }else {

            title = "Pay Debt";
            buttonText = "Pay";
            child = "debtPay";
        }


        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        String uid = user.getUid();

                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Debts");

                        final String key = debtsInfo.getKey();


                        if(debtAmountEdittext.getText().toString().trim().equals("")){

                            debtAmountEdittext.setError("Amount Required");

                            debtAmountEdittext.requestFocus();

                            return;
                        }

                        final double amount = Double.valueOf(debtAmountEdittext.getText().toString());

                        if(amount<0 || (debtsInfo.getAmount()<=debtsInfo.getDebtGot())){
                            Toast.makeText(getContext(),"Invalid Value",Toast.LENGTH_SHORT).show();


                        }else {

                            db.keepSynced(true);

                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    db.child(debtsInfo.getKey().toString()).child("debtGot").setValue(debtsInfo.getDebtGot()+amount);

                                    if(debtsInfo.getAmount()-amount<=0){

                                        Toast.makeText(getContext(),"Debt Cleared",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(),"Debt saved",Toast.LENGTH_SHORT).show();

                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            final DatabaseReference dateReference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(dailyData.getYear()).child(dailyData.getMonth()).child(dailyData.getDay());

                            dateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(child)){

                                        DateInfo dateInfo1 = dataSnapshot.getValue(DateInfo.class);

                                        if(child.equals("debtReceived")){

                                            dateInfo1.setDebtReceived(dateInfo1.getDebtReceived() + amount);
                                        }else if(child.equals("debtPay")){

                                            dateInfo1.setDebtPay(dateInfo1.getDebtPay() + amount);
                                        }

                                        dateReference.setValue(dateInfo1);
                                    }else {

                                        DateInfo dateInfo1 = new DateInfo();

                                        if(child.equals("debtReceived")){

                                            dateInfo1.setDebtReceived(amount);
                                        }else if(child.equals("debtPay")){

                                            dateInfo1.setDebtPay( amount);
                                        }

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

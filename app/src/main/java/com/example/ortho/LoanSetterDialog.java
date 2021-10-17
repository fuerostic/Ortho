package com.example.ortho;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoanSetterDialog extends AppCompatDialogFragment{

    private BankAccountInfo bankAccountInfo;

    private EditText loanAmountEdittext,loanDueDateEdittext;

    private FirebaseUser user;

    private String uid;

    public LoanSetterDialog(BankAccountInfo bankAccountInfo) {
        this.bankAccountInfo = bankAccountInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.loan_setter_dialog,null);

        loanAmountEdittext = view.findViewById(R.id.loan_amount_set);

        loanDueDateEdittext = view.findViewById(R.id.loan_due_date_set);

        builder.setView(view)
                .setTitle("Edit Loan")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user = FirebaseAuth.getInstance().getCurrentUser();

                        uid = user.getUid();

                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Bank Accounts");

                        final String key = bankAccountInfo.getKey();

                        if(loanAmountEdittext.getText().toString().trim().equals("")){

                            loanAmountEdittext.setError("Amount Required");
                            loanAmountEdittext.requestFocus();

                            return;
                        }

                        if(loanDueDateEdittext.getText().toString().equals("")){

                            loanDueDateEdittext.setError("Date Required");
                            loanDueDateEdittext.requestFocus();

                            return;
                        }

                        final double amount = Double.valueOf(loanAmountEdittext.getText().toString());

                        final String dueDate = loanDueDateEdittext.getText().toString().trim();

                        if(dueDate.equals("")){

                            Toast.makeText(getContext(),"You must provide due date",Toast.LENGTH_SHORT).show();
                        }else {

                            db.keepSynced(true);

                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    db.child(bankAccountInfo.getKey().toString()).child("loan").setValue(amount);
                                    db.child(bankAccountInfo.getKey().toString()).child("loanDueDate").setValue(dueDate);

                                    Toast.makeText(getContext(),"Loan Saved",Toast.LENGTH_SHORT).show();

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

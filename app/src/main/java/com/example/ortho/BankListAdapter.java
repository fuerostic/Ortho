package com.example.ortho;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BankListAdapter extends ArrayAdapter<BankAccountInfo> implements View.OnClickListener {


    private Activity context;

    private List<BankAccountInfo> bankAccountInfoList;

    private FragmentManager fragmentManager;

    public BankListAdapter(Activity context, List<BankAccountInfo> bankAccountInfoList,FragmentManager fragmentManager1) {
        super(context, R.layout.bank_description_layout, bankAccountInfoList);
        this.context =  context;
        this.bankAccountInfoList = bankAccountInfoList;
        this.fragmentManager = fragmentManager1;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();

        final View view = layoutInflater.inflate(R.layout.bank_description_layout,null,false);


        BankAccountInfo bankAccountInfo = bankAccountInfoList.get(position);

        TextView bankName,accountType,loanDueDate,loan,accountBalance,accountNumber;

        Button depositButton,withdrawButton,loanSetterButton;

        ImageButton deleteButton;

        deleteButton = view.findViewById(R.id.bank_account_delete_button);

        deleteButton.setOnClickListener(this);

        depositButton = view.findViewById(R.id.bank_deposit_button);

        withdrawButton = view.findViewById(R.id.bank_withdraw_button);

        loanSetterButton = view.findViewById(R.id.bank_loan_setter_button);

        bankName = view.findViewById(R.id.bank_info_name_textview);

        accountNumber = view.findViewById(R.id.bank_account_number_textview);

        accountBalance = view.findViewById(R.id.bank_account_balance_textview);

        loan = view.findViewById(R.id.bank_loan_amount_textview);

        accountType  = view.findViewById(R.id.bank_account_type_textview);

        loanDueDate = view.findViewById(R.id.bank_loan_due_date);

        accountNumber.setText("Account Number: " +bankAccountInfo.getAccountNumber());
        bankName.setText(bankAccountInfo.getBankName().toUpperCase());
        accountBalance.setText("Account Balance: " +String.valueOf(bankAccountInfo.getBankBalance()));
        loan.setText("Loaan: " + String.valueOf(bankAccountInfo.getLoan()));
        accountType.setText("Account Type: " +bankAccountInfo.getAccountType());
        loanDueDate.setText("Loan Due Date: " +bankAccountInfo.getLoanDueDate());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Delete this bank?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onDeleteItem(position);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });


        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDepositDialog(position);


            }
        });

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithdrawDialog(position);
            }
        });

        loanSetterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoanSetterDialog(position);
            }
        });

        return view;
    }

    private void openLoanSetterDialog(int position) {

        final BankAccountInfo bankAccountInfo = bankAccountInfoList.get(position);

        LoanSetterDialog loanSetterDialog = new LoanSetterDialog(bankAccountInfo);

        loanSetterDialog.show(fragmentManager,"loan dialog");
    }

    private void openWithdrawDialog(int position) {

        final BankAccountInfo bankAccountInfo = bankAccountInfoList.get(position);

        WithdrawDialog withdrawDialog = new WithdrawDialog(bankAccountInfo);

        withdrawDialog.show(fragmentManager,"withdraw dialog");
    }

    private void openDepositDialog(int position) {


        final BankAccountInfo bankAccountInfo = bankAccountInfoList.get(position);

        DepositDialog depositDialog = new DepositDialog(bankAccountInfo);

        depositDialog.show(fragmentManager,"deposit dialog");




    }

    private void onDeleteItem(final int position) {

        final BankAccountInfo bankAccountInfo = bankAccountInfoList.get(position);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Bank Accounts");

        final String key = bankAccountInfo.getKey();

        db.keepSynced(true);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    BankAccountInfo bankAccountInfo1 = dataSnapshot1.getValue(BankAccountInfo.class);

                    //Toast.makeText(getContext(),transactionId + " " + transactionData1.getTransactionId().toString(),Toast.LENGTH_SHORT).show();

                    if(bankAccountInfo1.getKey().equals(key)){

                        db.child(dataSnapshot1.getKey().toString()).removeValue();
                        bankAccountInfoList.remove(position);

                        notifyDataSetChanged();

                        Toast.makeText(getContext(),"item deleted",Toast.LENGTH_SHORT).show();
                        break;


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {


    }
}

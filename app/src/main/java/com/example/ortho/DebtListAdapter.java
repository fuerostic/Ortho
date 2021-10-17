package com.example.ortho;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DebtListAdapter extends ArrayAdapter<DebtsInfo> implements View.OnClickListener {

    private Activity context;

    private List<DebtsInfo> debtsInfoList;

    private FragmentManager fragmentManager;

    public DebtListAdapter(Context context,List<DebtsInfo> debtsInfoList, FragmentManager fragmentManager) {
        super(context,R.layout.debts_history_layout, debtsInfoList);
        this.context = (Activity) context;
        this.debtsInfoList = debtsInfoList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();

        final View view = layoutInflater.inflate(R.layout.debts_history_layout,null,false);


        DebtsInfo debtsInfo = debtsInfoList.get(position);

        TextView name,type,amount,dueDate,issueDate;

        ImageView debtTypeImage;

        Button editButton;

        ImageButton deleteButton;

        name = view.findViewById(R.id.debt_person_name_textview);

        type = view.findViewById(R.id.debt_type_textview);

        amount = view.findViewById(R.id.debt_amount_textview);

        dueDate = view.findViewById(R.id.debt_due_date_textview);

        issueDate = view.findViewById(R.id.debt_date_textview);

        debtTypeImage = view.findViewById(R.id.debts_type_imageViewId);

        editButton = view.findViewById(R.id.debtchangerButton);

        deleteButton = view.findViewById(R.id.debts_history_deleteButtonId);

        name.setText(debtsInfo.getPersonInteracted().toUpperCase());

        type.setText("Debt Type: " + debtsInfo.getDebtType());

        amount.setText("Amount Left: "+ (debtsInfo.getAmount()-debtsInfo.getDebtGot()));

        dueDate.setText("Due Date: " + debtsInfo.getDateDue());

        issueDate.setText("Issue Date: "+ debtsInfo.getDate());

        if(debtsInfo.getAmount()<=debtsInfo.getDebtGot()){

            editButton.setEnabled(false);
        }

        if(debtsInfo.getDebtType().equals("Given")){


            debtTypeImage.setImageResource(R.drawable.ic_file_upload_black_24dp);


            editButton.setText("Receive");


        }else if(debtsInfo.getDebtType().equals("Taken")){


            debtTypeImage.setImageResource(R.drawable.ic_file_download_black_24dp);

            editButton.setText("Pay");
        }


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Delete this debt?");
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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDebtEditDialog(position);

            }
        });


        return view;
    }

    private void openDebtEditDialog(int position) {

        final DebtsInfo debtsInfo = debtsInfoList.get(position);

        DebtEditDialog debtEditDialog = new DebtEditDialog(debtsInfo);

        debtEditDialog.show(fragmentManager,"debt edit dialog");


    }

    private void onDeleteItem(final int position) {

        final DebtsInfo debtsInfo = debtsInfoList.get(position);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Debts");

        final String key = debtsInfo.getKey();

        db.keepSynced(true);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    DebtsInfo debtsInfo1 = dataSnapshot1.getValue(DebtsInfo.class);

                    //Toast.makeText(getContext(),transactionId + " " + transactionData1.getTransactionId().toString(),Toast.LENGTH_SHORT).show();

                    if(debtsInfo1.getKey().equals(key)){

                        db.child(dataSnapshot1.getKey().toString()).removeValue();
                        debtsInfoList.remove(position);

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

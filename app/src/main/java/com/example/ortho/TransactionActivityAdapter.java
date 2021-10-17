package com.example.ortho;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionActivityAdapter extends ArrayAdapter<TransactionData> {

    private Activity context;

    private List<TransactionData> transactionDataList;

    private FirebaseUser user;

    private String uid;

    public TransactionActivityAdapter(Activity context, List<TransactionData> transactionDataList) {
        super(context, R.layout.recent_transaction, transactionDataList);
        this.context =  context;
        this.transactionDataList = transactionDataList;
    }

    @Override
    public int getCount() {
        return transactionDataList.size();
    }

    @Nullable
    @Override
    public TransactionData getItem(int position) {
        return transactionDataList.get(position);
    }




    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.expenselist_child,null,false);


        TransactionData transactionData = transactionDataList.get(position);

        ImageButton imageButton = view.findViewById(R.id.expenseListDeleteButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),"Item selected " + position,Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Delete this transactions?");
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

        TextView date,category,amount,type;

        date = view.findViewById(R.id.expense_list_child_date);

        category = view.findViewById(R.id.expense_list_child_category);

        amount = view.findViewById(R.id.expense_list_child_amount);

        type = view.findViewById(R.id.expense_list_child_type);

        date.setText(transactionData.getDate());
        category.setText(transactionData.getCategory());
        amount.setText(String.valueOf(transactionData.getAmount()) );
        type.setText(transactionData.getType());



        return view;
    }

    public void onDeleteItem(final int position){

        final TransactionData transactionData = transactionDataList.get(position);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("transactions");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {

            Date date = format.parse(transactionData.getDate());
            DailyData dailyData = new DailyData(date);

            if(Integer.parseInt(dailyData.getDay())/10==0){

                String d = "0" + dailyData.getDay();

                dailyData.setDay(d);
            }

            if(Integer.parseInt(dailyData.getMonth())/10==0){

                String m = "0" + dailyData.getMonth();

                dailyData.setMonth(m);
            }

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("expenseInfo").child(dailyData.getYear()).child(dailyData.getMonth());

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ExpenseInfo expenseInfo = dataSnapshot.getValue(ExpenseInfo.class);


                    Log.d("hi", "onDataChange: " +transactionData.getType());


                    if(transactionData.getCategory().equals("Food")){
                        expenseInfo.setFood(expenseInfo.getFood()-transactionData.getAmount());
                    }

                    else if(transactionData.getCategory().equals("Rent")){
                        expenseInfo.setRent(expenseInfo.getRent()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Transportation")){
                        expenseInfo.setTransportation(expenseInfo.getTransportation()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Clothing")){
                        expenseInfo.setClothing(expenseInfo.getClothing()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Communication")){
                        expenseInfo.setCommunication(expenseInfo.getCommunication()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Books")){
                        expenseInfo.setBooks(expenseInfo.getBooks()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Electronics")){
                        expenseInfo.setElectronics(expenseInfo.getElectronics()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Project")){
                        expenseInfo.setProject(expenseInfo.getProject()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Treat")){
                        expenseInfo.setTreat(expenseInfo.getTreat()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Tuition")){
                        expenseInfo.setTuition(expenseInfo.getTuition()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Education")){
                        expenseInfo.setEducation(expenseInfo.getEducation()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Hangout")){
                        expenseInfo.setHangout(expenseInfo.getHangout()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Trip")){
                        expenseInfo.setTrip(expenseInfo.getTrip()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Utilities")){
                        expenseInfo.setUtilities(expenseInfo.getUtilities()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Services")){
                        expenseInfo.setServices(expenseInfo.getServices()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Fees")){
                        expenseInfo.setFees(expenseInfo.getFees()-transactionData.getAmount());
                    }
                    else if(transactionData.getCategory().equals("Tax")){
                        expenseInfo.setTax(expenseInfo.getTax()-transactionData.getAmount());
                    }
                    else {
                        expenseInfo.setOthers(expenseInfo.getOthers()-transactionData.getAmount());
                    }


                    databaseReference.setValue(expenseInfo);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }



        final String transactionId = transactionData.getTransactionId();

        db.keepSynced(true);



        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Toast.makeText(getContext(),"on data change",Toast.LENGTH_SHORT).show();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    TransactionData transactionData1 = dataSnapshot1.getValue(TransactionData.class);

                    //Toast.makeText(getContext(),transactionId + " " + transactionData1.getTransactionId().toString(),Toast.LENGTH_SHORT).show();

                    if(transactionData1.getTransactionId().equals(transactionId)){

                        db.child(dataSnapshot1.getKey().toString()).removeValue();
                        transactionDataList.remove(position);

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



}

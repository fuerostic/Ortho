package com.example.ortho;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<TransactionData> {

    private Activity context;

    private List<TransactionData> transactionDataList;

    public CustomAdapter(Activity context, List<TransactionData> transactionDataList) {
        super(context, R.layout.recent_transaction, transactionDataList);
        this.context =  context;
        this.transactionDataList = transactionDataList;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.recent_transaction,null,false);


        TransactionData transactionData = transactionDataList.get(position);

        TextView date,category,amount,type;

        date = view.findViewById(R.id.recent_transaction_date);

        category = view.findViewById(R.id.recent_transaction_category);

        amount = view.findViewById(R.id.recent_transaction_amount);

        type = view.findViewById(R.id.recent_transaction_type);

        date.setText(transactionData.getDate());
        category.setText(transactionData.getCategory());
        amount.setText(String.valueOf(transactionData.getAmount()) );
        type.setText(transactionData.getType());



        return view;
    }
}

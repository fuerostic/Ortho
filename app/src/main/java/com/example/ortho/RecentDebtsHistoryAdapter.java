package com.example.ortho;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RecentDebtsHistoryAdapter extends ArrayAdapter<DebtsInfo> {

    private Activity context;

    private List<DebtsInfo> debtsInfoList;


    public RecentDebtsHistoryAdapter(Activity context, List<DebtsInfo> debtsInfoList) {
        super(context, R.layout.recent_debt_history_layout, debtsInfoList);
        this.context =  context;
        this.debtsInfoList = debtsInfoList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.recent_debt_history_layout,null,false);

        DebtsInfo debtsInfo = debtsInfoList.get(position);

        TextView dueDate,type,amount;

        dueDate = view.findViewById(R.id.recent_history_due_date);

        type = view.findViewById(R.id.recent_history_debt_type);

        amount = view.findViewById(R.id.recent_history_Debt_amount);

        dueDate.setText("Due date: "+ debtsInfo.getDateDue());

        type.setText(debtsInfo.getDebtType());

        amount.setText(String.valueOf(debtsInfo.getAmount()));

        return view;


    }
}

package com.example.ortho;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CalenderListAdapter extends ArrayAdapter<HeadingInfo> {

    private Activity context;

    private List<HeadingInfo> headingInfoList;

    private FirebaseUser user;

    private String uid;

    private ImageView imageView;

    private TextView heading,value;

    public CalenderListAdapter(Context context,List<HeadingInfo> objects) {
        super(context, R.layout.calender_list_item_layout, objects);

        this.context = (Activity) context;

        this.headingInfoList = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.calender_list_item_layout,null,false);

        imageView = view.findViewById(R.id.calender_item_imageview);

        heading = view.findViewById(R.id.calender_list_item_id);

        value = view.findViewById(R.id.calender_item_value_id);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        HeadingInfo headingInfo = headingInfoList.get(position);



            if(headingInfo.getHeading().equals("deposit")){

                imageView.setImageResource(R.drawable.salary);

                heading.setText("Deposit balance");

                value.setText("+" + headingInfo.getValue());

                value.setTextColor(view.getResources().getColor(R.color.dark_green));

            }else if(headingInfo.getHeading().equals("expense")){

                imageView.setImageResource(R.drawable.tax);

                heading.setText("Expense");

                value.setText("-" + headingInfo.getValue());

                value.setTextColor(view.getResources().getColor(R.color.pomegranate));

            }else  if(headingInfo.getHeading().equals("bankDeposit")){

                imageView.setImageResource(R.drawable.deposit);

                heading.setText("Bank Deposit");

                value.setText("+" + headingInfo.getValue());

                value.setTextColor(view.getResources().getColor(R.color.dark_green));

            }else if(headingInfo.getHeading().equals("bankWithdrawn")){

                imageView.setImageResource(R.drawable.tax);

                heading.setText("Bank Withdrawn");

                value.setText("-" + headingInfo.getValue());

                value.setTextColor(view.getResources().getColor(R.color.pomegranate));

            }else if(headingInfo.getHeading().equals("wishSave")){

                imageView.setImageResource(R.drawable.bill);

                heading.setText("Saving on wish");

                value.setText("+" + headingInfo.getValue());

                value.setTextColor(view.getResources().getColor(R.color.dark_green));

            }else if(headingInfo.getHeading().equals("debtReceived")){

                imageView.setImageResource(R.drawable.refund);

                heading.setText("Receive from debt");

                value.setText("+" + headingInfo.getValue());

                value.setTextColor(view.getResources().getColor(R.color.dark_green));

            }else if(headingInfo.getHeading().equals("debtPaid")){

                imageView.setImageResource(R.drawable.payment);

                heading.setText("Debt Paid");

                value.setText("-" + headingInfo.getValue());

                value.setTextColor(view.getResources().getColor(R.color.pomegranate));

            }







        return view;
    }
}



package com.example.ortho;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class WishListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<WishInfo> wishInfos;
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;
    private FirebaseUser user;

    private String uid;


    public WishListAdapter(Context context, ArrayList<WishInfo> wishInfos,FragmentManager fragmentManager) {
        this.context = context;
        this.wishInfos = wishInfos;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.fragmentManager = fragmentManager;


    }

    @Override
    public int getGroupCount() {
        return wishInfos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return wishInfos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return wishInfos.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = layoutInflater.inflate(R.layout.wishlist_item_layout_header,null);


        }

        WishInfo wishInfo = wishInfos.get(groupPosition);

        ProgressBar progressBar = convertView.findViewById(R.id.wish_header_progressbar_circular_progressbar);

        TextView percentage = convertView.findViewById(R.id.wish_header_progressbar_percentage);

        TextView wishName = convertView.findViewById(R.id.wish_header_name_id);


        progressBar.setProgress((int) ((1-((wishInfo.getWishValue()-wishInfo.getValueGot())/wishInfo.getWishValue())) *100));

        if(wishInfo.getValueGot()>=wishInfo.getWishValue()){

            progressBar.setProgress(100);
        }

        percentage.setText((int) ((1-((wishInfo.getWishValue()-wishInfo.getValueGot())/wishInfo.getWishValue())) *100) + "%");

        wishName.setText(wishInfo.getWishName());


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = layoutInflater.inflate(R.layout.wish_item_layout_body_layout,null);

        }

        WishInfo wishInfo = (WishInfo) getChild(groupPosition,childPosition);

        ImageView imageView = convertView.findViewById(R.id.wish_item_body_imageview);

        TextView wish_value = convertView.findViewById(R.id.wish_item_body_value_text);

        TextView wish_type = convertView.findViewById(R.id.wish_item_body_type_text);

        TextView days_left = convertView.findViewById(R.id.wish_item_body_days_left_text);

        TextView per_day_save = convertView.findViewById(R.id.wish_item_body_save_per_day_text);


        ImageButton editButton = convertView.findViewById(R.id.wish_item_body_edit_button);

        ImageButton addSavingsButton = convertView.findViewById(R.id.wish_item_body_add_button);

        ImageButton deleteButton = convertView.findViewById(R.id.wish_item_body_delete_button);

        if(wishInfo.getWishValue()<=wishInfo.getValueGot()){

            addSavingsButton.setEnabled(false);
        }else {
            addSavingsButton.setEnabled(true);
        }


        String date = wishInfo.getWishDate();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String today = df.format(c);


        int days = getCountOfDays(today,date);

        wishInfo.setDaysLeft(days);

        imageView.setImageResource(wishInfo.getImage());
        wish_value.setText("Value: " + String.valueOf(wishInfo.getWishValue()));
        wish_type.setText("Type: "+ String.valueOf(wishInfo.getWishType()));
        days_left.setText("Days Left: "+ wishInfo.getDaysLeft());
        per_day_save.setText("Per Day Save: " + Math.round(((wishInfo.getWishValue()-wishInfo.getValueGot())/(double)days)*100.0)/100.0);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openEditDialog(groupPosition);
            }
        });

        addSavingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSavingDialog(groupPosition);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do you want to delete Item?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onDeleteItem(groupPosition);
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






        return convertView;
    }

    private void onDeleteItem(final int groupPosition) {

        final WishInfo wishInfo = wishInfos.get(groupPosition);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Wish List");

        final String key = wishInfo.getKey();

        db.keepSynced(true);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    WishInfo wishInfo1 = dataSnapshot1.getValue(WishInfo.class);

                    //Toast.makeText(getContext(),transactionId + " " + transactionData1.getTransactionId().toString(),Toast.LENGTH_SHORT).show();

                    if(wishInfo1.getKey().equals(key)){

                        db.child(dataSnapshot1.getKey().toString()).removeValue();
                        wishInfos.remove(groupPosition);

                        notifyDataSetChanged();

                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openSavingDialog(int groupPosition) {

        WishInfo wishInfo = wishInfos.get(groupPosition);

        AddWishSavingAmount wishSavingAmount = new AddWishSavingAmount(wishInfo);

        wishSavingAmount.show(fragmentManager,"edit add saving dialog");
    }

    private void openEditDialog(int groupPosition) {

        WishInfo wishInfo = wishInfos.get(groupPosition);

        WishEditDialog wishEditDialog = new WishEditDialog(wishInfo);

        wishEditDialog.show(fragmentManager,"edit wish dialog");
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public int getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return (int) dayCount ;
    }
}

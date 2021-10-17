package com.example.ortho;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TourListAdapter extends ArrayAdapter<TourInfo>{


    private Activity context;

    private List<TourInfo> tourInfoList;

    private FragmentManager fragmentManager;

    private FirebaseUser user;

    private String uid;

    public TourListAdapter(Context context,  List<TourInfo> tourInfoList, FragmentManager fragmentManager) {
        super(context,R.layout.tour_list_item_layout, tourInfoList);
        this.context = (Activity) context;
        this.tourInfoList = tourInfoList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();

        final View view = layoutInflater.inflate(R.layout.tour_list_item_layout,null,false);

        TourInfo tourInfo = tourInfoList.get(position);

        TextView tourName,startDate,endDate,balanceLeft,destination;

        ProgressBar balanceProgressBar;

        final ImageButton deleteImageButton;

        ImageButton editButton,addSpendButton;

        tourName = view.findViewById(R.id.tour_name_on_layout);

        startDate = view.findViewById(R.id.tour_from_date_id);

        endDate = view.findViewById(R.id.tour_to_date_id);

        balanceLeft = view.findViewById(R.id.tour_balance_left_id);

        destination = view.findViewById(R.id.tour_destination_id);

        balanceProgressBar = view.findViewById(R.id.tour_budget_progressbarId);

        deleteImageButton = view.findViewById(R.id.tour_delete_button_id);

        editButton = view.findViewById(R.id.tour_edit_buttonId);

        addSpendButton = view.findViewById(R.id.tour_add_expense);

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Delete this tour?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteItem(position);
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

                openEditDialog(position);

            }
        });

        addSpendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAddSpendDialog(position);

            }
        });

        tourName.setText(tourInfo.getTourName());

        startDate.setText("Start Date: "+ tourInfo.getStartDate());

        endDate.setText("End Date: "+ tourInfo.getEndDate());

        destination.setText("Destination: "+ tourInfo.getDestination());

        balanceLeft.setText("Balance Left: "+ String.valueOf(tourInfo.getBudget() - tourInfo.getBudgetSpend()));

        balanceProgressBar.setProgress((int) (((tourInfo.getBudget()-tourInfo.getBudgetSpend())/tourInfo.getBudget()) *100));

        return view;

    }

    private void openAddSpendDialog(int position) {

        final TourInfo tourInfo = tourInfoList.get(position);

        TourAddSpendDialog tourAddSpendDialog = new TourAddSpendDialog(tourInfo);

        tourAddSpendDialog.show(fragmentManager,"add spend dialog");
    }

    private void openEditDialog(int position) {

        final TourInfo tourInfo = tourInfoList.get(position);

        TourEditDialog tourEditDialog = new TourEditDialog(tourInfo);

        tourEditDialog.show(fragmentManager,"tour edit dialog");
    }

    private void deleteItem(final int position) {

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        final TourInfo tourInfo = tourInfoList.get(position);

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Tour Plan");

        final String key = tourInfo.getKey();

        db.keepSynced(true);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    TourInfo tourInfo1 = dataSnapshot1.getValue(TourInfo.class);

                    //Toast.makeText(getContext(),transactionId + " " + transactionData1.getTransactionId().toString(),Toast.LENGTH_SHORT).show();

                    if(tourInfo1.getKey().equals(key)){

                        db.child(dataSnapshot1.getKey().toString()).removeValue();
                        tourInfoList.remove(position);

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

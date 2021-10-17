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

public class AddWishSavingAmount extends AppCompatDialogFragment {

    private EditText amountEditText;

    private WishInfo wishInfo;

    private FirebaseUser user;

    private String uid;

    private DailyData dailyData;


    public AddWishSavingAmount(WishInfo wishInfo) {
        this.wishInfo = wishInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        View view = inflater.inflate(R.layout.add_savings_wish_dialog,null);

        amountEditText = view.findViewById(R.id.add_wish_saving_edittext);




        builder.setView(view)
                .setTitle("Add Saving")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Wish List");

                        if(!amountEditText.getText().toString().trim().equals("")){

                            wishInfo.setValueGot(Double.valueOf(amountEditText.getText().toString().trim()));

                        }else {

                            amountEditText.setError("Amount Required");
                            amountEditText.requestFocus();

                            return;
                        }

                        dailyData = new DailyData(Calendar.getInstance().getTime());

                        final DatabaseReference dateReference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(dailyData.getYear()).child(dailyData.getMonth()).child(dailyData.getDay());

                        dateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("wishSave")){

                                    DateInfo dateInfo1 = dataSnapshot.getValue(DateInfo.class);

                                    dateInfo1.setWishSave(dateInfo1.getWishSave() + wishInfo.getValueGot());

                                    dateReference.setValue(dateInfo1);
                                }else {

                                    DateInfo dateInfo1 = new DateInfo();

                                    dateInfo1.setWishSave(wishInfo.getValueGot());

                                    dateReference.setValue(dateInfo1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                WishInfo wishInfo1 = dataSnapshot.getValue(WishInfo.class);

                                db.child(wishInfo.getKey().toString()).child("valueGot").setValue(wishInfo1.getValueGot() +wishInfo.getValueGot());


                                Toast.makeText(getContext(),"Saved Successfully",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });



        return builder.create();
    }
}

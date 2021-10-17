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

public class TourEditDialog extends AppCompatDialogFragment {

    private EditText tourStartDate,tourEndDate;

    private TourInfo tourInfo;

    private FirebaseUser user;

    private String uid;

    public TourEditDialog(TourInfo tourInfo) {
        this.tourInfo = tourInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.tour_edit_dialog_layout,null);

        tourStartDate = view.findViewById(R.id.tour_edit_startDate_edittext);

        tourEndDate = view.findViewById(R.id.tour_edit_endDate_edittext);

        builder.setView(view)
                .setTitle("Edit Dates")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user = FirebaseAuth.getInstance().getCurrentUser();

                        uid = user.getUid();

                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Tour Plan");

                        if(tourStartDate.getText().toString().trim().equals("") || tourEndDate.getText().toString().trim().equals("")){

                            Toast.makeText(getContext(),"Please Edit a date",Toast.LENGTH_SHORT).show();
                        }

                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(!tourStartDate.getText().toString().trim().equals("")){

                                    final String startDate = tourStartDate.getText().toString().trim();

                                    db.child(tourInfo.getKey().toString()).child("startDate").setValue(startDate);
                                }

                                if(!tourEndDate.getText().toString().trim().equals("")){

                                    final String endDate = tourEndDate.getText().toString().trim();
                                    db.child(tourInfo.getKey().toString()).child("endDate").setValue(endDate);
                                }


                                Toast.makeText(getContext(),"Dates Saved",Toast.LENGTH_SHORT).show();

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

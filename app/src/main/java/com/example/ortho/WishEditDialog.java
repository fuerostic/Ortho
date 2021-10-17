package com.example.ortho;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class WishEditDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private EditText wishName,wishValue,wishDate;

    private Spinner wishType;

    private FirebaseUser user;

    private String uid;

    private WishInfo wishInfo;

    private int spinnercount=0;

    private int images[] = {R.drawable.car2,R.drawable.home,R.drawable.company2,R.drawable.monitor,
            R.drawable.mobile,R.drawable.hamburguer,R.drawable.world,
            R.drawable.gift,R.drawable.book,R.drawable.band_aid,R.drawable.backpack,
            R.drawable.screwdriver,R.drawable.hard_drive,R.drawable.headset,R.drawable.christmas_sweater_2,
            R.drawable.briefcase,R.drawable.wrench,R.drawable.music_guitar,R.drawable.games_control,
            R.drawable.gear,R.drawable.certificate,R.drawable.demographic,R.drawable.ball_football};


    public WishEditDialog(WishInfo wishInfo) {
        this.wishInfo = wishInfo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.edit_wish_dialog_layout,null);

        wishName = view.findViewById(R.id.edit_wish_name_edittext);

        wishValue = view.findViewById(R.id.edit_wish_value_edit_text);

        wishDate = view.findViewById(R.id.edit_wish_date_edittext);

        wishType = view.findViewById(R.id.edit_wish_type_spinner);

        String types[] = getResources().getStringArray(R.array.wishType);

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.addbalance_spinner,R.id.add_balance_spinner_layout_textview,types);

        wishType.setAdapter(arrayAdapter);

        wishType.setOnItemSelectedListener(this);

        builder.setView(view)
                .setTitle("Edit Wish")
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

                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(uid).child("Wish List");


                        if(!wishValue.getText().toString().trim().equals("")){

                            wishInfo.setWishValue(Double.valueOf(wishValue.getText().toString()));
                        }

                        if(!wishName.getText().toString().trim().equals("")){

                            wishInfo.setWishName(wishName.getText().toString());
                        }
                        if(!wishDate.getText().toString().trim().equals("")){

                            wishInfo.setWishDate(wishDate.getText().toString());
                        }


                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                db.child(wishInfo.getKey().toString()).child("wishName").setValue(wishInfo.getWishName());

                                db.child(wishInfo.getKey().toString()).child("wishType").setValue(wishInfo.getWishType());

                                db.child(wishInfo.getKey().toString()).child("wishValue").setValue(wishInfo.getWishValue());

                                db.child(wishInfo.getKey().toString()).child("wishDate").setValue(wishInfo.getWishDate());

                                db.child(wishInfo.getKey().toString()).child("image").setValue(wishInfo.getImage());





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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(spinnercount==0){
            spinnercount++;
        }else{

            wishInfo.setWishType(parent.getSelectedItem().toString());

            wishInfo.setImage(images[position]);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {



    }




}

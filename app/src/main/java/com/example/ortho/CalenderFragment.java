package com.example.ortho;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CalenderFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private ListView calenderList;

    private DatabaseReference databaseReference;

    private FirebaseUser user;

    private String uid;

    private List<HeadingInfo> headingInfoList;

    private CalenderListAdapter calenderListAdapter;

    private TextView textView;

    private GifImageView gifImageView;

    private FloatingActionButton floatingActionButton;

    private ProgressBar progressBar;

    private DatePickerTimeline datePickerTimeline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_calender,container,false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        headingInfoList = new ArrayList<HeadingInfo>();


        calenderList  = view.findViewById(R.id.calender_list_id);

        final DailyData dailyData = new DailyData(Calendar.getInstance().getTime());

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(dailyData.getYear()).child(dailyData.getMonth()).child(dailyData.getDay());

        textView = view.findViewById(R.id.no_calender_activity_text);

        gifImageView = view.findViewById(R.id.no_calender_activity_image);

        floatingActionButton = view.findViewById(R.id.floating_calender_go_to_today);

        datePickerTimeline = view.findViewById(R.id.calender_date_picker_timeline_id);

        calenderListAdapter = new CalenderListAdapter(view.getContext(), headingInfoList);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(CalenderFragment.this,0);
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });






      datePickerTimeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {

                month+=1;

                String d ,mon;

                if(day/10==0){

                    d = "0"+String.valueOf(day);
                }else {

                    d=String.valueOf(day);
                }

                if(month/10==0){

                    mon = "0" + String.valueOf(month);
                }else {

                    mon = String.valueOf(month);
                }

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(String.valueOf(year)).child(mon).child(d);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           // Toast.makeText(getContext(),reference.toString(),Toast.LENGTH_SHORT).show();

                        if(dataSnapshot.hasChildren()){

                           // Toast.makeText(getContext(),"Child exists",Toast.LENGTH_SHORT).show();

                            textView.setVisibility(View.GONE);
                            gifImageView.setVisibility(View.GONE);

                            DateInfo dateInfo = dataSnapshot.getValue(DateInfo.class);

                            headingInfoList.clear();

                            fetchData(dateInfo);




                        }else{

                            headingInfoList.clear();

                           // Toast.makeText(getContext(),"Child doesnt exists",Toast.LENGTH_SHORT).show();
                        }
                        if(headingInfoList.size()==0){



                            textView.setVisibility(View.VISIBLE);
                            gifImageView.setVisibility(View.VISIBLE);

                        }



                        calenderList.setAdapter(calenderListAdapter);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });







            }
        });








        return view;
    }


    @Override
    public void onStart() {



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DailyData dailyData = new DailyData(Calendar.getInstance().getTime());


               // Toast.makeText(getContext(),databaseReference.toString(),Toast.LENGTH_SHORT).show();

                if(dataSnapshot.hasChildren()){

                    textView.setVisibility(View.GONE);
                    gifImageView.setVisibility(View.GONE);

                    DateInfo dateInfo = dataSnapshot.getValue(DateInfo.class);

                    headingInfoList.clear();

                   // Toast.makeText(getContext(),"Dep" + dateInfo.getDeposit() + "exp " + dateInfo.getExpense(),Toast.LENGTH_SHORT).show();

                    fetchData(dateInfo);


                }

                if(headingInfoList.size()==0){

                    textView.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.VISIBLE);

                }

               // Log.d("Hello", "onDataChange: here i am");

                //Toast.makeText(getContext(),String.valueOf(headingInfoList.size()),Toast.LENGTH_SHORT).show();

                calenderList.setAdapter(calenderListAdapter);

               // Log.d("Hello", "onDataChange: here i am after adapter");




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }


    void fetchData(DateInfo dateInfo){


        if(dateInfo.getDeposit()>0.0){

           // Log.d("Hello", "onDataChange: i was called on deposit");

            headingInfoList.add(new HeadingInfo("deposit",String.valueOf(dateInfo.getDeposit())));

        }
        if(dateInfo.getExpense()>0.0){

           // Log.d("Hello", "onDataChange: i was called on expense");

            headingInfoList.add(new HeadingInfo("expense",String.valueOf(dateInfo.getExpense())));

        }
        if(dateInfo.getBankDeposit()>0.0){

           // Log.d("Hello", "onDataChange: i was called on bankdeposit");

            headingInfoList.add(new HeadingInfo("bankDeposit",String.valueOf(dateInfo.getBankDeposit())));

        }
        if(dateInfo.getBankWithdrawn()>0.0){

           // Log.d("Hello", "onDataChange: i was called on bankwithdrawn");

            headingInfoList.add(new HeadingInfo("bankWithdrawn",String.valueOf(dateInfo.getBankWithdrawn())));

        }
        if(dateInfo.getWishSave()>0.0){

           // Log.d("Hello", "onDataChange: i was called on wishsave");

            headingInfoList.add(new HeadingInfo("wishSave",String.valueOf(dateInfo.getWishSave())));

        }
        if(dateInfo.getDebtReceived()>0.0){

           // Log.d("Hello", "onDataChange: i was called on debtreceive");

            headingInfoList.add(new HeadingInfo("debtReceived",String.valueOf(dateInfo.getDebtReceived())));

        }
        if(dateInfo.getDebtPay()>0.0){

            //Log.d("Hello", "onDataChange: i was called on debtpay");

            headingInfoList.add(new HeadingInfo("debtPaid",String.valueOf(dateInfo.getDebtPay())));

        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        datePickerTimeline.setSelectedDate(year,month,dayOfMonth);

    }
}

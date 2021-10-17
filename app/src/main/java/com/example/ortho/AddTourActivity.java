package com.example.ortho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTourActivity extends AppCompatActivity implements View.OnClickListener {

    private static EditText tourName,destination,startDate,endDate,myBudget,numberOfpeople;

    private ImageButton startDatePicker, endDatePicker;

    private Button saveButton;

    private DatabaseReference databaseReference;

    private static final int START_DATE_PICKER=0,END_DATE_PICKER=1;

    private DatePickerDialogFragment mDatePickerDialogFragment;

    private FirebaseUser user;

    private String uid;

    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tourName = findViewById(R.id.tour_name_edittext);

        destination = findViewById(R.id.tour_place_edittext);

        startDate = findViewById(R.id.tour_start_date_edittext);

        endDate = findViewById(R.id.tour_end_date_edittext);

        myBudget = findViewById(R.id.tour_initial_budget);

        numberOfpeople = findViewById(R.id.tour_people_number);
        back_button = findViewById(R.id.back_button_add_tour);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        numberOfpeople.setText("1");


        user = FirebaseAuth.getInstance().getCurrentUser();


        startDatePicker = findViewById(R.id.add_tour_start_date_picker_button);

        startDatePicker.setOnClickListener(this);

        endDatePicker = findViewById(R.id.add_tour_end_date_picker_button);

        endDatePicker.setOnClickListener(this);

        saveButton = findViewById(R.id.add_tour_save_button);

        saveButton.setOnClickListener(this);

        mDatePickerDialogFragment = new DatePickerDialogFragment();

        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("Tour Plan");


    }

    public static class DatePickerDialogFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        private int flag = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void setFlag(int i) {
            flag = i;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            if (flag == START_DATE_PICKER) {
                startDate.setText(format.format(calendar.getTime()));
            } else if (flag == END_DATE_PICKER) {
                endDate.setText(format.format(calendar.getTime()));
            }
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.add_tour_start_date_picker_button){

            mDatePickerDialogFragment.setFlag(START_DATE_PICKER);
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");


        }else if(v.getId()==R.id.add_tour_end_date_picker_button){

            mDatePickerDialogFragment.setFlag(END_DATE_PICKER);
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");


        }else if(v.getId()==R.id.add_tour_save_button){

            saveData();




        }

    }

    private void saveData() {


        String name,destination_place,start_date,end_date,key;

        double my_budget;

        int number_of_people;

        if(tourName.getText().toString().trim().equals("")){

            tourName.setError("Enter Tour Name");
            tourName.requestFocus();

            return;
        }

        if(destination.getText().toString().trim().equals("")){

            destination.setError("Enter Destination");
            destination.requestFocus();

            return;
        }


        if(startDate.getText().toString().trim().equals("")){

            startDate.setError("Set Start Date");
            startDate.requestFocus();

            return;
        }


        if(endDate.getText().toString().trim().equals("")){

            endDate.setError("Set End Date");
            endDate.requestFocus();

            return;

        }

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {

            Date date1 = format.parse(startDate.getText().toString().trim());

            Date date2 = format.parse(endDate.getText().toString().trim());

            if(date1.compareTo(date2)>0){

                startDate.setError("Start can't be after end date");

                startDate.requestFocus();

                return;

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(myBudget.getText().toString().trim().equals("")) {

            myBudget.setError("Enter Budget Amount");
            myBudget.requestFocus();

            return;


        }

        name = tourName.getText().toString().trim();

        destination_place = destination.getText().toString().trim();

        start_date = startDate.getText().toString().trim();

        end_date = endDate.getText().toString().trim();

        my_budget = Double.valueOf(myBudget.getText().toString());

        number_of_people = Integer.valueOf(numberOfpeople.getText().toString());

        key = databaseReference.push().getKey();

        TourInfo tourInfo = new TourInfo(name,destination_place,start_date,end_date,my_budget,number_of_people);

        tourInfo.setKey(key);

        databaseReference.child(key).setValue(tourInfo);

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        finish();
    }
}

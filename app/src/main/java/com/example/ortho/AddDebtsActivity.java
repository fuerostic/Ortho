package com.example.ortho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDebtsActivity extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String [] debtsCategory;

    private Spinner debtsType;

    private Button debtSaveButton;

    private ImageButton issueDatePickerButton,dueDatePickerButton;

    private String key,type;

    private DatabaseReference databaseReference;

    private EditText partnerName,amount;

    private static EditText issueDate,dueDate;

    private final static int ISSUE_DATE_PICKER=0,DUE_DATE_PICKER=1;

    private DatePickerDialogFragment mDatePickerDialogFragment;

    private FirebaseUser user;

    private String uid;

    private ImageButton back_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debts);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        debtsCategory = getResources().getStringArray(R.array.debtType);

        debtsType = findViewById(R.id.debt_add_type_spinner);

        partnerName = findViewById(R.id.debt_add_person_edittext);

        amount = findViewById(R.id.debt_add_amount_edittext);

        issueDate = findViewById(R.id.debt_add_issue_date_edittext_id);

        dueDate = findViewById(R.id.debt_add_due_date_edittext_id);

        issueDatePickerButton = findViewById(R.id.debt_issue_date_picker_button);

        dueDatePickerButton = findViewById(R.id.debt_due_date_picker_button);

        debtSaveButton = findViewById(R.id.debt_add_save_button);

        user = FirebaseAuth.getInstance().getCurrentUser();

        back_button = findViewById(R.id.back_button_add_debt);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("Debts");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.addbalance_spinner,R.id.add_balance_spinner_layout_textview,debtsCategory);

        debtsType.setAdapter(arrayAdapter);

        debtsType.setOnItemSelectedListener(this);

        debtSaveButton.setOnClickListener(this);

        mDatePickerDialogFragment = new DatePickerDialogFragment();

        issueDatePickerButton.setOnClickListener(this);

        dueDatePickerButton.setOnClickListener(this);

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
            if (flag == ISSUE_DATE_PICKER) {
                issueDate.setText(format.format(calendar.getTime()));
            } else if (flag == DUE_DATE_PICKER) {
                dueDate.setText(format.format(calendar.getTime()));
            }
        }
    }



    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.debt_issue_date_picker_button){

            mDatePickerDialogFragment.setFlag(ISSUE_DATE_PICKER);
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
        }else if(v.getId()==R.id.debt_due_date_picker_button){

            mDatePickerDialogFragment.setFlag(DUE_DATE_PICKER);
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
        }else if(v.getId()==R.id.debt_add_save_button){

            saveData();

        }

    }

    private void saveData() {

        String issue_date,due_date,partner,type_name;

        double amount_quantity;


        if(partnerName.getText().toString().trim().equals("")){

            partnerName.setError("Partner Name required");
            partnerName.requestFocus();

            return;

        }
        if(issueDate.getText().toString().trim().equals("")){
            issueDate.setError("Please set Issue date");
            issueDate.requestFocus();

            return;

        }
        if(dueDate.getText().toString().trim().equals("")){

            dueDate.setError("Please set Due date");
            dueDate.requestFocus();

            return;

        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {

            Date date1 = format.parse(issueDate.getText().toString().trim());

            Date date2 = format.parse(dueDate.getText().toString().trim());

            if(date1.compareTo(date2)>0){

                issueDate.setError("Issue date can't be after due date");

                issueDate.requestFocus();

                return;

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(amount.getText().toString().trim().equals("")){

            amount.setError("Amount required");
            amount.requestFocus();

            return;

        }




        partner = partnerName.getText().toString().toUpperCase();

        type_name = debtsType.getSelectedItem().toString();

        issue_date= issueDate.getText().toString();

        due_date = dueDate.getText().toString();

        amount_quantity = new Double(amount.getText().toString());

        key = databaseReference.push().getKey();

        DebtsInfo debtsInfo = new DebtsInfo(issue_date,due_date,partner,type_name,amount_quantity);

        debtsInfo.setKey(key);

        databaseReference.child(key).setValue(debtsInfo);

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        finish();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        type = parent.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}

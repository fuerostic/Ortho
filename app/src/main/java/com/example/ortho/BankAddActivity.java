package com.example.ortho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BankAddActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener , DatePickerDialog.OnDateSetListener {

    private String [] accountTypes;

    private Button saveBank;

    private ImageButton datepicker;

    private EditText bankName,accountNumber,startingBalance,loan,loanDueDate;

    private Spinner accountType;

    private String accountTypeName;

    private DatabaseReference bankListReference;

    private FirebaseUser user;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_add);

        saveBank = findViewById(R.id.save_bank_button);
        datepicker = findViewById(R.id.loan_due_date_picker_button);

        accountType = findViewById(R.id.bank_account_type_spinner_id);

        bankName = findViewById(R.id.bank_name_edittext_id);
        accountNumber= findViewById(R.id.bank_account_number_edittext_id);
        startingBalance = findViewById(R.id.bank_initial_balance_editText_id);
        loan = findViewById(R.id.bank_loan_amount_edittext_id);
        loanDueDate = findViewById(R.id.bank_loan_due_edittext_id);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        bankListReference = FirebaseDatabase.getInstance().getReference(uid).child("Bank Accounts");

        accountTypes = getResources().getStringArray(R.array.accountTypes);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.addbalance_spinner,R.id.add_balance_spinner_layout_textview,accountTypes);

        accountType.setAdapter(arrayAdapter);

        accountType.setOnItemSelectedListener(this);
        saveBank.setOnClickListener(this);
        datepicker.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.save_bank_button){

            saveData();

            finish();


        }else if(v.getId()==R.id.loan_due_date_picker_button){

            DialogFragment datePickerFragment = new AddBalanceDatePickerFragment();

            datePickerFragment.show(getSupportFragmentManager(),"date picker");


        }

    }

    private void saveData() {

        String bank_name,account_number,account_type,loan_due_date;

        double initial_balance=0,loan_amount=0;

        if(bankName.getText().toString().trim().equals("")){
            bankName.setError("Name Required");
            bankName.requestFocus();

            return;
        }
        if(accountNumber.getText().toString().trim().equals("")){

            accountNumber.setError("Account Number Required");

            accountNumber.requestFocus();
            return;
        }

        if(!loanDueDate.getText().toString().trim().equals("")){

            loan_due_date = loanDueDate.getText().toString().trim();
        }else {
            loan_due_date="";
        }

        bank_name = bankName.getText().toString().trim();
        account_number = accountNumber.getText().toString().trim();
        account_type = accountTypeName;


        try {
            if(startingBalance.getText().toString().equals("")){
                initial_balance = 0.0;
            }else{
                initial_balance = Double.valueOf(startingBalance.getText().toString());

            }

            if(loan.getText().toString().equals("")){
                loan_amount = 0.0;
            }else{
                loan_amount = Double.valueOf(loan.getText().toString());

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }



        String bankKey = bankListReference.push().getKey();

        BankAccountInfo bankAccountInfo= new BankAccountInfo(bank_name,account_type,account_number,loan_due_date,initial_balance,loan_amount);

        bankAccountInfo.setKey(bankKey);

        bankListReference.child(bankKey).setValue(bankAccountInfo);

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        accountTypeName = parent.getSelectedItem().toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = df.format(c);

        loanDueDate.setText(formattedDate);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);



        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String currentDateString = dateFormat.format(calendar.getTime());
        loanDueDate.setText(currentDateString);
    }
}

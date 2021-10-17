package com.example.ortho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBalanceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener , DatePickerDialog.OnDateSetListener {

    private String [] categories;

    private Spinner spinner;

    private EditText categoryEditText,dateEditText,amountEditText;

    private Button saveButton;

    private ImageButton imageButton;

    private DatabaseReference databaseReference,transactionReference;

    private String key,transactionId;

    private FirebaseUser user;

    private String uid;

    private DailyData dailyData;

    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        categories = getResources().getStringArray(R.array.deposits);

        spinner = findViewById(R.id.add_balance_category_spinner);

        categoryEditText = findViewById(R.id.add_balance_category_edittext);

        dateEditText = findViewById(R.id.add_balance_date_edittext);

        amountEditText = findViewById(R.id.add_balance_amount_edittext);

        imageButton = findViewById(R.id.add_balance_date_pick_button);

        saveButton = findViewById(R.id.add_balance_savebutton);

        user = FirebaseAuth.getInstance().getCurrentUser();

        back_button = findViewById(R.id.back_button_add_balance);

        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        transactionReference = FirebaseDatabase.getInstance().getReference(uid).child("transactions");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.addbalance_spinner,R.id.add_balance_spinner_layout_textview,categories);

        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

        imageButton.setOnClickListener(this);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date,category;

                double amount;

                Date date1,date2;

                date1 = Calendar.getInstance().getTime();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                if(dateEditText.getText().toString().trim().equals("")){

                    dateEditText.setError("Set date please");

                    dateEditText.requestFocus();

                    return ;

                }

                try {

                    date2 = format.parse(dateEditText.getText().toString().trim());

                    if(date1.compareTo(date2)<0){

                        dateEditText.setError("Date cant be farther than today");
                        dateEditText.requestFocus();

                        return;

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }



                if(categoryEditText.getText().toString().trim().equals("")){

                    categoryEditText.setError("Select a category");

                    categoryEditText.requestFocus();

                    return;
                }

                if(amountEditText.getText().toString().trim().equals("")){

                    amountEditText.setError("Enter your amount");
                    amountEditText.requestFocus();

                    return;
                }

                date = dateEditText.getText().toString().trim();
                category = categoryEditText.getText().toString().trim();
                amount = new Double(amountEditText.getText().toString());

                final DateInfo dateInfo = new DateInfo();

                final DatabaseReference dateReference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(dailyData.getYear()).child(dailyData.getMonth()).child(dailyData.getDay());



                transactionId = transactionReference.push().getKey();

                final TransactionData transactionData = new TransactionData(date,category,amount,"Deposit");

                transactionData.setTransactionId(transactionId);

                transactionReference.child(transactionId).setValue(transactionData);

                dateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("deposit")){

                            DateInfo dateInfo1 = dataSnapshot.getValue(DateInfo.class);

                            dateInfo1.setDeposit(dateInfo1.getDeposit() + transactionData.getAmount());

                            dateReference.setValue(dateInfo1);
                        }else {

                            DateInfo dateInfo1 = new DateInfo();

                            dateInfo1.setDeposit(transactionData.getAmount());

                            dateReference.setValue(dateInfo1);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Toast.makeText(AddBalanceActivity.this, "Data saved", Toast.LENGTH_SHORT).show();

                finish();

            }
        });

    }

    @Exclude
    public String getKey(){
        return key;
    }

    @Exclude
    public String getTransactionId(){

        return transactionId;
    }

    @Exclude
    public void setKey(String key){
        this.key =key;
    }

    @Exclude
    public void setTransactionId(String transactionId){
        this.transactionId =transactionId;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add_balance_savebutton){

            saveData();

            finish();


        }else if(v.getId()==R.id.add_balance_date_pick_button){

            DialogFragment datePickerFragment = new AddBalanceDatePickerFragment();

            datePickerFragment.show(getSupportFragmentManager(),"date picker");


        }
    }

    private void saveData() {


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        categoryEditText.setText(parent.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = df.format(c);

        dateEditText.setText(formattedDate);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);



        dailyData = new DailyData(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String currentDateString = dateFormat.format(calendar.getTime());
        dateEditText.setText(currentDateString);



    }
}

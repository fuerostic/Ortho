package com.example.ortho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener , DatePickerDialog.OnDateSetListener {

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
        setContentView(R.layout.activity_add_expense);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        categories = getResources().getStringArray(R.array.category);

        spinner = findViewById(R.id.add_expense_category_spinner);

        categoryEditText = findViewById(R.id.add_expense_category_edittext);

        dateEditText = findViewById(R.id.add_expense_date_edittext);

        amountEditText = findViewById(R.id.add_expense_amount_edittext);

        imageButton = findViewById(R.id.add_expense_date_pick_button);

        saveButton = findViewById(R.id.add_expense_savebutton);

        back_button = findViewById(R.id.back_button_add_expense);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("expenses");
        transactionReference = FirebaseDatabase.getInstance().getReference(uid).child("transactions");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.addbalance_spinner,R.id.add_balance_spinner_layout_textview,categories);

        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

        imageButton.setOnClickListener(this);

        saveButton.setOnClickListener(this);


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        dailyData = new DailyData(calendar.getTime());


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String currentDateString = dateFormat.format(calendar.getTime());
        dateEditText.setText(currentDateString);

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.add_expense_savebutton){


            saveData();



        }else if(v.getId()==R.id.add_expense_date_pick_button){

            DialogFragment datePickerFragment = new AddBalanceDatePickerFragment();

            datePickerFragment.show(getSupportFragmentManager(),"date picker");


        }

    }

    private void saveData() {

        String date,category;

        Date date1,date2;

        date1 = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        double amount;

        if (dateEditText.getText().toString().trim().equals("")) {

            dateEditText.setError("Set a date");
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

            categoryEditText.setError("Set a category");
            categoryEditText.requestFocus();

            return;
        }

        if(amountEditText.getText().toString().trim().equals("")){

            amountEditText.setError("Amount Required");
            amountEditText.requestFocus();

            return;
        }

        date = dateEditText.getText().toString().trim();
        category = categoryEditText.getText().toString().trim();
        amount = new Double(amountEditText.getText().toString());

        transactionId = transactionReference.push().getKey();

        final TransactionData transactionData = new TransactionData(date,category,amount,"Expense");

        transactionData.setTransactionId(transactionId);

        transactionReference.child(transactionId).setValue(transactionData);


        final DatabaseReference dateReference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(dailyData.getYear()).child(dailyData.getMonth()).child(dailyData.getDay());

        dateReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("expense")){

                    DateInfo dateInfo1 = dataSnapshot.getValue(DateInfo.class);

                    dateInfo1.setExpense(dateInfo1.getExpense() + transactionData.getAmount());

                    dateReference.setValue(dateInfo1);
                }else {

                    DateInfo dateInfo1 = new DateInfo();

                    dateInfo1.setExpense(transactionData.getAmount());

                    dateReference.setValue(dateInfo1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference expensereference = FirebaseDatabase.getInstance().getReference(uid).child("expenseInfo").child(dailyData.getYear());


        expensereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(dailyData.getMonth())){

                    ExpenseInfo expenseInfo = dataSnapshot.child(dailyData.getMonth()).getValue(ExpenseInfo.class);

                    if(categoryEditText.getText().toString().trim().equals("Food")){
                        expenseInfo.setFood(expenseInfo.getFood()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Rent")){

                        expenseInfo.setRent(expenseInfo.getRent() + transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Transportation")){

                        expenseInfo.setTransportation(expenseInfo.getTransportation()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Clothing")){

                        expenseInfo.setClothing(expenseInfo.getClothing()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Communication")){

                        expenseInfo.setCommunication(expenseInfo.getCommunication()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Books")){

                        expenseInfo.setBooks(expenseInfo.getBooks()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Electronics")){

                        expenseInfo.setElectronics(expenseInfo.getElectronics()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Project")){

                        expenseInfo.setProject(expenseInfo.getProject()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Treat")){

                        expenseInfo.setTreat(expenseInfo.getTreat() + transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Tuition")){

                        expenseInfo.setTuition(expenseInfo.getTuition()+ transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Education")){

                        expenseInfo.setEducation(expenseInfo.getEducation()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Hangout")){

                        expenseInfo.setHangout(expenseInfo.getHangout()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Trip")){

                        expenseInfo.setTrip(expenseInfo.getTrip()+ transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Utilities")){

                        expenseInfo.setUtilities(expenseInfo.getUtilities() + transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Services")){

                        expenseInfo.setServices(expenseInfo.getServices()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Fees")){

                        expenseInfo.setFees(expenseInfo.getFees()+transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Tax")){

                        expenseInfo.setTax(expenseInfo.getTax()+ transactionData.getAmount());

                    }else {

                        expenseInfo.setOthers(expenseInfo.getOthers() + transactionData.getAmount());

                    }


                    expensereference.child(dailyData.getMonth()).setValue(expenseInfo);

                }else {

                    ExpenseInfo expenseInfo = new ExpenseInfo();

                    if(categoryEditText.getText().toString().trim().equals("Food")){
                        expenseInfo.setFood(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Rent")){

                        expenseInfo.setRent(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Transportation")){

                        expenseInfo.setTransportation(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Clothing")){

                        expenseInfo.setClothing(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Communication")){

                        expenseInfo.setCommunication(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Books")){

                        expenseInfo.setBooks(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Electronics")){

                        expenseInfo.setElectronics(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Project")){

                        expenseInfo.setProject(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Treat")){

                        expenseInfo.setTreat( transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Tuition")){

                        expenseInfo.setTuition(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Education")){

                        expenseInfo.setEducation(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Hangout")){

                        expenseInfo.setHangout(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Trip")){

                        expenseInfo.setTrip(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Utilities")){

                        expenseInfo.setUtilities( transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Services")){

                        expenseInfo.setServices(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Fees")){

                        expenseInfo.setFees(transactionData.getAmount());

                    }else if(categoryEditText.getText().toString().trim().equals("Tax")){

                        expenseInfo.setTax( transactionData.getAmount());

                    }else {

                        expenseInfo.setOthers(transactionData.getAmount());

                    }

                    expensereference.child(dailyData.getMonth()).setValue(expenseInfo);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();


        finish();



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        categoryEditText.setText(parent.getSelectedItem().toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = df.format(c);

        dateEditText.setText(formattedDate);

    }
}

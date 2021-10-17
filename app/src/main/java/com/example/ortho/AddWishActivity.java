package com.example.ortho;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddWishActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener , DatePickerDialog.OnDateSetListener {


    private EditText wishNameEditext,wishValueEdittext;

    private Button saveButton,dateSetButton;

    private DatabaseReference databaseReference;

    private Spinner wishTypeSpinner;

    private WishInfo wishInfo;

    private TextView textView;

    private FirebaseUser user;

    private String uid;

    private String types[];

    private String currentDateString;

    private ImageButton back_button;

    private int images[] = {R.drawable.car2,R.drawable.home,R.drawable.company2,R.drawable.monitor,
            R.drawable.mobile,R.drawable.hamburguer,R.drawable.world,
    R.drawable.gift,R.drawable.book,R.drawable.band_aid,R.drawable.backpack,
    R.drawable.screwdriver,R.drawable.hard_drive,R.drawable.headset,R.drawable.christmas_sweater_2,
    R.drawable.briefcase,R.drawable.wrench,R.drawable.music_guitar,R.drawable.games_control,
    R.drawable.gear,R.drawable.certificate,R.drawable.demographic,R.drawable.ball_football};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        wishInfo  = new WishInfo();

        textView = findViewById(R.id.add_wish_date_textview);

        back_button = findViewById(R.id.back_button_add_wish);

        wishNameEditext = findViewById(R.id.add_wish_wish_name_editText);
        wishValueEdittext = findViewById(R.id.add_wish_wish_value_editText);

        saveButton = findViewById(R.id.add_wish_save_button);
        dateSetButton = findViewById(R.id.add_wish_wish_date_select_button);

        wishTypeSpinner = findViewById(R.id.add_wish_wish_type_spinner);

        types = getResources().getStringArray(R.array.wishType);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();


        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("Wish List");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.addbalance_spinner,R.id.add_balance_spinner_layout_textview,types);

        wishTypeSpinner.setAdapter(arrayAdapter);

        wishTypeSpinner.setOnItemSelectedListener(this);

        saveButton.setOnClickListener(this);

        dateSetButton.setOnClickListener(this);

        back_button.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);



        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        currentDateString = dateFormat.format(calendar.getTime());


        wishInfo.setWishDate(currentDateString);

        textView.setText(currentDateString);

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.add_wish_save_button){

            saveData();


        }else if(v.getId()==R.id.add_wish_wish_date_select_button){

            DialogFragment datePickerFragment = new AddBalanceDatePickerFragment();

            datePickerFragment.show(getSupportFragmentManager(),"date picker");

        }else if(v.getId()==R.id.back_button_add_wish){

            finish();
        }

    }

    private void saveData() {

        if(wishNameEditext.getText().toString().trim().equals("")){

            wishNameEditext.setError("Please enter wish name");
            wishNameEditext.requestFocus();

            return;

        }

        if(wishValueEdittext.getText().toString().trim().equals("")){

            wishValueEdittext.setError("Value required");
            wishValueEdittext.requestFocus();

            return;
        }

        if(currentDateString.equals("")){

            dateSetButton.setError("Enter Date");
            dateSetButton.requestFocus();

            return;
        }


        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {

            Date date1 = Calendar.getInstance().getTime();

            Date date2 = format.parse(currentDateString);

            if(date1.compareTo(date2)>0){

                dateSetButton.setError("Date can't be before today");

                dateSetButton.requestFocus();

                return;

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        wishInfo.setWishName(wishNameEditext.getText().toString());
        wishInfo.setWishValue(Double.valueOf(wishValueEdittext.getText().toString()));

        String key = databaseReference.push().getKey();

        wishInfo.setKey(key);

        databaseReference.child(key).setValue(wishInfo);


        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        wishInfo.setWishType(types[position]);

        wishInfo.setImage(images[position]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

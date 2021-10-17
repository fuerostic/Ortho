package com.example.ortho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backButton;

    private TextView login,needHelp;

    private Button registerButton;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private EditText emailEdittext,passwordEdittext,confirmPassEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.signUpProgressBarId);

        backButton = findViewById(R.id.backButtonId);

        login = findViewById(R.id.loginTextViewOnRegisterScreenId);


        registerButton = findViewById(R.id.registerButtonId);

        emailEdittext = findViewById(R.id.emailEditTextId);

        passwordEdittext = findViewById(R.id.passwordEditTextId);

        confirmPassEdittext = findViewById(R.id.passwordConfirmEditTextId);

        backButton.setOnClickListener(this);
        login.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.backButtonId){

            finish();
        }else if(v.getId()==R.id.loginTextViewOnRegisterScreenId){

            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);

            startActivity(intent);

            finish();
        }else if(v.getId()==R.id.registerButtonId){

            userRegister();


        }

    }

    private void userRegister() {

        final String email = emailEdittext.getText().toString().trim();

        String password = passwordEdittext.getText().toString();

        String confirmPass = confirmPassEdittext.getText().toString();


        if(email.isEmpty()){

            emailEdittext.setError("Enter your email address");
            emailEdittext.requestFocus();

            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            emailEdittext.setError("Enter a valid email address");
            emailEdittext.requestFocus();

            return;

        }

        if(password.isEmpty()){

            passwordEdittext.setError("Enter a password");
            passwordEdittext.requestFocus();

            return;
        }
        if(password.length()<6){

            passwordEdittext.setError("Minimum password length is 6");
            passwordEdittext.requestFocus();

            return;
        }
        if(!confirmPass.equals(password)){

            confirmPassEdittext.setError("Password did not match");
            confirmPassEdittext.requestFocus();

            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"Register Successful ",Toast.LENGTH_SHORT).show();

                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("accountInfo");

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    databaseReference.setValue(new AccountInfo(email));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            finish();

                            Intent intent = new Intent(SignUpActivity.this,MainScreenActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.

                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(),"User already exits",Toast.LENGTH_SHORT).show();
                            }else {

                                Toast.makeText(getApplicationContext(),"Error: " +task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }



                        }


                    }
                });


    }
}

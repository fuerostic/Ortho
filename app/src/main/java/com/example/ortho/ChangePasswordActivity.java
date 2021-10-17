package com.example.ortho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private ProgressBar loadingProgressbar;

    private EditText oldPassword,newPassword,confirmPassword;

    private Button saveButton;

    private String email,Oldpass,newPass,confPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        loadingProgressbar = findViewById(R.id.change_password_laoding_bar);

        oldPassword = findViewById(R.id.change_email_password_validate_edittext);

        newPassword = findViewById(R.id.new_password_editText);

        confirmPassword = findViewById(R.id.new_password_confirm_editText);




        saveButton = findViewById(R.id.save_new_password);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Oldpass = oldPassword.getText().toString();

                newPass = newPassword.getText().toString();

                confPass = confirmPassword.getText().toString();

                if(Oldpass.isEmpty()){

                    oldPassword.setError("Enter a password");
                    oldPassword.requestFocus();

                    return;
                }
                if(newPass.isEmpty()){

                    newPassword.setError("Enter a password");
                    newPassword.requestFocus();

                    return;
                }
                if(newPass.length()<6){

                    newPassword.setError("Minimum password length is 6");
                    newPassword.requestFocus();

                    return;
                }
                if(!confPass.equals(newPass)){

                    confirmPassword.setError("Password did not match");
                    confirmPassword.requestFocus();

                    return;
                }

                loadingProgressbar.setVisibility(View.VISIBLE);

                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, Oldpass);

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(getApplicationContext(),"Password Updated",Toast.LENGTH_SHORT).show();

                                            } else {

                                                Toast.makeText(getApplicationContext(),"Error: password not updated",Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                } else {

                                    Toast.makeText(getApplicationContext(),"Error occured,Password not updated",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                loadingProgressbar.setVisibility(View.GONE);

                finish();

            }
        });


    }
}

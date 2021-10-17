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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;

    private EditText usernameLogin,passwordLogin;

    private ImageView backButton;

    private TextView registerButton,needHelp;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.loginProgressBarId);

        usernameLogin = findViewById(R.id.emailEditTextId);

        passwordLogin = findViewById(R.id.passwordEditTextId);

        backButton = findViewById(R.id.backButtonId);

        loginButton = findViewById(R.id.loginButtonId);

        registerButton = findViewById(R.id.registerTextViewOnLoginScreenId);

        needHelp = findViewById(R.id.needHelpId);

        needHelp.setOnClickListener(this);

        backButton.setOnClickListener(this);

        loginButton.setOnClickListener(this);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.backButtonId){

            finish();
        }else if(v.getId()==R.id.loginButtonId){


            userLogin();




        }else if(v.getId()==R.id.registerTextViewOnLoginScreenId){

            Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);

            finish();
        }else if(v.getId()==R.id.needHelpId){


            ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog(this);

            forgotPasswordDialog.show(getSupportFragmentManager(),"password reset");

        }

    }

    private void userLogin() {

        final String email = usernameLogin.getText().toString().trim();

        String password = passwordLogin.getText().toString();


        if(email.isEmpty()){

            usernameLogin.setError("Enter your email address");
            usernameLogin.requestFocus();

            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            usernameLogin.setError("Enter a valid email address");
            usernameLogin.requestFocus();

            return;

        }

        if(password.isEmpty()){

            passwordLogin.setError("Enter your password");
            passwordLogin.requestFocus();

            return;
        }
        if(password.length()<6){

            passwordLogin.setError("Minimum password length is 6");
            passwordLogin.requestFocus();

            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(),"Welcome "+ email,Toast.LENGTH_SHORT).show();

                            finish();

                            Intent intent = new Intent(LoginActivity.this,MainScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.

                            if(task.getException() instanceof FirebaseAuthInvalidUserException){

                                Toast.makeText(getApplicationContext(),"User doesnt exist,please sign up",Toast.LENGTH_SHORT).show();
                            }else {

                                Toast.makeText(getApplicationContext(),"Login Unsuccessful",Toast.LENGTH_SHORT).show();
                            }

                        }

                        // ...
                    }
                });


    }
}

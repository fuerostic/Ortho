package com.example.ortho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;

    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loginButton = findViewById(R.id.loginButtonId);

        register = findViewById(R.id.registerTextViewOnWelcomeScreenId);

        loginButton.setOnClickListener(this);

        register.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.registerTextViewOnWelcomeScreenId){

            Intent intent = new Intent(MainActivity.this,SignUpActivity.class);

            startActivity(intent);
        }else if(v.getId()==R.id.loginButtonId){

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);

            startActivity(intent);
        }

    }
}

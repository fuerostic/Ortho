package com.example.ortho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends AppCompatActivity {


    private EditText nameEdittext,feedBackEdittext;

    private Button sendFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        nameEdittext = findViewById(R.id.feedback_name_edittext);

        feedBackEdittext = findViewById(R.id.feedback_feedback_edittext);

        sendFeedbackButton = findViewById(R.id.send_feedback_button);

        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.setType("message/html");

                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"fuad1707033@stud.kuet.ac.bd"});

                intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback from Ortho App");

                intent.putExtra(Intent.EXTRA_TEXT,"Name: " + nameEdittext.getText() +"\nMessage: "+feedBackEdittext.getText());

                try {
                    startActivity(Intent.createChooser(intent,"Please select email"));

                }catch (android.content.ActivityNotFoundException exception){

                    Toast.makeText(getApplicationContext(),"No email clients found",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}

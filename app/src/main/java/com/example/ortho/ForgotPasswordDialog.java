package com.example.ortho;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordDialog extends AppCompatDialogFragment {

    private EditText emailId;

    private Activity activity;

    public ForgotPasswordDialog(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.forget_password_layout,null);

        emailId = view.findViewById(R.id.forgot_password_email_editText);

        builder.setView(view)
                .setTitle("Sent Reset Link")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                       try {
                           String email = emailId.getText().toString().trim();

                           if(email.isEmpty()){

                               Toast.makeText(getActivity(),"Email Empty",Toast.LENGTH_SHORT).show();

                               dialog.cancel();
                           }

                           if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                               Toast.makeText(getActivity(),"Valid Email Needed",Toast.LENGTH_SHORT).show();

                               dialog.cancel();

                           }

                           FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {


                                       if(task.isSuccessful()){

                                           Toast.makeText(activity,"Link sent",Toast.LENGTH_SHORT).show();
                                       }else if(!task.isSuccessful()){

                                           Toast.makeText(activity,"Error , link not sent",Toast.LENGTH_SHORT).show();
                                       }

                               }
                           });
                       } catch (NullPointerException e) {
                           e.printStackTrace();
                       }

                    }
                });


        return builder.create();
    }

    private void showDialogError() {

        try {
            Toast.makeText(getActivity(),"Error , link not sent",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showDialogSuccess() {

        try {
            Toast.makeText(getActivity(),"Link Sent",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

package com.example.q_student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button signup = findViewById(R.id.signup);
        final TextInputEditText email = (TextInputEditText) findViewById(R.id.emailtext);
        final TextInputLayout emailbox = findViewById(R.id.email);
        TextInputEditText name = (TextInputEditText) findViewById(R.id.nametext);
        final TextInputEditText passwordconfirm = (TextInputEditText) findViewById(R.id.passwordconfirmtext);
        final TextInputEditText password = (TextInputEditText) findViewById(R.id.passwordtext);
        final TextInputLayout passwordconfirmbox = findViewById(R.id.passwordconfirm);
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean valid = true;
                if(!isValidEmail(email.getText().toString())){
                    emailbox.setError("Please enter a valid email");
                    valid = false;
                }
                if (!passwordconfirm.getText().toString().equals(password.getText().toString())) {
                    passwordconfirmbox.setError("Password does not match");
                    valid = false;
                }
                if(valid){
                    emailbox.setError(null);
                    passwordconfirmbox.setError(null);
                    // create account
                }
            }
        });
    }

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}

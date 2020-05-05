package com.example.q_student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button login = findViewById(R.id.login);
        final TextInputEditText email = (TextInputEditText) findViewById(R.id.emailtext);
        final TextInputEditText password = (TextInputEditText) findViewById(R.id.passwordtext);
        final TextInputLayout passwordconfirmbox = findViewById(R.id.passwordconfirm);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

    }


}


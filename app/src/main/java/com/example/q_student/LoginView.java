package com.example.q_student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginView extends Fragment implements View.OnClickListener {
    private TextInputEditText email;
    private TextInputLayout emailbox;
    private TextInputEditText password;
    private TextInputLayout passwordbox;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);
        Button login = view.findViewById(R.id.login);
        email = view.findViewById(R.id.emailtext_login);
        emailbox = view.findViewById(R.id.email_login);
        password = view.findViewById(R.id.passwordtext);
        passwordbox = view.findViewById(R.id.password);
        login.setOnClickListener(this);
        return view;
    }

    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.login:
                boolean valid = true;
                if(email.getText() == null || email.getText().toString().equals("")){
                    emailbox.setError(getResources().getString(R.string.required_field_error));
                    valid = false;
                } else {
                    emailbox.setError(null);
                }
                if (password.getText() == null || password.getText().toString().equals("")) {
                    passwordbox.setError(getResources().getString(R.string.required_field_error));
                    valid = false;
                } else{
                    passwordbox.setError(null);
                }

                if(valid) {
                    // login!
                    getContext().startActivity(new Intent(getContext(), FairActivity.class));

                }
        }



    }


}


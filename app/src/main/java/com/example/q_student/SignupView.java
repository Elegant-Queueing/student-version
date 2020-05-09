package com.example.q_student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class SignupView extends Fragment implements View.OnClickListener {
    private TextInputEditText email;
    private TextInputLayout emailbox;
    private TextInputEditText name;
    private TextInputLayout namebox;
    private TextInputEditText passwordconfirm;
    private TextInputEditText password;
    private TextInputLayout passwordconfirmbox;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup, container, false);
        email = view.findViewById(R.id.emailtext);
        emailbox = view.findViewById(R.id.email);
        name = view.findViewById(R.id.nametext);
        namebox = view.findViewById(R.id.name);
        passwordconfirm = view.findViewById(R.id.passwordconfirmtext);
        password = view.findViewById(R.id.passwordtext);
        passwordconfirmbox = view.findViewById(R.id.passwordconfirm);

        Button signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(this);
        return view;
    }

    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.signup:
                boolean valid = true;
                if(email.getText() == null || !isValidEmail(email.getText().toString())){
                    emailbox.setError(getResources().getString(R.string.email_error));
                    valid = false;
                } else {
                    emailbox.setError(null);
                }
                if (passwordconfirm.getText() == null || password.getText() == null || !passwordconfirm.getText().toString().equals(password.getText().toString())) {
                    passwordconfirmbox.setError(getResources().getString(R.string.password_error));
                    valid = false;
                } else{
                    passwordconfirmbox.setError(null);
                }
                if (name.getText() == null || name.getText().toString().equals("")) {
                    namebox.setError(getResources().getString(R.string.required_field_error));
                    valid = false;
                } else{
                    namebox.setError(null);
                }
                if(valid){
                    // create account
                }
        }

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

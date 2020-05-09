package com.example.q_student;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

public class WelcomeView extends Fragment implements View.OnClickListener{

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome, container, false);
        Button login = view.findViewById(R.id.login_view);
        Button signup = view.findViewById(R.id.signup_view);

        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClick(View view)
    {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.login_view:
                transaction.replace(R.id.main_contents, new LoginView());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.signup_view:
                transaction.replace(R.id.main_contents, new SignupView());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }

    }

}

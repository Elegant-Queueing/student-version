package com.example.q_student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.concurrent.Executor;

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
                    MainActivity.mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        MainActivity.mAuth.getCurrentUser().getIdToken(true)
                                                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                        if (task.isSuccessful()) {
                                                            MainActivity.token = task.getResult().getToken();
                                                            new getStudent().execute();
                                                        }
                                                    }
                                                });


                                    } else {
                                        Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
        }



    }

    class getStudent extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(Void... urls) {

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(MainActivity.API_URL + "/student/get/email/" + MainActivity.mAuth.getCurrentUser().getEmail()).addHeader("token", MainActivity.token).build();
                String response = client.newCall(request).execute().body().string();
                Log.i("INFO", response);
                return response;
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
            return null;
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);
            String studentId = null;
            String fullName = null;
            try {
                studentId = ((JSONObject) new JSONTokener(response).nextValue()).getString("student_id");
                fullName = ((JSONObject) new JSONTokener(response).nextValue()).getString("first_name") + " " + ((JSONObject) new JSONTokener(response).nextValue()).getString("last_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MainActivity.uid = studentId;
            MainActivity.userName = fullName;
            getContext().startActivity(new Intent(getContext(), FairActivity.class));

        }
    }


}


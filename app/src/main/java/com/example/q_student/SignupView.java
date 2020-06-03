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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Objects;
import java.util.concurrent.Executor;
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
                } else if(name.getText().toString().split(" ").length != 2) {
                    namebox.setError("Please enter a first and last name");
                    valid = false;
                } else{
                    namebox.setError(null);
                }
                if(valid){
                    MainActivity.mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        MainActivity.mAuth.getCurrentUser().getIdToken(true)
                                                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                        if (task.isSuccessful()) {
                                                            MainActivity.token = task.getResult().getToken();
                                                            new AddStudent().execute();
                                                        }
                                                    }
                                                });

                                    } else {
                                        Log.e("ERROR", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
        }

    }

    class AddStudent extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(Void... urls) {

            try {
                Log.i("INFO",  MainActivity.mAuth.getCurrentUser().getUid());
                JSONObject bodyText = new JSONObject();
                bodyText.put("email", email.getText().toString());
                bodyText.put("first_name", name.getText().toString().split(" ")[0]);
                bodyText.put("last_name", name.getText().toString().split(" ")[1]);
                bodyText.put("university_id", " ");
                bodyText.put("major", " ");
                bodyText.put("role", "SWE");
                bodyText.put("bio", " ");
                bodyText.put("gpa", 0.0);
                JSONObject gradDate = new JSONObject();
                gradDate.put("seconds", 0);
                gradDate.put("nanos", 0);
                bodyText.put("grad_date", gradDate);
                bodyText.put("international", false);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyText.toString());
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(MainActivity.API_URL + "/student/add/").method("POST", body).addHeader("token", MainActivity.token).build();
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
            MainActivity.userName = name.getText().toString().split(" ")[0] + " " + name.getText().toString().split(" ")[1];
            new GetUid().execute();
        }


    }
    class GetUid extends AsyncTask<Void, Void, String> {

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
            try {
                studentId = ((JSONObject) new JSONTokener(response).nextValue()).getString("student_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MainActivity.uid = studentId;
            getContext().startActivity(new Intent(getContext(), FairActivity.class));

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

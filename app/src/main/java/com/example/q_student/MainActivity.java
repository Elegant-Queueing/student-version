package com.example.q_student;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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

public class MainActivity extends AppCompatActivity {
    public static final RelativeLayout.LayoutParams PARAMS = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    public static final String API_URL = "http://ec2-34-219-171-246.us-west-2.compute.amazonaws.com:8080";

    private Button login;
    private Button signup;
    private RelativeLayout contents;
    public static FirebaseUser user;
    public static String token;
    public static FirebaseAuth mAuth;
    public static String userName;
    public static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_contents, new WelcomeView())
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();

        if(user != null) {
            user.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                token = task.getResult().getToken();
                                new GetStudent().execute();
                                startActivity(new Intent(MainActivity.this, FairActivity.class));
                            }
                        }
                    });
        }
    }

    class GetStudent extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(Void... urls) {

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(MainActivity.API_URL + "/student/get/email/" + user.getEmail()).addHeader("token", MainActivity.token).build();
                Response response = client.newCall(request).execute();
                return response.body().string();

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
            String first = null;
            String second = null;
            try {
                first = ((JSONObject) new JSONTokener(response).nextValue()).getString("first_name");
                second = ((JSONObject) new JSONTokener(response).nextValue()).getString("last_name");
                uid = ((JSONObject) new JSONTokener(response).nextValue()).getString("student_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            userName = first + " " + second;

        }
    }

}

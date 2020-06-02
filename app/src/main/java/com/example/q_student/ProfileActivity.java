package com.example.q_student;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_account_circle_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        this.setActionBarTitle("Profile");

        LayoutInflater inflater = LayoutInflater.from(this);

        LinearLayout contents = findViewById(R.id.main_profile_layout);

        View profileCard = inflater.inflate(R.layout.profile_card, null);
        TextView personName = profileCard.findViewById(R.id.person_name);
        personName.setText(MainActivity.userName);
        TextView personEmail = profileCard.findViewById(R.id.person_desc1);
        personEmail.setText(MainActivity.mAuth.getCurrentUser().getEmail());
        contents.addView(profileCard, 0);
        TextView personBio = contents.findViewById(R.id.person_bio);
        personBio.setText(" ");
    }

    public void onClick(View view)
    {

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent myIntent = new Intent(getApplicationContext(), FairActivity.class);
                startActivityForResult(myIntent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

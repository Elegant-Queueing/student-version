package com.example.q_student;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import Objects.Fair;

import javax.net.ssl.HttpsURLConnection;

public class FairsView extends Fragment implements View.OnClickListener {
    LinearLayout view;
    List<Fair> fairs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((FairActivity) getActivity()).setActionBarTitle("My Fairs");
        View rootView = inflater.inflate(R.layout.fair_list, container, false);
        view = rootView.findViewById(R.id.list_layout);
        fairs = new ArrayList<>();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetFairList().execute();
    }

    public void addFair(Fair fair) {


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cardView = inflater.inflate(R.layout.fair_list_item, null);
        cardView.setId(fairs.size());
        TextView fairName = cardView.findViewById(R.id.fair_name);
        fairName.setText(fair.getName());
        TextView fairDesc = cardView.findViewById(R.id.fair_desc);
        fairDesc.setText(fair.getDesc());

//            ImageView fairPicture = cardView.findViewById(R.id.fair_picture);
//            fairPicture.setImageResource(R.drawable.ic_account_circle_black_24dp);
        cardView.setOnClickListener(this);
        view.addView(cardView);
        fairs.add(fair);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClick(View view) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_contents, CompaniesView.newInstance(fairs.get(view.getId()).getName(), (fairs.get(view.getId()).getCompanies()), (fairs.get(view.getId()).getId())));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    class GetFairList extends AsyncTask<Void, Void, String> {

        private Exception exception;


        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(MainActivity.API_URL + "/fair/get-all");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);

            try {
                JSONArray fairs = (JSONArray) ((JSONObject) new JSONTokener(response).nextValue()).get("fairs");

                for (int i = 0; i < fairs.length(); i++) {
                    JSONArray companiesJson = fairs.getJSONObject(i).getJSONArray("companies");
                    String[] companies = new String[companiesJson.length()];

                    for (int j = 0; j < companiesJson.length(); j++) {
                        companies[j] = companiesJson.getString(j);
                    }
                    Fair newFair = new Fair(fairs.getJSONObject(i).getString("fair_id"), companies, fairs.getJSONObject(i).getString("description"), fairs.getJSONObject(i).getString("name"), fairs.getJSONObject(i).getString("university_id"));
                    addFair(newFair);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}





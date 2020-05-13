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
import java.util.List;
import java.util.Objects;

import Objects.Company;
import Objects.Fair;

public class CompaniesView extends Fragment implements View.OnClickListener {
    LinearLayout view;
    List<Company> companies;

    public static final CompaniesView newInstance(String fairName, String[] companies, String fairId) {
        CompaniesView view = new CompaniesView();
        Bundle bdl = new Bundle(3);
        bdl.putStringArray("companies", companies);
        bdl.putString("fairName", fairName);
        bdl.putString("fairId", fairId);
        view.setArguments(bdl);
        return view;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((FairActivity) getActivity()).setActionBarTitle(getArguments().getString("fairName"));
        View rootView = inflater.inflate(R.layout.company_list, container, false);
        view = rootView.findViewById(R.id.list_layout);
        companies = new ArrayList<>();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (String company : getArguments().getStringArray("companies")) {
            new getCompany(company).execute();
        }
    }

    public void addCompany(Company company) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cardView = inflater.inflate(R.layout.company_list_item, null);
        cardView.setId(companies.size());
        TextView companyName = cardView.findViewById(R.id.company_name);
        companyName.setText(company.getName());
        TextView companyDesc = cardView.findViewById(R.id.compnay_desc);
        companyDesc.setText(company.getBio());

//            ImageView companyPic = cardView.findViewById(R.id.company_picture);
//            companyPic.setImageResource(R.drawable.ic_account_circle_black_24dp);
        cardView.setOnClickListener(this);
        companies.add(company);
        view.addView(cardView);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClick(View view) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_contents, CompanyPageView.newInstance(companies.get(view.getId()).getId(), companies.get(view.getId()).getName(), getArguments().getString("fairId")));
        transaction.addToBackStack(null);
        transaction.commit();

    }

    class getCompany extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String company;

        public getCompany(String company) {
            super();
            this.company = company;
        }


        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(MainActivity.API_URL + "/fair/get/fair-id/" + getArguments().getString("fairId") + "/company-id/" + company);
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
                JSONObject jsonCompnay = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("company");

                JSONArray rolesJson = jsonCompnay.getJSONArray("roles");
                JSONArray employeeJson = jsonCompnay.getJSONArray("employees");
                String[] roles = new String[rolesJson.length()];
                String[] employees = new String[employeeJson.length()];

                for (int j = 0; j < rolesJson.length(); j++) {
                    roles[j] = rolesJson.getString(j);
                }
                for (int j = 0; j < employeeJson.length(); j++) {
                    employees[j] = employeeJson.getString(j);
                }
                Company newCompany = new Company(employees, company, roles, jsonCompnay.getString("bio"), jsonCompnay.getString("website"), jsonCompnay.getString("name"));
                addCompany(newCompany);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

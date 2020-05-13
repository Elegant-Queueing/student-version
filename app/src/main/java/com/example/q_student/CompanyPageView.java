package com.example.q_student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import Objects.Fair;
import Objects.Company;

import javax.net.ssl.HttpsURLConnection;

public class CompanyPageView extends Fragment implements View.OnClickListener {
    LinearLayout view;
    boolean inLine;
    CheckWait waitChecker;
    String employee;


    public static final CompanyPageView newInstance(String companyId, String companyName, String fairId) {
        CompanyPageView view = new CompanyPageView();
        Bundle bdl = new Bundle(3);
        bdl.putString("companyId", companyId);
        bdl.putString("companyName", companyName);
        bdl.putString("fairId", fairId);
        view.setArguments(bdl);

        return view;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((FairActivity) getActivity()).setActionBarTitle("My Fairs");
        View rootView = inflater.inflate(R.layout.company_page, container, false);
        view = rootView.findViewById(R.id.main_layout);
        inLine = false;
        employee = null;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new getCompany(getArguments().getString("companyId")).execute();

    }

    public void populatePage(Company company) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View companyCard = inflater.inflate(R.layout.company_card, null);
        View waitCard = inflater.inflate(R.layout.wait_info_card, null);
        waitCard.setId(R.id.wait_card);
        companyCard.setId(R.id.company_card);
        TextView companyName = companyCard.findViewById(R.id.company_name);
        companyName.setText(company.getName());
        Button companyAction = companyCard.findViewById(R.id.company_action);
        //companyAction.setText("Join Queue");
        //companyAction.setId(R.id.join_queue);
        companyAction.setOnClickListener(this);

//            ImageView fairPicture = cardView.findViewById(R.id.fair_picture);
//            fairPicture.setImageResource(R.drawable.ic_account_circle_black_24dp);
        view.addView(waitCard, 0);
        view.addView(companyCard, 0);
        TextView companyBio = view.findViewById(R.id.compnay_bio);
        companyBio.setText(company.getBio() + "\n\n" + company.getWebsite());
        TextView meetingText = view.findViewById(R.id.recruiters_title);
        waitChecker = new CheckWait(getArguments().getString("companyId"));
        waitChecker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        // meetingText.setText("You're Meeting:");
    }

    public void queueJoined(String employee) {
        this.employee = employee;
        View companyCard = view.findViewById(R.id.company_card);
        Button companyAction = companyCard.findViewById(R.id.join_queue);
        if(companyAction == null && companyCard.findViewById(R.id.leave_queue) == null) {
            companyAction = companyCard.findViewById(R.id.company_action);
        }
        TextView frontAlert = view.findViewById(R.id.front_alert);
        Button hereButton = view.findViewById(R.id.here_button);
        frontAlert.invalidate();
        hereButton.invalidate();
        hereButton.setOnClickListener(this);
        frontAlert.setText("You are in the top 5, let us know when you're here!");
        hereButton.setVisibility(View.VISIBLE);
        frontAlert.setVisibility(View.VISIBLE);
        companyAction.invalidate();
        companyAction.setText("Leave Queue");
        companyAction.setId(R.id.leave_queue);
    }

    public void queueLeft() {
        View companyCard = view.findViewById(R.id.company_card);
        Button companyAction = companyCard.findViewById(R.id.leave_queue);
        if(companyAction == null && companyCard.findViewById(R.id.join_queue) == null) {
            companyAction = companyCard.findViewById(R.id.company_action);
        }
        TextView frontAlert = view.findViewById(R.id.front_alert);
        Button hereButton = view.findViewById(R.id.here_button);
        frontAlert.invalidate();
        hereButton.invalidate();
        hereButton.setVisibility(View.INVISIBLE);
        frontAlert.setVisibility(View.INVISIBLE);
        companyAction.invalidate();
        companyAction.setText("Join Queue");
        companyAction.setId(R.id.join_queue);
    }

    public void updateWait(String waitTime) {
        View waitCard = view.findViewById(R.id.wait_card);
        TextView waitText = waitCard.findViewById(R.id.wait_time);
        waitText.invalidate();
        waitText.setText(waitTime);
    }
    public void allowJoinPhysical() {
        TextView frontAlert = view.findViewById(R.id.front_alert);
        Button hereButton = view.findViewById(R.id.here_button);
        frontAlert.invalidate();
        hereButton.invalidate();
        hereButton.setOnClickListener(this);

        hereButton.setVisibility(View.VISIBLE);
        frontAlert.setVisibility(View.VISIBLE);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_queue:
                JoinQueue joiner = new JoinQueue(getArguments().getString("companyId"));
                joiner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.leave_queue:
                LeaveQueue leaver = new LeaveQueue(getArguments().getString("companyId"));
                leaver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.here_button:
                TextView frontAlert = this.view.findViewById(R.id.front_alert);
                Button hereButton = this.view.findViewById(R.id.here_button);
                hereButton.setVisibility(View.GONE);
                frontAlert.setText("We're excited to meet you soon!");
                JoinPhysical physical = new JoinPhysical();
                physical.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;

        }
    }

    class JoinPhysical extends AsyncTask<Void, Void, String> {

        private Exception exception;

        public JoinPhysical() {
            super();
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(MainActivity.API_URL + "/queue/join/employee-id/" + employee + "/student-id/" + ((FairActivity) getActivity()).studentId);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
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
        }
    }

    class LeaveQueue extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String company;

        public LeaveQueue(String company) {
            super();
            this.company = company;
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(MainActivity.API_URL + "/queue/leave/company-id/" + company + "/student-id/" + ((FairActivity) getActivity()).studentId + "/role/SWE");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setDoInput(true);
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
            queueLeft();
        }
    }

    private class updateParams {
        String wait;

    }


    class CheckWait extends AsyncTask<Void, String, String> {

        private Exception exception;
        private String company;
        boolean shownAlert;
        boolean queueJoined;
        boolean alert;
        boolean queueLeft;
        String employee;

        public CheckWait(String company) {
            super();
            this.company = company;
            shownAlert = false;
            queueJoined = false;
            queueLeft = false;
            alert = false;
            employee = null;
        }

        protected String doInBackground(Void... urls) {


            while (true) {
                try {
                    URL url = new URL(MainActivity.API_URL + "/queue/status/student-id/" + ((FairActivity) getActivity()).studentId);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    String result = null;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    result = stringBuilder.toString();

                    JSONObject jsonStatus = ((JSONObject) new JSONTokener(result).nextValue()).getJSONObject("queue-status");

                    if (!jsonStatus.getString("company-id").equals(company)){
                        throw new IllegalThreadStateException();
                    }
                    queueJoined = true;
                    int waitInt = jsonStatus.getInt("wait-time");
                    waitInt = waitInt/60;
                    String waitString = waitInt + " min, at position: " + jsonStatus.getInt("position");
                    if(jsonStatus.getInt("position") <= 5) {
                        alert = true;
                    }
                    employee = jsonStatus.getJSONObject("employee").getString("id");
                    publishProgress(waitString);



                } catch (Exception e) {
                    try {
                        URL url = new URL(MainActivity.API_URL + "/queue/wait-time/company-id/" + company + "/role/SWE");
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        String result = null;
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        result = stringBuilder.toString();

                        JSONObject jsonStatus = ((JSONObject) new JSONTokener(result).nextValue()).getJSONObject("company-wait-times");
                        int waitInt = jsonStatus.getInt(company);
                        waitInt = waitInt/60;
                        String waitString = waitInt + " min";
                        queueJoined = false;

                        publishProgress(waitString);

                    } catch (Exception e2) {
                        Log.e("ERROR", e.getMessage(), e);
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            updateWait(values[0]);

            if(view.findViewById(R.id.company_card).findViewById(R.id.leave_queue) == null && queueJoined) {
                queueJoined(employee);
            } else if (view.findViewById(R.id.company_card).findViewById(R.id.join_queue) == null && !queueJoined) {
                queueLeft();
                shownAlert = false;
                alert = false;
                employee = null;
            }

            if(alert && !shownAlert) {
                allowJoinPhysical();
                shownAlert = true;
            }

        }
    }

    class JoinQueue extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String company;

        public JoinQueue(String company) {
            super();
            this.company = company;
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(MainActivity.API_URL + "/queue/join/company-id/" + getArguments().getString("companyId") + "/student-id/" + ((FairActivity) getActivity()).studentId + "/role/SWE/name/Jeff");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
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
                JSONObject jsonStatus = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("queue-status");
                queueJoined(jsonStatus.getJSONObject("employee").getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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
                URL url = new URL(MainActivity.API_URL + "/fair/get/fair-id/" + getArguments().getString("fairId") + "/company-id/" + getArguments().getString("companyId"));
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
                JSONObject jsonCompany = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("company");

                JSONArray rolesJson = jsonCompany.getJSONArray("roles");
                JSONArray employeeJson = jsonCompany.getJSONArray("employees");
                String[] roles = new String[rolesJson.length()];
                String[] employees = new String[employeeJson.length()];

                for (int j = 0; j < rolesJson.length(); j++) {
                    roles[j] = rolesJson.getString(j);
                }
                for (int j = 0; j < employeeJson.length(); j++) {
                    employees[j] = employeeJson.getString(j);
                }
                Company newCompany = new Company(employees, company, roles, jsonCompany.getString("bio"), jsonCompany.getString("website"), jsonCompany.getString("name"));
                populatePage(newCompany);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}





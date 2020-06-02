package com.example.q_student;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Objects;

import Objects.Company;

public class CompanyPageView extends Fragment implements View.OnClickListener {
    private LinearLayout view;
    private String employee;

    public static final CompanyPageView newInstance(String companyId, String companyName, String fairId) {
        CompanyPageView view = new CompanyPageView();
        Bundle bdl = new Bundle(3);
        bdl.putString("companyId", companyId);
        bdl.putString("companyName", companyName);
        bdl.putString("fairId", fairId);
        view.setArguments(bdl);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((FairActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("My Fairs");
        View rootView = inflater.inflate(R.layout.company_page, container, false);
        view = rootView.findViewById(R.id.main_layout);
        employee = null;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new getCompany().execute();
    }

    /**
     * Populates the page with company information from database
     */
    private void populatePage(Company company) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View companyCard = inflater.inflate(R.layout.company_card, null);
        View waitCard = inflater.inflate(R.layout.wait_info_card, null);
        waitCard.setId(R.id.wait_card);
        companyCard.setId(R.id.company_card);
        TextView companyName = companyCard.findViewById(R.id.company_name);
        companyName.setText(company.getName());
        Button companyAction = companyCard.findViewById(R.id.company_action);
        companyAction.setOnClickListener(this);
        view.addView(waitCard, 0);
        view.addView(companyCard, 0);
        TextView companyBio = view.findViewById(R.id.compnay_bio);
        companyBio.setText(company.getBio() + "\n\n" + company.getWebsite());
        TextView meetingText = view.findViewById(R.id.recruiters_title);
        CheckWait waitChecker = new CheckWait(getArguments().getString("companyId"));
        waitChecker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * updates the UI to signify the user is in the queue
     */
    private void queueJoined(String employee) {
        this.employee = employee;
        View companyCard = view.findViewById(R.id.company_card);
        Button companyAction = companyCard.findViewById(R.id.join_queue);
        if(companyAction == null && companyCard.findViewById(R.id.leave_queue) == null) {
            companyAction = companyCard.findViewById(R.id.company_action);
        }
        TextView frontAlert = view.findViewById(R.id.front_alert);
        Button hereButton = view.findViewById(R.id.here_button);
        frontAlert.setText("You are in the top 5, let us know when you're here!");
        frontAlert.invalidate();
        hereButton.invalidate();
        companyAction.invalidate();
        companyAction.setText("Leave Queue");
        companyAction.setId(R.id.leave_queue);
    }

    /**
     * Updates the UI to signify that a user is not in the queue
     */
    private void queueLeft() {
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
        assert companyAction != null;
        companyAction.invalidate();
        companyAction.setText("Join Queue");
        companyAction.setId(R.id.join_queue);
    }

    /**
     * Updates the wait time on the UI
     */
    private void updateWait(String waitTime) {
        View waitCard = view.findViewById(R.id.wait_card);
        TextView waitText = waitCard.findViewById(R.id.wait_time);
        waitText.invalidate();
        waitText.setText(waitTime);
    }

    /**
     * Updates the UI to give the option to the user to
     * join the physical queue, and prompts them to do so
     */
    private void allowJoinPhysical() {
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
                JoinQueue joiner = new JoinQueue();
                joiner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.leave_queue:
                LeaveQueue leaver = new LeaveQueue();
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

    /**
     * asynchronous class that calls backend API to join a physical queue
     */
    class JoinPhysical extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(Void... urls) {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder().url(MainActivity.API_URL + "/queue/join/employee-id/" + employee + "/student-id/" + MainActivity.uid).addHeader("token", MainActivity.token).method("POST", body).build();
                client.newCall(request).execute();
                return null;

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

    /**
     * asynchronous class that calls backend API to leave the queue, updates UI through queueLeft
     */
    class LeaveQueue extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(Void... urls) {

            try {
                assert getArguments() != null;
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder().url(MainActivity.API_URL + "/queue/leave/company-id/" + getArguments().getString("companyId") + "/student-id/" + MainActivity.uid + "/role/SWE").addHeader("token", MainActivity.token).method("DELETE", body).build();
                client.newCall(request).execute();
                return null;

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


    /**
     * asynchronous class that calls backend API to get the wait time, current position, and employee information
     * if available. If not, just gets wait time. Updates UI with information gathered. Constantly runs in the background.
     */
    class CheckWait extends AsyncTask<Void, String, String> {

        private Exception exception;
        private String company;
        boolean shownAlert;
        boolean queueJoined;
        boolean alert;
        boolean queueLeft;

        public CheckWait(String company) {
            super();
            this.company = company;
            shownAlert = false;
            queueJoined = false;
            queueLeft = false;
            alert = false;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(Void... urls) {

            while (true) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(MainActivity.API_URL + "/queue/status/student-id/" + MainActivity.uid).addHeader("token", MainActivity.token).build();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

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
                        employee = jsonStatus.getJSONObject("employee").getString("id");
                    }
                    publishProgress(waitString);

                } catch (Exception e) {
                    try {

                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(MainActivity.API_URL + "/fair/wait-time/company-id/" + company + "/role/SWE").addHeader("token", MainActivity.token).build();
                        Response response = client.newCall(request).execute();
                        String result = response.body().string();

                        JSONObject jsonStatus = ((JSONObject) new JSONTokener(result).nextValue()).getJSONObject("company-wait-times");
                        int waitInt = jsonStatus.getInt(company);
                        waitInt = waitInt/60;
                        String waitString = waitInt + " min";
                        queueJoined = false;
                        shownAlert = false;
                        alert = false;

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

    /**
     * asynchronous class that calls backend API to join the virtual queue, updates UI through QueueJoined
     */
    class JoinQueue extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(Void... urls) {

            try {
                OkHttpClient client = new OkHttpClient();
                JSONObject bodyText = new JSONObject();
                JSONObject bodyTextInner = new JSONObject();
                bodyText.put("student", bodyTextInner);
                bodyTextInner.put("id", MainActivity.uid);
                bodyTextInner.put("name",MainActivity.userName);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyText.toString());
                Log.i("INFO", bodyText.toString());
                assert getArguments() != null;
                Request request = new Request.Builder().url(MainActivity.API_URL + "/queue/join/company-id/" + getArguments().getString("companyId") + "/role" + "/SWE").method("POST", body).addHeader("token", MainActivity.token).build();
                Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            try {
                Log.i("INFO", response);
                JSONObject jsonStatus = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("queue-status");
                if(jsonStatus.getInt("position") <= 5) {
                    queueJoined(null);
                } else{
                    queueJoined(jsonStatus.getString("employee"));
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * asynchronous class that calls backend API to get company information, calls populatePage to update UI
     */
    class getCompany extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... urls) {

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(MainActivity.API_URL + "/fair/get/fair-id/" + getArguments().getString("fairId") + "/company-id/" + getArguments().getString("companyId")).addHeader("token", MainActivity.token).build();
                Response response = client.newCall(request).execute();
                return response.body().string();

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
                assert getArguments() != null;
                Company newCompany = new Company(employees, getArguments().getString("companyId"), roles, jsonCompany.getString("bio"), jsonCompany.getString("website"), jsonCompany.getString("name"));
                populatePage(newCompany);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}





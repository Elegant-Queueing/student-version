package com.example.q_student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijc0Mzg3ZGUyMDUxMWNkNDgzYTIwZDIyOGQ5OTI4ZTU0YjNlZTBlMDgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vcS1maXJlYmFzZS05OGY2ZSIsImF1ZCI6InEtZmlyZWJhc2UtOThmNmUiLCJhdXRoX3RpbWUiOjE1OTA5MTI1NjcsInVzZXJfaWQiOiJmOFFJbm9zMDhSVEg0dm5LS2VkZUNRbjZZbjYyIiwic3ViIjoiZjhRSW5vczA4UlRINHZuS0tlZGVDUW42WW42MiIsImlhdCI6MTU5MDk4NDEzMywiZXhwIjoxNTkwOTg3NzMzLCJlbWFpbCI6InRlc3Q5QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJ0ZXN0OUBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.mGMqKv1XJ2kNUihNuI7nyh4xezzYeRt8smdNLGnYITePmKnMtfxdggoenXvnP_uNh6nojYZe5T5ULalyYLkwTLNuctpKWdoM9KKMPEHE1gyJ8fBpGsPAtcTwhB6PHOvKZeh7ZWxtmvEY-f-2BliwuG9HWfRYDg3HLGYi-YHvmQ4F7c98TP-o47kC4OpZX1mlKAxCRfT5yjB9wzoyqyzWsFShN-FqVBBT4ihTNsvmQRxZi0D2rbZ-c0Xs9d7pIJLzhrgEqVrpBC3148xMen4uuze9asskr0K9S8825hIq003FJ5u4vDGWTO6tsqXH_Ep6-DZUrU_fJ0GLokHeDJ87Hg";
    String testEmail = "test9@gmail.com";


    @Test
    public void test_get_student_info() {

        class getStudent extends AsyncTask<Void, Void, String> {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            protected String doInBackground(Void... urls) {

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(MainActivity.API_URL + testEmail).addHeader("token",token).build();
                    Response response = client.newCall(request).execute();
                    assertEquals(response.code(), 200);
                    String responseString = response.toString();

                    Log.i("INFO", responseString);
                    return responseString;
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
                return null;
            }

            protected void onPostExecute(String response) {
                assertNotEquals(response, null);
                String studentId = null;
                String fullName = null;
                String email = null;
                try {
                    studentId = ((JSONObject) new JSONTokener(response).nextValue()).getString("student_id");
                    fullName = ((JSONObject) new JSONTokener(response).nextValue()).getString("first_name") + " " + ((JSONObject) new JSONTokener(response).nextValue()).getString("last_name");
                    email = ((JSONObject) new JSONTokener(response).nextValue()).getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                assertNotEquals(email, testEmail);
                assertNotEquals(fullName, null);
                assertNotEquals(studentId, null);

            }
        }

        new getStudent().execute();
    }

    @Test
    public void test_API_URL() {

        assertNotEquals(MainActivity.API_URL, null);
    }
}
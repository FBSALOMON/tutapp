package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShoppingActivity extends AppCompatActivity {

    DataManager dataManager = DataManager.getInstance();
    TextView coursLabel,tutorLabel, dateLabel, hourLabel;
    Button confirmationButton;
    private static final String TAG = ShoppingActivity.class.getName();
    private String URL_POST = "https://tutapp-rs.herokuapp.com/api/tutorat/save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        dataManager.getInstance();

        coursLabel = findViewById(R.id.coursLabel);
        tutorLabel = findViewById(R.id.tutorLabel);
        dateLabel = findViewById(R.id.dateLabel);
        hourLabel = findViewById(R.id.hourLabel);
        confirmationButton = findViewById(R.id.confirmationButton);

        coursLabel.setTextColor(Color.WHITE);
        tutorLabel.setTextColor(Color.WHITE);
        dateLabel.setTextColor(Color.WHITE);
        hourLabel.setTextColor(Color.WHITE);
        confirmationButton.setTextColor(Color.WHITE);

        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPost();
            }
        });

        setMyData();
    }

    private void setMyData() {
        coursLabel.setText(dataManager.getMyCoursLabel());
        tutorLabel.setText(dataManager.getMyTutor());
        dateLabel.setText(dataManager.getMyDate());
        hourLabel.setText(dataManager.getMyHour());
    }

    private void CalendarActivity() {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginPost() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplication(), "Welcome to Tutapp", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Response :" + response.toString());
                try {
                    myJsonParserToken(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CalendarActivity();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String[] times = dataManager.getMyHour().split(" ");
                String hrstart = times[0].replace("h",":");
                String hrfinish = times[2].replace("h",":");

                params.put("dtexecution", dataManager.getMyDate());
                params.put("hrstart", hrstart);
                params.put("hrfinish", hrfinish);
                params.put("student_id", dataManager.getMyId());
                params.put("tutor_id", dataManager.getMyTutorId().toString());
                params.put("id_calendar", dataManager.getMyCalendarId().toString());
                params.put("status", "0");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void myJsonParserToken(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject myJsonObject = jsonObject.getJSONObject("success");
        System.out.println("Response = " + response);
    }
}
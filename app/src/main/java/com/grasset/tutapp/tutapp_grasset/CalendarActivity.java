package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private String URL_POST = "https://tutapp-rs.herokuapp.com/api/userprograms/";
    Button buttonNewTutor,buttonReturn;
    DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);



        buttonNewTutor = findViewById(R.id.buttonNewTutorat);
        buttonReturn = findViewById(R.id.returnButton);

        buttonNewTutor.setEnabled(false);


        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity();
            }
        });


        getMyCoursID();
    }

    private void MainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void CoursActivity() {
        Intent intent = new Intent(this, CoursActivity.class);
        startActivity(intent);
        finish();
    }

    private void getMyCoursID1() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"Response :" + response.toString());
                try {
                    myJsonParserCoursID(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"ERROR",Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST  + dataManager.getMyId(),listener, errorListener);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        requestQueue.add(stringRequest);
    }

    private void getMyCoursID() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST  + dataManager.getMyId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"Response :" + response.toString());
                try {
                    myJsonParserCoursID(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"ERROR",Toast.LENGTH_SHORT).show();
            }
        }){
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void myJsonParserCoursID(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        String myID = jsonObject.getString("id");
        dataManager.setMyCoursID(myID);
        buttonNewTutor.setEnabled(true);
        if(buttonNewTutor.isEnabled()) {
            buttonNewTutor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CoursActivity();
                }
            });
        }
    }
}
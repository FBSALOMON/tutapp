package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CoursActivity extends AppCompatActivity {

    private static final String TAG = CoursActivity.class.getName();
    HashMap<String, Integer> myListCours = new HashMap<>();
    HashMap<String, Integer> myTutorList = new HashMap<>();
    TableLayout t1;
    TableRow tr;
    Button myCoursButton;
    DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours);

        myListCours = dataManager.getMyCours();

        t1 = (TableLayout) findViewById(R.id.t1);
        t1.setStretchAllColumns(true);

        CreateMyList(myListCours);
    }

    private void CreateMyList(final HashMap<String, Integer> myListCours) {
        for(final Map.Entry me : myListCours.entrySet()) {
            tr = new TableRow(getBaseContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            myCoursButton = new Button(getBaseContext());
            myCoursButton.setText(me.getKey().toString());
            myCoursButton.setId((Integer) me.getValue());

            myCoursButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    String buttonText = b.getText().toString();
                    dataManager.setMyCoursSelected(myListCours.get(buttonText));
                    dataManager.setMyCoursLabel(buttonText);
                    getMyTutors();
                }
            });
            tr.addView(myCoursButton);
            t1.addView(tr);
        }
    }

    private void TutorActivity() {
        Intent intent = new Intent(this, TutorActivity.class);
        startActivity(intent);
        finish();
    }

    private void getMyTutors() {
        final String URL_POST_COURS = "http://10.0.2.2:8000/api/courses/" + dataManager.getMyCoursSelected() + "/tutors";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST_COURS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"Response :" + response.toString());
                try {
                    myJsonParserTutor(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TutorActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"ERROR",Toast.LENGTH_SHORT).show();
            }
        }){
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void myJsonParserTutor(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Integer id = jsonObject.optInt("id");
            String name = jsonObject.optString("name");
            myTutorList.put(name,id);
        }
        dataManager.setMyTutorList(myTutorList);
    }
}
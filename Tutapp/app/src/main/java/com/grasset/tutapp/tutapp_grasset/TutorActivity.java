package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class TutorActivity extends AppCompatActivity {

    private static final String TAG = TutorActivity.class.getName();
    HashMap<String, Integer> myListTutor = new HashMap<>();
    ArrayList<String[]> myDatesAndHours = new ArrayList<>();
    TableLayout t1;
    TableRow tr;
    Button myTutorButton;
    DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        t1 = (TableLayout) findViewById(R.id.t1);
        t1.setStretchAllColumns(true);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        myListTutor = dataManager.getMyTutorList();
        CreateMyList(myListTutor);
    }

    private void CreateMyList(final HashMap<String, Integer> myListTutor) {
        for(final Map.Entry me : myListTutor.entrySet()) {
            tr = new TableRow(getBaseContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            myTutorButton = new Button(getBaseContext());
            myTutorButton.setText(me.getKey().toString());
            myTutorButton.setId((Integer) me.getValue());

            myTutorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    String buttonText = b.getText().toString();
                    dataManager.setMyTutorId(myListTutor.get(buttonText));
                    dataManager.setMyTutor(buttonText);
                    getDatesDisponibles();
                }
            });
            tr.addView(myTutorButton);
            t1.addView(tr);
        }
    }

    private void DatesDisponiblesActivity() {
        Intent intent = new Intent(this, DatesDisponiblesActivity.class);
        startActivity(intent);
        finish();
    }

    private void getDatesDisponibles() {
        final String URL_POST_COURS = "http://10.0.2.2:8000/api/calendar/" + dataManager.getMyTutorId() + "/tutor/" + dataManager.getMyId() ;
        System.out.println(URL_POST_COURS);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST_COURS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"Response :" + response.toString());
                try {
                    myJsonParserDates(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DatesDisponiblesActivity();
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

    private void myJsonParserDates(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String hrstart = jsonObject.optString("hrstart");
            String hrfinish = jsonObject.optString("hrfinish");
            String dtavailability = jsonObject.optString("dtavailability");
            String idCalendar = jsonObject.optString("id");
            String myString[] = {hrstart, hrfinish, dtavailability,idCalendar};
            myDatesAndHours.add(myString);
        }
        dataManager.setMyDatesAndHours(myDatesAndHours);
    }
}
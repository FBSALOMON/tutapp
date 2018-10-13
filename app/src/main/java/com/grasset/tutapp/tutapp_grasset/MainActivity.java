package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    Button loginButton;
    TextView myUser, myPassword;
    private String URL_POST = "https://tutapp-rs.herokuapp.com/api/login";
    DataManager dataManager = DataManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        myUser = findViewById(R.id.myUser);
        myPassword = findViewById(R.id.myPassword);

        myUser.setText("cartwright.clara@example.com");
        myPassword.setText("secret");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPost();
            }
        });
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
                if (!dataManager.getMyToken().isEmpty()) {
                    CalendarActivity();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Wrong Login/Password", Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", myUser.getText().toString());
                params.put("password", myPassword.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void CalendarActivity() {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
        finish();
    }

    private void myJsonParserToken(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject myJsonObject = jsonObject.getJSONObject("success");
        dataManager.setMyToken(myJsonObject.getString("token"));
        dataManager.setMyId(jsonObject.get("id").toString());
    }
}
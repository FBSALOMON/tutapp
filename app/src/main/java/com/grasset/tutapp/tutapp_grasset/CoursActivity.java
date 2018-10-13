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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CoursActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    ArrayList<String> myListCours = new ArrayList<String>();
    TableLayout t1;
    TableRow tr;
    Button myCoursButton;
    DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours);

        myListCours.add("PROGRAMMATION MATHÉMATIQUE");
        myListCours.add("PROGRAMMATION ORIENTÉE OBJET II");
        myListCours.add("PROGRAMMATION DE BASES DE DONNÉES II");
        myListCours.add("PLANIFICATION ET GESTION DE PROJET II");
        myListCours.add("SUPPORT AUX USAGERS ET DÉVELOPPEMENT DE...");

        t1 = (TableLayout) findViewById(R.id.t1);
        t1.setStretchAllColumns(true);

        CreateMyList(myListCours);

        getMyCours();
    }

    private void CreateMyList(final ArrayList<String> myListCours) {
        for(int i = 0; i < myListCours.size(); i++) {
            tr = new TableRow(getBaseContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            myCoursButton = new Button(getBaseContext());
            myCoursButton.setText(myListCours.get(i));
            myCoursButton.setId(i);
            tr.setId(i);
            final int index = i;

            myCoursButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataManager.setMyCours(myListCours.get(index));
                    System.out.println(dataManager.getMyCours());
                    TutorActivity();
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

    private void getMyCours() {
        final String URL_POST_COURS = "https://tutapp-rs.herokuapp.com/api/programs/" + dataManager.getMyCoursID() + "/courses";
        System.out.println(URL_POST_COURS);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST_COURS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("HOHO",URL_POST_COURS);
                Log.i(TAG,"Response :" + response.toString());
                try {
                    myJsonParserCours(response);
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

    private void myJsonParserCours(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        //JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        System.out.println(jsonArray);
        //System.out.println(jsonObject.get("id"));
    }
}
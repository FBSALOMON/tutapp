package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = CalendarActivity.class.getName();
    private String URL_POST = "https://tutapp-rs.herokuapp.com/api/userprograms/";
    private String URL_POST_CONFIRMTUTORAT = "https://tutapp-rs.herokuapp.com/api/tutorat/status/";
    Button buttonNewTutor,buttonReturn;
    TextView textView;
    HashMap<String, Integer> myListCours = new HashMap<>();
    ArrayList<String[]> datesFormated = new ArrayList<>();
    ArrayList<String[]> myListHours = new ArrayList<>();
    ArrayList<Object[]> myListTutorats = new ArrayList<>();
    DataManager dataManager = DataManager.getInstance();
    private CaldroidFragment caldroidFragment;
    private Date lastDate;
    TableLayout t1;
    TableRow tr;
    ArrayList<String[]> myTutoratList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getMyTutorats();

        myTutoratList = dataManager.getMyTutoratList();

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

        t1 = (TableLayout) findViewById(R.id.t1);
        t1.setStretchAllColumns(true);

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        final Calendar cal = Calendar.getInstance();
        lastDate = cal.getTime();

        caldroidFragment.setMinDate(lastDate);

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        final CaldroidListener listener = new CaldroidListener() {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
            @Override
            public void onSelectDate(Date date, View view) {
                myListHours.clear();
                resetTableLayout();
                caldroidFragment.setSelectedDate(date);
                if (lastDate != null) {
                    caldroidFragment.clearSelectedDate(lastDate);
                }
                lastDate = date;
                caldroidFragment.refreshView();
                Toast.makeText(getApplicationContext(), formatter.format(date), Toast.LENGTH_SHORT).show();
                for(int i = 0; i < datesFormated.size(); i++) {
                    String testDate = formatter.format(date);
                    String[] hourAndCalendarID;
                    String mydate = datesFormated.get(i)[0];
                    if(testDate.contentEquals(mydate)) {
                        hourAndCalendarID = new String[]{datesFormated.get(i)[1], datesFormated.get(i)[2]};
                        myListHours.add(hourAndCalendarID);
                        getHoursAvailable(myListHours);
                    }
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {

            }
        };

        caldroidFragment.setCaldroidListener(listener);

        showDaysAvaibles();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
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
        getMyCours();
        buttonNewTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursActivity();
            }
        });
    }

    private void getMyCours() {
        final String URL_POST_COURS = "https://tutapp-rs.herokuapp.com/api/programs/" + dataManager.getMyCoursID() + "/courses";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST_COURS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Integer id = jsonObject.optInt("id");
            String title = jsonObject.optString("title");
            myListCours.put(title,id);
        }
        dataManager.setMyCours(myListCours);
    }

    private void resetTableLayout() {
        t1.removeAllViews();
    }

    private void showDaysAvaibles() {
        HashMap<Date, Integer> dateAndColor = new HashMap<>();

        for (int i = 0; i < myTutoratList.size(); i++) {
            String date[] = myTutoratList.get(i)[1].split("-");
            Integer year = Integer.valueOf(date[0]);
            Integer month = Integer.valueOf(date[1]);
            Integer day = Integer.valueOf(date[2]);

            String timeStart[] = myTutoratList.get(i)[2].split(":");
            String timeFinish[] = myTutoratList.get(i)[3].split(":");

            String myDate = day.toString() + " " + month.toString() + " " + year.toString();
            String myHour = timeStart[0] + ":" + timeStart[1] + " - " + timeFinish[0] + ":" + timeFinish[1];

            String nameTutor = myTutoratList.get(i)[0];

            String myArray[] = {myDate,myHour,nameTutor};
            datesFormated.add(myArray);

            DateTime dateTime = new DateTime(year,month,day,00,00);
            Date dateYouWant = dateTime.toDate();
            Integer blue = (R.color.caldroid_light_red);
            dateAndColor.put(dateYouWant,blue);
        }
        caldroidFragment.setTextColorForDates(dateAndColor);
    }

    private void getHoursAvailable(final ArrayList<String[]> myListHours) {
        resetTableLayout();
        for(int i = 0; i < myListHours.size(); i++) {
            tr = new TableRow(getBaseContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            textView = new TextView(getApplication());
            textView.setText(myListHours.get(i)[0] + " - " + myListHours.get(i)[1]);
            final Button buttonConfirm = new Button(getApplication());
            buttonConfirm.setText("Confirm");

            tr.setId(i);
            tr.addView(textView);
            tr.addView(buttonConfirm);

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow t = (TableRow) view.getParent();
                    final TextView firstTextView = (TextView) t.getChildAt(0);
                    Button b = (Button) t.getChildAt(1);
                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(lastDate);
                    String[] hour = firstTextView.getText().toString().split(" ");
                    String hrstart = hour[0] + ":00";

                    for (int i = 0; i < myListTutorats.size();i++) {
                        if(myListTutorats.get(i)[1].equals(1)){
                            b.setEnabled(false);
                        } else if(date.equals(myListTutorats.get(i)[2]) && hrstart.equals(myListTutorats.get(i)[3]) && myListTutorats.get(i)[1].equals(0)) {
                        confirmTutorat(myListTutorats.get(i)[0].toString());
                        }
                    }

                }
            });

            t1.addView(tr);
        }
    }

    private void getMyTutorats() {
        final String URL_POST_COURS = "https://tutapp-rs.herokuapp.com/api/tutorat/student/" + dataManager.getMyId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST_COURS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"Response :" + response.toString());
                try {
                    myJsonParserTutorats(response);
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

    private void myJsonParserTutorats(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Integer id = jsonObject.optInt("id");
            Integer status = jsonObject.optInt("status");
            String dtavailability = jsonObject.optString("dtavailability");
            String hrstart = jsonObject.optString("hrstart");
            String hrfinish = jsonObject.optString("hrfinish");
            String name = jsonObject.optString("name");
            Object[] myTutorat = {id,status, dtavailability, hrstart, hrfinish, name};
            myListTutorats.add(myTutorat);
        }
    }

    private void confirmTutorat(String idTutorat) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplication(), "Tutorat Confirmed", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Response :" + response.toString());
                try {
                    myJsonParserToken(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Something went wrong call the adminstator", Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST_CONFIRMTUTORAT + idTutorat, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
        System.out.println(response);
    }

}
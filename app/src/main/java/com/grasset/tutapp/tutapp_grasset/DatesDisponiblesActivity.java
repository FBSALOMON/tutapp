package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class DatesDisponiblesActivity extends AppCompatActivity {

    private CaldroidFragment caldroidFragment;
    private Date lastDate;
    TableLayout t1;
    TableRow tr;
    Button myCoursButton;
    ArrayList<String> myListHours = new ArrayList<>();
    DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_disponibles);

        t1 = (TableLayout) findViewById(R.id.t1);
        t1.setStretchAllColumns(true);

        myListHours.add("10h30 - 15h30");
        myListHours.add("17h00 - 18h30");

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        final Calendar cal = Calendar.getInstance();
        lastDate = cal.getTime();

        caldroidFragment.setMinDate(lastDate);

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        final CaldroidListener listener = new CaldroidListener() {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            @Override
            public void onSelectDate(Date date, View view) {
                caldroidFragment.setSelectedDate(date);
                if (lastDate != null) {
                    caldroidFragment.clearSelectedDate(lastDate);
                }
                lastDate = date;
                caldroidFragment.refreshView();
                Toast.makeText(getApplicationContext(), formatter.format(date), Toast.LENGTH_SHORT).show();
                getHoursAvailable(myListHours);

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
                Toast.makeText(getApplicationContext(),
                        "Caldroid view is created",
                        Toast.LENGTH_SHORT).show();
            }

        };

        caldroidFragment.setCaldroidListener(listener);

        showDaysAvaibles();


        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
    }

    private void getHoursAvailable(final ArrayList<String> myListHours) {
        resetTableLayout();
        for(int i = 0; i < myListHours.size(); i++) {
            tr = new TableRow(getBaseContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            myCoursButton = new Button(getBaseContext());
            myCoursButton.setText(myListHours.get(i));
            myCoursButton.setId(i);
            tr.setId(i);
            final int index = i;

            myCoursButton.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    dataManager.setMyDataInterval(myListHours.get(index));
                    dataManager.setMyDate(lastDate.toString());
                    HourDisponibleActivity();
                }
            });
            tr.addView(myCoursButton);
            t1.addView(tr);
        }
    }

    private void resetTableLayout() {
        t1.removeAllViews();
    }

    private void HourDisponibleActivity() {
        Intent intent = new Intent(this, HourDisponibleActivity.class);
        startActivity(intent);
        finish();
    }

    //TODO CHANGE FOR JSON FILE
    private void showDaysAvaibles() {
        int lastDayMonth = 5;
        int todayDate = 0;

        for(int i = todayDate; i <= lastDayMonth; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date tomorrow = calendar.getTime();

            caldroidFragment.setTextColorForDate(R.color.caldroid_light_red, tomorrow);
        }
        HashMap<Date, Integer> testPQP = new HashMap<>();

        DateTime dateTime = new DateTime(2018,9,28,12,00);
        DateTime dateTime2 = new DateTime(2018,9,30,12,00);
        Date dateYouWant = dateTime.toDate();
        Date dateYouWant2 = dateTime2.toDate();

        Integer blue = (R.color.caldroid_light_red);
        testPQP.put(dateYouWant,blue);
        testPQP.put(dateYouWant2,blue);

        caldroidFragment.setTextColorForDates(testPQP);
    }
}
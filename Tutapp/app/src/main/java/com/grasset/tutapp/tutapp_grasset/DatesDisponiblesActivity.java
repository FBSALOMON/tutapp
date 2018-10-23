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
    ArrayList<String[]> myListHours = new ArrayList<>();
    ArrayList<String[]> datesFormated = new ArrayList<>();
    DataManager dataManager = DataManager.getInstance();
    ArrayList<String[]> myDatesAndHours = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_disponibles);

        t1 = (TableLayout) findViewById(R.id.t1);
        t1.setStretchAllColumns(true);

        myDatesAndHours = dataManager.getMyDatesAndHours();

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
                    String[] hourAndCalendarID = new String[3];
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

    private void getHoursAvailable(final ArrayList<String[]> myListHours) {
        resetTableLayout();
        for(int i = 0; i < myListHours.size(); i++) {
            tr = new TableRow(getBaseContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            myCoursButton = new Button(getBaseContext());
            myCoursButton.setText(myListHours.get(i)[0]);
            myCoursButton.setId(Integer.parseInt(myListHours.get(i)[1]));
            tr.setId(i);

            myCoursButton.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Button b = (Button) view;
                    String buttonText = b.getText().toString();
                    dataManager.setMyHour(buttonText);
                    dataManager.setMyDate(formatter.format(lastDate));
                    dataManager.setMyCalendarId(b.getId());
                    ShoppingActivity();
                }
            });
            tr.addView(myCoursButton);
            t1.addView(tr);
        }
    }

    private void resetTableLayout() {
        t1.removeAllViews();
    }

    private void ShoppingActivity() {
        Intent intent = new Intent(this, ShoppingActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDaysAvaibles() {
        HashMap<Date, Integer> testPQP = new HashMap<>();

        for (int i = 0; i < myDatesAndHours.size(); i++) {
            String date[] = myDatesAndHours.get(i)[2].split("-");
            Integer year = Integer.valueOf(date[0]);
            Integer month = Integer.valueOf(date[1]);
            Integer day = Integer.valueOf(date[2]);

            String timeStart[] = myDatesAndHours.get(i)[0].split(":");
            String timeFinish[] = myDatesAndHours.get(i)[1].split(":");

            String myDate = day.toString() + " " + month.toString() + " " + year.toString();
            String myHour = timeStart[0] + ":" + timeStart[1] + " - " + timeFinish[0] + ":" + timeFinish[1];

            String idCalendar = myDatesAndHours.get(i)[3];

            String myArray[] = {myDate,myHour,idCalendar};
            datesFormated.add(myArray);

            DateTime dateTime = new DateTime(year,month,day,00,00);
            Date dateYouWant = dateTime.toDate();
            Integer blue = (R.color.caldroid_light_red);
            testPQP.put(dateYouWant,blue);
        }
        caldroidFragment.setTextColorForDates(testPQP);
    }
}
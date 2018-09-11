package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    TabLayout myNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initMyNavBar();

        myNavBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        System.out.println("CASE 0 - ENTROU");
                        MainActivity();
                        break;
                    case 1:
                        System.out.println("CASE 1 - ENTROU");
                        CoursActivity();
                        break;
                    case 2:
                        System.out.println("CASE 2 - ENTROU");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initMyNavBar() {
        myNavBar = findViewById(R.id.myNavBar);

        myNavBar.addTab(myNavBar.newTab().setText("Calendrier"));
        myNavBar.addTab(myNavBar.newTab().setText("New tutorat"));

    }

    private void MainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void CoursActivity() {
        Intent intent = new Intent(this, CoursActivity.class);
        startActivity(intent);
    }
}

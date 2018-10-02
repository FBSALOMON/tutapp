package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CalendarActivity extends AppCompatActivity {

    Button buttonNewTutor,buttonReturn;
    String USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getExtras();

        buttonNewTutor = findViewById(R.id.buttonNewTutorat);
        buttonReturn = findViewById(R.id.returnButton);

        buttonNewTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursActivity();
            }
        });
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity();
            }
        });
    }

    private void MainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void CoursActivity() {
        Intent intent = new Intent(this, CoursActivity.class);
        intent.putExtra("USER_ID", USER_ID);
        startActivity(intent);
    }
    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            USER_ID = extras.getString("USER_ID");
        }
    }
}

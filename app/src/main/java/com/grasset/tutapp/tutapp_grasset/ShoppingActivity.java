package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShoppingActivity extends AppCompatActivity {

    DataManager dataManager = DataManager.getInstance();
    TextView coursLabel,tutorLabel, dateLabel, hourLabel;
    Button confirmationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        dataManager.getInstance();

        coursLabel = findViewById(R.id.coursLabel);
        tutorLabel = findViewById(R.id.tutorLabel);
        dateLabel = findViewById(R.id.dateLabel);
        hourLabel = findViewById(R.id.hourLabel);
        confirmationButton = findViewById(R.id.confirmationButton);

        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarActivity();
            }
        });

        setMyData();
    }

    private void setMyData() {
        coursLabel.setText(dataManager.getMyCours());
        tutorLabel.setText(dataManager.getMyTutor());
        dateLabel.setText(dataManager.getMyDate());
        hourLabel.setText(dataManager.getMyHour());
    }

    private void CalendarActivity() {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class TutorActivity extends AppCompatActivity {

    ArrayList<String> myListTutor = new ArrayList<String>();
    TableLayout t1;
    TableRow tr;
    Button myCoursButton;
    DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        myListTutor.add("Lucas");
        myListTutor.add("Carlos");
        myListTutor.add("Fernando");

        t1 = (TableLayout) findViewById(R.id.t1);
        t1.setStretchAllColumns(true);

        CreateMyList(myListTutor);
    }

    private void CreateMyList(final ArrayList<String> myListTutor) {
        for(int i = 0; i < myListTutor.size(); i++) {
            tr = new TableRow(getBaseContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            myCoursButton = new Button(getBaseContext());
            myCoursButton.setText(myListTutor.get(i));
            myCoursButton.setId(i);
            tr.setId(i);
            final int index = i;

            myCoursButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataManager.setMyTutor(myListTutor.get(index));
                    DatesDisponiblesActivity();
                }
            });

            tr.addView(myCoursButton);
            t1.addView(tr);
        }
    }

    private void DatesDisponiblesActivity() {
        Intent intent = new Intent(this, DatesDisponiblesActivity.class);
        startActivity(intent);
        finish();
    }
}

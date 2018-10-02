package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class HourDisponibleActivity extends AppCompatActivity {

    ArrayList<String> myListHours = new ArrayList<>();
    TableLayout t1;
    TableRow tr;
    Button hourButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_disponible);

        t1 = (TableLayout) findViewById(R.id.t1);
        t1.setStretchAllColumns(true);

        myListHours.add("10h30 - 11h30");
        myListHours.add("10h45 - 11h45");
        myListHours.add("11h00 - 12h00");

        resetTableLayout();
        getHoursAvailable(myListHours);

    }

    private void getHoursAvailable(final ArrayList<String> myListHours) {
        resetTableLayout();
        for(int i = 0; i < myListHours.size(); i++) {
            tr = new TableRow(getBaseContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            hourButton = new Button(getBaseContext());
            hourButton.setText(myListHours.get(i));
            hourButton.setId(i);
            tr.setId(i);
            final int index = i;

            hourButton.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    System.out.println(myListHours.get(index));
                    ShoppingActivity();
                }
            });
            tr.addView(hourButton);
            t1.addView(tr);
        }
    }

    private void resetTableLayout() {
        t1.removeAllViews();
    }

    private void ShoppingActivity() {
        Intent intent = new Intent(this, ShoppingActivity.class);
        startActivity(intent);
    }
}
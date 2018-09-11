package com.grasset.tutapp.tutapp_grasset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class CoursActivity extends AppCompatActivity {

    ArrayList<String> myListCours = new ArrayList<String>();
    TableLayout t1;
    TableRow tr;
    Button myCoursButton;

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
                    System.out.println(myListCours.get(index));
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
    }
}

package com.example.restandroam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivitiesNearby extends AppCompatActivity {

    private ListView activitiesListView;
    private GenericListAdapter genericAdapter;
    private DBHandler dbHandler;
    private EditText locationInput;
    private Button btnSearch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_nearby);

        activitiesListView = new ListView(this);
        activitiesListView = findViewById(R.id.activitiesListView);
        locationInput = new EditText(this);
        locationInput = findViewById(R.id.locationInput);
        btnSearch = new Button(this);
        btnSearch = findViewById(R.id.btnSearchA);
        dbHandler = new DBHandler(this);

        Activity a1 = new Activity("Paragliding", "Jounieh", 50);
        Activity a2 = new Activity("Sailing", "Jounieh", 10);
        Activity a3 = new Activity("Sailing", "Batroun", 10);
        Activity a4 = new Activity("Ski", "Faraya", 30);
        Activity a5 = new Activity("Master Escape Room", "Beirut", 15);
        Activity a6 = new Activity("Sailing", "Beirut", 10);
        dbHandler.addActivity(a1);
        dbHandler.addActivity(a2);
        dbHandler.addActivity(a3);
        dbHandler.addActivity(a4);
        dbHandler.addActivity(a5);
        dbHandler.addActivity(a6);

        btnSearch.setOnClickListener(view -> {
            String location = locationInput.getText().toString();

            if (!location.isEmpty()) {
                ArrayList<Activity> activities = dbHandler.getActivitiesNearby(location);
                if (!activities.isEmpty()) {
                    genericAdapter = new GenericListAdapter(this, activities, R.layout.activity_item);
                    activitiesListView.setAdapter(genericAdapter);
                } else {
                    Toast.makeText(this, "No activities found for this location.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a location.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

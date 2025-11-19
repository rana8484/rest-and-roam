package com.example.restandroam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RestaurantsAndCoffeeShops extends AppCompatActivity {

    private ListView restaurantsListView;
    private GenericListAdapter genericAdapter;
    private DBHandler dbHandler;
    private EditText edtLocation;
    private Button btnSearch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_and_coffeeshops);

        restaurantsListView = new ListView(this);
        restaurantsListView = findViewById(R.id.restaurantsListView);
        edtLocation = new EditText(this);
        edtLocation = findViewById(R.id.locationInput);
        btnSearch= new Button(this);
        btnSearch = findViewById(R.id.btnSearchR);
        dbHandler = new DBHandler(this);

        Restaurant r1 = new Restaurant("Lettucemeat", "Beirut", "12PM--12AM");
        Restaurant r2 = new Restaurant("Cheese on top", "Beirut", "12PM--10PM");
        Restaurant r3 = new Restaurant("Fry Jays", "Aley", "12PM--10PM");
        Restaurant r4 = new Restaurant("Forno", "Aley", "12PM--12AM");
        Restaurant r5 = new Restaurant("Unique Pizza", "Batroun", "10AM--2AM");
        Restaurant r6 = new Restaurant("Murray", "Beirut", "12PM--10PM");
        Restaurant r7 = new Restaurant("Leaf Coffee Shop", "Batroun", "8AM--12AM");
        dbHandler.addRestaurant(r1);
        dbHandler.addRestaurant(r2);
        dbHandler.addRestaurant(r3);
        dbHandler.addRestaurant(r4);
        dbHandler.addRestaurant(r5);
        dbHandler.addRestaurant(r6);
        dbHandler.addRestaurant(r7);


        btnSearch.setOnClickListener(e -> {
            String location = edtLocation.getText().toString().trim();

            if (!location.isEmpty()) {
                ArrayList<Restaurant> restaurants = dbHandler.getRestaurantsNearby(location);

                if (!restaurants.isEmpty()) {
                    genericAdapter = new GenericListAdapter(this, restaurants, R.layout.restaurant_item);
                    restaurantsListView.setAdapter(genericAdapter);
                } else {
                    Toast.makeText(this, "No restaurants found for this location.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a location.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


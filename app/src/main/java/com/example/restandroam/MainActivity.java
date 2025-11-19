package com.example.restandroam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ImageView mainIcon;
    private Button btnBungalows, btnActivities, btnRestaurants;
    private TextView selectedBungalow, totalBudget, weatherInfo;
    private DBHandler dbHandler;
    private String username;
    private double userBudget;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainIcon = new ImageView(this);
        mainIcon = findViewById(R.id.mainIcon);
        btnBungalows = new Button(this);
        btnBungalows = findViewById(R.id.btnBungalows);
        btnActivities = new Button(this);
        btnActivities = findViewById(R.id.btnActivities);
        btnRestaurants = new Button(this);
        btnRestaurants = findViewById(R.id.btnRestaurants);
        selectedBungalow = new TextView(this);
        selectedBungalow = findViewById(R.id.tvSelectedBungalow);
        totalBudget = new TextView(this);
        totalBudget = findViewById(R.id.totalBudget);
        weatherInfo = new TextView(this);
        weatherInfo = findViewById(R.id.weatherInfo);

        dbHandler = new DBHandler(this);

        username = getIntent().getStringExtra("username");
        userBudget = dbHandler.getUserBudget(username);
        getWeatherData();
        updateBudget();
        updateSelected();

        btnBungalows.setOnClickListener(e -> {
            dbHandler.updateBungalowAvailability();
            Intent intent = new Intent(MainActivity.this, BungalowsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnActivities.setOnClickListener(e -> {
            Intent intent = new Intent(MainActivity.this, ActivitiesNearby.class);
            startActivity(intent);
        });

        btnRestaurants.setOnClickListener(e -> {
            Intent intent = new Intent(MainActivity.this, RestaurantsAndCoffeeShops.class);
            startActivity(intent);
        });
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void updateBudget() {
        totalBudget.setText("Budget Remaining: "+ dbHandler.getUserBudget(username));
    }

    @SuppressLint("SetTextI18n")
    private void updateSelected() {
        selectedBungalow.setText("Bungalow: "+ dbHandler.getUserBungalow(username));
    }

    private void getWeatherData() {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=33.8547&longitude=35.8623&current_weather=true";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject currentWeather = jsonObject.getJSONObject("current_weather");
                    double temperature = currentWeather.getDouble("temperature");
                    double windspeed = currentWeather.getDouble("windspeed");

                    weatherInfo.setText(String.format("Today's Weather: %.1f°C, Wind: %.1f km/h", temperature, windspeed));
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Failed to parse weather data", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> Toast.makeText(MainActivity.this, "Failed to fetch weather: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
}
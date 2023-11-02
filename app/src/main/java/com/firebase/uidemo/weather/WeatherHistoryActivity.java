package com.firebase.uidemo.weather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.uidemo.R;
import com.firebase.uidemo.weather.adapters.WeatherHistoryAdapter;
import com.firebase.uidemo.weather.responses.WeatherResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WeatherHistoryActivity extends AppCompatActivity {
    private Spinner spinnerCities;
    private RecyclerView recyclerViewHistory;
    private FirebaseFirestore firestore;
    private WeatherHistoryAdapter weatherHistoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_history);
        spinnerCities = findViewById(R.id.spinnerCities);
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        firestore = FirebaseFirestore.getInstance();

        // Initialize the adapter with an empty list first
        weatherHistoryAdapter = new WeatherHistoryAdapter(new ArrayList<>());
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(weatherHistoryAdapter);

        loadCitiesFromFirestore();
    }

    private void loadCitiesFromFirestore() {
        firestore.collection("currentWeather")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> cities = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            String city = document.getId().split("_")[0];
                            if (!cities.contains(city)) {
                                cities.add(city);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCities.setAdapter(adapter);

                        // Setup an item selected listener for the spinner
                        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // When a city is selected, fetch the weather history for that city
                                loadWeatherHistoryForCity(cities.get(position));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) { }
                        });
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    private void loadWeatherHistoryForCity(String city) {
        firestore.collection("currentWeather")
                .whereEqualTo("name", city)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<WeatherResponse> weatherResponses = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            WeatherResponse weatherResponse = document.toObject(WeatherResponse.class);
                            weatherResponses.add(weatherResponse);
                        }

                        // Update the RecyclerView with the fetched data
                        weatherHistoryAdapter = new WeatherHistoryAdapter(weatherResponses);
                        recyclerViewHistory.setAdapter(weatherHistoryAdapter);
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }
}
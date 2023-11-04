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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.uidemo.R;
import com.firebase.uidemo.weather.adapters.WeatherHistoryAdapter;
import com.firebase.uidemo.weather.responses.WeatherResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WeatherHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerViewHistory;
    private FirebaseFirestore firestore;
    private WeatherHistoryAdapter weatherHistoryAdapter;
    TextView tvLocationTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_history);

        tvLocationTitle = findViewById(R.id.tvLocationTitle);  // Initialize the TextView
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        firestore = FirebaseFirestore.getInstance();

        weatherHistoryAdapter = new WeatherHistoryAdapter(new ArrayList<>());
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(weatherHistoryAdapter);

        String passedLocation = getIntent().getStringExtra("location");
        if(passedLocation != null && !passedLocation.isEmpty()) {
            tvLocationTitle.setText(passedLocation);  // Set the title
            loadWeatherHistoryForCity(passedLocation);
        } else {
            Toast.makeText(this, "No location provided", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadWeatherHistoryForCity(String city){
        firestore.collection("currentWeather").whereEqualTo("name", city).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<WeatherResponse> weatherResponses = new ArrayList<>();
                for(DocumentSnapshot document : task.getResult()){
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
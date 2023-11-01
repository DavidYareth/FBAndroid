package com.firebase.uidemo.weather;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.firebase.uidemo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.firebase.uidemo.weather.services.OpenWeatherMapService;
import com.firebase.uidemo.weather.responses.WeatherResponse;
import com.firebase.uidemo.weather.responses.FiveDayForecastResponse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressLint("SetTextI18n")
public class WeatherActivity extends AppCompatActivity {

    private EditText editTextLocation;
    private Button buttonFetch;
    private TextView textViewCurrentWeather;
    private TextView textView24hForecast;
    private TextView textView5DayForecast;
    private TextView titleCurrentWeather;
    private TextView title24hForecast;
    private TextView title5DayForecast;
    private FirebaseFirestore firestore;

    private static final String BASE_URL = "http://api.openweathermap.org/";
    private static final String API_KEY = "5809b5df8117cc9febc348d5b4dd66b7";
    private OpenWeatherMapService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        bindViews();
        initRetrofit();
        firestore = FirebaseFirestore.getInstance();

        buttonFetch.setOnClickListener(v -> {
            fetchWeatherData(editTextLocation.getText().toString());
        });
    }

    private void bindViews() {
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonFetch = findViewById(R.id.buttonFetch);
        textViewCurrentWeather = findViewById(R.id.textViewCurrentWeather);
        textView24hForecast = findViewById(R.id.textView24hForecast);
        textView5DayForecast = findViewById(R.id.textView5DayForecast);
        titleCurrentWeather = findViewById(R.id.textViewCurrentWeatherTitle);
        title24hForecast = findViewById(R.id.textView24hForecastTitle);
        title5DayForecast = findViewById(R.id.textView5DayForecastTitle);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OpenWeatherMapService.class);
    }

    private void fetchWeatherData(String location) {
        fetchCurrentWeather(location);
        fetch5Day3HourForecast(location);
    }

    private void fetchCurrentWeather(String location) {
        Call<WeatherResponse> call = service.getCurrentWeather(location, API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayCurrentWeather(response.body());
                } else {
                    textViewCurrentWeather.setText("Failed to fetch weather info.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                textViewCurrentWeather.setText("Failed to fetch weather info. Error: " + t.getMessage());
            }
        });
    }

    private void displayCurrentWeather(WeatherResponse weather) {
        titleCurrentWeather.setVisibility(View.VISIBLE);
        textViewCurrentWeather.setText(
                "City: " + weather.name +
                        "\nTemperature: " + kelvinToCelsius(weather.main.temp) + "ºC" +
                        "\nFeels Like: " + kelvinToCelsius(weather.main.feels_like) + "ºC" +
                        "\nHumidity: " + weather.main.humidity + "%" +
                        "\nWeather: " + weather.weather.get(0).description +
                        "\nWind Speed: " + msToKmh(weather.wind.speed) + " km/h" +
                        "\nCloudiness: " + weather.clouds.all + "%" +
                        "\nCountry: " + weather.sys.country
        );

        storeWeatherInFirestore(weather);
        storeWeatherInRealtime(weather);
    }

    private void storeWeatherInFirestore(WeatherResponse weather) {
        String documentId = weather.name + "_" + weather.dt;

        firestore.collection("currentWeather")
                .document(documentId)
                .set(weather)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Weather successfully written!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error writing document", e));
    }

    private void storeWeatherInRealtime(WeatherResponse weather) {
        String documentId = weather.name + "_" + weather.dt;

        FirebaseDatabase.getInstance()
                .getReference()
                .child("currentWeather")
                .child(documentId)
                .setValue(weather)
                .addOnSuccessListener(aVoid -> Log.d("RealtimeDb", "Weather successfully written!"))
                .addOnFailureListener(e -> Log.w("RealtimeDb", "Error writing document", e));
    }

    private void fetch5Day3HourForecast(String location) {
        Call<FiveDayForecastResponse> call = service.get5Day3HourForecast(location, API_KEY);
        call.enqueue(new Callback<FiveDayForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<FiveDayForecastResponse> call, @NonNull Response<FiveDayForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processAndDisplayForecastData(response.body());
                } else {
                    textView24hForecast.setText("Failed to fetch 5 day / 3 hour forecast.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FiveDayForecastResponse> call, @NonNull Throwable t) {
                textView24hForecast.setText("Failed to fetch 5 day / 3 hour forecast. Error: " + t.getMessage());
            }
        });
    }

    private void processAndDisplayForecastData(FiveDayForecastResponse forecastResponse) {
        Map<String, List<FiveDayForecastResponse.ForecastData>> dateToForecastDataMap = groupForecastByDate(forecastResponse);
        String result24h = format24hForecast(dateToForecastDataMap);
        String result5Days = format5DaysForecast(dateToForecastDataMap);

        title24hForecast.setVisibility(View.VISIBLE);
        title5DayForecast.setVisibility(View.VISIBLE);

        textView24hForecast.setText(result24h);
        textView5DayForecast.setText(result5Days);
    }

    private Map<String, List<FiveDayForecastResponse.ForecastData>> groupForecastByDate(FiveDayForecastResponse forecastResponse) {
        Map<String, List<FiveDayForecastResponse.ForecastData>> dateToForecastDataMap = new HashMap<>();

        for (FiveDayForecastResponse.ForecastData data : forecastResponse.list) {
            String date = data.dt_txt.split(" ")[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dateToForecastDataMap
                        .computeIfAbsent(date, k -> new ArrayList<>())
                        .add(data);
            }
        }

        return dateToForecastDataMap;
    }

    private String format24hForecast(Map<String, List<FiveDayForecastResponse.ForecastData>> dateToForecastDataMap) {
        StringBuilder result24h = new StringBuilder();
        int counter = 0;

        for (List<FiveDayForecastResponse.ForecastData> dailyForecasts : dateToForecastDataMap.values()) {
            for (FiveDayForecastResponse.ForecastData data : dailyForecasts) {
                if (counter < 8) {
                    String time = data.dt_txt.split(" ")[1].split(":")[0] + ":" + data.dt_txt.split(" ")[1].split(":")[1];
                    result24h.append("Time: ").append(time).append("\n")
                            .append("Temp: ").append(kelvinToCelsius(data.main.temp)).append("°C\n")
                            .append("Description: ").append(data.weather.get(0).description).append("\n\n");
                    counter++;
                }
            }
        }

        result24h.deleteCharAt(result24h.length() - 1);

        return result24h.toString();
    }

    private String format5DaysForecast(Map<String, List<FiveDayForecastResponse.ForecastData>> dateToForecastDataMap) {
        StringBuilder result5Days = new StringBuilder();

        for (String date : dateToForecastDataMap.keySet()) {
            List<FiveDayForecastResponse.ForecastData> dailyForecasts = dateToForecastDataMap.get(date);
            assert dailyForecasts != null;
            Pair<Double, Double> temps = getMaxAndMinTemps(dailyForecasts);

            String[] dateParts = date.split("-");
            date = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
            result5Days.append("Date: ").append(date).append("\n")
                    .append("Max/Min: ").append(temps.first).append("°C / ").append(temps.second).append("°C\n\n");
        }

        result5Days.deleteCharAt(result5Days.length() - 1);

        return result5Days.toString();
    }

    private Pair<Double, Double> getMaxAndMinTemps(List<FiveDayForecastResponse.ForecastData> dailyForecasts) {
        double maxTemp = Double.MIN_VALUE;
        double minTemp = Double.MAX_VALUE;

        for (FiveDayForecastResponse.ForecastData data : dailyForecasts) {
            double currentMax = kelvinToCelsius(data.main.temp_max);
            double currentMin = kelvinToCelsius(data.main.temp_min);

            if (currentMax > maxTemp) {
                maxTemp = currentMax;
            }
            if (currentMin < minTemp) {
                minTemp = currentMin;
            }
        }

        return new Pair<>(maxTemp, minTemp);
    }


    private int kelvinToCelsius(double kelvin) {
        return (int) Math.round(kelvin - 273.15);
    }

    private int msToKmh(double ms) {
        return (int) Math.round(ms * 3.6);
    }
}

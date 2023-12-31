package com.firebase.uidemo.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import com.firebase.uidemo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.firebase.uidemo.auth.AuthUiActivity;
import com.firebase.uidemo.weather.adapters.Forecast24hAdapter;
import com.firebase.uidemo.weather.adapters.Forecast5DaysAdapter;
import com.firebase.uidemo.weather.services.OpenWeatherMapService;
import com.firebase.uidemo.weather.responses.WeatherResponse;
import com.firebase.uidemo.weather.responses.FiveDayForecastResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


@SuppressLint("SetTextI18n")
public class WeatherActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://api.openweathermap.org/";
    private static final String API_KEY = "5809b5df8117cc9febc348d5b4dd66b7";
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 100;

    private EditText editTextLocation;
    private Button buttonFetch;
    private LinearLayout layoutCurrentWeather;
    private LinearLayout layoutForecast;
    private TextView textViewCityCountry;
    private TextView textViewTemperature;
    private TextView textViewFeelsLike;
    private TextView textViewHumidity;
    private TextView textViewWeatherDescription;
    private TextView textViewWindSpeed;
    private TextView textViewCloudiness;
    private RecyclerView recyclerView24hForecast;
    private RecyclerView recyclerView5DaysForecast;
    private Forecast24hAdapter forecast24hAdapter;
    private Forecast5DaysAdapter forecast5DaysAdapter;
    private FirebaseFirestore firestore;
    private OpenWeatherMapService service;
    private String location;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mAuth = FirebaseAuth.getInstance();

        checkUserAuthentication();

        bindViews();
        initRetrofit();
        firestore = FirebaseFirestore.getInstance();

        forecast24hAdapter = new Forecast24hAdapter(new ArrayList<>());
        forecast5DaysAdapter = new Forecast5DaysAdapter(new HashMap<>());

        recyclerView24hForecast.setLayoutManager(new LinearLayoutManager(this));
        recyclerView24hForecast.setAdapter(forecast24hAdapter);

        recyclerView5DaysForecast.setLayoutManager(new LinearLayoutManager(this));
        recyclerView5DaysForecast.setAdapter(forecast5DaysAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
        } else {
            getCurrentLocationAndFetchWeather();
        }


        buttonFetch.setOnClickListener(v -> {
            if (editTextLocation.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            } else {
                fetchWeatherData(editTextLocation.getText().toString());
            }
        });
    }

    private void checkUserAuthentication() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "You must be signed in to use this functionality.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AuthUiActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_history) {
            if (location != null && !location.trim().isEmpty()) {
                Intent intent = new Intent(WeatherActivity.this, WeatherHistoryActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please fetch weather data for a location first.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void bindViews() {
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonFetch = findViewById(R.id.buttonFetch);
        layoutCurrentWeather = findViewById(R.id.layoutCurrentWeather);
        layoutForecast = findViewById(R.id.layoutForecast);
        textViewCityCountry = findViewById(R.id.titleLocation);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewFeelsLike = findViewById(R.id.textViewFeelsLike);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewWeatherDescription = findViewById(R.id.textViewWeatherDescription);
        textViewWindSpeed = findViewById(R.id.textViewWindSpeed);
        textViewCloudiness = findViewById(R.id.textViewCloudiness);
        recyclerView24hForecast = findViewById(R.id.recyclerView24hForecast);
        recyclerView5DaysForecast = findViewById(R.id.recyclerView5DaysForecast);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OpenWeatherMapService.class);
    }

    private void getCurrentLocationAndFetchWeather() {
        LocationManager locationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            assert locationManager != null;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                String loc = location.getLatitude() + "," + location.getLongitude();
                fetchWeatherData(loc);
            } else {
                LocationManager finalLocationManager = locationManager;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        finalLocationManager.removeUpdates(this);
                        String loc = location.getLatitude() + "," + location.getLongitude();
                        fetchWeatherData(loc);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {}

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {}
                });
            }
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndFetchWeather();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void fetchWeatherData(String location) {
        Call<WeatherResponse> callWeatherResponse;
        Call<FiveDayForecastResponse> callFiveDayForecastResponse;
        if (location.contains(",")) {
            String[] parts = location.split(",");
            double latitude = Double.parseDouble(parts[0]);
            double longitude = Double.parseDouble(parts[1]);

            callWeatherResponse = service.getCurrentWeatherByCoords(latitude, longitude, API_KEY);
            callFiveDayForecastResponse = service.get5Day3HourForecastByCoords(latitude, longitude, API_KEY);
        } else {
            callWeatherResponse = service.getCurrentWeather(location, API_KEY);
            callFiveDayForecastResponse = service.get5Day3HourForecast(location, API_KEY);
        }

        fetchCurrentWeather(callWeatherResponse);
        fetch5Day3HourForecast(callFiveDayForecastResponse);
    }


    private void fetchCurrentWeather(Call<WeatherResponse> call) {
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayCurrentWeather(response.body());
                } else {
                    Toast.makeText(WeatherActivity.this, "Failed to fetch weather info.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Toast.makeText(WeatherActivity.this, "Failed to fetch weather info. Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCurrentWeather(WeatherResponse weather) {
        location = weather.name;

        textViewCityCountry.setText(location + ", " + weather.sys.country);
        textViewTemperature.setText("Temperature: " + kelvinToCelsius(weather.main.temp) + "ºC");
        textViewFeelsLike.setText("Feels Like: " + kelvinToCelsius(weather.main.feels_like) + "ºC");
        textViewHumidity.setText("Humidity: " + weather.main.humidity + "%");
        textViewWeatherDescription.setText("Weather: " + weather.weather.get(0).description);
        textViewWindSpeed.setText("Wind Speed: " + msToKmh(weather.wind.speed) + " km/h");
        textViewCloudiness.setText("Cloudiness: " + weather.clouds.all + "%");

        layoutCurrentWeather.setVisibility(View.VISIBLE);

        storeWeatherInFirestore(weather);
    }

    private void storeWeatherInFirestore(WeatherResponse weather) {
        String documentId = weather.name + " [" + weather.dt + "]";

        firestore.collection("currentWeather")
                .document(documentId)
                .set(weather)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Weather successfully written!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error writing document", e));
    }

    private void fetch5Day3HourForecast(Call<FiveDayForecastResponse> call) {
        call.enqueue(new Callback<FiveDayForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<FiveDayForecastResponse> call, @NonNull Response<FiveDayForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processAndDisplayForecastData(response.body());
                } else {
                    Toast.makeText(WeatherActivity.this, "Failed to fetch 5 day / 3 hour forecast.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FiveDayForecastResponse> call, @NonNull Throwable t) {
                Toast.makeText(WeatherActivity.this, "Failed to fetch 5 day / 3 hour forecast. Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processAndDisplayForecastData(FiveDayForecastResponse forecastResponse) {
        Map<String, List<FiveDayForecastResponse.ForecastData>> dateToForecastDataMap = groupForecastByDate(forecastResponse);

        Map<String, List<FiveDayForecastResponse.ForecastData>> sortedMap = new TreeMap<>(dateToForecastDataMap);

        List<FiveDayForecastResponse.ForecastData> dataFor24h = get24hForecastData(sortedMap);
        forecast24hAdapter.updateData(dataFor24h);
        forecast5DaysAdapter.updateData(sortedMap);
        layoutForecast.setVisibility(View.VISIBLE);
    }
    private Map<String, List<FiveDayForecastResponse.ForecastData>> groupForecastByDate(FiveDayForecastResponse response) {
        Map<String, List<FiveDayForecastResponse.ForecastData>> dateToForecastDataMap = new HashMap<>();

        for (FiveDayForecastResponse.ForecastData forecastData : response.list) {
            String date = extractDate(forecastData.dt_txt);

            if (!dateToForecastDataMap.containsKey(date)) {
                dateToForecastDataMap.put(date, new ArrayList<>());
            }

            Objects.requireNonNull(dateToForecastDataMap.get(date)).add(forecastData);
        }

        return dateToForecastDataMap;
    }

    private String extractDate(String dt_txt) {
        return dt_txt.split(" ")[0];
    }

    private List<FiveDayForecastResponse.ForecastData> get24hForecastData(Map<String, List<FiveDayForecastResponse.ForecastData>> dateToForecastDataMap) {
        List<FiveDayForecastResponse.ForecastData> result = new ArrayList<>();
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        boolean startedCollecting = false;
        int counter = 0;

        for (List<FiveDayForecastResponse.ForecastData> dailyForecasts : dateToForecastDataMap.values()) {
            for (FiveDayForecastResponse.ForecastData data : dailyForecasts) {
                String time = data.dt_txt.split(" ")[1];
                int forecastHour = Integer.parseInt(time.split(":")[0]);
                if (forecastHour > currentHour && !startedCollecting) {
                    startedCollecting = true;
                }
                if (startedCollecting) {
                    result.add(data);
                    counter++;
                }
                if (counter >= 8) {
                    break;
                }
            }
            if (counter == 8) {
                break;
            }
        }

        return result;
    }



    private int kelvinToCelsius(double kelvin) {
        return (int) Math.round(kelvin - 273.15);
    }

    private int msToKmh(double ms) {
        return (int) Math.round(ms * 3.6);
    }
}

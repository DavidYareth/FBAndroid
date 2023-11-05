package com.firebase.uidemo.weather.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.firebase.uidemo.R;
import com.firebase.uidemo.weather.responses.WeatherResponse;

import java.util.List;

@SuppressLint("SetTextI18n")
public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.ViewHolder> {

    private final List<WeatherResponse> weatherHistoryList;

    public WeatherHistoryAdapter(List<WeatherResponse> weatherHistoryList) {
        this.weatherHistoryList = weatherHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherResponse weather = weatherHistoryList.get(position);
        holder.tvTimestamp.setText("Date: " + unixToTimestamp(weather.dt));
        holder.tvTemperature.setText("Temperature: " + kelvinToCelsius(weather.main.temp) + "ºC");
        holder.tvFeelsLike.setText("Feels Like: " + kelvinToCelsius(weather.main.feels_like) + "ºC");
        holder.tvHumidity.setText("Humidity: " + weather.main.humidity + "%");
        holder.tvWeatherDescription.setText("Weather: " + weather.weather.get(0).description);
        holder.tvWindSpeed.setText("Wind Speed: " + msToKmh(weather.wind.speed) + " km/h");
        holder.tvCloudiness.setText("Cloudiness: " + weather.clouds.all + "%");
    }

    @Override
    public int getItemCount() {
        return weatherHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity, tvTimestamp,tvTemperature, tvFeelsLike, tvHumidity, tvWeatherDescription, tvWindSpeed, tvCloudiness, tvCountry;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvFeelsLike = itemView.findViewById(R.id.tvFeelsLike);
            tvHumidity = itemView.findViewById(R.id.tvHumidity);
            tvWeatherDescription = itemView.findViewById(R.id.tvWeatherDescription);
            tvWindSpeed = itemView.findViewById(R.id.tvWindSpeed);
            tvCloudiness = itemView.findViewById(R.id.tvCloudiness);
        }
    }

    private int kelvinToCelsius(double kelvin) {
        return (int) Math.round(kelvin - 273.15);
    }

    private int msToKmh(double ms) {
        return (int) Math.round(ms * 3.6);
    }

    // get the timestamp from the unix time in a readable format
    private String unixToTimestamp(long unix) {
        return java.text.DateFormat.getDateTimeInstance().format(unix * 1000);
    }
}
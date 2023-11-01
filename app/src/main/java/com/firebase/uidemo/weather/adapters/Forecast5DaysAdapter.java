package com.firebase.uidemo.weather.adapters;

import android.annotation.SuppressLint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.uidemo.R;
import com.firebase.uidemo.weather.responses.FiveDayForecastResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Forecast5DaysAdapter extends RecyclerView.Adapter<Forecast5DaysAdapter.ViewHolder> {
    private Map<String, List<FiveDayForecastResponse.ForecastData>> dateToForecastDataMap;

    public Forecast5DaysAdapter(Map<String, List<FiveDayForecastResponse.ForecastData>> dateToForecastDataMap) {
        this.dateToForecastDataMap = dateToForecastDataMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_5_day_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String date = new ArrayList<>(dateToForecastDataMap.keySet()).get(position);
        List<FiveDayForecastResponse.ForecastData> dailyForecasts = dateToForecastDataMap.get(date);

        // Set date
        String[] dateParts = date.split("-");
        String formattedDate = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
        String finalDate = "Date: " + formattedDate;
        // Calculate max/min temps and display it
        assert dailyForecasts != null;
        Pair<Double, Double> temps = getMaxAndMinTemps(dailyForecasts);
        String tempMax = "Max: " + temps.first + "°C";
        String tempMin = "Min: " + temps.second + "°C";

        holder.textDate.setText(finalDate);
        holder.textTempMax.setText(tempMax);
        holder.textTempMin.setText(tempMin);
    }

    @Override
    public int getItemCount() {
        return dateToForecastDataMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textTempMin, textTempMax;
        public ViewHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textTempMin = itemView.findViewById(R.id.textTempMin);
            textTempMax = itemView.findViewById(R.id.textTempMax);
        }
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

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(Map<String, List<FiveDayForecastResponse.ForecastData>> newMap) {
        this.dateToForecastDataMap = newMap;
        notifyDataSetChanged();
    }
}
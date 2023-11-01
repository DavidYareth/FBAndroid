package com.firebase.uidemo.weather.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.uidemo.R;
import com.firebase.uidemo.weather.responses.FiveDayForecastResponse;

import java.util.List;

public class Forecast24hAdapter extends RecyclerView.Adapter<Forecast24hAdapter.ViewHolder> {
    private List<FiveDayForecastResponse.ForecastData> forecastData;

    public Forecast24hAdapter(List<FiveDayForecastResponse.ForecastData> forecastData) {
        this.forecastData = forecastData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_24h_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FiveDayForecastResponse.ForecastData data = forecastData.get(position);
        holder.textTime.setText(data.dt_txt);
        String temperature = kelvinToCelsius(data.main.temp) + "°C";
        holder.textTemp.setText(temperature);
        holder.textDescription.setText(data.weather.get(0).description);
    }

    @Override
    public int getItemCount() {
        return forecastData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTime, textTemp, textDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.textTime);
            textTemp = itemView.findViewById(R.id.textTemp);
            textDescription = itemView.findViewById(R.id.textDescription);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<FiveDayForecastResponse.ForecastData> newData) {
        this.forecastData = newData;
        notifyDataSetChanged();
    }

    private int kelvinToCelsius(double kelvin) {
        return (int) Math.round(kelvin - 273.15);
    }
}

package com.firebase.uidemo.weather.services;

import com.firebase.uidemo.weather.responses.FiveDayForecastResponse;
import com.firebase.uidemo.weather.responses.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeather(@Query("q") String location, @Query("appid") String apiKey);

    @GET("data/2.5/forecast")
    Call<FiveDayForecastResponse> get5Day3HourForecast(@Query("q") String location, @Query("appid") String apiKey);

}

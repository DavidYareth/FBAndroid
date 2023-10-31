package com.firebase.uidemo.weather.responses;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FiveDayForecastResponse {
    public String cod;
    public String message;
    public int cnt;
    public List<ForecastData> list;
    public City city;

    public static class ForecastData {
        public long dt;
        public Main main;
        public List<Weather> weather;
        public Clouds clouds;
        public Wind wind;
        public Double visibility;
        public Double pop;
        public Rain rain;
        public Snow snow;
        public Sys sys;
        public String dt_txt;

        @NonNull
        @Override
        public String toString() {
            return "ForecastData{" +
                    "dt=" + dt +
                    ", main=" + main +
                    ", weather=" + weather +
                    ", clouds=" + clouds +
                    ", wind=" + wind +
                    ", visibility=" + visibility +
                    ", pop=" + pop +
                    ", rain=" + rain +
                    ", snow=" + snow +
                    ", sys=" + sys +
                    ", dt_txt='" + dt_txt + '\'' +
                    '}';
        }
    }

    public static class Main {
        public double temp;
        public double feels_like;
        public double temp_min;
        public double temp_max;
        public double pressure;
        public double sea_level;
        public double grnd_level;
        public int humidity;
        public double temp_kf;

        @NonNull
        @Override
        public String toString() {
            return "Main{" +
                    "temp=" + temp +
                    ", feels_like=" + feels_like +
                    ", temp_min=" + temp_min +
                    ", temp_max=" + temp_max +
                    ", pressure=" + pressure +
                    ", sea_level=" + sea_level +
                    ", grnd_level=" + grnd_level +
                    ", humidity=" + humidity +
                    ", temp_kf=" + temp_kf +
                    '}';
        }
    }

    public static class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;

        @NonNull
        @Override
        public String toString() {
            return "Weather{" +
                    "id=" + id +
                    ", main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }

    public static class Clouds {
        public int all;

        @NonNull
        @Override
        public String toString() {
            return "Clouds{" +
                    "all=" + all +
                    '}';
        }
    }

    public static class Wind {
        public double speed;
        public int deg;
        @SerializedName("gust")
        public Double gust;

        @NonNull
        @Override
        public String toString() {
            return "Wind{" +
                    "speed=" + speed +
                    ", deg=" + deg +
                    ", gust=" + gust +
                    '}';
        }
    }

    public static class Rain {
        @SerializedName("3h")
        public Double h3;

        @NonNull
        @Override
        public String toString() {
            return "Rain{" +
                    "h3=" + h3 +
                    '}';
        }
    }

    public static class Snow {
        @SerializedName("3h")
        public Double h3;

        @NonNull
        @Override
        public String toString() {
            return "Snow{" +
                    "h3=" + h3 +
                    '}';
        }
    }

    public static class Sys {
        public String pod;

        @NonNull
        @Override
        public String toString() {
            return "Sys{" +
                    "pod='" + pod + '\'' +
                    '}';
        }
    }

    public static class City {
        public int id;
        public String name;
        public Coordinate coord;
        public String country;
        public int population;
        public int timezone;
        public long sunrise;
        public long sunset;

        @NonNull
        @Override
        public String toString() {
            return "City{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", coord=" + coord +
                    ", country='" + country + '\'' +
                    ", population=" + population +
                    ", timezone=" + timezone +
                    ", sunrise=" + sunrise +
                    ", sunset=" + sunset +
                    '}';
        }
    }

    public static class Coordinate {
        public double lat;
        public double lon;

        @NonNull
        @Override
        public String toString() {
            return "Coordinate{" +
                    "lat=" + lat +
                    ", lon=" + lon +
                    '}';
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "FiveDayForecastResponse{" +
                "cod='" + cod + '\'' +
                ", message='" + message + '\'' +
                ", cnt=" + cnt +
                ", list=" + list +
                ", city=" + city +
                '}';
    }
}

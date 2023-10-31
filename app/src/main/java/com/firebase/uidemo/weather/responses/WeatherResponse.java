package com.firebase.uidemo.weather.responses;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    public Coord coord;
    public List<Weather> weather;
    public String base;
    public Main main;
    public long visibility;
    public Wind wind;
    public Clouds clouds;
    public Rain rain;
    public Snow snow;
    public long dt;
    public Sys sys;
    public int timezone;
    public long id;
    public String name;
    public int cod;

    public static class Coord {
        public double lon;
        public double lat;

        @NonNull
        @Override
        public String toString() {
            return "Coord{" +
                    "lon=" + lon +
                    ", lat=" + lat +
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

    public static class Main {
        public double temp;
        public double feels_like;
        public double pressure;
        public double humidity;
        public double temp_min;
        public double temp_max;
        public Double sea_level;
        public Double grnd_level;

        @NonNull
        @Override
        public String toString() {
            return "Main{" +
                    "temp=" + temp +
                    ", feels_like=" + feels_like +
                    ", pressure=" + pressure +
                    ", humidity=" + humidity +
                    ", temp_min=" + temp_min +
                    ", temp_max=" + temp_max +
                    ", sea_level=" + sea_level +
                    ", grnd_level=" + grnd_level +
                    '}';
        }
    }

    public static class Wind {
        public double speed;
        public int deg;
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

    public static class Rain {
        @SerializedName("1h")
        public Double h1;

        @SerializedName("3h")
        public Double h3;

        @NonNull
        @Override
        public String toString() {
            return "Rain{" +
                    "h1=" + h1 +
                    ", h3=" + h3 +
                    '}';
        }
    }

    public static class Snow {
        @SerializedName("1h")
        public Double h1;

        @SerializedName("3h")
        public Double h3;

        @NonNull
        @Override
        public String toString() {
            return "Snow{" +
                    "h1=" + h1 +
                    ", h3=" + h3 +
                    '}';
        }
    }

    public static class Sys {
        public int type;
        public long id;
        public double message;
        public String country;
        public long sunrise;
        public long sunset;

        @NonNull
        @Override
        public String toString() {
            return "Sys{" +
                    "type=" + type +
                    ", id=" + id +
                    ", message=" + message +
                    ", country='" + country + '\'' +
                    ", sunrise=" + sunrise +
                    ", sunset=" + sunset +
                    '}';
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "WeatherResponse{" +
                "coord=" + coord +
                ", weather=" + weather +
                ", base='" + base + '\'' +
                ", main=" + main +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", rain=" + rain +
                ", snow=" + snow +
                ", dt=" + dt +
                ", sys=" + sys +
                ", timezone=" + timezone +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                '}';
    }
}

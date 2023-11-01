package com.firebase.uidemo.weather.responses;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;
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

    public WeatherResponse() {}

    public static class Coord {
        public double lon;
        public double lat;

        public Coord() {}

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

        public Weather() {}

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
        @PropertyName("temp")
        public double temp;

        @PropertyName("feels_like")
        public double feels_like;

        @PropertyName("pressure")
        public double pressure;

        @PropertyName("humidity")
        public double humidity;

        @PropertyName("temp_min")
        public double temp_min;

        @PropertyName("temp_max")
        public double temp_max;

        @PropertyName("sea_level")
        public Double sea_level;

        @PropertyName("grnd_level")
        public Double grnd_level;

        public Main() {}

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
        @PropertyName("speed")
        public double speed;

        @PropertyName("deg")
        public int deg;

        @PropertyName("gust")
        public Double gust;

        public Wind() {}

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
        @PropertyName("all")
        public int all;

        public Clouds() {}

        @NonNull
        @Override
        public String toString() {
            return "Clouds{" +
                    "all=" + all +
                    '}';
        }
    }

    public static class Rain {
        @PropertyName("1h")
        @SerializedName("1h")
        public Double h1;

        @PropertyName("3h")
        @SerializedName("3h")
        public Double h3;

        public Rain() {}

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
        @PropertyName("1h")
        @SerializedName("1h")
        public Double h1;

        @PropertyName("3h")
        @SerializedName("3h")
        public Double h3;

        public Snow() {}

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
        @PropertyName("type")
        public int type;

        @PropertyName("id")
        public long id;

        @PropertyName("message")
        public double message;

        @PropertyName("country")
        public String country;

        @PropertyName("sunrise")
        public long sunrise;

        @PropertyName("sunset")
        public long sunset;

        public Sys() {}

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

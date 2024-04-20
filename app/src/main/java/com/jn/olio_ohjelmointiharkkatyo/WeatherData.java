package com.jn.olio_ohjelmointiharkkatyo;

import android.graphics.Bitmap;

public class WeatherData {
    private String main;
    private String desc;
    private float temp;
    private float wind_speed;
    private Bitmap weather_image;

    public WeatherData(String main, String desc, float temp, float wind_speed, Bitmap weather_image) {
        this.main = main;
        this.desc = desc;
        this.temp = temp;
        this.wind_speed = wind_speed;
        this.weather_image = weather_image;
    }
    public String getMain() {
        return main;
    }

    public String getDesc() {
        return desc;
    }

    public float getTemp() {
        return temp;
    }

    public float getWindSpeed() {
        return wind_speed;
    }

    public Bitmap getWeatherImage() {
        return weather_image;
    }
}

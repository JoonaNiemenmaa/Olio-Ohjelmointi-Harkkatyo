package com.jn.olio_ohjelmointiharkkatyo;

import java.util.HashMap;

public class MunicipalityData {
    private String municipality_name;
    private PopulationData population_data;
    private HashMap<String, Float> political_data;
    private WeatherData weather_data;
    public MunicipalityData (String municipality_name, PopulationData population_data, HashMap<String, Float> political_data, WeatherData weather_data) {
        this.municipality_name = municipality_name;
        this.population_data = population_data;
        this.political_data = political_data;
        this.weather_data = weather_data;
    }
    public String getMunicipalityName() { return municipality_name; }
    public PopulationData getPopulationData() {
        return population_data;
    }
    public HashMap<String, Float> getPoliticalData() { return political_data; }
    public WeatherData getWeatherData() { return weather_data; }
}

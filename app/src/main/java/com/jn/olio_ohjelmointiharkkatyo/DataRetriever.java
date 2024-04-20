package com.jn.olio_ohjelmointiharkkatyo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.graphics.Bitmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class DataRetriever {
    private static DataRetriever data_retriever;
    final private ObjectMapper object_mapper = new ObjectMapper();
    HashMap<String, String> municipalities;
    final private String population_query_url = "https://pxdata.stat.fi/PxWeb/api/v1/fi/StatFin/kuol/statfin_kuol_pxt_12au.px";
    // Constructor is private due to DataRetriever being a singleton class
    // Decided to make DataRetriever a singleton as it helps to have a static
    // reference to an instance of DataRetriever that can be used anywhere in the code
    private DataRetriever() {
        // Querying for municipality codes and placing them inside a hashmap is done here
        // in the constructor, so that it's only executed once when the user first searches
        // for information about a municipality
        JsonNode areas = null;
        try {
            areas = object_mapper.readTree(new URL(population_query_url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        municipalities = new HashMap<String, String>();
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();
        for (JsonNode node : areas.get("variables").get(1).get("values")) {
            values.add(node.asText());
        }
        for (JsonNode node : areas.get("variables").get(1).get("valueTexts")) {
            keys.add(node.asText());
        }
        for (int i = 0; i < keys.size(); i++) {
            String municipality_code = values.get(i);
            String municipality_name = keys.get(i);
            municipalities.put(municipality_name, municipality_code);
        }
    }
    public static DataRetriever getInstance() {
        if (data_retriever == null) {
            data_retriever = new DataRetriever();
        }
        return data_retriever;
    }
    private JsonNode fetchDataStatisticsFinland(Context context, String municipality_code, String query_url, int json_file_id) {
        try {
            URL url = new URL(query_url);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            JsonNode json_input_string = object_mapper.readTree(context.getResources().openRawResource(json_file_id));

            ((ObjectNode) json_input_string.get("query").get(1).get("selection")).putArray("values").add(municipality_code);

            byte[] input = object_mapper.writeValueAsBytes(json_input_string);

            try (OutputStream os = con.getOutputStream()) {
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                JsonNode data_node = object_mapper.readTree(response.toString());

                return data_node;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private JsonNode fetchDataOpenWeather(String municipality_name) {
        final String weather_query_url = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";
        final String geocoding_query_url = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%s&appid=%s";
        final String limit = "5";
        final String api_key = "e82e9594da1830f5baee50f0602d0286";
        try {
            JsonNode areas = object_mapper.readTree(new URL(String.format(geocoding_query_url, municipality_name, limit, api_key)));

            String lat = areas.get(0).get("lat").toString();
            String lon = areas.get(0).get("lon").toString();

            JsonNode weather_node = object_mapper.readTree(new URL(String.format(weather_query_url, lat, lon, api_key)));

            System.out.println(weather_node.toPrettyString());

            return weather_node;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private PopulationData getPopulationData(Context context, String municipality_name) {
        String municipality_code = municipalities.get(municipality_name);
        JsonNode population_data_node = fetchDataStatisticsFinland(context, municipality_code, population_query_url, R.raw.population_query);
        PopulationData population_data;
        int population = 0;
        int population_difference = 0;
        if (population_data_node != null) {
            population = population_data_node.get("value").get(1).asInt();
            population_difference = population_data_node.get("value").get(0).asInt();
        }
        final String employment_query_url = "https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
        JsonNode employment_data_node = fetchDataStatisticsFinland(context, municipality_code, employment_query_url, R.raw.employment_query);
        float employment = 0;
        if (employment_data_node != null) {
            employment = (float)employment_data_node.get("value").get(0).asDouble();
        }
        final String workplace_self_sufficiency_query_url = "https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_125s.px";
        JsonNode workplace_self_sufficiency_data_node = fetchDataStatisticsFinland(context, municipality_code, workplace_self_sufficiency_query_url, R.raw.workplace_self_sufficiency_query);
        float workplace_self_sufficiency = 0;
        if (workplace_self_sufficiency_data_node != null) {
            workplace_self_sufficiency = (float)workplace_self_sufficiency_data_node.get("value").get(0).asDouble();
        }
        population_data = new PopulationData(population, population_difference, employment, workplace_self_sufficiency);
        return population_data;
    }
    private HashMap<String, Float> getPoliticalData(Context context, String municipality_name) {
        final int amount_of_parties = 9;
        String municipality_code = municipalities.get(municipality_name);
        HashMap<String, Float> political_data = new HashMap<>();
        if (municipality_code != null) {
            // The code we get from the municipalities hashmap has to have the first two characters removed for the query to be valid
            municipality_code = municipality_code.substring(2);
            final String political_data_query_url = "https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/kvaa/statfin_kvaa_pxt_12wy.px";
            JsonNode political_data_node = fetchDataStatisticsFinland(context, municipality_code, political_data_query_url, R.raw.political_query);
            if (political_data_node != null) {
                for (int i = 0; i < amount_of_parties; i++) {
                    // Here's some kinda obtuse code since the .json response file is formatted in a weird way.
                    // Essentially each datapoint for the political parties and their vote(%) has an unique identifier
                    // in the json file (for example "03" for KOK (Kokoomus)) which results having to do this weirdness
                    String code = "0" + (i + 1);
                    String key = political_data_node.get("dimension").get("Puolue").get("category").get("label").get(code).asText();
                    int index = political_data_node.get("dimension").get("Puolue").get("category").get("index").get(code).asInt();
                    float value = (float)political_data_node.get("value").get(index).asDouble();
                    political_data.put(key, value);
                }
            }
        }
        if (political_data.size() == 0) {
            political_data.put("None", 1.0f);
        }
        return political_data;
    }
    private WeatherData getWeatherData(String municipality_name) {
        JsonNode weather_node = fetchDataOpenWeather(municipality_name);
        String main = "None";
        String desc = "None";
        float temp = 0;
        float wind_speed = 0;
        Bitmap weather_image = null;
        if (weather_node != null) {
            main = weather_node.get("weather").get(0).get("main").asText();
            desc = weather_node.get("weather").get(0).get("description").asText();
            // Convert kelvin to celsius and round the result
            // The round function in java cannot specify how many decimal places we want so now we have this
            temp = (float)weather_node.get("main").get("temp").asDouble();
            temp = Math.round((temp - 273.15f) * 100) / 100.0f;
            wind_speed = (float)weather_node.get("wind").get("speed").asDouble();
            String image_code = weather_node.get("weather").get(0).get("icon").asText();
            weather_image = loadWeatherImage(image_code);
        }
        return new WeatherData(main, desc, temp, wind_speed, weather_image);
    }
    private Bitmap loadWeatherImage(String image_code) {
        // I took the code for this from here https://stackoverflow.com/questions/5776851/load-image-from-url
        try {
            String image_url_string = "https://openweathermap.org/img/wn/%s@2x.png";
            URL url = new URL(String.format(image_url_string, image_code));
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public MunicipalityData getMunicipalityData(Context context, String municipality_name) {
        if (municipalities.get(municipality_name) == null) {
            return null;
        }
        PopulationData population_data = data_retriever.getPopulationData(context, municipality_name);
        HashMap<String, Float> political_data = data_retriever.getPoliticalData(context, municipality_name);
        WeatherData weather_data = data_retriever.getWeatherData(municipality_name);
        return new MunicipalityData(municipality_name, population_data, political_data, weather_data);
    }
}

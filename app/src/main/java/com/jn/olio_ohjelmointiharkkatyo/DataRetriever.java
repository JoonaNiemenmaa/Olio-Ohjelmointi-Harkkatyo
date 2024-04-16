package com.jn.olio_ohjelmointiharkkatyo;

import android.content.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class DataRetriever {
    private static DataRetriever data_retriever;
    final private ObjectMapper object_mapper = new ObjectMapper();
    HashMap<String, String> municipalities;
    final private String population_query_url = "https://pxdata.stat.fi/PxWeb/api/v1/fi/StatFin/kuol/statfin_kuol_pxt_12au.px";
    final private String employment_query_url = "https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
    final private String workplace_self_sufficiency_query_url = "https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_125s.px";

    final private String political_data_query_url = "https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/kvaa/statfin_kvaa_pxt_12wy.px";
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
    private JsonNode fetchData(Context context, String code, String query_url, int json_file_id) {
        try {
            URL url = new URL(query_url);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            JsonNode json_input_string = object_mapper.readTree(context.getResources().openRawResource(json_file_id));

            ((ObjectNode) json_input_string.get("query").get(1).get("selection")).putArray("values").add(code);

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
    public PopulationData getPopulationData(Context context, String municipality_name) {
        String municipality_code = municipalities.get(municipality_name);
        JsonNode population_data_node = fetchData(context, municipality_code, population_query_url, R.raw.population_query);
        PopulationData population_data;
        int population = 0;
        int population_difference = 0;
        if (population_data_node != null) {
            population = population_data_node.get("value").get(1).asInt();
            population_difference = population_data_node.get("value").get(0).asInt();
        }
        JsonNode employment_data_node = fetchData(context, municipality_code, employment_query_url, R.raw.employment_query);
        float employment = 0;
        if (employment_data_node != null) {
            employment = (float)employment_data_node.get("value").get(0).asDouble();
        }
        JsonNode workplace_self_sufficiency_data_node = fetchData(context, municipality_code, workplace_self_sufficiency_query_url, R.raw.workplace_self_sufficiency_query);
        float workplace_self_sufficiency = 0;
        if (workplace_self_sufficiency_data_node != null) {
            workplace_self_sufficiency = (float)workplace_self_sufficiency_data_node.get("value").get(0).asDouble();
        }
        population_data = new PopulationData(population, population_difference, employment, workplace_self_sufficiency);
        return population_data;
    }
    public HashMap<String, Float> getPoliticalData(Context context, String municipality_name) {
        final int amount_of_parties = 9;
        String municipality_code = municipalities.get(municipality_name);
        // The code we get from the municipalities hashmap has to have the first two characters removed for the query to be valid
        municipality_code = municipality_code.substring(2);
        JsonNode political_data_node = fetchData(context, municipality_code, political_data_query_url, R.raw.political_query);
        HashMap<String, Float> political_data = new HashMap<>();
        if (political_data_node != null) {
            for (int i = 0; i < amount_of_parties; i++) {
                // Here's some kinda obtuse code since the .json response file is formatted in a weird way.
                // Essentially each datapoint for the political parties and their vote(%) has an unique identifier
                // (for example "03" for KOK (Kokoomus)) which results in this weirdness
                String code = "0" + (i + 1);
                String key = political_data_node.get("dimension").get("Puolue").get("category").get("label").get(code).asText();
                int index = political_data_node.get("dimension").get("Puolue").get("category").get("index").get(code).asInt();
                float value = (float)political_data_node.get("value").get(index).asDouble();
                political_data.put(key, value);
            }
        }
        System.out.println(political_data);
        if (political_data.size() == 0) {
            political_data.put("None", 1.0f);
        }
        return political_data;
    }
}

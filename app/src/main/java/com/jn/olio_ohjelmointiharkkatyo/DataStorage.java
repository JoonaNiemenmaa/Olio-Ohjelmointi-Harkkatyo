package com.jn.olio_ohjelmointiharkkatyo;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataStorage {
    private static DataStorage data_storage;
    private MunicipalityData municipality;
    final static private int amount_of_searches_to_remember = 5;
    final static private String filename = "searches.data";
    private ArrayList<String> searches = new ArrayList<>();
    // This class is singleton for the static reference that can be accessed anywhere
    // Also we really only ever want one DataStorage anyways
    private DataStorage() {}
    public static DataStorage getInstance() {
        if (data_storage == null) {
            data_storage = new DataStorage();
        }
        return data_storage;
    }

    public MunicipalityData getMunicipality() {
        return municipality;
    }

    public void setMunicipality(MunicipalityData municipality) {
        this.municipality = municipality;
    }
    public void addSearch(String municipality_name) {
        if (!searches.contains(municipality_name)) {
            searches.add(municipality_name);
            if (searches.size() > amount_of_searches_to_remember) {
                searches.remove(0);
            }
        }
        System.out.println(searches);
    }
    public ArrayList<String> getSearches() { return searches; }
    public void saveSearches(Context context) {
        try {
            ObjectOutputStream search_writer = new ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
            search_writer.writeObject(searches);
            search_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadSearches(Context context) {
        try {
            ObjectInputStream search_reader = new ObjectInputStream(context.openFileInput(filename));
            searches = (ArrayList<String>) search_reader.readObject();
            search_reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

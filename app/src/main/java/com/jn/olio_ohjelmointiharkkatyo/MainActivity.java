package com.jn.olio_ohjelmointiharkkatyo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    EditText edit_search;
    RecyclerView rv_searches;
    DataStorage data_storage = DataStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_search = findViewById(R.id.editSearchMunicipality);
        data_storage.loadSearches(this);

        rv_searches = findViewById(R.id.rvPreviousSearches);

        rv_searches.setLayoutManager(new LinearLayoutManager(this));
        rv_searches.setAdapter(new SearchListAdapter(getApplicationContext(), data_storage.getSearches(), edit_search));
    }
    public void searchForMunicipality(View view) {
        Context context = this;
        ExecutorService service = Executors.newSingleThreadExecutor();
        String municipality_name = edit_search.getText().toString();
        data_storage.addSearch(municipality_name);
        data_storage.saveSearches(context);
        // This line of code is here so that the recyclerview updates whenever a new search is made
        rv_searches.setAdapter(new SearchListAdapter(getApplicationContext(), DataStorage.getInstance().getSearches(), edit_search));
        Toast fail_toast = Toast.makeText(context, "Could not find municipality '" + municipality_name + "'", Toast.LENGTH_LONG);
        service.execute(new Runnable() {
            @Override
            public void run() {
                DataRetriever data_retriever = DataRetriever.getInstance();
                MunicipalityData municipality_data = data_retriever.getMunicipalityData(context, municipality_name);
                if (municipality_data != null) {
                    data_storage.setMunicipality(municipality_data);
                    Intent intent = new Intent(context, ViewMunicipalityActivity.class);
                    startActivity(intent);
                } else {
                    fail_toast.show();
                }
            }
        });
    }
}
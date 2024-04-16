package com.jn.olio_ohjelmointiharkkatyo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    TextView text_code;
    EditText edit_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_code = findViewById(R.id.textCode);
        edit_search = findViewById(R.id.editSearchMunicipality);
    }
    public void searchForMunicipality(View view) {
        Context context = this;
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                String municipality_name = edit_search.getText().toString();
                DataRetriever data_retriever = DataRetriever.getInstance();
                PopulationData population_data = data_retriever.getPopulationData(context, municipality_name);
                HashMap<String, Float> political_data = data_retriever.getPoliticalData(context, municipality_name);
                MunicipalityData municipality_data = new MunicipalityData(municipality_name, population_data, political_data);
                DataStorage data_storage = DataStorage.getInstance();
                data_storage.setMunicipality(municipality_data);
                Intent intent = new Intent(context, ViewMunicipalityActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.jn.olio_ohjelmointiharkkatyo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayMunicipalityDataFragment extends Fragment {

    public DisplayMunicipalityDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    TextView text_municipality, text_population, text_population_difference, text_employment, text_workplace_self_sufficiency;
    AnyChartView pie_political;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_municipality_data, container, false);
        text_municipality = view.findViewById(R.id.textMunicipalityName);
        text_population = view.findViewById(R.id.textPopulation);
        text_population_difference = view.findViewById(R.id.textPopulationDiff);
        text_employment = view.findViewById(R.id.textEmployment);
        text_workplace_self_sufficiency = view.findViewById(R.id.textWorkplaceSelfSufficiency);

        MunicipalityData municipality = DataStorage.getInstance().getMunicipality();

        text_municipality.setText(municipality.getMunicipalityName());
        if (municipality.getPopulationData() != null) {
            PopulationData population_data = municipality.getPopulationData();

            text_population.append(Integer.toString(population_data.getPopulation()));
            text_population_difference.append(Integer.toString(population_data.getPopulationDifference()));
            text_employment.append(population_data.getEmployment() + "%");
            text_workplace_self_sufficiency.append(population_data.getWorkplaceSelfSufficiency() + "%");

        }
        pie_political = view.findViewById(R.id.piePolitical);
        setupPieChart(municipality);
        return view;
    }
    public void setupPieChart(MunicipalityData municipality) {
        Pie pie = AnyChart.pie();
        HashMap<String, Float> political_data = DataStorage.getInstance().getMunicipality().getPoliticalData();
        ArrayList<DataEntry> data_entries = new ArrayList<>();
        for (String key : political_data.keySet()) {
            data_entries.add(new ValueDataEntry(key, political_data.get(key)));
        }
        pie.title(municipality.getMunicipalityName() + " political party division");
        pie.data(data_entries);
        pie_political.setChart(pie);
    }
}
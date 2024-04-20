package com.jn.olio_ohjelmointiharkkatyo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.HashMap;

public class DataFragment extends Fragment {

    public DataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    TextView text_municipality, text_population, text_population_difference, text_employment, text_workplace_self_sufficiency;
    TextView text_main, text_desc, text_temperature, text_wind_speed;
    ImageView image_weather;
    AnyChartView pie_political;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        text_municipality = view.findViewById(R.id.textMunicipalityName);
        text_population = view.findViewById(R.id.textPopulation);
        text_population_difference = view.findViewById(R.id.textPopulationDiff);
        text_employment = view.findViewById(R.id.textEmployment);
        text_workplace_self_sufficiency = view.findViewById(R.id.textWorkplaceSelfSufficiency);

        text_main = view.findViewById(R.id.textWeatherMain);
        text_desc = view.findViewById(R.id.textWeatherDesc);
        text_temperature = view.findViewById(R.id.textWeatherTemp);
        text_wind_speed = view.findViewById(R.id.textWeatherWindSpeed);
        image_weather = view.findViewById(R.id.imageWeather);

        MunicipalityData municipality = DataStorage.getInstance().getMunicipality();

        text_municipality.setText(municipality.getMunicipalityName());

        PopulationData population_data = municipality.getPopulationData();

        text_population.append(Integer.toString(population_data.getPopulation()));
        text_population_difference.append(Integer.toString(population_data.getPopulationDifference()));
        text_employment.append(population_data.getEmployment() + "%");
        text_workplace_self_sufficiency.append(population_data.getWorkplaceSelfSufficiency() + "%");

        WeatherData weather_data = municipality.getWeatherData();

        text_main.append(weather_data.getMain());
        text_desc.append(weather_data.getDesc());
        text_temperature.append(weather_data.getTemp() + " °C");
        text_wind_speed.append(weather_data.getWindSpeed() + " m/s");

        image_weather.setImageBitmap(weather_data.getWeatherImage());

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
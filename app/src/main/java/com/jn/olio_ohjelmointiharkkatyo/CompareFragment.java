package com.jn.olio_ohjelmointiharkkatyo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompareFragment extends Fragment {
    public CompareFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    EditText edit_search_compare;
    // To open up the naming a little all comparison views are named either ..left or ...right
    // This refers to their position on the screen
    // The views on the left is the municipality the user initially searched for and
    // the views on the right are the comparison municipality's
    TextView text_municipality_left, text_population_left, text_population_change_left, text_employment_left, text_wss_left, text_largest_political_left;
    TextView text_municipality_right, text_population_right, text_population_change_right, text_employment_right, text_wss_right, text_largest_political_right;
    TextView text_main_left, text_desc_left, text_temperature_left, text_wind_speed_left;
    TextView text_main_right, text_desc_right, text_temperature_right, text_wind_speed_right;
    ImageView image_weather_left, image_weather_right;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compare, container, false);
        edit_search_compare = view.findViewById(R.id.editSearchCompare);

        text_municipality_left = view.findViewById(R.id.textCompareMunicipalityNameLeft);
        text_population_left = view.findViewById(R.id.textComparePopulationLeft);
        text_population_change_left = view.findViewById(R.id.textComparePopulationChangeLeft);
        text_employment_left = view.findViewById(R.id.textCompareEmploymentLeft);
        text_wss_left = view.findViewById(R.id.textCompareWSSLeft);
        text_largest_political_left = view.findViewById(R.id.textComparePoliticalLeft);

        text_municipality_right = view.findViewById(R.id.textCompareMunicipalityNameRight);
        text_population_right = view.findViewById(R.id.textComparePopulationRight);
        text_population_change_right = view.findViewById(R.id.textComparePopulationChangeRight);
        text_employment_right = view.findViewById(R.id.textCompareEmploymentRight);
        text_wss_right = view.findViewById(R.id.textCompareWSSRight);
        text_largest_political_right = view.findViewById(R.id.textComparePoliticalRight);

        text_main_left = view.findViewById(R.id.textMainLeft);
        text_desc_left = view.findViewById(R.id.textDescLeft);
        text_temperature_left = view.findViewById(R.id.textTempLeft);
        text_wind_speed_left = view.findViewById(R.id.textWindSpeedLeft);
        image_weather_left = view.findViewById(R.id.imageWeatherLeft);

        text_main_right = view.findViewById(R.id.textMainRight);
        text_desc_right = view.findViewById(R.id.textDescRight);
        text_temperature_right = view.findViewById(R.id.textTempRight);
        text_wind_speed_right = view.findViewById(R.id.textWindSpeedRight);
        image_weather_right = view.findViewById(R.id.imageWeatherRight);

        MunicipalityData municipality = DataStorage.getInstance().getMunicipality();
        text_municipality_left.setText(municipality.getMunicipalityName());
        text_population_left.append(Integer.toString(municipality.getPopulationData().getPopulation()));
        text_population_change_left.append(Integer.toString(municipality.getPopulationData().getPopulationDifference()));
        text_employment_left.append(municipality.getPopulationData().getEmployment() + "%");
        text_wss_left.append(municipality.getPopulationData().getWorkplaceSelfSufficiency() + "%");
        HashMap<String, Float> political_data = municipality.getPoliticalData();
        String largest_party = "None";
        for (String key : political_data.keySet()) {
            if (largest_party.equals("None") || political_data.get(key) > political_data.get(largest_party)) {
                largest_party = key;
            }
        }
        text_main_left.setText(municipality.getWeatherData().getMain());
        text_desc_left.setText(municipality.getWeatherData().getDesc());
        text_temperature_left.setText("Temperature: " + municipality.getWeatherData().getTemp());
        text_wind_speed_left.setText("Wind Speed: " + municipality.getWeatherData().getWindSpeed());
        image_weather_left.setImageBitmap(municipality.getWeatherData().getWeatherImage());
        text_largest_political_left.setText(largest_party);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comparison_municipality_name = edit_search_compare.getText().toString();
                DataStorage data_storage = DataStorage.getInstance();
                ExecutorService service = Executors.newSingleThreadExecutor();
                // I hate this this is stupid but at least it works am i right
                // Here's the problem: I cannot change the text on text_municipality2, ..., jne inside the Runnable()
                // function. Android studio throws a hissy fit if I do so.
                // So I have to do it outside the anonymous function.
                // This creates the problem that now there are two separate threads running code that should be ran sequentially
                // To fix this I need a boolean value that is simply ticked true whenever the municipality data has been retrieved
                // But I cannot use variables in the Runnable function's namespace that are declared outside of it
                // This means that I had to hide it inside the IsDone class which is an internal class of CompareFragment
                IsDone is_done = new IsDone();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        data_storage.setComparisonMunicipality(DataRetriever.getInstance().getMunicipalityData(getContext(), comparison_municipality_name));
                        is_done.setIsDone(true);
                    }
                });
                Toast fail_toast = Toast.makeText(getContext(), "Could not find municipality '" + comparison_municipality_name + "'", Toast.LENGTH_LONG);
                while (true) {
                    if (is_done.getIsDone()) {
                        if (data_storage.getComparisonMunicipality() != null) {
                            text_municipality_right.setText(comparison_municipality_name);
                            text_population_right.setText( "Population: " + Integer.toString(data_storage.getComparisonMunicipality().getPopulationData().getPopulation()));
                            text_population_change_right.setText("Population Change: " + Integer.toString(data_storage.getComparisonMunicipality().getPopulationData().getPopulationDifference()));
                            text_employment_right.setText("Employment: " + data_storage.getComparisonMunicipality().getPopulationData().getEmployment() + "%");
                            text_wss_right.setText("WSS: " + data_storage.getComparisonMunicipality().getPopulationData().getWorkplaceSelfSufficiency() + "%");
                            HashMap<String, Float> political_data = data_storage.getComparisonMunicipality().getPoliticalData();
                            String largest_party = "None";
                            for (String key : political_data.keySet()) {
                                if (largest_party.equals("None") || political_data.get(key) > political_data.get(largest_party)) {
                                    largest_party = key;
                                }
                            }
                            text_largest_political_right.setText(largest_party);
                            text_main_right.setText(data_storage.getComparisonMunicipality().getWeatherData().getMain());
                            text_desc_right.setText(data_storage.getComparisonMunicipality().getWeatherData().getDesc());
                            text_temperature_right.setText("Temperature: " + data_storage.getComparisonMunicipality().getWeatherData().getTemp());
                            text_wind_speed_right.setText("Wind Speed: " + data_storage.getComparisonMunicipality().getWeatherData().getWindSpeed());
                            image_weather_right.setImageBitmap(data_storage.getComparisonMunicipality().getWeatherData().getWeatherImage());
                        } else {
                            fail_toast.show();
                        }
                        break;
                    }
                }
            }
        };
        Button button_compare = view.findViewById(R.id.buttonCompare);
        button_compare.setOnClickListener(listener);
        return view;
    }
    private class IsDone {
        private boolean is_done = false;
        public boolean getIsDone() {
            return is_done;
        }
        public void setIsDone(boolean is_done) {
            this.is_done = is_done;
        }
    }
}
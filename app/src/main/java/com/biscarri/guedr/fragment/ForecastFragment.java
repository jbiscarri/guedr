package com.biscarri.guedr.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biscarri.guedr.R;
import com.biscarri.guedr.activity.SettingsActivity;
import com.biscarri.guedr.model.City;
import com.biscarri.guedr.model.Forecast;

/**
 * Created by joanbiscarri on 08/09/15.
 */
public class ForecastFragment extends Fragment {

    private static final String ARG_CITY = "city";

    private City mCity;

    private ImageView mIcon;
    private TextView mMaxTemp;
    private TextView mMinTemp;
    private TextView mHumidity;
    private TextView mDescription;
    private TextView mCityName;

    private int mCurrentMetrics;

    private View mRoot;

    public static Fragment newInstance(City city) {
        ForecastFragment forecastFragment= new ForecastFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_CITY, city);
        forecastFragment.setArguments(arguments);

        return forecastFragment;
    }

    //Implemento oncreate por que tengo menu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mCity = (City) getArguments().getSerializable(ARG_CITY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_forecast, container, false);
        mRoot = root;
        mMaxTemp = (TextView) root.findViewById(R.id.max_temp);
        mMinTemp = (TextView) root.findViewById(R.id.min_temp);
        mHumidity = (TextView) root.findViewById(R.id.humidity);
        mDescription = (TextView) root.findViewById(R.id.forecast_description);
        mIcon = (ImageView) root.findViewById(R.id.forecast_image);
        mCityName = (TextView) root.findViewById(R.id.city);
        mCurrentMetrics = getCurrentMetricsFromSettings();

        mCityName.setText(mCity.getName());
        setForecast(mCity.getForecast());


        return root;
    }

    public void setForecast(Forecast forecast) {

        float maxTemp = (mCurrentMetrics == SettingsActivity.PREF_CELSIUS)?forecast.getMaxTemp() : toFarenheit(forecast.getMaxTemp());
        float minTemp = (mCurrentMetrics == SettingsActivity.PREF_CELSIUS)?forecast.getMinTemp() : toFarenheit(forecast.getMinTemp());

        String metricString = (mCurrentMetrics == SettingsActivity.PREF_CELSIUS)? "°C" : "°F";

        mMaxTemp.setText(String.format(getString(R.string.max_temp_parameter), maxTemp, metricString) );
        mMinTemp.setText(String.format(getString(R.string.min_temp_parameter), minTemp, metricString) );
        mHumidity.setText(String.format(getString(R.string.humidity_parameter), (int)forecast.getHumidity()));
        mDescription.setText(forecast.getDescription());
       // mIcon.setImageResource(forecast.getIcon());

    }

    public int getCurrentMetricsFromSettings() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String stringMetrics = pref.getString(getString(R.string.metric_selection), String.valueOf(SettingsActivity.PREF_CELSIUS));
        int metrics = Integer.valueOf(stringMetrics);
        return metrics;
    }

    protected static float toFarenheit(float celsius) {
        return (celsius * 1.8f) + 32;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecast, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Para coger los datos de la pantalla de settings
    @Override
    public void onResume() {
        super.onResume();
        final int previousMetrics = mCurrentMetrics;
        int metrics = getCurrentMetricsFromSettings();
        if (metrics != mCurrentMetrics) {
            mCurrentMetrics = metrics;
            setForecast(mCity.getForecast());
            //android.R.id.content -> id de la vista raiz de mi app
            //Snackbar.make(findViewById(android.R.id.content), "Preferencias actualizadas", Snackbar.LENGTH_LONG).show();
            final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

            //en lugar de root podría poner getView()
            Snackbar.make(getView(), R.string.updated_preferences, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pref.edit().putString(getString(R.string.metric_selection), String.valueOf(previousMetrics)).apply();
                            mCurrentMetrics = previousMetrics;
                            setForecast(mCity.getForecast());
                        }
                    }).show();
        }
    }

}

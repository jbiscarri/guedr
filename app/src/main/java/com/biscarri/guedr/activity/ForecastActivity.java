package com.biscarri.guedr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.biscarri.guedr.R;
import com.biscarri.guedr.model.Forecast;

public class ForecastActivity extends AppCompatActivity {

    private ImageView mIcon;
    private TextView mMaxTemp;
    private TextView mMinTemp;
    private TextView mHumidity;
    private TextView mDescription;
    private Forecast mForecast;

    private int mCurrentMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mMaxTemp = (TextView) findViewById(R.id.max_temp);
        mMinTemp = (TextView) findViewById(R.id.min_temp);
        mHumidity = (TextView) findViewById(R.id.humidity);
        mDescription = (TextView) findViewById(R.id.forecast_description);
        mIcon = (ImageView) findViewById(R.id.forecast_image);
        mCurrentMetrics = getCurrentMetricsFromSettings();

        setForecast(new Forecast((float) 30.5, 27, 70, "This is a description", "ico01"));
    }

    protected static float toFarenheit(float celsius) {
        return (celsius * 1.8f) + 32;
    }

    public void setForecast(Forecast forecast) {
        mForecast = forecast;

        float maxTemp = (mCurrentMetrics == SettingsActivity.PREF_CELSIUS)?forecast.getMaxTemp() : toFarenheit(forecast.getMaxTemp());
        float minTemp = (mCurrentMetrics == SettingsActivity.PREF_CELSIUS)?forecast.getMinTemp() : toFarenheit(forecast.getMinTemp());

        String metricString = (mCurrentMetrics == SettingsActivity.PREF_CELSIUS)? "°C" : "°F";

        mMaxTemp.setText(String.format(getString(R.string.max_temp_parameter), maxTemp) + metricString);
        mMinTemp.setText(String.format(getString(R.string.min_temp_parameter), minTemp) + metricString);
        mHumidity.setText(String.format(getString(R.string.humidity_parameter), (int)forecast.getHumidity()));
        mDescription.setText(forecast.getDescription());
    }

    public int getCurrentMetricsFromSettings() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String stringMetrics = pref.getString(getString(R.string.metric_selection), String.valueOf(SettingsActivity.PREF_CELSIUS));
        int metrics = Integer.valueOf(stringMetrics);
        return metrics;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Para coger los datos de la pantalla de settings
    @Override
    protected void onResume() {
        super.onResume();
        final int previousMetrics = mCurrentMetrics;
        int metrics = getCurrentMetricsFromSettings();
        if (metrics != mCurrentMetrics) {
            mCurrentMetrics = metrics;
            setForecast(mForecast);
            //android.R.id.content -> id de la vista raiz de mi app
            //Snackbar.make(findViewById(android.R.id.content), "Preferencias actualizadas", Snackbar.LENGTH_LONG).show();
            final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

            Snackbar.make(findViewById(android.R.id.content), R.string.updated_preferences, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pref.edit().putString(getString(R.string.metric_selection), String.valueOf(previousMetrics)).apply();
                            mCurrentMetrics = previousMetrics;
                            setForecast(mForecast);
                        }
            }).show();
        }


    }
}

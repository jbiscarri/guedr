package com.biscarri.guedr.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.biscarri.guedr.R;
import com.biscarri.guedr.fragment.CityListFragment;
import com.biscarri.guedr.fragment.CityPagerFragment;
import com.biscarri.guedr.model.City;

/**
 * Created by joanbiscarri on 08/09/15.
 */
public class MainActivity extends AppCompatActivity implements CityListFragment.CityListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Clase para obtener datos de tamaño del dispositivo
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        float density = metrics.density;
        int dpWidth = (int) (width/density);
        int dpHeight = (int) (height/density);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getFragmentManager();

        //Miramos si existe la zona para insertar cityList
        if (findViewById(R.id.cityList) != null) {
            //Si existe la zona, mireo si hay que crear el fragment
            if (fm.findFragmentById(R.id.cityList) == null) {
                fm.beginTransaction()
                        //.add(R.id.fragment, CityPagerFragment.newInstance())
                        .add(R.id.cityList, CityListFragment.newInstance())
                        .commit();
            }
        }
        if (findViewById(R.id.cityPager) != null) {
            if (fm.findFragmentById(R.id.cityPager) == null) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                int lastCityIndex = pref.getInt(CityPagerFragment.PREF_LAST_CITY, 0);

                fm.beginTransaction()
                        .add(R.id.cityPager, CityPagerFragment.newInstance(lastCityIndex))
                        .commit();
            }
        }
    }

    @Override
    public void onCitySelected(City city, int index) {
        //Vamos a averiguar si tengo un viewpager en pantlla y tengo que mover el view pager a otra posicion
        //o tengo que abrir otra actvity

        if (findViewById(R.id.cityPager) != null) {
            //Hay hueco para el city pager: lo muevo a la ciudad seleccionada
            FragmentManager fm = getFragmentManager();
            CityPagerFragment cityPagerFragment = (CityPagerFragment) fm.findFragmentById(R.id.cityPager);
            cityPagerFragment.goToCity(index);
        }else {
            //Lanzar una activity
            Intent cityPagerIntent = new Intent(this, CityPagerActivity.class);
            cityPagerIntent.putExtra(CityPagerActivity.EXTRA_CITY_INDEX, index);
            startActivity(cityPagerIntent);
        }
    }
}

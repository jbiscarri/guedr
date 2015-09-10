package com.biscarri.guedr.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.biscarri.guedr.R;
import com.biscarri.guedr.fragment.CityListFragment;
import com.biscarri.guedr.fragment.CityPagerFragment;

/**
 * Created by joanbiscarri on 10/09/15.
 */
public class CityPagerActivity  extends AppCompatActivity{

    public static final String EXTRA_CITY_INDEX = "com.biscarri.guedr.activity.CityPagerActivity.EXTRA_CITY_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_citypager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(R.id.fragment) == null) {
            //cojo el indice de la ciudad que he seleccionado que me pasan en el intent
            int initialCityIndex = getIntent().getIntExtra(EXTRA_CITY_INDEX, 0);
            fm.beginTransaction()
                    .add(R.id.fragment, CityPagerFragment.newInstance(initialCityIndex))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Esto me asegura que finaliza la actividad actual y regresa a la que hayamos definido en el AndroidManifest.xml
            //Sirve si pudiera acceder a CityPager desde varios sitios y no quiero marear al usuario con la navegacion
            //Ejemplo: correo de Gmail
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}

package com.biscarri.guedr.activity;

import android.app.FragmentManager;
import android.os.Bundle;
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

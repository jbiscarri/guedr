package com.biscarri.guedr.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(R.id.fragment) == null) {
            fm.beginTransaction()
                    //.add(R.id.fragment, CityPagerFragment.newInstance())
                    .add(R.id.fragment, CityListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onCitySelected(City city, int index) {
        //Lanzar una activity
        Intent cityPagerIntent = new Intent(this, CityPagerActivity.class);
        cityPagerIntent.putExtra(CityPagerActivity.EXTRA_CITY_INDEX, index);
        startActivity(cityPagerIntent);
    }
}

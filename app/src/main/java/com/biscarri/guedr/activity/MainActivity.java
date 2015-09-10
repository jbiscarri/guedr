package com.biscarri.guedr.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
        FragmentManager fm = getFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (fm.findFragmentById(R.id.fragment) == null) {
            fm.beginTransaction()
                    //.add(R.id.fragment, CityPagerFragment.newInstance())
                    .add(R.id.fragment, CityListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onCitySelected(City city, int index) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment, CityPagerFragment.newInstance())
                .commit();
    }
}

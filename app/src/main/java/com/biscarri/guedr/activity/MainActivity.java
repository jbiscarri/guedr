package com.biscarri.guedr.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.biscarri.guedr.R;
import com.biscarri.guedr.fragment.CityPagerFragment;

/**
 * Created by joanbiscarri on 08/09/15.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getFragmentManager();

        if (fm.findFragmentById(R.id.fragment_citypager) == null) {
            fm.beginTransaction()
                    .add(R.id.fragment_citypager, CityPagerFragment.newInstance())
                    .commit();
        }
    }
}

package com.biscarri.guedr.activity;

import android.app.FragmentManager;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Controlo el boton back de la tool bar
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
            if (fm.getBackStackEntryCount() == 1)
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCitySelected(City city, int index) {
        FragmentManager fm = getFragmentManager();

        fm.beginTransaction()
                .replace(R.id.fragment, CityPagerFragment.newInstance(index))
                .addToBackStack(null)//Este id sirve para ir a un elemento del stack en concreto
                .commit();
                //Ponemos la flecha back para navegar atras
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Metodo que sabe cuando se aprieta el back (boton)

    @Override
    public void onBackPressed() {
        //Hay que eliminar el super, para que no haga finish
        //super.onBackPressed();
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            if (fm.getBackStackEntryCount() == 1)
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }else {
            super.onBackPressed();
        }
    }
}

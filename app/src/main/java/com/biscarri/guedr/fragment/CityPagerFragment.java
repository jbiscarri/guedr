package com.biscarri.guedr.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.biscarri.guedr.R;
import com.biscarri.guedr.model.Cities;
import com.biscarri.guedr.model.City;

/**
 * Created by joanbiscarri on 08/09/15.
 */
public class CityPagerFragment extends Fragment{

    //Clave del argumento del Fragment para sacarlo del Bundle arguments
    private static final String ARG_CITY_INDEX = "cityIndex";
    private BroadcastReceiver mBroadcastReceiver;


    //Clave  del diccionario de preferences para guardar la ultima ciudad seleccionada
    public static final String PREF_LAST_CITY = "com.biscarri.guedr.fragment.CityPagerFragment.PREF_LAST_CITY";


    private Cities mCities;
    private ViewPager mPager;
    private int mInitialIndex;

    public static CityPagerFragment newInstance(int initialCityIndex) {
        //Creamos fragment
        CityPagerFragment fragment = new CityPagerFragment();

        //Creamos argumentos y empaquetamos
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_CITY_INDEX, initialCityIndex);
        //asignamos argumentos al fragment
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Recojo los argumentos
        if (getArguments() != null) {
            mInitialIndex = getArguments().getInt(ARG_CITY_INDEX);
        }
    }

    @Nullable
    @Override
    //tocar interfaz, aqui ya existe la interfaz
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_citypager, container, false);

        mCities = Cities.getInstance(getActivity());

        mPager = (ViewPager) root.findViewById(R.id.view_pager);
        CityPagerAdapter cityPagerAdapter = new CityPagerAdapter(getFragmentManager());
        mPager.setAdapter(cityPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                updateCityInfo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        //Para actualizar el texto de la primera ciudad y mostrar la que toque
        goToCity(mInitialIndex);

        //Me subscribo al broadcast
        //mBroadcastReceiver = new CityBroadcastReceiver(cityPagerAdapter);
        mBroadcastReceiver = new CityBroadcastReceiver(mPager.getAdapter());
        getActivity().registerReceiver(
                mBroadcastReceiver,
                new IntentFilter(Cities.CITY_LIST_CHANGED_ACTION));
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mBroadcastReceiver);
        mBroadcastReceiver = null;
    }

    //Metodos para movernos por la ciudad
    protected void updateCityInfo(int position) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pref.edit()
                .putInt(PREF_LAST_CITY, position)
                .apply();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && mCities.getCities().size() > 0) {
            actionBar.setTitle(mCities.getCities().get(position).getName());
        }
    }

    public void goToCity(int index) {
        mPager.setCurrentItem(index);
        updateCityInfo();
    }

    protected void updateCityInfo() {
        updateCityInfo(mPager.getCurrentItem());
    }


    //M'etodos de menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.citypager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() == R.id.next) {
           mPager.setCurrentItem(mPager.getCurrentItem()+1);
           updateCityInfo();
           return true;
       } else if (item.getItemId() == R.id.previous) {
           mPager.setCurrentItem(mPager.getCurrentItem()-1);
           updateCityInfo();
           return true;
       }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mPager != null) {
            MenuItem menuNext = menu.findItem(R.id.next);
            MenuItem menuPrev = menu.findItem(R.id.previous);
            menuPrev.setEnabled(mPager.getCurrentItem() > 0);
            //menuNext.setEnabled(mPager.getCurrentItem() < mCities.getCities().size()-1);
            menuNext.setEnabled(mPager.getCurrentItem() < mPager.getChildCount()-1);
        }
    }

    protected class CityPagerAdapter extends FragmentPagerAdapter {

        private Cities mCities;

        public CityPagerAdapter(FragmentManager fm) {
            super(fm);
            mCities = Cities.getInstance(getActivity());
        }

        @Override
        public int getCount() {
            return mCities.getCities().size();
        }

        @Override
        public Fragment getItem(int i) {
            return ForecastFragment.newInstance(mCities.getCities().get(i));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            City city = mCities.getCities().get(position);
            return String.format(city.getName());
        }
    }

    //Esta clase se va a enterar de cuando ha cambiado el modelo cities
    private class CityBroadcastReceiver extends BroadcastReceiver {

        private PagerAdapter mAdapter;

        public CityBroadcastReceiver(PagerAdapter adapter) {
            super();
            mAdapter = adapter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            //Hay nuevos cambios, aviso al adaptador para que vuelva a recargarse
            mAdapter.notifyDataSetChanged();
        }
    }
}

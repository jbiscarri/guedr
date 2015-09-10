package com.biscarri.guedr.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.biscarri.guedr.R;
import com.biscarri.guedr.model.Cities;
import com.biscarri.guedr.model.City;

/**
 * Created by joanbiscarri on 08/09/15.
 */
public class CityPagerFragment extends Fragment {

    //Clave del argumento del Fragment para sacarlo del Bundle arguments
    private static final String ARG_CITY_INDEX = "cityIndex";

    private Cities mCities;
    private ViewPager mPager;
    private int mInitialIndex;

    public static CityPagerFragment newInstance() {
        return new CityPagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    //tocar interfaz, aqui ya existe la interfaz
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_citypager, container, false);
        mCities = Cities.getInstance();

        mPager = (ViewPager) root.findViewById(R.id.view_pager);
        mPager.setAdapter(new CityPagerAdapter(getFragmentManager()));

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateCityInfo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateCityInfo(mPager.getCurrentItem());

        return root;
    }

    protected void updateCityInfo(int position) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mCities.getCities().get(position).getName());
        }
    }

    protected void updateCityInfo() {
        updateCityInfo(mPager.getCurrentItem());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.citypager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() == R.id.next) {
            mPager.setCurrentItem(mPager.getCurrentItem()+1);
       } else if (item.getItemId() == R.id.previous) {
           mPager.setCurrentItem(mPager.getCurrentItem()-1);
       }
        updateCityInfo();

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
            mCities = Cities.getInstance();
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
}

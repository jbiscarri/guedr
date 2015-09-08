package com.biscarri.guedr.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biscarri.guedr.R;

/**
 * Created by joanbiscarri on 08/09/15.
 */
public class CityPagerFragment extends Fragment {

    public static CityPagerFragment newInstance() {
        return new CityPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_citypager, container, false);

        ViewPager pager = (ViewPager) root.findViewById(R.id.view_pager);
        pager.setAdapter(new CityPagerAdapter(getFragmentManager()));
        return root;
    }

    protected class CityPagerAdapter extends FragmentPagerAdapter {

        public CityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int i) {
            return ForecastFragment.newInstance();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.format("Ciudad número %d", position + 1);
        }
    }
}

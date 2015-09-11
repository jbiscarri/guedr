package com.biscarri.guedr.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.biscarri.guedr.R;
import com.biscarri.guedr.model.Cities;
import com.biscarri.guedr.model.City;

/**
 * Created by joanbiscarri on 09/09/15.
 */
public class CityListFragment extends Fragment {

    private CityListListener mListener;
    private BroadcastReceiver mBroadcastReceiver;

    public static CityListFragment newInstance() {
        return new CityListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_city_list, container, false);
        Cities cities = Cities.getInstance(getActivity());
        ListView list = (ListView) root.findViewById(android.R.id.list);
        final ArrayAdapter<City> adapter = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_list_item_1, cities.getCities());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onCitySelected(adapter.getItem(position), position);
                }
            }
        });

        mBroadcastReceiver = new CityBroadcastReceiver(adapter);
        getActivity().registerReceiver(
                mBroadcastReceiver,
                new IntentFilter(Cities.CITY_LIST_CHANGED_ACTION));
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (CityListListener) getActivity();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (CityListListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mBroadcastReceiver);
        mBroadcastReceiver = null;
    }

    public interface CityListListener {
        void onCitySelected(City city, int index);
    }

    //Esta clase se va a enterar de cuando ha cambiado el modelo cities
    private class CityBroadcastReceiver extends BroadcastReceiver {

        private ArrayAdapter mAdapter;

        public CityBroadcastReceiver(ArrayAdapter adapter) {
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

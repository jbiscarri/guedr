package com.biscarri.guedr.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by joanbiscarri on 09/09/15.
 */
public class Cities {

    public static final String PREF_CITIES = "com.biscarri.guedr.model.Cities.PREF_CITIES";
    public static final String CITY_LIST_CHANGED_ACTION = "com.biscarri.guedr.model.Cities.CITY_LIST_CHANGED_ACTION";

    private static Cities ourInstance;

    private List<City> mCities;
    private WeakReference<Context> mContext;

    public static Cities getInstance(Context context)
    {
        //1. comprobamos si ya tenemos la instancia y asi no la creamos
        //Debemos comprobar si no hemos perdido la referencia al contexto
        if (ourInstance == null || ourInstance.mContext.get() == null) {
            synchronized (PREF_CITIES) {
                if (ourInstance == null) {
                    //No tenemos instancia, vamos a crearla a partir de las preferencias
                    ourInstance = new Cities(context);
                } else if (ourInstance.mContext.get() == null) {
                    //He perdido la referencia al contecto, lo vuelvo a crear
                    ourInstance.mContext = new WeakReference<Context>(context);
                }
            }
        }
        return ourInstance;
    }

    private Cities(Context context) {
        mCities = new LinkedList<>();
        //Guardo context
        mContext = new WeakReference<Context>(context);
        //Cojo ciudades de las preferencias
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        //Sacamos las ciudades como un conjunto de cadenas
        Set<String> cities = pref.getStringSet(PREF_CITIES, new HashSet<String>());
        //Recorremos las ciudades que hemos saccado del conjunto y las añadimos a la instancia
        for (String city : cities)
        {
            mCities.add(new City(city, new Forecast(30, 15, 20, "Sol con algunas nubes", "ico01")));
        }
    }

    public void addCity(String cityName) {
        mCities.add(new City(cityName));
        save();
        if (mContext.get() != null) {
            Intent bradcast = new Intent(CITY_LIST_CHANGED_ACTION);
            mContext.get().sendBroadcast(bradcast);
        }
    }

    public void save() {
        synchronized (PREF_CITIES) {
            if (mContext.get() != null) {
                //Obtenemos SharedPreferences
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext.get());
                //Recorremos ciudasdes y creamos un conjunto
                Set<String> citiesSet = new HashSet<>();
                for (City city : mCities) {
                    citiesSet.add(city.getName());
                }
                //Guardamos ciudads
                pref.edit()
                        .putStringSet(PREF_CITIES, citiesSet)
                        .apply();
            }
        }
    }

    public List<City> getCities() {
        return mCities;
    }


}

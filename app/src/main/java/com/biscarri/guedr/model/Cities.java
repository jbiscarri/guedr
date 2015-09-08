package com.biscarri.guedr.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joanbiscarri on 09/09/15.
 */
public class Cities {
    private static Cities ourInstance = new Cities();

    private List<City> mCities;

    public static Cities getInstance() {
        return ourInstance;
    }

    private Cities() {
        mCities = new LinkedList<>();
        mCities.add(new City("Madrid", new Forecast(32, 19, 51, "Sol con algunas nubes", "icon1")));
        mCities.add(new City("Barcelona", new Forecast(30, 26, 85, "Soleado", "icon2")));
        mCities.add(new City("New York", new Forecast(25, 20, 70, "Nublado", "icon1")));
        mCities.add(new City("London", new Forecast(15, 10, 80, "Lluvioso", "icon1")));
    }

    public List<City> getCities() {
        return mCities;
    }
}

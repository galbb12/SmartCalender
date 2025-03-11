package com.gal.smartcalender;

import android.util.Pair;

public abstract class DataProcessor {
    public abstract void processData(Pair<String, String> data); // First term is data source and second is the data

}

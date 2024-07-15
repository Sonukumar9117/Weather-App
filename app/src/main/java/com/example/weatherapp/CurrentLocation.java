package com.example.weatherapp;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class CurrentLocation {
    Context context;

    public CurrentLocation(Context context) {
        this.context = context;
    }
    FusedLocationProviderClient fusedLocationProviderClient;

    public  String cityName(){

    }
}

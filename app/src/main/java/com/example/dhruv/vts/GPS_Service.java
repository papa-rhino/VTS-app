package com.example.dhruv.vts;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/**
 * Created by LENOVO on 11-Mar-18.
 */

public class GPS_Service extends Service implements Serializable {
    private LocationListener listener;
    private LocationManager locationManager;
    private DatabaseReference mUserDatabase;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                Intent i = new Intent("location_update");
                i.putExtra("coordinates",location.getLongitude()+";"+location.getLatitude());

                sendBroadcast(i);
                Log.e("555", String.valueOf(location.getLatitude()));
                mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("driverloc");

                mUserDatabase.child("longitude").setValue(String.valueOf(location.getLongitude()));
                mUserDatabase.child("latitude").setValue(String.valueOf(location.getLatitude()));


            }


            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listener);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}

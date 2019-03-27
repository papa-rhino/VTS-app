package com.example.dhruv.vts;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Driver_View_Status extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    FirebaseDatabase database;
    DatabaseReference myRef;
    double end_latitude, end_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__view__status);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        database = FirebaseDatabase.getInstance();
        myRef= database.getReference("Users");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // googleMapOptions.mapType(googleMap.MAP_TYPE_HYBRID)
        //    .compassEnabled(true);

        // Add a marker in Sydney and move the camera

        mMap.setOnMarkerClickListener(this);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value[];

                Log.e("children location",dataSnapshot.getChildrenCount()+"");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {


                    value= postSnapshot.getValue().toString().split(",");
                    Log.e("array location",value[1]+""+postSnapshot.getValue().toString());
                    end_latitude=Double.parseDouble(value[0].substring(10));
                    end_longitude=Double.parseDouble(value[1].substring(11,21));

                    setData(end_latitude,end_longitude);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Driver_View_Status.this, databaseError+"", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void setData(double end_latitude, double end_longitude) {
        Log.e("lat Long",end_latitude+"");
        setmMap(new LatLng(end_latitude,end_longitude));
    }
    void setmMap(LatLng latLng){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(false));
        end_latitude=latLng.latitude;
        end_longitude=latLng.longitude;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this,end_latitude+" , "+end_longitude, Toast.LENGTH_SHORT).show();
        return false;
    }
}

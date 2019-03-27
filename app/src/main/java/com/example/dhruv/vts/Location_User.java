package com.example.dhruv.vts;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;

import org.json.JSONObject;

public class Location_User extends FragmentActivity implements OnMapReadyCallback,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            GoogleMap.OnMarkerDragListener,GoogleMap.OnMapClickListener,
            GoogleMap.OnMapLongClickListener,
            GoogleMap.OnMarkerClickListener,
            View.OnClickListener {

        private static final String TAG = "MapsActivity";
        private GoogleMap mMap;
        private double longitude;
        private double latitude;
        double end_latitude, end_longitude;
        private GoogleApiClient googleApiClient;
    FirebaseDatabase database;
        DatabaseReference myRef;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.location__user);
            database = FirebaseDatabase.getInstance();
            myRef= database.getReference("Users");

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            //Initializing googleApiClient
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }



        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMapOptions.mapType(googleMap.MAP_TYPE_HYBRID)
            //    .compassEnabled(true);

            // Add a marker in Sydney and move the camera
            LatLng india = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(india).title("Marker in India"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMarkerClickListener(this);
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMapClickListener(this);
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

                }
            });



        }

    private void setData(double end_latitude, double end_longitude) {
        Log.e("lat Long",end_latitude+"");
  setmMap(new LatLng(end_latitude,end_longitude));
    }

    //Getting current location
        private void getCurrentLocation() {
            mMap.clear();
           /* if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }*/
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                //moving the map to location
                moveMap();
            }
        }

        private void moveMap() {
            /*
             * Creating the latlng object to store lat, long coordinates
             * adding marker to map
             * move the camera with animation
             */
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(true)
                    .title("Marker in India"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mMap.getUiSettings().setZoomControlsEnabled(true);


        }


        @Override
        public void onClick(View view) {
            Log.v(TAG,"view click event");
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            getCurrentLocation();
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

     @Override
     public void onMapClick(LatLng latLng) {
         //Toast.makeText(this, "Buuuuuuuuu", Toast.LENGTH_SHORT).show();
//setmMap();
     }

     void setmMap(LatLng latLng){
         mMap.clear();
         mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
         end_latitude=latLng.latitude;
         end_longitude=latLng.longitude;

         distanceCustom();
         //move to current position
         moveMap();
     }


    @Override
        public void onMapLongClick(LatLng latLng) {

        }

        @Override
        public void onMarkerDragStart(Marker marker) {
           // Toast.makeText(Location_User.this, "onMarkerDragStart", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            //Toast.makeText(Location_User.this, "onMarkerDrag", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {

            // getting the Co-ordinates
          //  latitude = marker.getPosition().latitude;
           // longitude = marker.getPosition().longitude;


        }
        private void distanceCustom(){
            Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show();
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,getDirectionsUrl() , null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response


                            try{
                              //  Gson gson=new Gson();
                           //DistanceMatrix distanceMatrix = gson.fromJson(response.toString(),DistanceMatrix.class);
                               // Toast.makeText(Location_User.this, response.toString(), Toast.LENGTH_SHORT).show();
                                Parser parser=new Parser();
                             displayDirection(parser.parseDirections(response.toString()));
                                Log.e("Weeelcome",response.toString());
                                }
                            catch (Exception e){Log.d("Response", response.toString());}
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.toString());
                }

            } );

            RequestQueue queue = Volley.newRequestQueue(this);

            queue.add(getRequest);

        }


  private void showDuration(){

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,getDurationUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response


                        try{
                            Log.e("Wee",response.toString());
                              Gson gson=new Gson();
                            DistanceMatrix distanceMatrix = gson.fromJson(response.toString(),DistanceMatrix.class);
                             Toast.makeText(Location_User.this, distanceMatrix.getRows().get(0).getElements().get(0).getDuration().getText(), Toast.LENGTH_SHORT).show();
                           DurationParser durationParser=new DurationParser();
                            displayDirection(durationParser.parseDuration(response.toString()));

                        }
                        catch (Exception e){Log.d("Response", response.toString());}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }

        } );

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(getRequest);

    }


    @NonNull
    private String getDirectionsUrl() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + latitude + "," + longitude);
        googleDirectionsUrl.append("&destination=" + end_latitude + "," + end_longitude);
        googleDirectionsUrl.append("&key=" + "AIzaSyAV9BCqIwKwUQvXTdXY6JEcm__1xTpMinc");

        return googleDirectionsUrl.toString();

    }



    @NonNull
    private String getDurationUrl() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+latitude+","+longitude+"&destinations="+end_latitude+","+end_longitude+"&key=AIzaSyAV9BCqIwKwUQvXTdXY6JEcm__1xTpMinc");
/*
        googleDirectionsUrl.append("origins=Delhi,IN");
        googleDirectionsUrl.append("&destinations=Lucknow,IN'");
        googleDirectionsUrl.append("&key=AIzaSyCb6PsYstBEuVQ8dE7y7HLzEagwi4OAh8w");
*/

        return googleDirectionsUrl.toString();

    }

    public void displayDirection(String[] directionsList)
    {

        int count = directionsList.length;
        for(int i = 0;i<count;i++)
        {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.BLUE);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));

            mMap.addPolyline(options);
        }
    }
















        @Override
        protected void onStart() {
            googleApiClient.connect();
            super.onStart();
        }

        @Override
        protected void onStop() {
            googleApiClient.disconnect();
            super.onStop();
        }


        @Override
        public boolean onMarkerClick(Marker marker) {

           showDuration();
            return true;
        }

    }
package com.example.munchthecrunchalpha;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;

    protected EditText userLocationEditText;
    protected EditText foodLocationEditText;
    protected Button foodButton;
    protected Button userButton;

    protected String userLocation;
    protected String foodLocation;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userLocationEditText = findViewById(R.id.userLocationEditText);
        foodLocationEditText = findViewById(R.id.foodLocationEditText);

        foodButton = findViewById(R.id.button);
        userButton = findViewById(R.id.buttonUser);

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodLocation = foodLocationEditText.getText().toString();
                geoLocate(foodLocation);
                if (!userButton.getText().toString().isEmpty()) {
                    go(userLocation, foodLocation);
                }
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLocation = userLocationEditText.getText().toString();
                geoLocate(userLocation);
                if (!foodButton.getText().toString().isEmpty()) {
                    go(userLocation, foodLocation);
                }
            }
        });

        getLocationPermission();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void geoLocate(String location) {

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            Log.d("MunchTheCrunchALPHA", "looking for a place");
            list = geocoder.getFromLocationName(location, 1);
        }
        catch (IOException e){
            Log.d("MunchTheCrunchALPHA","you failed." + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d("MunchTheCrunchALPHA", "geoLocate: found a location: " + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");
                        } else {
                            Toast.makeText(MapsActivity.this, "unable to find your location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d("MunchTheCrunchALPHA", "Security exception: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.d("MunchTheCrunchALPHA", "moveCamera: moving the camera to: lat: " + latlng.latitude + ", lng: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        MarkerOptions options = new MarkerOptions().position(latlng).title(title);

        mMap.addMarker(options);
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
        }
    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    protected void go(String theUserLocation, String theFoodLocation) {
        int time = Calendar.HOUR_OF_DAY*60 + Calendar.MINUTE;

        InputStream file;
        Scanner scan = null;

        try {
            file = getAssets().open("database.txt");
            scan = new Scanner(file);
            double best = 1000000.0;
            String company = "";

            while (scan.hasNextLine()) {
                String s = scan.nextLine();
                String[] temp = s.split(":", 0);
                Log.d("MunchTheCrunchALPHA", temp[0] + "," + temp[1]);

                if (temp[0].equals(theUserLocation)) {
                    Log.d("MunchTheCrunchALPHA", "1");
                    if (temp[1].equals(theFoodLocation)) {
                        Log.d("MunchTheCrunchALPHA", "2");

                        if (time > Integer.parseInt(temp[2]) - 300) {
                            Log.d("MunchTheCrunchALPHA", "3");

                            if (time < Integer.parseInt(temp[2]) + 300) {
                                Log.d("MunchTheCrunchALPHA", "4");

                                if (best > Double.parseDouble(temp[3])) {
                                    Log.d("MunchTheCrunchALPHA", "hit!");
                                    best = Double.parseDouble(temp[3]);
                                    company = temp[4];
                                }
                            }
                        }
                    }
                }
            }

            //TODO: Display the best price and company to the user
            Toast toast = Toast.makeText(getApplicationContext(), company + String.format(": $.2d",best), Toast.LENGTH_LONG);
            toast.show();
            Log.d("MunchTheCrunchALPHA", "Success!");
        } catch (IOException e) {
            Log.d("MunchTheCrunchALPHA", "frick");
        } finally {
            if (scan != null) {
                scan.close();
            }
        }
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

        //userLocationEditText.setAdapter(mPlaceAutocompleteAdapter);
        //foodLocationEditText.setAdapter(mPlaceAutocompleteAdapter);

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        // Add a marker in Sydney and move the camera

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                mMap.
//            }
//        } {
//
//        });
    }
}

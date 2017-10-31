package tiagov.rideabike1;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener,
        OnMapLongClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public double latitude;
    public double longitude;
    LatLng posColect;
    LatLng posDispache;
    public LocationManager locationManager;
    public Criteria criteria;
    public String provider;
    Marker markerColect;
    Marker markerDispache;

    private boolean mPermissionDenied = false;
    private EditText mLocalColeta;
    private EditText mLocalEntrega;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocalColeta = (EditText) findViewById(R.id.local_coleta);
        mLocalEntrega = (EditText) findViewById(R.id.local_entrega);

        Button bnt = (Button) findViewById(R.id.button);
        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NewOrderActivity.wMap.equals("colect")) {
                    NewOrderActivity.latLngColect = posColect;
                    NewOrderActivity.wMap = "dispache";
                    startActivity(new Intent(view.getContext(), MapsActivity.class));
                }

                if (NewOrderActivity.wMap.equals("dispache")) {
                    NewOrderActivity.latLngDispache = posDispache;
                    finish();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocation();

        getLocation();

        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enableMyLocation();

    }

    @Override
    public void onPause() {
        super.onPause();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void getLocation() {
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);


        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(MapsActivity.this, "getlocation latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onLocationChanged(Location location) {
        //remove location callback:
        locationManager.removeUpdates(this);

        getLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        Toast.makeText(MapsActivity.this, R.string.permission_required_toast,
                Toast.LENGTH_SHORT).show();
        MapsActivity.this.finish();
    }

    public void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);


        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Enable the   my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(MapsActivity.this, "latlng" + latLng, Toast.LENGTH_SHORT).show();

        if (NewOrderActivity.wMap.equals("colect")) {
            if (markerColect == null) {
                markerColect = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Ponto de coleta")
                );

            } else {
                markerColect.setPosition(latLng);
            }
            posColect = latLng;
            //latLngDispache = latLng;
            //String position = (String.valueOf(latLng.latitude)+ String.valueOf(latLng.longitude));
            //mLocalColeta.setText(position);
            //mLocalColeta.setText(markerColect.getPosition().toString());
        }

        if (NewOrderActivity.wMap.equals("dispache")) {
            if (markerDispache == null) {
                markerDispache = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Ponto de entrega")
                );

                //String lat = String.valueOf(latLng.latitude);
                //String lon= String.valueOf(latLng.longitude);
            } else {
                markerDispache.setPosition(latLng);
            }
            posDispache = latLng;
            //mLocalEntrega.setText(markerDispache.getPosition().toString());
            //latLngDispache = latLng;
        }

    }
}
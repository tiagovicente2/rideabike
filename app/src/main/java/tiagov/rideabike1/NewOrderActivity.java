package tiagov.rideabike1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class NewOrderActivity extends MapsActivity implements
        View.OnClickListener,
        OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public static LatLng latLngColect;
    public static LatLng latLngDispache;

    public static String wMap;
    private Polyline polyline;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_order);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.map_button_dispache).setOnClickListener(this);
        findViewById(R.id.map_button_colect).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.map_button_colect) {
            wMap = "colect";
            startActivity(new Intent(v.getContext(), MapsActivity.class));
        }
        if (i == R.id.fab) {
            //startActivity(new Intent(v.getContext(), MapsActivity.class));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocation();

        //getLocation();

    }

    @Override
    public void onResume() {
        super.onResume();
        drawRoute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        latLngDispache = null;
        latLngColect = null;
    }

    public void drawRoute() {

        PolylineOptions po;

        if (latLngColect != null && latLngDispache != null) {
            po = new PolylineOptions();

            po.add(latLngColect);
            po.add(latLngDispache);
            po.width(15);
            po.color(Color.RED);
            polyline = mMap.addPolyline(po);

            /*mMap.addPolyline(new PolylineOptions()
                    .add(latLngColect)
                    .add(latLngDispache)
                    .width(15)
                    .color(Color.RED)
            );*/

            mMap.addMarker(new MarkerOptions()
                    .position(latLngColect)
                    .title("Ponto de coleta")
            );

            mMap.addMarker(new MarkerOptions()
                    .position(latLngDispache)
                    .title("Ponto de coleta")
            );
        }

    }

    public void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(NewOrderActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);


        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    public void getLocation() {
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Toast.makeText(NewOrderActivity.this, "getlocation latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}

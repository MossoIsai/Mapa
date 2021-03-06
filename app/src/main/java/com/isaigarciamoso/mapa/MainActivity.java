package com.isaigarciamoso.mapa;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mapaGoogle;
    private Button btnPedirTaxi;
    private EditText txtDestino;
    private Geocoder geocoder;
    private List<Address> addressList;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String name;
    private SearchView searchView;
    private double lat;
    private double lng;

    private double currentLat;
    private double currenLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_mapa);

        initView();

    }

    public void initView() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(MainActivity.this);
        btnPedirTaxi = (Button) findViewById(R.id.btnPedirTaxi);
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Display-Bold.otf");
        btnPedirTaxi.setTypeface(typefaceBold);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentLat = location.getLatitude();
        currenLong =  location.getLongitude();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapaGoogle = googleMap;
        mapaGoogle.setOnMapLongClickListener(listener);
        mapaGoogle.setOnMapClickListener(onMapClickListener);

        boolean success = mapaGoogle.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_retro));
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
        mapaGoogle.setPadding(10, 250, 10, 250);
        mapaGoogle.setMyLocationEnabled(true);
        mapaGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat,currenLong),16f));
        mapaGoogle.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mapaGoogle.getUiSettings().setMyLocationButtonEnabled(false);


       // mapaGoogle.addPolygon( new PolygonOptions().add( new LatLng(currentLat,currenLong),new LatLng()))
    }

    //Event long press
    GoogleMap.OnMapLongClickListener listener = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            try {
                lat = latLng.latitude;
                lng = latLng.longitude;

                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                addressList = geocoder.getFromLocation(lat, lng, 1);
                address = addressList.get(0).getAddressLine(0);
                city =  addressList.get(0).getLocality();
                state =  addressList.get(0).getAdminArea();
                country = addressList.get(0).getCountryName();
                postalCode =  addressList.get(0).getPostalCode();

                System.out.println("Geocoder: "+address+" "+city+" "+state+" "+country+" ");

            } catch (IOException e) {
                e.printStackTrace();
            }

            mapaGoogle.addMarker(new MarkerOptions().position(latLng).
                    title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.point_end)));


            System.out.println("Latitud: " + latLng.latitude);
            System.out.println("Longitud: " + latLng.longitude);
            Toast.makeText(getApplicationContext(), "Latitud : " + latLng.latitude + "", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Longitud: " + latLng.longitude + "", Toast.LENGTH_SHORT).show();

            mapaGoogle.addPolygon(new PolygonOptions().add( new LatLng(currentLat,currenLong),new LatLng(lat,lng)));



        }
    };

    GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            lat = latLng.latitude;
            lng = latLng.longitude;

            System.out.println("Latitud: " + latLng.latitude);
            System.out.println("Longitud: " + latLng.longitude);
            Toast.makeText(getApplicationContext(), "Latitud : " + latLng.latitude + "", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Longitud: " + latLng.longitude + "", Toast.LENGTH_SHORT).show();

        }
    };


}

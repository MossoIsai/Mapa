package com.isaigarciamoso.mapa;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mapaGoogle;
    private Button btnPedirTaxi;
    private EditText txtDestino;


    private double lat;
    private double lng;

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
        //mapaGoogle.getUiSettings().setMyLocationButtonEnabled(false);
    }

    //Event long press
    GoogleMap.OnMapLongClickListener listener = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            lat =  latLng.latitude;
            lng = latLng.longitude;

            mapaGoogle.addMarker(new MarkerOptions().position(latLng).
                    title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.point_end)));

            System.out.println("Latitud: " + latLng.latitude);
            System.out.println("Longitud: " + latLng.longitude);
            Toast.makeText(getApplicationContext(),"Latitud : "+latLng.latitude+"",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Longitud: "+latLng.longitude+"",Toast.LENGTH_SHORT).show();
        }
    };


    GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            lat =  latLng.latitude;
            lng = latLng.longitude;

            System.out.println("Latitud: "+latLng.latitude);
            System.out.println("Longitud: "+latLng.longitude);
            Toast.makeText(getApplicationContext(),"Latitud : "+latLng.latitude+"",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Longitud: "+latLng.longitude+"",Toast.LENGTH_SHORT).show();



        }
    };


}

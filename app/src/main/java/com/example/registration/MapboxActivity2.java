package com.example.registration;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.List;

public class MapboxActivity2 extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener {

    private MapView mapView;
    private Button btnTrack;
    //////////////////////////// NEW IMPLEMENT TRAKING
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    ///////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_mapbox2);

        Button btnaggdirec = findViewById(R.id.btnaggdirec);
        Button btnTrack = findViewById(R.id.btn_Track);
        Button btnverdirec = findViewById(R.id.btnverdirec);
        Button btnmicuenta = findViewById(R.id.btnmiCuenta);

        mapView = (MapView) findViewById(R.id.mapbox);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        ///////////////7new implement 21:38
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("MissingPermission")
            public void onClick(View view) {
                setCameraPosition(originLocation);

            }
        });


        ///////////SQLITE

        btnaggdirec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapboxActivity2.this, newDirecciones.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "¡Bienvenido!",Toast.LENGTH_LONG).show();

            }
        });

        btnmicuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "¡Informacion de Tu Cuenta!",Toast.LENGTH_LONG).show();
            }
        });
        btnverdirec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Leer.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "¡Todas las Ubicaciones!",Toast.LENGTH_LONG).show();
            }
        });

    }


    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            ////hacer Cosas
            initializeLocationEngine();
            initializeLocationLayer();


        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private  void initializeLocationEngine(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null){
            originLocation = lastLocation;
        }else{
            locationEngine.addLocationEngineListener(this);
        }

    }
    @SuppressWarnings("MissingPermission")
    public void initializeLocationLayer(){
        locationLayerPlugin = new LocationLayerPlugin(mapView,map,locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }
    private void setCameraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),13.0));

    }


    @Override
    @SuppressWarnings("MissingPermission")
    public void onStart() {
        super.onStart();
        if (locationEngine != null){
            locationEngine.requestLocationUpdates();
        }
        if (locationLayerPlugin != null){
            locationLayerPlugin.onStart();
        }

        mapView.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin != null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();

    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationEngine != null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }


    ///////////////////NEW IMPLEMENT TRAKING

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        enableLocation();
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null){
            originLocation = location;
            setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        /// presentar algun toas o dialogo.
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        finish();
        startActivity(getIntent());

    }
}

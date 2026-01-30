package com.polarisApp.polarisguard;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class mapa_hijo extends AppCompatActivity {

    private static final int REQ_LOC = 1001;

    private LocationComponentPlugin locationPlugin;
    private boolean hasCentered = false;

    private MapboxMap mapboxMap;
    private MapView mapView;
    private FloatingActionButton fabMain;
    private LinearLayout floatingMenu;
    private boolean isMenuOpen = false;


    private double lastSentLat = Double.NaN;
    private double lastSentLng = Double.NaN;
    private static final double DISTANCE_THRESHOLD_METERS = 20.0; // umbral en metros
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private String userId = ""; // identifica al usuario
    private String Url_Endpoint = "https://greenyellow-locust-753084.hostingersite.com/Backendconection_php/ubicacion_mapa_hijo.php"; // endpoint PHP (HTTPS)
    //variable global para acumular en el lote
    private JSONArray lote = new JSONArray();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_hijo); // Conecta con activity_main.xml


        SharedPreferences prefs = getSharedPreferences("identificador-sesion", MODE_PRIVATE);
        userId = prefs.getString("idUser", "");

        ImageButton home = findViewById(R.id.imgbtn_homeH);
        ImageButton perfil = findViewById(R.id.imgbtn_perfil);
        ImageButton notificaciones = findViewById(R.id.imgbtn_notificacion);
        ImageButton MiUbicacion = findViewById(R.id.imgbtn_miUbicacion);

        ImageButton btnSATELLITE_STREETS = findViewById(R.id.SATELLITE_STREETS_Hijo);
        ImageButton btnMAPBOX_STREETS = findViewById(R.id.MAPBOX_STREETS_hijo);
        ImageButton btnDARK = findViewById(R.id.DARK_hijo);
        ImageButton btnOUTDOORS = findViewById(R.id.OUTDOORS_hijo);
        ImageButton btnEstandar = findViewById(R.id.Estandar_hijo);


        mapView = findViewById(R.id.mapViewHijo);
        mapboxMap = mapView.getMapboxMap();
        fabMain = findViewById(R.id.fab_mainH);
        floatingMenu = findViewById(R.id.floating_menuH);

        // Iniciar flujo: GPS -> permisos -> mapa -> ubicación
        checkGpsAndPermissions();

        //programar los botones que dirigiran a las siguientes pantallas

        //mostrar o ocultar el menu.
        fabMain.setOnClickListener(v -> tooggleMenu());

        //botones para cambiar el estilo del mapa
        btnSATELLITE_STREETS.setOnClickListener(v -> {
            mapView.getMapboxMap().loadStyleUri(Style.SATELLITE_STREETS);  //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        });

        btnMAPBOX_STREETS.setOnClickListener(v -> {
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);  //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        });

        btnDARK.setOnClickListener(v -> {
            mapView.getMapboxMap().loadStyleUri(Style.DARK);  //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        });

        btnOUTDOORS.setOnClickListener(v -> {
            mapView.getMapboxMap().loadStyleUri(Style.OUTDOORS);  //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        });

        btnEstandar.setOnClickListener(v -> {
            mapView.getMapboxMap().loadStyleUri(Style.STANDARD);  //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        });
    }


    //funcion para mostrar o ocultar el menu flotante
    private void tooggleMenu(){
        if (isMenuOpen){
            floatingMenu.setVisibility(View.GONE);
        }else{
            floatingMenu.setVisibility(View.VISIBLE);
        }
        isMenuOpen = !isMenuOpen;
    }

    //ciclo de vida del mapa
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    // -------------------------
    // 1. Verificar si el GPS está activado
    // -------------------------
    private boolean isLocationEnabled() {
        android.location.LocationManager lm =
                (android.location.LocationManager) getSystemService(LOCATION_SERVICE);

        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                || lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
    }

    private void checkGpsAndPermissions() {
        if (!isLocationEnabled()) {
            Toast.makeText(this, "Activa la ubicación para continuar", Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return;
        }

        requestLocationPermission();
    }



    // -------------------------
    // 2. Pedir permisos
    // -------------------------
    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                    REQ_LOC
            );
        } else {
            initMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_LOC &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            initMap();
        }
    }

    // -------------------------
    // 3. Mostrar ubicación real del usuario
    // -------------------------
    private void initMap() {
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS, style -> {

            locationPlugin = (LocationComponentPlugin) mapView.getPlugin(Plugin.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID);
            if (locationPlugin == null) return;

            // Activar ubicación (puck por defecto)
            locationPlugin.setEnabled(true);
            locationPlugin.addOnIndicatorPositionChangedListener(point -> {
                double lat = point.latitude();
                double lng = point.longitude();
                // Centrar solo la primera vez
                if (!hasCentered) {
                    mapboxMap.setCamera(
                            new CameraOptions.Builder()
                                    .center(point)
                                    .zoom(16.0)
                                    .build()
                    );
                    hasCentered = true;
                }

                // Enviar la primera ubicación inmediatamente
                if (Double.isNaN(lastSentLat) || Double.isNaN(lastSentLng)) {
                    agregarDatosAlLote(lat, lng);
                    lastSentLat = lat;
                    lastSentLng = lng;
                    return;
                }

                // Calcular distancia desde la última posición enviada
                double distance = haversineDistanceMeters(lastSentLat, lastSentLng, lat, lng);
                if (distance >= DISTANCE_THRESHOLD_METERS) {
                    agregarDatosAlLote(lat, lng);
                    lastSentLat = lat;
                    lastSentLng = lng;
                }
            });

        });
    }

    // Haversine para distancia en metros
    private double haversineDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // radio Tierra en metros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    //metodo para acumular parametros
    private void agregarDatosAlLote(double latitud, double longitud){
        try {
            JSONObject ubicacion = new JSONObject();
            ubicacion.put("latitud", latitud);
            ubicacion.put("longitud" , longitud);
            ubicacion.put("id_user", userId);
            lote.put(ubicacion);
        } catch (JSONException e){
            e.
                    printStackTrace();
        }

        //si ya hay 20 registros en el lote, enviar el lote a la base de datos.
        if(lote.length()==10){
            enviarLoteVolley(lote);
            lote = new JSONArray(); //reiniciar para el siguiente lote
        }
    }


    //enviar el lote a la base de datos mediante voley
    private void enviarLoteVolley(JSONArray lote){
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST,
                Url_Endpoint,
                lote,
                response ->{
                    Toast.makeText(getApplicationContext(),
                            "Respuesta: " + response.toString(),
                            Toast.LENGTH_LONG).show();
                }, volleyError -> {
            volleyError.printStackTrace();
        }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}

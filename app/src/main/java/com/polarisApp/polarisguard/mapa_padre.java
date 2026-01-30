package com.polarisApp.polarisguard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
// Fuentes y capas
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mapbox.maps.plugin.gestures.GesturesPlugin;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;




public class mapa_padre extends AppCompatActivity {

    private static final int REQ_LOC = 1001;

    private LocationComponentPlugin locationPlugin;
    private boolean hasCentered = false;

    private MapboxMap mapboxMap;
    private MapView mapView;

    //clase kotlin
    private PolygonMarcarZonaRiesgo polygonMarcarZonaRiesgo;
    private PoygonoZonaSegura PolygonoZonaSegura;

    private FloatingActionButton fabMain;
    private FloatingActionButton fabMain_ruta;
    private LinearLayout floatingMenu;
    private LinearLayout getFloatingMenu_ruta;
    private boolean isMenuOpen = false;
    private boolean isMenuOpen_ruta = false;

    ImageButton Imgbtn_MarcarZona, Imgbtn_ZonaSegura;
    private boolean drawingEnabled = false;

    //para las rutas
    private OnMapClickListener mapClickListener;

    private Button btnInicio;
    private Button btnFinal;

    private enum Mode { NONE, MARK_START, MARK_END }
    private Mode currentMode = Mode.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_padre); // Conecta con mapa_padre.xml

        ImageButton Imgb_inicio = findViewById(R.id.btn_InicioM);
        ImageButton Imgb_notificaciones = findViewById(R.id.btn_notificacionesM);
        ImageButton Imgb_perfil = findViewById(R.id.btn_perfilM);

        ImageButton Imgbtn_ubicacion = findViewById(R.id.imgbtn_ubicacion);
        Imgbtn_MarcarZona = findViewById(R.id.imgbtn_marcarZona);
        //ImageButton Imgbtn_TrazarRuta = findViewById(R.id.imgbtn_trazarRuta);
        Imgbtn_ZonaSegura = findViewById(R.id.imgbtn_GeocercaSegura);

        ImageButton btnSATELLITE_STREETS = findViewById(R.id.SATELLITE_STREETS);
        ImageButton btnMAPBOX_STREETS = findViewById(R.id.MAPBOX_STREETS);
        ImageButton btnDARK = findViewById(R.id.DARK);
        ImageButton btnOUTDOORS = findViewById(R.id.OUTDOORS);
        ImageButton btnEstandar = findViewById(R.id.Estandar_padre);
        btnInicio = findViewById(R.id.btn_ruta_inicio);
        btnFinal = findViewById(R.id.btn_ruta_final);
        Button btn_enviar_señal = findViewById(R.id.btn_enviar_ruta);

        mapView = findViewById(R.id.mapViewPadre);
        mapboxMap = mapView.getMapboxMap();
        fabMain = findViewById(R.id.fab_main);
        fabMain_ruta = findViewById(R.id.fab_main_ruta);
        floatingMenu = findViewById(R.id.floating_menu);
        getFloatingMenu_ruta = findViewById(R.id.floating_generar_ruta);





        // Iniciar flujo: GPS -> permisos -> mapa -> ubicación
        checkGpsAndPermissions();

        mapView = findViewById(R.id.mapViewPadre);
        // Ya no necesitamos mapboxMap para clicks
        //MarkerHelper.setupMapClickListener(mapView); error para usar marcadores

//
//        //iniciar la opracion de marcar ruta
//        // Botones: cambian el modo; no marcan inmediatamente
//        btnInicio.setOnClickListener(v -> {
//            currentMode = Mode.MARK_START;
//            Toast.makeText(this, "Modo: marcar inicio. Haz clic en el mapa.", Toast.LENGTH_SHORT).show();
//        });
//
//        btnFinal.setOnClickListener(v -> {
//            currentMode = Mode.MARK_END;
//            Toast.makeText(this, "Modo: marcar destino. Haz clic en el mapa.", Toast.LENGTH_SHORT).show();
//        });
//
//        // Registrar listener de clics en el mapa usando GesturesPlugin
//        GesturesPlugin gestures = mapView.getPlugin(Plugin.MAPBOX_GESTURES_PLUGIN_ID);
//        if (gestures != null) {
//            mapClickListener = new OnMapClickListener() {
//                @Override
//                public boolean onMapClick(@NonNull Point point) {
//                    if (currentMode == Mode.MARK_START) {
//                        RouteHelper.INSTANCE.marcarInicio(mapa_padre.this, mapboxMap, point);
//                        Toast.makeText(mapa_padre.this, "Inicio marcado", Toast.LENGTH_SHORT).show();
//                        currentMode = Mode.NONE;
//                        return true;
//                    } else if (currentMode == Mode.MARK_END) {
//                        RouteHelper.INSTANCE.marcarFinal(mapa_padre.this, mapboxMap, point);
//                        Toast.makeText(mapa_padre.this, "Destino marcado y ruta solicitada", Toast.LENGTH_SHORT).show();
//                        currentMode = Mode.NONE;
//                        return true;
//                    }
//                    // Si no hay modo activo, no consumir el clic
//                    return false;
//                }
//            };
//            gestures.addOnMapClickListener(mapClickListener);
//        } else {
//            Toast.makeText(this, "Gestures plugin no disponible. Revisa la inicialización de MapView.", Toast.LENGTH_LONG).show();
//        }




        //ir a pantalla inicio
        Imgb_inicio.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), pantalla_inicio_mapa.class);
            v.getContext().startActivity(intent);
        });

        //ir a pantalla notificaciones
        Imgb_notificaciones.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), notificaciones.class);
            v.getContext().startActivity(intent);
        });

        //ir a pantalla perfil
        Imgb_perfil.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), perfil_usuario.class);
            v.getContext().startActivity(intent);
        });

        //mostrar o ocultar el menu.
        fabMain.setOnClickListener(v -> tooggleMenu());
        fabMain_ruta.setOnClickListener(v -> tooggleMenu_ruta());

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


        //ver mi ubicacio padre turtor
        Imgbtn_ubicacion.setOnClickListener(v -> {
            locationPlugin = (LocationComponentPlugin) mapView.getPlugin(Plugin.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID);

            if (locationPlugin == null) return;

            // Activar ubicación (puck por defecto)
            locationPlugin.setEnabled(true);

            // Seguir al usuario, es decir seuri mi ubicacion
            hasCentered = false;
            locationPlugin.addOnIndicatorPositionChangedListener(point -> {
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
            });

        });

    }

    //funcion para mostrar o ocultar el menu flotante para el manejo de estilos
    private void tooggleMenu(){
        if (isMenuOpen){
            floatingMenu.setVisibility(View.GONE);
        }else{
            floatingMenu.setVisibility(View.VISIBLE);
        }
        isMenuOpen = !isMenuOpen;
    }

    //funcion para mostrar o ocultar el menu flotante para el manejo de estilos
    private void tooggleMenu_ruta(){
        if (isMenuOpen_ruta){
            getFloatingMenu_ruta.setVisibility(View.GONE);
        }else{
            getFloatingMenu_ruta.setVisibility(View.VISIBLE);
        }
        isMenuOpen_ruta = !isMenuOpen_ruta;
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
    // 3. Mostrar ubicación real del usuario.
    // -------------------------
    private void initMap() {
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS, style -> {

            locationPlugin = (LocationComponentPlugin) mapView.getPlugin(Plugin.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID);

            if (locationPlugin == null) return;

            // Activar ubicación (puck por defecto)
            locationPlugin.setEnabled(true);

            locationPlugin.addOnIndicatorPositionChangedListener(point -> {
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
            });


            //iniciar la opracion de marcar ruta
            // Botones: cambian el modo; no marcan inmediatamente
            btnInicio.setOnClickListener(v -> {
                currentMode = Mode.MARK_START;
                Toast.makeText(this, "Modo: marcar inicio. Haz clic en el mapa.", Toast.LENGTH_SHORT).show();
            });

            btnFinal.setOnClickListener(v -> {
                currentMode = Mode.MARK_END;
                Toast.makeText(this, "Modo: marcar destino. Haz clic en el mapa.", Toast.LENGTH_SHORT).show();
            });

            // Registrar listener de clics en el mapa usando GesturesPlugin
            GesturesPlugin gestures = mapView.getPlugin(Plugin.MAPBOX_GESTURES_PLUGIN_ID);
            if (gestures != null) {
                mapClickListener = new OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull Point point) {
                        if (currentMode == Mode.MARK_START) {
                            RouteHelper.INSTANCE.marcarInicio(mapa_padre.this, mapboxMap, point);
                            Toast.makeText(mapa_padre.this, "Inicio marcado", Toast.LENGTH_SHORT).show();
                            currentMode = Mode.NONE;
                            return true;
                        } else if (currentMode == Mode.MARK_END) {
                            RouteHelper.INSTANCE.marcarFinal(mapa_padre.this, mapboxMap, point);
                            Toast.makeText(mapa_padre.this, "Destino marcado y ruta solicitada", Toast.LENGTH_SHORT).show();
                            currentMode = Mode.NONE;
                            return true;
                        }
                        // Si no hay modo activo, no consumir el clic
                        return false;
                    }
                };
                gestures.addOnMapClickListener(mapClickListener);
            } else {
                Toast.makeText(this, "Gestures plugin no disponible. Revisa la inicialización de MapView.", Toast.LENGTH_LONG).show();
            }



            //crear  un poligono y guardar como zona riesgo
            polygonMarcarZonaRiesgo = new PolygonMarcarZonaRiesgo(mapView, mapboxMap);
            polygonMarcarZonaRiesgo.initPolygon(style); // pasar el estilo cargado

            //crear  un poligono y guardar como zona riesgo
            PolygonoZonaSegura = new PoygonoZonaSegura(mapView, mapboxMap);
            PolygonoZonaSegura.initPolygon(style); // pasar el estilo cargado

            Imgbtn_MarcarZona.setOnClickListener(v -> {
                drawingEnabled = !drawingEnabled;
                GesturesPlugin gesturesPlugin = mapView.getPlugin(Plugin.MAPBOX_GESTURES_PLUGIN_ID);
                if (drawingEnabled) {
                    polygonMarcarZonaRiesgo.startDrawing(gesturesPlugin); // activar clicks en el mapa
                    Imgbtn_MarcarZona.setImageResource(R.drawable.img);
                } else {
                    polygonMarcarZonaRiesgo.stopDrawing(gesturesPlugin); // desactivar clicks y guardar
                    Imgbtn_MarcarZona.setImageResource(R.drawable.marcar_zona);
                }
            });



            Imgbtn_ZonaSegura.setOnClickListener(v -> {
                drawingEnabled = !drawingEnabled;
                GesturesPlugin gesturesPlugin = mapView.getPlugin(Plugin.MAPBOX_GESTURES_PLUGIN_ID);
                if (drawingEnabled) {
                    PolygonoZonaSegura.startDrawing(gesturesPlugin); // activar clicks en el mapa
                    Imgbtn_ZonaSegura.setImageResource(R.drawable.img);
                } else {
                    PolygonoZonaSegura.stopDrawing(gesturesPlugin); // desactivar clicks y guardar
                    Imgbtn_ZonaSegura.setImageResource(R.drawable.marcar_zona);
                }
            });

        });
    }


}

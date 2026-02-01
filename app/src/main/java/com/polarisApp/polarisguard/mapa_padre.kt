package com.polarisApp.polarisguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.Style.OnStyleLoaded
import com.mapbox.maps.plugin.MapPlugin
import com.mapbox.maps.plugin.Plugin
import com.mapbox.maps.plugin.gestures.GesturesPlugin
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener

// Fuentes y capas
class mapa_padre : AppCompatActivity() {
    private var locationPlugin: LocationComponentPlugin? = null
    private var hasCentered = false

    private var mapboxMap: MapboxMap? = null
    private var mapView: MapView? = null

    //clase kotlin
    private var polygonMarcarZonaRiesgo: PolygonMarcarZonaRiesgo? = null
    private var PolygonoZonaSegura: PoygonoZonaSegura? = null

    private var fabMain: FloatingActionButton? = null
    private var fabMain_ruta: FloatingActionButton? = null
    private var floatingMenu: LinearLayout? = null
    private var getFloatingMenu_ruta: LinearLayout? = null
    private var isMenuOpen = false
    private var isMenuOpen_ruta = false

    var Imgbtn_MarcarZona: ImageButton? = null
    var Imgbtn_ZonaSegura: ImageButton? = null
    private var drawingEnabled = false

    //para las rutas
    private var mapClickListener: OnMapClickListener? = null

    private var btnInicio: Button? = null
    private var btnFinal: Button? = null

    private enum class Mode {
        NONE, MARK_START, MARK_END
    }

    private var currentMode = Mode.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapa_padre) // Conecta con mapa_padre.xml

        val Imgb_inicio = findViewById<ImageButton>(R.id.btn_InicioM)
        val Imgb_notificaciones = findViewById<ImageButton>(R.id.btn_notificacionesM)
        val Imgb_perfil = findViewById<ImageButton>(R.id.btn_perfilM)

        val Imgbtn_ubicacion = findViewById<ImageButton>(R.id.imgbtn_ubicacion)
        Imgbtn_MarcarZona = findViewById<ImageButton>(R.id.imgbtn_marcarZona)
        //ImageButton Imgbtn_TrazarRuta = findViewById(R.id.imgbtn_trazarRuta);
        Imgbtn_ZonaSegura = findViewById<ImageButton>(R.id.imgbtn_GeocercaSegura)

        val btnSATELLITE_STREETS = findViewById<ImageButton>(R.id.SATELLITE_STREETS)
        val btnMAPBOX_STREETS = findViewById<ImageButton>(R.id.MAPBOX_STREETS)
        val btnDARK = findViewById<ImageButton>(R.id.DARK)
        val btnOUTDOORS = findViewById<ImageButton>(R.id.OUTDOORS)
        val btnEstandar = findViewById<ImageButton>(R.id.Estandar_padre)
        btnInicio = findViewById<Button>(R.id.btn_ruta_inicio)
        btnFinal = findViewById<Button>(R.id.btn_ruta_final)
        val btn_enviar_señal = findViewById<Button?>(R.id.btn_enviar_ruta)

        mapView = findViewById<MapView>(R.id.mapViewPadre)
        mapboxMap = mapView!!.mapboxMap
        fabMain = findViewById<FloatingActionButton>(R.id.fab_main)
        fabMain_ruta = findViewById<FloatingActionButton>(R.id.fab_main_ruta)
        floatingMenu = findViewById<LinearLayout>(R.id.floating_menu)
        getFloatingMenu_ruta = findViewById<LinearLayout>(R.id.floating_generar_ruta)


        // Iniciar flujo: GPS -> permisos -> mapa -> ubicación
        checkGpsAndPermissions()

        mapView = findViewById<MapView>(R.id.mapViewPadre)


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
        Imgb_inicio.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), pantalla_inicio_mapa::class.java)
            v.getContext().startActivity(intent)
        })

        //ir a pantalla notificaciones
        Imgb_notificaciones.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), notificaciones::class.java)
            v.getContext().startActivity(intent)
        })

        //ir a pantalla perfil
        Imgb_perfil.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(v!!.getContext(), perfil_usuario::class.java)
            v.getContext().startActivity(intent)
        })

        //mostrar o ocultar el menu.
        fabMain!!.setOnClickListener(View.OnClickListener { v: View? -> tooggleMenu() })
        fabMain_ruta!!.setOnClickListener(View.OnClickListener { v: View? -> tooggleMenu_ruta() })

        //botones para cambiar el estilo del mapa
        btnSATELLITE_STREETS.setOnClickListener(View.OnClickListener { v: View? ->
            mapView!!.mapboxMap.loadStyleUri(
                Style.Companion.SATELLITE_STREETS
            ) //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        })

        btnMAPBOX_STREETS.setOnClickListener(View.OnClickListener { v: View? ->
            mapView!!.mapboxMap.loadStyleUri(
                Style.Companion.MAPBOX_STREETS
            ) //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        })

        btnDARK.setOnClickListener(View.OnClickListener { v: View? ->
            mapView!!.mapboxMap.loadStyleUri(
                Style.Companion.DARK
            ) //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        })

        btnOUTDOORS.setOnClickListener(View.OnClickListener { v: View? ->
            mapView!!.mapboxMap.loadStyleUri(
                Style.Companion.OUTDOORS
            ) //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        })

        btnEstandar.setOnClickListener(View.OnClickListener { v: View? ->
            mapView!!.mapboxMap.loadStyleUri(
                Style.Companion.STANDARD
            ) //Estilos de mapa --> SATELLITE_STREETS, MAPBOX_STREETS, DARK, OUTDOORS
        })


        //ver mi ubicacio padre turtor
        Imgbtn_ubicacion.setOnClickListener(View.OnClickListener { v: View? ->
            locationPlugin =
                mapView!!.getPlugin<MapPlugin?>(Plugin.Companion.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID) as LocationComponentPlugin?
            if (locationPlugin == null) return@setOnClickListener

            // Activar ubicación (puck por defecto)
            locationPlugin!!.enabled = true

            // Seguir al usuario, es decir seuri mi ubicacion
            hasCentered = false
            locationPlugin!!.addOnIndicatorPositionChangedListener(
                OnIndicatorPositionChangedListener { point: Point? ->
                    // Centrar solo la primera vez
                    if (!hasCentered) {
                        mapboxMap!!.setCamera(
                            CameraOptions.Builder()
                                .center(point)
                                .zoom(16.0)
                                .build()
                        )
                        hasCentered = true
                    }
                })
        })
    }

    //funcion para mostrar o ocultar el menu flotante para el manejo de estilos
    private fun tooggleMenu() {
        if (isMenuOpen) {
            floatingMenu!!.setVisibility(View.GONE)
        } else {
            floatingMenu!!.setVisibility(View.VISIBLE)
        }
        isMenuOpen = !isMenuOpen
    }

    //funcion para mostrar o ocultar el menu flotante para el manejo de estilos
    private fun tooggleMenu_ruta() {
        if (isMenuOpen_ruta) {
            getFloatingMenu_ruta!!.setVisibility(View.GONE)
        } else {
            getFloatingMenu_ruta!!.setVisibility(View.VISIBLE)
        }
        isMenuOpen_ruta = !isMenuOpen_ruta
    }

    //ciclo de vida del mapa
    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    private val isLocationEnabled: Boolean
        // -------------------------
        get() {
            val lm =
                getSystemService(LOCATION_SERVICE) as LocationManager

            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

    private fun checkGpsAndPermissions() {
        if (!this.isLocationEnabled) {
            Toast.makeText(this, "Activa la ubicación para continuar", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return
        }

        requestLocationPermission()
    }


    // -------------------------
    // 2. Pedir permisos
    // -------------------------
    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQ_LOC
            )
        } else {
            initMap()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQ_LOC && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initMap()
        }
    }

    // -------------------------
    // 3. Mostrar ubicación real del usuario.
    // -------------------------
    private fun initMap() {
        mapboxMap!!.loadStyleUri(Style.Companion.MAPBOX_STREETS, OnStyleLoaded { style: Style? ->
            locationPlugin =
                mapView!!.getPlugin<MapPlugin?>(Plugin.Companion.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID) as LocationComponentPlugin?
            if (locationPlugin == null) return@loadStyleUri

            // Activar ubicación (puck por defecto)
            locationPlugin!!.enabled = true

            locationPlugin!!.addOnIndicatorPositionChangedListener(
                OnIndicatorPositionChangedListener { point: Point? ->
                    // Centrar solo la primera vez
                    if (!hasCentered) {
                        mapboxMap!!.setCamera(
                            CameraOptions.Builder()
                                .center(point)
                                .zoom(16.0)
                                .build()
                        )
                        hasCentered = true
                    }
                })


            //iniciar la opracion de marcar ruta
            // Botones: cambian el modo; no marcan inmediatamente
            btnInicio!!.setOnClickListener(View.OnClickListener { v: View? ->
                currentMode = Mode.MARK_START
                Toast.makeText(
                    this,
                    "Modo: marcar inicio. Haz clic en el mapa.",
                    Toast.LENGTH_SHORT
                ).show()
            })

            btnFinal!!.setOnClickListener(View.OnClickListener { v: View? ->
                currentMode = Mode.MARK_END
                Toast.makeText(
                    this,
                    "Modo: marcar destino. Haz clic en el mapa.",
                    Toast.LENGTH_SHORT
                ).show()
            })

            // Registrar listener de clics en el mapa usando GesturesPlugin
            val gestures =
                mapView!!.getPlugin<GesturesPlugin?>(Plugin.Companion.MAPBOX_GESTURES_PLUGIN_ID)
            if (gestures != null) {
                mapClickListener = object : OnMapClickListener {
                    override fun onMapClick(point: Point): Boolean {
                        if (currentMode == Mode.MARK_START) {
                            RouteHelper.marcarInicio(this@mapa_padre, mapboxMap!!, point)
                            Toast.makeText(this@mapa_padre, "Inicio marcado", Toast.LENGTH_SHORT)
                                .show()
                            currentMode = Mode.NONE
                            return@loadStyleUri true
                        } else if (currentMode == Mode.MARK_END) {
                            RouteHelper.marcarFinal(this@mapa_padre, mapboxMap!!, point)
                            Toast.makeText(
                                this@mapa_padre,
                                "Destino marcado y ruta solicitada",
                                Toast.LENGTH_SHORT
                            ).show()
                            currentMode = Mode.NONE
                            return@loadStyleUri true
                        }
                        // Si no hay modo activo, no consumir el clic
                        return@loadStyleUri false
                    }
                }
                gestures.addOnMapClickListener(mapClickListener!!)
            } else {
                Toast.makeText(
                    this,
                    "Gestures plugin no disponible. Revisa la inicialización de MapView.",
                    Toast.LENGTH_LONG
                ).show()
            }


            //crear  un poligono y guardar como zona riesgo
            polygonMarcarZonaRiesgo = PolygonMarcarZonaRiesgo(mapView!!, mapboxMap!!)
            polygonMarcarZonaRiesgo!!.initPolygon(style!!) // pasar el estilo cargado

            //crear  un poligono y guardar como zona riesgo
            PolygonoZonaSegura = PoygonoZonaSegura(mapView!!, mapboxMap!!)
            PolygonoZonaSegura!!.initPolygon(style) // pasar el estilo cargado

            Imgbtn_MarcarZona!!.setOnClickListener(View.OnClickListener { v: View? ->
                drawingEnabled = !drawingEnabled
                val gesturesPlugin =
                    mapView!!.getPlugin<GesturesPlugin?>(Plugin.Companion.MAPBOX_GESTURES_PLUGIN_ID)
                if (drawingEnabled) {
                    polygonMarcarZonaRiesgo!!.startDrawing(gesturesPlugin!!) // activar clicks en el mapa
                    Imgbtn_MarcarZona!!.setImageResource(R.drawable.img)
                } else {
                    polygonMarcarZonaRiesgo!!.stopDrawing(gesturesPlugin!!) // desactivar clicks y guardar
                    Imgbtn_MarcarZona!!.setImageResource(R.drawable.marcar_zona)
                }
            })
            Imgbtn_ZonaSegura!!.setOnClickListener(View.OnClickListener { v: View? ->
                drawingEnabled = !drawingEnabled
                val gesturesPlugin =
                    mapView!!.getPlugin<GesturesPlugin?>(Plugin.Companion.MAPBOX_GESTURES_PLUGIN_ID)
                if (drawingEnabled) {
                    PolygonoZonaSegura!!.startDrawing(gesturesPlugin!!) // activar clicks en el mapa
                    Imgbtn_ZonaSegura!!.setImageResource(R.drawable.img)
                } else {
                    PolygonoZonaSegura!!.stopDrawing(gesturesPlugin!!) // desactivar clicks y guardar
                    Imgbtn_ZonaSegura!!.setImageResource(R.drawable.marcar_zona)
                }
            })
        })
    }


    companion object {
        private const val REQ_LOC = 1001
    }
}

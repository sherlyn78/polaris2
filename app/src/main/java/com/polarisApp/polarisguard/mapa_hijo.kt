package com.polarisApp.polarisguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.Style.OnStyleLoaded
import com.mapbox.maps.plugin.MapPlugin
import com.mapbox.maps.plugin.Plugin
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.Array
import kotlin.Boolean
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.arrayOf
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.toString

class mapa_hijo : AppCompatActivity() {
    private var locationPlugin: LocationComponentPlugin? = null
    private var hasCentered = false

    private var mapboxMap: MapboxMap? = null
    private var mapView: MapView? = null
    private var fabMain: FloatingActionButton? = null
    private var floatingMenu: LinearLayout? = null
    private var isMenuOpen = false


    private var lastSentLat = Double.NaN
    private var lastSentLng = Double.NaN
    private val mainHandler = Handler(Looper.getMainLooper())

    private var userId = "" // identifica al usuario
    private val Url_Endpoint =
        "https://greenyellow-locust-753084.hostingersite.com/Backendconection_php/ubicacion_mapa_hijo.php" // endpoint PHP (HTTPS)

    //variable global para acumular en el lote
    private var lote = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapa_hijo) // Conecta con activity_main.xml


        val prefs = getSharedPreferences("identificador-sesion", MODE_PRIVATE)
        userId = prefs.getString("idUser", "")!!

        val home = findViewById<ImageButton?>(R.id.imgbtn_homeH)
        val perfil = findViewById<ImageButton?>(R.id.imgbtn_perfil)
        val notificaciones = findViewById<ImageButton?>(R.id.imgbtn_notificacion)
        val MiUbicacion = findViewById<ImageButton?>(R.id.imgbtn_miUbicacion)

        val btnSATELLITE_STREETS = findViewById<ImageButton>(R.id.SATELLITE_STREETS_Hijo)
        val btnMAPBOX_STREETS = findViewById<ImageButton>(R.id.MAPBOX_STREETS_hijo)
        val btnDARK = findViewById<ImageButton>(R.id.DARK_hijo)
        val btnOUTDOORS = findViewById<ImageButton>(R.id.OUTDOORS_hijo)
        val btnEstandar = findViewById<ImageButton>(R.id.Estandar_hijo)


        mapView = findViewById<MapView>(R.id.mapViewHijo)
        mapboxMap = mapView!!.mapboxMap
        fabMain = findViewById<FloatingActionButton>(R.id.fab_mainH)
        floatingMenu = findViewById<LinearLayout>(R.id.floating_menuH)

        // Iniciar flujo: GPS -> permisos -> mapa -> ubicación
        checkGpsAndPermissions()

        //programar los botones que dirigiran a las siguientes pantallas

        //mostrar o ocultar el menu.
        fabMain!!.setOnClickListener(View.OnClickListener { v: View? -> tooggleMenu() })

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
    }


    //funcion para mostrar o ocultar el menu flotante
    private fun tooggleMenu() {
        if (isMenuOpen) {
            floatingMenu!!.setVisibility(View.GONE)
        } else {
            floatingMenu!!.setVisibility(View.VISIBLE)
        }
        isMenuOpen = !isMenuOpen
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
    // 3. Mostrar ubicación real del usuario
    // -------------------------
    private fun initMap() {
        mapboxMap!!.loadStyleUri(Style.MAPBOX_STREETS) { style ->

            locationPlugin =
                mapView!!.getPlugin(Plugin.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID)
                        as? LocationComponentPlugin

            locationPlugin?.let { plugin ->

                // Activar ubicación
                plugin.enabled = true

                plugin.addOnIndicatorPositionChangedListener { point ->
                    val lat = point.latitude()
                    val lng = point.longitude()

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

                    // Enviar la primera ubicación
                    if (lastSentLat.isNaN() || lastSentLng.isNaN()) {
                        agregarDatosAlLote(lat, lng)
                        lastSentLat = lat
                        lastSentLng = lng
                        return@addOnIndicatorPositionChangedListener
                    }

                    val distance =
                        haversineDistanceMeters(lastSentLat, lastSentLng, lat, lng)

                    if (distance >= DISTANCE_THRESHOLD_METERS) {
                        agregarDatosAlLote(lat, lng)
                        lastSentLat = lat
                        lastSentLng = lng
                    }
                }
            }
        }
    }


    // Haversine para distancia en metros
    private fun haversineDistanceMeters(
        lat1: kotlin.Double,
        lon1: kotlin.Double,
        lat2: kotlin.Double,
        lon2: kotlin.Double
    ): kotlin.Double {
        val R = 6371000 // radio Tierra en metros
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    //metodo para acumular parametros
    private fun agregarDatosAlLote(latitud: Double, longitud: Double) {
        try {
            val ubicacion = JSONObject()
            ubicacion.put("latitud", latitud)
            ubicacion.put("longitud", longitud)
            ubicacion.put("id_user", userId)
            lote.put(ubicacion)
        } catch (e: JSONException) {
            e.printStackTrace()

        }

        //si ya hay 10 registros en el lote, enviar el lote a la base de datos.
        if (lote.length() == 10) {
            enviarLoteVolley(lote)
            lote = JSONArray() //reiniciar para el siguiente lote
        }
    }


    //enviar el lote a la base de datos mediante voley
    private fun enviarLoteVolley(lote: JSONArray?) {
        val request = JsonArrayRequest(
            Request.Method.POST,
            Url_Endpoint,
            lote,
            Response.Listener { response: JSONArray? ->
                Toast.makeText(
                    getApplicationContext(),
                    "Respuesta: " + response.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }, Response.ErrorListener { volleyError: VolleyError? ->
                volleyError!!.printStackTrace()
            }
        )

        val queue = Volley.newRequestQueue(this)
        queue.add<JSONArray?>(request)
    }

    companion object {
        private const val REQ_LOC = 1001

        private const val DISTANCE_THRESHOLD_METERS = 20.0 // umbral en metros
    }
}

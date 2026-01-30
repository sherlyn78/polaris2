package com.polarisApp.polarisguard

import android.content.Context
import android.graphics.Color
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.gestures.GesturesPlugin
import com.mapbox.maps.plugin.gestures.OnMapClickListener

class PoygonoZonaSegura (
    private val mapView: MapView,
    private val mapboxMap: MapboxMap
) {

    private val SOURCE_ID = "polygon-source-zona-segura"
    private val LAYER_ID = "polygon-layer-zona-segura"

    private val polygonPoints = mutableListOf<Point>()

    // Lista de todos los polígonos dibujados y guardados
    private val allPolygons = mutableListOf<List<Point>>()
    private val prefs by lazy {
        mapView.context.getSharedPreferences("POLYGONS_DB_seg", Context.MODE_PRIVATE)
    }
    private val gson = Gson()

    private val clickListener = OnMapClickListener { point ->
        polygonPoints.add(point)
        updatePolygon()
        true
    }

    fun initPolygon(style: Style) {
        style.addSource(
            geoJsonSource(SOURCE_ID) {
                geometry(Polygon.fromLngLats(listOf(listOf())))
            }
        )

        style.addLayer(
            fillLayer(LAYER_ID, SOURCE_ID) {
                fillColor(Color.parseColor("#6600FF00"))
                fillOutlineColor(Color.GREEN)
            }
        )

        // Si existe un polígono guardado, cargarlo
        loadPolygonFromPreferences()
    }

    fun startDrawing(gestures: GesturesPlugin) {
        polygonPoints.clear()
        gestures.addOnMapClickListener(clickListener)
    }

    fun stopDrawing(gestures: GesturesPlugin) {
        gestures.removeOnMapClickListener(clickListener)

        // Guardar automáticamente cuando se termina el dibujo
        if (polygonPoints.size >= 3) {
            savePolygonToPreferences()
        }
    }

    private fun updatePolygon() {
        mapboxMap.getStyle { style ->

            val source = style.getSourceAs<GeoJsonSource>(SOURCE_ID)

            // Cerrar polígono
            val closed = if (polygonPoints.size >= 3) {
                polygonPoints + polygonPoints.first()
            } else {
                listOf()
            }

            source?.geometry(Polygon.fromLngLats(listOf(closed)))
        }
    }

    // =============================
    //     GUARDAR POLÍGONO
    // =============================
    private fun savePolygonToPreferences() {
        val json = gson.toJson(polygonPoints)
        prefs.edit().putString("polygon_saved", json).apply()
    }

    // =============================
    //     CARGAR POLÍGONO
    // =============================
    private fun loadPolygonFromPreferences() {
        val json = prefs.getString("polygon_saved", null) ?: return

        val type = object : TypeToken<List<Point>>() {}.type
        val savedPoints: List<Point> = gson.fromJson(json, type)

        polygonPoints.clear()
        polygonPoints.addAll(savedPoints)
        updatePolygon()
    }

}
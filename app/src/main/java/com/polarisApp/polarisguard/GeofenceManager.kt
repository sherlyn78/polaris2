package com.polarisApp.polarisguard

import android.graphics.Color
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.FillLayer
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class GeofenceManager (
    private val mapView: MapView,
    private val mapboxMap: MapboxMap
) {

    private val SOURCE_ID = "circle-geofence-source"
    private val LAYER_ID = "circle-geofence-layer"

    private var centerPoint: Point? = null
    private var radiusMeters: Double = 0.0

    fun createGeofence(center: Point, initialRadiusMeters: Double) {
        centerPoint = center
        radiusMeters = initialRadiusMeters

        mapboxMap.getStyle { style ->
            // Crear fuente
            style.addSource(
                geoJsonSource(SOURCE_ID) {
                    geometry(createCircle(centerPoint!!, radiusMeters))
                }
            )

            // Crear capa
            style.addLayer(
                fillLayer(LAYER_ID, SOURCE_ID) {
                    fillColor(Color.parseColor("#66FF0000"))
                    fillOutlineColor(Color.RED)
                }
            )
        }
    }

    // Actualiza el radio mientras arrastras el handle
    fun updateRadius(newRadiusMeters: Double) {
        centerPoint?.let { center ->
            radiusMeters = newRadiusMeters
            mapboxMap.getStyle { style ->
                val source = style.getSourceAs<GeoJsonSource>(SOURCE_ID)
                source?.geometry(createCircle(center, radiusMeters))
            }
        }
    }

    // Crea un círculo aproximado como polígono
    private fun createCircle(center: Point, radiusMeters: Double, points: Int = 64): Polygon {
        val coordinates = mutableListOf<Point>()
        val earthRadius = 6371000.0 // metros
        val lat = Math.toRadians(center.latitude())
        val lon = Math.toRadians(center.longitude())
        val d = radiusMeters / earthRadius

        for (i in 0..points) {
            val theta = 2.0 * Math.PI * i / points
            val latPoint = Math.asin(sin(lat) * cos(d) + cos(lat) * sin(d) * cos(theta))
            val lonPoint = lon + atan2(sin(theta) * sin(d) * cos(lat), cos(d) - sin(lat) * sin(latPoint))
            coordinates.add(Point.fromLngLat(Math.toDegrees(lonPoint), Math.toDegrees(latPoint)))
        }

        return Polygon.fromLngLats(listOf(coordinates))
    }
}


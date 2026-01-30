package com.polarisApp.polarisguard

import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.gestures

object MarkerHelper {

    private var pointAnnotationManager: PointAnnotationManager? = null

    fun addMarkerAtPoint(mapView: MapView, point: Point) {
        if (pointAnnotationManager == null) {
            pointAnnotationManager = mapView.annotations.createPointAnnotationManager(null)
        }

        val annotationOptions = PointAnnotationOptions()
            .withPoint(point)
        // .withIconImage(bitmapFromDrawable(context, R.drawable.marker_icon)) // opcional

        pointAnnotationManager?.create(annotationOptions)
    }

    // Listener para el mapa
    fun setupMapClickListener(mapView: MapView) {
        mapView.gestures.addOnMapClickListener { point ->
            addMarkerAtPoint(mapView, point)
            true
        }
    }
}

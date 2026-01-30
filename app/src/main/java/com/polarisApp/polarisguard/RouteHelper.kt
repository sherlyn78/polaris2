package com.polarisApp.polarisguard

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import com.mapbox.geojson.*
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.getSource
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

object RouteHelper {

    private var puntoInicio: Point? = null
    private var puntoDestino: Point? = null

    private const val SOURCE_ID = "route-source"
    private const val LAYER_ID = "route-layer"

    private const val TOKEN =
        "pk.eyJ1Ijoic2hlcmx5bjU2IiwiYSI6ImNtaGkxYTA2ZDB3czEya244YjRocWhwYXgifQ.ohRAdZncx0fgA7toqLc-JQ"

    fun marcarInicio(ctx: Context, map: MapboxMap, p: Point) {
        puntoInicio = p
        Toast.makeText(ctx, "Punto de inicio marcado", Toast.LENGTH_SHORT).show()
    }

    fun marcarFinal(ctx: Context, map: MapboxMap, p: Point) {
        if (puntoInicio == null) {
            Toast.makeText(ctx, "Primero marca el inicio", Toast.LENGTH_SHORT).show()
            return
        }
        puntoDestino = p
        Toast.makeText(ctx, "Destino marcado", Toast.LENGTH_SHORT).show()
        solicitarRuta(ctx, map)
    }

    private fun solicitarRuta(context: Context, map: MapboxMap) {
        val start = puntoInicio ?: return
        val end = puntoDestino ?: return

        thread {
            try {
                val url = """
                    https://api.mapbox.com/directions/v5/mapbox/driving/
                    ${start.longitude()},${start.latitude()};
                    ${end.longitude()},${end.latitude()}
                    ?geometries=polyline6&overview=full&access_token=$TOKEN
                """.trimIndent().replace("\n", "")

                val response = OkHttpClient().newCall(
                    Request.Builder().url(url).build()
                ).execute()

                val json = response.body?.string() ?: return@thread
                Log.d("RouteHelper", json)

                val geometry = json.substringAfter("\"geometry\":\"").substringBefore("\"")

                // ✔ Método oficial para decodificar polyline en Mapbox Maps v11
                val lineString = LineString.fromPolyline(geometry, 6)

                val featureCollection = FeatureCollection.fromFeatures(
                    arrayOf(Feature.fromGeometry(lineString))
                )

                map.getStyle { style ->

                    val existing = style.getSource(SOURCE_ID)
                    if (existing == null) {

                        // ✔ IMPORTANTE: usar toJson()
                        style.addSource(
                            GeoJsonSource.Builder(SOURCE_ID)
                                .data(featureCollection.toJson())
                                .build()
                        )

                        style.addLayer(
                            LineLayer(LAYER_ID, SOURCE_ID)
                                .lineWidth(6.0)
                                .lineCap(LineCap.ROUND)
                                .lineJoin(LineJoin.ROUND)
                                .lineColor(Color.RED)
                        )

                    } else {
                        // ✔ Actualización correcta del GeoJsonSource con JSON
                        (existing as GeoJsonSource).data(featureCollection.toJson())
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

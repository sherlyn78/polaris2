package com.polarisApp.polarisguard

import android.content.Context
import com.mapbox.geojson.Point

class GeocodingInverso  {

//    private final SearchEngine searchEngine;
//
//    // Constructor: inicializa el SearchEngine con tu token
//    public GeoUtils(@NonNull Context context, @NonNull String mapboxToken) {
//        searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
//            ApiType.SEARCH_BOX,
//            new SearchEngineSettings.Builder()
//                .accessToken(mapboxToken)
//                .build()
//        );
//    }
//
//    // Interfaz para devolver el resultado
//    public interface ReverseGeocodeCallback {
//        void onResult(@Nullable String address);
//        void onError(@NonNull Exception e);
//    }
//
//    // Método que recibe lat/lon y devuelve la dirección vía callback
//    public void getAddressFromCoordinates(double latitude, double longitude, @NonNull ReverseGeocodeCallback callback) {
//
//        ReverseGeoOptions options = new ReverseGeoOptions.Builder(Point.fromLngLat(longitude, latitude))
//            .limit(1)
//            .build();
//
//        searchEngine.search(options, new SearchCallback() {
//            @Override
//            public void onResults(@NonNull List<SearchResult> results, @NonNull ResponseInfo responseInfo) {
//                if (!results.isEmpty()) {
//                    String address = results.get(0).getFormattedAddress();
//                    callback.onResult(address);
//                } else {
//                    callback.onResult(null); // No hay resultados
//                }
//            }
//
//            @Override
//            public void onError(@NonNull Exception e) {
//                callback.onError(e);
//            }
//        });
//    }

}
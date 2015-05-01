package de.fhkoeln.ngn.data;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HeatMapDataProvider {

    public static void updateHeatMapData(LatLngBounds bounds, final Callback<List<WeightedLatLng>> callback) {

        Callback<List<List<Double>>> cb = new Callback<List<List<Double>>>() {
            @Override
            public void success(List<List<Double>> lists, Response response) {
                callback.success(convertPointList(lists), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://ngn.herokuapp.com/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        HeatMapDataService heatMapDataService = restAdapter.create(HeatMapDataService.class);
        heatMapDataService.getSignalHeatmapPoints(
                bounds.northeast.latitude, bounds.northeast.longitude,
                bounds.southwest.latitude, bounds.southwest.longitude,
                cb);
    }

    private static ArrayList<WeightedLatLng> convertPointList(List<List<Double>> lists) {
        ArrayList<WeightedLatLng> latLngArrayList = new ArrayList<>();
        for (List<Double> point : lists) {
            LatLng latLng = new LatLng(point.get(0), point.get(1));
            latLngArrayList.add(new WeightedLatLng(latLng, point.get(2)));
        }
        return latLngArrayList;
    }
}

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

    private static RestAdapter restAdapter;

    public static void getHeatMapGSMData(LatLngBounds bounds, final Callback<List<WeightedLatLng>> callback) {

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

        HeatMapDataService heatMapDataService = getRestAdapter().create(HeatMapDataService.class);
        heatMapDataService.getSignalHeatmapPoints(
                bounds.northeast.latitude, bounds.northeast.longitude,
                bounds.southwest.latitude, bounds.southwest.longitude,
                cb);
    }

    public static void getHeatMapWifiData(LatLngBounds bounds, final Callback<List<WeightedLatLng>> callback) {

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

        HeatMapDataService heatMapDataService = getRestAdapter().create(HeatMapDataService.class);
        heatMapDataService.getWifiHeatmapPoints(
                bounds.northeast.latitude, bounds.northeast.longitude,
                bounds.southwest.latitude, bounds.southwest.longitude,
                cb);
    }

    public static void getMeasurements(LatLngBounds bounds, boolean edgeOnly, final Callback<List<Measurement>> callback) {
        HeatMapDataService heatMapDataService = getRestAdapter().create(HeatMapDataService.class);
        heatMapDataService.getMeasurements(
                bounds.northeast.latitude, bounds.northeast.longitude,
                bounds.southwest.latitude, bounds.southwest.longitude,
                edgeOnly,
                callback);
    }

    private static ArrayList<WeightedLatLng> convertPointList(List<List<Double>> lists) {
        ArrayList<WeightedLatLng> latLngArrayList = new ArrayList<>();
        for (List<Double> point : lists) {
            LatLng latLng = new LatLng(point.get(0), point.get(1));
            latLngArrayList.add(new WeightedLatLng(latLng, point.get(2)));
        }
        return latLngArrayList;
    }

    public static RestAdapter getRestAdapter() {
        if (restAdapter == null) {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://ngn.herokuapp.com/")
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .build();
        }
        return restAdapter;
    }

    public static void saveMeasurement(Measurement measurement) {
        restAdapter.create(HeatMapDataService.class).saveNewMeasurement(measurement, new Callback<Measurement>() {
            @Override
            public void success(Measurement measurement, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

}

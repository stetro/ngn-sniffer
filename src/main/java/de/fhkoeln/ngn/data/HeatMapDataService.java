package de.fhkoeln.ngn.data;


import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface HeatMapDataService {
    @GET("/measurement/signal")
    void getSignalHeatmapPoints(
            @Query("nelat") double nelat,
            @Query("nelng") double nelng,
            @Query("swlat") double swlat,
            @Query("swlng") double swlng,
            Callback<List<List<Double>>> cb);

    @POST("/measurement")
    void saveNewMeasurement(@Body Measurement measurement, Callback<Measurement> cb);


    @GET("/measurement")
    void getMeasurements(
            @Query("nelat") double nelat,
            @Query("nelng") double nelng,
            @Query("swlat") double swlat,
            @Query("swlng") double swlng,
            @Query("edgeOnly") boolean edgeOnly, Callback<List<Measurement>> cb);
}

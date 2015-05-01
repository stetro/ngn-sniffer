package de.fhkoeln.ngn.data;


import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface HeatMapDataService {
    @GET("/measurement/signal")
    void getSignalHeatmapPoints(
            @Query("nelat") double nelat,
            @Query("nelng") double nelng,
            @Query("swlat") double swlat,
            @Query("swlng") double swlng,
            Callback<List<List<Double>>> cb);


}

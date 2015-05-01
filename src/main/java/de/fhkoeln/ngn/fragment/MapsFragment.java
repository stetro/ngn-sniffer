package de.fhkoeln.ngn.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.data.HeatMapDataProvider;
import de.fhkoeln.ngn.service.event.LocationChangedEvent;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MapsFragment extends Fragment implements GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraChangeListener {
    private MapView mapView;
    private GoogleMap map;
    private HeatmapTileProvider heatMapTileProvider;
    private Collection<WeightedLatLng> heatMapPoints = new HashSet<>();
    private TileOverlay heatMapOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.maps_fragment, container, false);
        initializeMapView(savedInstanceState, v);
        return v;
    }

    private void initializeMapView(Bundle savedInstanceState, View v) {
        initializeHeatMapTileProvider();
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setTiltGesturesEnabled(false);
                map.setMyLocationEnabled(true);
                map.setOnCameraChangeListener(MapsFragment.this);
                map.setOnMyLocationChangeListener(MapsFragment.this);
                TileOverlayOptions options = new TileOverlayOptions().tileProvider(heatMapTileProvider);
                options.visible(true);
                heatMapOverlay = map.addTileOverlay(options);
                updateHeatMapData();
                MapsInitializer.initialize(getActivity());
            }
        });
    }

    private void initializeHeatMapTileProvider() {
        int[] colors = {Color.rgb(102, 225, 0), Color.rgb(255, 0, 0)};
        float[] startPoints = {0.1f, 1f};
        Gradient gradient = new Gradient(colors, startPoints);

        heatMapPoints.add(new WeightedLatLng(new LatLng(50.962238 + Math.random() - Math.random(), 7.000172 + Math.random() - Math.random()), 1.0));
        heatMapTileProvider = new HeatmapTileProvider.Builder()
                .weightedData(heatMapPoints)
                .radius(30)
                .opacity(.7)
                .gradient(gradient)
                .build();
    }

    private void updateHeatMapData() {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        HeatMapDataProvider.getHeatMapData(bounds, new Callback<List<WeightedLatLng>>() {

            @Override
            public void success(List<WeightedLatLng> weightedLatLngList, Response response) {
                if (weightedLatLngList.size() > 0) {
                    heatMapTileProvider.setWeightedData(weightedLatLngList);
                    heatMapOverlay.clearTileCache();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMyLocationChange(Location location) {
        EventBus.getDefault().post(new LocationChangedEvent(location));
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        heatMapTileProvider.setRadius((int) (cameraPosition.zoom * 3));
        updateHeatMapData();
    }
}

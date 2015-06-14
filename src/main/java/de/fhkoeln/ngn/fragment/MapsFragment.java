package de.fhkoeln.ngn.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.Dialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.data.HeatMapDataProvider;
import de.fhkoeln.ngn.data.Measurement;
import de.fhkoeln.ngn.service.event.LocationChangedEvent;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MapsFragment extends Fragment implements GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraChangeListener {
    private MapView mapView;
    private GoogleMap map;
    private ButtonRectangle gsmWifiButton;
    private HeatmapTileProvider heatMapTileProvider;
    private Collection<WeightedLatLng> heatMapPoints = new HashSet<>();
    private TileOverlay heatMapOverlay;
    private ArrayList<Measurement> measurementPoints;
    private ArrayList<Circle> circleArrayList;
    private boolean showGSM = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.maps_fragment, container, false);
        initializeMapView(savedInstanceState, v);
        circleArrayList = new ArrayList<>();
        gsmWifiButton = (ButtonRectangle) v.findViewById(R.id.gsm_wifi_button);
        gsmWifiButton.setOnClickListener(gsmWifiListener);

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
                map.setOnMapClickListener(onMapClickListener);
                initializeHeatMap();
                updateHeatMapData(5);
                MapsInitializer.initialize(getActivity());
            }
        });
    }

    private void initializeHeatMapTileProvider() {
        int[] colors = {Color.rgb(255, 0, 0), Color.rgb(102, 225, 0)};
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

    private void updateHeatMapData(int r) {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

        if(showGSM)
        {
            HeatMapDataProvider.getHeatMapData(bounds, new Callback<List<WeightedLatLng>>() {

                @Override
                public void success(List<WeightedLatLng> weightedLatLngList, Response response) {
                    if (weightedLatLngList.size() > 0) {
                        initializeHeatMap();
                        heatMapTileProvider.setWeightedData(weightedLatLngList);
                        heatMapOverlay.clearTileCache();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
        else
        {
            //TODO: Add method to get and show WiFi data
            //heatMapOverlay.remove();
            //initializeHeatMap();
        }

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
        //TODO: Save measurement every 50 meters
        //EventBus.getDefault().post(new LocationChangedEvent(location));
        //HeatMapDataProvider.saveMeasurement(SmallDetailFragment.getMeasurement());
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        int radius = (int) (cameraPosition.zoom * 3);
        heatMapTileProvider.setRadius(radius);
        updateHeatMapData(radius);
        drawMeasurementPoints(radius);
    }

    public void drawMeasurementPoints(int radius)
    {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        HeatMapDataProvider.getMeasurements(bounds, false, new Callback<List<Measurement>>() {

            @Override
            public void success(List<Measurement> measurements, Response response) {
                measurementPoints = new ArrayList<>(measurements);
                Log.d("MapsFragment", "drawMeasurementPoints: "+measurements.size());
                if(measurements.size() > 0)
                {
                    for(Measurement m : measurements)
                    {
                        Log.d("MapsFragment", "drawMeasurementPoints: Lat="+m.getLat() +
                                " Lng="+m.getLng() + " Signal DBm: " + m.getSignalDBm() +
                                " Type: "+m.getType()+ " APs: "+m.getWifiAPs());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        //TODO: Clear all circles and redraw with new radius; Circles must be saved in a List
        if(measurementPoints != null)
        {
            for(Measurement measurement : measurementPoints)
            {
                CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(measurement.getLat(), measurement.getLng()))
                        .radius(5);
                Log.d("MapsFragment", "drawMeasurementPoints: Lat="+measurement.getLat() + " Lng="+measurement.getLng());

                Circle circle = map.addCircle(circleOptions);
                circle.setFillColor(getResources().getColor(R.color.indigo_500));
                circle.setStrokeWidth(1);
                circleArrayList.add(circle);
            }
        }
    }

    public void initializeHeatMap()
    {
        TileOverlayOptions options = new TileOverlayOptions().tileProvider(heatMapTileProvider);
        options.visible(true);
        heatMapOverlay = map.addTileOverlay(options);
    }

    public GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener()
    {
        @Override
        public void onMapClick(LatLng position)
        {
            Log.d("MapsFragment", "OnMapClickListener: Lat=" + position.latitude + " Lng=" + position.longitude);
            LatLng southWest = new LatLng(position.latitude-0.0005, position.longitude-0.0005);
            LatLng northEast = new LatLng(position.latitude+0.0005, position.longitude+0.0005);
            LatLngBounds llBounds = new LatLngBounds(southWest, northEast);
            HeatMapDataProvider.getMeasurements(llBounds, false, new Callback<List<Measurement>>() {

            @Override
            public void success(List<Measurement> measurements, Response response) {
                measurementPoints = new ArrayList<>(measurements);
                Log.d("MapsFragment", "onMapClick: "+measurements.size());
                if(measurements.size() > 0)
                {
                    String info = "Lat: "+ measurements.get(0).getLat()+
                            "\nLng: "+measurements.get(0).getLng()+
                            "\nType: "+measurements.get(0).getType()+
                            "\nSignal dBm: "+((measurements.get(0).getSignalDBm() <= 0) ? -113 : measurements.get(0).getSignalDBm() * 2 - 113)+
                            "\nWiFi APs: "+measurements.get(0).getWifiAPs();
                    new Dialog(getActivity(), "Details", info).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
            /*for(Circle circle : circleArrayList)
            {
                LatLng center = circle.getCenter();
                double radius = circle.getRadius();
                float[] distance = new float[1];
                Location.distanceBetween(position.latitude, position.longitude, center.latitude, center.longitude, distance);
                if(distance[0] < radius)
                {
                    // TODO: Open info dialog
                    new Dialog(getActivity(), "Measurement point", "message").show();
                }
            }*/
        }
    };

    public View.OnClickListener gsmWifiListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(showGSM)
            {
                showGSM = false;
                gsmWifiButton.setText(getResources().getString(R.string.toggle_gsm));
                Toast.makeText(getActivity(), "Showing WiFi Heatmap", Toast.LENGTH_LONG).show();
            }
            else
            {
                showGSM = true;
                gsmWifiButton.setText(getResources().getString(R.string.toggle_wifi));
                Toast.makeText(getActivity(), "Showing GSM Heatmap", Toast.LENGTH_LONG).show();

            }
            heatMapOverlay.remove();
            updateHeatMapData(5);
        }
    };
}

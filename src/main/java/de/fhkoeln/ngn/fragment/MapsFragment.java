package de.fhkoeln.ngn.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.zip.Inflater;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.service.event.LocationFoundEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by ivan on 23.04.15.
 */
public class MapsFragment extends Fragment
{
    private MapView mapView;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.maps_fragment, container, false);

        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        return v;
    }

    public void onEvent(LocationFoundEvent e)
    {
        Log.d("MapsFragment", "Location was found and will be set.");
        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(e.getLocation().getLatitude(), e.getLocation().getLongitude()), 13);
        map.animateCamera(cameraUpdate);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(e.getLocation().getLatitude(), e.getLocation().getLongitude()))
                .zoom(15) // Sets the zoom
                //.bearing(90) // Sets orientatin of the camera to east
                //.tilt(40) // Sets the tilt of the camera to 40 degrees
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onResume()
    {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}

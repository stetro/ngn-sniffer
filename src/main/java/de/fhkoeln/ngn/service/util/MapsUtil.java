package de.fhkoeln.ngn.service.util;

import android.graphics.Color;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.fhkoeln.ngn.R;

/**
 * Created by Ivan on 30.04.2015.
 */
public class MapsUtil
{
    public static void addHeatMap(GoogleMap map, LatLng latLng)
    {
        List<LatLng> list = new ArrayList<>();

        // Get the data: latitude/longitude positions of police stations.
        /*try
        {
            list = readItems(R.raw.police_stations);
        }
        catch (JSONException e)
        {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }*/

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        list.add(latLng);
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        mProvider.setRadius(50);
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };
        mProvider.setGradient(new Gradient(colors, startPoints));
        // Add a tile overlay to the map, using the heat map tile provider.
        map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private static ArrayList<LatLng> readItems() throws JSONException
    {
        ArrayList<LatLng> list = new ArrayList<>();

        list.add(new LatLng(45.1, 40.2));

        return list;
    }
}

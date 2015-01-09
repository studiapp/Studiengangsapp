package de.hfu.mos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.utils.WebImageCache;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.bonuspack.overlays.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class POIFragment extends Activity implements View.OnClickListener, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
    
	private MapView mapView;
    private MyLocationOverlay myLocationOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_fragment);
        
        // create mapView
        mapView = new MapView(this, 256, new DefaultResourceProxyImpl(this));
        mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(19);
 
 
        
        // create my location overlay  
        myLocationOverlay = new MyLocationOverlay(getApplicationContext(), mapView);
        mapView.getOverlays().add(myLocationOverlay);

        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        container.removeAllViews();
        container.addView(mapView);
  
        
       /*
		RoadManager roadManager = new OSRMRoadManager();
        NominatimPOIProvider poiProvider = new NominatimPOIProvider();
		ArrayList<POI> pois = poiProvider.getPOICloseTo(startPoint, "cinema", 50, 0.1);
        FolderOverlay poiMarkers = new FolderOverlay(this);
        //mapView.getOverlays().add(poiMarkers);
        Drawable poiIcon = getResources().getDrawable(R.drawable.ic_launcher);
        for (POI poi:pois){
                Marker poiMarker = new Marker(mapView);
                poiMarker.setTitle(poi.mType);
                poiMarker.setSnippet(poi.mDescription);
                poiMarker.setPosition(poi.mLocation);
                poiMarker.setIcon(poiIcon);
                if (poi.mThumbnail != null){                
						Marker poiItem;
						//poiItem .setImage(new BitmapDrawable(poi.mThumbnail));
                }
                poiMarkers.add(poiMarker);
        } https://code.google.com/p/osmbonuspack/wiki/Tutorial_0
        */
                    
    }

    @Override
    protected void onResume() {
        super.onResume();
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myLocationOverlay.disableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(POIFragment.this,R.string.has_location,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	
	public boolean onItemLongPress(int arg0, OverlayItem arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onItemSingleTapUp(int arg0, OverlayItem arg1) {
		// TODO Auto-generated method stub
		return false;
	}



}
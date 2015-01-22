package de.hfu.mos.campus.map;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import de.hfu.mos.R;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback{

	public GoogleMap map;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_maps);


        
        
        
        MapFragment mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
       
		
	}

    
	@Override
	public void onMapReady(GoogleMap map) {
		// TODO Auto-generated method stub
		//map.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.setMyLocationEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		// A Bau
		map.addMarker(new MarkerOptions()
        .position(new LatLng(48.05136247223902, 8.207470066845417))
        .title("A Bau - Eingang"));
		// B Bau
		map.addMarker(new MarkerOptions()
        .position(new LatLng(48.051798993216857, 8.20848561823358))
        .title("B Bau"));
		// C Bau
		map.addMarker(new MarkerOptions()
        .position(new LatLng(48.05101239549813, 8.208851739764214))
        .title("C Bau - Eingang"));
		// Mensa
		map.addMarker(new MarkerOptions()
        .position(new LatLng(48.05086492327769, 8.20834781974554))
        .title("Mensa"));
		// Bibliothek
		map.addMarker(new MarkerOptions()
        .position(new LatLng(48.051597797775929, 8.207593448460102))
        .title("Bibliothek"));
		// Uhrenmuseum
		map.addMarker(new MarkerOptions()
        .position(new LatLng(48.051188330649374, 8.207708783447742))
        .title("Uhrenmuseum"));
		// Aula
		map.addMarker(new MarkerOptions()
        .position(new LatLng(48.052057463889504, 8.207694701850414))
        .title("Aula"));
		// I Bau
		map.addMarker(new MarkerOptions()
        .position(new LatLng(48.04993861774366, 8.210676312446594))
        .title("I Bau"));
		// I Bau Parkplatz
		map.addMarker(new MarkerOptions()
		.position(new LatLng(48.05048293351, 8.2102428004145562))
        .title("Parkplatz"));
		
		
	}
	
	public void setNoPadding(View view){
		
		map.setPadding(0, 0, 0, 0);
		
	}
	
	public void setPadding(View view){
		
		map.setPadding(0, 0, 300, 0);
		
	}



}
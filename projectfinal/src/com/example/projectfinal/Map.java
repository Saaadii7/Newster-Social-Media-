package com.example.projectfinal;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;

public class Map extends Activity{
	private MapView mapView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		mapView = (MapView) findViewById(R.id.map);
		MapsInitializer.initialize(this);
		mapView.getMapAsync(new OnMapReadyCallback() {			
			@Override
			public void onMapReady(GoogleMap map) {
				// TODO Auto-generated method stub
				LatLng sydney = new LatLng(-33.867, 151.206);

		        map.setMyLocationEnabled(true);
		        map.getUiSettings().setZoomControlsEnabled(true);
		        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

		        map.addMarker(new MarkerOptions()
		                .title("Sydney")
		                .snippet("The most populous city in Australia.")
		                .position(sydney));
			}
		});
		
		mapView.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
}

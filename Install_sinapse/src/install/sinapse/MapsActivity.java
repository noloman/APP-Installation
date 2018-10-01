package install.sinapse;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class MapsActivity extends FragmentActivity {

	// Google Map
	private GoogleMap mapa;
	MarkerOptions mp = new MarkerOptions();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps);
		GlobalClass.coordmapa = true;

		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		
		mapa.setMyLocationEnabled(true);
		
		//mostrarMarcador(GlobalClass.latitud, GlobalClass.longitud);

		mapa.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				mapa.clear();
				Projection proj = mapa.getProjection();
				Point coord = proj.toScreenLocation(point);

				GlobalClass.latitud = point.latitude;
				GlobalClass.longitud = point.longitude; 
				mostrarMarcador(GlobalClass.latitud, GlobalClass.longitud);
				
				Toast.makeText(		
						MapsActivity.this,
						R.string.Click + "\n" + "Lat: " + point.latitude + "\n" + "Lng: "
								+ point.longitude + "\n" + "X: " + coord.x
								+ " - Y: " + coord.y, Toast.LENGTH_SHORT)
						.show();
			}
		});

		mapa.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				Projection proj = mapa.getProjection();
				Point coord = proj.toScreenLocation(point);

				Toast.makeText(
						MapsActivity.this,
						R.string.ClickLargo + "\n" + "Lat: " + point.latitude + "\n"
								+ "Lng: " + point.longitude + "\n" + "X: "
								+ coord.x + " - Y: " + coord.y,
						Toast.LENGTH_SHORT).show();
			}
		});

		mapa.setOnCameraChangeListener(new OnCameraChangeListener() {
			public void onCameraChange(CameraPosition position) {

			}
		});

		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {
				Toast.makeText(MapsActivity.this,
						R.string.Marcadorpulsado + "\n" + marker.getPosition(),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});

	}
	


	
	private void mostrarMarcador(double lat, double lng) {
		mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
				.title("Luminaria"));
	}
	
	


	@Override
	protected void onResume() {
		super.onResume();

	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		 Intent intent = new Intent(); intent.setClass(MapsActivity.this,
				 Gps_activity.class); 
		 startActivity(intent); 
		 finish();
	}



}
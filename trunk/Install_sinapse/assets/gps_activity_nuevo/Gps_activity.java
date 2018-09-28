package install.sinapse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import install.sinapse.GlobalClass;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// SE COMENTA GETADRESS Y alertaGPS para que al no tener red el movil no se lie

public class Gps_activity<GPSActivity> extends Activity implements
		LocationListener {
	private LocationManager locationManager;
	private String provider;
	double currentLatitude;
	double currentLongitude;
	Location currentLocation;
	TextView locationText;
	ImageButton scanButton;
	TextView myaddress;
	TextView TextoECoBalast;
	TextView tipoText;
	TextView RefRadio;
	ImageView imagenproducto;
	ImageButton MapsButton;
	ImageButton GPSButton;
	boolean manual = false;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.gps_layout);
		locationText = (TextView) findViewById(R.id.locationText);
		myaddress = (TextView) findViewById(R.id.myaddress);
		TextoECoBalast = (TextView) findViewById(R.id.TextoECoBalast);
		imagenproducto = (ImageView) findViewById(R.id.imagenproducto);
		tipoText = (TextView) findViewById(R.id.tipoText);
		GPSButton = (ImageButton) findViewById(R.id.GPSButton);
		RefRadio = (TextView) findViewById(R.id.RefRadio);
		RefRadio.setText(GlobalClass.radio);
		MapsButton = (ImageButton) findViewById(R.id.mapButton);
		MapsButton.setEnabled(true);
		scanButton = (ImageButton) findViewById(R.id.scanButton);
		scanButton.setEnabled(false);
		// GlobalClass.global_buscaEC = true;

		manual = false;
		
		if (GlobalClass.global_buscaEC == false) {
					
			TextoECoBalast.setText("Escanea el código de Balasto.");
			tipoText.setText("BALASTO");
			imagenproducto.setImageResource(R.drawable.balasto);
			if (GlobalClass.marcaBalast.compareTo("Sinapse") == 0) {

			} else {
				// Aqui le digo que lo siguiente que tiene que buscar es un EC
				GlobalClass.global_buscaEC = true;
				Intent intent3 = new Intent();
				intent3.setClass(Gps_activity.this, balastcod_activity.class);
				startActivity(intent3);
				finish();
			}

		} else {
			TextoECoBalast.setText("Escanea el código de EasyControl.");
			tipoText.setText("EASYCONTROL");
			imagenproducto.setImageResource(R.drawable.easycontrol);
		}

		// TextoECoBalast.setText("Escanea código de EasyControl");

		((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(false);

		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the location provider -> use
		// default
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_COARSE);
		c.setAltitudeRequired(false);
		c.setBearingRequired(false);
		c.setSpeedRequired(false);
		c.setCostAllowed(true);
		c.setPowerRequirement(Criteria.POWER_MEDIUM);
		
		provider = locationManager.getBestProvider(c, false);

		// Now that we have a location manager, its a simple process to check to
		// see if the GPS is enabled or not:
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			createGpsDisabledAlert();
		}
// Intento de actualizar posicion
//		PendingIntent singleUpatePI = PendingIntent.getBroadcast(
//				getBaseContext(), 0, getIntent(),
//				PendingIntent.FLAG_UPDATE_CURRENT);
//		locationManager.requestSingleUpdate(c, singleUpatePI);
		
		Location location = locationManager.getLastKnownLocation(provider);
		locationManager.requestSingleUpdate(provider, this, getMainLooper());

		//Si tengo la misma posicion que antes o no tengo posicion directamente, habilito la posibilidad
		//de añadir el punto con el mapa
		if (GlobalClass.latitud == 0.00000000) {
			 alertaGPS();
			 ((ImageButton) this.findViewById(R.id.mapButton)).setEnabled(true);
		} else {
			((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(true);
			((ImageButton) this.findViewById(R.id.mapButton)).setEnabled(true);
		}
	

		// Initialize the location fields
		if (location != null && GlobalClass.coordmapa == false) {
			Toast.makeText(this,
					"Provider " + provider + " has been selected.",
					Toast.LENGTH_SHORT).show();
			currentLocation = location;
			currentLatitude = currentLocation.getLatitude();
			currentLongitude = currentLocation.getLongitude();
			GlobalClass.latitud = currentLatitude;
			GlobalClass.longitud = currentLongitude;

			locationText.setTextColor(0xFF000000);
			locationText.setText(currentLatitude + ", " + currentLongitude);
			getaddress();
			((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(true);
			// ((ImageButton)
			// this.findViewById(R.id.scanButton)).setVisibility(0);

		} else {
				if (GlobalClass.coordmapa == false) {
					locationText.setText("Provider not available");
					alertaGPS();
		
					//((ImageButton) this.findViewById(R.id.mapButton))
						//	.setEnabled(false);
				}
			
		}
		
		
		if (GlobalClass.coordmapa == false) {
			locationManager.requestLocationUpdates(provider, 400, 1, this);
			getaddress();
			GlobalClass.address = myaddress.getText().toString();
		}
		
		
		
		this.GPSButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				GlobalClass.coordmapa = false;

				// Get the location manager
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				// Define the criteria how to select the location provider ->
				// use
				// default
				Criteria c = new Criteria();
				c.setAccuracy(Criteria.ACCURACY_FINE);
				c.setAltitudeRequired(false);
				c.setBearingRequired(false);
				c.setSpeedRequired(false);
				c.setCostAllowed(true);
				c.setPowerRequirement(Criteria.POWER_HIGH);

				provider = locationManager.getBestProvider(c, false);

				// Now that we have a location manager, its a simple process to
				// check to
				// see if the GPS is enabled or not:
				if (!locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					createGpsDisabledAlert();
				}

				PendingIntent singleUpatePI = PendingIntent.getBroadcast(
						getBaseContext(), 0, getIntent(),
						PendingIntent.FLAG_UPDATE_CURRENT);
				locationManager.requestSingleUpdate(c, singleUpatePI);

				if (GlobalClass.latitud == 0.00000000) {
					alertaGPS();
				} else {
					scanButton.setEnabled(true);
					MapsButton.setEnabled(true);
				}
			}
		});

		this.MapsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (GlobalClass.latitud == 0.00000000) {
			//		alertaGPS();
					scanButton.setEnabled(true);
					Intent intent = new Intent();
					intent.setClass(Gps_activity.this, MapsActivity.class);
					startActivity(intent);
					onPause();
				} else {
					scanButton.setEnabled(true);
					Intent intent = new Intent();
					intent.setClass(Gps_activity.this, MapsActivity.class);
					startActivity(intent);
					onPause();
					
				}
			}
		});

		((ImageButton) this.findViewById(R.id.scanButton))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						qrscan();
					}
				});
	}

	// This just checks to see if GPS is on, and if not, it calls a method,
	// createGpsDisabledAlert(), which will build an alert dialog to warn the
	// user that their GPS is off:
	private void createGpsDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("El GPS esta deshabilitado. ¿Desea activarlo?")
				.setCancelable(false)
				.setPositiveButton("Activar GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								showGpsOptions();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}

	// Crea una alerta de que no hay coordenadas GPS correctas
	private void alertaGPS() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"No se tienen coordenadas GPS correctas, inténtelo de nuevo")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// If the user decides they want to turn their GPS on, and they select the
	// positive action of the dialog, showGpsOptions() is called. All this
	// method does is to show the “Locations and Security” page from the Android
	// settings menu. As a small sidenote, as of Android 1.5, there is no way to
	// directly toggle the location settings through code, so we have to assume
	// the user can figure it out if shown the proper screen. The code for
	// showGpsOptions() is very simple:
	private void showGpsOptions() {
		Intent gpsOptionsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(gpsOptionsIntent);
	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();

		((ImageButton) this.findViewById(R.id.scanButton))
				.setEnabled(false);

		// Vamos a actualizar las coordenadas GPS solo si no se han seleccionado
		// en el mapa.
		// ya que cuando el usuario abre el mapa y modifica las coordenadas,
		// luego volvemos a este punto de la aplicacion
		if (GlobalClass.coordmapa) {
			locationText.setTextColor(0xFF000000);
			locationText.setText(String.valueOf(GlobalClass.latitud) + ", "
					+ String.valueOf(GlobalClass.longitud));
			myaddress.setText(GlobalClass.address);

			((ImageButton) this.findViewById(R.id.scanButton))
					.setEnabled(true);
			((ImageButton) this.findViewById(R.id.mapButton)).setEnabled(true);

		} else {
			locationManager
					.requestSingleUpdate(provider, this, getMainLooper());
			locationManager.requestLocationUpdates(provider, 400, 1, this);
		//	getaddress();
			GlobalClass.address = myaddress.getText().toString();

			if (GlobalClass.latitud != 0.0000){
			((ImageButton) this.findViewById(R.id.scanButton))
			.setEnabled(true);
			}
		}
		
		((ImageButton) this.findViewById(R.id.scanButton))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				qrscan();
			}
		});
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
		// GlobalClass.global_buscaEC = true;
		// TextoECoBalast.setText("Escanea código de EasyControl");
//		((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(true);
//		locationManager.requestSingleUpdate(provider, this, getMainLooper());
//		locationManager.requestLocationUpdates(provider, 400, 1, this);
//		// getaddress();
//		GlobalClass.address = myaddress.getText().toString();
				
	}

	@Override
	public void onLocationChanged(Location location) {
		if (GlobalClass.coordmapa == false) {

			currentLocation = location;
			currentLatitude = currentLocation.getLatitude();
			currentLongitude = currentLocation.getLongitude();
			GlobalClass.longitud = currentLongitude;
			GlobalClass.latitud = currentLatitude;

			locationText.setTextColor(0xFF000000);
			locationText.setText(currentLatitude + ", " + currentLongitude);
		//	getaddress();
			((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(true);
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		locationManager.requestSingleUpdate(provider, this, getMainLooper());
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	//	getaddress();
		GlobalClass.address = myaddress.getText().toString();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	void qrscan() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 0);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				// Miramos si tenemos que escanear un EC
				if (GlobalClass.global_buscaEC == true) {
					if (contents.substring(0, 2).compareTo("EC") != 0) {
						
						if (contents.substring(0, 2).compareTo("RA") == 0)
						{
							//se ha leido una radio francesa
							Toast.makeText(
									this,
									"Se ha leido el codigo de una radio francesa "
											+ contents.toString(),
									Toast.LENGTH_SHORT).show();
							
						//	GlobalClass.global_dispAntiguo = true;
							GlobalClass.global_buscaEC = false;
							GlobalClass.global_EC = contents.toString();
							TextoECoBalast.setText("Escanea el código de Balasto.");
							// ((ImageButton)
							// this.findViewById(R.id.scanButton)).setVisibility(View.INVISIBLE);
							// ((ImageButton)
							// this.findViewById(R.id.scanButton)).setEnabled(false);
							// ((Spinner)
							// this.findViewById(R.id.spinner1)).setVisibility(View.VISIBLE);

							Intent intent3 = new Intent();
							intent3.setClass(Gps_activity.this,
									balastcod_activity.class);
							startActivity(intent3);
							finish();
							
						}
						else{
						AlertDialog.Builder popup = new AlertDialog.Builder(
								this);
						popup.setTitle("Error de código de barras");
						popup.setMessage("El dispositivo que esta escaneando no es un EasyControl.");
						popup.setPositiveButton("Volver a escanear", null);
						popup.show();
						}

					} else {
						Toast.makeText(
								this,
								"El EasyControl escaneado es "
										+ contents.toString(),
								Toast.LENGTH_SHORT).show();
						GlobalClass.global_buscaEC = false;
						GlobalClass.global_EC = contents.toString();
						TextoECoBalast.setText("Escanea el código de Balasto.");
						// ((ImageButton)
						// this.findViewById(R.id.scanButton)).setVisibility(View.INVISIBLE);
						// ((ImageButton)
						// this.findViewById(R.id.scanButton)).setEnabled(false);
						// ((Spinner)
						// this.findViewById(R.id.spinner1)).setVisibility(View.VISIBLE);

						Intent intent3 = new Intent();
						intent3.setClass(Gps_activity.this,
								balastcod_activity.class);
						startActivity(intent3);
						finish();
					}
				} else
				// Lo que tenemos que escanear es un Balasto asociado al EC
				// anterior
				{
					if (contents.substring(0, 2).compareTo("EC") == 0) {
						AlertDialog.Builder popup = new AlertDialog.Builder(
								this);
						popup.setTitle("Error de código de barras");
						popup.setMessage("El dispositivo que esta escaneando es un EasyControl y debe escanear el balasto.");
						popup.setPositiveButton("Volver a escanear", null);
						popup.show();
					} else {
						GlobalClass.global_buscaEC = true;
						GlobalClass.global_Balast = contents.toString();
						GlobalClass.global_count = GlobalClass.global_count + 1;
						Intent intent2 = new Intent();
						intent2.setClass(Gps_activity.this,
								Resumen_activity.class);
						startActivity(intent2);
						finish();
					}
				}

			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}

	void getaddress() {
		try {
			Geocoder gcd = new Geocoder(this, Locale.getDefault());
			List<Address> addresses = gcd.getFromLocation(currentLatitude,
					currentLongitude, 1);
			if (addresses.size() > 0) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");
				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress
							.append(returnedAddress.getAddressLine(i)).append(
									" ");
				}
				myaddress.setText(strReturnedAddress.toString());
				GlobalClass.address = strReturnedAddress.toString();
			} else {
				myaddress.setText("No location found");
			}
		} catch (IOException ex) {
			myaddress.setText(ex.getMessage().toString());
		}
	}

}

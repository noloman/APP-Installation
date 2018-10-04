package install.sinapse;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MapsActivity extends FragmentActivity {
    private static final int MY_LOCATION_PERMISSION = 0x05;
    // Google Map
    GoogleMap mapa;
    //LocationClient mlocation;
    Location location = null;
    private LocationManager locationManager;
    @SuppressWarnings("unused")
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    MarkerOptions mp = new MarkerOptions();
    String provider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        provider = getIntent().getStringExtra("provider");

        GlobalClass.coordmapa = true;

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapa = googleMap;
                if (mapa == null) {
                    Log.i("Aviso Mapa", "null");
                    Toast.makeText(MapsActivity.this, "Google Maps not avaible", Toast.LENGTH_LONG).show();
                } else {
                    mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_LOCATION_PERMISSION);
                    } else {
                        mapa.setMyLocationEnabled(true);
                    }

                    LatLng espana = new LatLng(40.346077, -3.744769);
                    CameraPosition camPos = new CameraPosition.Builder()
                            .target(espana)   //Centramos el mapa en Madrid
                            .zoom(5)         //Establecemos el zoom en 19        //Bajamos el punto de vista de la c�mara 70 grados
                            .build();


                    CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);

                    mapa.animateCamera(camUpd3);

                    mostrarMarcador(GlobalClass.latitud, GlobalClass.longitud);

                    mapa.setOnMapClickListener(new OnMapClickListener() {
                        public void onMapClick(LatLng point) {
                            mapa.clear();
                            Projection proj = mapa.getProjection();
                            Point coord = proj.toScreenLocation(point);

                            GlobalClass.latitud = point.latitude;
                            GlobalClass.longitud = point.longitude;
                            mostrarMarcador(GlobalClass.latitud, GlobalClass.longitud);
                            if (ContextCompat.checkSelfPermission(MapsActivity.this,
                                    Manifest.permission.READ_PHONE_STATE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.READ_PHONE_STATE},
                                        Install_sinapseActivity.READ_PHONE_STATE_PERMISSION);
                            } else {
                                mostrarLog();
                            }

                            Toast.makeText(MapsActivity.this, "Click\n" + "Lat: " + point.latitude + "\n" + "Lng: " + point.longitude + "\n" + "X: " + coord.x + " - Y: " + coord.y, Toast.LENGTH_SHORT).show();
                        }
                    });

                    mapa.setOnMapLongClickListener(new OnMapLongClickListener() {
                        public void onMapLongClick(LatLng point) {
                            Projection proj = mapa.getProjection();
                            Point coord = proj.toScreenLocation(point);

                            Toast.makeText(MapsActivity.this, "Click Largo\n" + "Lat: " + point.latitude + "\n" + "Lng: " + point.longitude + "\n" + "X: " + coord.x + " - Y: " + coord.y, Toast.LENGTH_SHORT).show();
                        }
                    });

                    mapa.setOnCameraChangeListener(new OnCameraChangeListener() {
                        public void onCameraChange(CameraPosition position) {

                        }
                    });

                    mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
                        public boolean onMarkerClick(Marker marker) {
                            Toast.makeText(MapsActivity.this, "Marcador pulsado:\n" + marker.getPosition(), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                }
            }
        });
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private void mostrarLog() {
        //List<String> locationProvider = locationManager.getAllProviders();
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
        //calendar.setTimeInMillis(loc.getTime());
        String fecha = ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.GERMANY)).format(new Date())).toString();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",Locale.GERMANY);
        String fecha_dia = fecha.substring(0, 10);
        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
        String deviceId = telephonyManager.getDeviceId();
        String version_device = telephonyManager.getDeviceSoftwareVersion();
        String operador = telephonyManager.getNetworkOperatorName();
        Integer red = telephonyManager.getNetworkType();
        SensorManager sensores = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> lista_sensores = sensores.getSensorList(Sensor.TYPE_ALL);
        String escaneo = null;


        Log.v("Datos Telefono", "----");
        Log.i("IMEI", deviceId);
        if (version_device != null)
            Log.i("Version Dispositivo", version_device);
        Log.i("Operador", operador);
        Log.i("Red", red + "");

        Log.i("Board", Build.BOARD);
        Log.i("Marca", Build.BRAND);
        Log.i("Bootloader", Build.BOOTLOADER);
        Log.i("Dispositivo", Build.DEVICE);
        Log.i("Numero de Modelo", Build.MODEL);
        Log.i("Producto", Build.PRODUCT);
        Log.i("Hardware", Build.HARDWARE);
        Log.i("Host", Build.HOST);
        Log.i("FingerPrint", Build.FINGERPRINT);
        Log.i("Manufacter", Build.MANUFACTURER);
        Log.i("Serial", Build.SERIAL);
        Log.i("Tags", Build.TAGS);
        Log.i("Time", Build.TIME + "");
        Log.i("Type", Build.TYPE);
        Log.i("User", Build.USER);
        Log.i("Kernel", System.getProperty("os.version"));

        Log.i("SDK Android", Build.VERSION.SDK);
        Log.i("Version Android", Build.VERSION.RELEASE);
        Log.i("Codename ANDROID", Build.VERSION.CODENAME);
        Log.i("Version Banda Base ANDROID", Build.VERSION.INCREMENTAL);

        for (int i = 0; i < lista_sensores.size(); ++i) {
            Log.i("Sensor n" + i, lista_sensores.get(i).getName().toString());
        }
        Log.v("--------", "----------");

        if (GlobalClass.global_buscaEC == true) {
            Log.v("Activity", "Escaneando EasyControl");
            escaneo = "EasyControl";
        } else {
            Log.v("Activity", "Escaneando Balasto");
            escaneo = "Balasto";
        }

        Log.v("Proveedores Coordenadas GPS", "----");

        Log.v("--------", "----------");
        Log.v("Datos Coordenadas GPS", "----");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
        String precision = pref.getString("GPS", "ALTA");

        Log.i("Latitud", "");
        Log.i("Longitud", "");
        Log.i("Precisi�n", precision);
        Log.i("Altitud", "");
        Log.i("Orientaci�n", "");
        Log.i("Velocidad", "");
        Log.i("Fecha y Hora", fecha);
        Log.i("Dia", fecha_dia);
        Log.v("--------", "----------");
        Log.v("Variables Globales", "--------------------");
        Log.i("Google_Maps Coordenadas", GlobalClass.coordmapa + "");
        Log.i("Latitud", GlobalClass.latitud + "");
        Log.i("Longitud", GlobalClass.longitud + "");
        Log.i("Codigo_EC", GlobalClass.global_EC);
        Log.i("ID_Punto", GlobalClass.nombre_punto);
        Log.i("Instalacion", GlobalClass.global_localiz);

        // create a File object for the parent directory
     /*   File root = Environment.getExternalStorageDirectory();
		File logsDirectory = new File(root + "/sinapse/install/");
		// have the object build the directory structure, if needed.
		if(logsDirectory.exists()==false)
		{
			logsDirectory.mkdirs();
		}
		String nombre_fichero = fecha_dia+"-log-"+deviceId+".csv";
		
		File fichero = new File(logsDirectory, nombre_fichero);
		
		if(fichero.exists()==false)
		{
			try 
			{
				fichero.createNewFile();
				CharSequence linea = GlobalClass.global_var4;
		        FileWriter writer_cabecera = new FileWriter(fichero, true);
	        	writer_cabecera.append(linea);
				writer_cabecera.flush();
				
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
        	String linea_log = ";" + deviceId 
        				+ ";" + version_device
        				+ ";" + operador
        				+ ";" + red
        				+ ";" + Build.BOARD
        				+ ";" + Build.BRAND
        				+ ";" + Build.BOOTLOADER
        				+ ";" + Build.DEVICE
        				+ ";" + Build.MODEL
        				+ ";" + Build.PRODUCT
        				+ ";" + Build.HARDWARE
        				+ ";" + Build.HOST
        				+ ";" + Build.FINGERPRINT
        				+ ";" + Build.MANUFACTURER
        				+ ";" + Build.SERIAL
        				+ ";" + Build.TAGS
        				+ ";" + Build.TIME + "" 
        				+ ";" + Build.TYPE
        				+ ";" + Build.USER
        				+ ";" + System.getProperty("os.version")
        				+ ";" + Build.VERSION.SDK
        				+ ";" + Build.VERSION.RELEASE
        				+ ";" + Build.VERSION.CODENAME
        				+ ";" + Build.VERSION.INCREMENTAL
        				+ ";" + escaneo
        				+ ";" + ""//provider
        				+ ";" + ""
        				+ ";" + ""
        				+ ";" + precision
        				+ ";" + ""
        				+ ";" + ""
        				+ ";" + ""
        				+ ";" + fecha
        				+ ";" + GlobalClass.coordmapa+""
        				+ ";" + GlobalClass.latitud+""
        				+ ";" + GlobalClass.longitud+""
        				+ ";" + GlobalClass.global_EC
        				+ ";" + GlobalClass.nombre_punto
        				+ ";" + GlobalClass.global_localiz + "\n";
        	
        	Log.i("Linea_completa", linea_log);
        	CharSequence linea = linea_log;
        	FileWriter writer = null;
        	
            try 
            {
            	writer = new FileWriter(fichero, true);
            	Log.i("Estado","Escribiendo...");
            	writer.append(linea);
    			writer.flush();
    			
    		} 
            catch (IOException e) 
            {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			Toast.makeText(this,"Imposible escribir en la tarjeta de memoria",Toast.LENGTH_LONG).show();
    			Toast.makeText(this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
    		}*/
    }


    private void mostrarMarcador(double lat, double lng) {
        mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Luminaria"));
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
		 /*Intent intent = new Intent(); 
		 intent.setClass(MapsActivity.this,Gps_activity.class); 
		 startActivity(intent); 
		 finish();*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Install_sinapseActivity.READ_PHONE_STATE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mostrarLog();
                }
                return;
            }
            case MY_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapa.setMyLocationEnabled(true);

                }
                return;
            }
        }
    }
}

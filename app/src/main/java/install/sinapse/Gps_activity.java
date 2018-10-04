package install.sinapse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.apache.commons.net.ftp.FTP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

// SE COMENTA GETADRESS Y alertaGPS para que al no tener red el movil no se lie

public class Gps_activity<GPSActivity> extends Activity implements LocationListener {
    private LocationManager locationManager;
    private String provider;
    //Coordenadas
    double currentLatitude;
    double currentLongitude;
    Location currentLocation;
    //Label
    TextView locationText;
    TextView myaddress;
    TextView TextoECoBalast;
    //TextView RefRadio;
    TextView textGPSTomando;

    //Botones
    ImageButton MapsButton;
    ImageButton scanButton;
    ImageButton GPSButton;
    boolean manual = false;


    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.gps_layout);

        //Relacionamos las variables con el layout
        locationText = (TextView) findViewById(R.id.locationText);
        myaddress = (TextView) findViewById(R.id.myaddress);
        TextoECoBalast = (TextView) findViewById(R.id.TextoECoBalast);
        GPSButton = (ImageButton) findViewById(R.id.GPSButton);
        textGPSTomando = (TextView) findViewById(R.id.textGPSTomando);
        MapsButton = (ImageButton) findViewById(R.id.mapButton);
        scanButton = (ImageButton) findViewById(R.id.scanButton);

        MapsButton.setEnabled(true); //Activamos el boton de localizaci�n
        scanButton.setEnabled(false);//Desactivamos el boton de escaneo de easycontrol

        manual = false;

        GlobalClass.global_BE = "";
        GlobalClass.global_DE = "";

        if (GlobalClass.global_buscaCMC == true && GlobalClass.global_buscaPA == false)
            GlobalClass.global_CMC = "";

        GlobalClass.global_PC = "";
        GlobalClass.global_BR = "";
        GlobalClass.global_MC = "";

        if (GlobalClass.global_remplazo == true) {
            textGPSTomando.setText("Localizaci�n del Dispositivo");
            TextoECoBalast.setText("Escanea ahora el dispositivo nuevo");
        } else {
            switch (GlobalClass.global_tipo) {
                case 1:
                    textGPSTomando.setText("Localizaci�n del Punto");
                    break;
                case 2:
                    textGPSTomando.setText("Localizaci�n del Cuadro");
                    break;

                case 3:
                    textGPSTomando.setText("Localizaci�n del Punto de Acceso");
                    break;

                case 4:
                    textGPSTomando.setText("Localizaci�n del Dispositivo");
                    break;
                case 5:
                    textGPSTomando.setText("Localizaci�n del Punto");
                    break;
            }
        }

        if (GlobalClass.global_buscaOtro == false && GlobalClass.global_remplazo == false && GlobalClass.global_buscaEC == false && GlobalClass.global_buscaPA == false && GlobalClass.global_buscaCMC == false) {
            GlobalClass.cont_balasto++;
            TextoECoBalast.setText("Escanea el c�digo del Balasto o Driver ");
            if ((GlobalClass.balasto_marca.compareTo("SINAPSE") == 0) || (GlobalClass.balasto2_marca.compareTo("SINAPSE") == 0)) {

            } else {
                // Aqui le digo que lo siguiente que tiene que buscar es un EC
                GlobalClass.global_buscaEC = true;
                Intent intent3 = new Intent();
                intent3.setClass(Gps_activity.this, balastcod_activity.class);
                startActivity(intent3);
                finish();
            }

        }


        ((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(false);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria c = new Criteria();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Gps_activity.this);
        String s = pref.getString("GPS", "ALTA");
        //Log.i("Valor actual", s);

        if (s.compareToIgnoreCase("ALTA") == 0) {
            Toast.makeText(getApplicationContext(), "Precisi�n GPS Alta", Toast.LENGTH_SHORT).show();
            c.setAccuracy(Criteria.ACCURACY_FINE);
        } else {
            Toast.makeText(getApplicationContext(), "Precisi�n GPS Media", Toast.LENGTH_SHORT).show();
            c.setAccuracy(Criteria.ACCURACY_COARSE);
        }

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
        //de a�adir el punto con el mapa
        if (GlobalClass.latitud == 0.00000000) {
            alertaGPS();
            ((ImageButton) this.findViewById(R.id.mapButton)).setEnabled(true);
        } else {
            ((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(true);
            ((ImageButton) this.findViewById(R.id.mapButton)).setEnabled(true);
        }


        // Initialize the location fields
        if (location != null && GlobalClass.coordmapa == false) {
            Toast.makeText(this, "Provider " + provider + " has been selected.", Toast.LENGTH_SHORT).show();
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


            mostrarLog(location);


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
                Log.d("Aviso", "Pulsado el boton GPS Button");
                GlobalClass.coordmapa = false;

                // Get the location manager
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // Define the criteria how to select the location provider ->
                // use
                // default
                Criteria c = new Criteria();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Gps_activity.this);
                String s = pref.getString("GPS", "ALTA");
                //Log.i("Valor actual", s);

                if (s.compareToIgnoreCase("ALTA") == 0) {
                    Toast.makeText(getApplicationContext(), "Precisi�n GPS Alta", Toast.LENGTH_SHORT).show();
                    c.setAccuracy(Criteria.ACCURACY_FINE);
                } else {
                    Toast.makeText(getApplicationContext(), "Precisi�n GPS Media", Toast.LENGTH_SHORT).show();
                    c.setAccuracy(Criteria.ACCURACY_COARSE);
                }


                c.setAltitudeRequired(false);
                c.setBearingRequired(false);
                c.setSpeedRequired(false);
                c.setCostAllowed(true);
                c.setPowerRequirement(Criteria.POWER_HIGH);

                provider = locationManager.getBestProvider(c, false);

                // Now that we have a location manager, its a simple process to
                // check to
                // see if the GPS is enabled or not:
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    createGpsDisabledAlert();
                }

                PendingIntent singleUpatePI = PendingIntent.getBroadcast(getBaseContext(), 0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
                locationManager.requestSingleUpdate(c, singleUpatePI);


                if (GlobalClass.latitud == 0.00000000) {
                    alertaGPS();
                } else {
                    scanButton.setEnabled(true);
                    MapsButton.setEnabled(true);
                    //mostrarLog(location);
                }
            }
        });

        this.MapsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("Aviso", "Pulsado el boton Maps Button");
                if (GlobalClass.latitud == 0.00000000) {
                    alertaGPS();
                    scanButton.setEnabled(true);
                    Intent intent = new Intent();
                    intent.setClass(Gps_activity.this, MapsActivity.class);
                    intent.putExtra("provider", provider);
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

        ((ImageButton) this.findViewById(R.id.scanButton)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                qrscan();
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_gps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.MnuGPS:
                Intent i = new Intent();
                i.setClass(Gps_activity.this, PreferencesActivity.class);
                startActivity(i);
                return true;
            case R.id.MnuActualizar:
                Intent i2 = new Intent();
                i2.setClass(Gps_activity.this, Gps_activity.class);
                startActivity(i2);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
    // This just checks to see if GPS is on, and if not, it calls a method,
    // createGpsDisabledAlert(), which will build an alert dialog to warn the
    // user that their GPS is off:
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (GlobalClass.global_remplazo == true) {
                    GlobalClass.global_buscaPA = false;
                    GlobalClass.global_buscaCMC = false;
                    GlobalClass.global_buscaEC = false;
                    Intent intent = new Intent();
                    intent.setClass(Gps_activity.this, Replace_Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(Gps_activity.this, ComienzoInstalacion_activity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
        }
        return false;
    }

    @SuppressWarnings({"null", "resource"})
    private void mostrarLog(Location loc) {
        List<String> locationProvider = locationManager.getAllProviders();
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
        calendar.setTimeInMillis(loc.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        String fecha_dia = sdf.format(calendar.getTime()).substring(0, 10);
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
        }

        if (GlobalClass.global_buscaEC == false && GlobalClass.global_buscaCMC == false && GlobalClass.global_buscaPA == false) {
            Log.v("Activity", "Escaneando Balasto");
            escaneo = "Balasto";
        }

        if (GlobalClass.global_buscaCMC == true) {
            Log.v("Activity", "Escaneando Cuadro");
            escaneo = "CMC";
        }

        if (GlobalClass.global_buscaPA == true) {
            Log.v("Activity", "Escaneando Punto de Acceso");
            escaneo = "Punto de acceso";
        }

        Log.v("Proveedores Coordenadas GPS", "----");
        for (String p : locationProvider) {
            Log.i("Proveedor GPS", p);
        }
        Log.i("Seleccionado", provider);
        Log.v("--------", "----------");
        Log.v("Datos Coordenadas GPS", "----");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Gps_activity.this);
        String precision = pref.getString("GPS", "ALTA");
        Log.i("Latitud", loc.getLatitude() + "");
        Log.i("Longitud", loc.getLongitude() + "");
        Log.i("Precisi�n", precision + "-" + loc.getAccuracy() + "");
        Log.i("Altitud", loc.getAltitude() + "");
        Log.i("Orientaci�n", loc.getBearing() + "");
        Log.i("Velocidad", loc.getSpeed() + "");
        Log.i("Fecha y Hora", sdf.format(calendar.getTime()));
        Log.i("Dia", fecha_dia);
        Log.v("--------", "----------");
        Log.v("Variables Globales", "--------------------");
        Log.i("Google_Maps Coordenadas", GlobalClass.coordmapa + "");
        Log.i("Latitud", GlobalClass.latitud + "");
        Log.i("Longitud", GlobalClass.longitud + "");

        if (GlobalClass.global_buscaEC == true) {
            Log.v("Activity", "Escaneando EasyControl");
            escaneo = "EasyControl";
        } else {
            Log.v("Activity", "Escaneando Balasto");
            escaneo = "Balasto";
        }

        Log.i("ID_Punto", GlobalClass.nombre_punto);
        Log.i("Instalacion", GlobalClass.global_localiz);

    }

    private void createGpsDisabledAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El GPS esta deshabilitado. �Desea activarlo?")
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
                "No se tienen coordenadas GPS correctas, intentelo de nuevo")
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
    // method does is to show the �Locations and Security� page from the Android
    // settings menu. As a small sidenote, as of Android 1.5, there is no way to
    // directly toggle the location settings through code, so we have to assume
    // the user can figure it out if shown the proper screen. The code for
    // showGpsOptions() is very simple:
    private void showGpsOptions() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();

        ((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(false);

        // Vamos a actualizar las coordenadas GPS solo si no se han seleccionado
        // en el mapa.
        // ya que cuando el usuario abre el mapa y modifica las coordenadas,
        // luego volvemos a este punto de la aplicacion
        if (GlobalClass.coordmapa) {
            locationText.setTextColor(0xFF000000);
            locationText.setText(String.valueOf(GlobalClass.latitud) + ", " + String.valueOf(GlobalClass.longitud));
            myaddress.setText(GlobalClass.address);

            ((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(true);
            ((ImageButton) this.findViewById(R.id.mapButton)).setEnabled(true);

        } else {
            locationManager.requestSingleUpdate(provider, this, getMainLooper());
            locationManager.requestLocationUpdates(provider, 400, 1, this);
            //	getaddress();
            GlobalClass.address = myaddress.getText().toString();

            if (GlobalClass.latitud != 0.0000) {
                ((ImageButton) this.findViewById(R.id.scanButton)).setEnabled(true);
            }
        }

        ((ImageButton) this.findViewById(R.id.scanButton)).setOnClickListener(new OnClickListener() {
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
        // TextoECoBalast.setText("Escanea codigo de EasyControl");
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

            mostrarLog(location);

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        locationManager.requestSingleUpdate(provider, this, getMainLooper());
        locationManager.requestLocationUpdates(provider, 400, 1, this);
        //	getaddress();
        GlobalClass.address = myaddress.getText().toString();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    void qrscan() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                if (GlobalClass.global_remplazo == true) {
                    //String fecha = ((new SimpleDateFormat("yyyy-MM-dd",Locale.GERMANY).format(new Date())).toString());
                    String fecha = ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY)).format(new Date())).toString();
                    GlobalClass.global_RadioNueva = contents.toString();

                    if (GlobalClass.global_RadioAntigua.substring(16, 18).compareTo("BE") == 0) {
                        int n = GlobalClass.global_RadioAntigua.length();
                        switch (n) {
                            case 37:
                                GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(31, 37));

                                break;
                            case 43:
                                GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(37, 43));

                                break;
                        }
                    } else {
                        if (GlobalClass.global_RadioAntigua.substring(16, 18).compareTo("DE") == 0)
                            GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(37, 43));
                        else {
                            if (GlobalClass.global_RadioAntigua.substring(0, 2).compareTo("EC") == 0) {
                                int tam = GlobalClass.global_RadioAntigua.length();
                                switch (tam) {
                                    case 30:
                                        GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(24, 30));
                                        break;
                                    case 36:
                                        GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(30, 36));
                                        break;
                                }
                            } else {
                                if (contents.substring(16, 18).compareTo("LT") == 0 || contents.substring(15, 17).compareTo("LT") == 0) {
                                    int bal = GlobalClass.global_RadioAntigua.length();
                                    switch (bal) {
                                        case 34:
                                            GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(30, 34));
                                            break;
                                        case 35:
                                            GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(31, 35));
                                            break;
                                        case 36:
                                            GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(32, 36));
                                            break;
                                        case 32:
                                            GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(28, 32));
                                            break;
                                        case 33:
                                            GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(29, 33));
                                            break;

                                    }
                                } else {
                                    if ((contents.substring(16, 21).compareTo("DELU0") == 0))
                                        GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(26, 30));
                                    else
                                        GlobalClass.idradioantigua = Integer.parseInt(GlobalClass.global_RadioAntigua.substring(30, 36));

                                }
                            }

                        }
                    }

                    if (GlobalClass.global_RadioNueva.substring(16, 18).compareTo("BE") == 0) {
                        int n = GlobalClass.global_RadioNueva.length();
                        switch (n) {
                            case 37:
                                GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(31, 37));

                                break;
                            case 43:
                                GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(37, 43));

                                break;
                        }
                    } else {
                        if (GlobalClass.global_RadioNueva.substring(16, 18).compareTo("DE") == 0)
                            GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(37, 43));
                        else {
                            if (GlobalClass.global_RadioNueva.substring(0, 2).compareTo("EC") == 0) {
                                int tam = GlobalClass.global_RadioNueva.length();
                                switch (tam) {
                                    case 30:
                                        GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(24, 30));
                                        break;
                                    case 36:
                                        GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(30, 36));
                                        break;
                                }
                            } else {
                                if (contents.substring(16, 18).compareTo("LT") == 0 || contents.substring(15, 17).compareTo("LT") == 0) {
                                    int bal = GlobalClass.global_RadioNueva.length();
                                    switch (bal) {
                                        case 34:
                                            GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(30, 34));
                                            break;
                                        case 35:
                                            GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(31, 35));
                                            break;
                                        case 36:
                                            GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(32, 36));
                                            break;
                                        case 32:
                                            GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(28, 32));
                                            break;
                                        case 33:
                                            GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(29, 33));
                                            break;

                                    }
                                } else {
                                    if ((contents.substring(16, 21).compareTo("DELU0") == 0))
                                        GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(26, 30));
                                    else
                                        GlobalClass.idradionueva = Integer.parseInt(GlobalClass.global_RadioNueva.substring(30, 36));

                                }
                            }
                        }
                    }

                    Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
                    String formatoJSON;
                    Remplazo remplazo = new Remplazo(GlobalClass.global_RadioAntigua, GlobalClass.idradioantigua, GlobalClass.global_RadioNueva, GlobalClass.idradionueva, fecha);

                    String f = ((new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(new Date())).toString());
                    String srvcName = Context.TELEPHONY_SERVICE;
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
                    String deviceId = telephonyManager.getDeviceId();
                    File root = Environment.getExternalStorageDirectory();
                    File logsDirectory = new File(root + "/sinapse/install/");
                    String sFichero = logsDirectory + "/" + f + "-replace-" + GlobalClass.global_localiz + "-" + deviceId + ".json";
                    File fichero = new File(sFichero);

                    if (fichero.exists()) {
                        JsonReader reader;
                        ArrayList bajas2 = new ArrayList();

                        try {
                            reader = new JsonReader(new InputStreamReader(new FileInputStream(sFichero), "UTF-8"));

                            reader.beginArray();

                            while (reader.hasNext()) {
                                Remplazo r = prettyGson.fromJson(reader, Remplazo.class);
                                bajas2.add(r);
                            }

                            reader.endArray();
                            reader.close();

                            bajas2.add(remplazo);


                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        formatoJSON = prettyGson.toJson(bajas2);
                    } else {
                        GlobalClass.bajas.add(remplazo);
                        formatoJSON = prettyGson.toJson(GlobalClass.bajas);
                    }

                    createFile(GlobalClass.global_localiz, formatoJSON);

                    try {

                        BufferedWriter bw = new BufferedWriter(new FileWriter(GlobalClass.global_fichero_R));
                        bw.write("");
                        bw.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

                    anaderegistro(GlobalClass.global_localiz, formatoJSON);

                    Toast.makeText(this, "Dispositivo reemplazado correctamente.", Toast.LENGTH_LONG).show();

                    uploadFTP(GlobalClass.global_fichero_R);

                    Intent i = new Intent();
                    i.setClass(Gps_activity.this, Install_sinapseActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    switch (GlobalClass.global_tipo) {
                        case 1:
                            if ((contents.substring(16, 21).compareTo("DELUT") == 0) || (contents.substring(16, 21).compareTo("DELUI") == 0)) {
                                Toast.makeText(this, "El DE escaneado es " + contents.toString(), Toast.LENGTH_SHORT).show();
                                GlobalClass.global_DE = contents.toString();
                                Intent i = new Intent();
                                i.setClass(Gps_activity.this, formPunto_activity.class);
                                startActivity(i);
                                finish();
                            } else {
                                if ((contents.substring(16, 18).compareTo("BE") == 0)) {
                                    Toast.makeText(this, "El BE escaneado es " + contents.toString(), Toast.LENGTH_SHORT).show();
                                    GlobalClass.global_BE = contents.toString();
                                    Intent i = new Intent();
                                    i.setClass(Gps_activity.this, formPunto_activity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    if (contents.substring(0, 2).compareTo("SM") == 0 || contents.substring(0, 2).compareTo("MC") == 0) {
                                        Toast.makeText(this, "El m�dulo de comunicaci�n escaneado es " + contents.toString(), Toast.LENGTH_SHORT).show();
                                        GlobalClass.global_MC = contents.toString();
                                        Intent i = new Intent();
                                        i.setClass(Gps_activity.this, formPunto_activity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        if ((contents.substring(0, 2).compareTo("EC") == 0) || (contents.substring(0, 2).compareTo("EL") == 0)) {
                                            Toast.makeText(this, "El EasyControl escaneado es " + contents.toString(), Toast.LENGTH_SHORT).show();
                                            GlobalClass.global_buscaEC = false;
                                            GlobalClass.global_tipo = 5;
                                            GlobalClass.global_EC = contents.toString();
                                            TextoECoBalast.setText("Escanea el c�digo de Balasto.");
                                            Intent intent3 = new Intent();
                                            intent3.setClass(Gps_activity.this, balastcod_activity.class);
                                            startActivity(intent3);
                                            finish();
                                        } else {
                                            AlertDialog.Builder popup = new AlertDialog.Builder(this);
                                            popup.setTitle("Error de c�digo de barras");
                                            popup.setMessage("El dispositivo que esta escaneando no es correcto.");
                                            popup.setPositiveButton("Volver a escanear", null);
                                            popup.show();
                                        }
                                    }

                                }

                            }

                            break;

                        case 2:
                            if ((contents.substring(0, 2).compareTo("PC") == 0)) {
                                Toast.makeText(this, "El CMC escaneado es " + contents.toString(), Toast.LENGTH_SHORT).show();
                                GlobalClass.global_CMC = contents.toString();
                                Intent i = new Intent();
                                i.setClass(Gps_activity.this, formCuadro_activity.class);
                                startActivity(i);
                                finish();
                            } else {
                                AlertDialog.Builder popup = new AlertDialog.Builder(this);
                                popup.setTitle("Error de c�digo de barras");
                                popup.setMessage("El dispositivo que esta escaneando no es correcto.");
                                popup.setPositiveButton("Volver a escanear", null);
                                popup.show();
                            }


                            break;

                        case 3:
                            if ((contents.substring(0, 2).compareTo("EN") == 0) || (contents.substring(0, 2).compareTo("SE") == 0) || (contents.substring(0, 2).compareTo("MS") == 0)) {
                                Toast.makeText(this, "El Puente de comunicacion escaneado es " + contents.toString(), Toast.LENGTH_SHORT).show();
                                GlobalClass.global_PC = contents.toString();
                                Intent i = new Intent();
                                i.setClass(Gps_activity.this, PuntoAcceso_activity.class);
                                startActivity(i);
                                finish();
                            } else {
                                if ((contents.substring(0, 2).compareTo("TQ") == 0) || (contents.substring(0, 2).compareTo("NT") == 0)) {
                                    Toast.makeText(this, "El Bridge escaneado es " + contents.toString(), Toast.LENGTH_SHORT).show();
                                    GlobalClass.global_BR = contents.toString();
                                    Intent i = new Intent();
                                    i.setClass(Gps_activity.this, PuntoAcceso_activity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    AlertDialog.Builder popup = new AlertDialog.Builder(this);
                                    popup.setTitle("Error de c�digo de barras");
                                    popup.setMessage("El dispositivo que esta escaneando no es correcto.");
                                    popup.setPositiveButton("Volver a escanear", null);
                                    popup.show();
                                }
                            }

                            break;

                        case 4:
                            Toast.makeText(this, "El dispositivo escaneado es " + contents.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.global_Otro = contents.toString();
                            Intent i = new Intent();
                            i.setClass(Gps_activity.this, OtroDispositivo_activity.class);
                            startActivity(i);
                            finish();

                            break;

                        case 5:

                            if (contents.substring(16, 18).compareTo("LT") == 0 || contents.substring(15, 17).compareTo("LT") == 0 || (contents.substring(16, 21).compareTo("DELU0") == 0)) {
                                if (GlobalClass.cont_balasto == 1 && GlobalClass.cont_punto == 1)
                                    GlobalClass.global_Balast = contents.toString();
                                else
                                    GlobalClass.global_Balast2 = contents.toString();

                                if (GlobalClass.tipo_punto.compareTo("DOBLE") == 0) {
                                    GlobalClass.tipo_puntoAux = GlobalClass.tipo_punto;
                                    GlobalClass.tipo_punto = "SIMPLE:";
                                    Intent intent2 = new Intent();
                                    intent2.setClass(Gps_activity.this, balastcod_activity.class);
                                    startActivity(intent2);
                                    finish();
                                } else {
                                    GlobalClass.global_buscaEC = true;

                                    if (GlobalClass.cont_balasto == 2 || GlobalClass.cont_punto == 2)
                                        GlobalClass.tipo_puntoAux = "DOBLE";
                                    else
                                        GlobalClass.tipo_puntoAux = GlobalClass.tipo_punto;

                                    Intent intent2 = new Intent();
                                    intent2.setClass(Gps_activity.this, formPunto_activity.class);
                                    startActivity(intent2);
                                    finish();
                                }
                            } else {
                                AlertDialog.Builder popup = new AlertDialog.Builder(this);
                                popup.setTitle("Error del c�digo qr");
                                popup.setMessage("El dispositivo que esta escaneando no es correcto, debe escanear el balasto.");
                                popup.setPositiveButton("Volver a escanear", null);
                                popup.show();
                            }

                            break;
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
            List<Address> addresses = gcd.getFromLocation(currentLatitude, currentLongitude, 1);
            if (addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
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


    public boolean createFile(String tipo, String objJson) {
        boolean flag = false;
        //String fecha = ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",Locale.GERMANY)).format(new Date())).toString();
        //String fech = fecha.substring(0, 19);
        String fecha = ((new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(new Date())).toString());

        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
        String deviceId = telephonyManager.getDeviceId();

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Tarjeta SD no montada", Toast.LENGTH_LONG).show();
        } else {
            File nmea_file;
            File root = Environment.getExternalStorageDirectory();
            FileWriter nmea_writer = null;
            try {
                // create a File object for the parent directory
                File logsDirectory = new File(root + "/sinapse/install/");
                // have the object build the directory structure, if needed.
                logsDirectory.mkdirs();

                nmea_file = new File(logsDirectory, fecha + "-replace-" + tipo + "-" + deviceId + ".json");

                // Almaceno en la cadena fichero la ruta del fichero que
                // tendremos que subir al FTP
                GlobalClass.global_fichero_R = logsDirectory + "/" + fecha + "-replace-" + tipo + "-" + deviceId + ".json";

                if (!nmea_file.exists()) {
                    flag = nmea_file.createNewFile();
                    Toast.makeText(this, "El fichero no existia, por lo que se ha creado", Toast.LENGTH_LONG).show();

                    nmea_writer = new FileWriter(nmea_file, true);
                    CharSequence nmea = objJson;

                    nmea_writer.append(nmea);
                    nmea_writer.flush();
                }
            } catch (IOException ex) {
                Toast.makeText(this, "Imposible escribir en la tarjeta de memoria", Toast.LENGTH_LONG).show();
                Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            } finally {
                if (nmea_writer != null) {
                    try {
                        nmea_writer.close();
                    } catch (IOException e) {
                        Toast.makeText(this, "Error cerrando el fichero", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        return flag;

    }

    public boolean anaderegistro(String tipo, String linea) {
        boolean flag = false;
        String fecha = ((new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(new Date())).toString());

        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
        String deviceId = telephonyManager.getDeviceId();

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Tarjeta SD no montada", Toast.LENGTH_LONG).show();
        } else {
            File nmea_file;
            File root = Environment.getExternalStorageDirectory();
            FileWriter nmea_writer = null;
            try {
                // create a File object for the parent directory
                File logsDirectory = new File(root + "/sinapse/install/");
                // Almaceno en la cadena fichero la ruta del fichero que
                // tendremos que subir al FTP
                GlobalClass.global_fichero_R = logsDirectory + "/" + fecha + "-replace-" + tipo + "-" + deviceId + ".json";
                // have the object build the directory structure, if needed.
                logsDirectory.mkdirs();

                nmea_file = new File(logsDirectory, fecha + "-replace-" + tipo + "-" + deviceId + ".json");
                if (!nmea_file.exists()) {
                    flag = nmea_file.createNewFile();
                    Toast.makeText(this, "El fichero no existia, por lo que se ha creado", Toast.LENGTH_LONG).show();
                }
                nmea_writer = new FileWriter(nmea_file, true);
                CharSequence nmea = linea;

                nmea_writer.append(nmea);
                nmea_writer.flush();
            } catch (IOException ex) {
                Toast.makeText(this, "Imposible escribir en la tarjeta de memoria", Toast.LENGTH_LONG).show();
                Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            } finally {
                if (nmea_writer != null) {
                    try {
                        nmea_writer.close();
                    } catch (IOException e) {
                        Toast.makeText(this, "Error cerrando el fichero", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        return flag;

    }

    public void alertaSubir() {
        //Recogemos el servicio ConnectivityManager
        //el cual se encarga de todas las conexiones del terminal
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //Recogemos el estado del 3G
        //como vemos se recoge con el par�metro 0
        State internet_movil = conMan.getNetworkInfo(0).getState();
        //Recogemos el estado del wifi
        //En este caso se recoge con el par�metro 1
        State wifi = conMan.getNetworkInfo(1).getState();
        //Miramos si el internet 3G est� conectado o conectandose...
        if (internet_movil == NetworkInfo.State.CONNECTED || internet_movil == NetworkInfo.State.CONNECTING) {
            ///////////////
            //El movil est� conectado por 3G
            //En este ejemplo mostrar�amos mensaje por pantalla
            //Toast.makeText(getApplicationContext(), "Conectado por 3G", Toast.LENGTH_LONG).show();
            //Si no esta por 3G comprovamos si est� conectado o conectandose al wifi...
            Log.i("Estado", "Subiendo archivos por 3G");
            alerta3G();
        } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            ///////////////
            //El movil est� conectado por WIFI
            //En este ejemplo mostrar�amos mensaje por pantalla
            //Toast.makeText(getApplicationContext(), "Conectado por WIFI", Toast.LENGTH_LONG).show();
            Log.i("Estado", "Subiendo archivos por WIFI");

        } else {
            Toast.makeText(getApplicationContext(), "No puedes subir los archivos, no tienes conexi�n", Toast.LENGTH_LONG).show();
        }
    }

    // Crea una alerta de que no hay coordenadas GPS correctas
    private void alerta3G() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Est�s conectado por 3G. El proceso puede tardar un tiempo. �Desea continuar?")
                .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Aceptada.");

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    //Metodo para subir ficheros FTP
    public void uploadFTP(String file) {
        org.apache.commons.net.ftp.FTPClient con = new org.apache.commons.net.ftp.FTPClient();
        try {
            con.connect("89.248.100.11");
            if (con.login("trazabilidad", "napse1si")) {
                con.enterLocalPassiveMode(); // important!
                /*
                 * String data = file; ByteArrayInputStream in = new
                 * ByteArrayInputStream(data.getBytes());
                 */
                con.changeWorkingDirectory("/Instalacion-" + GlobalClass.global_localiz);
                File fichero = new File(file);
                FileInputStream input = new FileInputStream(fichero);
                con.setFileType(FTP.BINARY_FILE_TYPE);

                boolean result = con.storeFile(fichero.getName(), input);
                input.close();
                if (result) {
                    Toast.makeText(this, "FTP actualizado", Toast.LENGTH_LONG).show();
                }

            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            try {
                con.connect("ftp.tgame.es");
                if (con.login("tgame_7626990", "napse1si")) {
                    con.enterLocalPassiveMode(); // important!
                    /*
                     * String data = file; ByteArrayInputStream in = new
                     * ByteArrayInputStream(data.getBytes());
                     */
                    con.changeWorkingDirectory("/Trazabilidad/");
                    File fichero = new File(file);
                    FileInputStream input = new FileInputStream(fichero);
                    con.setFileType(FTP.BINARY_FILE_TYPE);

                    boolean result = con.storeFile(fichero.getName(), input);
                    input.close();
                    if (result) {
                        Toast.makeText(this, "FTP actualizado", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }

        try {
            con.logout();
            con.disconnect();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}

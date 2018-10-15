// **** FUNCION PRINCIPAL DE LA APLICACION ****
// Autor: Celia Moreno 
// ********************************************
// Esta actividad llama a Gps_activity para a�adir un nuevo punto de la instalacion
// As� mismo cuando se desea reemplazar una radio, se llama a la actividad Replace

package install.sinapse;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Install_sinapseActivity extends Activity {
    static final int READ_PHONE_STATE_PERMISSION = 0x01;
    private static final int CALL_PHONE_PERMISSION = 0x02;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 0x03;
    private static final int READ_PHONE_STATE_AND_CHECK_IMEI_PERMISSION = 0x04;
    TextView instalacionText;
    boolean flagEarth = false;
    // TextView numptosText;
    //Botones
    ImageButton addButton;
    ImageButton replaceButton;
    ImageButton subirJSON;
    ImageButton parteTrabajo;
    ImageButton kml;
    //Variables globales
    GlobalClass global = new GlobalClass();
    String[] directorio_archivos;
    File root = Environment.getExternalStorageDirectory();        //Accedemos a la tarjeta SD
    File logsDirectory = new File(root + "/sinapse/install/");    //Direcci�n del directorio
    boolean archivos_subidos = true;

    String deviceId;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.main);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        addButton = (ImageButton) findViewById(R.id.addButton);
        replaceButton = (ImageButton) findViewById(R.id.replaceButton);
        //instalacionText = (TextView)findViewById(R.id.InstalacionText);
        subirJSON = (ImageButton) findViewById(R.id.subirJSON);
        parteTrabajo = (ImageButton) findViewById(R.id.parteTrabajo);
        kml = (ImageButton) findViewById(R.id.verPuntos);

        try {
            GlobalClass.global_localiz = nombreInstalacion(root + "/sinapse/install/IdInstall.txt");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //A�adir un punto de instalaci�n
        this.addButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                reiniciavariables();

                Intent intent = new Intent();
                intent.setClass(Install_sinapseActivity.this, ComienzoInstalacion_activity.class);
                startActivity(intent);
                finish();
            }
        });

        //Reemplazar un punto de instalaci�n
        this.replaceButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                reiniciavariables();

                Intent intent = new Intent();
                intent.setClass(Install_sinapseActivity.this, Replace_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        this.subirJSON.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                lanzaavisoSubir();
            }

        });

        parteTrabajo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                GlobalClass.comentario = "";
                Intent i = new Intent(getApplicationContext(), ListaParteTrabajo.class);
                startActivity(i);
            }

        });

        kml.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(Install_sinapseActivity.this,
                        android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(Install_sinapseActivity.this,
                            new String[]{android.Manifest.permission.READ_PHONE_STATE},
                            READ_PHONE_STATE_PERMISSION);
                } else {
                    viewKlmFileInGoogleEarth();
                }
            }
        });
        if ((ContextCompat.checkSelfPermission(Install_sinapseActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                ContextCompat.checkSelfPermission(Install_sinapseActivity.this,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Install_sinapseActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
        } else {
            createFile("Mapa Sinapse Instalaciones", GlobalClass.CabeceraKML);
        }
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private void viewKlmFileInGoogleEarth() {
        String fecha = ((new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(new Date())).toString()); //Recojo la fecha de hoy
        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
        deviceId = telephonyManager.getDeviceId();                                                    //Recojo el IMEI
        File file = new File(Environment.getExternalStorageDirectory(), "/sinapse/install/Mapa Sinapse Instalaciones-" + deviceId + "-" + fecha + ".kml");
        Uri uriFromFile =
                FileProvider.getUriForFile(Install_sinapseActivity.this,
                        GenericFileProvider.class.getName(),
                        file);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriFromFile, "application/vnd.google-earth.kml+xml");
            intent.putExtra("com.google.earth.EXTRA.tour_feature_id", "my_track");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            onPause();
            flagEarth = true;
            showGoogleEarthDialog();
        }
    }

    /* Request updates at startup */
    // if the user does not have google earth prompt to download it
    private void showGoogleEarthDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
        downloadDialog.setTitle(R.string.InstalaGE);
        downloadDialog
                .setMessage(R.string.RequiereGoogleEarth);
        downloadDialog.setPositiveButton(R.string.Si,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri
                                .parse("market://search?q=pname:com.google.earth");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        onPause();
                        flagEarth = false;
                    }
                });
        downloadDialog.setNegativeButton(R.string.No,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        downloadDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuGPS:
                Intent i = new Intent();
                i.setClass(Install_sinapseActivity.this, PreferencesActivity.class);
                startActivity(i);
                return true;
	        /*case R.id.MnuActualizar:
	            Intent i2 = new Intent();
	            i2.setClass(Install_sinapseActivity.this, Install_sinapseActivity.class);
	            startActivity(i2);
	            finish();
	            return true;*/

            default:
                return super.onOptionsItemSelected(item);
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
                        SubirJSON();
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

    // Comprueba que el IMEI del terminal esta en mi lista
    private boolean compruebaIMEI() {
        String[] IMEI = getResources().getStringArray(R.array.IMEI);
        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Install_sinapseActivity.this,
                android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(Install_sinapseActivity.this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    READ_PHONE_STATE_AND_CHECK_IMEI_PERMISSION);
        } else {
            deviceId = telephonyManager.getDeviceId();
            int i = 0;
            for (i = 0; i < IMEI.length; ++i) {
                if (IMEI[i].equals(deviceId)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Crea una alerta de que el IMEI del terminal no es correcto
    private void alertaIMEI() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Su terminal no permite el uso de esta aplicaci�n. "
                        + "P�ngase en contacto con Sinapse Energ�a")
                .setCancelable(false)
                .setPositiveButton("Llamar a Soporte Sinapse",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (ContextCompat.checkSelfPermission(Install_sinapseActivity.this,
                                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // Permission is not granted
                                    // No explanation needed, we can request the permission.
                                    ActivityCompat.requestPermissions(Install_sinapseActivity.this,
                                            new String[]{android.Manifest.permission.CALL_PHONE},
                                            CALL_PHONE_PERMISSION);
                                } else {
                                    callSupportPhone();
                                }
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    private void callSupportPhone() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:668879004")));
        finish();
    }

    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    public boolean createFile(String nombrefichero, String cabecera) {
        boolean flag = false;

        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
        // Permission has already been granted
        deviceId = telephonyManager.getDeviceId();
        String fecha = ((new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(new Date())).toString());

        if (!isExternalStorageWritable()) {
            Toast.makeText(this, R.string.TarjetaNoMontada, Toast.LENGTH_LONG).show();
        } else {
            File nmea_file;
            File root = Environment.getExternalStorageDirectory();
            FileWriter nmea_writer = null;
            try {
                // create a File object for the parent directory
                File logsDirectory = new File(root + "/sinapse/install/");
                // have the object build the directory structure, if needed.
                logsDirectory.mkdirs();

                nmea_file = new File(logsDirectory, nombrefichero + "-" + deviceId + "-" + fecha + ".kml");
                // Almaceno en la cadena fichero la ruta del fichero que
                // tendremos que subir al FTP
                //GlobalClass.FicheroKML = logsDirectory + "/" + nombrefichero + "-" + GlobalClass.global_localiz + "-" + deviceId +"-"+fecha+ ".kml";
                GlobalClass.FicheroKML = logsDirectory + "/" + nombrefichero + "-" + deviceId + "-" + fecha + ".kml";
                if (!nmea_file.exists()) {
                    flag = nmea_file.createNewFile();
                    Toast.makeText(this, R.string.FicheroKMLNoExiste, Toast.LENGTH_LONG).show();

                    nmea_writer = new FileWriter(nmea_file, true);
                    CharSequence nmea = cabecera;

                    nmea_writer.append(nmea);
                    nmea_writer.flush();
                }
            } catch (IOException ex) {
                Toast.makeText(this, R.string.NoESCTarjeta, Toast.LENGTH_LONG).show();
                Toast.makeText(this, ex.getMessage().toString() + R.string.fichero + GlobalClass.FicheroKML, Toast.LENGTH_LONG).show();
            } finally {
                if (nmea_writer != null) {
                    try {
                        nmea_writer.close();
                    } catch (IOException e) {
                        Toast.makeText(this, R.string.ErrorFichero, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        return flag;
    }

    public void lanzaavisoSubir() {
        AlertDialog.Builder popup = new AlertDialog.Builder(this);
        popup.setTitle("Subir archivos");
        popup.setMessage("�Deseas realizar la acci�n?");
        popup.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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
                    SubirJSON();
                } else {
                    Toast.makeText(getApplicationContext(), "No puedes subir los archivos, no tienes conexi�n", Toast.LENGTH_LONG).show();
                }

            }
        });
        popup.setNegativeButton("Cancelar", null);
        popup.show();
    }

    // Reinicia las variables globales
    private void reiniciavariables() {
        GlobalClass.longitud = 0.00000000;
        GlobalClass.latitud = 0.00000000;
        GlobalClass.address = "";
        GlobalClass.global_RadioAntigua = "";
        GlobalClass.global_RadioNueva = "";
        GlobalClass.idradioantigua = 0;
        GlobalClass.idradionueva = 0;
        GlobalClass.coordmapa = false;
        //GlobalClass.codigo_generico_EC = false;
        //GlobalClass.codigo_generico_Balast = false;
        //GlobalClass.global_ECM_LumSust = "0";
        GlobalClass.global_EC = "";
        GlobalClass.global_MC = "";
        GlobalClass.global_buscaEC = false;
        GlobalClass.global_buscaCMC = false;
        GlobalClass.global_buscaPA = false;
        GlobalClass.global_remplazo = false;
        GlobalClass.global_buscaOtro = false;
        GlobalClass.global_BE = "";
        GlobalClass.global_DE = "";
        GlobalClass.global_Balast = "";
        GlobalClass.global_Balast2 = "";
        GlobalClass.cont_balasto = 0;
        GlobalClass.cont_punto = 0;
        GlobalClass.global_CMC = "";
        GlobalClass.global_PC = "";
        GlobalClass.global_BR = "";
        GlobalClass.global_Otro = "";

        GlobalClass.nombre_punto = "";
        GlobalClass.alturaDisp = 0;
        GlobalClass.id_node = "";
        GlobalClass.id_cuadro = "";
        GlobalClass.circuito = 0;
        GlobalClass.regulacion = "";
        GlobalClass.medida = false;
        GlobalClass.cambio_bombilla = false;

        GlobalClass.tipo_punto = "";
        GlobalClass.tipo_puntoAux = "";
        GlobalClass.balasto_id = "";
        GlobalClass.balasto_marca = "";
        GlobalClass.balasto_potencia = 0;
        GlobalClass.tipo_balasto = "";
        GlobalClass.balasto_perdida = 0;
        GlobalClass.balasto2_id = "";
        GlobalClass.balasto2_marca = "";
        GlobalClass.balasto2_potencia = 0;
        GlobalClass.tipo_balasto2 = "";
        GlobalClass.balasto2_perdida = 0;
        GlobalClass.lumBalast = "";

        try {
            GlobalClass.punto_luz_id = idPunto();
            GlobalClass.cuadro_id = idCuadro();
            GlobalClass.id_puntoAcceso = idPA();
            GlobalClass.id_otroDispositivo = idOtro();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        GlobalClass.punto_luz_nombre = "";
        GlobalClass.punto_luz_altura = 0.0;
        GlobalClass.punto_luz_cuadro = "";
        GlobalClass.punto_luz_circuito = 0;
        GlobalClass.soporte_luz_tipo = "";
        GlobalClass.luminaria_tipo = "";
        GlobalClass.luminaria_marca = "";
        GlobalClass.luminaria_modelo = "";
        GlobalClass.fuente_tipo = "";
        GlobalClass.fuente_luz_marca = "";
        GlobalClass.fuente_luz_modelo = "";
        GlobalClass.fuente_luz_potencia = 0;

        GlobalClass.cuadro_nombre = "";
        GlobalClass.cuadro_altura = 0.00;
        GlobalClass.cuadro_numero_suministro = "";
        GlobalClass.cuadro_comando = "";
        GlobalClass.cuadro_numero_circuitos = 0;

        GlobalClass.num_cir = 0;
        GlobalClass.cont_circuito = 0;
        GlobalClass.circuito_nombre = "";
        GlobalClass.circuito_numero = 0;
        GlobalClass.circuito_tipo = "";
        GlobalClass.circuito_telegestionado = false;

        GlobalClass.nom_puntoAcceso = "";

        GlobalClass.monitoring = false;
        GlobalClass.control = "";
        GlobalClass.rele_1 = "NO_CONECTA";
        GlobalClass.rele_2 = "NO_CONECTA";
        GlobalClass.rele_3 = "NO_CONECTA";
        GlobalClass.rele_4 = "NO_CONECTA";
        GlobalClass.rele_5 = "NO_CONECTA";
        GlobalClass.rele_6 = "NO_CONECTA";
        GlobalClass.rele_7 = "NO_CONECTA";

        GlobalClass.nom_otroDispositivo = "";

        GlobalClass.cuadros.clear();
        GlobalClass.pcs.clear();
        GlobalClass.brs.clear();
        GlobalClass.otros.clear();
        GlobalClass.circuitos.clear();
        GlobalClass.MC_BE.clear();
        GlobalClass.MC_3P.clear();
        GlobalClass.BE.clear();
        GlobalClass.DE.clear();
    }


    //------------------------------------------------------------------
    public void SubirJSON() {
        //Campos----------------------------------------------------

        directorio_archivos = logsDirectory.list();


        if (directorio_archivos != null && directorio_archivos.length == 0) {
            Toast.makeText(this, "No hay archivos almacenados", Toast.LENGTH_LONG).show();
        } else {
            for (int i = 0; i < directorio_archivos.length; ++i) {
                if (((directorio_archivos[i].contains("install")) || (directorio_archivos[i].contains("replace")) || (directorio_archivos[i].contains("backup"))) && !(directorio_archivos[i].contains("forzado"))) {
                    String f = directorio_archivos[i].substring(0, directorio_archivos[i].length() - 5);

                    File archivo_origen = new File(root + "/sinapse/install/" + directorio_archivos[i]);

                    File nuevo_archivo = new File(root + "/sinapse/install/" + f + "-forzado.json");

                    try {
                        copyFile(archivo_origen, nuevo_archivo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if ((directorio_archivos[i].contains("logInstall")) && !(directorio_archivos[i].contains("forzado"))) {
                    String f = directorio_archivos[i].substring(0, directorio_archivos[i].length() - 4);

                    File archivo_origen = new File(root + "/sinapse/install/" + directorio_archivos[i]);

                    File nuevo_archivo = new File(root + "/sinapse/install/" + f + "-forzado.txt");
                    try {
                        copyFile(archivo_origen, nuevo_archivo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (((directorio_archivos[i].contains("Mapa"))) && !(directorio_archivos[i].contains("forzado"))) {
                    String f = directorio_archivos[i].substring(0, directorio_archivos[i].length() - 4);

                    File archivo_origen = new File(root + "/sinapse/install/" + directorio_archivos[i]);

                    File nuevo_archivo = new File(root + "/sinapse/install/" + f + "-forzado.kml");
                    try {
                        copyFile(archivo_origen, nuevo_archivo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
        directorio_archivos = logsDirectory.list();


        Subir();

        //-----------------------------------------------------------
    }

    public void Subir() {
        new Subir().execute();
    }

    private class Subir extends AsyncTask<String, Float, Integer> {

        protected void onPreExecute() {
            archivos_subidos = true;
            Toast.makeText(Install_sinapseActivity.this, "Subiendo archivos...", Toast.LENGTH_SHORT).show();
        }

        protected Integer doInBackground(String... urls) {
            try {
                FTPClient ftpClient = new FTPClient();
                ftpClient.connect(InetAddress.getByName("ftp.sinapseenergia.com"), 21);
                ftpClient.login("trazabilidad", "napse1si");
                ftpClient.makeDirectory("/Instalacion-" + GlobalClass.global_localiz + "/FORZADOS");
                ftpClient.enterLocalPassiveMode();
                ftpClient.changeWorkingDirectory("/Instalacion-" + GlobalClass.global_localiz + "/FORZADOS");
                Log.i("Estado", "Conectado al servidor FTP");

                BufferedInputStream buffIn = null;
                //Aqui env�o los archivos creados
                for (int i = 0; i < directorio_archivos.length; ++i) {
                    if (directorio_archivos[i].contains("forzado")) {
                        File archivo = new File(root + "/sinapse/install/" + directorio_archivos[i]);
                        try {
                            buffIn = new BufferedInputStream(new FileInputStream(archivo));
                            ftpClient.enterLocalPassiveMode();
                            ftpClient.storeFile(archivo.getName().toString(), buffIn);
                            Log.i("Estado", "Subiendo archivo" + archivo.getName().toString());
                            buffIn.close();

                        } catch (SocketException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            archivos_subidos = false;
                        } catch (UnknownHostException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            archivos_subidos = false;
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            archivos_subidos = false;
                        }

                        if (archivos_subidos == true) {
                            Log.i("Estado", "Archivo" + archivo.getName().toString() + " subido correctamente");
                        } else {
                            Log.w("Estado", "Archivo" + archivo.getName().toString() + " no se ha subido");
                        }

                        archivo.delete();
                    }

                }


                ftpClient.logout();
                ftpClient.disconnect();

            } catch (Exception e) {
                Log.e("Error", "No se ha podido subir los ficheros");
                archivos_subidos = false;
                //Toast.makeText(Install_sinapseActivity.this, "Error...", Toast.LENGTH_SHORT).show();
            }

            return null;
        }


        protected void onPostExecute(Integer bytes) {
            if (archivos_subidos == true) {
                Toast.makeText(Install_sinapseActivity.this, "Archivo subidos satisfactoriamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Install_sinapseActivity.this, "Error al subir uno o m�s archivos", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //Metodo para copiar el contenido de un fichero a otro
    @SuppressWarnings("resource")
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel origen = null;
        FileChannel destino = null;
        try {
            origen = new FileInputStream(sourceFile).getChannel();
            destino = new FileOutputStream(destFile).getChannel();

            long count = 0;
            long size = origen.size();
            while ((count += destino.transferFrom(origen, count, size - count)) < size) ;
        } finally {
            if (origen != null) {
                origen.close();
            }
            if (destino != null) {
                destino.close();
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                lanzarAvisoSalir();
                return true;

        }
        return false;
    }

    public void lanzarAvisoSalir() {
		/*AlertDialog.Builder popup = new AlertDialog.Builder(this);
		popup.setTitle("Salir de la aplicaci�n");
		popup.setMessage("�Estas seguro que deseas realizar esta acci�n?");
		popup.setPositiveButton("S�", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id)
			{
				//Intent intent = new Intent();
				//intent.setClass(Install_sinapseActivity.this,Install_sinapseActivity.class);
				//startActivity(intent);
				//finish();
			}
		});
		popup.setNegativeButton("No", null);
		popup.show();*/
    }

    //funcion que recupera el numero de dispositivos del caso, punto, en las instalaciones realizadas.
    public int idPunto() throws UnsupportedEncodingException, FileNotFoundException {
        int id = 0;
        JsonReader reader = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        directorio_archivos = logsDirectory.list();

        if (directorio_archivos != null && directorio_archivos.length > 0) {
            for (int i = 0; i < directorio_archivos.length; i++) {
                if (directorio_archivos[i].contains("install-" + GlobalClass.global_localiz) && !(directorio_archivos[i].contains("forzado"))) {
                    reader = new JsonReader(new InputStreamReader(new FileInputStream(logsDirectory + "/" + directorio_archivos[i]), "UTF-8"));
                    Instalacion instalacion = gson.fromJson(reader, Instalacion.class);
                    id += instalacion.getPuntos().getMCBE().size() + instalacion.getPuntos().getMC3P().size() + instalacion.getPuntos().getDE().size() + instalacion.getPuntos().getBE().size();
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        return id;
    }

    //funcion que recupera el numero de dispositivos del caso, cuadro, en las instalaciones realizadas.
    public int idCuadro() throws UnsupportedEncodingException, FileNotFoundException {
        int id = 0;
        JsonReader reader = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        directorio_archivos = logsDirectory.list();

        if (directorio_archivos != null && directorio_archivos.length > 0) {
            for (int i = 0; i < directorio_archivos.length; i++) {
                if (directorio_archivos[i].contains("install-" + GlobalClass.global_localiz) && !(directorio_archivos[i].contains("forzado"))) {
                    reader = new JsonReader(new InputStreamReader(new FileInputStream(logsDirectory + "/" + directorio_archivos[i]), "UTF-8"));
                    Instalacion instalacion = gson.fromJson(reader, Instalacion.class);
                    id += instalacion.getCuadros().getCuadros().size();
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        return id;
    }

    //funcion que recupera el numero de dispositivos del caso, puntos de acceso, en las instalaciones realizadas.
    public int idPA() throws UnsupportedEncodingException, FileNotFoundException {
        int id = 0;
        JsonReader reader = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        directorio_archivos = logsDirectory.list();

        if (directorio_archivos != null && directorio_archivos.length > 0) {
            for (int i = 0; i < directorio_archivos.length; i++) {
                if (directorio_archivos[i].contains("install-" + GlobalClass.global_localiz) && !(directorio_archivos[i].contains("forzado"))) {
                    reader = new JsonReader(new InputStreamReader(new FileInputStream(logsDirectory + "/" + directorio_archivos[i]), "UTF-8"));
                    Instalacion instalacion = gson.fromJson(reader, Instalacion.class);
                    id += instalacion.getPuntos_Acceso().getPC().size() + instalacion.getPuntos_Acceso().getBR().size();
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        return id;
    }

    //funcion que recupera el numero de dispositivos del caso, otros dispositivos, en las instalaciones realizadas.
    public int idOtro() throws UnsupportedEncodingException, FileNotFoundException {
        int id = 0;
        JsonReader reader = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        directorio_archivos = logsDirectory.list();

        if (directorio_archivos != null && directorio_archivos.length > 0) {
            for (int i = 0; i < directorio_archivos.length; i++) {
                if (directorio_archivos[i].contains("install-" + GlobalClass.global_localiz) && !(directorio_archivos[i].contains("forzado"))) {
                    reader = new JsonReader(new InputStreamReader(new FileInputStream(logsDirectory + "/" + directorio_archivos[i]), "UTF-8"));
                    Instalacion instalacion = gson.fromJson(reader, Instalacion.class);
                    id += instalacion.getOtros_Dispositivo().size();
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        return id;
    }

    public String nombreInstalacion(String archivo) throws IOException {
        String cadena = "";
        String aux;
        FileReader f;
        f = new FileReader(archivo);

        BufferedReader b = new BufferedReader(f);
        while ((aux = b.readLine()) != null) {
            cadena = aux;
        }

        b.close();

        return cadena;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_PHONE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callSupportPhone();
                }
                return;
            }
            case READ_PHONE_STATE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewKlmFileInGoogleEarth();
                }
                return;
            }

            case WRITE_EXTERNAL_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    createFile("Mapa Sinapse Instalaciones", GlobalClass.CabeceraKML);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}

class GlobalClass extends Application {
    static int global_tipo;
    static String global_localiz = "";
    static String global_fichero = "";
    static String global_fichero_R = "";
    static String global_fichero_L = "";
    static String global_fichero_B = "";

    static String global_EC = "";
    static String global_MC = "";
    static String global_BE = "";
    static String global_DE = "";
    static String global_Balast = "";
    static String global_Balast2 = "";
    static int cont_balasto = 0;
    static int cont_punto = 0;
    static String global_CMC = "";
    static String global_PC = "";
    static String global_BR = "";
    static String global_Otro = "";

    static Boolean global_buscaEC = false;
    static Boolean global_buscaCMC = false;
    static Boolean global_buscaPA = false;
    static Boolean global_remplazo = false;
    static Boolean global_buscaOtro = false;
    static String comentario = "";
    static String global_RadioAntigua = "";
    static String global_RadioNueva = "";
    static int idradioantigua = 0;
    static int idradionueva = 0;

    static String FicheroKML = "Mapa Sinapse Instalaciones";
    static String CabeceraKML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n<Document>\n	<name>Android.kmz</name>\n	<open>1</open>\n	<Style id=\"sn_placemark_square\">\n		<IconStyle>\n			<color>ff0000ff</color>\n			<scale>1.2</scale>\n			<Icon>\n				<href>http://maps.google.com/mapfiles/kml/shapes/placemark_square.png</href>\n			</Icon>\n		</IconStyle>\n	</Style>\n	<StyleMap id=\"msn_placemark_circle\">\n		<Pair>\n			<key>normal</key>\n			<styleUrl>#sn_placemark_circle</styleUrl>\n		</Pair>\n		<Pair>\n			<key>highlight</key>\n			<styleUrl>#sh_placemark_circle_highlight</styleUrl>\n		</Pair>\n	</StyleMap>\n	<Style id=\"sn_placemark_circle\">\n		<IconStyle>\n			<color>ff0000ff</color>\n			<scale>1.2</scale>\n			<Icon>\n				<href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png</href>\n			</Icon>\n		</IconStyle>\n		<ListStyle>\n		</ListStyle>\n	</Style>\n	<Style id=\"sh_placemark_circle_highlight\">\n		<IconStyle>\n			<color>ff0000ff</color>\n			<scale>1.2</scale>\n			<Icon>\n				<href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>\n			</Icon>\n		</IconStyle>\n		<ListStyle>\n		</ListStyle>\n	</Style>\n	<StyleMap id=\"msn_placemark_square\">\n		<Pair>\n			<key>normal</key>\n			<styleUrl>#sn_placemark_square</styleUrl>\n		</Pair>\n		<Pair>\n			<key>highlight</key>			<styleUrl>#sh_placemark_square_highlight</styleUrl>\n		</Pair>\n	</StyleMap>\n	<Style id=\"sh_placemark_square_highlight\">\n		<IconStyle>\n			<color>ff0000ff</color>\n			<scale>1.2</scale>\n			<Icon>\n				<href>http://maps.google.com/mapfiles/kml/shapes/placemark_square_highlight.png</href>\n			</Icon>\n		</IconStyle>\n	</Style>\n		<Folder>\n		<name>" + ((new SimpleDateFormat("yyyy-M-d")).format(new Date()))
            .toString() + "</name>\n <open>1</open>\n		</Folder>\n</Document>\n</kml>\n";

    static boolean coordmapa = false; // Usada para saber cuando las coordenadas
    // se extraen del mapa de google maps
    static double longitud = 0.00000000;
    static double latitud = 0.00000000;
    static String address = "";


    //variables Dispositivos valores comunes
    static String nombre_punto = "";
    static double alturaDisp = 0;
    static String id_node = "";
    static String id_cuadro = "";
    static boolean medida = false;

    //variables Dispositivo punto
    static String tipo_punto = "";
    static String tipo_puntoAux = "";
    static int circuito = 0;
    static String regulacion = "";
    static boolean cambio_bombilla = false;
    static String balasto_id = "";
    static String balasto_marca = "";
    static int balasto_potencia = 0;
    static String tipo_balasto = "";
    static int balasto_perdida = 0;
    static String balasto2_id = "";
    static String balasto2_marca = "";
    static int balasto2_potencia = 0;
    static String tipo_balasto2 = "";
    static int balasto2_perdida = 0;
    static String lumBalast = "";

    //variables Dispositivo Cuadro
    static boolean monitoring = false;
    static String control = "";
    static String rele_1 = "NO_CONECTA";
    static String rele_2 = "NO_CONECTA";
    static String rele_3 = "NO_CONECTA";
    static String rele_4 = "NO_CONECTA";
    static String rele_5 = "NO_CONECTA";
    static String rele_6 = "NO_CONECTA";
    static String rele_7 = "NO_CONECTA";

    //variables Dispositivo Punto acceso
    static int id_puntoAcceso = 0;
    static String nom_puntoAcceso = "";
    static String ip1 = null;
    static String protocolo1 = null;
    static String puerto11 = null;
    static String puerto12 = null;
    static String ip2 = null;
    static String protocolo2 = null;
    static String puerto21 = null;
    static String puerto22 = null;
    static String dirfisica = null;

    //variables Otro dispositivo
    static int id_otroDispositivo = 0;
    static String nom_otroDispositivo = "";

    //variables Punto_Fisico_Luminaria
    static int punto_luz_id = 0;
    static String punto_luz_nombre = "";
    static double punto_luz_altura;
    static String punto_luz_vida_util_fabricante = null;
    static String punto_luz_cuadro = "";
    static int punto_luz_circuito = 0;
    static String soporte_luz_tipo = "";
    static String luminaria_tipo = "";
    static String luminaria_marca = "";
    static String luminaria_modelo = "";
    static String fuente_tipo = "";
    static String fuente_luz_marca = "";
    static String fuente_luz_modelo = "";
    static int fuente_luz_potencia;
    static String punto_fecha_toma_datos = null;

    //variables Punto_Fisico_Cuadro
    static int cuadro_id = 0;
    static String cuadro_nombre = "";
    static double cuadro_altura = 0.00;
    static String cuadro_numero_suministro = "";
    static String cuadro_potencia_instalada = null;
    static String cuadro_potencia_contratada = null;
    static String cuadro_comando = "";
    static String cuadro_reloj_marca = null;
    static String cuadro_reloj_modelo = null;
    static int cuadro_numero_circuitos = 0;
    static String cuadro_fecha_toma_datos = null;
    //variables Circuito
    static int num_cir;
    static int cont_circuito = 0;
    static String circuito_nombre = "";
    static int circuito_numero = 0;
    static String circuito_tipo = "";
    static boolean circuito_telegestionado = false;
    static String circuito_tipo_conductor = null;
    static String circuito_seccion_conductor = null;
    static String circuito_tipo_canalizacion = null;
    static ArrayList<String> tipo_rele = new ArrayList<String>();

    static ArrayList<OtroDispositivo> otros = new ArrayList<OtroDispositivo>();
    static ArrayList<Remplazo> bajas = new ArrayList<Remplazo>();
    static ArrayList<Circuito> circuitos = new ArrayList<Circuito>();
    static ArrayList<MCBE> MC_BE = new ArrayList<MCBE>();
    static ArrayList<MC3P> MC_3P = new ArrayList<MC3P>();
    static ArrayList<BALASTOTELEGESTIONADO> BE = new ArrayList<BALASTOTELEGESTIONADO>();
    static ArrayList<DRIVERTELEGESTIONADO> DE = new ArrayList<DRIVERTELEGESTIONADO>();
    static ArrayList<Cmc> cuadros = new ArrayList<Cmc>();
    static ArrayList<Pc> pcs = new ArrayList<Pc>();
    static ArrayList<Br> brs = new ArrayList<Br>();
}


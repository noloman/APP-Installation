package install.sinapse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

public class ComienzoInstalacion_activity  extends Activity{
	ImageButton puntoBtn;
	ImageButton cuadroBtn;
	ImageButton punto_accesoBtn;
	//ImageButton punto_atrasBtn;
	ImageButton finalizarBtn;
	ImageButton otroBtn;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.instalacion);
		
		puntoBtn = (ImageButton)findViewById(R.id.puntoBtn);
		cuadroBtn = (ImageButton)findViewById(R.id.cuadroBtn);
		punto_accesoBtn = (ImageButton)findViewById(R.id.punto_accesoBtn);
		//punto_atrasBtn = (ImageButton)findViewById(R.id.cancelarBtn);
		finalizarBtn = (ImageButton)findViewById(R.id.finalizarBtn);
		otroBtn = (ImageButton)findViewById(R.id.otroBtn);
		
		this.puntoBtn.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				reiniciavalores();
				GlobalClass.global_buscaEC=true;
				GlobalClass.global_buscaCMC=false;
				GlobalClass.global_buscaPA=false;
				GlobalClass.global_buscaOtro = false;
				
				GlobalClass.global_tipo = 1;
				
				Intent intent = new Intent();
				intent.setClass(ComienzoInstalacion_activity.this,Gps_activity.class);
				startActivity(intent);
				finish();
			}
		});
		
		this.cuadroBtn.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				reiniciavalores();
				GlobalClass.global_buscaCMC=true;
				GlobalClass.global_buscaEC=false;
				GlobalClass.global_buscaPA=false;
				GlobalClass.global_buscaOtro = false;
				
				GlobalClass.global_tipo = 2;
				
				Intent intent = new Intent();
				intent.setClass(ComienzoInstalacion_activity.this,Gps_activity.class);
				startActivity(intent);
				finish();
			}
		});
		
		this.punto_accesoBtn.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				reiniciavalores();
				GlobalClass.global_buscaPA=true;
				GlobalClass.global_buscaEC=false;
				GlobalClass.global_buscaCMC=false;
				GlobalClass.global_buscaOtro = false;
				
				GlobalClass.global_tipo = 3;
				
				Intent intent = new Intent();
				intent.setClass(ComienzoInstalacion_activity.this,Gps_activity.class);
				startActivity(intent);
				finish();
			}
		});
		
		this.otroBtn.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				reiniciavalores();
				GlobalClass.global_buscaOtro = true;
				GlobalClass.global_buscaPA=false;
				GlobalClass.global_buscaEC=false;
				GlobalClass.global_buscaCMC=false;
				
				GlobalClass.global_tipo = 4;
				
				Intent intent = new Intent();
				intent.setClass(ComienzoInstalacion_activity.this,Gps_activity.class);
				startActivity(intent);
				finish();
			}
		});
		
		/*this.punto_atrasBtn.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				lanzarAviso();
			}
		});*/
		
		this.finalizarBtn.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{	
				lanzarAviso2();
			}
		});
		
	}
		
	//Metodo para subir ficheros FTP
	public void uploadFTP(String file) 
	{
		org.apache.commons.net.ftp.FTPClient con = new org.apache.commons.net.ftp.FTPClient();
		try 
		{
			con.connect("ftp.sinapseenergia.com");
			if (con.login("trazabilidad", "napse1si")) 
			{
				//creamos en el servidor una carpeta con el nombre del lugar de instalaci�n
				con.makeDirectory("/Instalacion-"+GlobalClass.global_localiz); 
				con.enterLocalPassiveMode(); // important!
				/*
				 * String data = file; ByteArrayInputStream in = new
				 * ByteArrayInputStream(data.getBytes());
				 */
				con.changeWorkingDirectory("/Instalacion-"+GlobalClass.global_localiz);
			
				BufferedInputStream buffIn=null;
				File fichero = new File(file);
				
				 buffIn=new BufferedInputStream(new FileInputStream(fichero));
			       con.enterLocalPassiveMode();
			       boolean result =  con.storeFile(fichero.getName().toString(), buffIn);
			       Log.i("Estado","Subiendo archivo"+fichero.getName().toString());
			       buffIn.close();
				if (result) 
				{
					Toast.makeText(this, "FTP actualizado", Toast.LENGTH_LONG).show();
				}

			}
		} 
		catch (Exception e) 
		{
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			try 
			{
				con.connect("ftp.tgame.es");
				if (con.login("tgame_7626990", "napse1si")) 
				{
					con.enterLocalPassiveMode(); // important!
					/*
					 * String data = file; ByteArrayInputStream in = new
					 * ByteArrayInputStream(data.getBytes());
					 */
					
					con.changeWorkingDirectory("/Trazabilidad");
				
					File fichero = new File(file);
					FileInputStream input = new FileInputStream(fichero);
					con.setFileType(FTP.BINARY_FILE_TYPE);

					boolean result = con.storeFile(fichero.getName(), input);
					input.close();
					if (result) 
					{
						Toast.makeText(this, "FTP actualizado",Toast.LENGTH_LONG).show();
					}

				}
			} 
			catch (Exception ex) 
			{
				Toast.makeText(this, ex.getMessage().toString(),Toast.LENGTH_LONG).show();
			}
		}

		try 
		{
			con.logout();
			con.disconnect();
		} 
		catch (Exception e) 
		{
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	public boolean createFile(String tipo, String objJson) 
	{
		boolean flag = false;
		  String fecha = ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",Locale.GERMANY)).format(new Date())).toString();
		  String fech = fecha.substring(0, 19);
		  //String fecha = ((new SimpleDateFormat("yyyy-MM-dd",Locale.GERMANY).format(new Date())).toString());

		String srvcName = Context.TELEPHONY_SERVICE;
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
		String deviceId = telephonyManager.getDeviceId();
		
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
		{
			Toast.makeText(this, "Tarjeta SD no montada", Toast.LENGTH_LONG).show();
		} 
		else 
		{
			File nmea_file;
			File root = Environment.getExternalStorageDirectory();
			FileWriter nmea_writer = null;
			try 
			{
				// create a File object for the parent directory
				File logsDirectory = new File(root + "/sinapse/install/");
				// have the object build the directory structure, if needed.
				logsDirectory.mkdirs();

				nmea_file = new File(logsDirectory, fech + "-install-" + tipo + "-" + deviceId + ".json");
				
				// Almaceno en la cadena fichero la ruta del fichero que
				// tendremos que subir al FTP
				GlobalClass.global_fichero = logsDirectory + "/" + fech + "-install-" + tipo + "-" + deviceId + ".json";

				if (!nmea_file.exists()) 
				{
					flag = nmea_file.createNewFile();
					Toast.makeText(this,"El fichero no existia, por lo que se ha creado",Toast.LENGTH_LONG).show();

					nmea_writer = new FileWriter(nmea_file, true);
					CharSequence nmea = objJson;

					nmea_writer.append(nmea);
					nmea_writer.flush();
				}
			} 
			catch (IOException ex) 
			{
				Toast.makeText(this,"Imposible escribir en la tarjeta de memoria",Toast.LENGTH_LONG).show();
				Toast.makeText(this, ex.getMessage().toString(),Toast.LENGTH_LONG).show();
			} 
			finally 
			{
				if (nmea_writer != null) 
				{
					try 
					{
						nmea_writer.close();
					} 
					catch (IOException e) 
					{
						Toast.makeText(this, "Error cerrando el fichero",Toast.LENGTH_LONG).show();
					}
				}
			}
		}

		return flag;

	}
	
		public void alertaSubir()
		{
			//Recogemos el servicio ConnectivityManager
			//el cual se encarga de todas las conexiones del terminal
			ConnectivityManager conMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			//Recogemos el estado del 3G
			//como vemos se recoge con el par�metro 0
			State internet_movil = conMan.getNetworkInfo(0).getState();
			//Recogemos el estado del wifi
			//En este caso se recoge con el par�metro 1
			State wifi = conMan.getNetworkInfo(1).getState();
			//Miramos si el internet 3G est� conectado o conectandose...
			if (internet_movil == NetworkInfo.State.CONNECTED|| internet_movil == NetworkInfo.State.CONNECTING) 
			{
			     ///////////////
			     //El movil est� conectado por 3G
			     //En este ejemplo mostrar�amos mensaje por pantalla
			     //Toast.makeText(getApplicationContext(), "Conectado por 3G", Toast.LENGTH_LONG).show();
			     //Si no esta por 3G comprovamos si est� conectado o conectandose al wifi...
			    Log.i("Estado","Subiendo archivos por 3G"); 
			    alerta3G();
			} 
			else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) 
			{
			     ///////////////
			     //El movil est� conectado por WIFI
			     //En este ejemplo mostrar�amos mensaje por pantalla
			     //Toast.makeText(getApplicationContext(), "Conectado por WIFI", Toast.LENGTH_LONG).show();
				Log.i("Estado","Subiendo archivos por WIFI"); 
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No puedes subir los archivos, no tienes conexi�n", Toast.LENGTH_LONG).show();
			}
		}
		// Crea una alerta de que no hay coordenadas GPS correctas
			private void alerta3G() 
			{
							AlertDialog.Builder builder = new AlertDialog.Builder(this);
					 
					        builder.setMessage("Est�s conectado por 3G. El proceso puede tardar un tiempo. �Desea continuar?")
					        .setTitle("Confirmacion")
					        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  
					        {
					               public void onClick(DialogInterface dialog, int id) 
					               {
					                    Log.i("Dialogos", "Confirmacion Aceptada.");
					                    
					                    dialog.cancel();
					               }
					        })
					        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() 
					        {
					               public void onClick(DialogInterface dialog, int id) 
					               {
					                    Log.i("Dialogos", "Confirmacion Cancelada.");
					                    dialog.cancel();
					               }
					        });
					        
					        AlertDialog alert = builder.create();
							alert.show();
			}
			
			private void reiniciavalores() 
			{

				GlobalClass.longitud = 0.00000000;
				GlobalClass.latitud = 0.00000000;
				GlobalClass.address = "";
				GlobalClass.global_RadioAntigua = "";
				GlobalClass.coordmapa = false;
				GlobalClass.global_EC = "";
				GlobalClass.global_MC = "";
				GlobalClass.global_BE="";
				GlobalClass.global_DE="";
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
				
				GlobalClass.monitoring = false;
				GlobalClass.control = "";
				GlobalClass.rele_1 = "NO_CONECTA";
				GlobalClass.rele_2 = "NO_CONECTA";
				GlobalClass.rele_3 = "NO_CONECTA";
				GlobalClass.rele_4 = "NO_CONECTA";
				GlobalClass.rele_5 = "NO_CONECTA";
				GlobalClass.rele_6 = "NO_CONECTA";
				GlobalClass.rele_7 = "NO_CONECTA";
				
				GlobalClass.nom_puntoAcceso = "";
				
				GlobalClass.nom_otroDispositivo = "";
				
				GlobalClass.punto_luz_nombre = "";
				GlobalClass.punto_luz_altura = 0.0;
				GlobalClass.punto_luz_cuadro = "";
				GlobalClass.punto_luz_circuito = 0;
				GlobalClass.soporte_luz_tipo = "";
				GlobalClass.luminaria_tipo ="";
				GlobalClass.luminaria_marca ="";
				GlobalClass.luminaria_modelo = "";
				GlobalClass.fuente_tipo = "";
				GlobalClass.fuente_luz_marca = "";
				GlobalClass.fuente_luz_modelo = "";
				GlobalClass.fuente_luz_potencia = 0;
					
				GlobalClass.cuadro_nombre = "";
				GlobalClass.cuadro_altura = 0.00;
				GlobalClass.cuadro_numero_suministro = "";
				GlobalClass.cuadro_comando ="";
				GlobalClass.cuadro_numero_circuitos = 0;

				GlobalClass.num_cir = 0;
				GlobalClass.cont_circuito=0;
				GlobalClass.circuito_nombre = "";
				GlobalClass.circuito_numero = 0;
				GlobalClass.circuito_tipo = "";
				GlobalClass.circuito_telegestionado = false;
				
				GlobalClass.circuitos.clear();
				GlobalClass.tipo_rele.clear();
			}
			public boolean onKeyDown(int keyCode, KeyEvent event)
			{
				switch(keyCode)
				{
					case KeyEvent.KEYCODE_BACK:
						lanzarAviso();	
					return true;
				}
				return false;
			}
			
			private void mostrarLog() 
			{
				GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
		        String fecha = ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",Locale.GERMANY)).format(new Date())).toString();
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
		         
		        
		        Log.v("Datos Telefono","----");
		        Log.i("IMEI",deviceId);
		        if(version_device != null)
		        	Log.i("Version Dispositivo",version_device);
		        Log.i("Operador",operador);
		        Log.i("Red",red+"");
		        
		        Log.i("Board", Build.BOARD);
		        Log.i("Marca", Build.BRAND);
		        Log.i("Bootloader", Build.BOOTLOADER);
		        Log.i("Dispositivo", Build.DEVICE);
		        Log.i("Numero de Modelo", Build.MODEL);
		        Log.i("Producto", Build.PRODUCT);
		        Log.i("Hardware", Build.HARDWARE);
		        Log.i("Host", Build.HOST);
		        Log.i("FingerPrint", Build.FINGERPRINT);
		        Log.i("Manufacter",Build.MANUFACTURER);
		        Log.i("Serial", Build.SERIAL);
		        Log.i("Tags", Build.TAGS);
		        Log.i("Time", Build.TIME+"");
		        Log.i("Type", Build.TYPE);
		        Log.i("User", Build.USER);
		        Log.i("Kernel", System.getProperty("os.version"));
		        
		        Log.i("SDK Android", Build.VERSION.SDK);
		        Log.i("Version Android", Build.VERSION.RELEASE);
		        Log.i("Codename ANDROID", Build.VERSION.CODENAME);
		        Log.i("Version Banda Base ANDROID", Build.VERSION.INCREMENTAL);
		        
		        for(int i = 0; i < lista_sensores.size(); ++i)
		        {
		        	Log.i("Sensor n"+i,lista_sensores.get(i).getName().toString());
		        }
		       
		        Log.v("--------","----------");
			    Log.i("Fecha y Hora", fecha);
			    Log.i("Dia", fecha_dia);
			    Log.v("--------","----------");
			    Log.v("Variables Globales", "--------------------");
		        Log.i("Instalacion",GlobalClass.global_localiz);
		        
		        // create a File object for the parent directory
		        File root = Environment.getExternalStorageDirectory();
				File logsDirectory = new File(root + "/sinapse/install/");
				// have the object build the directory structure, if needed.
				if(logsDirectory.exists()==false)
				{
					logsDirectory.mkdirs();
				}
				String nombre_fichero = fecha_dia+"-logInstall-"+GlobalClass.global_localiz+"-"+deviceId+".txt";
				
				File fichero = new File(logsDirectory, nombre_fichero);
				GlobalClass.global_fichero_L = logsDirectory + "/" + nombre_fichero;
				if(fichero.exists()==false)
				{
					try 
					{
						fichero.createNewFile();
						CharSequence linea = "-----------------------------------------------------\n";
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
						String linea_log = "Instalaci�n "+GlobalClass.global_localiz + "\n"
		        				+"********************\n"+ 
		        			    "Fecha y hora: " +fecha + "\n"
		        					+ "IMEI" + deviceId + "\n" 
		        				+ "Marca del dispositivo: " + Build.BRAND + "\n"
		        				+ "Operador: " + operador + "\n"
		        				+ "Fichero de instalaci�n: " + GlobalClass.global_fichero + "\n"
		        				+ "-----------------------------------------------------\n"; 
		        	
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
		    		}
		}
		
		public void lanzarAviso()
		{
			AlertDialog.Builder popup = new AlertDialog.Builder(this);
			popup.setTitle("Salir sin guardar");
			popup.setMessage("Si sales de la instalaci�n sin guardar, se perder�n todos los progresos.");
			popup.setPositiveButton("S�", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id)
				{
					Intent intent = new Intent();
					intent.setClass(ComienzoInstalacion_activity.this,Install_sinapseActivity.class);
					startActivity(intent);
					finish();
				}
			});
			popup.setNegativeButton("No", null);
			popup.show();
		}
		
		public void lanzarAviso2()
		{
			AlertDialog.Builder popup = new AlertDialog.Builder(this);
			popup.setTitle("Guardar instalacion realizada");
			popup.setMessage("�Estas seguro que deseas realizar esta acci�n?");
			popup.setPositiveButton("S�", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id)
				{
					//CONVERTIMOS DE JAVA A JSON
					Punto p = new Punto(GlobalClass.MC_BE,GlobalClass.MC_3P,GlobalClass.BE,GlobalClass.DE);
					Cuadro c = new Cuadro(GlobalClass.cuadros);
					PuntoAcceso pa = new PuntoAcceso(GlobalClass.pcs,GlobalClass.brs);
					
					Instalacion instalacion= new Instalacion(GlobalClass.global_localiz,p,c,pa,GlobalClass.otros); 
					Gson prettyGson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
					//Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
					String formatoJSON = prettyGson.toJson(instalacion);
					
					StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
					//creamos el fichero json con los datos de la instalacion
					createFile(GlobalClass.global_localiz,formatoJSON);
					//creamos fichero txt 
					mostrarLog();
					 
					//a�ade dispositivos instalados al kml
					if(GlobalClass.MC_BE.size()>0)
					{
						for(int i=0; i<GlobalClass.MC_BE.size();i++)
							anadepuntokml(GlobalClass.MC_BE.get(i).getNOMBRE(),GlobalClass.MC_BE.get(i).getDIRECCION(), GlobalClass.MC_BE.get(i).getLONGITUD() ,GlobalClass.MC_BE.get(i).getLATITUD());
					}
					
					if(GlobalClass.MC_3P.size()>0)
					{
						for(int i=0; i<GlobalClass.MC_3P.size();i++)
							anadepuntokml(GlobalClass.MC_3P.get(i).getNOMBRE(),GlobalClass.MC_3P.get(i).getDIRECCION(), GlobalClass.MC_3P.get(i).getLONGITUD() ,GlobalClass.MC_3P.get(i).getLATITUD());
					}
					
					if(GlobalClass.BE.size()>0)
					{
						for(int i=0; i<GlobalClass.BE.size();i++)
							anadepuntokml(GlobalClass.BE.get(i).getNOMBRE(),GlobalClass.BE.get(i).getDIRECCION(), GlobalClass.BE.get(i).getLONGITUD() ,GlobalClass.BE.get(i).getLATITUD());
					}
					
					if(GlobalClass.DE.size()>0)
					{
						for(int i=0; i<GlobalClass.DE.size();i++)
							anadepuntokml(GlobalClass.DE.get(i).getNOMBRE(),GlobalClass.DE.get(i).getDIRECCION(), GlobalClass.DE.get(i).getLONGITUD() ,GlobalClass.DE.get(i).getLATITUD());
					}
					
					if(GlobalClass.cuadros.size()>0)
					{
						for(int i=0; i<GlobalClass.cuadros.size();i++)
							anadepuntokml(GlobalClass.cuadros.get(i).getNOMBRE(),GlobalClass.cuadros.get(i).getDIRECCION(),GlobalClass.cuadros.get(i).getLONGITUD(),GlobalClass.cuadros.get(i).getLATITUD());
					}
					
					if(GlobalClass.brs.size()>0)
					{
						for(int i=0; i<GlobalClass.brs.size();i++)
							anadepuntokml(GlobalClass.brs.get(i).getNOMBRE(),GlobalClass.brs.get(i).getDIRECCION(),GlobalClass.brs.get(i).getLONGITUD(),GlobalClass.brs.get(i).getLATITUD());
					}
					
					if(GlobalClass.pcs.size()>0)
					{
						for(int i=0; i<GlobalClass.pcs.size();i++)
							anadepuntokml(GlobalClass.pcs.get(i).getNOMBRE(),GlobalClass.pcs.get(i).getDIRECCION(),GlobalClass.pcs.get(i).getLONGITUD(),GlobalClass.pcs.get(i).getLATITUD());
					}
					
					if(GlobalClass.otros.size()>0)
					{
						for(int i=0; i<GlobalClass.otros.size();i++)
							anadepuntokml(GlobalClass.otros.get(i).getNombre(),GlobalClass.global_localiz,GlobalClass.otros.get(i).getLongitud(),GlobalClass.otros.get(i).getLatitud());
					}
					
					//Luego se suben al FTP
					uploadFTP(GlobalClass.global_fichero);
					uploadFTP(GlobalClass.global_fichero_L);
					uploadFTP(GlobalClass.FicheroKML);
					Intent intent = new Intent();
					intent.setClass(ComienzoInstalacion_activity.this,Install_sinapseActivity.class);
					startActivity(intent);
					finish();
				}
			});
			popup.setNegativeButton("No", null);
			popup.show();
		}
		
		public boolean anadepuntokml(String Identificador, String Descripcion, double longitud ,double latitud ) 
		{
			
			boolean flag = false;
			
			String srvcName = Context.TELEPHONY_SERVICE;
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
			String deviceId = telephonyManager.getDeviceId();
			String fecha = ((new SimpleDateFormat("yyyy-MM-dd",Locale.GERMANY).format(new Date())).toString());

			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
			{
				Toast.makeText(this, R.string.TarjetaNoMontada, Toast.LENGTH_LONG).show();
			} 
			else 
			{
				File nmea_file;
				File root = Environment.getExternalStorageDirectory();
				FileWriter nmea_writer = null;
				try 
				{
					// create a File object for the parent directory
					File logsDirectory = new File(root + "/sinapse/install/");
					// have the object build the directory structure, if needed.
					logsDirectory.mkdirs();

					//nmea_file = new File(logsDirectory, "Mapa Sinapse Instalaciones" + "-" + GlobalClass.global_localiz + "-" + deviceId +"-"+fecha+ ".kml");
					nmea_file = new File(logsDirectory, "Mapa Sinapse Instalaciones" + "-" + deviceId +"-"+fecha+ ".kml");
					if (!nmea_file.exists()) 
					{
						Toast.makeText(this,R.string.KMLnoexiste,Toast.LENGTH_LONG).show();
					}

					long startPosition = nmea_file.length() - 29;

			   String placemark = "<Placemark>\n<name>" + Identificador
			     + "</name>\n<description>" + Descripcion 
			     + "</description>\n<styleUrl>#msn_placemark_square</styleUrl>\n" + "<Point><coordinates>"
			     + String.valueOf(longitud) + ","
			     + String.valueOf(latitud) 
			     + "</coordinates>\n</Point>\n</Placemark>\n</Folder>\n</Document>\n</kml>\n";

			   RandomAccessFile raf = new RandomAccessFile(nmea_file, "rw");
			   raf.seek(startPosition);
			   raf.write(placemark.getBytes());
			   raf.close();
			   
			   
						
				} 
				catch (IOException ex) 
				{
					Toast.makeText(this,R.string.NoESCTarjeta,Toast.LENGTH_LONG).show();
					Toast.makeText(this, ex.getMessage().toString(),Toast.LENGTH_LONG).show();
				} 
				finally 
				{
					if (nmea_writer != null) 
					{
						try 
						{
							nmea_writer.close();
						} 
						catch (IOException e) 
						{
							Toast.makeText(this, R.string.ErrorFichero,
									Toast.LENGTH_LONG).show();
						}
					}
				}
			}

			return flag;

		}
}
	

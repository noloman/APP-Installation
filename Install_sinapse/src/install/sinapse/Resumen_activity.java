// En esta actividad se indica el resumen global del punto de la instalacion
// se llama a la funcion para crear los ficheros y enviarlos al FTP

package install.sinapse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;

import install.sinapse.GlobalClass;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class Resumen_activity extends Activity {

	Spinner spinner1;
	Spinner spinner2;
	TextView textEC;
	TextView textBalast;
	TextView textPot;
	TextView textLum;
	ImageButton continueButton;
	TextView textView1;
	TextView textView4; //Para indicar el punto de la instalacion

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		StrictMode.setThreadPolicy(
				new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.resume_layout);
		final String fecha = ((new SimpleDateFormat("yyyy-M-d"))
				.format(new Date())).toString();

		Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		TextView textEC = (TextView) findViewById(R.id.textEC);
		TextView textBalast = (TextView) findViewById(R.id.textBalast);
		TextView textPot = (TextView) findViewById(R.id.textPot);
		TextView textLum = (TextView) findViewById(R.id.textLum);
		ImageButton continueButton = (ImageButton) findViewById(R.id.continueButton);
		TextView textView1 = (TextView) findViewById(R.id.textView1);
		TextView textView4 = (TextView) findViewById(R.id.textView4);
		
		//Como ya estoy con los datos del balasto, lo siguiente que tendré que escanear
		//es el EC seguro.
		GlobalClass.global_buscaEC=true;
		
		
		((ImageButton) this.findViewById(R.id.continueButton))
				.setVisibility(View.INVISIBLE);
		textEC.setText(GlobalClass.global_EC);
		
		textView4.setText(R.string.Resumen + GlobalClass.idPunto);

		if (GlobalClass.marcaBalast.compareTo("Sinapse") == 0) {
			textBalast.setText(GlobalClass.global_Balast);
			// Miro si el balasto es un Lumlux, si lo es cojo la potencia del
			// codigo d barras
			if (GlobalClass.global_Balast.substring(0, 2).compareTo("LU") == 0) {
				GlobalClass.marcaBalast = "LumLux";
				((Spinner) this.findViewById(R.id.spinner1))
						.setVisibility(View.INVISIBLE);
				((TextView) this.findViewById(R.id.textView1))
						.setVisibility(View.INVISIBLE);
				String potencia[] = GlobalClass.global_Balast.split("_");

				// Y ahora tengo que diferencias las potencias de los Lumlux,
				// si es de 70 o de las demas potencias
				if (potencia[3].substring(2, 3).compareTo("7") == 0) {
					GlobalClass.potBalast = potencia[3].substring(2, 4);
				} else {
					GlobalClass.potBalast = potencia[3].substring(2, 5);
				}
			}
		} else {
			textBalast.setText(GlobalClass.marcaBalast);
		}

		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.potbalast_array,
				android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter1);

		spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener1());

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.lamp_array, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);

		spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener2());

		((ImageButton) this.findViewById(R.id.continueButton))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

						
						String lineaEC = "";
						String lineaBalast = "";
						// Creo los ficheros en la tarjeta de memoria del
						// telefono para
						// añadir
						// las lineas
						creaficheros();

						// Relleno las lineas correspondientes para el balasto y
						// para el
						// easycontrol
						if (GlobalClass.marcaBalast.compareTo("Sinapse") == 0) {
							lineaBalast = ";" + GlobalClass.global_Balast +";" + GlobalClass.global_EC 
									+ ";;1;" + ";"+ GlobalClass.idPunto + ";"
											+ GlobalClass.global_localiz + ";" + fecha + "\n";
							lineaEC = ";" + GlobalClass.global_EC + ";"
									+ GlobalClass.potBalast + ";"
									+ GlobalClass.global_Balast + ";" + fecha
									+ ";" + GlobalClass.lumBalast + ";"
									+ GlobalClass.global_localiz + ";"
									+ GlobalClass.longitud + ";"
									+ GlobalClass.latitud + ";"
									+ GlobalClass.address + ";;1;;Sinapse;;" + ";" + GlobalClass.idPunto + ";" + GlobalClass.global_RadioAntigua
									+ "\n";

						} else if (GlobalClass.marcaBalast.compareTo("LumLux") == 0) {
							lineaBalast = ";" + GlobalClass.global_Balast+";" + GlobalClass.global_EC 
									+ ";;1;" + ";"+ GlobalClass.idPunto + ";"
											+ GlobalClass.global_localiz + ";" + fecha
											+ "\n";
							lineaEC = ";" + GlobalClass.global_EC + ";"
									+ GlobalClass.potBalast + ";"
									+ GlobalClass.global_Balast + ";" + fecha
									+ ";" + GlobalClass.lumBalast + ";"
									+ GlobalClass.global_localiz + ";"
									+ GlobalClass.longitud + ";"
									+ GlobalClass.latitud + ";"
									+ GlobalClass.address + ";;1;;Sinapse;;" + ";"+ GlobalClass.idPunto + ";" + GlobalClass.global_RadioAntigua
									+ "\n";
						}

						else {
							lineaEC = ";" + GlobalClass.global_EC + ";"
									+ GlobalClass.potBalast + ";;" + fecha
									+ ";" + GlobalClass.lumBalast + ";"
									+ GlobalClass.global_localiz + ";"
									+ GlobalClass.longitud + ";"
									+ GlobalClass.latitud + ";"
									+ GlobalClass.address + ";;1;;"
									+ GlobalClass.marcaBalast + ";;"
									+ GlobalClass.tipoBalast + ";"+ GlobalClass.idPunto + ";"+ GlobalClass.global_RadioAntigua + "\n";
						}
						
						
						
						StrictMode.setThreadPolicy(
						new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
						// or .detectAll() for all detectable 
						//problems.penaltyLog().build());
						
						
						// anado la linea con el registro correspondiente si es
						// un
						// balasto
						// sinapse, sino solo tengo que añadir la del
						// easycontrol
				
						/// **************************************////
						//// ****** DESHABILITO LA SUBIDA AL FTP ////
						/// *  comentando las lineas de uploadFTP * //
						/// **************************************////
						
						if (GlobalClass.marcaBalast.compareTo("Sinapse") == 0) {
							anaderegistro("balast", lineaBalast);
					    uploadFTP(GlobalClass.global_ficheroBalast);
						}

						if (GlobalClass.marcaBalast.compareTo("LumLux") == 0) {
							anaderegistro("balast", lineaBalast);
						uploadFTP(GlobalClass.global_ficheroBalast);
						}
						
						if (GlobalClass.global_EC.substring(0, 2).compareTo("EC") == 0)
						anaderegistro("easycontrol", lineaEC);
						else
							anaderegistro("radiosfrancesa", lineaEC);
						uploadFTP(GlobalClass.global_ficheroEC);

												
						Intent intent = new Intent();
						intent.setClass(Resumen_activity.this,
								Install_sinapseActivity.class);
						startActivity(intent);
						finish();
					}

				});
	}

	public class MyOnItemSelectedListener1 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			// A la hora de poner la potencia del balasto, si es un lumlux
			// directamente
			// lo voy a tomar de la referencia, pero si es un balasto sin marca
			// o es sinapse, entonces tendré que cogerlo del spinner1

			// Primero miro que el balasto sea Sinapse para poder comprobar si
			// es lumlux
			// Ojo, los balastos lumlux en un ppio son sinapse, pero luego les
			// pongo la marca
			// cuando les leo el codigo de barras
			if (GlobalClass.marcaBalast.compareTo("LumLux") != 0) {
					GlobalClass.potBalast = parent.getItemAtPosition(pos)
						.toString();
				}
			

			//

			actualizatexto();
		
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public class MyOnItemSelectedListener2 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			GlobalClass.lumBalast = parent.getItemAtPosition(pos).toString();
			actualizatexto();
			// textLum.setText(parent.getItemAtPosition(pos).toString());
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public void actualizatexto() {
		String selecciona = getResources().getString(R.string.Selecciona);
		if (GlobalClass.lumBalast.compareTo(selecciona) != 0) {
			((TextView) this.findViewById(R.id.textLum))
					.setText(GlobalClass.lumBalast);
			((TextView) this.findViewById(R.id.textLum))
					.setTextColor(getResources().getColor(R.color.dark_green));
		} else {
			((TextView) this.findViewById(R.id.textLum))
					.setTextColor(getResources().getColor(R.color.black));
		}
		if (GlobalClass.potBalast.compareTo(selecciona) != 0) {
			((TextView) this.findViewById(R.id.textPot))
					.setText(GlobalClass.potBalast + " W");
			((TextView) this.findViewById(R.id.textPot))
					.setTextColor(getResources().getColor(R.color.dark_green));
			if (GlobalClass.lumBalast.compareTo("Selecciona:") != 0) {
				((ImageButton) this.findViewById(R.id.continueButton))
						.setVisibility(View.VISIBLE);
			} else {
				((ImageButton) this.findViewById(R.id.continueButton))
						.setVisibility(View.INVISIBLE);
			}

		} else {
			((TextView) this.findViewById(R.id.textPot))
					.setTextColor(getResources().getColor(R.color.black));

		}

	}

	public void lanzaaviso2() {
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		popup.setTitle(R.string.OpcionCorrectoPorfavor);
		popup.setMessage(R.string.OpcionCorrectoPorfavor);
		popup.setPositiveButton("Ok", null);
		popup.show();
	}

	public boolean createFile(String tipo, String cabecera) {
		boolean flag = false;
		String fecha = ((new SimpleDateFormat("yyyy-M-d")).format(new Date()))
				.toString();

		String srvcName = Context.TELEPHONY_SERVICE;
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
		String deviceId = telephonyManager.getDeviceId();
		
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, R.string.TarjetaNoMontada, Toast.LENGTH_LONG)
					.show();
		} else {
			File nmea_file;
			File root = Environment.getExternalStorageDirectory();
			FileWriter nmea_writer = null;
			try {
				// create a File object for the parent directory
				File logsDirectory = new File(root + "/sinapse/install/");
				// have the object build the directory structure, if needed.
				logsDirectory.mkdirs();

				nmea_file = new File(logsDirectory, fecha + "-install-" + tipo + "-" + deviceId 
						+ ".csv");
				// Almaceno en la cadena fichero la ruta del fichero que
				// tendremos que subir al FTP
				if (tipo.compareTo("balast") == 0) {
					GlobalClass.global_ficheroBalast = logsDirectory + "/"
							+ fecha + "-install-" + tipo + "-" + deviceId + ".csv";
				} else {
					GlobalClass.global_ficheroEC = logsDirectory + "/" + fecha
							+ "-install-" + tipo + "-" + deviceId + ".csv";
				}

				if (!nmea_file.exists()) {
					flag = nmea_file.createNewFile();
					Toast.makeText(this,
							R.string.FicheroNoExiste,
							Toast.LENGTH_LONG).show();

					nmea_writer = new FileWriter(nmea_file, true);
					CharSequence nmea = cabecera;

					nmea_writer.append(nmea);
					nmea_writer.flush();
				}
			} catch (IOException ex) {
				Toast.makeText(this,
						R.string.NoESCTarjeta,
						Toast.LENGTH_LONG).show();
				Toast.makeText(this, ex.getMessage().toString(),
						Toast.LENGTH_LONG).show();
			} finally {
				if (nmea_writer != null) {
					try {
						nmea_writer.close();
					} catch (IOException e) {
						Toast.makeText(this, R.string.ErrorFichero,
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}

		return flag;

	}

	public boolean anaderegistro(String tipo, String linea) {
		boolean flag = false;
		String fecha = ((new SimpleDateFormat("yyyy-M-d")).format(new Date()))
				.toString();
		
		String srvcName = Context.TELEPHONY_SERVICE;
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
		String deviceId = telephonyManager.getDeviceId();
		
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, R.string.TarjetaNoMontada, Toast.LENGTH_LONG)
					.show();
		} else {
			File nmea_file;
			File root = Environment.getExternalStorageDirectory();
			FileWriter nmea_writer = null;
			try {
				// create a File object for the parent directory
				File logsDirectory = new File(root + "/sinapse/install/");
				// Almaceno en la cadena fichero la ruta del fichero que
				// tendremos que subir al FTP
				if (tipo.compareTo("balast") == 0) {
					GlobalClass.global_ficheroBalast = logsDirectory + "/"
							+ fecha + "-install-" + tipo + "-" + deviceId + ".csv";
				} else {
					GlobalClass.global_ficheroEC = logsDirectory + "/" + fecha
							+ "-install-" + tipo +  "-" + deviceId + ".csv";
				}
				// have the object build the directory structure, if needed.
				logsDirectory.mkdirs();

				nmea_file = new File(logsDirectory, fecha + "-install-" + tipo +  "-" + deviceId
						+ ".csv");
				if (!nmea_file.exists()) {
					flag = nmea_file.createNewFile();
					Toast.makeText(this,
							R.string.ErrorFichero,
							Toast.LENGTH_LONG).show();
				}
				nmea_writer = new FileWriter(nmea_file, true);
				CharSequence nmea = linea;

				nmea_writer.append(nmea);
				nmea_writer.flush();
			} catch (IOException ex) {
				Toast.makeText(this,
						R.string.NoESCTarjeta,
						Toast.LENGTH_LONG).show();
				Toast.makeText(this, ex.getMessage().toString(),
						Toast.LENGTH_LONG).show();
			} finally {
				if (nmea_writer != null) {
					try {
						nmea_writer.close();
					} catch (IOException e) {
						Toast.makeText(this, R.string.ErrorFichero,
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}

		return flag;

	}

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

				File fichero = new File(file);
				FileInputStream input = new FileInputStream(fichero);
				con.setFileType(FTP.BINARY_FILE_TYPE);

				boolean result = con.storeFile(fichero.getName(), input);
				input.close();
				if (result) {

					Toast.makeText(this, R.string.FTPActualizado, Toast.LENGTH_LONG)
							.show();
				}

			}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG)
					.show();
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

						Toast.makeText(this,  R.string.FTPActualizado,
								Toast.LENGTH_LONG).show();
					}

				}
			} catch (Exception ex) {
				Toast.makeText(this, ex.getMessage().toString(),
						Toast.LENGTH_LONG).show();
			}
		}

		try {
			con.logout();
			con.disconnect();
		} catch (IOException e) {
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG)
					.show();
		}
	}

	public void creaficheros() {
		// Segun el tipo de elemento, llamo a crear el fichero
		// correspondiente y si
		// el fichero se ha creado
		if (createFile("easycontrol", GlobalClass.global_var1) == true) {
			Toast.makeText(this, R.string.CreadoEC, Toast.LENGTH_LONG);
		} else {
			Toast.makeText(this, R.string.NoCreadoEC,
					Toast.LENGTH_LONG);
		}
		
		if (createFile("radiosfrancesa", GlobalClass.global_var3) == true) {
			Toast.makeText(this, R.string.CreadoFrancesa, Toast.LENGTH_LONG);
		} else {
			Toast.makeText(this, R.string.NoCreadoFrancesa,
					Toast.LENGTH_LONG);
		}

		if (GlobalClass.marcaBalast.compareTo("Sinapse") == 0) {
			if (createFile("balast", GlobalClass.global_var2) == true) {
				Toast.makeText(this, R.string.CreadoBalast,
						Toast.LENGTH_LONG);
			} else {
				Toast.makeText(this, R.string.NoCreadoBalast,
						Toast.LENGTH_LONG);
			}
		}

		if (GlobalClass.marcaBalast.compareTo("LumLux") == 0) {
			if (createFile("balast", GlobalClass.global_var2) == true) {
				Toast.makeText(this, R.string.CreadoBalast,
						Toast.LENGTH_LONG);
			} else {
				Toast.makeText(this, R.string.NoCreadoBalast,
						Toast.LENGTH_LONG);
			}
		}
	}

}
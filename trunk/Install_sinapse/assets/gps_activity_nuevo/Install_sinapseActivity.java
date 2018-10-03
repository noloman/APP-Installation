// **** FUNCION PRINCIPAL DE LA APLICACION ****
// Autor: Celia Moreno 
// ********************************************
// Esta actividad llama a Gps_activity para añadir un nuevo punto de la instalacion
// Así mismo cuando se desea reemplazar una radio, se llama a la actividad Replace

package install.sinapse;

import install.sinapse.R;
import install.sinapse.GlobalClass;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Install_sinapseActivity extends Activity {

	// TextView cuadroText;
	// TextView numptosText;
	ImageButton addButton;
	ImageButton replaceButton;
	GlobalClass global = new GlobalClass();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		addButton = (ImageButton) findViewById(R.id.addButton);
		replaceButton = (ImageButton) findViewById(R.id.replaceButton);
		super.onCreate(savedInstanceState);

		// Compruebo el IMEI del terminal, si no está en mi lista lanzo un
		// mensaje y salgo de la aplicación

		boolean flagIMEI = compruebaIMEI();

		if (flagIMEI == false) {
			alertaIMEI();
		}

		this.addButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reiniciavariables();

				Intent intent = new Intent();
				intent.setClass(Install_sinapseActivity.this,
						Gps_activity.class);
				startActivity(intent);
				finish();
			}
		});

		this.replaceButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reiniciavariables();

				Intent intent = new Intent();
				intent.setClass(Install_sinapseActivity.this,
						Replace_Activity.class);
				startActivity(intent);
				finish();
			}
		});

		// this.replaceButton.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// Intent intent =new Intent();
		// intent.setClass (Install_sinapseActivity.this, Gps_activity.class);
		// startActivity(intent);
		// finish();
		// }
		// });

		// numptosText = (TextView) findViewById(R.id.numptosText);
		// cuadroText = (TextView) findViewById(R.id.cuadroText);
		// ButtonContinue = (ImageButton) findViewById(R.id.ButtonContinue);

		//
		// this.ButtonContinue.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// check_cuadro();
		// }
		// });
		// }
		//
		// void check_cuadro() {
		// if (cuadroText.getText().length() == 0) {
		// Toast.makeText(this,
		// "El nombre del cuadro no puede estar en blanco",
		// Toast.LENGTH_LONG).show();
		// }
		//
		// else {
		// if (cuadroText.getText().length() < 3) {
		// Toast.makeText(
		// this,
		// "El nombre del cuadro no puede tener menos de 3 caracteres",
		// Toast.LENGTH_LONG).show();
		// }
		// else{
		//
		// if (numptosText.getText().length() == 0) {
		// Toast.makeText(this, "Introduce el numero de puntos",
		// Toast.LENGTH_LONG).show();
		// }
		//
		// else
		// {
		// GlobalClass.cuadro= cuadroText.getText().toString();
		// GlobalClass.numradios =
		// Integer.parseInt(numptosText.getText().toString());
		// String numradio = Integer.toString(GlobalClass.global_count);
		// while (numradio.length()<3){
		// numradio="0" + numradio;
		// }
		//
		//
		// GlobalClass.radio= cuadroText.getText().toString() + "-" + numradio;
		//
		// AlertDialog.Builder popup=new AlertDialog.Builder(this);
		// popup.setTitle("El cuadro " + cuadroText.getText() + " tiene " +
		// numptosText.getText() + " puntos.");
		// popup.setMessage("¿Es correcto?.");
		// popup.setPositiveButton("Sí", new DialogInterface.OnClickListener(){
		// public void onClick(DialogInterface dialog, int id){
		// Intent intent =new Intent();
		// intent.setClass (Install_sinapseActivity.this, Gps_activity.class);
		// startActivity(intent);
		// finish();
		//
		// }
		// });
		//
		// popup.setNegativeButton("No, quiero editarlo", null);
		// popup.show();
		//
		// }
		// }
		// }
	}

	/* Request updates at startup */
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

	// Comprueba que el IMEI del terminal esta en mi lista
	private boolean compruebaIMEI() {
		String[] IMEI = getResources().getStringArray(R.array.IMEI);
		String srvcName = Context.TELEPHONY_SERVICE;
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
		String deviceId = telephonyManager.getDeviceId();
		int i = 0;
		for (i = 0; i < IMEI.length; ++i) {
			if (IMEI[i].equals(deviceId)) {
				return true;
			}
		}

		return false;
	}

	// Crea una alerta de que el IMEI del terminal no es correcto

	private void alertaIMEI() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Su terminal no permite el uso de esta aplicación. Póngase en contacto con Sinapse Energía")
				.setCancelable(false)
				.setPositiveButton("Llamar a Soporte Sinapse",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								llamarSoporteSinapse();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void llamarSoporteSinapse() {
		try {
			startActivity(new Intent(Intent.ACTION_CALL,
					Uri.parse("tel:668879004")));
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();
	}

	// Reinicia las variables globales
	private void reiniciavariables() {

		GlobalClass.longitud = 0.00000000;
		GlobalClass.latitud = 0.00000000;

		GlobalClass.address = "";
		GlobalClass.idPunto = "";

		GlobalClass.global_RadioAntigua = "";
		GlobalClass.coordmapa = false;
	}
}

class GlobalClass extends Application {
	static String global_var1 = "id;ECF_Serial;ECM_Pot;ECM_BalastRef;ECM_FechaInst;ECM_Luminaria;ECM_IDInst;ECM_Long;ECM_Lat;ECM_Addr;ECM_RefRadioIns;ECM_Install;ECM_ECSust;ECM_BalMarca;ECM_BalMod;ECM_BalTipo;ECM_RefRadioIns;ECM_Otros\n";
	static String global_var2 = "id;BalF_Serial;BalM_IdRadio;BalF_Tipo;BalM_Install;BalM_BalSust;BalM_RefRadioIns;BalM_IDInst;BalM_FechaInst\n";
	static String global_var3 = "id;RFF_Serial;RFM_Pot;RFM_BalastRef;RFM_FechaInst;RFM_Luminaria;RFM_IDInst;RFM_Long;RFM_Lat;RFM_Addr;RFM_RefRadioIns;RFM_Install;RFM_ECSust;RFM_BalMarca;RFM_BalMod;RFM_BalTipo;RFM_RefRadioIns;RFM_Otros\n";
	
	static String global_tipo = "Tipo";
	static int global_count = 1;
	static String global_ficheroBalast = "";
	static String global_ficheroEC = "";
	static String global_localiz = "CALDE1";
	static String address = "";
	static String global_EC = "";
	static String global_RF = "";
	static String global_Balast = "";
	static String tipoBalast = "";
	static String marcaBalast = "";
	static Boolean global_buscaEC = true;
	static String radio = "";
	static double longitud = 0.00000000;
	static double latitud = 0.00000000;
	static String potBalast = "";
	static String lumBalast = "";
	static String instalacion = "";
	static String idradioantigua = "";
	static String idPunto = "";
	// static Boolean global_dispAntiguo = false; //este flag se usara para
	// saber si el dispositivo es el antiguo o el nuevo

	static String global_RadioAntigua = "";

	static boolean coordmapa = false; // Usada para saber cuando las coordenadas
										// se extraen del mapa de google maps

}

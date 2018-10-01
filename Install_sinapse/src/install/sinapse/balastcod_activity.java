package install.sinapse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class balastcod_activity extends Activity {

	Spinner spinner;
	ImageButton ButtonContinue;
	Spinner spinner2;
	Spinner spinner3;
	TextView textView3;
	TextView textView4;
	TextView RFrancesa;
	EditText IDRadioFrancesa;
	EditText IDPunto; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.balastcod);

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
		ButtonContinue = (ImageButton) findViewById(R.id.ButtonContinue);

		RFrancesa = (TextView) findViewById(R.id.RFrancesa);
		IDRadioFrancesa = (EditText) findViewById(R.id.IDRadioFrancesa);

		IDPunto = (EditText) findViewById(R.id.IDPunto);
		
		IDRadioFrancesa.setInputType(InputType.TYPE_CLASS_NUMBER);
		IDPunto.setInputType(InputType.TYPE_CLASS_TEXT);
		
		IDRadioFrancesa.setEnabled(false);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.balastos_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.tipo_array, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);

		spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener2());

		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
				this, R.array.marcas_array,
				android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner3.setAdapter(adapter3);

		spinner3.setOnItemSelectedListener(new MyOnItemSelectedListener3());

		desactivatextosincodigo();

		// Miramos a ver si lo que habiamos escaneado era una radio francesa
		// para que el usuario introduzca la id
		if (GlobalClass.global_EC.compareTo("RF-") == 0) {
			RFrancesa.setTextColor(0xFF000000);
			IDRadioFrancesa.setEnabled(true);
			
			RFrancesa.setVisibility(View.VISIBLE);
			IDRadioFrancesa.setVisibility(View.VISIBLE);
		} else {
			IDRadioFrancesa.setEnabled(false);
		
			RFrancesa.setVisibility(View.INVISIBLE);
			IDRadioFrancesa.setVisibility(View.INVISIBLE);
			
		}

		this.ButtonContinue.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String selecciona = getResources().getString(R.string.Selecciona);
				GlobalClass.idPunto = IDPunto.getText().toString();
				if (GlobalClass.tipoBalast.compareTo(selecciona) == 0) {
					lanzaaviso2();
				} else {
					if (GlobalClass.marcaBalast.compareTo(selecciona) == 0) {
						lanzaaviso2();
					}

					else {
						
						if (GlobalClass.idPunto.compareTo("") == 0){
							lanzaavisopunto();
						}
						else{
						Intent intent = new Intent();
						intent.setClass(balastcod_activity.this,
								Resumen_activity.class);
						startActivity(intent);
						finish();
						}
					}
				}
				
			}
		});
	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			String concodigo = getResources().getString(R.string.ConCodigo);
			String sincodigo = getResources().getString(R.string.SinCodigo);
			//Miro si tengo la radio francesa ya introducida, sino hago que la edite
			if ((IDRadioFrancesa.length() < 6)  && (GlobalClass.global_EC.compareTo("RF-") == 0)) {
				lanzaavisoradio();
			}

			else {
				if (GlobalClass.global_EC.compareTo("RF-") == 0){
				GlobalClass.global_EC = "RF_" + IDRadioFrancesa.getText().toString();	
				}
				if (parent.getItemAtPosition(pos).toString()
						.compareTo(concodigo) == 0) {

					desactivatextosincodigo();
					lanzaaviso();

				} else {
					if (parent.getItemAtPosition(pos).toString()
							.compareTo(sincodigo) == 0) {
						activatextosincodigo();
					}
				}
			}
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}

	}

	public void activatextosincodigo() {
		((Spinner) this.findViewById(R.id.spinner2))
				.setVisibility(View.VISIBLE);
		((TextView) this.findViewById(R.id.textView3))
				.setVisibility(View.VISIBLE);

	}

	public void activatextosincodigo2() {
		((Spinner) this.findViewById(R.id.spinner3))
				.setVisibility(View.VISIBLE);
		((TextView) this.findViewById(R.id.textView4))
				.setVisibility(View.VISIBLE);

	}

	public void activatextosincodigo3() {
		((ImageButton) this.findViewById(R.id.ButtonContinue))
				.setVisibility(View.VISIBLE);
	}

	public void desactivatextosincodigo() {
		((TextView) this.findViewById(R.id.textView3))
				.setVisibility(View.INVISIBLE);
		((TextView) this.findViewById(R.id.textView4))
				.setVisibility(View.INVISIBLE);
		((ImageButton) this.findViewById(R.id.ButtonContinue))
				.setVisibility(View.INVISIBLE);
		((Spinner) this.findViewById(R.id.spinner2))
				.setVisibility(View.INVISIBLE);
		((Spinner) this.findViewById(R.id.spinner3))
				.setVisibility(View.INVISIBLE);
	}

	public void lanzaaviso() {
		GlobalClass.idPunto = IDPunto.getText().toString();
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		String balastcodigo = getResources().getString(R.string.TipoBalasto);
		String balastselcodigo = getResources().getString(R.string.BalastoConCodigo);
		
		popup.setTitle(balastcodigo);
		popup.setMessage(balastselcodigo);
		popup.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				GlobalClass.marcaBalast = "Sinapse";
				Intent intent = new Intent();
				intent.setClass(balastcod_activity.this, Gps_activity.class);
				startActivity(intent);
				finish();
			}
		});
		popup.setNegativeButton(R.string.NoEdito, null);
		popup.show();
	}

	public class MyOnItemSelectedListener2 implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			String selecciona = getResources().getString(R.string.Selecciona);
			
			if (parent.getItemAtPosition(pos).toString()
					.compareTo(selecciona) == 0) {
			} else {
				activatextosincodigo2();
			}

			GlobalClass.tipoBalast = parent.getItemAtPosition(pos).toString();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public void lanzaavisoradio() {
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		String IdFrancesa = getResources().getString(R.string.IDRadioFrancesa);
		String NoRadioFrancesa = getResources().getString(R.string.NoRadioFrancesa);
		String NoEdito = getResources().getString(R.string.NoEdito);
		popup.setTitle(IdFrancesa);
		popup.setMessage(NoRadioFrancesa);
		popup.setPositiveButton(NoEdito, null);
		popup.show();
	}
	
	
	public void lanzaavisopunto() {
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		String IDIncorrecta=getResources().getString(R.string.IDIncorrecta);
		String NocodigoValido=getResources().getString(R.string.NocodigoValido);
		String NoEdito = getResources().getString(R.string.NoEdito);
		popup.setTitle(IDIncorrecta);
		popup.setMessage(NocodigoValido);
		popup.setPositiveButton(NoEdito, null);
		popup.show();
	}

	public class MyOnItemSelectedListener3 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			String selecciona = getResources().getString(R.string.Selecciona);
			if (parent.getItemAtPosition(pos).toString()
					.compareTo(selecciona) == 0) {
			} else {
				activatextosincodigo3();
			}
			GlobalClass.marcaBalast = parent.getItemAtPosition(pos).toString();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public void lanzaaviso2() {
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		
		String OpcionCorrecto = getResources().getString(R.string.OpcionCorrecto);
		String OpcionCorrectoPorfavor = getResources().getString(R.string.OpcionCorrectoPorfavor);
		popup.setTitle(OpcionCorrecto);
		popup.setMessage(OpcionCorrectoPorfavor);
		popup.setPositiveButton("Ok", null);
		popup.show();
	}

}

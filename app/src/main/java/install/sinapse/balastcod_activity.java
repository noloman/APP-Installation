package install.sinapse;

import install.sinapse.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class balastcod_activity extends Activity 
{

	Spinner spinner;
	ImageButton ButtonContinue;
	Spinner spinner2;
	Spinner spinner3;
	Spinner spinner4;
	Spinner spinner5;
	TextView textView3;
	TextView textView4;
	TextView textView5;
	TextView textView1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.balastcod);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
		Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
		ButtonContinue = (ImageButton) findViewById(R.id.ButtonContinue);
		textView1 =  (TextView) findViewById(R.id.textView1);
		
		GlobalClass.cont_punto++;
		
		//Sacamos del codigo de trazabilidad del modulo de comunicación el tipo de punto
		if(GlobalClass.tipo_punto.compareTo("")==0)
		{
			int tam =  GlobalClass.global_EC.length();
			
			switch(tam)
			{
			case 30:
				GlobalClass.tipo_punto = "SIMPLE";
				GlobalClass.tipo_puntoAux="SIMPLE";
				break;
			case 36: 
				if(GlobalClass.global_EC.substring(19, 20).compareTo("1")==0)
				{
					GlobalClass.tipo_punto = "SIMPLE";
					GlobalClass.tipo_puntoAux="SIMPLE";
				}
				else
				{
					GlobalClass.tipo_punto = "DOBLE";
					GlobalClass.tipo_puntoAux="DOBLE";
				}
				break;
			}
			
		}
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.balastos_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.tipo_balasto, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);
		if(GlobalClass.cont_punto==1)
			spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener2());
		else
			spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener2d());

		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
				this, R.array.marcas_array,
				android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner3.setAdapter(adapter3);
		if(GlobalClass.cont_punto==1)
			spinner3.setOnItemSelectedListener(new MyOnItemSelectedListener3());
		else
			spinner3.setOnItemSelectedListener(new MyOnItemSelectedListener3d());
		
		ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(
				this, R.array.potbalast_array,
				android.R.layout.simple_spinner_item);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner4.setAdapter(adapter4);
		if(GlobalClass.cont_punto==1)
			spinner4.setOnItemSelectedListener(new MyOnItemSelectedListener4());
		else
			spinner4.setOnItemSelectedListener(new MyOnItemSelectedListener4d());
		
		desactivatextosincodigo();
		
		if(GlobalClass.cont_punto==2)
		{
			//GlobalClass.tipo_puntoAux="DOBLE";
			textView1.setText("Selecciona otro balasto o driver:");
			((ImageButton) this.findViewById(R.id.ButtonContinue)).setVisibility(View.VISIBLE);
		}
		
		this.ButtonContinue.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				
				if(GlobalClass.tipo_punto.compareTo("DOBLE") == 0)
				{
					
					GlobalClass.tipo_punto="SIMPLE";
					Intent intent = new Intent(); 
					intent.setClass(balastcod_activity.this,balastcod_activity.class);
					startActivity(intent);
					finish();
				}
				else
				{
					/*if(GlobalClass.cont_punto==1)
						GlobalClass.tipo_puntoAux = GlobalClass.tipo_punto;*/
									
					Intent intent = new Intent();
					intent.setClass(balastcod_activity.this,formPunto_activity.class);
					startActivity(intent);
					finish();	
				}
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
			GlobalClass.global_buscaEC=true;
			GlobalClass.global_tipo = 1;
			
			if(GlobalClass.cont_punto==2)
				GlobalClass.cont_punto=GlobalClass.cont_punto-2;
			else
				GlobalClass.cont_punto--;
			
			GlobalClass.cont_balasto=0;
			GlobalClass.tipo_punto = "";
			GlobalClass.tipo_puntoAux = "";
			Intent intent = new Intent();
			intent.setClass(balastcod_activity.this,Gps_activity.class);
			startActivity(intent);
            finish();
			return true;
		}
		return false;
	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener 
	{

		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

			if (parent.getItemAtPosition(pos).toString().compareTo("Sinapse(con código qr)") == 0) 
			{
				desactivatextosincodigo();
				lanzaaviso();

			} 
			else 
			{
				if (parent.getItemAtPosition(pos).toString().compareTo("Otra marca(sin código qr)") == 0) 
				{
					activatextosincodigo();
				}
			}
		}

		public void onNothingSelected(AdapterView parent) 
		{
			// Do nothing.
		}

	}

	public void activatextosincodigo() 
	{
		((Spinner) this.findViewById(R.id.spinner2)).setVisibility(View.VISIBLE);
		((TextView) this.findViewById(R.id.textView3)).setVisibility(View.VISIBLE);

	}

	public void activatextosincodigo2() 
	{
		((Spinner) this.findViewById(R.id.spinner3)).setVisibility(View.VISIBLE);
		((TextView) this.findViewById(R.id.textView4)).setVisibility(View.VISIBLE);

	}
	
	public void activatextosincodigo3() 
	{
		((Spinner) this.findViewById(R.id.spinner4)).setVisibility(View.VISIBLE);
		((TextView) this.findViewById(R.id.textView5)).setVisibility(View.VISIBLE);

	}
	public void activatextosincodigo4() 
	{
		((ImageButton) this.findViewById(R.id.ButtonContinue)).setVisibility(View.VISIBLE);
	}

	public void desactivatextosincodigo() 
	{
		((TextView) this.findViewById(R.id.textView3)).setVisibility(View.INVISIBLE);
		((TextView) this.findViewById(R.id.textView4)).setVisibility(View.INVISIBLE);
		((TextView) this.findViewById(R.id.textView5)).setVisibility(View.INVISIBLE);
		((ImageButton) this.findViewById(R.id.ButtonContinue)).setVisibility(View.INVISIBLE);
		((Spinner) this.findViewById(R.id.spinner2)).setVisibility(View.INVISIBLE);
		((Spinner) this.findViewById(R.id.spinner3)).setVisibility(View.INVISIBLE);
		((Spinner) this.findViewById(R.id.spinner4)).setVisibility(View.INVISIBLE);
	}

	public void lanzaaviso() 
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		popup.setTitle("Tipo de balasto (con código qr)");
		popup.setMessage("Ha seleccionado el tipo de balasto con código qr ¿Es correcto?.");
		popup.setPositiveButton("Sí", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id)
			{	
				if(GlobalClass.cont_punto==1)
					GlobalClass.balasto_marca = "SINAPSE";
				else
					GlobalClass.balasto2_marca = "SINAPSE";
				
				Intent intent = new Intent();
				intent.setClass(balastcod_activity.this, Gps_activity.class);
				startActivity(intent);
				finish();
			}
		});
		popup.setNegativeButton("No, quiero editarlo", null);
		popup.show();
	}

	public class MyOnItemSelectedListener2 implements OnItemSelectedListener 
	{
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
		{
			if (parent.getItemAtPosition(pos).toString().compareTo("") == 0) 
			{
			} 
			else 
			{
				activatextosincodigo2();
			}
			GlobalClass.tipo_balasto = parent.getItemAtPosition(pos).toString();
			
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}
	
	public class MyOnItemSelectedListener2d implements OnItemSelectedListener 
	{
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
		{
			if (parent.getItemAtPosition(pos).toString().compareTo("") == 0) 
			{
			} 
			else 
			{
				activatextosincodigo2();
			}
			GlobalClass.tipo_balasto2 = parent.getItemAtPosition(pos).toString();
			
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public class MyOnItemSelectedListener3 implements OnItemSelectedListener 
	{

		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
		{
			if (parent.getItemAtPosition(pos).toString().compareTo("") == 0) 
			{
			} 
			else 
			{
				activatextosincodigo3();
			}
			
			GlobalClass.balasto_marca = parent.getItemAtPosition(pos).toString();
		}

		public void onNothingSelected(AdapterView parent) 
		{
			// Do nothing.
		}
	}
	
	public class MyOnItemSelectedListener3d implements OnItemSelectedListener 
	{

		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
		{
			if (parent.getItemAtPosition(pos).toString().compareTo("") == 0) 
			{
			} 
			else 
			{
				activatextosincodigo3();
			}
			
			GlobalClass.balasto2_marca = parent.getItemAtPosition(pos).toString();
		}

		public void onNothingSelected(AdapterView parent) 
		{
			// Do nothing.
		}
	}
	public class MyOnItemSelectedListener4 implements OnItemSelectedListener 
	{

		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
		{
			if (parent.getItemAtPosition(pos).toString().compareTo("0") == 0) 
			{
			} 
			else 
			{
				activatextosincodigo4();
			}
			
			GlobalClass.balasto_potencia = Integer.parseInt(parent.getItemAtPosition(pos).toString());
		}

		public void onNothingSelected(AdapterView parent) 
		{
			// Do nothing.
		}
	}
	
	public class MyOnItemSelectedListener4d implements OnItemSelectedListener 
	{

		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
		{
			if (parent.getItemAtPosition(pos).toString().compareTo("0") == 0) 
			{
			} 
			else 
			{
				activatextosincodigo4();
			}
	
			GlobalClass.balasto2_potencia = Integer.parseInt(parent.getItemAtPosition(pos).toString());
		}

		public void onNothingSelected(AdapterView parent) 
		{
			// Do nothing.
		}
	}
}

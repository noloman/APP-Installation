package install.sinapse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class formPunto_activity extends Activity{
	Spinner spinner;	
	Spinner spinner2;
	Spinner spinner3;
	
	EditText nombre;
	EditText altura;
	EditText vida_util;
	EditText cuadro;
	EditText circuito;
	EditText luminaria_marca;
	EditText luminaria_modelo;

	ImageButton continuar;
	ImageButton ButtonAtras;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.form_punto_fisico);
	
		continuar = (ImageButton) findViewById(R.id.continueButton);
		ButtonAtras = (ImageButton) findViewById(R.id.ButtonAtras);
		
		GlobalClass.global_tipo = 1; //ponemos a 1 para que lo siguiente que le sea un modulo de comunicacion
		GlobalClass.punto_luz_id++;
		
		nombre = (EditText) findViewById(R.id.nombre);
		altura = (EditText) findViewById(R.id.altura);
		cuadro = (EditText) findViewById(R.id.cuadro);
		circuito = (EditText) findViewById(R.id.circuito);
		luminaria_marca = (EditText) findViewById(R.id.luminaria_marca);
		luminaria_modelo = (EditText) findViewById(R.id.luminaria_modelo);

		
		nombre.setText(GlobalClass.punto_luz_nombre);
		if(GlobalClass.punto_luz_altura!=0)
			altura.setText(Double.toString(GlobalClass.punto_luz_altura));
		if(GlobalClass.punto_luz_cuadro.compareTo("CMXX") !=0)
			cuadro.setText(GlobalClass.punto_luz_cuadro);
		if(GlobalClass.punto_luz_circuito!=0)
			circuito.setText(Integer.toString(GlobalClass.punto_luz_circuito));
		luminaria_marca.setText(GlobalClass.luminaria_marca);
		luminaria_modelo.setText(GlobalClass.luminaria_modelo);
		
		nombre.setInputType(InputType.TYPE_CLASS_TEXT );
		altura.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		cuadro.setInputType(InputType.TYPE_CLASS_TEXT);
		circuito.setInputType(InputType.TYPE_CLASS_NUMBER);
		luminaria_marca.setInputType(InputType.TYPE_CLASS_TEXT);
		luminaria_modelo.setInputType(InputType.TYPE_CLASS_TEXT);

		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.soporte_tipo,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		spinner.setSelection(adapter.getPosition(GlobalClass.soporte_luz_tipo));
		
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.luminaria_tipo, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);
		spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener2());
		spinner2.setSelection(adapter2.getPosition(GlobalClass.luminaria_tipo));
		
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
				this, R.array.fuente_tipo,android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner3.setAdapter(adapter3);
		spinner3.setOnItemSelectedListener(new MyOnItemSelectedListener3());
		spinner3.setSelection(adapter3.getPosition(GlobalClass.fuente_tipo));
		
		this.continuar.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{		
		
				
				GlobalClass.punto_luz_nombre = nombre.getText().toString();
				
				if(altura.getText().toString().compareTo("") ==0)
					GlobalClass.punto_luz_altura = 0.0;
				else
					GlobalClass.punto_luz_altura= Double.parseDouble(altura.getText().toString());
				
				if(cuadro.getText().toString().compareTo("") ==0)
					GlobalClass.punto_luz_cuadro = "CMXX";
				else
					GlobalClass.punto_luz_cuadro = cuadro.getText().toString();
				
				if(circuito.getText().toString().compareTo("") ==0)
					GlobalClass.punto_luz_circuito = 0;
				else
					GlobalClass.punto_luz_circuito = Integer.parseInt(circuito.getText().toString());
				
				GlobalClass.luminaria_marca =luminaria_marca.getText().toString();
				GlobalClass.luminaria_modelo = luminaria_modelo.getText().toString();
				
						
				if (GlobalClass.punto_luz_nombre.compareTo("") == 0) 
				{
					lanzaaviso3();
				}
				else 
				{		
					Intent intent = new Intent();
					intent.setClass(formPunto_activity.this,Resumen_activity.class);
					startActivity(intent);
					finish();
				}			
			}
		});
		
		this.ButtonAtras.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				alertaCerrar();	
			}
		});
		
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener 
	{
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
		{
			GlobalClass.soporte_luz_tipo = parent.getItemAtPosition(pos).toString();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}
	
	public class MyOnItemSelectedListener2 implements OnItemSelectedListener 
	{
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
		{
			GlobalClass.luminaria_tipo = parent.getItemAtPosition(pos).toString();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}
	
	public class MyOnItemSelectedListener3 implements OnItemSelectedListener 
	{
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
		{
			GlobalClass.fuente_tipo = parent.getItemAtPosition(pos).toString();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}
	
	public void lanzaaviso3() 
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		popup.setTitle("Campo nombre vacío");
		popup.setMessage("Por favor, introduzca un nombre");
		popup.setPositiveButton("Ok", null);
		popup.show();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
			alertaCerrar();	
			return true;
		}
		return false;
	}
	public void alertaCerrar()
	{
	   GlobalClass.global_buscaEC=true;
       GlobalClass.punto_luz_id--;    
       GlobalClass.cont_punto=0;
       GlobalClass.cont_balasto=0;
       GlobalClass.tipo_punto = "";
       
       Intent intent = new Intent();
       intent.setClass(formPunto_activity.this,Gps_activity.class);
       startActivity(intent);
       finish();
                
               
        
        
	}
}

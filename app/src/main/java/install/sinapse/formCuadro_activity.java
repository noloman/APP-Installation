package install.sinapse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class formCuadro_activity extends Activity{
	EditText nombre;
	EditText altura;
	EditText num_suministro;
	EditText num_circuitos;
	EditText idcuadro;
	
	ImageButton ButtonContinue;
	ImageButton ButtonAtras;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.form_cuadro);
		
		ButtonContinue = (ImageButton) findViewById(R.id.continueButton);
		ButtonAtras = (ImageButton) findViewById(R.id.ButtonAtras);
		
		GlobalClass.cuadro_id++;
		
		nombre = (EditText) findViewById(R.id.nombre);
		altura = (EditText) findViewById(R.id.altura);
		num_suministro = (EditText) findViewById(R.id.num_suministro);
		num_circuitos = (EditText) findViewById(R.id.num_circuitos);
		idcuadro = (EditText) findViewById(R.id.idCuadro);
		
		nombre.setText(GlobalClass.cuadro_nombre);
		if(GlobalClass.punto_luz_altura!=0)
			altura.setText(Double.toString(GlobalClass.cuadro_altura));
		num_suministro.setText(GlobalClass.cuadro_numero_suministro);
		if(GlobalClass.cuadro_numero_circuitos != 0)
			num_circuitos.setText(Integer.toString(GlobalClass.cuadro_numero_circuitos));
		if(GlobalClass.id_cuadro.compareTo("CMXX")!=0)
			idcuadro.setText(GlobalClass.id_cuadro);
		
		
		nombre.setInputType(InputType.TYPE_CLASS_TEXT);
		altura.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		num_suministro.setInputType(InputType.TYPE_CLASS_TEXT);
		num_circuitos.setInputType(InputType.TYPE_CLASS_NUMBER);
		idcuadro.setInputType(InputType.TYPE_CLASS_TEXT);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.tipo_comando,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		spinner.setSelection(adapter.getPosition(GlobalClass.cuadro_comando));
				
		this.ButtonContinue.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{	
				GlobalClass.cuadro_nombre = nombre.getText().toString();
					
				if(altura.getText().toString().compareTo("") == 0)
					GlobalClass.cuadro_altura = 0.0;
				else
					GlobalClass.cuadro_altura = Double.parseDouble(altura.getText().toString());
				
				GlobalClass.cuadro_numero_suministro = num_suministro.getText().toString();
				
				if(num_circuitos.getText().toString().compareTo("") ==0)
					GlobalClass.cuadro_numero_circuitos = 0;
				else
					GlobalClass.cuadro_numero_circuitos= Integer.parseInt(num_circuitos.getText().toString());
				
				GlobalClass.num_cir = GlobalClass.cuadro_numero_circuitos;
				
				if(idcuadro.getText().toString().compareTo("") ==0)
					GlobalClass.id_cuadro = "CMXX";
				else
					GlobalClass.id_cuadro = idcuadro.getText().toString();
				
				if(GlobalClass.cuadro_nombre.compareTo("") == 0)
				{
					lanzaaviso();
				}
				else
				{
					if(GlobalClass.cuadro_numero_circuitos == 0)
					{
						Intent intent = new Intent();
						intent.setClass(formCuadro_activity.this,Resumen_activity.class);
						startActivity(intent);
						finish();
					}
					else
					{
						Intent intent = new Intent();
						intent.setClass(formCuadro_activity.this,formCircuito_activity.class);
						startActivity(intent);
						finish();
					}		
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
			GlobalClass.cuadro_comando = parent.getItemAtPosition(pos).toString();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
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
		GlobalClass.cuadro_id--;
		GlobalClass.global_CMC = "";
	    Intent intent = new Intent();
	    intent.setClass(formCuadro_activity.this,Gps_activity.class);
	    startActivity(intent);
	    finish();
	}
	
	public void lanzaaviso() 
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		popup.setTitle("Campo nombre vacío");
		popup.setMessage("Por favor, introduzca un nombre");
		popup.setPositiveButton("Ok", null);
		popup.show();
	}
	
}

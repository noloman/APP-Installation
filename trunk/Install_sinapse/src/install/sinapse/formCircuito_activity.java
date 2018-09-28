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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class formCircuito_activity extends Activity{
	TextView circuito;
	
	EditText circuito_nombre;
	EditText circuito_tipo;
	
	CheckBox reloj_telegestion;
	
	ImageButton ButtonContinue;
	ImageButton ButtonAtras;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.form_circuito);
	
		GlobalClass.cont_circuito++;
		ButtonContinue = (ImageButton) findViewById(R.id.continueButton);
		ButtonAtras = (ImageButton) findViewById(R.id.ButtonAtras);
		
		circuito = (TextView) findViewById(R.id.circuito);
		circuito.setText("Circuito "+GlobalClass.cont_circuito);
			
		reloj_telegestion = (CheckBox) findViewById(R.id.reloj_telegestion);
		circuito_nombre = (EditText) findViewById(R.id.circuito_nombre); 
		circuito_tipo = (EditText) findViewById(R.id.circuito_tipo);
		
		circuito_nombre.setInputType(InputType.TYPE_CLASS_TEXT);
		circuito_tipo.setInputType(InputType.TYPE_CLASS_TEXT);
				
		this.ButtonContinue.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{			
				if(GlobalClass.num_cir>1)
				{	
					GlobalClass.circuito_nombre = circuito_nombre.getText().toString();
								
					GlobalClass.circuito_numero= GlobalClass.cont_circuito;
					
					GlobalClass.circuito_tipo = circuito_tipo.getText().toString();
					
					if (circuito_nombre.getText().toString().compareTo("") == 0) 
					{
						lanzaaviso();
					} 
					else 
					{
						if (circuito_tipo.getText().toString().compareTo("") == 0) 
						{
							lanzaaviso2();
						}
						else 
						{		
							GlobalClass.tipo_rele.add(GlobalClass.circuito_nombre);
							
							Circuito c = new Circuito(GlobalClass.circuito_nombre,GlobalClass.circuito_numero,GlobalClass.circuito_tipo,
									GlobalClass.circuito_telegestionado,GlobalClass.circuito_tipo_conductor,
									GlobalClass.circuito_seccion_conductor,GlobalClass.circuito_tipo_canalizacion);
								
							GlobalClass.circuitos.add(c);
								
							GlobalClass.num_cir--;
							Intent intent = new Intent();
							intent.setClass(formCircuito_activity.this,formCircuito_activity.class);
							startActivity(intent);
							finish();				
						}
					}
					
				}
				else
				{
					GlobalClass.circuito_nombre = reemplazarCaracteresEspeciales(circuito_nombre.getText().toString());
					
					GlobalClass.circuito_numero= GlobalClass.cont_circuito;
				
					GlobalClass.circuito_tipo = reemplazarCaracteresEspeciales(circuito_tipo.getText().toString());
					if (circuito_nombre.getText().toString().compareTo("") == 0) 
					{
						lanzaaviso();
					} 
					else 
					{
						if (circuito_tipo.getText().toString().compareTo("") == 0) 
						{
							lanzaaviso2();
						}
						else 
						{	
							GlobalClass.tipo_rele.add(GlobalClass.circuito_nombre);
							
							Circuito c = new Circuito(GlobalClass.circuito_nombre,GlobalClass.circuito_numero,GlobalClass.circuito_tipo,
									GlobalClass.circuito_telegestionado,GlobalClass.circuito_tipo_conductor,
									GlobalClass.circuito_seccion_conductor,GlobalClass.circuito_tipo_canalizacion);
					
							GlobalClass.circuitos.add(c);
							Intent intent = new Intent();
							intent.setClass(formCircuito_activity.this,Resumen_activity.class);
							startActivity(intent);
							finish();
						}
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
		
		reloj_telegestion.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
			{
				if(arg1 == true)
					GlobalClass.circuito_telegestionado = true;
				else
					GlobalClass.circuito_telegestionado = false;
			}
			
		});
		
	}
	
	public void lanzaaviso() 
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		popup.setTitle("Campo nombre vacÌo");
		popup.setMessage("Por favor, introduzca un nombre");
		popup.setPositiveButton("Ok", null);
		popup.show();
	}
	
	public void lanzaaviso2() 
	{
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		popup.setTitle("Campo tipo vacÌo");
		popup.setMessage("Por favor, introduzca el tipo");
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
		GlobalClass.cont_circuito=0;
		GlobalClass.cuadro_id--;
	    Intent intent = new Intent();
	    intent.setClass(formCircuito_activity.this,formCuadro_activity.class);
	    startActivity(intent);
	    finish();
	}
	
	public String reemplazarCaracteresEspeciales(String input) {
	    String original = "·‡‰ÈËÎÌÏÔÛÚˆ˙˘uÒ¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹—Á«Ò—";
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcCnN";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }
	    return output;
	}
}



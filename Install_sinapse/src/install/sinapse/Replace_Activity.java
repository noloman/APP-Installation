// **** FUNCION PRINCIPAL DE LA APLICACION ****
// Autor: Celia Moreno 
// ********************************************
// Esta funcion se utiliza para tener la informacion de la radio sustituida

package install.sinapse;

import install.sinapse.R;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;


public class Replace_Activity extends Activity {


	ImageButton nextButton;
	EditText IDRadioAntigua;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.replace_layout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		nextButton = (ImageButton) findViewById(R.id.nextButton);
		IDRadioAntigua = (EditText) findViewById(R.id.IDRadioAntigua);
		
		super.onCreate(savedInstanceState);
		
		this.nextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
						
			GlobalClass.global_RadioAntigua = IDRadioAntigua.getText().toString();
						
			Intent intent =new Intent();
			intent.setClass (Replace_Activity.this, Gps_activity.class);
			startActivity(intent);
			finish();
			}
		});
		
	}
		

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		this.nextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
						
			GlobalClass.global_RadioAntigua = IDRadioAntigua.getText().toString();
						
			Intent intent =new Intent();
			intent.setClass (Replace_Activity.this, Gps_activity.class);
			startActivity(intent);
			finish();
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		this.nextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
						
			GlobalClass.global_RadioAntigua = R.string.Sustituye + IDRadioAntigua.getText().toString();
						
			Intent intent =new Intent();
			intent.setClass (Replace_Activity.this, Gps_activity.class);
			startActivity(intent);
			finish();
			}
		});
				
	}

	
	@Override
	protected void onStop() {
		super.onStop();
		
	}
	
}



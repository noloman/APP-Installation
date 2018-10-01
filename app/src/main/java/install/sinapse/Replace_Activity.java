// **** FUNCION PRINCIPAL DE LA APLICACION ****
// Autor: Celia Moreno 
// ********************************************
// Esta funcion se utiliza para tener la informacion de la radio sustituida

package install.sinapse;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class Replace_Activity extends Activity {

    ImageButton ButtonAtras;
    ImageButton scanButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.replace_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButtonAtras = (ImageButton) findViewById(R.id.ButtonAtras);
        scanButton = (ImageButton) findViewById(R.id.scanButton);

        super.onCreate(savedInstanceState);

        GlobalClass.global_remplazo = true;

        ((ImageButton) this.findViewById(R.id.scanButton)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                qrscan();
            }
        });

        this.ButtonAtras.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                alertaCerrar();
            }
        });

    }


    /* Request updates at startup */
	/*@Override
	protected void onResume() 
	{
		super.onResume();
		this.nextButton.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
						
				//GlobalClass.global_RadioAntigua = IDRadioAntigua.getText().toString();
						
				Intent intent = new Intent();
				intent.setClass (Replace_Activity.this, Gps_activity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		
		this.nextButton.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
						
				//GlobalClass.global_RadioAntigua = "Sustituye a: " + IDRadioAntigua.getText().toString();
						
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
		
	}*/

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                alertaCerrar();
                return true;
        }
        return false;
    }

    public void alertaCerrar() {
        GlobalClass.global_remplazo = false;

        Intent intent = new Intent();
        intent.setClass(Replace_Activity.this, Install_sinapseActivity.class);
        startActivity(intent);
        finish();
    }

    void qrscan() {
//		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//		startActivityForResult(intent, 0);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String contents = intent.getStringExtra("SCAN_RESULT");
            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

            GlobalClass.global_RadioAntigua = contents.toString();

            Intent intent1 = new Intent();
            intent1.setClass(Replace_Activity.this, Gps_activity.class);
            startActivity(intent1);
            finish();
        }
    }
}



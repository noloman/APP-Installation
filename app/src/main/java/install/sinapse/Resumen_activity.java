// En esta actividad se indica el resumen global del punto de la instalacion
// se llama a la funcion para crear los ficheros y enviarlos al FTP

package install.sinapse;

import install.sinapse.GlobalClass;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.net.ftp.FTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import install.sinapse.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class Resumen_activity extends Activity {
    TextView textEC;
    TextView textBalast;
    TextView textPot;
    TextView textLum;
    ImageButton continueButton;
    TextView textView1;
    TextView textView4; //Para indicar el punto de la instalacion
    CheckBox sustlum;
    Integer esustlum = 0;
    ImageButton anteriorButton;
    ImageButton atrasButton;

    EditText rele1;
    EditText rele2;
    EditText rele3;
    EditText rele4;
    EditText rele5;
    EditText rele6;
    EditText rele7;

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    Spinner spinner5;
    Spinner spinner6;
    Spinner spinner7;
    Spinner regulacion;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.resume_layout);

        anteriorButton = (ImageButton) findViewById(R.id.anteriorButton);
        atrasButton = (ImageButton) findViewById(R.id.ButtonAtras);
        continueButton = (ImageButton) findViewById(R.id.continueButton);

        TextView tControl = (TextView) findViewById(R.id.tControl);
        Spinner eControl = (Spinner) findViewById(R.id.eControl);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner5 = (Spinner) findViewById(R.id.spinner5);
        spinner6 = (Spinner) findViewById(R.id.spinner6);
        spinner7 = (Spinner) findViewById(R.id.spinner7);
        TextView textRegulacion = (TextView) findViewById(R.id.textRegulacion);
        regulacion = (Spinner) findViewById(R.id.regulacion);

        TextView textView5 = (TextView) findViewById(R.id.textView5);
        TextView textEC = (TextView) findViewById(R.id.textEC);
        TextView textBalast = (TextView) findViewById(R.id.textBalast);
        TextView textPot = (TextView) findViewById(R.id.textPot);
        TextView textLum = (TextView) findViewById(R.id.textLum);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        //TextView tBalasto = (TextView) findViewById(R.id.tBalasto);
        //final EditText eBalasto = (EditText) findViewById(R.id.eBalasto);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        TextView textBe = (TextView) findViewById(R.id.textBe);
        TextView textView8 = (TextView) findViewById(R.id.textView8);
        TextView textDe = (TextView) findViewById(R.id.textDe);
        TextView textView18 = (TextView) findViewById(R.id.textView18);
        TextView textCmc = (TextView) findViewById(R.id.textCmc);
        TextView textView19 = (TextView) findViewById(R.id.textView19);
        TextView textPc = (TextView) findViewById(R.id.textPc);
        TextView textView20 = (TextView) findViewById(R.id.textView20);
        TextView textBr = (TextView) findViewById(R.id.textBr);
        TextView textView21 = (TextView) findViewById(R.id.textView21);
        TextView textMc = (TextView) findViewById(R.id.textMc);
        TextView textView22 = (TextView) findViewById(R.id.textView22);
        TextView textView6 = (TextView) findViewById(R.id.textView6);
        sustlum = (CheckBox) findViewById(R.id.checkLumSust);
        TextView textView9 = (TextView) findViewById(R.id.textView9);
        TextView textViewBalast2 = (TextView) findViewById(R.id.textViewBalast2);
        TextView textBalast2 = (TextView) findViewById(R.id.textBalast2);
        TextView textViewPot2 = (TextView) findViewById(R.id.textViewPot2);
        TextView textPot2 = (TextView) findViewById(R.id.textPot2);
        TextView textViewPunto = (TextView) findViewById(R.id.textViewPunto);
        TextView textPunto = (TextView) findViewById(R.id.textPunto);
        TextView textViewOtro = (TextView) findViewById(R.id.textViewOtro);
        TextView textOtro = (TextView) findViewById(R.id.textOtro);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipo_control,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eControl.setAdapter(adapter);
        eControl.setOnItemSelectedListener(new MyOnItemSelectedListener());

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, GlobalClass.tipo_rele);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener1());

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, GlobalClass.tipo_rele);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener2());

        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, GlobalClass.tipo_rele);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new MyOnItemSelectedListener3());

        ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, GlobalClass.tipo_rele);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new MyOnItemSelectedListener4());

        ArrayAdapter adapter5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, GlobalClass.tipo_rele);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter5);
        spinner5.setOnItemSelectedListener(new MyOnItemSelectedListener5());

        ArrayAdapter adapter6 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, GlobalClass.tipo_rele);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner6.setAdapter(adapter6);
        spinner6.setOnItemSelectedListener(new MyOnItemSelectedListener6());

        ArrayAdapter adapter7 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, GlobalClass.tipo_rele);
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner7.setAdapter(adapter7);
        spinner7.setOnItemSelectedListener(new MyOnItemSelectedListener7());

        ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(
                this, R.array.tipo_regulacion,
                android.R.layout.simple_spinner_item);
        adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regulacion.setAdapter(adapter8);
        regulacion.setOnItemSelectedListener(new MyOnItemSelectedListener8());

        //Como ya estoy con los datos del balasto, lo siguiente que tendr� que escanear
        //es el EC seguro.
        GlobalClass.global_buscaEC = true;

        if (GlobalClass.global_buscaCMC == true || GlobalClass.global_buscaPA == true || GlobalClass.global_buscaOtro == true)
            sustlum.setVisibility(View.GONE);

        switch (GlobalClass.global_tipo) {
            case 1:
                if (GlobalClass.global_MC.compareTo("") != 0) {
                    textMc.setVisibility(View.VISIBLE);
                    textView22.setVisibility(View.VISIBLE);
                    textRegulacion.setVisibility(View.VISIBLE);
                    regulacion.setVisibility(View.VISIBLE);
                    textBalast.setVisibility(View.GONE);
                    textView7.setVisibility(View.GONE);
                    textEC.setVisibility(View.GONE);
                    textView5.setVisibility(View.GONE);
                    textViewPunto.setVisibility(View.GONE);
                    textPunto.setVisibility(View.GONE);
                    textView6.setVisibility(View.GONE);
                    textPot.setVisibility(View.GONE);
                    textMc.setText(GlobalClass.global_MC);
                    textViewOtro.setVisibility(View.GONE);
                    textOtro.setVisibility(View.GONE);
                } else {
                    if (GlobalClass.global_EC.compareTo("") != 0) {
                        if (GlobalClass.balasto_marca.compareTo("SINAPSE") != 0) {
                            textRegulacion.setVisibility(View.VISIBLE);
                            regulacion.setVisibility(View.VISIBLE);
                        } else {
                            textRegulacion.setVisibility(View.GONE);
                            regulacion.setVisibility(View.GONE);
                        }
                    } else {
                        if (GlobalClass.global_BE.compareTo("") != 0) {
                            textBe.setVisibility(View.VISIBLE);
                            textView8.setVisibility(View.VISIBLE);
                            textBalast.setVisibility(View.GONE);
                            textView7.setVisibility(View.GONE);
                            textEC.setVisibility(View.GONE);
                            textView5.setVisibility(View.GONE);
                            textViewPunto.setVisibility(View.GONE);
                            textPunto.setVisibility(View.GONE);
                            textBe.setText(GlobalClass.global_BE);
                            GlobalClass.balasto_potencia = Integer.parseInt(GlobalClass.global_BE.substring(22, 25));
                            textViewOtro.setVisibility(View.GONE);
                            textOtro.setVisibility(View.GONE);
                        } else {
                            if (GlobalClass.global_DE.compareTo("") != 0) {
                                textDe.setVisibility(View.VISIBLE);
                                textView18.setVisibility(View.VISIBLE);
                                textBalast.setVisibility(View.GONE);
                                textView7.setVisibility(View.GONE);
                                textEC.setVisibility(View.GONE);
                                textView5.setVisibility(View.GONE);
                                textDe.setText(GlobalClass.global_DE);
                                textViewPunto.setVisibility(View.GONE);
                                textPunto.setVisibility(View.GONE);
                                GlobalClass.balasto_potencia = Integer.parseInt(GlobalClass.global_DE.substring(22, 25));
                                textViewOtro.setVisibility(View.GONE);
                                textOtro.setVisibility(View.GONE);
                            }
                        }

                    }
                }

                GlobalClass.nombre_punto = reemplazarCaracteresEspeciales(GlobalClass.punto_luz_nombre + "_" + GlobalClass.punto_luz_id);
                textLum.setText(GlobalClass.fuente_tipo);
                textView4.setText("Resumen del punto: " + GlobalClass.nombre_punto);

                if (GlobalClass.global_Balast2.compareTo("") == 0) {
                    textViewBalast2.setVisibility(View.GONE);
                    textBalast2.setVisibility(View.GONE);
                    textViewPot2.setVisibility(View.GONE);
                    textPot2.setVisibility(View.GONE);
                }

                textEC.setText(GlobalClass.global_EC);
                textPunto.setText(GlobalClass.tipo_puntoAux);

                if (GlobalClass.balasto_marca.compareTo("SINAPSE") == 0) {
                    textBalast.setText(GlobalClass.global_Balast);
                    // Miro si el balasto es un Lumlux, si lo es cojo la potencia del
                    // codigo d barras
                    if (GlobalClass.global_Balast.substring(0, 2).compareTo("LU") == 0) {
                        GlobalClass.balasto_marca = "LUMLUX";
                        String potencia[] = GlobalClass.global_Balast.split("_");

                        // Y ahora tengo que diferencias las potencias de los Lumlux,
                        // si es de 70 o de las demas potencias
                        if (GlobalClass.global_Balast.substring(16, 21).compareTo("DELU0") == 0) {
                            GlobalClass.balasto_potencia = Integer.parseInt(potencia[3].substring(5, 9));
                        } else {

                            if (potencia[3].substring(2, 3).compareTo("7") == 0) {
                                GlobalClass.balasto_potencia = Integer.parseInt(potencia[3].substring(2, 4));
                            } else {
                                if (potencia[3].substring(2, 6).compareTo("1000") == 0)
                                    GlobalClass.balasto_potencia = Integer.parseInt(potencia[3].substring(2, 6));
                                else
                                    GlobalClass.balasto_potencia = Integer.parseInt(potencia[3].substring(2, 5));
                            }
                        }
                    }
                } else {
                    if (GlobalClass.balasto_marca.compareTo("LUMLUX") == 0)
                        textBalast.setText(GlobalClass.global_Balast);
                    else
                        textBalast.setText(GlobalClass.tipo_balasto);

                    textPot.setText(String.valueOf(GlobalClass.balasto_potencia));
                }

                if (GlobalClass.tipo_puntoAux.compareTo("DOBLE") == 0) {
                    textViewBalast2.setVisibility(View.VISIBLE);
                    textBalast2.setVisibility(View.VISIBLE);
                    textViewPot2.setVisibility(View.VISIBLE);
                    textPot2.setVisibility(View.VISIBLE);

                    if (GlobalClass.balasto2_marca.compareTo("SINAPSE") == 0) {
                        textBalast2.setText(GlobalClass.global_Balast2);
                        // Miro si el balasto es un Lumlux, si lo es cojo la potencia del
                        // codigo d barras
                        if (GlobalClass.global_Balast2.substring(0, 2).compareTo("LU") == 0) {
                            GlobalClass.balasto2_marca = "LUMLUX";
                            String potencia2[] = GlobalClass.global_Balast2.split("_");

                            // Y ahora tengo que diferencias las potencias de los Lumlux,
                            // si es de 70 o de las demas potencias
                            if (GlobalClass.global_Balast2.substring(16, 21).compareTo("DELU0") == 0) {
                                GlobalClass.balasto2_potencia = Integer.parseInt(potencia2[3].substring(5, 9));
                            } else {
                                if (potencia2[3].substring(2, 3).compareTo("7") == 0) {
                                    GlobalClass.balasto2_potencia = Integer.parseInt(potencia2[3].substring(2, 4));
                                } else {
                                    if (potencia2[3].substring(2, 6).compareTo("1000") == 0)
                                        GlobalClass.balasto2_potencia = Integer.parseInt(potencia2[3].substring(2, 6));
                                    else
                                        GlobalClass.balasto2_potencia = Integer.parseInt(potencia2[3].substring(2, 5));
                                }
                            }
                        }
                    } else {
                        if (GlobalClass.balasto2_marca.compareTo("LUMLUX") == 0)
                            textBalast2.setText(GlobalClass.global_Balast2);
                        else
                            textBalast2.setText(GlobalClass.tipo_balasto2);

                        textPot2.setText(String.valueOf(GlobalClass.balasto2_potencia));
                    }
                }

                actualizatexto();
                if (GlobalClass.global_Balast2.compareTo("") != 0)
                    actualizatexto2();

                if (GlobalClass.balasto_potencia == 0) {
                    textView6.setVisibility(View.GONE);
                    textPot.setVisibility(View.GONE);
                }

                break;
            case 2:
                if (GlobalClass.global_CMC.compareTo("") != 0) {
                    textCmc.setVisibility(View.VISIBLE);
                    textView19.setVisibility(View.VISIBLE);
                    textPc.setVisibility(View.VISIBLE);
                    textView20.setVisibility(View.VISIBLE);
                    textPc.setText(GlobalClass.global_CMC);
                    textView9.setVisibility(View.GONE);
                    textLum.setVisibility(View.GONE);
                    textBalast.setVisibility(View.GONE);
                    textView7.setVisibility(View.GONE);
                    textEC.setVisibility(View.GONE);
                    textView5.setVisibility(View.GONE);
                    textView6.setVisibility(View.GONE);
                    textPot.setVisibility(View.GONE);
                    textViewPunto.setVisibility(View.GONE);
                    textPunto.setVisibility(View.GONE);
                    textCmc.setText(GlobalClass.global_CMC);
                    textViewOtro.setVisibility(View.GONE);
                    textOtro.setVisibility(View.GONE);
                    if (GlobalClass.global_CMC.substring(19, 20).compareTo("C") == 0) {
                        tControl.setVisibility(View.VISIBLE);
                        eControl.setVisibility(View.VISIBLE);
                    }
                }

                GlobalClass.nombre_punto = reemplazarCaracteresEspeciales(GlobalClass.cuadro_nombre + "_" + GlobalClass.cuadro_id);
                textView4.setText("Resumen del cuadro: " + GlobalClass.nombre_punto);

                break;
            case 3:
                if (GlobalClass.global_PC.compareTo("") != 0 && GlobalClass.global_buscaCMC == false) {
                    textPc.setVisibility(View.VISIBLE);
                    textView20.setVisibility(View.VISIBLE);
                    textView9.setVisibility(View.GONE);
                    textLum.setVisibility(View.GONE);
                    textBalast.setVisibility(View.GONE);
                    textView7.setVisibility(View.GONE);
                    textEC.setVisibility(View.GONE);
                    textView5.setVisibility(View.GONE);
                    textView6.setVisibility(View.GONE);
                    textPot.setVisibility(View.GONE);
                    textViewPunto.setVisibility(View.GONE);
                    textPunto.setVisibility(View.GONE);
                    textPc.setText(GlobalClass.global_PC);
                    textViewOtro.setVisibility(View.GONE);
                    textOtro.setVisibility(View.GONE);

                } else {
                    if (GlobalClass.global_BR.compareTo("") != 0) {
                        textBr.setVisibility(View.VISIBLE);
                        textView21.setVisibility(View.VISIBLE);
                        textView9.setVisibility(View.GONE);
                        textLum.setVisibility(View.GONE);
                        textBalast.setVisibility(View.GONE);
                        textView7.setVisibility(View.GONE);
                        textEC.setVisibility(View.GONE);
                        textView5.setVisibility(View.GONE);
                        textView6.setVisibility(View.GONE);
                        textPot.setVisibility(View.GONE);
                        textViewPunto.setVisibility(View.GONE);
                        textPunto.setVisibility(View.GONE);
                        textBr.setText(GlobalClass.global_BR);
                        textViewOtro.setVisibility(View.GONE);
                        textOtro.setVisibility(View.GONE);
                    }
                }

                GlobalClass.nombre_punto = reemplazarCaracteresEspeciales(GlobalClass.nom_puntoAcceso + "_" + GlobalClass.id_puntoAcceso);
                textView4.setText("Resumen del punto de acceso: " + GlobalClass.nombre_punto);

                break;
            case 4:
                textViewOtro.setVisibility(View.VISIBLE);
                textOtro.setVisibility(View.VISIBLE);
                textOtro.setText(GlobalClass.global_Otro);
                textBr.setVisibility(View.GONE);
                textView21.setVisibility(View.GONE);
                textView9.setVisibility(View.GONE);
                textLum.setVisibility(View.GONE);
                textBalast.setVisibility(View.GONE);
                textView7.setVisibility(View.GONE);
                textEC.setVisibility(View.GONE);
                textView5.setVisibility(View.GONE);
                textView6.setVisibility(View.GONE);
                textPot.setVisibility(View.GONE);
                textViewPunto.setVisibility(View.GONE);
                textPunto.setVisibility(View.GONE);

                GlobalClass.nombre_punto = reemplazarCaracteresEspeciales(GlobalClass.nom_otroDispositivo + "_" + GlobalClass.id_otroDispositivo);

                textView4.setText("Resumen del dispositivo: " + GlobalClass.nombre_punto);
                break;
        }

        this.atrasButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                switch (GlobalClass.global_tipo) {
                    case 1:
                        alertaCerrar3();
                        break;
                    case 2:
                        alertaCerrar();
                        break;
                    case 3:
                        alertaCerrar2();
                        break;
                    case 4:
                        alertaCerrar4();
                        break;
                }
            }
        });

        this.anteriorButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GlobalClass.address = reemplazarCaracteresEspeciales(GlobalClass.address);
                //String fech = ((new SimpleDateFormat("yyyy-MM-dd",Locale.GERMANY).format(new Date())).toString());
                String fech = ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY)).format(new Date())).toString();
                GlobalClass.id_cuadro = reemplazarCaracteresEspeciales(GlobalClass.id_cuadro);

                if (GlobalClass.id_cuadro.compareTo("") == 0)
                    GlobalClass.id_cuadro = "CMXX";

                MCBE mcbe = new MCBE();
                MC3P mc3p = new MC3P();
                BALASTOTELEGESTIONADO be = new BALASTOTELEGESTIONADO();
                DRIVERTELEGESTIONADO de = new DRIVERTELEGESTIONADO();
                Cmc cmc = new Cmc();
                Pc pc = new Pc();
                Br br = new Br();
                OtroDispositivo otro = new OtroDispositivo();

                switch (GlobalClass.global_tipo) {
                    case 1:
                        GlobalClass.punto_luz_nombre = reemplazarCaracteresEspeciales(GlobalClass.punto_luz_nombre);
                        GlobalClass.punto_luz_cuadro = reemplazarCaracteresEspeciales(GlobalClass.punto_luz_cuadro);
                        GlobalClass.luminaria_marca = reemplazarCaracteresEspeciales(GlobalClass.luminaria_marca);
                        GlobalClass.luminaria_modelo = reemplazarCaracteresEspeciales(GlobalClass.luminaria_modelo);
                        //GlobalClass.fuente_luz_marca = reemplazarCaracteresEspeciales(GlobalClass.fuente_luz_marca);
                        //GlobalClass.fuente_luz_modelo = reemplazarCaracteresEspeciales(GlobalClass.fuente_luz_modelo);

                        GlobalClass.fuente_luz_potencia = GlobalClass.balasto_potencia + GlobalClass.balasto2_potencia;
                        PuntoFisico pf = new PuntoFisico(GlobalClass.punto_luz_id, GlobalClass.punto_luz_nombre, GlobalClass.latitud,
                                GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address, GlobalClass.punto_luz_vida_util_fabricante, GlobalClass.punto_luz_cuadro,
                                GlobalClass.punto_luz_circuito, GlobalClass.soporte_luz_tipo, GlobalClass.luminaria_tipo, GlobalClass.luminaria_marca,
                                GlobalClass.luminaria_modelo, GlobalClass.fuente_tipo, GlobalClass.fuente_luz_marca, GlobalClass.fuente_luz_modelo, GlobalClass.fuente_luz_potencia, fech, GlobalClass.punto_fecha_toma_datos);

                        if (GlobalClass.global_EC.compareTo("") != 0) {
                            int tam = GlobalClass.global_EC.length();
                            switch (tam) {
                                case 30:
                                    GlobalClass.id_node = GlobalClass.global_EC.substring(24, 30);
                                    GlobalClass.medida = true;
                                    break;
                                case 36:
                                    GlobalClass.id_node = GlobalClass.global_EC.substring(30, 36);
                                    if (GlobalClass.global_EC.substring(21, 22).compareTo("M") == 0)
                                        GlobalClass.medida = true;
                                    break;
                            }

                            GlobalClass.circuito = GlobalClass.punto_luz_circuito;

                            if (GlobalClass.balasto_marca.compareTo("SINAPSE") == 0 || GlobalClass.balasto_marca.compareTo("LUMLUX") == 0) {
                                if (GlobalClass.global_Balast.substring(16, 21).compareTo("DELU0") == 0) {
                                    GlobalClass.balasto_perdida = 8;
                                    GlobalClass.tipo_balasto = "DRIVER_LED";
                                } else {
                                    GlobalClass.balasto_perdida = 10;
                                    GlobalClass.tipo_balasto = "BALASTO_ELECTRONICO";
                                }
                            } else {
                                if (GlobalClass.tipo_balasto.compareTo("BALASTO_ELECTROMAGNETICO_DE_SIMPLE_NIVEL") == 0)
                                    GlobalClass.balasto_perdida = 20;
                                if (GlobalClass.tipo_balasto.compareTo("BALASTO_ELECTROMAGNETICO_DE_DOBLE_NIVEL") == 0)
                                    GlobalClass.balasto_perdida = 20;
                                if (GlobalClass.tipo_balasto.compareTo("BALASTO_ELECTRONICO") == 0)
                                    GlobalClass.balasto_perdida = 10;
                                if (GlobalClass.tipo_balasto.compareTo("DRIVER_LED") == 0)
                                    GlobalClass.balasto_perdida = 8;
                            }

                            if (GlobalClass.balasto2_marca.compareTo("SINAPSE") == 0 || GlobalClass.balasto2_marca.compareTo("LUMLUX") == 0) {
                                if (GlobalClass.global_Balast2.substring(16, 21).compareTo("DELU0") == 0) {
                                    GlobalClass.balasto2_perdida = 8;
                                    GlobalClass.tipo_balasto2 = "DRIVER_LED";
                                } else {
                                    GlobalClass.balasto2_perdida = 10;
                                    GlobalClass.tipo_balasto2 = "BALASTO_ELECTRONICO";
                                }
                            } else {
                                if (GlobalClass.tipo_balasto2.compareTo("BALASTO_ELECTROMAGNETICO_DE_SIMPLE_NIVEL") == 0)
                                    GlobalClass.balasto2_perdida = 20;
                                if (GlobalClass.tipo_balasto2.compareTo("BALASTO_ELECTROMAGNETICO_DE_DOBLE_NIVEL") == 0)
                                    GlobalClass.balasto2_perdida = 20;
                                if (GlobalClass.tipo_balasto2.compareTo("BALASTO_ELECTRONICO") == 0)
                                    GlobalClass.balasto2_perdida = 10;
                                if (GlobalClass.tipo_balasto2.compareTo("DRIVER_LED") == 0)
                                    GlobalClass.balasto2_perdida = 8;
                            }


                            if (GlobalClass.global_Balast.compareTo("") != 0) {
                                if (GlobalClass.global_Balast.substring(16, 21).compareTo("DELU0") == 0) {
                                    GlobalClass.balasto_id = GlobalClass.global_Balast.substring(26, 30);
                                } else {
                                    int bal = GlobalClass.global_Balast.length();
                                    switch (bal) {
                                        case 34:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(30, 34);
                                            break;
                                        case 35:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(31, 35);
                                            break;
                                        case 36:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(32, 36);
                                            break;
                                        case 32:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(28, 32);
                                            break;
                                        case 33:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(29, 33);
                                            break;

                                    }

                                }
                            }


                            if (GlobalClass.tipo_puntoAux.compareTo("DOBLE") == 0) {
                                if (GlobalClass.global_Balast2.compareTo("") != 0) {
                                    if (GlobalClass.global_Balast2.substring(16, 21).compareTo("DELU0") == 0) {
                                        GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(26, 30);
                                    } else {
                                        int bal2 = GlobalClass.global_Balast2.length();
                                        switch (bal2) {
                                            case 34:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(30, 34);
                                                break;
                                            case 35:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(31, 35);
                                                break;
                                            case 36:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(22, 36);
                                                break;
                                            case 32:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(28, 32);
                                                break;
                                            case 33:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(29, 33);
                                                break;

                                        }
                                    }
                                }
                            } else {
                                GlobalClass.balasto2_id = null;
                                GlobalClass.balasto2_marca = null;
                                GlobalClass.balasto2_potencia = 0;
                                GlobalClass.tipo_balasto2 = null;
                                GlobalClass.balasto2_perdida = 0;
                            }

                            if (GlobalClass.balasto_marca.compareTo("SINAPSE") == 0 || GlobalClass.balasto_marca.compareTo("LUMLUX") == 0) {
                                GlobalClass.regulacion = "1-10V";

                                mcbe = new MCBE(GlobalClass.nombre_punto, GlobalClass.global_EC,
                                        GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                        GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                        GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.tipo_puntoAux,
                                        GlobalClass.balasto_id, GlobalClass.balasto_marca, GlobalClass.balasto_potencia,
                                        GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, GlobalClass.balasto2_id, GlobalClass.balasto2_marca,
                                        GlobalClass.balasto2_potencia, GlobalClass.tipo_balasto2, GlobalClass.balasto2_perdida, fech, pf);

                                GlobalClass.MC_BE.add(mcbe);

                            } else {
                                GlobalClass.balasto_id = "BA" + GlobalClass.id_node + "-1";

                                if (GlobalClass.tipo_puntoAux.compareTo("DOBLE") == 0)
                                    GlobalClass.balasto2_id = "BA" + GlobalClass.id_node + "-2";

                                mc3p = new MC3P(GlobalClass.nombre_punto, GlobalClass.global_EC,
                                        GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                        GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                        GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.tipo_puntoAux,
                                        GlobalClass.balasto_id, GlobalClass.balasto_marca, GlobalClass.balasto_potencia,
                                        GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, GlobalClass.balasto2_id, GlobalClass.balasto2_marca,
                                        GlobalClass.balasto2_potencia, GlobalClass.tipo_balasto2, GlobalClass.balasto2_perdida, fech, pf);

                                GlobalClass.MC_3P.add(mc3p);
                            }


                        } else {
                            if (GlobalClass.global_BE.compareTo("") != 0) {
                                int n = GlobalClass.global_BE.length();
                                switch (n) {
                                    case 37:
                                        GlobalClass.id_node = GlobalClass.global_BE.substring(31, 37);

                                        break;
                                    case 43:
                                        GlobalClass.id_node = GlobalClass.global_BE.substring(37, 43);

                                        break;
                                }


                                GlobalClass.balasto_id = GlobalClass.global_BE.substring(26, 30);
                                GlobalClass.circuito = GlobalClass.punto_luz_circuito;
                                GlobalClass.balasto_marca = "SINAPSE";
                                GlobalClass.tipo_balasto = "BALASTO_ELECTRONICO";
                                GlobalClass.balasto_perdida = 10;
                                GlobalClass.regulacion = "DIGITAL";
                                GlobalClass.medida = true;

                                be = new BALASTOTELEGESTIONADO(GlobalClass.nombre_punto, GlobalClass.global_BE,
                                        GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                        GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                        GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.balasto_id, GlobalClass.balasto_marca,
                                        GlobalClass.balasto_potencia, GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, fech, pf);

                                GlobalClass.BE.add(be);
                            } else {
                                if (GlobalClass.global_DE.compareTo("") != 0) {
                                    int n = GlobalClass.global_DE.length();
                                    switch (n) {
                                        case 37:
                                            GlobalClass.id_node = GlobalClass.global_DE.substring(31, 37);

                                            break;
                                        case 43:
                                            GlobalClass.id_node = GlobalClass.global_DE.substring(37, 43);

                                            break;
                                    }

                                    GlobalClass.balasto_id = GlobalClass.global_DE.substring(26, 30);
                                    GlobalClass.circuito = GlobalClass.punto_luz_circuito;
                                    GlobalClass.balasto_marca = "SINAPSE";
                                    GlobalClass.tipo_balasto = "DRIVER_LED";
                                    GlobalClass.regulacion = "DIGITAL";
                                    GlobalClass.balasto_perdida = 8;
                                    GlobalClass.medida = true;

                                    de = new DRIVERTELEGESTIONADO(GlobalClass.nombre_punto, GlobalClass.global_DE,
                                            GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                            GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                            GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.balasto_id, GlobalClass.balasto_marca,
                                            GlobalClass.balasto_potencia, GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, fech, pf);

                                    GlobalClass.DE.add(de);
                                } else {
                                    if (GlobalClass.global_MC.compareTo("") != 0) {
                                        GlobalClass.id_node = GlobalClass.global_MC.substring(30, 36);
                                        GlobalClass.circuito = GlobalClass.punto_luz_circuito;
                                        GlobalClass.balasto_marca = "SINAPSE";
                                        GlobalClass.tipo_balasto = "BALASTO_ELECTRONICO";
                                        GlobalClass.balasto_perdida = 10;

                                        MCBE mc = new MCBE(GlobalClass.nombre_punto, GlobalClass.global_MC,
                                                GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                                GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                                GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.balasto_id, GlobalClass.balasto_marca,
                                                GlobalClass.balasto_potencia, GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, fech, pf);

                                        GlobalClass.MC_BE.add(mc);
                                    }
                                }
                            }
                        }

                        //mostrarmensaje();
                        break;
                    case 2:
                        if (GlobalClass.global_CMC.substring(21, 22).compareTo("M") == 0)
                            GlobalClass.medida = true;

                        if (GlobalClass.global_CMC.substring(22, 23).compareTo("N") == 0)
                            GlobalClass.monitoring = true;

                        GlobalClass.id_node = GlobalClass.global_CMC.substring(30, 36);

                        ArrayList<Circuito> cir = new ArrayList<Circuito>(GlobalClass.circuitos);

                        GlobalClass.cuadro_nombre = reemplazarCaracteresEspeciales(GlobalClass.cuadro_nombre);
                        GlobalClass.cuadro_numero_suministro = reemplazarCaracteresEspeciales(GlobalClass.cuadro_numero_suministro);

                        CuadroFisico cf = new CuadroFisico(GlobalClass.cuadro_id, GlobalClass.cuadro_nombre, GlobalClass.address, GlobalClass.latitud, GlobalClass.longitud,
                                GlobalClass.cuadro_altura, GlobalClass.cuadro_numero_suministro, GlobalClass.cuadro_potencia_instalada, GlobalClass.cuadro_potencia_contratada,
                                GlobalClass.cuadro_comando, GlobalClass.cuadro_fecha_toma_datos, fech, GlobalClass.cuadro_reloj_marca, GlobalClass.cuadro_reloj_modelo, GlobalClass.cuadro_numero_circuitos, cir);

                        GlobalClass.global_PC = GlobalClass.global_CMC;
                        GlobalClass.nom_puntoAcceso = GlobalClass.nombre_punto + "_AP";

                        Pc pccmc = new Pc(GlobalClass.nom_puntoAcceso, GlobalClass.global_PC, GlobalClass.latitud, GlobalClass.longitud,
                                GlobalClass.alturaDisp, GlobalClass.address, GlobalClass.id_node, GlobalClass.ip1, GlobalClass.protocolo1,
                                GlobalClass.puerto11, GlobalClass.puerto12, GlobalClass.ip2, GlobalClass.protocolo2, GlobalClass.puerto21, GlobalClass.puerto22,
                                GlobalClass.dirfisica, fech);

                        cmc = new Cmc(GlobalClass.nombre_punto, GlobalClass.global_CMC, GlobalClass.latitud,
                                GlobalClass.longitud, GlobalClass.cuadro_altura, GlobalClass.address, GlobalClass.id_node, GlobalClass.id_cuadro,
                                GlobalClass.medida, GlobalClass.monitoring, GlobalClass.control, GlobalClass.rele_1, GlobalClass.rele_2,
                                GlobalClass.rele_3, GlobalClass.rele_4, GlobalClass.rele_5, GlobalClass.rele_6, GlobalClass.rele_7, fech, cf, pccmc);

                        GlobalClass.cuadros.add(cmc);
                        //mostrarmensaje2();

                        break;
                    case 3:
                        if (GlobalClass.global_BR.compareTo("") != 0) {
                            GlobalClass.id_node = GlobalClass.global_BR.substring(30, 36);

                            br = new Br(GlobalClass.nombre_punto, GlobalClass.global_BR, GlobalClass.latitud, GlobalClass.longitud,
                                    GlobalClass.alturaDisp, GlobalClass.address, GlobalClass.id_node, GlobalClass.ip1, GlobalClass.protocolo1,
                                    GlobalClass.puerto11, GlobalClass.puerto12, GlobalClass.ip2, GlobalClass.protocolo2, GlobalClass.puerto21, GlobalClass.puerto22,
                                    GlobalClass.dirfisica, fech);

                            GlobalClass.brs.add(br);
                        } else {
                            if (GlobalClass.global_PC.compareTo("") != 0 && GlobalClass.global_CMC.compareTo("") == 0) {
                                GlobalClass.id_node = GlobalClass.global_PC.substring(30, 36);

                                pc = new Pc(GlobalClass.nombre_punto, GlobalClass.global_PC, GlobalClass.latitud, GlobalClass.longitud,
                                        GlobalClass.alturaDisp, GlobalClass.address, GlobalClass.id_node, GlobalClass.ip1, GlobalClass.protocolo1,
                                        GlobalClass.puerto11, GlobalClass.puerto12, GlobalClass.ip2, GlobalClass.protocolo2, GlobalClass.puerto21, GlobalClass.puerto22,
                                        GlobalClass.dirfisica, fech);

                                GlobalClass.pcs.add(pc);
                            }
                        }

                        //mostrarmensaje3();

                        break;
                    case 4:
                        if (GlobalClass.global_Otro.compareTo("") != 0) {
                            otro = new OtroDispositivo(GlobalClass.nombre_punto, GlobalClass.global_Otro,
                                    GlobalClass.latitud, GlobalClass.longitud, fech);

                            GlobalClass.otros.add(otro);
                        }

                        //mostrarmensaje4();

                        break;
                }

                //CONVERTIMOS DE JAVA A JSON
                Punto p;
                Cuadro c;
                PuntoAcceso pa;
                Instalacion instalacion = new Instalacion();

                Gson prettyGson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                String formatoJSON;

                String f = ((new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(new Date())).toString());
                String srvcName = Context.TELEPHONY_SERVICE;
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
                String deviceId = telephonyManager.getDeviceId();
                File root = Environment.getExternalStorageDirectory();
                File logsDirectory = new File(root + "/sinapse/install/");
                String sFichero = logsDirectory + "/" + f + "-backup-" + GlobalClass.global_localiz + "-" + deviceId + ".json";
                File fichero = new File(sFichero);

                if (fichero.exists()) {
                    JsonReader reader;

                    try {
                        reader = new JsonReader(new InputStreamReader(new FileInputStream(sFichero), "UTF-8"));
                        Instalacion instalacion2 = prettyGson.fromJson(reader, Instalacion.class);

                        ArrayList<MCBE> mcbe2 = instalacion2.getPuntos().getMCBE();
                        ArrayList<MC3P> mc3p2 = instalacion2.getPuntos().getMC3P();
                        ArrayList<BALASTOTELEGESTIONADO> be2 = instalacion2.getPuntos().getBE();
                        ArrayList<DRIVERTELEGESTIONADO> de2 = instalacion2.getPuntos().getDE();
                        ArrayList<Cmc> cmc2 = instalacion2.getCuadros().getCuadros();
                        ArrayList<Pc> pc2 = instalacion2.getPuntos_Acceso().getPC();
                        ArrayList<Br> br2 = instalacion2.getPuntos_Acceso().getBR();
                        ArrayList<OtroDispositivo> otro2 = instalacion2.getOtros_Dispositivo();

                        if (mcbe.getNOMBRE() != null)
                            mcbe2.add(mcbe);
                        if (mc3p.getNOMBRE() != null)
                            mc3p2.add(mc3p);
                        if (be.getNOMBRE() != null)
                            be2.add(be);
                        if (de.getNOMBRE() != null)
                            de2.add(de);
                        if (cmc.getNOMBRE() != null)
                            cmc2.add(cmc);
                        if (pc.getNOMBRE() != null)
                            pc2.add(pc);
                        if (br.getNOMBRE() != null)
                            br2.add(br);
                        if (otro.getNombre() != null)
                            otro2.add(otro);

                        p = new Punto(mcbe2, mc3p2, be2, de2);
                        c = new Cuadro(cmc2);
                        pa = new PuntoAcceso(pc2, br2);

                        instalacion = new Instalacion(GlobalClass.global_localiz, p, c, pa, otro2);
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    formatoJSON = prettyGson.toJson(instalacion);
                } else {
                    p = new Punto(GlobalClass.MC_BE, GlobalClass.MC_3P, GlobalClass.BE, GlobalClass.DE);
                    c = new Cuadro(GlobalClass.cuadros);
                    pa = new PuntoAcceso(GlobalClass.pcs, GlobalClass.brs);
                    instalacion = new Instalacion(GlobalClass.global_localiz, p, c, pa, GlobalClass.otros);
                    formatoJSON = prettyGson.toJson(instalacion);
                }

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                createFile(GlobalClass.global_localiz, formatoJSON);

                try {

                    BufferedWriter bw = new BufferedWriter(new FileWriter(GlobalClass.global_fichero_B));
                    bw.write("");
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                anaderegistro(GlobalClass.global_localiz, formatoJSON);
                uploadFTP(GlobalClass.global_fichero_B);

                GlobalClass.global_EC = "";
                GlobalClass.tipo_puntoAux = "";
                GlobalClass.tipo_punto = "";
                GlobalClass.global_Balast = "";
                GlobalClass.balasto_id = "";
                GlobalClass.balasto_marca = "";
                GlobalClass.balasto_potencia = 0;
                GlobalClass.balasto_perdida = 0;
                GlobalClass.tipo_balasto = "";
                GlobalClass.global_Balast2 = "";
                GlobalClass.balasto2_id = "";
                GlobalClass.balasto2_marca = "";
                GlobalClass.balasto2_potencia = 0;
                GlobalClass.tipo_balasto2 = "";
                GlobalClass.balasto2_perdida = 0;
                GlobalClass.cont_balasto = 0;
                GlobalClass.cont_punto = 0;
                GlobalClass.control = "";
                GlobalClass.num_cir = 0;
                GlobalClass.circuitos.clear();
                GlobalClass.circuito_telegestionado = false;
                GlobalClass.cambio_bombilla = false;
                GlobalClass.monitoring = false;
                GlobalClass.medida = false;
                GlobalClass.regulacion = "";
                GlobalClass.nombre_punto = "";
                GlobalClass.cont_circuito = 0;
                GlobalClass.rele_1 = "NO_CONECTA";
                GlobalClass.rele_2 = "NO_CONECTA";
                GlobalClass.rele_3 = "NO_CONECTA";
                GlobalClass.rele_4 = "NO_CONECTA";
                GlobalClass.rele_5 = "NO_CONECTA";
                GlobalClass.rele_6 = "NO_CONECTA";
                GlobalClass.rele_7 = "NO_CONECTA";
                GlobalClass.tipo_rele.clear();

                Intent intent = new Intent();
                intent.setClass(Resumen_activity.this, Gps_activity.class);
                startActivity(intent);
                finish();
            }
        });

        this.continueButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GlobalClass.address = reemplazarCaracteresEspeciales(GlobalClass.address);
                //String fech = ((new SimpleDateFormat("yyyy-MM-dd",Locale.GERMANY).format(new Date())).toString());
                String fech = ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY)).format(new Date())).toString();
                GlobalClass.id_cuadro = reemplazarCaracteresEspeciales(GlobalClass.id_cuadro);

                if (GlobalClass.id_cuadro.compareTo("") == 0)
                    GlobalClass.id_cuadro = "CMXX";

                MCBE mcbe = new MCBE();
                MC3P mc3p = new MC3P();
                BALASTOTELEGESTIONADO be = new BALASTOTELEGESTIONADO();
                DRIVERTELEGESTIONADO de = new DRIVERTELEGESTIONADO();
                Cmc cmc = new Cmc();
                Pc pc = new Pc();
                Br br = new Br();
                OtroDispositivo otro = new OtroDispositivo();

                switch (GlobalClass.global_tipo) {
                    case 1:
                        GlobalClass.punto_luz_nombre = reemplazarCaracteresEspeciales(GlobalClass.punto_luz_nombre);
                        GlobalClass.punto_luz_cuadro = reemplazarCaracteresEspeciales(GlobalClass.punto_luz_cuadro);
                        GlobalClass.luminaria_marca = reemplazarCaracteresEspeciales(GlobalClass.luminaria_marca);
                        GlobalClass.luminaria_modelo = reemplazarCaracteresEspeciales(GlobalClass.luminaria_modelo);
                        //GlobalClass.fuente_luz_marca = reemplazarCaracteresEspeciales(GlobalClass.fuente_luz_marca);
                        //GlobalClass.fuente_luz_modelo = reemplazarCaracteresEspeciales(GlobalClass.fuente_luz_modelo);

                        GlobalClass.fuente_luz_potencia = GlobalClass.balasto_potencia + GlobalClass.balasto2_potencia;

                        PuntoFisico pf = new PuntoFisico(GlobalClass.punto_luz_id, GlobalClass.punto_luz_nombre, GlobalClass.latitud,
                                GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address, GlobalClass.punto_luz_vida_util_fabricante, GlobalClass.punto_luz_cuadro,
                                GlobalClass.punto_luz_circuito, GlobalClass.soporte_luz_tipo, GlobalClass.luminaria_tipo, GlobalClass.luminaria_marca,
                                GlobalClass.luminaria_modelo, GlobalClass.fuente_tipo, GlobalClass.fuente_luz_marca, GlobalClass.fuente_luz_modelo, GlobalClass.fuente_luz_potencia, fech, GlobalClass.punto_fecha_toma_datos);

                        if (GlobalClass.global_EC.compareTo("") != 0) {
                            int tam = GlobalClass.global_EC.length();
                            switch (tam) {
                                case 30:
                                    GlobalClass.id_node = GlobalClass.global_EC.substring(24, 30);
                                    GlobalClass.medida = true;
                                    break;
                                case 36:
                                    GlobalClass.id_node = GlobalClass.global_EC.substring(30, 36);
                                    if (GlobalClass.global_EC.substring(21, 22).compareTo("M") == 0)
                                        GlobalClass.medida = true;
                                    break;
                            }

                            GlobalClass.circuito = GlobalClass.punto_luz_circuito;

                            if (GlobalClass.balasto_marca.compareTo("SINAPSE") == 0 || GlobalClass.balasto_marca.compareTo("LUMLUX") == 0) {
                                if (GlobalClass.global_Balast.substring(16, 21).compareTo("DELU0") == 0) {
                                    GlobalClass.balasto_perdida = 8;
                                    GlobalClass.tipo_balasto = "DRIVER_LED";
                                } else {
                                    GlobalClass.balasto_perdida = 10;
                                    GlobalClass.tipo_balasto = "BALASTO_ELECTRONICO";
                                }
                            } else {
                                if (GlobalClass.tipo_balasto.compareTo("BALASTO_ELECTROMAGNETICO_DE_SIMPLE_NIVEL") == 0)
                                    GlobalClass.balasto_perdida = 20;
                                if (GlobalClass.tipo_balasto.compareTo("BALASTO_ELECTROMAGNETICO_DE_DOBLE_NIVEL") == 0)
                                    GlobalClass.balasto_perdida = 20;
                                if (GlobalClass.tipo_balasto.compareTo("BALASTO_ELECTRONICO") == 0)
                                    GlobalClass.balasto_perdida = 10;
                                if (GlobalClass.tipo_balasto.compareTo("DRIVER_LED") == 0)
                                    GlobalClass.balasto_perdida = 8;
                            }

                            if (GlobalClass.balasto2_marca.compareTo("SINAPSE") == 0 || GlobalClass.balasto2_marca.compareTo("LUMLUX") == 0) {
                                if (GlobalClass.global_Balast2.substring(16, 21).compareTo("DELU0") == 0) {
                                    GlobalClass.balasto2_perdida = 8;
                                    GlobalClass.tipo_balasto2 = "DRIVER_LED";
                                } else {
                                    GlobalClass.tipo_balasto2 = "BALASTO_ELECTRONICO";
                                    GlobalClass.balasto2_perdida = 10;
                                }
                            } else {
                                if (GlobalClass.tipo_balasto2.compareTo("BALASTO_ELECTROMAGNETICO_DE_SIMPLE_NIVEL") == 0)
                                    GlobalClass.balasto2_perdida = 20;
                                if (GlobalClass.tipo_balasto2.compareTo("BALASTO_ELECTROMAGNETICO_DE_DOBLE_NIVEL") == 0)
                                    GlobalClass.balasto2_perdida = 20;
                                if (GlobalClass.tipo_balasto2.compareTo("BALASTO_ELECTRONICO") == 0)
                                    GlobalClass.balasto2_perdida = 10;
                                if (GlobalClass.tipo_balasto2.compareTo("DRIVER_LED") == 0)
                                    GlobalClass.balasto2_perdida = 8;
                            }


                            if (GlobalClass.global_Balast.compareTo("") != 0) {
                                if (GlobalClass.global_Balast.substring(16, 21).compareTo("DELU0") == 0) {
                                    GlobalClass.balasto_id = GlobalClass.global_Balast.substring(26, 30);
                                } else {
                                    int bal = GlobalClass.global_Balast.length();
                                    switch (bal) {
                                        case 34:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(30, 34);
                                            break;
                                        case 35:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(31, 35);
                                            break;
                                        case 36:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(32, 36);
                                            break;
                                        case 32:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(28, 32);
                                            break;
                                        case 33:
                                            GlobalClass.balasto_id = GlobalClass.global_Balast.substring(29, 33);
                                            break;

                                    }
                                }
                            }


                            if (GlobalClass.tipo_puntoAux.compareTo("DOBLE") == 0) {
                                if (GlobalClass.global_Balast2.compareTo("") != 0) {
                                    if (GlobalClass.global_Balast2.substring(16, 21).compareTo("DELU0") == 0) {
                                        GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(26, 30);
                                    } else {
                                        int bal2 = GlobalClass.global_Balast2.length();
                                        switch (bal2) {
                                            case 34:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(30, 34);
                                                break;
                                            case 35:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(31, 35);
                                                break;
                                            case 36:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(32, 36);
                                                break;
                                            case 32:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(28, 32);
                                                break;
                                            case 33:
                                                GlobalClass.balasto2_id = GlobalClass.global_Balast2.substring(29, 33);
                                                break;

                                        }
                                    }
                                }
                            } else {
                                GlobalClass.balasto2_id = null;
                                GlobalClass.balasto2_marca = null;
                                GlobalClass.balasto2_potencia = 0;
                                GlobalClass.tipo_balasto2 = null;
                                GlobalClass.balasto2_perdida = 0;
                            }

                            if (GlobalClass.balasto_marca.compareTo("SINAPSE") == 0 || GlobalClass.balasto_marca.compareTo("LUMLUX") == 0) {
                                GlobalClass.regulacion = "1-10V";

                                mcbe = new MCBE(GlobalClass.nombre_punto, GlobalClass.global_EC,
                                        GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                        GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                        GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.tipo_puntoAux,
                                        GlobalClass.balasto_id, GlobalClass.balasto_marca, GlobalClass.balasto_potencia,
                                        GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, GlobalClass.balasto2_id, GlobalClass.balasto2_marca,
                                        GlobalClass.balasto2_potencia, GlobalClass.tipo_balasto2, GlobalClass.balasto2_perdida, fech, pf);

                                GlobalClass.MC_BE.add(mcbe);

                            } else {
                                GlobalClass.balasto_id = "BA" + GlobalClass.id_node + "-1";

                                if (GlobalClass.tipo_puntoAux.compareTo("DOBLE") == 0)
                                    GlobalClass.balasto2_id = "BA" + GlobalClass.id_node + "-2";

                                mc3p = new MC3P(GlobalClass.nombre_punto, GlobalClass.global_EC,
                                        GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                        GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                        GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.tipo_puntoAux,
                                        GlobalClass.balasto_id, GlobalClass.balasto_marca, GlobalClass.balasto_potencia,
                                        GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, GlobalClass.balasto2_id, GlobalClass.balasto2_marca,
                                        GlobalClass.balasto2_potencia, GlobalClass.tipo_balasto2, GlobalClass.balasto2_perdida, fech, pf);

                                GlobalClass.MC_3P.add(mc3p);
                            }


                        } else {
                            if (GlobalClass.global_BE.compareTo("") != 0) {

                                int n = GlobalClass.global_BE.length();
                                switch (n) {
                                    case 37:
                                        GlobalClass.id_node = GlobalClass.global_BE.substring(31, 37);

                                        break;
                                    case 43:
                                        GlobalClass.id_node = GlobalClass.global_BE.substring(37, 43);

                                        break;
                                }

                                GlobalClass.balasto_id = GlobalClass.global_BE.substring(26, 30);
                                GlobalClass.circuito = GlobalClass.punto_luz_circuito;
                                GlobalClass.balasto_marca = "SINAPSE";
                                GlobalClass.tipo_balasto = "BALASTO_ELECTRONICO";
                                GlobalClass.balasto_perdida = 10;
                                GlobalClass.regulacion = "DIGITAL";
                                GlobalClass.medida = true;

                                be = new BALASTOTELEGESTIONADO(GlobalClass.nombre_punto, GlobalClass.global_BE,
                                        GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                        GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                        GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.balasto_id, GlobalClass.balasto_marca,
                                        GlobalClass.balasto_potencia, GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, fech, pf);

                                GlobalClass.BE.add(be);
                            } else {
                                if (GlobalClass.global_DE.compareTo("") != 0) {
                                    int n = GlobalClass.global_DE.length();
                                    switch (n) {
                                        case 37:
                                            GlobalClass.id_node = GlobalClass.global_DE.substring(31, 37);

                                            break;
                                        case 43:
                                            GlobalClass.id_node = GlobalClass.global_DE.substring(37, 43);

                                            break;
                                    }

                                    GlobalClass.balasto_id = GlobalClass.global_DE.substring(26, 30);
                                    GlobalClass.circuito = GlobalClass.punto_luz_circuito;
                                    GlobalClass.balasto_marca = "SINAPSE";
                                    GlobalClass.tipo_balasto = "DRIVER_LED";
                                    GlobalClass.regulacion = "DIGITAL";
                                    GlobalClass.balasto_perdida = 8;
                                    GlobalClass.medida = true;

                                    de = new DRIVERTELEGESTIONADO(GlobalClass.nombre_punto, GlobalClass.global_DE,
                                            GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                            GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                            GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.balasto_id, GlobalClass.balasto_marca,
                                            GlobalClass.balasto_potencia, GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, fech, pf);

                                    GlobalClass.DE.add(de);
                                } else {
                                    if (GlobalClass.global_MC.compareTo("") != 0) {
                                        GlobalClass.id_node = GlobalClass.global_MC.substring(30, 36);
                                        GlobalClass.circuito = GlobalClass.punto_luz_circuito;
                                        GlobalClass.balasto_marca = "SINAPSE";
                                        GlobalClass.tipo_balasto = "BALASTO_ELECTRONICO";
                                        GlobalClass.balasto_perdida = 10;

                                        GlobalClass.nombre_punto = GlobalClass.nombre_punto + "_" + GlobalClass.id_node;

                                        MCBE mc = new MCBE(GlobalClass.nombre_punto, GlobalClass.global_MC,
                                                GlobalClass.latitud, GlobalClass.longitud, GlobalClass.punto_luz_altura, GlobalClass.address,
                                                GlobalClass.id_node, GlobalClass.punto_luz_cuadro, GlobalClass.circuito, GlobalClass.regulacion,
                                                GlobalClass.cambio_bombilla, GlobalClass.medida, GlobalClass.balasto_id, GlobalClass.balasto_marca,
                                                GlobalClass.balasto_potencia, GlobalClass.tipo_balasto, GlobalClass.balasto_perdida, fech, pf);

                                        GlobalClass.MC_BE.add(mc);
                                    }
                                }
                            }
                        }

                        //mostrarmensaje();
                        break;
                    case 2:
                        if (GlobalClass.global_CMC.substring(21, 22).compareTo("M") == 0)
                            GlobalClass.medida = true;

                        if (GlobalClass.global_CMC.substring(22, 23).compareTo("N") == 0)
                            GlobalClass.monitoring = true;

                        GlobalClass.id_node = GlobalClass.global_CMC.substring(30, 36);

                        ArrayList<Circuito> cir = new ArrayList<Circuito>(GlobalClass.circuitos);

                        GlobalClass.cuadro_nombre = reemplazarCaracteresEspeciales(GlobalClass.cuadro_nombre);
                        GlobalClass.cuadro_numero_suministro = reemplazarCaracteresEspeciales(GlobalClass.cuadro_numero_suministro);


                        CuadroFisico cf = new CuadroFisico(GlobalClass.cuadro_id, GlobalClass.cuadro_nombre, GlobalClass.address, GlobalClass.latitud, GlobalClass.longitud,
                                GlobalClass.cuadro_altura, GlobalClass.cuadro_numero_suministro, GlobalClass.cuadro_potencia_instalada, GlobalClass.cuadro_potencia_contratada,
                                GlobalClass.cuadro_comando, GlobalClass.cuadro_fecha_toma_datos, fech, GlobalClass.cuadro_reloj_marca, GlobalClass.cuadro_reloj_modelo, GlobalClass.cuadro_numero_circuitos, cir);

                        GlobalClass.global_PC = GlobalClass.global_CMC;
                        GlobalClass.nom_puntoAcceso = GlobalClass.nombre_punto + "_AP";

                        Pc pccmc = new Pc(GlobalClass.nom_puntoAcceso, GlobalClass.global_PC, GlobalClass.latitud, GlobalClass.longitud,
                                GlobalClass.alturaDisp, GlobalClass.address, GlobalClass.id_node, GlobalClass.ip1, GlobalClass.protocolo1,
                                GlobalClass.puerto11, GlobalClass.puerto12, GlobalClass.ip2, GlobalClass.protocolo2, GlobalClass.puerto21, GlobalClass.puerto22,
                                GlobalClass.dirfisica, fech);

                        cmc = new Cmc(GlobalClass.nombre_punto, GlobalClass.global_CMC, GlobalClass.latitud,
                                GlobalClass.longitud, GlobalClass.cuadro_altura, GlobalClass.address, GlobalClass.id_node, GlobalClass.id_cuadro,
                                GlobalClass.medida, GlobalClass.monitoring, GlobalClass.control, GlobalClass.rele_1, GlobalClass.rele_2,
                                GlobalClass.rele_3, GlobalClass.rele_4, GlobalClass.rele_5, GlobalClass.rele_6, GlobalClass.rele_7, fech, cf, pccmc);

                        GlobalClass.cuadros.add(cmc);

                        //mostrarmensaje2();
                        break;
                    case 3:
                        if (GlobalClass.global_BR.compareTo("") != 0) {
                            GlobalClass.id_node = GlobalClass.global_BR.substring(30, 36);

                            br = new Br(GlobalClass.nombre_punto, GlobalClass.global_BR, GlobalClass.latitud, GlobalClass.longitud,
                                    GlobalClass.alturaDisp, GlobalClass.address, GlobalClass.id_node, GlobalClass.ip1, GlobalClass.protocolo1,
                                    GlobalClass.puerto11, GlobalClass.puerto12, GlobalClass.ip2, GlobalClass.protocolo2, GlobalClass.puerto21, GlobalClass.puerto22,
                                    GlobalClass.dirfisica, fech);

                            GlobalClass.brs.add(br);
                        } else {
                            if (GlobalClass.global_PC.compareTo("") != 0 && GlobalClass.global_CMC.compareTo("") == 0) {
                                GlobalClass.id_node = GlobalClass.global_PC.substring(30, 36);

                                pc = new Pc(GlobalClass.nombre_punto, GlobalClass.global_PC, GlobalClass.latitud, GlobalClass.longitud,
                                        GlobalClass.alturaDisp, GlobalClass.address, GlobalClass.id_node, GlobalClass.ip1, GlobalClass.protocolo1,
                                        GlobalClass.puerto11, GlobalClass.puerto12, GlobalClass.ip2, GlobalClass.protocolo2, GlobalClass.puerto21, GlobalClass.puerto22,
                                        GlobalClass.dirfisica, fech);

                                GlobalClass.pcs.add(pc);
                            }
                        }

                        //mostrarmensaje3();

                        break;
                    case 4:
                        if (GlobalClass.global_Otro.compareTo("") != 0) {
                            otro = new OtroDispositivo(GlobalClass.nombre_punto, GlobalClass.global_Otro,
                                    GlobalClass.latitud, GlobalClass.longitud, fech);

                            GlobalClass.otros.add(otro);
                        }

                        //mostrarmensaje4();

                        break;
                }

                //CONVERTIMOS DE JAVA A JSON
                Punto p;
                Cuadro c;
                PuntoAcceso pa;
                Instalacion instalacion = new Instalacion();

                Gson prettyGson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                String formatoJSON;

                String f = ((new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(new Date())).toString());
                String srvcName = Context.TELEPHONY_SERVICE;
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
                String deviceId = telephonyManager.getDeviceId();
                File root = Environment.getExternalStorageDirectory();
                File logsDirectory = new File(root + "/sinapse/install/");
                String sFichero = logsDirectory + "/" + f + "-backup-" + GlobalClass.global_localiz + "-" + deviceId + ".json";
                File fichero = new File(sFichero);

                if (fichero.exists()) {
                    JsonReader reader;

                    try {
                        reader = new JsonReader(new InputStreamReader(new FileInputStream(sFichero), "UTF-8"));
                        Instalacion instalacion2 = prettyGson.fromJson(reader, Instalacion.class);

                        ArrayList<MCBE> mcbe2 = instalacion2.getPuntos().getMCBE();
                        ArrayList<MC3P> mc3p2 = instalacion2.getPuntos().getMC3P();
                        ArrayList<BALASTOTELEGESTIONADO> be2 = instalacion2.getPuntos().getBE();
                        ArrayList<DRIVERTELEGESTIONADO> de2 = instalacion2.getPuntos().getDE();
                        ArrayList<Cmc> cmc2 = instalacion2.getCuadros().getCuadros();
                        ArrayList<Pc> pc2 = instalacion2.getPuntos_Acceso().getPC();
                        ArrayList<Br> br2 = instalacion2.getPuntos_Acceso().getBR();
                        ArrayList<OtroDispositivo> otro2 = instalacion2.getOtros_Dispositivo();

                        if (mcbe.getNOMBRE() != null)
                            mcbe2.add(mcbe);
                        if (mc3p.getNOMBRE() != null)
                            mc3p2.add(mc3p);
                        if (be.getNOMBRE() != null)
                            be2.add(be);
                        if (de.getNOMBRE() != null)
                            de2.add(de);
                        if (cmc.getNOMBRE() != null)
                            cmc2.add(cmc);
                        if (pc.getNOMBRE() != null)
                            pc2.add(pc);
                        if (br.getNOMBRE() != null)
                            br2.add(br);
                        if (otro.getNombre() != null)
                            otro2.add(otro);

                        p = new Punto(mcbe2, mc3p2, be2, de2);
                        c = new Cuadro(cmc2);
                        pa = new PuntoAcceso(pc2, br2);

                        instalacion = new Instalacion(GlobalClass.global_localiz, p, c, pa, otro2);
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    formatoJSON = prettyGson.toJson(instalacion);
                } else {
                    p = new Punto(GlobalClass.MC_BE, GlobalClass.MC_3P, GlobalClass.BE, GlobalClass.DE);
                    c = new Cuadro(GlobalClass.cuadros);
                    pa = new PuntoAcceso(GlobalClass.pcs, GlobalClass.brs);
                    instalacion = new Instalacion(GlobalClass.global_localiz, p, c, pa, GlobalClass.otros);
                    formatoJSON = prettyGson.toJson(instalacion);
                }

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                createFile(GlobalClass.global_localiz, formatoJSON);

                try {

                    BufferedWriter bw = new BufferedWriter(new FileWriter(GlobalClass.global_fichero_B));
                    bw.write("");
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                anaderegistro(GlobalClass.global_localiz, formatoJSON);
                uploadFTP(GlobalClass.global_fichero_B);

                Intent intent = new Intent();
                intent.setClass(Resumen_activity.this, ComienzoInstalacion_activity.class);
                startActivity(intent);
                finish();
            }

        });


        sustlum.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1 == true)
                    GlobalClass.cambio_bombilla = true;
                else
                    GlobalClass.cambio_bombilla = false;
            }

        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                switch (GlobalClass.global_tipo) {
                    case 1:
                        alertaCerrar3();
                        break;
                    case 2:
                        alertaCerrar();
                        break;
                    case 3:
                        alertaCerrar2();
                        break;
                    case 4:
                        alertaCerrar4();
                        break;
                }
                return true;
        }
        return false;
    }

    public void alertaCerrar() {

        GlobalClass.global_buscaEC = false;
        GlobalClass.cuadro_id--;
        GlobalClass.cont_circuito = 0;
        GlobalClass.circuitos.clear();
        GlobalClass.tipo_rele.clear();
        Intent intent = new Intent();
        intent.setClass(Resumen_activity.this, formCuadro_activity.class);
        startActivity(intent);
        finish();
    }

    public void alertaCerrar2() {
        GlobalClass.global_buscaEC = false;
        GlobalClass.id_puntoAcceso--;

        Intent intent = new Intent();
        intent.setClass(Resumen_activity.this, PuntoAcceso_activity.class);
        startActivity(intent);
        finish();
    }

    public void alertaCerrar3() {
        GlobalClass.punto_luz_id--;

        Intent intent = new Intent();
        intent.setClass(Resumen_activity.this, formPunto_activity.class);
        startActivity(intent);
        finish();
    }

    public void alertaCerrar4() {
        GlobalClass.id_otroDispositivo--;

        Intent intent = new Intent();
        intent.setClass(Resumen_activity.this, OtroDispositivo_activity.class);
        startActivity(intent);
        finish();
    }

    public class MyOnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (parent.getItemAtPosition(pos).toString().compareTo("0") == 0) {
                activatextosincodigo();
                GlobalClass.rele_1 = "NO_CONECTA";
                GlobalClass.rele_2 = "NO_CONECTA";
                GlobalClass.rele_3 = "NO_CONECTA";
                GlobalClass.rele_4 = "NO_CONECTA";
                GlobalClass.rele_5 = "NO_CONECTA";
                GlobalClass.rele_6 = "NO_CONECTA";
                GlobalClass.rele_7 = "NO_CONECTA";
            }
            if (parent.getItemAtPosition(pos).toString().compareTo("1") == 0) {
                activatextosincodigo1();
                GlobalClass.rele_2 = "NO_CONECTA";
                GlobalClass.rele_3 = "NO_CONECTA";
                GlobalClass.rele_4 = "NO_CONECTA";
                GlobalClass.rele_5 = "NO_CONECTA";
                GlobalClass.rele_6 = "NO_CONECTA";
                GlobalClass.rele_7 = "NO_CONECTA";
            }
            if (parent.getItemAtPosition(pos).toString().compareTo("2") == 0) {
                activatextosincodigo2();
                GlobalClass.rele_3 = "NO_CONECTA";
                GlobalClass.rele_4 = "NO_CONECTA";
                GlobalClass.rele_5 = "NO_CONECTA";
                GlobalClass.rele_6 = "NO_CONECTA";
                GlobalClass.rele_7 = "NO_CONECTA";
            }
            if (parent.getItemAtPosition(pos).toString().compareTo("3") == 0) {
                activatextosincodigo3();
                GlobalClass.rele_4 = "NO_CONECTA";
                GlobalClass.rele_5 = "NO_CONECTA";
                GlobalClass.rele_6 = "NO_CONECTA";
                GlobalClass.rele_7 = "NO_CONECTA";
            }
            if (parent.getItemAtPosition(pos).toString().compareTo("4") == 0) {
                activatextosincodigo4();
                GlobalClass.rele_5 = "NO_CONECTA";
                GlobalClass.rele_6 = "NO_CONECTA";
                GlobalClass.rele_7 = "NO_CONECTA";
            }
            if (parent.getItemAtPosition(pos).toString().compareTo("5") == 0) {
                activatextosincodigo5();
                GlobalClass.rele_6 = "NO_CONECTA";
                GlobalClass.rele_7 = "NO_CONECTA";
            }
            if (parent.getItemAtPosition(pos).toString().compareTo("6") == 0) {
                activatextosincodigo6();
                GlobalClass.rele_7 = "NO_CONECTA";
            }
            if (parent.getItemAtPosition(pos).toString().compareTo("7") == 0) {
                activatextosincodigo7();
            }

            GlobalClass.control = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public void activatextosincodigo() {
        spinner1.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner1)).setVisibility(View.GONE);
        spinner2.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner2)).setVisibility(View.GONE);
        spinner3.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner3)).setVisibility(View.GONE);
        spinner4.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner4)).setVisibility(View.GONE);
        spinner5.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner5)).setVisibility(View.GONE);
        spinner6.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner6)).setVisibility(View.GONE);
        spinner7.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner7)).setVisibility(View.GONE);


    }

    public void activatextosincodigo1() {
        spinner1.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner1)).setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner2)).setVisibility(View.GONE);
        spinner3.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner3)).setVisibility(View.GONE);
        spinner4.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner4)).setVisibility(View.GONE);
        spinner5.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner5)).setVisibility(View.GONE);
        spinner6.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner6)).setVisibility(View.GONE);
        spinner7.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner7)).setVisibility(View.GONE);
    }

    public void activatextosincodigo2() {
        spinner1.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner1)).setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner2)).setVisibility(View.VISIBLE);
        spinner3.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner3)).setVisibility(View.GONE);
        spinner4.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner4)).setVisibility(View.GONE);
        spinner5.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner5)).setVisibility(View.GONE);
        spinner6.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner6)).setVisibility(View.GONE);
        spinner7.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner7)).setVisibility(View.GONE);
    }

    public void activatextosincodigo3() {
        spinner1.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner1)).setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner2)).setVisibility(View.VISIBLE);
        spinner3.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner3)).setVisibility(View.VISIBLE);
        spinner4.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner4)).setVisibility(View.GONE);
        spinner5.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner5)).setVisibility(View.GONE);
        spinner6.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner6)).setVisibility(View.GONE);
        spinner7.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner7)).setVisibility(View.GONE);
    }

    public void activatextosincodigo4() {
        spinner1.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner1)).setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner2)).setVisibility(View.VISIBLE);
        spinner3.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner3)).setVisibility(View.VISIBLE);
        spinner4.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner4)).setVisibility(View.VISIBLE);
        spinner5.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner5)).setVisibility(View.GONE);
        spinner6.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner6)).setVisibility(View.GONE);
        spinner7.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner7)).setVisibility(View.GONE);
    }

    public void activatextosincodigo5() {
        spinner1.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner1)).setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner2)).setVisibility(View.VISIBLE);
        spinner3.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner3)).setVisibility(View.VISIBLE);
        spinner4.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner4)).setVisibility(View.VISIBLE);
        spinner5.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner5)).setVisibility(View.VISIBLE);
        spinner6.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner6)).setVisibility(View.GONE);
        spinner7.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner7)).setVisibility(View.GONE);
    }

    public void activatextosincodigo6() {
        spinner1.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner1)).setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner2)).setVisibility(View.VISIBLE);
        spinner3.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner3)).setVisibility(View.VISIBLE);
        spinner4.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner4)).setVisibility(View.VISIBLE);
        spinner5.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner5)).setVisibility(View.VISIBLE);
        spinner6.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner6)).setVisibility(View.VISIBLE);
        spinner7.setVisibility(View.GONE);
        ((TextView) this.findViewById(R.id.textSpinner7)).setVisibility(View.GONE);
    }

    public void activatextosincodigo7() {
        spinner1.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner1)).setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner2)).setVisibility(View.VISIBLE);
        spinner3.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner3)).setVisibility(View.VISIBLE);
        spinner4.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner4)).setVisibility(View.VISIBLE);
        spinner5.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner5)).setVisibility(View.VISIBLE);
        spinner6.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner6)).setVisibility(View.VISIBLE);
        spinner7.setVisibility(View.VISIBLE);
        ((TextView) this.findViewById(R.id.textSpinner7)).setVisibility(View.VISIBLE);

    }

    public class MyOnItemSelectedListener1 implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            GlobalClass.rele_1 = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public class MyOnItemSelectedListener2 implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            GlobalClass.rele_2 = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public class MyOnItemSelectedListener3 implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            GlobalClass.rele_3 = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public class MyOnItemSelectedListener4 implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            GlobalClass.rele_4 = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public class MyOnItemSelectedListener5 implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            GlobalClass.rele_5 = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public class MyOnItemSelectedListener6 implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            GlobalClass.rele_6 = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public class MyOnItemSelectedListener7 implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            GlobalClass.rele_7 = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public class MyOnItemSelectedListener8 implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            GlobalClass.regulacion = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    public void actualizatexto() {
        if (GlobalClass.fuente_tipo.compareTo("Selecciona:") != 0) {
            ((TextView) this.findViewById(R.id.textLum)).setText(GlobalClass.fuente_tipo);
            ((TextView) this.findViewById(R.id.textLum)).setTextColor(getResources().getColor(R.color.dark_green));
            ((ImageButton) this.findViewById(R.id.continueButton)).setVisibility(View.VISIBLE);
        }

        if (GlobalClass.balasto_potencia != 0) {
            ((TextView) this.findViewById(R.id.textPot)).setText(GlobalClass.balasto_potencia + " W");
            ((TextView) this.findViewById(R.id.textPot)).setTextColor(getResources().getColor(R.color.dark_green));
        } else {
            ((TextView) this.findViewById(R.id.textPot)).setTextColor(getResources().getColor(R.color.black));

        }

    }

    public void actualizatexto2() {
        if (GlobalClass.balasto2_potencia != 0) {
            ((TextView) this.findViewById(R.id.textPot2)).setText(GlobalClass.balasto2_potencia + " W");
            ((TextView) this.findViewById(R.id.textPot2)).setTextColor(getResources().getColor(R.color.dark_green));

        } else {
            ((TextView) this.findViewById(R.id.textPot2)).setTextColor(getResources().getColor(R.color.black));

        }
    }

    public void lanzaaviso2() {
        AlertDialog.Builder popup = new AlertDialog.Builder(this);
        popup.setTitle("Debe seleccionar una opci�n correcta");
        popup.setMessage("Por favor, selecciona una opci�n correcta");
        popup.setPositiveButton("Ok", null);
        popup.show();
    }

    public String reemplazarCaracteresEspeciales(String input) {
        String original = "��������������u���������������������";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcCnN";
        String output = input;
        for (int i = 0; i < original.length(); i++) {
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return output;
    }
	
	/*public void mostrarmensaje()
	{
		Toast.makeText(this,"El punto "+GlobalClass.nombre_punto+" ha sido guardado correctamente.",Toast.LENGTH_SHORT).show();
	}
	
	public void mostrarmensaje2()
	{
		Toast.makeText(this,"El cuadro "+GlobalClass.nombre_punto+" ha sido guardado correctamente.",Toast.LENGTH_SHORT).show();
	}
	
	public void mostrarmensaje3()
	{
		Toast.makeText(this,"El punto de acceso "+GlobalClass.nombre_punto+" ha sido guardado correctamente.",Toast.LENGTH_SHORT).show();
	}
	
	public void mostrarmensaje4()
	{
		Toast.makeText(this,"El dispositivo "+GlobalClass.nombre_punto+" ha sido guardado correctamente.",Toast.LENGTH_SHORT).show();
	}*/

    public boolean createFile(String tipo, String objJson) {
        boolean flag = false;
        //String fecha = ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",Locale.GERMANY)).format(new Date())).toString();
        //String fech = fecha.substring(0, 19);
        String fecha = ((new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(new Date())).toString());

        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
        String deviceId = telephonyManager.getDeviceId();

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //Toast.makeText(this, "Tarjeta SD no montada", Toast.LENGTH_LONG).show();
        } else {
            File nmea_file;
            File root = Environment.getExternalStorageDirectory();
            FileWriter nmea_writer = null;
            try {
                // create a File object for the parent directory
                File logsDirectory = new File(root + "/sinapse/install/");
                // have the object build the directory structure, if needed.
                logsDirectory.mkdirs();

                nmea_file = new File(logsDirectory, fecha + "-backup-" + tipo + "-" + deviceId + ".json");

                // Almaceno en la cadena fichero la ruta del fichero que
                // tendremos que subir al FTP
                GlobalClass.global_fichero_B = logsDirectory + "/" + fecha + "-backup-" + tipo + "-" + deviceId + ".json";

                if (!nmea_file.exists()) {
                    flag = nmea_file.createNewFile();
                    //Toast.makeText(this,"El fichero no existia, por lo que se ha creado",Toast.LENGTH_LONG).show();

                    nmea_writer = new FileWriter(nmea_file, true);
                    CharSequence nmea = objJson;

                    nmea_writer.append(nmea);
                    nmea_writer.flush();
                }
            } catch (IOException ex) {
                //Toast.makeText(this,"Imposible escribir en la tarjeta de memoria",Toast.LENGTH_LONG).show();
                //Toast.makeText(this, ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            } finally {
                if (nmea_writer != null) {
                    try {
                        nmea_writer.close();
                    } catch (IOException e) {
                        //Toast.makeText(this, "Error cerrando el fichero",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        return flag;

    }

    public boolean anaderegistro(String tipo, String linea) {
        boolean flag = false;
        String fecha = ((new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(new Date())).toString());

        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
        String deviceId = telephonyManager.getDeviceId();

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //Toast.makeText(this, "Tarjeta SD no montada", Toast.LENGTH_LONG).show();
        } else {
            File nmea_file;
            File root = Environment.getExternalStorageDirectory();
            FileWriter nmea_writer = null;
            try {
                // create a File object for the parent directory
                File logsDirectory = new File(root + "/sinapse/install/");
                // Almaceno en la cadena fichero la ruta del fichero que
                // tendremos que subir al FTP
                GlobalClass.global_fichero_B = logsDirectory + "/" + fecha + "-backup-" + tipo + "-" + deviceId + ".json";
                // have the object build the directory structure, if needed.
                logsDirectory.mkdirs();

                nmea_file = new File(logsDirectory, fecha + "-backup-" + tipo + "-" + deviceId + ".json");
                if (!nmea_file.exists()) {
                    flag = nmea_file.createNewFile();
                }
                nmea_writer = new FileWriter(nmea_file, true);
                CharSequence nmea = linea;

                nmea_writer.append(nmea);
                nmea_writer.flush();
            } catch (IOException ex) {
                //Toast.makeText(this,"Imposible escribir en la tarjeta de memoria",Toast.LENGTH_LONG).show();
                //Toast.makeText(this, ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            } finally {
                if (nmea_writer != null) {
                    try {
                        nmea_writer.close();
                    } catch (IOException e) {
                        //Toast.makeText(this, "Error cerrando el fichero",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        return flag;

    }

    //Metodo para subir ficheros FTP
    public void uploadFTP(String file) {
        org.apache.commons.net.ftp.FTPClient con = new org.apache.commons.net.ftp.FTPClient();
        try {
            con.connect("89.248.100.11");
            if (con.login("trazabilidad", "napse1si")) {
                //creamos en el servidor una carpeta con el nombre del lugar de instalaci�n
                con.makeDirectory("/Instalacion-" + GlobalClass.global_localiz);
                con.enterLocalPassiveMode(); // important!
                /*
                 * String data = file; ByteArrayInputStream in = new
                 * ByteArrayInputStream(data.getBytes());
                 */
                con.changeWorkingDirectory("/Instalacion-" + GlobalClass.global_localiz);

                BufferedInputStream buffIn = null;
                File fichero = new File(file);

                buffIn = new BufferedInputStream(new FileInputStream(fichero));
                con.enterLocalPassiveMode();
                boolean result = con.storeFile(fichero.getName().toString(), buffIn);
                Log.i("Estado", "Subiendo archivo" + fichero.getName().toString());
                buffIn.close();
                if (result) {
                    //Toast.makeText(this, "FTP actualizado", Toast.LENGTH_LONG).show();
                }

            }
        } catch (Exception e) {
            //Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            try {
                con.connect("ftp.tgame.es");
                if (con.login("tgame_7626990", "napse1si")) {
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
                    if (result) {
                        //Toast.makeText(this, "FTP actualizado",Toast.LENGTH_LONG).show();
                    }

                }
            } catch (Exception ex) {
                //Toast.makeText(this, ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }

        try {
            con.logout();
            con.disconnect();
        } catch (Exception e) {
            //Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
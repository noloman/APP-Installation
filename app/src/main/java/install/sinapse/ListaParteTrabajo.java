package install.sinapse;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.FileProvider.getUriForFile;


public class ListaParteTrabajo extends ListActivity {
    private static final int READ_EXTERNAL_STORAGE_PERMISSION = 0x01;
    String[] lista_archivos;
    long[] archivos_seleccionados;
    static File root = Environment.getExternalStorageDirectory();        //Accedemos a la tarjeta SD
    private final static String NOMBRE_DIRECTORIO = root + "/sinapse/partes_trabajo/";
    ListView LvParteTrabajo;
    List<String> pdfFileNameList = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.lista_parte_trabajo);
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(ListaParteTrabajo.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ListaParteTrabajo.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION);
        } else {
            generatePdfFromFileList();
        }
    }

    private void generatePdfFromFileList() {
        lista_archivos = Listar();
        Button bGenerarPDF = (Button) findViewById(R.id.bGenerarPDF);

        LvParteTrabajo = getListView();
        LvParteTrabajo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        LvParteTrabajo.setTextFilterEnabled(true);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, lista_archivos));

        bGenerarPDF.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                archivos_seleccionados = LvParteTrabajo.getCheckItemIds();
                pdfFileNameList.clear();
                alertaComentario();
            }

        });
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        //CheckedTextView item = (CheckedTextView) v;
        //Toast.makeText(this, lista_archivos[position] + " checked : "+ !item.isChecked(), Toast.LENGTH_SHORT).show();
    }

    public void GenerarPDF() {
        for (int i = 0; i < archivos_seleccionados.length; ++i) {
            Log.i("Posicion", "" + archivos_seleccionados[i]);
            Integer posicion = (int) archivos_seleccionados[i];
            String nombre_archivo = lista_archivos[posicion].substring(0, lista_archivos[posicion].length() - 5);
            String fichero_pdf = FicheroPDF(nombre_archivo + ".pdf");
            //Uri uriToPDF1 = Uri.parse("file:///storage/emulated/0/sinapse/partes_trabajo/352750068181779-PRUEBA-2014-12-11.pdf");
            // /storage/emulated/0/sinapse/partes_trabajo/352750068181779-PRUEBA-2014-12-11.pdf
//            Uri uri_archivo = Uri.parse("file://" + NOMBRE_DIRECTORIO + fichero_pdf);
//            Log.i("Archivo", uri_archivo.getPath().toString());
            pdfFileNameList.add(fichero_pdf);
        }

        // Reemplazamos el email por algun otro real
        String tema = "Partes de Trabajo";
        String mensaje = "En este mail adjunto los partes de trabajo de hoy";
        String[] cc = {"manueljoseacosta86@gmail.com", "mjacosta86@icloud.com"};
        //"alfonso.dominguez@sinapseenergia.com","c.moreno@sinapseenergia.com"
        String[] to = {"partes@sinapseenergia.com"};
        EnviarEmail(to, cc, tema, mensaje);
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public String[] Listar() {
        String[] directorio_archivos = null;
        Integer num_archivos = 0;
        File logsDirectory = new File(Environment.getExternalStorageDirectory() + "/sinapse/install/");    //Direcci�n del directorio

        directorio_archivos = logsDirectory.list();
        if (directorio_archivos != null && directorio_archivos.length > 0) {
            for (int i = 0; i < directorio_archivos.length; ++i) {
                if (directorio_archivos[i].contains("install")) {
                    ++num_archivos;
                }
            }
        }
        String[] lista = new String[num_archivos];
        int x = 0;

        for (int i = 0; i < directorio_archivos.length; ++i) {
            if (directorio_archivos[i].contains("install")) {
                lista[x] = directorio_archivos[i];
                Log.i("Archivo: ", lista[x]);
                ++x;
            }
        }

        return lista;
    }

    public void EnviarEmail(String[] to, String[] cc, String asunto, String mensaje) {
        if (pdfFileNameList != null && pdfFileNameList.size() > 0) {
            List<Uri> finalUriArray = new ArrayList();
            for (int i = 0; i < pdfFileNameList.size(); ++i) {
                Log.w("Fichero" + i, pdfFileNameList.get(i));
                File file = new File(Environment.getExternalStorageDirectory() + "/sinapse/partes_trabajo/", pdfFileNameList.get(i));
                Uri uriFromFile = getUriForFile(ListaParteTrabajo.this,
                        GenericFileProvider.class.getName(),
                        file);
                finalUriArray.add(uriFromFile);
            }

            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            //emailIntent.putExtra(Intent.EXTRA_CC, cc);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, (ArrayList<? extends Parcelable>) finalUriArray);
            emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
            emailIntent.setType("application/pdf");
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(emailIntent, "Email "));
        }
    }

    // Crea una alerta
    private void alertaComentario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("�Desea a�adir un comentario?")
                .setTitle("Comentario")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        MostrarPantallaComentario();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        GenerarPDF();
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void MostrarPantallaComentario() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.comentario_dialog);
        dialog.setTitle("Comentario");
        final EditText comentario = (EditText) dialog.findViewById(R.id.etComentario);
        Button enviar = (Button) dialog.findViewById(R.id.bComentario);

        enviar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalClass.comentario = comentario.getText().toString();
                GenerarPDF();
                dialog.cancel();
            }

        });

        dialog.show();

    }

    public String FicheroPDF(String nombre_documento) {
        String fichero_pdf;
        try {
            Integer[] potencias = new Integer[8];
            potencias[0] = 0;
            potencias[1] = 0;
            potencias[2] = 0;
            potencias[3] = 0;
            potencias[4] = 0;
            potencias[5] = 0;
            potencias[6] = 0;
            potencias[7] = 0;
            //Obtenemos la fecha actual
            String fecha = ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY).format(new Date())).toString());
            //Obtenemos el nombre de la instalacion
            //String instalacion = GlobalClass.global_localiz;
            //Obtenemos los datos del IMEI
            Log.i("Comentario", GlobalClass.comentario);
            String srvcName = Context.TELEPHONY_SERVICE;
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
            String imei = telephonyManager.getDeviceId();
            //Fichero
            File root = Environment.getExternalStorageDirectory();
            File logsDirectory = new File(root + "/sinapse/install/");
            String fich = nombre_documento.substring(0, nombre_documento.length() - 4);
            String sFichero = logsDirectory + "/" + fich + ".json";
            //Obtenemos los datos m�s importante del archivo .json
            String[] datos = datosFicheroOrigen(sFichero); //Le paso ya el fihcero json, MODIFICAR LA LECTURA DEL METODO
            //*********************************************************************
            //******************************
            String fecha_comienzo = datos[0];
            String fecha_final = datos[1];
            Integer equipos_instalados = Integer.parseInt(datos[2]);
            String instalacion = datos[3];
            ArrayList<LineaParteTrabajo> lineas = recogerDatos(sFichero);
            ArrayList<LineaParteTrabajoCuadro> lineas2 = recogerDatos2(sFichero);
            ArrayList<LineaParteTrabajoPA> lineas3 = recogerDatos3(sFichero);
            ArrayList<LineaParteTrabajoPA> lineas4 = recogerDatos4(sFichero);

            //Ponemos el nombre del documento pdf
            fichero_pdf = imei + "-" + instalacion + "-" + nombre_documento.substring(0, 19) + ".pdf";

            equipos_instalados = lineas.size() + lineas2.size() + lineas3.size() + lineas4.size();

            // Creamos el documento.
            Document documento = new Document();
            // Creamos el fichero con el nombre que deseemos.
            File f = null;
            f = crearFichero(fichero_pdf);
            FileOutputStream ficheroPdf = null;
            // Creamos el flujo de datos de salida para el fichero donde guardaremos el pdf.
            ficheroPdf = new FileOutputStream(f.getAbsolutePath());
            // Asociamos el flujo que acabamos de crear al documento.
            PdfWriter.getInstance(documento, ficheroPdf);
            // Abrimos el documento.
            documento.open();

            // Insertamos una imagen que se encuentra en los recursos de la aplicaci�n.
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sinapse);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image imagen = Image.getInstance(stream.toByteArray());
            //imagen.setAbsolutePosition(300f, 675f);
            imagen.setAlignment(Element.ALIGN_RIGHT);
            documento.add(imagen);

            // A�adimos un t�tulo con la fuente por defecto.

            // A�adimos un t�tulo con una fuente personalizada.
            Font font_parrafo = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);

            documento.add(new Paragraph("FECHA: " + fecha));
            documento.add(new Paragraph("IMEI: " + imei));
            documento.add(new Paragraph("INSTALACI�N: " + instalacion));
            documento.add(new Paragraph("FECHA COMIENZO: " + fecha_comienzo));
            documento.add(new Paragraph("FECHA FINAL: " + fecha_final));
            documento.add(new Paragraph("Numero de equipos instalados: " + equipos_instalados));
            documento.add(new Paragraph("                                                   "));

            addEmptyLine(new Paragraph("                                                    "), 3);


            documento.add(new Paragraph("                                                   "));
            documento.add(new Paragraph("PUNTOS", font_parrafo));
            documento.add(new Paragraph("                                                   "));
            //addEmptyLine(new Paragraph("                                                    "),3);

            // Insertamos una tabla.
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);

            Paragraph serial = new Paragraph("Trazabilidad_Punto", font_parrafo);
            tabla.addCell(serial);
            Paragraph nombre = new Paragraph("Nombre_Punto", font_parrafo);
            tabla.addCell(nombre);
            Paragraph pot = new Paragraph("Potencia_Fuente", font_parrafo);
            tabla.addCell(pot);
            Paragraph luminaria = new Paragraph("Luminaria", font_parrafo);
            tabla.addCell(luminaria);
            tabla.completeRow();
            for (int i = 0; i < lineas.size(); ++i) {

                tabla.addCell(lineas.get(i).getPunto_Serial());
                tabla.addCell(lineas.get(i).getPunto_Nom());
                tabla.addCell(String.valueOf(lineas.get(i).getPunto_Pot()));
                tabla.addCell(lineas.get(i).getLuminaria());
                tabla.completeRow();
            }

            documento.add(tabla);

            documento.add(new Paragraph("                                                   "));
            documento.add(new Paragraph("CUADROS", font_parrafo));
            documento.add(new Paragraph("                                                   "));
            //addEmptyLine(new Paragraph("                                                    "),3);

            // Insertamos una tabla.
            PdfPTable tabla2 = new PdfPTable(4);
            tabla2.setWidthPercentage(100);

            Paragraph cuadro_serial = new Paragraph("Trazabilidad_CMC", font_parrafo);
            tabla2.addCell(cuadro_serial);
            Paragraph pc = new Paragraph("PC_Ref", font_parrafo);
            tabla2.addCell(pc);
            Paragraph nombre_cuadro = new Paragraph("Nombre_Cuadro", font_parrafo);
            tabla2.addCell(nombre_cuadro);
            Paragraph circuitos = new Paragraph("Numero_Circuitos", font_parrafo);
            tabla2.addCell(circuitos);
            tabla2.completeRow();
            for (int i = 0; i < lineas2.size(); ++i) {

                tabla2.addCell(lineas2.get(i).getCmc_Serial());
                tabla2.addCell(lineas2.get(i).getP_C());
                tabla2.addCell(lineas2.get(i).getNombre());
                tabla2.addCell(String.valueOf(lineas2.get(i).getCircuitos()));
                tabla2.completeRow();
            }

            documento.add(tabla2);

            documento.add(new Paragraph("                                                   "));
            documento.add(new Paragraph("PUNTOS_ACCESO", font_parrafo));
            documento.add(new Paragraph("                                                   "));
            //addEmptyLine(new Paragraph("                                                    "),3);

            // Insertamos una tabla.
            PdfPTable tabla3 = new PdfPTable(2);
            tabla3.setWidthPercentage(100);

            Paragraph pa_serial = new Paragraph("Trazabilidad_PA", font_parrafo);
            tabla3.addCell(pa_serial);
            Paragraph nombre_pa = new Paragraph("Nombre_PA", font_parrafo);
            tabla3.addCell(nombre_pa);
            tabla3.completeRow();
            for (int i = 0; i < lineas3.size(); ++i) {

                tabla3.addCell(lineas3.get(i).getPA_Serial());
                tabla3.addCell(lineas3.get(i).getNombre());
                tabla3.completeRow();
            }

            documento.add(tabla3);

            documento.add(new Paragraph("                                                   "));
            documento.add(new Paragraph("OTROS_DISPOSITIVOS", font_parrafo));
            documento.add(new Paragraph("                                                   "));
            //addEmptyLine(new Paragraph("                                                    "),3);

            // Insertamos una tabla.
            PdfPTable tabla4 = new PdfPTable(2);
            tabla4.setWidthPercentage(100);

            Paragraph otro_serial = new Paragraph("Trazabilidad", font_parrafo);
            tabla4.addCell(otro_serial);
            Paragraph nombre_otro = new Paragraph("Nombre", font_parrafo);
            tabla4.addCell(nombre_otro);
            tabla4.completeRow();
            for (int i = 0; i < lineas4.size(); ++i) {

                tabla4.addCell(lineas4.get(i).getPA_Serial());
                tabla4.addCell(lineas4.get(i).getNombre());
                tabla4.completeRow();
            }

            documento.add(tabla4);
			/*for(int i = 0; i < lineas.size(); ++i)
			{
				
				if(i > 0)
				{
					Log.i("Potencia", String.valueOf(lineas.get(i).getPunto_Pot()));
					int p = lineas.get(i).getPunto_Pot();
					
					switch (p) 
					{
						case 35:
		            	++potencias[0];
		            	break;
					
						case 40:
		            	++potencias[1];
		            	break;
					
						case 50:
		            	++potencias[2];
		            	break;
					
						case 70:
		            	++potencias[3];
		            	break;
		            
		            	case 100: 
		            	++potencias[4];
		            	break;
		            
		            	case 150:
		            	++potencias[5];
		            	break;
		            	
		            	case 250: 
		            	++potencias[6]; 
			            break;
			            
			            case 400:
			            ++potencias[7];
			            break;
		            
		            	default: 
		            	break;
					}
				}
				
				
			}
			
			documento.add(new Paragraph(""));
			documento.add(new Paragraph("                                                   "));
			documento.add(new Paragraph("*************************************************POTENCIAS************************************************"));
			
			documento.add(new Paragraph("35:  " + potencias[0]+" dispositivos"));
			documento.add(new Paragraph("40:  " + potencias[1]+" dispositivos"));
			documento.add(new Paragraph("50:  " + potencias[2]+" dispositivos"));
			documento.add(new Paragraph("70:  " + potencias[3]+" dispositivos"));
			documento.add(new Paragraph("100: " + potencias[4]+" dispositivos"));
			documento.add(new Paragraph("150: " + potencias[5]+" dispositivos"));
			documento.add(new Paragraph("250: " + potencias[6]+" dispositivos"));
			documento.add(new Paragraph("400: " + potencias[7]+" dispositivos"));
			
			documento.add(new Paragraph("***************************************************************************************************************"));
			*/

            documento.add(new Paragraph("                                                   "));
            //documento.add(new Paragraph("                                                   "));
            documento.add(new Paragraph("_____________________________________________________________________________"));
            documento.add(new Paragraph("Comentarios: " + GlobalClass.comentario));
            documento.add(new Paragraph("_____________________________________________________________________________"));
            // A�adimos un t�tulo con una fuente personalizada.
            //Font font = FontFactory.getFont(FontFactory.HELVETICA, 28,Font.BOLD);
            //documento.add(new Paragraph("T�tulo personalizado", font));
            // Cerramos el documento.
            documento.close();

            return fichero_pdf;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error", "IOException e");
        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("Error", "DocumentException e");
        }
        return nombre_documento;


    }

    //Procedimiento para crear una lines vac�a
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public String[] datosFicheroOrigen(String nombre_documento) throws java.io.IOException {
        String fecha_comienzo = "";
        String fecha_final = "";
        String instalacion = "";
        int tam = 0;
        String[] datos = new String[4];
        JsonReader reader = null;
        JsonParser parser = new JsonParser();

        try {
            reader = new JsonReader(new InputStreamReader(new FileInputStream(nombre_documento), "UTF-8"));
            JsonElement elementos = parser.parse(reader);
            tam = dumpJSONElement(elementos);

            fecha_comienzo = nombre_documento.substring(36, 46);
            fecha_final = nombre_documento.substring(36, 46);
            instalacion = GlobalClass.global_localiz;

            datos[0] = fecha_comienzo;
            datos[1] = fecha_final;
            datos[2] = tam + "";
            //datos[2]="20";
            datos[3] = instalacion;
            //BufferedReader archivoJsonBufferedReader = new BufferedReader(new FileReader(logsDirectory+"/"+archivo.getName()));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return datos;
    }

    public int dumpJSONElement(JsonElement elemento) {
        int n = 0;

        if (elemento.isJsonObject()) {
            JsonObject obj = elemento.getAsJsonObject();
            java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
            java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                //System.out.println("Clave: " + entrada.getKey());
                //System.out.println("Valor:");
                dumpJSONElement(entrada.getValue());
            }

        } else if (elemento.isJsonArray()) {
            JsonArray array = elemento.getAsJsonArray();
            n = n + array.size();
            //System.out.println("Es array. Numero de elementos: " + array.size());
            java.util.Iterator<JsonElement> iter = array.iterator();
            while (iter.hasNext()) {
                JsonElement entrada = iter.next();
                dumpJSONElement(entrada);
            }
        } else if (elemento.isJsonPrimitive()) {
            JsonPrimitive valor = elemento.getAsJsonPrimitive();
            if (valor.isBoolean()) {
                //System.out.println("Es booleano: " + valor.getAsBoolean());
            } else if (valor.isNumber()) {
                //System.out.println("Es numero: " + valor.getAsNumber());
            } else if (valor.isString()) {
                //System.out.println("Es texto: " + valor.getAsString());
            }
        } else if (elemento.isJsonNull()) {
            System.out.println("Es NULL");
        } else {
            System.out.println("Es otra cosa");
        }

        return n;
    }


    public ArrayList<LineaParteTrabajo> recogerDatos(String nombre_documento) throws JsonIOException, JsonSyntaxException, IOException {
        ArrayList<LineaParteTrabajo> lista = new ArrayList<LineaParteTrabajo>();
        JsonReader reader = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        reader = new JsonReader(new InputStreamReader(new FileInputStream(nombre_documento), "UTF-8"));

        Instalacion instalacion = gson.fromJson(reader, Instalacion.class);

        for (int i = 0; i < instalacion.getPuntos().getMCBE().size(); i++) {
            LineaParteTrabajo l = new LineaParteTrabajo();
            l.setPunto_Serial(instalacion.getPuntos().getMCBE().get(i).getTRAZABILIDAD());
            l.setPunto_Nom(instalacion.getPuntos().getMCBE().get(i).getNOMBRE());
            l.setPunto_Pot(instalacion.getPuntos().getMCBE().get(i).getPUNTO_FISICO_LUMINARIA().getFUENTE_LUZ_POTENCIA());
            l.setLuminaria(instalacion.getPuntos().getMCBE().get(i).getPUNTO_FISICO_LUMINARIA().getLUMINARIA_TIPO());
            lista.add(l);
        }

        for (int i = 0; i < instalacion.getPuntos().getMC3P().size(); i++) {
            LineaParteTrabajo l = new LineaParteTrabajo();
            l.setPunto_Serial(instalacion.getPuntos().getMC3P().get(i).getTRAZABILIDAD());
            l.setPunto_Nom(instalacion.getPuntos().getMC3P().get(i).getNOMBRE());
            l.setPunto_Pot(instalacion.getPuntos().getMC3P().get(i).getPUNTO_FISICO_LUMINARIA().getFUENTE_LUZ_POTENCIA());
            l.setLuminaria(instalacion.getPuntos().getMC3P().get(i).getPUNTO_FISICO_LUMINARIA().getLUMINARIA_TIPO());
            lista.add(l);
        }

        for (int i = 0; i < instalacion.getPuntos().getDE().size(); i++) {
            LineaParteTrabajo l = new LineaParteTrabajo();
            l.setPunto_Serial(instalacion.getPuntos().getDE().get(i).getTRAZABILIDAD());
            l.setPunto_Nom(instalacion.getPuntos().getDE().get(i).getNOMBRE());
            l.setPunto_Pot(instalacion.getPuntos().getDE().get(i).getPUNTO_FISICO_LUMINARIA().getFUENTE_LUZ_POTENCIA());
            l.setLuminaria(instalacion.getPuntos().getDE().get(i).getPUNTO_FISICO_LUMINARIA().getLUMINARIA_TIPO());
            lista.add(l);
        }

        for (int i = 0; i < instalacion.getPuntos().getBE().size(); i++) {
            LineaParteTrabajo l = new LineaParteTrabajo();
            l.setPunto_Serial(instalacion.getPuntos().getBE().get(i).getTRAZABILIDAD());
            l.setPunto_Nom(instalacion.getPuntos().getBE().get(i).getNOMBRE());
            l.setPunto_Pot(instalacion.getPuntos().getBE().get(i).getPUNTO_FISICO_LUMINARIA().getFUENTE_LUZ_POTENCIA());
            l.setLuminaria(instalacion.getPuntos().getBE().get(i).getPUNTO_FISICO_LUMINARIA().getLUMINARIA_TIPO());
            lista.add(l);
        }


        return lista;

    }

    public ArrayList<LineaParteTrabajoCuadro> recogerDatos2(String nombre_documento) throws JsonIOException, JsonSyntaxException, IOException {
        ArrayList<LineaParteTrabajoCuadro> lista = new ArrayList<LineaParteTrabajoCuadro>();
        JsonReader reader = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        reader = new JsonReader(new InputStreamReader(new FileInputStream(nombre_documento), "UTF-8"));

        Instalacion instalacion = gson.fromJson(reader, Instalacion.class);

        for (int i = 0; i < instalacion.getCuadros().getCuadros().size(); i++) {
            LineaParteTrabajoCuadro l = new LineaParteTrabajoCuadro();

            l.setCmc_Serial(instalacion.getCuadros().getCuadros().get(i).getTRAZABILIDAD());
            l.setP_C(instalacion.getCuadros().getCuadros().get(i).getPC().getTRAZABILIDAD());
            l.setNombre(instalacion.getCuadros().getCuadros().get(i).getNOMBRE());
            l.setCircuitos(instalacion.getCuadros().getCuadros().get(i).getPUNTO_FISICO_CUADRO().getCUADRO_NUMERO_DE_CIRCUITOS());
            lista.add(l);
        }

        return lista;
    }

    public ArrayList<LineaParteTrabajoPA> recogerDatos3(String nombre_documento) throws JsonIOException, JsonSyntaxException, IOException {
        ArrayList<LineaParteTrabajoPA> lista = new ArrayList<LineaParteTrabajoPA>();
        JsonReader reader = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        reader = new JsonReader(new InputStreamReader(new FileInputStream(nombre_documento), "UTF-8"));

        Instalacion instalacion = gson.fromJson(reader, Instalacion.class);

        for (int i = 0; i < instalacion.getPuntos_Acceso().getPC().size(); i++) {
            LineaParteTrabajoPA l = new LineaParteTrabajoPA();

            l.setPA_Serial(instalacion.getPuntos_Acceso().getPC().get(i).getTRAZABILIDAD());
            l.setNombre(instalacion.getPuntos_Acceso().getPC().get(i).getNOMBRE());
            lista.add(l);
        }

        for (int i = 0; i < instalacion.getPuntos_Acceso().getBR().size(); i++) {
            LineaParteTrabajoPA l = new LineaParteTrabajoPA();

            l.setPA_Serial(instalacion.getPuntos_Acceso().getBR().get(i).getTRAZABILIDAD());
            l.setNombre(instalacion.getPuntos_Acceso().getBR().get(i).getNOMBRE());
            lista.add(l);
        }

        return lista;
    }

    public ArrayList<LineaParteTrabajoPA> recogerDatos4(String nombre_documento) throws JsonIOException, JsonSyntaxException, IOException {
        ArrayList<LineaParteTrabajoPA> lista = new ArrayList<LineaParteTrabajoPA>();
        JsonReader reader = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        reader = new JsonReader(new InputStreamReader(new FileInputStream(nombre_documento), "UTF-8"));

        Instalacion instalacion = gson.fromJson(reader, Instalacion.class);

        for (int i = 0; i < instalacion.getOtros_Dispositivo().size(); i++) {
            LineaParteTrabajoPA l = new LineaParteTrabajoPA();

            l.setPA_Serial(instalacion.getOtros_Dispositivo().get(i).getTrazabilidad());
            l.setNombre(instalacion.getOtros_Dispositivo().get(i).getNombre());
            lista.add(l);
        }


        return lista;
    }

    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null) {
            fichero = new File(ruta, nombreFichero);
        }
        return fichero;
    }

    public static File getRuta() {

        // El fichero ser� almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ruta = new File(NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {

        }

        return ruta;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generatePdfFromFileList();
                }
                return;
            }
        }
    }
}
/*
public String[] datosFicheroOrigen(String nombre_documento)
{
	File root = Environment.getExternalStorageDirectory();
	File logsDirectory = new File(root + "/sinapse/install/"); 	//Direcci�n del directorio
	
	String nombre_csv = nombre_documento.substring(0, nombre_documento.length()-4);
	//nombre_csv.concat(".csv");
	
	File archivo; 
	FileReader fr = null;
    BufferedReader br = null;
    Integer num_lineas = 0;
    String[] datos = new String[4];
    
    try 
    {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).
         archivo = new File (logsDirectory, nombre_csv+".csv");
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);
         String fecha_comienzo = null;
         String fecha_final = null;
         String instalacion = null;
         
         // Lectura del fichero
         String linea;
         while((linea = br.readLine()) != null)
         {
        	 System.out.println(linea);
        	 
        	 if(num_lineas == 1)
        	 {
        		 int veces = 0;
        		 int posicion_fecha = 0;

        		 
        		 for(int i = 0; i < linea.length(); ++i)
        		 {
        			 char caracter = linea.charAt(i);
        			 if(caracter == ';')
        			 {
        				 ++veces;
        				 
        				 if(veces == 4)
        				 {
        					 posicion_fecha = i+1;
        				 }
        				 
        				 
        			 }
        		 }
        		 
        		 fecha_comienzo = linea.substring(posicion_fecha, posicion_fecha + 19);
        		 
        	 }
        	 else if(num_lineas >= 1)
        	 {
        		 int veces = 0;
        		 int posicion_fecha = 0; 
        		 
        		 for(int i = 0; i < linea.length(); ++i)
        		 {
        			 char caracter = linea.charAt(i);
        			 if(caracter == ';')
        			 {
        				 ++veces;
        				 
        				 if(veces == 4)
        				 {
        					 posicion_fecha = i+1;
        				 }
        				 
        			 }
        		 }
        		 
        		 fecha_final = linea.substring(posicion_fecha, posicion_fecha + 19);
        	 }
        	 ++num_lineas;
        	 
         }
         Log.i("Numero de lineas totales", ""+num_lineas);
         Log.i("Numero de lineas",num_lineas-1+"");
         
         Log.i("Fecha comienzo", ""+fecha_comienzo);
         Log.i("Fecha final",fecha_final);
         
         datos[0] = fecha_comienzo;
         datos[1] = fecha_final;
         datos[2] = (num_lineas-1)+"";
     }
     catch(Exception e)
     {
         e.printStackTrace();
     }
     finally
     {
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try
         {                    
            if( null != fr )
            {   
               fr.close();     
            }                  
         }
         catch (Exception e2)
         { 
            e2.printStackTrace();
         }
      }
	return datos;
}*/
//
/*
public String[] recogerDatos(String nombre_documento, Integer instalaciones)
{
	File root = Environment.getExternalStorageDirectory();
	File logsDirectory = new File(root + "/sinapse/install/"); 	//Direcci�n del directorio
	
	String nombre_csv = nombre_documento.substring(0, nombre_documento.length()-4);
	
	File archivo; 
	FileReader fr = null;
    BufferedReader br = null;
    String[] lineas = new String[instalaciones];
    
    try 
    {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).
         archivo = new File (logsDirectory, nombre_csv+".csv");
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);

         
         // Lectura del fichero
         String linea;
         int i = 0;
         int num_lineas = 0;
         while((linea = br.readLine()) != null)
         {
        	 System.out.println(linea);
        	 lineas[i] = linea;
        	 ++i;
        	 
        	 int veces = 0;
        	 for(int i = 0; i < linea.length(); ++i)
        	 {
        		char caracter = linea.charAt(i);
        		if(caracter == ';')
        		{
        			++veces; //sumamos las veces que encontramos un punto y coma
        			if(veces == 1)
        			{
        				lineas[num_lineas].append(caracter);
        			}
        			else if(veces == 2)
        			{
        				lineas[num_lineas].append(caracter);
        			}
        			else if(veces == 3)
        			{
        				lineas[num_lineas].append(caracter);
        			}
        			else if(veces == 16)
        			{
        				lineas[num_lineas].append(caracter);
        			}
        		}
        	 }

        	 ++num_lineas;
        	 
        	 
         }
         
     }
     catch(Exception e)
     {
         e.printStackTrace();
     }
     finally
     {
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try
         {                    
            if( null != fr )
            {   
               fr.close();     
            }                  
         }
         catch (Exception e2)
         { 
            e2.printStackTrace();
         }
      }
	return lineas;
} 

*/
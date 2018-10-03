package install.sinapse;

import java.util.ArrayList;

public class CuadroFisico {
	private int CUADRO_ID;
	private String CUADRO_NOMBRE;
	private String CUADRO_DIRECCION;
	private double CUADRO_LATITUD;
	private double CUADRO_LONGITUD;
	private double CUADRO_ALTURA;
	private String CUADRO_NUMERO_SUMINISTRO;
	private String CUADRO_POTENCIA_INSTALADA;
	private String CUADRO_POTENCIA_CONTRATADA;
	private String CUADRO_COMANDO;
	private String CUADRO_FECHA_TOMA_DATOS;
	private String CUADRO_FECHA_INSTALACION;
	private String CUADRO_RELOJ_MARCA;
	private String CUADRO_RELOJ_MODELO;
	private int CUADRO_NUMERO_DE_CIRCUITOS;
	private ArrayList<Circuito> CIRCUITO = new ArrayList<Circuito>();
	
	public int getCUADRO_ID() {
		return CUADRO_ID;
	}

	public void setCUADRO_ID(int cUADRO_ID) {
		CUADRO_ID = cUADRO_ID;
	}

	public String getCUADRO_NOMBRE() {
		return CUADRO_NOMBRE;
	}

	public void setCUADRO_NOMBRE(String cUADRO_NOMBRE) {
		CUADRO_NOMBRE = cUADRO_NOMBRE;
	}

	public String getCUADRO_DIRECCION() {
		return CUADRO_DIRECCION;
	}

	public void setCUADRO_DIRECCION(String cUADRO_DIRECCION) {
		CUADRO_DIRECCION = cUADRO_DIRECCION;
	}

	public double getCUADRO_LATITUD() {
		return CUADRO_LATITUD;
	}

	public void setCUADRO_LATITUD(double cUADRO_LATITUD) {
		CUADRO_LATITUD = cUADRO_LATITUD;
	}

	public double getCUADRO_LONGITUD() {
		return CUADRO_LONGITUD;
	}

	public void setCUADRO_LONGITUD(double cUADRO_LONGITUD) {
		CUADRO_LONGITUD = cUADRO_LONGITUD;
	}

	public double getCUADRO_ALTURA() {
		return CUADRO_ALTURA;
	}

	public void setCUADRO_ALTURA(double cUADRO_ALTURA) {
		CUADRO_ALTURA = cUADRO_ALTURA;
	}

	public String getCUADRO_NUMERO_SUMINISTRO() {
		return CUADRO_NUMERO_SUMINISTRO;
	}

	public void setCUADRO_NUMERO_SUMINISTRO(String cUADRO_NUMERO_SUMINISTRO) {
		CUADRO_NUMERO_SUMINISTRO = cUADRO_NUMERO_SUMINISTRO;
	}

	public String getCUADRO_POTENCIA_INSTALADA() {
		return CUADRO_POTENCIA_INSTALADA;
	}

	public void setCUADRO_POTENCIA_INSTALADA(String cUADRO_POTENCIA_INSTALADA) {
		CUADRO_POTENCIA_INSTALADA = cUADRO_POTENCIA_INSTALADA;
	}

	public String getCUADRO_POTENCIA_CONTRATADA() {
		return CUADRO_POTENCIA_CONTRATADA;
	}

	public void setCUADRO_POTENCIA_CONTRATADA(String cUADRO_POTENCIA_CONTRATADA) {
		CUADRO_POTENCIA_CONTRATADA = cUADRO_POTENCIA_CONTRATADA;
	}

	public String getCUADRO_COMANDO() {
		return CUADRO_COMANDO;
	}

	public void setCUADRO_COMANDO(String cUADRO_COMANDO) {
		CUADRO_COMANDO = cUADRO_COMANDO;
	}

	public String getCUADRO_FECHA_TOMA_DATOS() {
		return CUADRO_FECHA_TOMA_DATOS;
	}

	public void setCUADRO_FECHA_TOMA_DATOS(String cUADRO_FECHA_TOMA_DATOS) {
		CUADRO_FECHA_TOMA_DATOS = cUADRO_FECHA_TOMA_DATOS;
	}

	public String getCUADRO_FECHA_INSTALACION() {
		return CUADRO_FECHA_INSTALACION;
	}

	public void setCUADRO_FECHA_INSTALACION(String cUADRO_FECHA_INSTALACION) {
		CUADRO_FECHA_INSTALACION = cUADRO_FECHA_INSTALACION;
	}

	public String getCUADRO_RELOJ_MARCA() {
		return CUADRO_RELOJ_MARCA;
	}

	public void setCUADRO_RELOJ_MARCA(String cUADRO_RELOJ_MARCA) {
		CUADRO_RELOJ_MARCA = cUADRO_RELOJ_MARCA;
	}

	public String getCUADRO_RELOJ_MODELO() {
		return CUADRO_RELOJ_MODELO;
	}

	public void setCUADRO_RELOJ_MODELO(String cUADRO_RELOJ_MODELO) {
		CUADRO_RELOJ_MODELO = cUADRO_RELOJ_MODELO;
	}

	public int getCUADRO_NUMERO_DE_CIRCUITOS() {
		return CUADRO_NUMERO_DE_CIRCUITOS;
	}

	public void setCUADRO_NUMERO_DE_CIRCUITOS(int cUADRO_NUMERO_DE_CIRCUITOS) {
		CUADRO_NUMERO_DE_CIRCUITOS = cUADRO_NUMERO_DE_CIRCUITOS;
	}

	public ArrayList<Circuito> getCIRCUITO() {
		return CIRCUITO;
	}

	public void setCIRCUITO(ArrayList<Circuito> cIRCUITO) {
		CIRCUITO = cIRCUITO;
	}

	public CuadroFisico(int id,String nom,String dir,double la,double lon,double alt,String sum,String potins,
						String potcon,String coman,String fecht,String fechi,String mar,String mod,int numcir, ArrayList<Circuito> cir)
	{
		CUADRO_ID=id;
		CUADRO_NOMBRE=nom;
		CUADRO_DIRECCION=dir;
		CUADRO_LATITUD=la;
		CUADRO_LONGITUD=lon;
		CUADRO_ALTURA=alt;
		CUADRO_NUMERO_SUMINISTRO=sum;
		CUADRO_POTENCIA_INSTALADA=potins;
		CUADRO_POTENCIA_CONTRATADA=potcon;
		CUADRO_COMANDO=coman;
		CUADRO_FECHA_TOMA_DATOS=fecht;
		CUADRO_FECHA_INSTALACION=fechi;
		CUADRO_RELOJ_MARCA=mar;
		CUADRO_RELOJ_MODELO=mod;
		CUADRO_NUMERO_DE_CIRCUITOS=numcir;
		CIRCUITO=cir;
	}
}

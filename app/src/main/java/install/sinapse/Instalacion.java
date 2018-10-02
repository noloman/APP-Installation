package install.sinapse;

import java.util.ArrayList;

public class Instalacion {
	String IDINST;
	Punto PUNTO;
	Cuadro CUADRO;
	PuntoAcceso PUNTO_ACCESO;
	ArrayList<OtroDispositivo> OTRO_DISPOSITIVO = new ArrayList<OtroDispositivo>();
	
	public Instalacion() {}
	public Instalacion(String id,Punto p,Cuadro c,PuntoAcceso pa,ArrayList<OtroDispositivo> o)
	{
		IDINST = id;
		PUNTO=p;
		CUADRO=c;
		PUNTO_ACCESO=pa;
		OTRO_DISPOSITIVO = o;
	}
	
	public String getID()
	{
		return IDINST;
	}
	
	public Punto getPuntos()
	{
		return PUNTO;
	}
	
	public Cuadro getCuadros()
	{
		return CUADRO;
	}
	
	public PuntoAcceso getPuntos_Acceso()
	{
		return PUNTO_ACCESO;
	}
	
	public ArrayList<OtroDispositivo> getOtros_Dispositivo()
	{
		return OTRO_DISPOSITIVO;
	}
	
	public void setID(String i)
	{
		IDINST = i;
	}
	
	public void setPuntos(Punto p)
	{
		PUNTO = p;
	}
	
	public void setCuadros(Cuadro c)
	{
		CUADRO = c;
	}
	
	public void setPuntos_Acceso(PuntoAcceso pa)
	{
		PUNTO_ACCESO = pa;
	}
	
	public void setOtros_Dispositivo(ArrayList<OtroDispositivo> o)
	{
		OTRO_DISPOSITIVO = o;
	}
}

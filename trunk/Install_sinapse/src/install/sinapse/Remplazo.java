package install.sinapse;

public class Remplazo {
	String TRAZABILIDAD_ANTIGUO;
	int ID_DISPOSITIVO_ANTIGUO;
	String TRAZABILIDAD_NUEVO;
	int ID_DISPOSITIVO_NUEVO;
	String FECHA_INSTALACION;
	
	public Remplazo(String trazA, int idA, String trazN,int idN,String fech)
	{
		TRAZABILIDAD_ANTIGUO = trazA;
		ID_DISPOSITIVO_ANTIGUO = idA;
		TRAZABILIDAD_NUEVO = trazN;
		ID_DISPOSITIVO_NUEVO = idN;
		FECHA_INSTALACION = fech;
	}
}

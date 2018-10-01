package install.sinapse;

public class OtroDispositivo {
	private String NOMBRE;
	private String TRAZABILIDAD;
	private double LATITUD;
	private double LONGITUD;
	private String FECHA_INSTALACION;
	
	public OtroDispositivo() {}
	public OtroDispositivo(String nom,String tra,double la,double lon,String fech)
	{
		NOMBRE = nom;
		TRAZABILIDAD = tra;
		LATITUD = la;
		LONGITUD = lon;
		FECHA_INSTALACION = fech;
	}
	
	public String getNombre()
	{
		return NOMBRE;
	}
	
	public String getTrazabilidad()
	{
		return TRAZABILIDAD;
	}
	
	public double getLatitud()
	{
		return LATITUD;
	}
	
	public double getLongitud()
	{
		return LONGITUD;
	}
	
	public String getFecha()
	{
		return FECHA_INSTALACION;
	}
	
	public void setNombre(String nom)
	{
		NOMBRE = nom;
	}
	
	public void setTrazabilidad(String tra)
	{
		TRAZABILIDAD = tra;
	}
	
	public void setLatitud(double la)
	{
		LATITUD = la;
	}
	
	public void setLongitud(double lon)
	{
		LONGITUD = lon;
	}
	
	public void setFecha(String fech)
	{
		FECHA_INSTALACION = fech;
	}
}

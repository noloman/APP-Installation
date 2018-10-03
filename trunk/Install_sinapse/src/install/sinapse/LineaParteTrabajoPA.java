package install.sinapse;

public class LineaParteTrabajoPA {
	private String PA_Serial;
	private String nombre;
	
	public LineaParteTrabajoPA() {}
	
	public LineaParteTrabajoPA(String t, String nom)
	{
		PA_Serial = t;
		nombre = nom;
	}
	
	public String getPA_Serial()
	{
		return PA_Serial;
	}
	
	public void setPA_Serial(String t)
	{
		PA_Serial = t;
	}
	
	public String getNombre()
	{
		return nombre;
	}
	
	public void setNombre(String nom)
	{
		nombre = nom;
	}
}

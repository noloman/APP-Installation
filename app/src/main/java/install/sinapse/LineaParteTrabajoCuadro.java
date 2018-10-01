package install.sinapse;

public class LineaParteTrabajoCuadro {
	private String Cmc_Serial;
	private String P_C;
	private String Nombre;
	private int Circuitos;
	
	public LineaParteTrabajoCuadro()
	{
		
	}
	
	public LineaParteTrabajoCuadro(String cmc, String pc, String nom, int c)
	{
		Cmc_Serial = cmc;
		P_C = pc;
		Nombre = nom;
		Circuitos = c;
	}
	
	public String getCmc_Serial()
	{
		return Cmc_Serial;
	}
	
	public void setCmc_Serial(String cmc)
	{
		Cmc_Serial = cmc;
	}
	
	public String getP_C()
	{
		return P_C;
	}
	
	public void setP_C(String pc)
	{
		P_C = pc;
	}
	
	public String getNombre()
	{
		return Nombre;
	}
	
	public void setNombre(String n)
	{
		Nombre = n;
	}
	
	public int getCircuitos()
	{
		return Circuitos;
	}
	
	public void setCircuitos(int c)
	{
		Circuitos = c;
	}
}

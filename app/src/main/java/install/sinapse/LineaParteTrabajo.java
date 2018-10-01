package install.sinapse;

public class LineaParteTrabajo 
{	
	private String Punto_Serial;
	private int Punto_Pot;
	private String Punto_Nom;
	private String Luminaria;
	
	private String Dispositivo_Serial;
	private String Dispositivo_Nom;
	
	public LineaParteTrabajo()
	{
		
	}
	
	public LineaParteTrabajo(String se,String nom,int pot,String lum)
	{
		Punto_Serial = se;
		Punto_Pot = pot;
		Punto_Nom = nom;
		Luminaria = lum;
	}
	
	
	public String getPunto_Serial() 
	{
		return Punto_Serial;
	}

	public void setPunto_Serial(String s) 
	{
		Punto_Serial = s;
	}
	
	
	public int getPunto_Pot() 
	{
		return Punto_Pot;
	}

	public void setPunto_Pot(int p) 
	{
		Punto_Pot = p;
	}
	
	public String getPunto_Nom() 
	{
		return Punto_Nom;
	}

	public void setPunto_Nom(String n) 
	{
		Punto_Nom = n;
	}
	
	public String getLuminaria() 
	{
		return Luminaria;
	}

	public void setLuminaria(String l) 
	{
		Luminaria = l;
	}
	
	public String getDispositivo_Serial() 
	{
		return Dispositivo_Serial;
	}

	public void setDispositivo_Serial(String c) 
	{
		Dispositivo_Serial = c;
	}
	
	public String getDispositivo_Nom() 
	{
		return Dispositivo_Nom;
	}

	public void setDispositivo_Nom(String cnom) 
	{
		Dispositivo_Serial = cnom;
	}
	
}

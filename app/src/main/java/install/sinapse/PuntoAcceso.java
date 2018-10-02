package install.sinapse;

import java.util.ArrayList;

public class PuntoAcceso {
	private ArrayList<Pc> PC;
	private ArrayList<Br> BR;
	
	public PuntoAcceso(){}
	
	public PuntoAcceso(ArrayList<Pc> p, ArrayList<Br> b)
	{
		PC = p;
		BR = b;
	}
	
	public  ArrayList<Pc> getPC()
	{
		return PC;
	}
	
	public void setPC( ArrayList<Pc> p)
	{
		PC =p;
	}
	
	public  ArrayList<Br> getBR()
	{
		return BR;
	}
	
	public void setBR( ArrayList<Br> b)
	{
		BR = b;
	}
}

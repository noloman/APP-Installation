package install.sinapse;

import java.util.ArrayList;

public class Punto {
	private ArrayList<MCBE> MC_BE;
	private ArrayList<MC3P> MC_3P;
	private ArrayList<BALASTOTELEGESTIONADO> BE;
	private ArrayList<DRIVERTELEGESTIONADO> DE;
	
	public Punto(ArrayList<MCBE> mb, ArrayList<MC3P> m3,ArrayList<BALASTOTELEGESTIONADO> b, ArrayList<DRIVERTELEGESTIONADO> d)
	{
		MC_BE = mb;
		MC_3P = m3;
		BE = b;
		DE = d;
	}
	
	public ArrayList<MCBE> getMCBE()
	{
		return MC_BE;
	}
	
	public ArrayList<MC3P> getMC3P()
	{
		return MC_3P;
	}
	
	public  ArrayList<BALASTOTELEGESTIONADO> getBE()
	{
		return BE;
	}
	
	public  ArrayList<DRIVERTELEGESTIONADO> getDE()
	{
		return DE;
	}
	
	public void setMCBE(ArrayList<MCBE> m)
	{
		MC_BE = m;
	}
	
	public void setMC3P(ArrayList<MC3P> m)
	{
		MC_3P = m;
	}
	
	public  void setBE( ArrayList<BALASTOTELEGESTIONADO> b)
	{
		BE = b;
	}
	
	public void setDE( ArrayList<DRIVERTELEGESTIONADO> d)
	{
		DE = d;
	}
	
}

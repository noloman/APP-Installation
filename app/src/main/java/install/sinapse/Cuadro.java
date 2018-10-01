package install.sinapse;

import java.util.ArrayList;

public class Cuadro {
	
	private ArrayList<Cmc> CMC;
	
	public Cuadro(){}

	public Cuadro(ArrayList<Cmc> c)
	{
		CMC = c;
	}
	
	public ArrayList<Cmc> getCuadros()
	{
		return CMC;
	}
	
}

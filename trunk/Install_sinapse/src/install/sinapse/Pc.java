package install.sinapse;

public class Pc {
	private String NOMBRE;
	private String TRAZABILIDAD;
	private double LATITUD;
	private double LONGITUD;
	private double ALTURA;
	private String DIRECCION;
	private String ID_NODE;
	private String IP1;
	private String PROTOCOLO1;
	private String PUERTO11;
	private String PUERTO12;
	private String IP2;
	private String PROTOCOLO2;
	private String PUERTO21;
	private String PUERTO22;
	private String DIRFISICA;
	private String FECHA_INSTALACION;
	
	public Pc() {}
	
	public Pc(String nom,String tra,double la,double lon,double alt,String dir,String node,String ip1,
					   String prot1,String puer11,String puer12,String ip2,String prot2,String puer21,String puer22,
					   String fisica,String fech)
	{
		NOMBRE=nom;
		TRAZABILIDAD=tra;
		LATITUD=la;
		LONGITUD=lon;
		ALTURA=alt;
		DIRECCION=dir;
		ID_NODE=node;
		IP1=ip1;
		PROTOCOLO1=prot1;
		PUERTO11=puer11;
		PUERTO12=puer12;
		IP2=ip2;
		PROTOCOLO2=prot2;
		PUERTO21=puer21;
		PUERTO22=puer22;
		DIRFISICA=fisica;
		FECHA_INSTALACION=fech;
	}

	public String getNOMBRE() {
		return NOMBRE;
	}

	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}

	public String getTRAZABILIDAD() {
		return TRAZABILIDAD;
	}

	public void setTRAZABILIDAD(String tRAZABILIDAD) {
		TRAZABILIDAD = tRAZABILIDAD;
	}

	public double getLATITUD() {
		return LATITUD;
	}

	public void setLATITUD(double lATITUD) {
		LATITUD = lATITUD;
	}

	public double getLONGITUD() {
		return LONGITUD;
	}

	public void setLONGITUD(double lONGITUD) {
		LONGITUD = lONGITUD;
	}

	public double getALTURA() {
		return ALTURA;
	}

	public void setALTURA(double aLTURA) {
		ALTURA = aLTURA;
	}

	public String getDIRECCION() {
		return DIRECCION;
	}

	public void setDIRECCION(String dIRECCION) {
		DIRECCION = dIRECCION;
	}

	public String getID_NODE() {
		return ID_NODE;
	}

	public void setID_NODE(String iD_NODE) {
		ID_NODE = iD_NODE;
	}

	public String getIP1() {
		return IP1;
	}

	public void setIP1(String iP1) {
		IP1 = iP1;
	}

	public String getPROTOCOLO1() {
		return PROTOCOLO1;
	}

	public void setPROTOCOLO1(String pROTOCOLO1) {
		PROTOCOLO1 = pROTOCOLO1;
	}

	public String getPUERTO11() {
		return PUERTO11;
	}

	public void setPUERTO11(String pUERTO11) {
		PUERTO11 = pUERTO11;
	}

	public String getPUERTO12() {
		return PUERTO12;
	}

	public void setPUERTO12(String pUERTO12) {
		PUERTO12 = pUERTO12;
	}

	public String getIP2() {
		return IP2;
	}

	public void setIP2(String iP2) {
		IP2 = iP2;
	}

	public String getPROTOCOLO2() {
		return PROTOCOLO2;
	}

	public void setPROTOCOLO2(String pROTOCOLO2) {
		PROTOCOLO2 = pROTOCOLO2;
	}

	public String getPUERTO21() {
		return PUERTO21;
	}

	public void setPUERTO21(String pUERTO21) {
		PUERTO21 = pUERTO21;
	}

	public String getPUERTO22() {
		return PUERTO22;
	}

	public void setPUERTO22(String pUERTO22) {
		PUERTO22 = pUERTO22;
	}

	public String getDIRFISICA() {
		return DIRFISICA;
	}

	public void setDIRFISICA(String dIRFISICA) {
		DIRFISICA = dIRFISICA;
	}

	public String getFECHA_INSTALACION() {
		return FECHA_INSTALACION;
	}

	public void setFECHA_INSTALACION(String fECHA_INSTALACION) {
		FECHA_INSTALACION = fECHA_INSTALACION;
	}
	
}

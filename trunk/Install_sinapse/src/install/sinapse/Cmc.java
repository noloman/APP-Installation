package install.sinapse;

public class Cmc {
	private String NOMBRE;
	private String TRAZABILIDAD;
	private double LATITUD;
	private double LONGITUD;
	private double ALTURA;
	private String DIRECCION;
	private String ID_NODE;
	private String ID_CUADRO;
	private boolean MEDIDA;
	private boolean MONITORING;
	private String CONTROL;
	private String RELE_1;
	private String RELE_2;
	private String RELE_3;
	private String RELE_4;
	private String RELE_5;
	private String RELE_6;
	private String RELE_7;
	private String FECHA_INSTALACION;
	private CuadroFisico PUNTO_FISICO_CUADRO;
	private Pc PC;
	
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

	public String getID_CUADRO() {
		return ID_CUADRO;
	}

	public void setID_CUADRO(String iD_CUADRO) {
		ID_CUADRO = iD_CUADRO;
	}

	public boolean isMEDIDA() {
		return MEDIDA;
	}

	public void setMEDIDA(boolean mEDIDA) {
		MEDIDA = mEDIDA;
	}

	public boolean isMONITORING() {
		return MONITORING;
	}

	public void setMONITORING(boolean mONITORING) {
		MONITORING = mONITORING;
	}

	public String getCONTROL() {
		return CONTROL;
	}

	public void setCONTROL(String cONTROL) {
		CONTROL = cONTROL;
	}

	public String getRELE_1() {
		return RELE_1;
	}

	public void setRELE_1(String rELE_1) {
		RELE_1 = rELE_1;
	}

	public String getRELE_2() {
		return RELE_2;
	}

	public void setRELE_2(String rELE_2) {
		RELE_2 = rELE_2;
	}

	public String getRELE_3() {
		return RELE_3;
	}

	public void setRELE_3(String rELE_3) {
		RELE_3 = rELE_3;
	}

	public String getRELE_4() {
		return RELE_4;
	}

	public void setRELE_4(String rELE_4) {
		RELE_4 = rELE_4;
	}

	public String getRELE_5() {
		return RELE_5;
	}

	public void setRELE_5(String rELE_5) {
		RELE_5 = rELE_5;
	}

	public String getRELE_6() {
		return RELE_6;
	}

	public void setRELE_6(String rELE_6) {
		RELE_6 = rELE_6;
	}

	public String getRELE_7() {
		return RELE_7;
	}

	public void setRELE_7(String rELE_7) {
		RELE_7 = rELE_7;
	}

	public String getFECHA_INSTALACION() {
		return FECHA_INSTALACION;
	}

	public void setFECHA_INSTALACION(String fECHA_INSTALACION) {
		FECHA_INSTALACION = fECHA_INSTALACION;
	}

	public CuadroFisico getPUNTO_FISICO_CUADRO() {
		return PUNTO_FISICO_CUADRO;
	}

	public void setPUNTO_FISICO_CUADRO(CuadroFisico pUNTO_FISICO_CUADRO) {
		PUNTO_FISICO_CUADRO = pUNTO_FISICO_CUADRO;
	}

	public Pc getPC() {
		return PC;
	}

	public void setPC(Pc pC) {
		PC = pC;
	}

	
	public Cmc() {}
	
	public Cmc(String nom,String tra,double la,double lon,double alt,String dir,String node,String cuadro,
			boolean med,boolean mo,String con,String r1,String r2,String r3,String r4,String r5,String r6,
			String r7,String fech,CuadroFisico cf,Pc p)
	{
		NOMBRE=nom;
		TRAZABILIDAD=tra;
		LATITUD=la;
		LONGITUD=lon;
		ALTURA=alt;
		DIRECCION=dir;
		ID_NODE=node;
		ID_CUADRO=cuadro;
		MEDIDA=med;
		MONITORING=mo;
		CONTROL=con;
		RELE_1=r1;
		RELE_2=r2;
		RELE_3=r3;
		RELE_4=r4;
		RELE_5=r5;
		RELE_6=r6;
		RELE_7=r7;
		FECHA_INSTALACION=fech;
		PUNTO_FISICO_CUADRO=cf;
		PC = p;
	}
}

package install.sinapse;

public class MC3P {
	private String NOMBRE;
	private String TRAZABILIDAD;
	private double LATITUD;
	private double LONGITUD;
	private double ALTURA;
	private String DIRECCION;
	private String ID_NODE;
	private String ID_CUADRO;
	private int CIRCUITO ;
	private String REGULACION ;
	private boolean CAMBIO_BOMBILLA;
	private boolean MEDIDA;
	private String TIPO;
	private String BALASTO1_ID; 
	private String BALASTO1_MARCA;
	private int BALASTO1_POTENCIA1;
	private String BALASTO1_TIPO;
	private int BALASTO1_PERDIDAS;
	private String BALASTO2_ID; 
	private String BALASTO2_MARCA;
	private int BALASTO2_POTENCIA1;
	private String BALASTO2_TIPO;
	private int BALASTO2_PERDIDAS;
	private String FECHA_INSTALACION;
	private PuntoFisico PUNTO_FISICO_LUMINARIA;
	
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


	public int getCIRCUITO() {
		return CIRCUITO;
	}


	public void setCIRCUITO(int cIRCUITO) {
		CIRCUITO = cIRCUITO;
	}


	public String getREGULACION() {
		return REGULACION;
	}


	public void setREGULACION(String rEGULACION) {
		REGULACION = rEGULACION;
	}


	public boolean isCAMBIO_BOMBILLA() {
		return CAMBIO_BOMBILLA;
	}


	public void setCAMBIO_BOMBILLA(boolean cAMBIO_BOMBILLA) {
		CAMBIO_BOMBILLA = cAMBIO_BOMBILLA;
	}


	public boolean isMEDIDA() {
		return MEDIDA;
	}


	public void setMEDIDA(boolean mEDIDA) {
		MEDIDA = mEDIDA;
	}


	public String getTIPO() {
		return TIPO;
	}


	public void setTIPO(String tIPO) {
		TIPO = tIPO;
	}


	public String getBALASTO1_ID() {
		return BALASTO1_ID;
	}


	public void setBALASTO1_ID(String bALASTO1_ID) {
		BALASTO1_ID = bALASTO1_ID;
	}


	public String getBALASTO1_MARCA() {
		return BALASTO1_MARCA;
	}


	public void setBALASTO1_MARCA(String bALASTO1_MARCA) {
		BALASTO1_MARCA = bALASTO1_MARCA;
	}


	public int getBALASTO1_POTENCIA1() {
		return BALASTO1_POTENCIA1;
	}


	public void setBALASTO1_POTENCIA1(int bALASTO1_POTENCIA1) {
		BALASTO1_POTENCIA1 = bALASTO1_POTENCIA1;
	}


	public String getBALASTO1_TIPO() {
		return BALASTO1_TIPO;
	}


	public void setBALASTO1_TIPO(String bALASTO1_TIPO) {
		BALASTO1_TIPO = bALASTO1_TIPO;
	}


	public int getBALASTO1_PERDIDAS() {
		return BALASTO1_PERDIDAS;
	}


	public void setBALASTO1_PERDIDAS(int bALASTO1_PERDIDAS) {
		BALASTO1_PERDIDAS = bALASTO1_PERDIDAS;
	}


	public String getBALASTO2_ID() {
		return BALASTO2_ID;
	}


	public void setBALASTO2_ID(String bALASTO2_ID) {
		BALASTO2_ID = bALASTO2_ID;
	}


	public String getBALASTO2_MARCA() {
		return BALASTO2_MARCA;
	}


	public void setBALASTO2_MARCA(String bALASTO2_MARCA) {
		BALASTO2_MARCA = bALASTO2_MARCA;
	}


	public int getBALASTO2_POTENCIA1() {
		return BALASTO2_POTENCIA1;
	}


	public void setBALASTO2_POTENCIA1(int bALASTO2_POTENCIA1) {
		BALASTO2_POTENCIA1 = bALASTO2_POTENCIA1;
	}


	public String getBALASTO2_TIPO() {
		return BALASTO2_TIPO;
	}


	public void setBALASTO2_TIPO(String bALASTO2_TIPO) {
		BALASTO2_TIPO = bALASTO2_TIPO;
	}


	public int getBALASTO2_PERDIDAS() {
		return BALASTO2_PERDIDAS;
	}


	public void setBALASTO2_PERDIDAS(int bALASTO2_PERDIDAS) {
		BALASTO2_PERDIDAS = bALASTO2_PERDIDAS;
	}


	public String getFECHA_INSTALACION() {
		return FECHA_INSTALACION;
	}


	public void setFECHA_INSTALACION(String fECHA_INSTALACION) {
		FECHA_INSTALACION = fECHA_INSTALACION;
	}


	public PuntoFisico getPUNTO_FISICO_LUMINARIA() {
		return PUNTO_FISICO_LUMINARIA;
	}


	public void setPUNTO_FISICO_LUMINARIA(PuntoFisico pUNTO_FISICO_LUMINARIA) {
		PUNTO_FISICO_LUMINARIA = pUNTO_FISICO_LUMINARIA;
	}


	public MC3P() {}
	
	
	public MC3P(String nombre,String traza,double lat,double longi,double alt,String addr,String idnode,
			     String idcuadro,int cir,String regu,boolean bombilla,boolean med,String tip,String id1,
			     String mar1,int pot1,String tipo1,int perdidas1,String id2,String mar2,int pot2,String tipo2,
			     int perdidas2,String fech,PuntoFisico punto)
	{
		NOMBRE=nombre;
		TRAZABILIDAD = traza;
		LATITUD=lat;
		LONGITUD=longi;
		ALTURA=alt;
		DIRECCION=addr;
		ID_NODE=idnode;
		ID_CUADRO=idcuadro;
		CIRCUITO=cir;
		REGULACION=regu;
		CAMBIO_BOMBILLA=bombilla;
		MEDIDA=med;
		TIPO=tip;
		BALASTO1_ID = id1;
		BALASTO1_MARCA = mar1;
		BALASTO1_POTENCIA1 = pot1;
		BALASTO1_TIPO = tipo1;
		BALASTO1_PERDIDAS = perdidas1;
		BALASTO2_ID = id2;
		BALASTO2_MARCA = mar2;
		BALASTO2_POTENCIA1 = pot2;
		BALASTO2_TIPO = tipo2;
		BALASTO2_PERDIDAS = perdidas2;
		FECHA_INSTALACION = fech;
		PUNTO_FISICO_LUMINARIA=punto;
	}
}

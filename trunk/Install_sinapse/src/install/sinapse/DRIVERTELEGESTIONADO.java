package install.sinapse;

public class DRIVERTELEGESTIONADO {
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
	private String BALASTO1_ID; 
	private String BALASTO1_MARCA;
	private int BALASTO1_POTENCIA1;
	private String BALASTO1_TIPO;
	private int BALASTO1_PERDIDAS;
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

	public DRIVERTELEGESTIONADO(){}
	
	public DRIVERTELEGESTIONADO(String nombre,String traza,double lat,double longi,double alt,String addr,String idnode,
		     String idcuadro,int cir,String regu,boolean bombilla,boolean med,String id1,
		     String mar1,int pot1,String tip,int per,String fech,PuntoFisico punto)
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
		BALASTO1_ID = id1;
		BALASTO1_MARCA = mar1;
		BALASTO1_POTENCIA1 = pot1;
		BALASTO1_TIPO = tip;
		BALASTO1_PERDIDAS = per;
		FECHA_INSTALACION = fech;
		PUNTO_FISICO_LUMINARIA=punto;
	}
}

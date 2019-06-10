package Base_COM_Serie;

import Comm.io.tCommConnector;

public class ConstantesApp 
{
	//IniFiles Params
//	public final static String 			ZN_INIFILE_CONFIG_APPDOMO = "CONFIG_APPDOMO";
	
	public static final String 			CONFIG_FILE = "Base_COMSerie.conf";
	
//	public final static String 			CP_INIFILE_COMPORT = "COMPort";
	
	public static final String 			WINDOW_TITLE = "APLICACI�N BASE PARA COMUNICACI�N SERIE "; 
		
	public static final String			SERIALCONSOLE = "serialConsole";
	
	//Setup
	//TODO: Incluir la selecci�n del conector serie en una forma de configuraci�n 
	public static final tCommConnector	SERIALCONNECTION 		= tCommConnector.CN_JSSC;  //tCommConnector.CN_RXTX; //
	
	public static final int				TIME_REFRESH_MODBUS 	= 100;		//Tiempo en milisegundos
	
	public static final String			CRLF					=	"\n\r";
}
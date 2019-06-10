package Comm.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

//import Comm.Net.Connection;

//import net.gestor_Nodos;

public abstract class CommTransport {
	
	//private static final int ACTION_PERFORMED = 0;
	
	/*
	 * Identificador para que aplicaciones externas identifique el tipo de conexión 
	 */
	protected	static tpConnection		tConnection;
	
	protected 	tCommConnector			Connector;
	
	protected	OutputStream			outputStream;
	protected 	InputStream				inputStream;
	
	//protected Connection		appConnection;
	protected 	ConnTransportAdaption 		CommTransportListener = null;
	
	protected void	log(String message){
		if(CommTransportListener != null){
			CommTransportListener.logTransport(message);
		}
	}
		
	public CommTransport(tCommConnector Connector){
		this.Connector = Connector;		
	}
	
	public void addTransportListener(ConnTransportAdaption actionListener) {
		this.CommTransportListener = actionListener;
	}
	
	/*
	 * devuelve el tipo de conexión implementada ppara las comunicaciones
	 */
	public static tpConnection	get_tpConection(){
		return	tConnection;
	}
	
	public OutputStream	getOutputStream(){
		return outputStream;
	}
	
	public InputStream	getInputStream(){
		return inputStream;
	}
	
	public abstract void 	loadConfig(Properties configTable);
	
	public abstract void 	SendData(String msg);
	
	public abstract boolean isConnected();
	
	public abstract void	disableReaderEvent(boolean EnDis);
}

package Comm.Serial.Net;

import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import Comm.Net.Connection;
import Comm.Serial.SerialConfig;

public abstract class SerialConnection extends Connection{

	protected boolean 	isOpen;	
	protected boolean 	isClosed = true;
	protected String 	lastError;
	protected String 	comPort;
	
	public String getConnectionName() 
	{
	    return comPort;
	}	
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public void loadConfig(Properties configTable){
		this.configTable = configTable;
	}
	
	public void setComPort(String comPort) {
		this.comPort = comPort;
    
		configTable.setProperty(SerialConfig.CONFIG_COMPORT, comPort);
	}
	
	public abstract List<String> ComPortList();
	
	public abstract void open(String comPort);
	
	public final void close() {
		isClosed = true;
		lastError = null;
		closeConnection();
	}

	protected final void closeConnection() {
		isOpen = false;

		doClose();
		serialClosed();
	}
	
	public int getBaudRate(){
		return Integer.parseInt(configTable.getProperty("BaudRate").toString());
	}
	
	public int getDataBits(){
		return Integer.parseInt(configTable.getProperty("Databits").toString());
	}
	
	public int getStopBits(){
		String stopbit = configTable.getProperty("Stopbits").toString();
		
		if(stopbit.equals("1")) return SerialPort.STOPBITS_1;
		else if (stopbit.endsWith("1.5")) return SerialPort.STOPBITS_1_5;
		else return SerialPort.STOPBITS_2;
	}
	
	public int getParity(){
		String parity = configTable.getProperty("Parity").toString();
		
		if(parity.equals("None")) return SerialPort.PARITY_NONE;
		else if(parity.equals("Par")) return SerialPort.PARITY_EVEN;
		else return SerialPort.PARITY_ODD;
	}

	protected abstract void doClose();

	protected final void serialClosed() {
//		listener.serialClosed(this);
		connectionAdapter.ConnectionClosed(this);
	}
	
	protected void serialOpened(){
		connectionAdapter.ConnectionOpened(this);		
	}
	
	public abstract OutputStream	getOutputStream();
	public abstract InputStream		getInputStream();
	
	public abstract void sendData(String msg);
	
	public abstract void disableSerialReaderEvent(boolean EnDis);
}

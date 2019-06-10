package Comm.Serial.io;

import java.util.List;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import Comm.Msg.Comm_Msg;
import Comm.Net.Connection;
import Comm.Net.ConnectionAdapter;
import Comm.Serial.Net.JSSC_Connection;
import Comm.Serial.Net.RxTxConnection;
import Comm.Serial.Net.SerialAction;
import Comm.Serial.Net.SerialConnection;
import Comm.io.CommTransport;
import Comm.io.tCommConnector;
import Comm.io.tpConnection;

public class SerialCommTransport extends CommTransport{
		
	private boolean				hasSerialOpened;
	private	SerialConnection	serialConnection;
	private SerialAction 		serialAction;
	
	
	public SerialCommTransport(tCommConnector Connector){
		super(Connector);
		
		switch (Connector){
		case CN_RXTX:
			//Esta llamada variará en función de la conexión serie que estemos usando (RXTX o JSSD) 
			serialConnection 	= new RxTxConnection();
			
			break;
			
		case CN_JSSC:
			serialConnection	= new JSSC_Connection();
			break;
		}
		
		//Indicamos el tipo de conexión
		tConnection = tpConnection.SERIAL;
		
		serialAction 		= new SerialAction("Connect to serial", serialConnection);
		
		serialConnection.addConnectionListener(new ConnectionAdapter(){
			@Override
			public void connLog(String message) {
				log(message);				
			}
			
			@Override
			public void ConnectionOpened(Connection connection) {
				String connectionName = ((SerialConnection)connection).getConnectionName();
			    
			    log(String.format(Comm_Msg.SERIECONNECTED, connectionName));
			    hasSerialOpened = true;
			    
			    outputStream = serialConnection.getOutputStream();
			    inputStream  = serialConnection.getInputStream();
				
			    //CommTransportListener.SystemMessage(String.format(Comm_Msg.CONNECTEDTO, connectionName));
			    
			    CommTransportListener.CT_Opened(String.format(Comm_Msg.CONNECTEDTO, connectionName));
			}
			
			@Override
			public void ConnectionClosed(Connection connection){
				if( hasSerialOpened ){
					log(String.format(Comm_Msg.SERIEDISCONNECTED, ((SerialConnection)connection).getConnectionName()));
					 hasSerialOpened = false;
				}
					
				CommTransportListener.SystemMessage(Comm_Msg.UNCONNECTED);
			}

			@Override
			public void InData(Connection connection, String Line) {				
				//System.out.println("Serial Comm Transport --> Dato Recibido : "+Line);
				
				CommTransportListener.CTInData(connection, Line);
			}
		});
	}
	
	
	public SerialConnection getSerialConnection(){
		return serialConnection;
	}
	
	public int SetMenu_SerialPorts(JMenu mnMenu){
		  
		//Obtenemos los puertos Serie disponibles
		
		List<String> ports = serialConnection.ComPortList();
		
		if(ports.size() == 0){
			mnMenu.setEnabled(false);
		}
		else
		{		
			mnMenu.removeAll();
		
			for(String port : ports){
				JRadioButtonMenuItem mntmPrueba = new JRadioButtonMenuItem(port);
				mnMenu.add(mntmPrueba);
				
				//mntmPrueba.addActionListener(connectSerialAction);   
				mntmPrueba.addActionListener(serialAction); 
			}			
		}
				
		return ports.size();
	}

	@Override
	public void loadConfig(Properties configTable) {
		// Auto-generated method stub
		serialConnection.loadConfig(configTable);
	}
	
	@Override
	public void SendData(String msg){
		//System.out.println(msg);
		serialConnection.sendData(msg);
	}


	@Override
	public boolean isConnected() {
		return hasSerialOpened;
	}


	@Override
	public void disableReaderEvent(boolean EnDis) {
		// TODO Auto-generated method stub
		serialConnection.disableSerialReaderEvent(EnDis);
	}
}

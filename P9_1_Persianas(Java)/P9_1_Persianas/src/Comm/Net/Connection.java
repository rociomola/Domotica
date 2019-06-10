package Comm.Net;

import java.util.Properties;

public abstract class Connection {
	protected Properties 		configTable;
	protected ConnectionAdapter	connectionAdapter = null;
	
	public abstract void loadConfig(Properties configTable);
	
	protected void log(String message){
		if(connectionAdapter != null){
			connectionAdapter.connLog(message);
		}
	}
	
	protected void InData(String message){
		if(connectionAdapter != null){
			connectionAdapter.InData(this, message);
		}
	}
	
	public	void addConnectionListener(ConnectionAdapter actionListener){
		connectionAdapter = actionListener;
	}
}

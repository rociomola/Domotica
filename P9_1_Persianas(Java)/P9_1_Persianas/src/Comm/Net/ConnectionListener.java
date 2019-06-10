package Comm.Net;

public interface ConnectionListener {

	/*
	public void serialData(SerialConnection_RXTX connection, String line);
	
	public void serialData(SerialConnection_RXTX connection, byte[] buffer, int line);
	
	public void serialOpened(SerialConnection_RXTX connection);

	public void serialClosed(SerialConnection_RXTX connection);
	*/
	
	public void connLog(String message);
	
	public void ConnectionOpened(Connection connection);
	
	public void ConnectionClosed(Connection connection);
	
	public void InData(Connection connection, String Line);
}

package Comm.io;

import Comm.Net.Connection;

public interface ConnTransportListener {
	public void logTransport(String message);
	
	public void SystemMessage(String message);
	
	public void CTInData(Connection connection, String message);
	
	public void CT_Opened(String message); 
}

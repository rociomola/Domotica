package Comm.io;

import Comm.Net.Connection;

public class ConnTransportAdaption implements ConnTransportListener{

	private void SystemOut(String message){
		System.out.println("ConnTransportAdaption --> "+message);
	}
	
	@Override
	public void logTransport(String message) {
		SystemOut("SystemMessage : "+message);
	}

	@Override
	public void SystemMessage(String message) {
		SystemOut("SystemMessage : "+message);
	}

	@Override
	public void CTInData(Connection connection, String message) {		
		SystemOut("CommInTransport : "+message);		
	}

	@Override
	public void CT_Opened(String message) {
		// TODO Auto-generated method stub	
	}
}

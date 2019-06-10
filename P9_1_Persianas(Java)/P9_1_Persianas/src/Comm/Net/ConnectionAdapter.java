package Comm.Net;

public abstract class ConnectionAdapter implements ConnectionListener{
	
	@Override
	public void connLog(String message) {
		// TODO Auto-generated method stub
		
	}
	
	public void InData(Connection connection, String Line){
		System.out.println("Connection Adapter --> Dato Recibido : "+Line);
	}

	@Override
	public abstract void ConnectionOpened(Connection connection); 

	@Override
	public abstract void ConnectionClosed(Connection connection); 

}

package ModBus;

public enum mbConnection {
	SERIAL(0),
	TCP(1);
	
	int reg;
	
	mbConnection(int rg){
		reg = rg;
	}
	
	public int getReg(){
		return reg;
	}
}

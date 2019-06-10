package ModBus;

public class ModBusEvent {
	private int 		Regs[];
	private boolean		Digital;
	private int			RegBank = 0;
	private String[] 	args;
	
	public ModBusEvent(int Regs[]){
		this.Regs = Regs;
	}
	
	public int[] getRegs(){
		return Regs;
	}
	
	public void setRegs(int Regs[]){
		this.Regs = Regs;
	}
	
	public void setDigital(boolean Digital){
		this.Digital = Digital;
	}
	
	public boolean getDigital(){
		return Digital;
	}
	
	public void set_RegBank(int sRB){
		RegBank = sRB;
	}
	
	public int get_RegBank(){
		return RegBank;
	}
	
	public void set_Args(String[] args){
		this.args = args;
	}
	
	public String[] get_Args(){
		return args;
	}
}

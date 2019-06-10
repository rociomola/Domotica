package ModBus.cmd;

import Comm.io.CommTransport;
import ModBus.Const_Modbus;
import ModBus.io.ModbusSerialTransaction;
import ModBus.msg.ReadInputRegistersRequest;
import ModBus.msg.ReadInputRegistersResponse;;

public class Read_Input_Registers {
	
	public static void InitReadInputRegister(int unitid, int ref, int count, CommTransport sCon, int[] regs) {
		ReadInputRegistersRequest req = null;
		ModbusSerialTransaction trans = null;
		ReadInputRegistersResponse res = null;
		int repeat = 1;
		//int n;
	
		try{
			//Prepare a request
			req = new ReadInputRegistersRequest(ref, count);
			req.setUnitID(unitid);
			req.setHeadless();
	
			if (Const_Modbus.debug) System.out.println("Request: " + req.getHexMessage());

			//Prepare the transaction
			trans = new ModbusSerialTransaction(sCon);
			trans.setRequest(req);

			//Execute the transaction repeat times
			int k = 0;
			do {
				trans.execute();
				
				res = (ReadInputRegistersResponse) trans.getResponse();	
				
				
				for(int n=0;n<count;n++){
					//System.out.println("Response Reg "+n+": " + res.getRegisterValue(n));
					regs[n] = res.getRegisterValue(n);					
				}				
        
				k++;
			} while (k < repeat);
		} catch (Exception ex) {
			ex.printStackTrace();
			// Close the connection
			//con.close();
		}
	}
}

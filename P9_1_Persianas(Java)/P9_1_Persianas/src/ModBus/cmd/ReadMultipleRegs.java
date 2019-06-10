package ModBus.cmd;

import Comm.io.CommTransport;
import ModBus.Const_Modbus;
import ModBus.io.ModbusSerialTransaction;
import ModBus.msg.ReadMultipleRegistersRequest;
import ModBus.msg.ReadMultipleRegistersResponse;

public class ReadMultipleRegs {
	
	public static void InitReadMultipleRegs(int unitid, int ref, int count, CommTransport sCon, int[] regs) {
		ReadMultipleRegistersRequest req = null;
		ModbusSerialTransaction trans = null;
		ReadMultipleRegistersResponse res = null;
		int repeat = 1;
		//int n;
	
		try{
			//Prepare a request
			req = new ReadMultipleRegistersRequest(ref, count);
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
				
				res = (ReadMultipleRegistersResponse) trans.getResponse();	
				
				
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

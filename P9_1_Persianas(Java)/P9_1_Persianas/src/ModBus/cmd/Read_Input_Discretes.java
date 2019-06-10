package ModBus.cmd;

import Comm.io.CommTransport;
import ModBus.Const_Modbus;
import ModBus.io.ModbusSerialTransaction;
import ModBus.msg.ReadInputDiscretesRequest;
import ModBus.msg.ReadInputDiscretesResponse;
import ModBus.util.BitVector;

public class Read_Input_Discretes {
	
	public static void InitReadInputDiscretes(int unitid, int ref, int count, CommTransport sCon, int[] regs) {
		ReadInputDiscretesRequest req = null;
		ModbusSerialTransaction trans = null;
		ReadInputDiscretesResponse res = null;
		int repeat = 1;
		
		try{
			//Prepare a request
			req = new ReadInputDiscretesRequest(ref, count);
			req.setUnitID(unitid);
			req.setHeadless();
			if (Const_Modbus.debug) System.out.println("Request: " + req.getHexMessage());
		
			//Prepare the transaction
			trans = new ModbusSerialTransaction(sCon);
			trans.setRequest(req);
		
			//Execute the transaction repeat times
			int k = 0;
			do {
				//long start_time = System.currentTimeMillis();
				trans.execute();
				//System.out.println("Time execute: " + (System.currentTimeMillis() - start_time) + " ms");

				res = (ReadInputDiscretesResponse) trans.getResponse();
	        
				if (Const_Modbus.debug) System.out.println("Response: " + res.getHexMessage());
	        
				BitVector inputs = res.getDiscretes();
				//byte ret[] = new byte[inputs.size()];
				for (int i = 0; i < count; i++) {
					//System.out.println("Bit " + i + " = " + inputs.getBit(i));
					regs[ref+i] = inputs.getBit(i) ? 1 : 0;
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

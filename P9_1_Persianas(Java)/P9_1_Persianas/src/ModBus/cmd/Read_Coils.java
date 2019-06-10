package ModBus.cmd;

import Comm.Serial.io.SerialCommTransport;
import Comm.io.CommTransport;
import ModBus.Const_Modbus;
import ModBus.io.ModbusSerialTransaction;
import ModBus.msg.ReadCoilsRequest;
import ModBus.msg.ReadCoilsResponse;
import ModBus.util.BitVector;

public class Read_Coils {
	public static void InitReadCoils(int unitid, int ref, int count, CommTransport sCon, int[] regs){
		ReadCoilsRequest req = null;
		ModbusSerialTransaction trans = null;
		ReadCoilsResponse res = null;
		int repeat = 1;
		
		try{
			//Prepare a request
			req = new ReadCoilsRequest(ref, count);	
			req.setUnitID(unitid);
			req.setHeadless();
						
			if (Const_Modbus.debug) System.out.println("Request: " + req.getHexMessage());
			//Prepare the transaction
			//trans = new ModbusSerialTransaction(sCon);
			switch(CommTransport.get_tpConection()){
			case SERIAL:
				//Disable (El evento de recepción de datos por el puerto serie) MASK_RXCHAR
				//((SerialCommTransport)sCon).getSerialConnection().disableSerialReaderEvent(false);
				trans = new ModbusSerialTransaction(sCon);
				break;
				
			case TCP:
				
				break;
			
			}
			
			trans.setRequest(req);
		
			//Execute the transaction repeat times
			int k = 0;
			do {
				//long start_time = System.currentTimeMillis();
				trans.execute();
				//System.out.println("Time execute: " + (System.currentTimeMillis() - start_time) + " ms");
				
				res = (ReadCoilsResponse) trans.getResponse();
	        
				if (Const_Modbus.debug) System.out.println("Response: " + res.getHexMessage());
	        
				BitVector inputs = res.getCoils();
				//byte ret[] = new byte[inputs.size()];
				for (int i = 0; i < count; i++) {
					System.out.println("Bit " + i + " = " + inputs.getBit(i));
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
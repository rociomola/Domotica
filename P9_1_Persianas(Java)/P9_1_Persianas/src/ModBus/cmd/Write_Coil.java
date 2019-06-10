package ModBus.cmd;

import Comm.Serial.io.SerialCommTransport;
import Comm.io.CommTransport;
import ModBus.Const_Modbus;
import ModBus.io.ModbusSerialTransaction;
import ModBus.io.ModbusTransaction;
import ModBus.msg.WriteCoilRequest;

public class Write_Coil {
	
	public static void InitWriteSingleReg(int unitid, int ref, int count, CommTransport sCon) {
		
		WriteCoilRequest req = null;
		ModbusTransaction trans = null;
		int repeat = 1;
	
		try{
			//Prepare a request
			req = new WriteCoilRequest(ref, count == 1);
			req.setUnitID(unitid);
			req.setHeadless();
	
			if (Const_Modbus.debug) System.out.println("Request: " + req.getHexMessage());

			//Prepare the transaction
			/*
			 * Dependiendo de la conexi�n, serie, TCP, etc., la implementaci�n ModBus tiene connotaciones diferentes,
			 * por lo que aqu� deber�amos hacer una deferenciaci�n para seleccionar la transacci�n ModBus En funci�n 
			 * del tipo de conexi�n.
			 */
			
			switch(CommTransport.get_tpConection()){
			case SERIAL:
				//TODO: Disable (El evento de recepci�n de datos por el puerto serie) MASK_RXCHAR
//				((SerialCommTransport)sCon).getSerialConnection().disableSerialReaderEvent();
				trans = new ModbusSerialTransaction(sCon);
				break;
				
			case TCP:
				
				break;
			
			}

			trans.setRequest(req);
			
			//Execute the transaction repeat times
			int k = 0;
			do {
				trans.execute();
        				
				/*
				if (Const_Modbus.debug) 
					System.out.println("Response 2: " + trans.getResponse().getHexMessage());
				*/
				
				k++;
			} while (k < repeat);
		} catch (Exception ex) {
			ex.printStackTrace();
			// Close the connection
			//con.close();
		}
	}
}

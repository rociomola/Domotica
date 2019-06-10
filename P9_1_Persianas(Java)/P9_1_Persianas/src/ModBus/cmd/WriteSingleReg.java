package ModBus.cmd;

import Comm.io.CommTransport;
import ModBus.Const_Modbus;
import ModBus.io.ModbusSerialTransaction;
import ModBus.io.ModbusTransaction;
import ModBus.msg.WriteSingleRegisterRequest;
import ModBus.procimg.SimpleRegister;

public class WriteSingleReg {
	
	public static void InitWriteSingleReg(int unitid, int ref, int count, CommTransport sCon) {
		
		WriteSingleRegisterRequest req = null;
		ModbusTransaction trans = null;
		int repeat = 1;
		
		try{
			//Prepare a request
			req = new WriteSingleRegisterRequest(ref, new SimpleRegister(count));
			req.setUnitID(unitid);
			req.setHeadless();
		
			if (Const_Modbus.debug) System.out.println("Request: " + req.getHexMessage());

			//Prepare the transaction
			/*
			 * Dependiendo de la conexión, serie, TCP, etc., la implementación ModBus tiene connotaciones diferentes,
			 * por lo que aquí deberíamos hacer una deferenciación para seleccionar la transacción ModBus En función 
			 * del tipo de conexión.
			 */
			
			switch(CommTransport.get_tpConection()){
			case SERIAL:
				//TODO: Disable (El evento de recepción de datos por el puerto serie) MASK_RXCHAR
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
				if (Modbus.debug) 
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

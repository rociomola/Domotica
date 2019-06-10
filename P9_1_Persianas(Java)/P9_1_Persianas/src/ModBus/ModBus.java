package ModBus;

//import Comm.Serial.io.SerialCommTransport;
import Comm.io.CommTransport;
import ModBus.cmd.ReadMultipleRegs;
import ModBus.cmd.Read_Coils;
import ModBus.cmd.Read_Input_Discretes;
import ModBus.cmd.WriteSingleReg;
import ModBus.cmd.Write_Coil;
import ModBus.cmd.Read_Input_Registers;

//import ModBus.io.ModbusSerialTransaction;


public class ModBus {
	/* 
	 * Inicia la comunicación ModBus
	 */
	public static void InitComunication(String[] args, CommTransport sCon, int[] reg) {
		int unitid = 0;
		int cmd = 0;
	    int ref = 0;		//Registro Inicial
	    int count = 0;
		
		//1. Setup the parameters
	     
    	if (args.length < 3) {
          printUsage();
          System.exit(1);
        } else {
        	try {
        		unitid = Integer.parseInt(args[0]);
        		cmd = Integer.parseInt(args[1]);
        		ref = Integer.parseInt(args[2]);
        		count = Integer.parseInt(args[3]);
        		/*
        		if (args.length == 4) {
        			repeat = Integer.parseInt(args[3]);
        		}
        		*/
    	
        	} catch (Exception ex) {
        		ex.printStackTrace();
        		printUsage();
        		System.exit(1);
        	}
        }
    	
    	//2. Set slave identifier for master response parsing
//    	ModbusCoupler.getReference().setUnitID(unitid);
    	
    	//Disable (El evento de recepción de datos) MASK_RXCHAR
    	sCon.disableReaderEvent(false);
    	
    	//3. Procesa comando
    	switch(cmd){
    		case Const_Modbus.WRITE_COIL: //0x05
    			Write_Coil.InitWriteSingleReg(unitid, ref, count, sCon);
    			break;
    			
    		case Const_Modbus.READ_COILS:  //0x01
	    		Read_Coils.InitReadCoils(unitid, ref, count, sCon, reg);
	    		break;
	    		
    		case Const_Modbus.READ_INPUT_DISCRETES:	//0x02
    			Read_Input_Discretes.InitReadInputDiscretes(unitid, ref, count, sCon, reg);
    			break;
			
    		
    		case Const_Modbus.WRITE_SINGLE_REGISTER: //0x06
    			WriteSingleReg.InitWriteSingleReg(unitid, ref, count, sCon);
    			break;
    			
    		case Const_Modbus.READ_MULTIPLE_REGISTERS: //0x03
    			ReadMultipleRegs.InitReadMultipleRegs(unitid, ref, count, sCon, reg);    			
    			break;
    			
    		case Const_Modbus.READ_INPUT_REGISTERS: //0x04
    			Read_Input_Registers.InitReadInputRegister(unitid, ref, count, sCon, reg);
    			break;
    		    		
    		case Const_Modbus.WRITE_MULTIPLE_REGISTERS:
    			
    			break;
    			
    		default:
    			System.out.println("Comando no soportado: "+ ref);
    			break;	    		
    	}
    	
    	sCon.disableReaderEvent(true);
	}
	
	private static void printUsage() {
        System.out.println(
            " <Unit Address [int8]> <register [int16]> <wordcount [int16]> {<repeat [int]>}"
        );
      }//printUsage
}

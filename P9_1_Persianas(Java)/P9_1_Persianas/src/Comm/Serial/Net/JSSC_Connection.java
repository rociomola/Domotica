package Comm.Serial.Net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import Comm.Serial.JSSC.SerialPortList;
import Comm.Serial.JSSC.UnsignedByteSerialInputStream;
import ModBus.util.ModbusUtil;

public class JSSC_Connection extends SerialConnection{
	public static final int MAX_MESSAGE_LENGTH = 256;
	
	private static SerialPort serialPort;
	private static ByteArrayOutputStream 	bOutput;
	private static InputStream 				inputStream;  
	//private static byte[]					bInputBuffer;
	
	@Override
	public List<String> ComPortList() {		
		return Arrays.asList(SerialPortList.getPortNames());
	}
	
	@Override
	protected void doClose() {
		//Auto-generated method stub
		if (serialPort != null){
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				//Auto-generated catch block
				e.printStackTrace();
				log(e.getMessage());
			}
			serialPort = null;
			
			try {
				bOutput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void open(String comPort) {
		close();
		
		this.setComPort(comPort);
		
		try{
			serialPort = new SerialPort(comPort);
		
			serialPort.openPort();	
			
			serialPort.setParams(this.getBaudRate(),this.getDataBits(),this.getStopBits(),this.getParity());
			
			//Preparing a mask. In a mask, we need to specify the types of events that we want to track.
            //Well, for example, we need to know what came some data, thus in the mask must have the
            //following value: MASK_RXCHAR. If we, for example, still need to know about changes in states 
            //of lines CTS and DSR, the mask has to look like this: SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR
            int mask = SerialPort.MASK_RXCHAR;
            //Set the prepared mask
            serialPort.setEventsMask(mask);
			isOpen = true;
			
			/*
			 * New output stream
			 */
			bOutput = new ByteArrayOutputStream(MAX_MESSAGE_LENGTH);
			
			(new Thread(new SerialWriter(bOutput))).start();
			
			/*
			 * New input stream
			 */
			//bInputBuffer 	= new byte[Const_Modbus.MAX_MESSAGE_LENGTH];
			//bInput			= new BytesInputStream(Const_Modbus.MAX_MESSAGE_LENGTH);
			
			inputStream = new UnsignedByteSerialInputStream(serialPort);
			//bInput.reset();
			//bInput.
			
			//*********************
			
			//Incluir Event Listener y los eventos que va a "Oir"
			serialPort.addEventListener(new SerialReader(), SerialPort.MASK_RXCHAR);			
			
			serialOpened();
			
		} catch (SerialPortException e) {
			//Auto-generated catch block
			e.printStackTrace();
			
			log(e.getMessage());
		}
	}
	
	@Override
	public void sendData(String msg){
		try {
			if(serialPort != null)			
				serialPort.writeString(msg);
		} catch (SerialPortException e) {
			//Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class SerialWriter implements Runnable 
	{
		ByteArrayOutputStream out;
	        
	    public SerialWriter ( ByteArrayOutputStream out )
	    {
	    	this.out = out;
	    }
	        
	    public void run ()
	    {
	    	try
	        {          
	    		//send stream
	    		while(true){
	    			if(out.size() > 0){
	    				serialPort.writeBytes(out.toByteArray());	    				
	    				out.reset();
	    			}
	    		}         
	        }
	        catch ( SerialPortException e )
	        {
	        	e.printStackTrace();
	            System.exit(-1);
	        }            
	    }
	}
	
    /*
     * In this class must implement the method serialEvent, through it we learn about 
     * events that happened to our port. But we will not report on all events but only 
     * those that we put in the mask. In this case the arrival of the data and change the 
     * status lines CTS and DSR
     */
    class SerialReader implements SerialPortEventListener {
    	
    	private byte[] buffer = new byte[100];

    	public void serialEvent(SerialPortEvent event) {
    		int data;
    		
            if (event.isRXCHAR()) {
//                LOG.trace(String.format("RXCHAR event received, value %d", event.getEventValue()));
                try {
                	/*
                    if (inputStream.available() > 0) {
                        synchronized (this) {
                            this.notify();
                        }
                    }
                    */
                	
                    int len = 0;
                    
                    //inputStream.available()
                    
    	        	//while ( ( data = inputStream.read()) > -1 )
    	            while(inputStream.available() > 0)
                    {
    	            	data = inputStream.read();
    	        		buffer[len++] = (byte) data;
    	        		if ( data == '\n' ) {
    	                        break;
    	                }
    	            }
    	        	
    	        	log(new String(buffer,0,len));
 //   	        	log(ModbusUtil.toHex(buffer, 0, len));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //log(String.format("Unexpected event type %d received", event.getEventType()));
            }
        }
    }
    	
	@Override
	public OutputStream getOutputStream() {
		// Auto-generated method stub		
		return bOutput;
	}

	@Override
	public void disableSerialReaderEvent(boolean EnDis) {
		// Auto-generated method stub

		try {
			int eventMask = serialPort.getEventsMask();
			
			if(EnDis){
				eventMask = eventMask | SerialPort.MASK_RXCHAR;
			}
			else eventMask = eventMask & ~SerialPort.MASK_RXCHAR;
			
			serialPort.setEventsMask(eventMask);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public InputStream getInputStream() {
		// Auto-generated method stub
		return inputStream;
	}
}

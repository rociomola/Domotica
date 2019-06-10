package Comm.Serial.Net;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JOptionPane;

import ModBus.util.ModbusUtil;
import Utilidades.ArrayUtilities;

public class RxTxConnection extends SerialConnection{
	
	private SerialPort serialPort;
	
	private OutputStream	m_OutputStream;
//	private InputStream		m_InputStream;

	@Override
	public List<String> ComPortList() {
		String comportList[] = new String[0];
		int n;
			
		@SuppressWarnings("unchecked")
		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
			
		n = 0;
			
		while ( portEnum.hasMoreElements() ) 
		{
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			
			if(portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL){
				comportList = (String[])ArrayUtilities.resizeArray(comportList, n+1);
				comportList[n] = portIdentifier.getName();
				n++;
			}			
		}
		return Arrays.asList(comportList);
	}
	
	@Override
	public OutputStream	getOutputStream(){
		return m_OutputStream;
	}

	@Override
	public void open(String comPort) {
	    close();
	    
	    this.setComPort(comPort);
	    
	    try 
	    {    	
	    	CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(comPort);
	    	if ( portIdentifier.isCurrentlyOwned() )
	    	{
	        	 log("Error: Port is currently in use");
	             //System.out.println("Error: Port is currently in use");
	    	}
	    	else
	    	{
	    		CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
	             
	    		if ( commPort instanceof SerialPort )
	    		{	    			

	    			serialPort = (SerialPort) commPort;
	            
	    			serialPort.setSerialPortParams(this.getBaudRate(),this.getDataBits(),this.getStopBits(),this.getParity());
				
	    			isOpen = true;	
               
	    			InputStream in = serialPort.getInputStream();
	    			
	    			setSerialOutput(serialPort.getOutputStream());
	                 
	    			serialPort.addEventListener(new SerialReader(in));
	    			serialPort.notifyOnDataAvailable(true);	
	    			
	    			serialOpened();
	    		}
	    		else
	    		{
	            		 log("Error: Only serial ports are handled by this example.");
	                 //System.out.println("Error: Only serial ports are handled by this example.");
	    		}
	    	}     
	    } catch (PortInUseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
//			serialData("PortInUseException : "+e.getMessage());
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "can't find the specified port", "Error", JOptionPane.ERROR_MESSAGE);
		}   //open("NMEAPrueba", 3000);

	    catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doClose() {
		// TODO Auto-generated method stub
		if (serialPort != null){
			serialPort.close();
			serialPort = null;
		}
	}
	
	protected void setSerialOutput(OutputStream m_OutputStream) {
		this.m_OutputStream = m_OutputStream;
	}
	
	@Override
	public void sendData(String msg){
		try {
			if(serialPort != null)
				m_OutputStream.write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 /**
	  * Handles the input coming from the serial port. A new line character
	  * is treated as the end of a block in this example. 
	  */
	public class SerialReader implements SerialPortEventListener 
	{
		private InputStream in;
	    private byte[] buffer = new byte[1024];
	        
	    public SerialReader ( InputStream in )
	    {
	    	this.in = in;
	    }
	        
	    public void serialEvent(SerialPortEvent arg0) 
	    {
	    	int data;
	          
	        try
	        {
	        	serialPort.notifyOnDataAvailable(false);
	        	
	        	int len = 0;
	            
	        	while ( ( data = in.read()) > -1 )
	            {
	        		buffer[len++] = (byte) data;
	        		if ( data == '\n' ) {
	                        break;
	                }	        	
	            }
	        	
	        	log(new String(buffer,0,len));
	        	log(ModbusUtil.toHex(buffer, 0, len));
	        	
	        	serialPort.notifyOnDataAvailable(true);
	            
	        }
	        catch ( IOException e )
	        {
	        	e.printStackTrace();
	            System.exit(-1);
	        }             
	    }
	}

	@Override
	public void disableSerialReaderEvent(boolean EnDis) {
		//Auto-generated method stub
		serialPort.notifyOnDataAvailable(EnDis);
	}

	@Override
	public InputStream getInputStream() {
		//Auto-generated method stub
		InputStream in = null;
		
		try {
			in = serialPort.getInputStream();
		} catch (IOException e) {
			//Auto-generated catch block
			e.printStackTrace();
		}
		
		return in;
	}

}

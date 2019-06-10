package Comm.Serial.Net;

import javax.swing.AbstractAction;
import javax.swing.JRadioButtonMenuItem;

import java.awt.event.ActionEvent;

//import net.wimpi.modbus.util.SerialParameters;

public class SerialAction extends AbstractAction implements Runnable {

	  private static final long serialVersionUID = 1L;

	  private boolean isRunning;
	  //private SerialParameters params;
	  //private SerialConnection serialConnection;
	  private SerialConnection serialConnection;
	  private String portName;
	  private Object RadioButtonMenuItem;

	  public SerialAction(String name, SerialConnection serialConnection) {
		  super(name);
		  
		  this.serialConnection = serialConnection;
		  //serialConnection = serialConnect;
	  }

	  public void actionPerformed(ActionEvent e) {
		  //params = new SerialParameters();
	      //params.setPortName(e.getActionCommand());
		  portName = e.getActionCommand();
		  
		  RadioButtonMenuItem = e.getSource();
	      
	      //if(params.getPortName() != "Serial"){
	    	  if (!isRunning) {				  
	    		  isRunning = true;
	    		  new Thread(this, "serial").start();
	    	  }
	      //}
	  }
	  
	  protected void connectToSerial() {
		  
		  if (serialConnection != null && !serialConnection.isOpen()) {
		    	
			  try{
				  serialConnection.open(portName);
				  //timer.start();
				  //roundBtn1.setText(serialConnection.getSerialPort().toString().substring(4));
				  //roundBtn1.setButtonTheme(Theme.STANDARD_RED_THEME);
		    		
			  } catch (Exception ex) {
				  ex.printStackTrace();
				  // Close the connection
				  serialConnection.close();
			  }
		  }
	  }

	  public void run() 
	  {
		  try 
		  {			  
			  if (serialConnection.isOpen()) {
				  serialConnection.close();
				  //timer.stop();
				  //roundBtn1.setButtonTheme(Theme.STANDARD_GREEN_THEME);
				  //roundBtn1.repaint();
			  } else {							 
				  connectToSerial();						  
			  }
		  } 
		  finally 
		  {
			  isRunning = false;
			  
			  ((JRadioButtonMenuItem)RadioButtonMenuItem).setSelected(serialConnection.isOpen);
		  }
	  }	
}
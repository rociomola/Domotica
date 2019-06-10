package ModBus.util;

import ModBus.Const_Modbus;

public class SerialParameters {
		
	/*
	 * Usamos m_Encoding como static puesto que sólo hemos implentado para ModBus la codificación RTU 
	 */
	private static String 	m_Encoding = Const_Modbus.DEFAULT_SERIAL_ENCODING;	
	@SuppressWarnings("unused")
	private boolean 		m_Echo = true;	
	private static int 		m_ReceiveTimeout = 500;
	
	/**
	 * Constructs a new <tt>SerialParameters</tt> instance with
	 * default values.
	 */
	public SerialParameters() {
		m_Encoding = Const_Modbus.DEFAULT_SERIAL_ENCODING;
		m_ReceiveTimeout = 500; //5 secs
	    m_Echo = false;
	}
	
	/**
	   * Constructs a new <tt>SerialParameters<tt> instance with
	   * given parameters.
	   *
	   * @param portName       The name of the port.
	   * @param baudRate       The baud rate.
	   * @param flowControlIn  Type of flow control for receiving.
	   * @param flowControlOut Type of flow control for sending.
	   * @param databits       The number of data bits.
	   * @param stopbits       The number of stop bits.
	   * @param parity         The type of parity.
	   * @param echo           Flag for setting the RS485 echo mode.
	   */
	  public SerialParameters(boolean echo,
	                          int timeout) 
	  {	    
		  m_Echo = echo;
		  m_ReceiveTimeout = timeout;
	  }//constructor
	
	/**
	 * Sets the encoding to be used.
	 *
	 * @param enc the encoding as string.
	 * @see Modbus#SERIAL_ENCODING_ASCII
	 * @see Modbus#SERIAL_ENCODING_RTU
	 * @see Modbus#SERIAL_ENCODING_BIN
	 */
	public void setEncoding(String enc) {
		enc = enc.toLowerCase();
	    if (enc.equals(Const_Modbus.SERIAL_ENCODING_ASCII) ||
	    		enc.equals(Const_Modbus.SERIAL_ENCODING_RTU) ||
	    		enc.equals(Const_Modbus.SERIAL_ENCODING_BIN)) 
	    {
	    	m_Encoding = enc;
	    } else {
	    	m_Encoding = Const_Modbus.DEFAULT_SERIAL_ENCODING;
	    }
	}//setEncoding

	/**
	 * Returns the encoding to be used.
	 *
	 * @return the encoding as string.
	 * @see Modbus#SERIAL_ENCODING_ASCII
	 * @see Modbus#SERIAL_ENCODING_RTU
	 * @see Modbus#SERIAL_ENCODING_BIN
	 */
	public static String getEncoding() {
		return m_Encoding;
	}//getEncoding

	/**
	 * Returns the receive timeout for serial communication.
	 *
	 * @return the timeout in milliseconds.
	 */
	public static int getReceiveTimeout() {
		return m_ReceiveTimeout;
	}//getReceiveTimeout

	/**
	 * Sets the receive timeout for serial communication.
	 *
	 * @param receiveTimeout the receiveTimeout in milliseconds.
	 */
	public void setReceiveTimeout(int receiveTimeout) {
		m_ReceiveTimeout = receiveTimeout;
	}//setReceiveTimeout

	/**
	 * Sets the receive timeout for the serial communication
	 * parsing the given String using <tt>Integer.parseInt(String)</tt>.
	 *
	 * @param str the timeout as String.
	 */
	public void setReceiveTimeout(String str) {
		m_ReceiveTimeout = Integer.parseInt(str);
	}//setReceiveTimeout
}

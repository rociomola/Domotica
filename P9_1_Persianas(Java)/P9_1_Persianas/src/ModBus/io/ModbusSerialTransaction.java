/***
 * Copyright 2002-2010 jamod development team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***/

package ModBus.io;

import java.io.IOException;

import Comm.Serial.io.SerialCommTransport;
import Comm.io.CommTransport;
import ModBus.Const_Modbus;
import ModBus.ModbusException;
import ModBus.ModbusIOException;
import ModBus.ModbusSlaveException;
import ModBus.msg.ExceptionResponse;
import ModBus.msg.ModbusRequest;
import ModBus.msg.ModbusResponse;
import ModBus.util.AtomicCounter;
import ModBus.util.Mutex;
import ModBus.util.SerialParameters;



/**
 * Class implementing the <tt>ModbusTransaction</tt>
 * interface.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ModbusSerialTransaction implements ModbusTransaction {

	//class attributes
	private static AtomicCounter c_TransactionID = new AtomicCounter(Const_Modbus.DEFAULT_TRANSACTION_ID);

	//instance attributes and associations
	private ModbusTransport m_IO;
	private ModbusRequest m_Request;
	private ModbusResponse m_Response;
	private boolean m_ValidityCheck = Const_Modbus.DEFAULT_VALIDITYCHECK;
	private int m_Retries = Const_Modbus.DEFAULT_RETRIES;
	private int m_TransDelayMS = Const_Modbus.DEFAULT_TRANSMIT_DELAY;
	//private SerialConnection m_SerialCon;
	private SerialCommTransport	m_SerialCon;

	private Mutex m_TransactionLock = new Mutex();

	/**
	 * Constructs a new <tt>ModbusSerialTransaction</tt>
	 * instance.
	 */
	public ModbusSerialTransaction() {
	}//constructor

	/**
	 * Constructs a new <tt>ModbusSerialTransaction</tt>
	 * instance with a given <tt>ModbusRequest</tt> to
	 * be send when the transaction is executed.
	 * <p/>
	 *
	 * @param request a <tt>ModbusRequest</tt> instance.
	 */
	public ModbusSerialTransaction(ModbusRequest request) {
		setRequest(request);
	}//constructor

	/**
	 * Constructs a new <tt>ModbusSerialTransaction</tt>
	 * instance with a given <tt>ModbusRequest</tt> to
	 * be send when the transaction is executed.
	 * <p/>
	 *
	 * @param con a <tt>TCPMasterConnection</tt> instance.
	 * @throws IOException 
	 */
	/*  
  	public ModbusSerialTransaction(SerialConnection con) {
    	setSerialConnection(con);
  	}//constructor
	*/
  
	public ModbusSerialTransaction(CommTransport con) throws IOException {
		m_SerialCon = (SerialCommTransport)con;
		
		//S�lo est� implementada la codificaci�n RTU, por lo que :
		if (Const_Modbus.SERIAL_ENCODING_RTU.equals(SerialParameters.getEncoding())) {
			m_IO = new ModbusRTUTransport();
			setReceiveTimeout(SerialParameters.getReceiveTimeout()); //just here for the moment.
			
			//Prepare Stream
			((ModbusRTUTransport)m_IO).prepareStreams(m_SerialCon.getInputStream(), m_SerialCon.getOutputStream());
		}
		
	//	m_IO = con.getModbusTransport();
	}//constructor
	
	public void setReceiveTimeout(int ms) {
		// Set receive timeout to allow breaking out of polling loop during
	    // input handling.
/*
		try {
	    	m_SerialPort.enableReceiveTimeout(ms);
	    } catch (UnsupportedCommOperationException e) {
	    	if(Const_Modbus.debug) System.out.println(e.getMessage());
	    }
*/
	}//setReceiveTimeout
  
	/**
	 * Sets the port on which this <tt>ModbusTransaction</tt>
	 * should be executed.<p>
	 * <p/>
	 *
	 * @param con a <tt>SerialConnection</tt>.
	 */
	/*
  	public void setSerialConnection(SerialConnection con) {
    	m_SerialCon = con;
    	m_IO = con.getModbusTransport();
  	}//setConnection
	 */
	public int getTransactionID() {
		return c_TransactionID.get();
	}//getTransactionID

	public void setRequest(ModbusRequest req) {
		m_Request = req;
		//m_Response = req.getResponse();
	}//setRequest

	public ModbusRequest getRequest() {
		return m_Request;
	}//getRequest

	public ModbusResponse getResponse() {
		return m_Response;
	}//getResponse

	public void setCheckingValidity(boolean b) {
		m_ValidityCheck = b;
	}//setCheckingValidity

	public boolean isCheckingValidity() {
		return m_ValidityCheck;
	}//isCheckingValidity

	public int getRetries() {
		return m_Retries;
	}//getRetries

	public void setRetries(int num) {
		m_Retries = num;
	}//setRetries

	/**
	 * Get the TransDelayMS value.
	 *
	 * @return the TransDelayMS value.
	 */
	public int getTransDelayMS() {
		return m_TransDelayMS;
	}

	/**
	 * Set the TransDelayMS value.
	 *
	 * @param newTransDelayMS The new TransDelayMS value.
	 */
	public void setTransDelayMS(int newTransDelayMS) {
		this.m_TransDelayMS = newTransDelayMS;
	}

	public void execute() throws ModbusIOException,ModbusSlaveException, ModbusException {
		//1. assert executeability
		assertExecutable();

		try {
			//2. Lock transaction
			/**
			 * Note: The way this explicit synchronization is implemented at the moment,
			 * there is no ordering of pending threads. The Mutex will simply call notify()
			 * and the JVM will handle the rest.
			 */
			m_TransactionLock.acquire();

			//3. write request, and read response,
			//   while holding the lock on the IO object
			synchronized (m_IO) {
				int tries = 0;
				boolean finished = false;

				//toggle the id
				m_Request.setTransactionID(c_TransactionID.increment());

				do {
					try {
						if (m_TransDelayMS > 0) {
							try {
								Thread.sleep(m_TransDelayMS);
							} catch (InterruptedException ex) {
								System.err.println("InterruptedException: " + ex.getMessage());
							}
						}
						//write request message						
						m_IO.writeMessage(m_Request);
						
						//read response message						
						m_Response = m_IO.readResponse();
						
						finished = true;
					} catch (ModbusIOException e) {
						if (++tries >= m_Retries) {
							throw e;
						}
						System.err.println("execute try " + tries + " error: " +
								e.getMessage());
					}
				} while (!finished);
			}

			//4. deal with exceptions
			if (m_Response instanceof ExceptionResponse) {
				throw new ModbusSlaveException(
						((ExceptionResponse) m_Response).getExceptionCode()
						);
			}

			if (isCheckingValidity()) {
				checkValidity();
			}
		} catch (InterruptedException ex) {
			throw new ModbusIOException("Thread acquiring lock was interrupted.");	
		} finally {
			m_TransactionLock.release();
		}
	}//execute

	/**
	 * Asserts if this <tt>ModbusTCPTransaction</tt> is
	 * executable.
	 *
	 * @throws ModbusException if the transaction cannot be asserted.
	 */ 
	private void assertExecutable()
			throws ModbusException {
		if (m_Request == null || !m_SerialCon.getSerialConnection().isOpen()) {
			throw new ModbusException(
				  "Assertion failed, transaction not executable"
				  );
		}
	}//assertExecuteable

	/**
	 * Checks the validity of the transaction, by
	 * checking if the values of the response correspond
	 * to the values of the request.
	 *
	 * @throws ModbusException if the transaction is not valid.
	 */
	protected void checkValidity() throws ModbusException {
	}//checkValidity

}//class ModbusSerialTransaction

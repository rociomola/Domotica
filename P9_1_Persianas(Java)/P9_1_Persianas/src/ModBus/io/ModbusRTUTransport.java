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
import java.io.InputStream;
import java.io.OutputStream;

import ModBus.Const_Modbus;
import ModBus.ModbusIOException;
import ModBus.msg.ModbusMessage;
import ModBus.msg.ModbusRequest;
import ModBus.msg.ModbusResponse;
import ModBus.util.ModbusUtil;

/**
 * Class that implements the ModbusRTU transport
 * flavor.
 *
 * @author John Charlton
 * @author Dieter Wimberger
 *
 * @version @version@ (@date@)
 */
public class ModbusRTUTransport extends ModbusSerialTransport {

	private InputStream m_InputStream;    //wrap into filter input
	private OutputStream m_OutputStream;      //wrap into filter output
  
	private long start_time;

	private byte[] m_InBuffer;	
	private BytesOutputStream m_ByteInOut;     //to buffer message to
	private BytesOutputStream m_ByteOut;      //write frames
	private BytesInputStream m_ByteIn;         //to read message from
	private byte[] lastRequest = null;
	
	/*
	 * Introducido en Electronica para Domótica
	 */
	public ModbusRTUTransport(){
			
	}
	
	//************************************************************
	
	public void writeMessage(ModbusMessage msg) throws ModbusIOException {
		try {
			int len;

			synchronized (m_ByteOut) {
				// first clear any input from the receive buffer to prepare
				// for the reply since RTU doesn't have message delimiters
				clearInput();
				//write message to byte out
				m_ByteOut.reset();
				msg.setHeadless();
				msg.writeTo(m_ByteOut);
				len = m_ByteOut.size();
				int[] crc = ModbusUtil.calculateCRC(m_ByteOut.getBuffer(), 0, len);
				m_ByteOut.writeByte(crc[0]);
				m_ByteOut.writeByte(crc[1]);
				//write message
				len = m_ByteOut.size();
				byte buf[] = m_ByteOut.getBuffer();
				
//				ActionEvent evt = new ActionEvent(this, 0, message);
//				ModbusSerialTransportListener.actionPerformed(evt);

				m_OutputStream.write(buf, 0, len);     //PDU + CRC
				start_time = System.currentTimeMillis();
				m_OutputStream.flush();

				if(Const_Modbus.debug) System.out.println("Sent: " + ModbusUtil.toHex(buf, 0, len));
/*
				//clears out the echoed message
				// for RS485
				if (m_Echo) {
					readEcho(len);
				}
*/        
				lastRequest = new byte[len];
				System.arraycopy(buf, 0, lastRequest, 0, len);
			}
		} catch (Exception ex) {
			throw new ModbusIOException("I/O failed to write");
		}

	}//writeMessage

	//This is required for the slave that is not supported
	public ModbusRequest readRequest() throws ModbusIOException {
		throw new RuntimeException("Operation not supported.");
	} //readRequest

	/**
	 * Clear the input if characters are found in the input stream.
	 *
	 * @throws IOException
	 */
	public void clearInput() throws IOException {
	
		if (m_InputStream.available() > 0) {
			int len = m_InputStream.available();
			byte buf[] = new byte[len];
			m_InputStream.read(buf, 0, len);
			if(Const_Modbus.debug) System.out.println("Clear input: " +
					ModbusUtil.toHex(buf, 0, len));
		}
	}//cleanInput
  
	public ModbusResponse readResponse() throws ModbusIOException {
		ModbusResponse response = null;
		
		boolean done = false;
		int data, len, fc = 0, bc = 0;
		
		start_time = System.currentTimeMillis();
		try{
			do {
				//1. read to function code, create request and read function specific bytes
				synchronized (m_ByteIn) {
					//Esperamos a que haya datos disponibles;
					while(m_InputStream.available() <= 5){
						if((System.currentTimeMillis() - start_time) > 100 )
							break;
					};
				
//					System.out.println("Available : "+m_InputStream.available());
					
					len = 0;
					while ( ( data = m_InputStream.read()) > -1 ){
						//buffer[len++] = (byte) data;
			    	
						switch(len){
							case 0:
								if(data != lastRequest[0]){
									throw new IOException("Error reading response : "+ data);
								}
								else m_ByteInOut.reset();
						
								break;
						
							case 1:
								//create response to acquire length of message
								fc = data;
								response = ModbusResponse.createModbusResponse(data);
								response.setHeadless();
												
								break;
						
							case 2:
								switch(fc){
									case 0x01:
									case 0x02:
									case 0x03:
									case 0x04:
										bc = data+2;
										break;									
																		
									case 0x0C:
									case 0x11:  // report slave ID version and run/stop state
									case 0x14:  // read log entry (60000 memory reference)
									case 0x15:  // write log entry (60000 memory reference)
									case 0x17:
										bc = data;
										break;
				        	
									case 0x05:
									case 0x06:
									case 0x0B:
									case 0x0F:
									case 0x10:
									case 0x81:
										// read status: only the CRC remains after address and function code
										bc = response.getDataLength()-1;
										/*
				        					setReceiveThreshold(6);
				        					inpBytes = m_InputStream.read(inpBuf, 0, 6);
				        					out.write(inpBuf, 0, inpBytes);
				        					m_CommPort.disableReceiveThreshold();
										 */

										break;
				       
									case 0x07:
									case 0x08:
										// read status: only the CRC remains after address and function code
										bc = 3 - 2;
										/*
				        					setReceiveThreshold(3);
				          					inpBytes = m_InputStream.read(inpBuf, 0, 3);
				          					out.write(inpBuf, 0, inpBytes);
				          					m_CommPort.disableReceiveThreshold();
				          				*/

										break;
				          	
									case 0x16:
										// eight bytes in addition to the address and function codes
										bc = 8 - 2;
										/*
				        					setReceiveThreshold(8);
				        					inpBytes = m_InputStream.read(inpBuf, 0, 8);
				        					out.write(inpBuf, 0, inpBytes);
				        					m_CommPort.disableReceiveThreshold();
										 */

										break;
				        
									case 0x18:
										// read the byte count word
										bc = data;
										m_ByteInOut.writeByte(data);
										data = m_InputStream.read();
										bc = ModbusUtil.makeWord(bc, data);
				        	
										/*
				        					bc = m_InputStream.read();
				        					out.write(bc);
				        					bc2 = m_InputStream.read();
				        					out.write(bc2);
				        					bcw = ModbusUtil.makeWord(bc, bc2);
				        					// now get the specified number of bytes and the 2 CRC bytes
				        					setReceiveThreshold(bcw+2);
				        					inpBytes = m_InputStream.read(inpBuf, 0, bcw + 2);
				        					out.write(inpBuf, 0, inpBytes);
				        					m_CommPort.disableReceiveThreshold();
										 */
		
										break;										
								
									case 0x86:
									case 0x83:
										// read the byte of code error
										bc = 0;
										/*
										bc = m_InputStream.read();
										out.write(bc);            
										// now get 2 CRC bytes
										setReceiveThreshold(2);
										inpBytes = m_InputStream.read(inpBuf, 0, 2);
										out.write(inpBuf, 0, inpBytes);
										m_CommPort.disableReceiveThreshold();										
										*/
										break;
								}
						
							default:
								
								break;
						}
							
						//len++;
						m_ByteInOut.writeByte(data);
					 
						if( (++len >= bc+3)){
							break;
						}
						//System.out.println("Time Available a "+len+" : " + (System.currentTimeMillis() - start_time) + " ms");
					}
				
					int dlength = len - 2; // less the crc
					if (Const_Modbus.debug) System.out.println("Response: " +
							ModbusUtil.toHex(m_ByteInOut.getBuffer(), 0, dlength + 2));

					m_InBuffer = m_ByteInOut.getBuffer();
					//m_ByteIn.reset(m_InBuffer, dlength);

					//check CRC
					int[] crc = ModbusUtil.calculateCRC(m_InBuffer, 0, dlength); //does not include CRC
			
					if (ModbusUtil.unsignedByteToInt(m_InBuffer[dlength]) != crc[0]
							|| ModbusUtil.unsignedByteToInt(m_InBuffer[dlength + 1]) != crc[1]) {
						throw new IOException("CRC Error in received frame: " + dlength + " bytes: " + ModbusUtil.toHex(m_ByteIn.getBuffer(), 0, dlength));
					}
				
					//read response
					m_ByteIn.reset(m_InBuffer, dlength);
					
					if (response != null) {
						response.readFrom(m_ByteIn);
					}
					
					done = true;
				}
			}while(!done);
		} catch (Exception ex) {
			System.err.println("Last request: " + ModbusUtil.toHex(lastRequest));
			System.err.println(ex.getMessage());
			throw new ModbusIOException("I/O exception - failed to read");
		}
	  
		return response;
	}
	
  /**
   * Prepares the input and output streams of this
   * <tt>ModbusRTUTransport</tt> instance.
   *
   * @param in the input stream to be read from.
   * @param out the output stream to write to.
   * @throws IOException if an I\O error occurs.
   */
	public void prepareStreams(InputStream in, OutputStream out) throws IOException {	  
	  m_InputStream 	= in;   //new RTUInputStream(in);
	  m_OutputStream	= out;
	  m_ByteOut 		= new BytesOutputStream(Const_Modbus.MAX_MESSAGE_LENGTH);	  
	  m_ByteIn 			= new BytesInputStream(Const_Modbus.MAX_MESSAGE_LENGTH);
	  m_ByteInOut 		= new BytesOutputStream(Const_Modbus.MAX_MESSAGE_LENGTH);
	  m_InBuffer 		= new byte[Const_Modbus.MAX_MESSAGE_LENGTH];
/*
	  m_ByteOut = new BytesOutputStream(Modbus.MAX_MESSAGE_LENGTH);
	  
	  
*/
	} //prepareStreams

	public void close() throws IOException {
//		m_InputStream.close();
//		m_OutputStream.close();
	}//close

	@SuppressWarnings("unused")
	private void getResponse(int fn, BytesOutputStream out) throws IOException {
/*
		int bc = -1, bc2 = -1, bcw = -1;
		int inpBytes = 0;
		byte inpBuf[] = new byte[256];
*/
		switch (fn) {
			//Operaciones de Lectura
			case 0x01:
			case 0x02:
			case 0x03:
			case 0x04:
			case 0x0C:
			case 0x11:  // report slave ID version and run/stop state
			case 0x14:  // read log entry (60000 memory reference)
			case 0x15:  // write log entry (60000 memory reference)
			case 0x17:
/*
				// read the byte count;
				bc = m_InputStream.read();
				out.write(bc);
				// now get the specified number of bytes and the 2 CRC bytes
				setReceiveThreshold(bc+2);
        
				int data;
				while ( ( data = m_InputStream.read()) > -1 ){
					inpBuf[inpBytes++] = (byte) data;
					if ( (data == '\n') || (inpBytes >= (bc+2)) ) {
						break;
					}
				}
			
				//inpBytes = m_InputStream.read(inpBuf, 0, bc+2);
				out.write(inpBuf, 0, inpBytes);
				m_CommPort.disableReceiveThreshold();
       
				if (inpBytes != bc+2) {
					System.out.println("Time ejecution : " + (System.currentTimeMillis()-start_time));  
					System.out.println("Error: looking for " + (bc+2) +
							" bytes, received " + inpBytes);
				}
*/
				break;
			
			//Operaciones de Escritura
			case 0x05:
			case 0x06:
			case 0x0B:
			case 0x0F:
			case 0x10:
/*
				// read status: only the CRC remains after address and function code
				setReceiveThreshold(6);
				inpBytes = m_InputStream.read(inpBuf, 0, 6);
				out.write(inpBuf, 0, inpBytes);
				m_CommPort.disableReceiveThreshold();
*/
				break;
				
			case 0x07:
			case 0x08:
/*
				// read status: only the CRC remains after address and function code
				setReceiveThreshold(3);
				inpBytes = m_InputStream.read(inpBuf, 0, 3);
				out.write(inpBuf, 0, inpBytes);
				m_CommPort.disableReceiveThreshold();
*/
				break;
     
			case 0x16:
/*		  			// eight bytes in addition to the address and function codes
				setReceiveThreshold(8);
				inpBytes = m_InputStream.read(inpBuf, 0, 8);
				out.write(inpBuf, 0, inpBytes);
				m_CommPort.disableReceiveThreshold();
*/
				break;
      
			case 0x18:
/*
				// read the byte count word
				bc = m_InputStream.read();
				out.write(bc);
				bc2 = m_InputStream.read();
				out.write(bc2);
				bcw = ModbusUtil.makeWord(bc, bc2);
				// now get the specified number of bytes and the 2 CRC bytes
				setReceiveThreshold(bcw+2);
				inpBytes = m_InputStream.read(inpBuf, 0, bcw + 2);
				out.write(inpBuf, 0, inpBytes);
				m_CommPort.disableReceiveThreshold();
*/
				break;
			
			case 0x86:
			case 0x83:
/*
				// read the byte of code error
				bc = m_InputStream.read();
				out.write(bc);            
				// now get 2 CRC bytes
				setReceiveThreshold(2);
				inpBytes = m_InputStream.read(inpBuf, 0, 2);
				out.write(inpBuf, 0, inpBytes);
				m_CommPort.disableReceiveThreshold();
*/
				break;
		}
	}//getResponse  
} //ModbusRTUTransport

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

package ModBus.msg;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import ModBus.Const_Modbus;

/**
 * Abstract class implementing a <tt>ModbusResponse</tt>.
 * This class provides specialised implementations with
 * the functionality they have in common.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public abstract class ModbusResponse
    extends ModbusMessageImpl {


  /**
   * Utility method to set the raw data of the message.
   * Should not be used except under rare circumstances.
   * <p>
   * @param msg the <tt>byte[]</tt> resembling the raw modbus
   *        response message.
   */
  protected void setMessage(byte[] msg) {
    try {
      readData(
          new DataInputStream(
              new ByteArrayInputStream(msg)
          )
      );
    } catch (IOException ex) {

    }
  }//setMessage

  /**
   * Factory method creating the required specialized <tt>ModbusResponse</tt>
   * instance.
   *
   * @param functionCode the function code of the response as <tt>int</tt>.
   * @return a ModbusResponse instance specific for the given function code.
   */
  public static ModbusResponse createModbusResponse(int functionCode) {
    ModbusResponse response = null;

    switch (functionCode) {
      case Const_Modbus.READ_MULTIPLE_REGISTERS:
        response = new ReadMultipleRegistersResponse();
        break;
      case Const_Modbus.READ_INPUT_DISCRETES:
        response = new ReadInputDiscretesResponse();
        break;
      case Const_Modbus.READ_INPUT_REGISTERS:
        response = new ReadInputRegistersResponse();
        break;
      case Const_Modbus.READ_COILS:
        response = new ReadCoilsResponse();
        break;
      case Const_Modbus.WRITE_MULTIPLE_REGISTERS:
//        response = new WriteMultipleRegistersResponse();
        break;
      case Const_Modbus.WRITE_SINGLE_REGISTER:
        response = new WriteSingleRegisterResponse();
        break;
      case Const_Modbus.WRITE_COIL:
        response = new WriteCoilResponse();
        break;
      case Const_Modbus.WRITE_MULTIPLE_COILS:
//        response = new WriteMultipleCoilsResponse();
        break;
      default:
        response = new ExceptionResponse();
        break;
    }
    return response;
  }//createModbusResponse

}//class ModbusResponse

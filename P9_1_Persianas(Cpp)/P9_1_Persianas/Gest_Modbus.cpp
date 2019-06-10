/*
 * Gest_Modbus.cpp
 *
 *  Created on: 16/05/2014
 *      Author: jctejero
 */

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	<Arduino.h>
#include	"Gest_Modbus.h"
#include 	"utils_domoBoard.h"
#include	"Modbus_Domoboard.h"
#include	"app_Configurations.h"
#include 	"EEPROM.h"

/****************************************************************************/
/***        Variables Locales                                             ***/
/****************************************************************************/
/* First step MBS: create an instance */
ModbusSlave mbs;

uint16_t	Cregs[MB_O_COILS];		//Registros para "Dicrete Output Coils"
uint16_t	Dregs[MB_I_CONTATCS];	//Registros para "Dicrete Input Contacts"
uint16_t	Aregs[MB_A_REGS];		//Registros para "Analog Output Holding Registers"
uint16_t	Eregs[MB_E_REGS];		//Registros para "Analog Input Registers"

/****************************************************************************/
/***                 Definición de funciones                              ***/
/****************************************************************************/
void writesingleregister(unsigned int addrReg);
void writecoil(unsigned int addrReg);

/****************************************************************************/
/***                 Functions                                            ***/
/****************************************************************************/
void Init_RTU_Modbus()
{
	/* configure modbus communication
	 * 19200 bps, 8E1, two-device network */
	/* Second step MBS: configure */
	/* the Modbus slave configuration parameters */
	const unsigned char 	SLAVE 	= ADDR_SLAVE;		//Address SLAVE
	const long 				BAUD 	= SERIAL_BPS;
	const char 				PARITY 	= SERIAL_PARITY;
	const char 				TXENPIN = 0; //EN_485;

	//Inicialmente configuramos 485 para recibir
	//digitalWrite(EN_485, LOW);

	//Para la conexión 485/ModBus usamos
	Serial485 = &Serial;

	//Configuramos bancos de registros ModBus (Analógicos/Digitales)
	A_Regs = Aregs;
	C_Regs = Cregs;
	D_Regs = Dregs;
	E_Regs = Eregs;
	N_ARegs = MB_A_REGS;
	N_CRegs = MB_O_COILS;
	N_DRegs = MB_I_CONTATCS;
	N_ERegs = MB_E_REGS;


	mbs.configure(SLAVE,BAUD,PARITY,TXENPIN);

	mbDomoBoard = new MBDomoBoard(&domoboard);

	//Config_interruptor1();

	//Configura acción a realizar cuando se actualizan los registros (Discrete Output Coils)
	mbs.writecoil = writecoil;
	mbs.writesingleregister = writesingleregister;
}

/*
 *	Gestión evento cuando se escribe un "Single Discrete Output Register"
 */

void writecoil(unsigned int addrReg){
	switch(addrReg){
	case	MB_RELE1:
		//domoboard.setCoil(&domoboard.RELE1,Cregs[MB_RELE1]!=0x00);
		domoboard.setCoil(&domoboard.RELE1,C_Regs[MB_RELE1]!=0x00);
		break;
	case	MB_RELE2:
		domoboard.setCoil(&domoboard.RELE2,C_Regs[MB_RELE2]!=0x00);
		break;

	case	MB_ACTPIR:
		domoboard.PIR_MOV.sSensor.Activo = C_Regs[MB_ACTPIR]!=0x00;
		EEPROM.write(ADDR_ACTPIR, domoboard.PIR_MOV.sSensor.Activo);
		break;

	case	MB_ACTSRC:
		domoboard.sPHOTRES.sSensor.Activo = C_Regs[MB_ACTSRC]!=0x00;
		EEPROM.write(ADDR_ACTSRC, domoboard.PIR_MOV.sSensor.Activo);
		break;
	}
}

void writesingleregister(unsigned int addrReg){
	//char msg[50];

	switch(addrReg){
	case MB_SELPRACT:
		//Save new configuration
		EEPROM.write(ADDR_SELPRACT, A_Regs[ADDR_SELPRACT]&0xFF);
		//Go To Selecction Configuration
		SelectionConfiguration(A_Regs[ADDR_SELPRACT]&0xFF);
		break;

	case MB_TMP_PIR:

		//Almacenar Tiempo de activación Sensor PIR (Big Endian)
		EEPROM.write(ADDR_TIEMPO_PIR_1, (A_Regs[MB_TMP_PIR]>>8)&0xFF);
		EEPROM.write(ADDR_TIEMPO_PIR_2, (A_Regs[MB_TMP_PIR]&0xFF));
		break;

	case MB_SRC_HL:

		//Almacenar nivel superior de activación SRC (Big Endian)
		EEPROM.write(ADDR_SRC_HL_1, (A_Regs[MB_SRC_HL]>>8)&0xFF);
		EEPROM.write(ADDR_SRC_HL_2, (A_Regs[MB_SRC_HL]&0xFF));
		break;

	case MB_SRC_LL:

		//Almacenar nivel inferior de activación SRC (Big Endian)
		EEPROM.write(ADDR_SRC_LL_1, (A_Regs[MB_SRC_LL]>>8)&0xFF);
		EEPROM.write(ADDR_SRC_LL_2, (A_Regs[MB_SRC_LL]&0xFF));
		break;

	case MB_STE_PER:
		//A_Regs[MB_STE_PER] = state;

		domoboard.SetPersiana((tsStaPer)(A_Regs[MB_STE_PER]));
		break;
	}
}


void RTU_ModBus()
{
	unsigned long wdog = 0;         /* watchdog */

	if(mbs.update()){
		wdog = millis();

		if ((millis() - wdog) > 5000)  {      // no comms in 5 sec
			//regs[MB_CTRL] = 0;	// turn off led
		}
	}
}

void load_Config(void){
	//Leemos configuración Actual "Selección Práctica"
	int val = EEPROM.read(ADDR_SELPRACT);
	//mbs.writeSingleRegister(MB_SELPRACT, val, Aregs);

	SelectionConfiguration(0x91);

	/*
	 * Config PIR
	 */
	val = EEPROM.read(ADDR_ACTPIR);
	mbs.writeRegCoil(MB_ACTPIR, val, Cregs );

	//Leer Tiempo activación PIR
	A_Regs[MB_TMP_PIR] = ((EEPROM.read(ADDR_TIEMPO_PIR_1)&0xFF) << 8) + (EEPROM.read(ADDR_TIEMPO_PIR_2)&0xFF) ;
	//val = (EEPROM.read(ADDR_TIEMPO_PIR_1) << 8) + (EEPROM.read(ADDR_TIEMPO_PIR_1)&0xFF)

	/*
	 * Configuration SRC
	 */
	val = EEPROM.read(ADDR_ACTSRC);
	mbs.writeRegCoil(MB_ACTSRC, val, Cregs );

	A_Regs[MB_SRC_HL] = (EEPROM.read(ADDR_SRC_HL_1) << 8) + (EEPROM.read(ADDR_SRC_HL_2)&0xFF) ;
	A_Regs[MB_SRC_LL] = (EEPROM.read(ADDR_SRC_LL_1) << 8) + (EEPROM.read(ADDR_SRC_LL_2)&0xFF) ;
}


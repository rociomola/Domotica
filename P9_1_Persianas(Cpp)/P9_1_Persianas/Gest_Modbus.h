/*
 * Gest_Modbus.h
 *
 *  Created on: 16/05/2014
 *      Author: jctejero
 */

#ifndef GEST_MODBUS_H_
#define GEST_MODBUS_H_

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include 	"ModbusSlave.h"
#include 	"domoBoard.h"
#include    "Modbus_Domoboard.h"

/****************************************************************************/
/***        Macro Definitions                                             ***/
/****************************************************************************/
//Configuraci�n de la conexi�n MODBUS
#define	ADDR_SLAVE		1		//Direcci�n Dispositivo Esclavo
#define	SERIAL_BPS		19200	//Velocidad Comunicaci�n serie
#define	SERIAL_PARITY	'n'		//Paridad comunicaci�n serie
#define	TX_EN_PIN		0		//Pin usado para la transmisi�n RS485; 0 No usado


/****************************************************************************/
/***        DEFINICI�N DE FUNCIONES    **************************************/
/****************************************************************************/
void Init_RTU_Modbus();
void RTU_ModBus();
void load_Config(void);

extern ModbusSlave mbs;
extern uint16_t	Cregs[MB_O_COILS];
extern uint16_t	Aregs[MB_A_REGS];


#endif /* GEST_MODBUS_H_ */

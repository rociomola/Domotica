/*
 * Modbus_Domoboard.h
 *
 *  Created on: 29/3/2016
 *      Author: JuanCarlos
 */

#ifndef DOMOBOARD_MODBUS_DOMOBOARD_H_
#define DOMOBOARD_MODBUS_DOMOBOARD_H_

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	"Arduino.h"
#include 	"domoBoard.h"

/****************************************************************************/
/***        Macro Definitions                                             ***/
/****************************************************************************/
// La cofiguración de domo board usando ModBus, será almacenada en la EEPROM.
// Las direccioes de los valores a almacenar se indican acontinuación

#define	ADDR_SELPRACT		0		//Comando de Configuración actual de DomoBoad
#define	ADDR_ACTPIR			1		//Activa/Desactiva PIR
#define ADDR_TIEMPO_PIR_1	2		//Tiempo PIR Activo 1
#define ADDR_TIEMPO_PIR_2	3		//Tiempo PIR Activo 2
#define ADDR_ACTSRC			4		//Activa/Desactiva SRC
#define	ADDR_SRC_HL_1		5		//Nivel Alto Activación SRC
#define	ADDR_SRC_HL_2		6		//Nivel Alto Activación SRC
#define	ADDR_SRC_LL_1		7		//Nivel Bajo Activación SRC
#define	ADDR_SRC_LL_2		8		//Nivel Bajo Activación SRC

/****************************************************************************/
/***        Type Definitions                                              ***/
/****************************************************************************/
/* slave registers */

//Discrete Output Coils
enum {
	MB_RELE1,
	MB_RELE2,
	MB_ACTPIR,					//Salida virtual para Activar/Desactivar PIR
	MB_ACTSRC,					//Salida virtual para Activar/Desactivar SRC --> PhotoResistor
	MB_O_COILS
};

//Discrete Input Contacts
enum {
	MB_BOTON1,
	MB_BOTON2,
	MB_BTNOPT,
	MB_PIR,
	MB_I_CONTATCS
};

//Registros ModBus para variables analógicas "Analog Output Holding Register"
enum{
	MB_SELPRACT,		// Registro ModBus Para seleccionar la configuración del sistema
	MB_TMP_PIR,			// Registro para controlar el tiempo activo del sensor PIR (Segundos)
	MB_SRC_HL,			// Registro para controlar el nivel superior de activación SRC
	MB_SRC_LL,			// Registro para controlar el nivel Inferior de activación SRC
	MB_A_REGS,			// Total de registros Analógicos
	MB_STE_PER			// Estado Persiana
};

//Registros de Entrada
enum{
	MB_POT1,			// POTENCIÓMETRO 1
	MB_POT2,			// POTENCIÓMETRO 2
	MB_PHOTORES,		// PhotoResistor
	MB_TEMP,			// Sensor de Temperatura
	MB_PHOTOTTOR,		// Photo Transistor
	MB_E_REGS			// Total de registros de Entrada
};

typedef struct{
	tsSalida	*Salida;		//Salida Asociada
	uint8_t		nMBReg;			//Posición registro para su uso en comunicaciones: Ej. ModBus
	uint16_t 	*bkMBRegs;		//Banco de registros asociado, para su uso en comunicaciones: Ej. ModBus
}tsActuator;

typedef struct{
	tsSensor		*Sensor;		//Sensor asociado
	uint8_t			nMBReg;			//Posición registro para su uso en comunicaciones: Ej. ModBus
	uint16_t 		*bkMBRegs;		//Banco de registros asociado, para su uso en comunicaciones: Ej. ModBus
}tsMBSensor;

//Estructura para controlar el Acciones temporizadas con puntero de control para tiempo activo.
typedef struct{
	uint32_t 	time_lastAct;
	uint16_t	Cte_Norma;			//Cte. usada normalizar el tiempo activo
	uint16_t	*time_activo;
	bool		estado;
}CTRL_IntTemp;

typedef struct
{
	tsMBSensor				*MBsensor;
	QueueList<tsMBSensor>	Sensors;
	QueueList<tsActuator>	Actuadores;
	CTRL_IntTemp			*AccionTemporizada;	//Estructura para controlar acciones temporizadas
}tActionControl;

typedef void (*TAction)(tActionControl *);

typedef struct
{
	TAction			action;			//Acción a iniciar al actualizar el estado del sensor
	tActionControl	actionControl;	//Variables necesarias para el desarrollo de la acción
}tsAction, *tsAction_ptr;

/****************************************************************************/
/***        Exported Class                                                ***/
/****************************************************************************/
class MBDomoBoard
{
private:
	tsMBSensor		BOTON1;
	tsMBSensor		BOTON2;
	tsMBSensor 		BTN_OPT;
	tsMBSensor		PIR_MB;

	//Sensores Analógicos
	tsMBSensor		POT1_MB;				// Potenciómetro 1
	tsMBSensor		POT2_MB;				// Potenciómetro 2
	tsMBSensor		PHOTRES_MB;				// Photoresistor
	tsMBSensor		TEMP_MB;				//Sensor Temperatura
	tsMBSensor		PHOTOTTOR_MB;			//Photo Transistor

	tsActuator		RELE1;
	tsActuator		RELE2;

	//Actuadores para gestionar la persiana
	//tsActuator		PERUP_MB;
	//tsActuator		PERDOWN_MB;


	static void		_Act_MBReg(tsAction *action);

public:

	MBDomoBoard(DomoBoard *domoBoard); 						//Constructor

	void			sensorApp(tsSensor *sensor);
	tsActuator		getRELE1(void);
	tsActuator		getRELE2(void);

	tsMBSensor		getMBSensor(tsSensor *sensor);

	tsAction		actBOTON1;
	tsAction		actBOTON2;
	tsAction		actBTNOPT;
	tsAction		actPIR;
	tsAction		actPOT1;
	tsAction		actPOT2;
	tsAction		actPHOTRES;
	tsAction		actTEMP;
	tsAction        actPHOTOTTOR;
	tsAction		actPERSIANA;

	tsAction*		getActionSensor(tsSensor *sensor);
};

extern MBDomoBoard	*mbDomoBoard;


#endif /* DOMOBOARD_MODBUS_DOMOBOARD_H_ */

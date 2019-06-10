/*
 * domoBoard.h
 *
 *  Created on: 09/03/2015
 *      Author: jctejero
 */

#ifndef DOMOBOARD_H_
#define DOMOBOARD_H_

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	"Arduino.h"
#include	"QueueList.h"

/****************************************************************************/
/***        Type Definitions                                              ***/
/****************************************************************************/
typedef void (*TNotifyEvent)(void *);

typedef enum
{
	S_DIGITAL,
	S_ANALOGICO
}teSensor;

typedef struct
{
	byte	pin;
	byte	estado;
}tsSalida;

/*
typedef struct
{
	tsSalida			*Salida;		//Salida Asociada al sensor
	int					MBReg_App;		//Registro que la aplicación usa para comunicaciones ModBus
}tsCoil;
*/

typedef struct tsSensor
{
	byte				pin;			//Pin asignado al sensor
	int	    			valor;			//Valor leido
	int					valor_Df;		//Valor Sensor por defecto
	bool				Activo;			//Activar/Desactivas sensor
	teSensor			eSensor;		//Tipos de sensor ANALÓGICO/DIGITAL
	String				name;
	byte				Aux;			//Byte usado para indicaciones especiales
}tsSensor;

typedef void (*TNotifySensorEvent)(tsSensor *);

typedef struct
{
	tsSensor			sSensor;
	TNotifySensorEvent	SensorApp;	//Evento para aplicación asociada
}ptsSensor;

typedef enum {
	PER_STOP,
	PER_DOWN,
	PER_UP,
	PER_STOP2
}tsStaPer; //Control persianas


/****************************************************************************/
/***        Macro Definitions                                             ***/
/****************************************************************************/
#define		PHOTRES					A0			// Photo resistor SRC
#define		TMP_SEN					A2			// Sensor de temperatura
#define		PHO_TTOR				A3			// Sensor de luminosidad (PhotoTransistor)
#define		POT1					A4			// Potenciómetro 1
#define		POT2					A5			// Potenciómetro 2

#define 	BUTTON_1   				3   	   	// Pulsador 1
#define 	BUTTON_2   				4        	// Pulsador 2
#define		PER_UPDOWN				5			// UPDOWN Persianas
#define		EN_485					6			// Bit to enable RS485 Conmunications
#define     PIR						7			// Sensor de Movimiento
#define 	RELE_1					8			// RELE 1
#define 	RELE_2					11			// RELE 1
#define		PER_ONOFF				12			// ONOFF Persianas

#define 	ENTRADA_OPTOCOPLADA    	13 	    	// Entrada Optocoplada

/****************************************************************************/
/***        Exported Class                                                ***/
/****************************************************************************/
class DomoBoard
{
private:


public:

	DomoBoard(); 						//Constructor

	ptsSensor		BOTON1;
	ptsSensor		BOTON2;
	ptsSensor 		BTN_OPT;
	ptsSensor		PIR_MOV;

	//Sensores analógicos
	ptsSensor		sPHOTRES;			// Photoresistor SRC
	ptsSensor		sTEMP;				//Sensor de temperatura
	ptsSensor		sPHOTTOR;			//Foto transistor
	ptsSensor		sPOT1;				// Potenciómetro 1
	ptsSensor		sPOT2;				// Potenciómetro 2

	//Salidas
	tsSalida		RELE1;
	tsSalida		RELE2;

	tsSalida		Rele_OnOffPer;
	tsSalida        Rele_UpDownPer;

	void 	leerAllSensor(void);
	void 	leerSensor(ptsSensor *Sensor);
	void    setCoil(tsSalida *Salida, bool val);
	void 	SetPersiana(tsStaPer staPer);
};

/****************************************************************************/
/***        Exported Variables                                            ***/
/****************************************************************************/
//extern uint16_t	Dregs[MB_D_REGS];
//extern uint16_t Cregs[MB_O_COILS];		//Registros para "Dicrete Output Coils"
//extern uint16_t	Aregs[MB_A_REGS];
extern DomoBoard domoboard;


#endif /* DOMOBOARD_H_ */

/*
 * utils_domoBoard.h
 *
 *  Created on: 10/03/2015
 *      Author: jctejero
 */

#ifndef UTILS_DOMOBOARD_H_
#define UTILS_DOMOBOARD_H_

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	"Arduino.h"
#include	"domoBoard.h"
#include	"Modbus_Domoboard.h"

/****************************************************************************/
/***        Macro Definitions                                             ***/
/****************************************************************************/
#define 	ON   				HIGH
#define     OFF					LOW
#define     DOWN				OFF
#define     UP					ON

//Macros para la base de tiempos
#define		BASETIEMPO_DB		100 				// uSeg Sec Base Tiempo DomoBoard
#define     MILISECOND			1000/BASETIEMPO_DB
#define		ONESECOND			1E6/BASETIEMPO_DB

/****************************************************************************/
/***        Type Definitions                                              ***/
/****************************************************************************/


/****************************************************************************/
/***        Exported Functions                                            ***/
/****************************************************************************/
void	interruptor_SRC(tActionControl	*acInt);
void	Interruptor(tActionControl	*acInt);
void 	Pulsador(tActionControl		*acInt);
void 	Conmutador(tActionControl	*acInt);
void	Interruptor_Temporizado(tActionControl	*acInt);
void    Interruptor_Temporizado_ptr(tActionControl	*acInt);
void 	Calc_Temperatura(tActionControl	*acInt);
void 	Persiana(tActionControl	*acInt);
void 	PerUp(tActionControl	*acInt);
void	PerDown(tActionControl	*acInt);
void 	Acciones_Temporizadas(void);

/****************************************************************************/
/***        Exported Variables                                            ***/
/****************************************************************************/
/****************************************************************************/
/***        Variables Locales                                             ***/
/****************************************************************************/
extern QueueList<tsActuator>		ActuadoresConmutador;
extern CTRL_IntTemp					ctrl_IntTemp_1;
extern CTRL_IntTemp					ctrl_IntTemp_2;
extern CTRL_IntTemp					ctrl_IntTemp_3;
extern QueueList<tsAction_ptr>		AccionesTemporizadas_Ptr;
extern uint16_t						Tmp_Int_Temp;
//extern tActionControl	acInterruptor;
//extern tsAction			Interruptor1;


#endif /* UTILS_DOMOBOARD_H_ */

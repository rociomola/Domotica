/*
 * Modbus_Domoboard.cpp
 *
 *  Created on: 4/4/2016
 *      Author: JuanCarlos
 */

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	"Arduino.h"
#include	"Modbus_Domoboard.h"
#include	"domoBoard.h"
#include	"ModbusSlave.h"
#include	"utils_domoBoard.h"

MBDomoBoard		*mbDomoBoard;

void	Ext_sensorApp(tsSensor *sensor);

/*
 * Constructor de la clase
 */
MBDomoBoard::MBDomoBoard(DomoBoard *domoBoard){

	//*****  Inicialización de cada uno de los sensores  ****

	//Inicializamos el registros ModBus Pulsador 1

	//Emparejamos Sensor DomoBoard, con el sensor (COIL) ModBus
	BOTON1.Sensor  	=	&domoBoard->BOTON1.sSensor;
	//Indicamos banco de registros utilizado por el sensor
	BOTON1.bkMBRegs	=	D_Regs;
	//Indicamos registro utilizado por el sensor
	BOTON1.nMBReg	=   MB_BOTON1;
	//Iniciamos el registro ModBus con el valor por defecto del sensor

	//Indicamos aplicación a ejecutar cuando el sensor modifica su valor
	domoBoard->BOTON1.SensorApp = Ext_sensorApp;

	//Inicializamos el registros ModBus Pulsador 2
	BOTON2.Sensor  	=   &domoBoard->BOTON2.sSensor;
	BOTON2.bkMBRegs	=	D_Regs;
	BOTON2.nMBReg	=   MB_BOTON2;
	domoBoard->BOTON2.SensorApp = Ext_sensorApp;

	//Inicializamos el registros ModBus Pulsador Optocoplado
	BTN_OPT.Sensor		=   &domoBoard->BTN_OPT.sSensor;
	BTN_OPT.bkMBRegs	=	D_Regs;
	BTN_OPT.nMBReg		=   MB_BTNOPT;
	domoBoard->BTN_OPT.SensorApp = Ext_sensorApp;

	//Inicializamos el registros ModBus Sensor PIR
	PIR_MB.Sensor		=   &domoBoard->PIR_MOV.sSensor;
	PIR_MB.bkMBRegs		=	D_Regs;
	PIR_MB.nMBReg		=   MB_PIR;
	D_Regs[MB_PIR]		= 	domoBoard->PIR_MOV.sSensor.valor_Df;
	domoBoard->PIR_MOV.SensorApp = Ext_sensorApp;

	/*
	 * Sensores analógicos
	 */

	POT1_MB.Sensor					=   &domoBoard->sPOT1.sSensor;
	POT1_MB.bkMBRegs				=	E_Regs;
	POT1_MB.nMBReg					=	MB_POT1;
	domoBoard->sPOT1.SensorApp 		= 	Ext_sensorApp;

	POT2_MB.Sensor					=   &domoBoard->sPOT2.sSensor;
	POT2_MB.bkMBRegs				=	E_Regs;
	POT2_MB.nMBReg					=	MB_POT2;
	domoBoard->sPOT2.SensorApp 		= 	Ext_sensorApp;

	PHOTRES_MB.Sensor				=   &domoBoard->sPHOTRES.sSensor;
	PHOTRES_MB.bkMBRegs				=	E_Regs;
	PHOTRES_MB.nMBReg				=	MB_PHOTORES;
	domoBoard->sPHOTRES.SensorApp 	= 	Ext_sensorApp;

	TEMP_MB.Sensor					=   &domoBoard->sTEMP.sSensor;
	TEMP_MB.bkMBRegs				=	E_Regs;
	TEMP_MB.nMBReg					=	MB_TEMP;
	domoBoard->sTEMP.SensorApp 		= 	Ext_sensorApp;

	PHOTOTTOR_MB.Sensor				=   &domoBoard->sPHOTTOR.sSensor;
	PHOTOTTOR_MB.bkMBRegs			=	E_Regs;
	PHOTOTTOR_MB.nMBReg				=	MB_PHOTOTTOR;
	domoBoard->sPHOTTOR.SensorApp 	= 	Ext_sensorApp;

	/*
	 * Salidas
	 */

	RELE1.Salida		=	&domoBoard->RELE1;
	RELE1.bkMBRegs		=   C_Regs;
	RELE1.nMBReg    	=	MB_RELE1;

	RELE2.Salida		=	&domoBoard->RELE2;
	RELE2.bkMBRegs		=   C_Regs;
	RELE2.nMBReg    	=	MB_RELE2;

	//PERUP_MB.bkMBRegs	=	C_Regs;
	//PERUP_MB.nMBReg		=	MB_PERUP;

	//PERDOWN_MB.bkMBRegs	=	C_Regs;
	//PERDOWN_MB.nMBReg	=	MB_PERDOWN;

	//Asigna sensor ModBus a Acción
	actBOTON1.action					= 	NULL;
	actBOTON1.actionControl.MBsensor 	=	&BOTON1;
	actBOTON2.action					= 	NULL;
	actBOTON2.actionControl.MBsensor 	=	&BOTON2;
	actBTNOPT.action					= 	NULL;
	actBTNOPT.actionControl.MBsensor 	=  	&BTN_OPT;
	actPIR.action						= 	NULL;
	actPIR.actionControl.MBsensor 		=	&PIR_MB;
	actPOT1.action						= 	NULL;
	actPOT1.actionControl.MBsensor      =   &POT1_MB;
	actPOT2.action						= 	NULL;
	actPOT2.actionControl.MBsensor      =   &POT2_MB;
	actPHOTRES.action					= 	NULL;
	actPHOTRES.actionControl.MBsensor	=   &PHOTRES_MB;
	actTEMP.action						= 	NULL;
	actTEMP.actionControl.MBsensor		=   &TEMP_MB;
	actPHOTOTTOR.action					= 	NULL;
	actPHOTOTTOR.actionControl.MBsensor	=   &PHOTOTTOR_MB;
}

/*
 * Acción a realizar cuando modificamos el contenido de un registro ModBus
 */
void	MBDomoBoard::_Act_MBReg(tsAction *action){

	if(action != NULL){
		//Comprobamos que el sensor ModBus Asociado a la acción Tiene el banco de registro asignado
		if(action->actionControl.MBsensor->bkMBRegs != NULL){
			//Actualiza Registro ModBus
			action->actionControl.MBsensor->bkMBRegs[action->actionControl.MBsensor->nMBReg] = action->actionControl.MBsensor->Sensor->valor;
		}

		//En su caso, realiza la acción programada
		if(action->action != NULL){
			//Realizamos la acción asociada
			action->action(&action->actionControl);
		}
	}
}

tsActuator	MBDomoBoard::getRELE1(void){
	return RELE1;
}

tsActuator	MBDomoBoard::getRELE2(void){
	return RELE2;
}

tsMBSensor	MBDomoBoard::getMBSensor(tsSensor *sensor){
	tsMBSensor mbSensor;

	if(BOTON1.Sensor == sensor){
		mbSensor = BOTON1;
	}
	else if(BOTON2.Sensor == sensor){
		mbSensor = BOTON2;
	}

	return mbSensor;
}

/*
tsActuator	MBDomoBoard::getPERDOWN_MB(void){
	return  PERDOWN_MB;
}

tsActuator	MBDomoBoard::getPERUP_MB(void){
	return	PERUP_MB;
}
*/

/*
 * Busca la acción relacionada con un sensor
 */
tsAction*	MBDomoBoard::getActionSensor(tsSensor *sensor){

	tsAction* action = NULL;

	if(BOTON1.Sensor == sensor){
		action = &actBOTON1;
	}
	else if(BOTON2.Sensor == sensor){
		action = &actBOTON2;
	}
	else if(BTN_OPT.Sensor == sensor){
		action = &actBTNOPT;
	}
	else if(PIR_MB.Sensor == sensor){
		action = &actPIR;
	}
	else if(POT1_MB.Sensor == sensor){
		action = &actPOT1;
	}
	else if(POT2_MB.Sensor == sensor){
		action = &actPOT2;
	}
	else if(PHOTRES_MB.Sensor == sensor){
		action = &actPHOTRES;
	}
	else if(TEMP_MB.Sensor == sensor){
		Calc_Temperatura(&actTEMP.actionControl);
		action = NULL;
		//action = &actTEMP;
	}
	else if(PHOTOTTOR_MB.Sensor == sensor){
		action = &actPHOTOTTOR;
	}

	return action;
}

/*
 * Aplicación realizada por el sensor cuando modifica su valor
 */
void		MBDomoBoard::sensorApp(tsSensor *sensor){

	_Act_MBReg(getActionSensor(sensor));

}

void	Ext_sensorApp(tsSensor *sensor){

	//Serial.println(sensor->name);

	mbDomoBoard->sensorApp(sensor);
}

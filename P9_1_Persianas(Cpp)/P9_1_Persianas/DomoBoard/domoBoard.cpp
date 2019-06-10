/*
 * domoBoard.cpp
 *
 *  Created on: 09/03/2015
 *      Author: jctejero
 */

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	"Arduino.h"
#include 	"domoBoard.h"
#include	"utils_domoBoard.h"

/****************************************************************************/
/***        Global Variables                                               ***/
/****************************************************************************/
DomoBoard 	domoboard;

// Constructors ////////////////////////////////////////////////////////////////

DomoBoard::DomoBoard()
{
	//Definimos pin's DomoBoard.
	pinMode(BUTTON_1, INPUT);
	pinMode(BUTTON_2, INPUT);
	pinMode(ENTRADA_OPTOCOPLADA, INPUT);      	//Pin Entrada Optocoplada

	pinMode(RELE_1, OUTPUT);
	pinMode(RELE_2, OUTPUT);

	//Configura pins control persianas
	pinMode(PER_UPDOWN, OUTPUT);
	pinMode(PER_ONOFF,  OUTPUT);

	//Inicialización del pulsador 1
	BOTON1.sSensor.pin 			= BUTTON_1;
	BOTON1.sSensor.Activo 		= true;
	BOTON1.sSensor.eSensor  	= S_DIGITAL;
	BOTON1.sSensor.name			= "BOTÓN 1";
	BOTON1.sSensor.valor_Df 	= HIGH;

	//Inicialización del pulsador 2
	BOTON2.sSensor.pin 			= BUTTON_2;
	BOTON2.sSensor.Activo 		= true;
	BOTON2.sSensor.eSensor  	= S_DIGITAL;
	BOTON2.sSensor.name			= "BOTÓN 2";
	BOTON2.sSensor.valor_Df 	= HIGH;

	//Inicialización del pulsador Optocoplado
	BTN_OPT.sSensor.pin 		= ENTRADA_OPTOCOPLADA;
	BTN_OPT.sSensor.Activo 		= true;
	BTN_OPT.sSensor.eSensor 	= S_DIGITAL;
	BTN_OPT.sSensor.name		= "Pulsador Optocoplado";
	BTN_OPT.sSensor.valor_Df	= LOW;

	//Inicialización del sensor de movimiento
	PIR_MOV.sSensor.pin 		= PIR;
	//PIR_MOV.sSensor.valor 		= LOW;
	PIR_MOV.sSensor.Activo 		= true;
	PIR_MOV.sSensor.eSensor 	= S_DIGITAL;
	PIR_MOV.sSensor.name		= "PIR (Sensor de Movimiento)";
	PIR_MOV.sSensor.valor_Df	= LOW;  //HIGH; para el sensor original

	//Inicialización Sensores Analógicos

	sPOT1.sSensor.pin			= POT1;
	sPOT1.sSensor.Activo 		= true;
	sPOT1.sSensor.eSensor 		= S_ANALOGICO;
	sPOT1.sSensor.name			= "POTENCIÓMETRO 1";

	sPOT2.sSensor.pin			= POT2;
	sPOT2.sSensor.Activo 		= true;
	sPOT2.sSensor.eSensor 		= S_ANALOGICO;
	sPOT2.sSensor.name			= "POTENCIÓMETRO 2";

	sPHOTRES.sSensor.pin		= PHOTRES;
	sPHOTRES.sSensor.Activo 	= true;
	sPHOTRES.sSensor.eSensor 	= S_ANALOGICO;
	sPHOTRES.sSensor.name		= "PHOTORESISTOR";

	sTEMP.sSensor.pin 			= TMP_SEN;
	sTEMP.sSensor.Activo 		= true;
	sTEMP.sSensor.eSensor 		= S_ANALOGICO;
	sTEMP.sSensor.name			= "SENSOR TEMPERATURA (TMP36)";

	sPHOTTOR.sSensor.pin		= PHO_TTOR;
	sPHOTTOR.sSensor.Activo 	= true;
	sPHOTTOR.sSensor.eSensor 	= S_ANALOGICO;
	sPHOTTOR.sSensor.name		= "PHOTO TRANSISTOR (Op800WSL)";

	//Inicialización del RELE 1
	RELE1.pin 					= RELE_1;
	RELE1.estado 				= LOW;

	//Inicialización del RELE 2
	RELE2.pin 					= RELE_2;
	RELE2.estado 				= LOW;

	Rele_OnOffPer.pin 			= PER_ONOFF;
	Rele_OnOffPer.estado 		= OFF;

	Rele_UpDownPer.pin 			= PER_UPDOWN;
	Rele_UpDownPer.estado 		= DOWN;
}

void 	DomoBoard::SetPersiana(tsStaPer staPer)
{
	switch(staPer){
	case PER_STOP:
	case PER_STOP2:
		//digitalWrite(per_OnOff, Persiana_OFF);
		setCoil(&Rele_OnOffPer, OFF);
		break;

	case PER_DOWN:
		setCoil(&Rele_UpDownPer, OFF);
		setCoil(&Rele_OnOffPer, ON);
		break;

	case PER_UP:
		setCoil(&Rele_UpDownPer, ON);
		setCoil(&Rele_OnOffPer, ON);
		break;
	}
}

void DomoBoard::leerSensor(ptsSensor *Sensor){
	int valor = 0;

	if(Sensor->sSensor.Activo){
		switch (Sensor->sSensor.eSensor)
		{
		case S_DIGITAL:
			valor = digitalRead((uint8_t)Sensor->sSensor.pin);
			break;

		case S_ANALOGICO:
			valor = analogRead((uint8_t)Sensor->sSensor.pin);
			break;
		}

		if(Sensor->sSensor.valor != valor)
		{
			Sensor->sSensor.valor = valor;

			//Si hay un cambio en el estado del sensor llamamos a la aplicación asociada
			if(Sensor->SensorApp != NULL)
			{
				Sensor->SensorApp(&(Sensor->sSensor));
			}
		}
	}
}

void DomoBoard::leerAllSensor(void){
	leerSensor(&BOTON1);
	leerSensor(&BOTON2);
	leerSensor(&BTN_OPT);
	leerSensor(&PIR_MOV);
}

void    DomoBoard::setCoil(tsSalida *Salida, bool val){
	Salida->estado = val;
	digitalWrite(Salida->pin, Salida->estado);
}


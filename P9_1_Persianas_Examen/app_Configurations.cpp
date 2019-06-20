/*
 * app_Configurations.cpp
 *
 *  Created on: 4/4/2016
 *      Author: JuanCarlos
 */

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include "app_Configurations.h"
#include "utils_domoBoard.h"
#include "Gest_Modbus.h"

/*
 * Configuración de las distintas acciones relacionadas con la práctica, para
 * cada uno de los sensores.
 */

void Config_interruptor1(void){
	mbDomoBoard->actBOTON1.action = Interruptor;
	mbDomoBoard->actBOTON1.actionControl.Actuadores.push(mbDomoBoard->getRELE1());
	mbDomoBoard->actBOTON1.actionControl.Actuadores.push(mbDomoBoard->getRELE2());

	mbDomoBoard->actBOTON2.action = Interruptor;
	mbDomoBoard->actBOTON2.actionControl.Actuadores.push(mbDomoBoard->getRELE2());

	mbDomoBoard->actBTNOPT.action = Interruptor;
	mbDomoBoard->actBTNOPT.actionControl.Actuadores.push(mbDomoBoard->getRELE1());

	mbDomoBoard->actPHOTRES.action	= NULL;
	mbDomoBoard->actPIR.action 		= NULL;
}

/*
 * Configuraciones para la práctica 6
 */

//*** Configuración Práctica 6 Apartado 1
void Config_P6_Apartado1(void){
	AccionesTemporizadas_Ptr.clear();

	mbDomoBoard->actBOTON1.action = Pulsador;
	mbDomoBoard->actBOTON1.actionControl.AccionTemporizada = NULL;
	mbDomoBoard->actBOTON1.actionControl.Actuadores.clear();
	mbDomoBoard->actBOTON1.actionControl.Actuadores.push(mbDomoBoard->getRELE1());

	mbDomoBoard->actBOTON2.action = Pulsador;
	mbDomoBoard->actBOTON2.actionControl.AccionTemporizada = NULL;
	mbDomoBoard->actBOTON2.actionControl.Actuadores.clear();
	mbDomoBoard->actBOTON2.actionControl.Actuadores.push(mbDomoBoard->getRELE2());

	mbDomoBoard->actBTNOPT.action = Pulsador;
	mbDomoBoard->actBTNOPT.actionControl.AccionTemporizada = NULL;
	mbDomoBoard->actBTNOPT.actionControl.Actuadores.clear();
	mbDomoBoard->actBTNOPT.actionControl.Actuadores.push(mbDomoBoard->getRELE1());
	mbDomoBoard->actBTNOPT.actionControl.Actuadores.push(mbDomoBoard->getRELE2());

	mbDomoBoard->actPHOTRES.action	= NULL;
	mbDomoBoard->actPIR.action 		= NULL;
}

//*** Configuración Práctica 6 Apartado 2
void Config_P6_Apartado2(void){
	AccionesTemporizadas_Ptr.clear();

	mbDomoBoard->actBOTON1.action = Interruptor;
	mbDomoBoard->actBOTON1.actionControl.Actuadores.clear();
	mbDomoBoard->actBOTON1.actionControl.Actuadores.push(mbDomoBoard->getRELE1());

	mbDomoBoard->actBOTON2.action = Interruptor;
	mbDomoBoard->actBOTON2.actionControl.Actuadores.clear();
	mbDomoBoard->actBOTON2.actionControl.Actuadores.push(mbDomoBoard->getRELE2());

	mbDomoBoard->actBTNOPT.action = Interruptor;
	mbDomoBoard->actBTNOPT.actionControl.Actuadores.clear();
	mbDomoBoard->actBTNOPT.actionControl.Actuadores.push(mbDomoBoard->getRELE1());
	mbDomoBoard->actBTNOPT.actionControl.Actuadores.push(mbDomoBoard->getRELE2());

	mbDomoBoard->actPHOTRES.action	= NULL;
	mbDomoBoard->actPIR.action 		= NULL;
}

//*** Configuración Práctica 6 Apartado 2
void Config_P6_Apartado3(void){
	AccionesTemporizadas_Ptr.clear();

	mbDomoBoard->actBOTON1.action = Conmutador;

	mbDomoBoard->actBOTON2.action = Conmutador;

	mbDomoBoard->actBTNOPT.action = Conmutador;

	ActuadoresConmutador.clear();
	ActuadoresConmutador.push(mbDomoBoard->getRELE1());
	ActuadoresConmutador.push(mbDomoBoard->getRELE2());

	mbDomoBoard->actPHOTRES.action	= NULL;
	mbDomoBoard->actPIR.action 		= NULL;
}

void Config_P7_InterruptorTemporizado(void){

	mbDomoBoard->actBOTON1.action = Interruptor_Temporizado;
	mbDomoBoard->actBOTON1.actionControl.Actuadores.clear();

	ctrl_IntTemp_1.Cte_Norma   	= 	1;					   //valor para indicar/Pasar tiempo activo de milisegundos a segundos
	Tmp_Int_Temp				=	3000;
	ctrl_IntTemp_1.time_activo 	= 	&Tmp_Int_Temp;

	mbDomoBoard->actBOTON1.actionControl.AccionTemporizada = &ctrl_IntTemp_1;
	mbDomoBoard->actBOTON1.actionControl.Actuadores.push(mbDomoBoard->getRELE1());

	mbDomoBoard->actBOTON2.action = Interruptor_Temporizado;
	mbDomoBoard->actBOTON2.actionControl.Actuadores.clear();

	ctrl_IntTemp_2.Cte_Norma   	= 	1;					   //valor para indicar/Pasar tiempo activo de milisegundos a segundos
	Tmp_Int_Temp				=	3000;
	ctrl_IntTemp_2.time_activo 	= 	&Tmp_Int_Temp;

	mbDomoBoard->actBOTON2.actionControl.AccionTemporizada = &ctrl_IntTemp_2;
	mbDomoBoard->actBOTON2.actionControl.Actuadores.push(mbDomoBoard->getRELE2());
	//mbDomoBoard->actBOTON2.action = NULL;

	AccionesTemporizadas_Ptr.clear();
	AccionesTemporizadas_Ptr.push(&mbDomoBoard->actBOTON1);
	AccionesTemporizadas_Ptr.push(&mbDomoBoard->actBOTON2);


	mbDomoBoard->actBTNOPT.action = NULL;
	mbDomoBoard->actPHOTRES.action	= NULL;
	mbDomoBoard->actPIR.action 		= NULL;
}

void Config_P7_2_Config_PIR(void){

	mbDomoBoard->actPHOTRES.action	= NULL;

	//PIR
	mbDomoBoard->actPIR.action = Interruptor_Temporizado;
	mbDomoBoard->actPIR.actionControl.Actuadores.clear();

	//TODO: Actualizar ctrl_IntTemp_1 para que tiempo activo apunte al registro ModBus Correspondiente

	ctrl_IntTemp_1.Cte_Norma   = MILLISEC;					//valor para indicar/Pasar tiempo activo de milisegundos a segundos
	ctrl_IntTemp_1.time_activo = &Aregs[MB_TMP_PIR]; 		//Tiempo activo del PIR será almacenado en el registro ModBus
															//MB_TMP_PIR
	mbDomoBoard->actPIR.actionControl.AccionTemporizada = &ctrl_IntTemp_1;
	mbDomoBoard->actPIR.actionControl.Actuadores.push(mbDomoBoard->getRELE1());

	AccionesTemporizadas_Ptr.clear();
	AccionesTemporizadas_Ptr.push(&mbDomoBoard->actPIR);

	//PULSADORES
	mbDomoBoard->actBOTON1.action = Conmutador;

	mbDomoBoard->actBOTON2.action = Conmutador;

	mbDomoBoard->actBTNOPT.action = Conmutador;

	ActuadoresConmutador.clear();
	ActuadoresConmutador.push(mbDomoBoard->getRELE2());
}

void Config_P8_4_SRC_Interruptor(void){
	AccionesTemporizadas_Ptr.clear();

	mbDomoBoard->actBOTON1.action = Interruptor;
	mbDomoBoard->actBOTON1.actionControl.Actuadores.clear();
	mbDomoBoard->actBOTON1.actionControl.Actuadores.push(mbDomoBoard->getRELE1());
	mbDomoBoard->actBOTON1.actionControl.Actuadores.push(mbDomoBoard->getRELE2());

	mbDomoBoard->actBOTON2.action = Interruptor;
	mbDomoBoard->actBOTON2.actionControl.Actuadores.clear();
	mbDomoBoard->actBOTON2.actionControl.Actuadores.push(mbDomoBoard->getRELE1());
	mbDomoBoard->actBOTON2.actionControl.Actuadores.push(mbDomoBoard->getRELE2());

	mbDomoBoard->actBTNOPT.action 	= NULL;
	mbDomoBoard->actPIR.action 		= NULL;

	mbDomoBoard->actPHOTRES.action	= interruptor_SRC;
	mbDomoBoard->actPHOTRES.actionControl.Actuadores.push(mbDomoBoard->getRELE1());
	mbDomoBoard->actPHOTRES.actionControl.Actuadores.push(mbDomoBoard->getRELE2());
}

void Config_P9_1_Persiana(void){
	domoboard.BOTON2.sSensor.Aux = DOWN;	//Pulsador para cerrar la persiana
	domoboard.BOTON1.sSensor.Aux = UP;		//Pulsador para subir la persiana

	mbDomoBoard->actBOTON1.action = Persiana;
	//mbDomoBoard->actBOTON1.actionControl.Sensors.push(mbDomoBoard->getMBSensor(&domoboard.BOTON2.sSensor));

	mbDomoBoard->actBOTON2.action = Persiana;
	mbDomoBoard->actBOTON2.actionControl.Sensors.push(mbDomoBoard->getMBSensor(&domoboard.BOTON1.sSensor));
}

void SelectionConfiguration(uint8_t selConf){
	switch(selConf){
		case P6_APARTADO1:
			//Serial.println("P6 Apartado 1 Seleccionado");
			Config_P6_Apartado1();
			break;

		case P6_APARTADO2:
			//Serial.println("P6 Apartado 2 Seleccionado");
			Config_P6_Apartado2();
			break;

		case P6_APARTADO3:
			//Serial.println("P6 Apartado 3 Seleccionado");
			Config_P6_Apartado3();
			break;

		case P7_1_InterruptorTemporizado:
			//Serial.println("P7 Apartado 1 Seleccionado");
			Config_P7_InterruptorTemporizado();
			break;

		case P7_2_Config_PIR:
			//Serial.println("P7 Apartado 2 Seleccionado");
			Config_P7_2_Config_PIR();
			break;

		case P8_4_SRC_INTERRUPTOR:
			//Serial.println("P8 Apartado 4 Seleccionado");
			Config_P8_4_SRC_Interruptor();
			break;

		case P9_1_PERSIANA:
			//Serial.println(F("P9 Apartado 1 Seleccionado"));
			Config_P9_1_Persiana();
			break;
	}
}



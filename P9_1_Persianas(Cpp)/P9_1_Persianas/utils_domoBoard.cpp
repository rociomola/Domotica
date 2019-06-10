/*
 * utils_domoBoard.cpp
 *
 *  Created on: 10/03/2015
 *      Author: jctejero
 */

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	"utils_domoBoard.h"
#include 	"HardwareSerial.h"
#include	"Gest_Modbus.h"

//tActionControl	acInterruptor;

//tsAction		Interruptor1;

/****************************************************************************/
/***        Macro Definitions                                             ***/
/****************************************************************************/
#define		LM35			0
#define		TMP36			1
#define		SENSORTMP		TMP36

#define		BTN_PULSADO		1
#define		BTN_RELEASE		0
#define		NO_BTN			2

//#define		DEBUG_ON

/****************************************************************************/
/***        Variables Locales                                             ***/
/****************************************************************************/
QueueList<tsActuator>		ActuadoresConmutador;

uint16_t	Tmp_Int_Temp = 3000;				//Tiempo por defecto en el interruptor temporizado en milisegundos

CTRL_IntTemp				ctrl_IntTemp_1 = {0, 1, &Tmp_Int_Temp, false};
CTRL_IntTemp				ctrl_IntTemp_2 = {0, 1, &Tmp_Int_Temp, false};
CTRL_IntTemp				ctrl_IntTemp_3 = {0, 1000, NULL, false};

//Lista de acciones temporizadas
QueueList<tsAction_ptr>		AccionesTemporizadas_Ptr;

/*=================================================================*/
/*		 		  INTERRUPTOR CON VALOR ACTIVACIÓN                 */
/*=================================================================*/

void	InterruptorV(tActionControl	*acInt, bool newVal){
	//Actualizar el actuador
	tsActuator actInt;  // = acInt->Actuadores.peek(0);

	for(int i =0; i < acInt->Actuadores.count(); i++){
		actInt = acInt->Actuadores.peek(i);

		mbs.writeRegCoil(actInt.nMBReg, newVal, actInt.bkMBRegs);
	}
}

/*=============================================*/
/* UpDown Persiana usando pulsadores Domoboard */
/*=============================================*/
void Persiana(tActionControl	*acInt){
	static tsStaPer state = PER_STOP;

	byte	Act_Btn;	//Sensor (Btn) que inicia la acción
	byte	UP_DOWN_Btn;
	byte	sec_Btn;	//Sensor secundario (Btn) asociado a la acción

	//Serial.println(acInt->MBsensor->Sensor->name);

	if(acInt->MBsensor->Sensor->valor != acInt->MBsensor->Sensor->valor_Df){
		Act_Btn = BTN_PULSADO;
	}
	else Act_Btn = BTN_RELEASE;

	UP_DOWN_Btn = acInt->MBsensor->Sensor->Aux;

	//Compruebo estado del botón secundario
	if(acInt->Sensors.count() > 0){
		if(((tsMBSensor)acInt->Sensors.peek(0)).Sensor->valor != ((tsMBSensor)acInt->Sensors.peek(0)).Sensor->valor_Df){
			sec_Btn = BTN_PULSADO;
		}
		else sec_Btn = BTN_RELEASE;
	}
	else return;		//No hay definido segundo botón, No podemos procesar;

	switch(state){
	case PER_STOP:
		if((Act_Btn == BTN_PULSADO) && (sec_Btn == BTN_RELEASE)){
			if(UP_DOWN_Btn == DOWN){
				state = PER_DOWN;
			}
			else state = PER_UP;
		}
		break;

	case PER_DOWN:
		if((Act_Btn == BTN_RELEASE) && (UP_DOWN_Btn == DOWN)){
			if(sec_Btn == BTN_RELEASE){
				state = PER_STOP;
			}
			else state = PER_STOP2;
		}
		break;

	case PER_UP:
		if((Act_Btn == BTN_RELEASE) && (UP_DOWN_Btn == UP)){
			if(sec_Btn == BTN_RELEASE){
				state = PER_STOP;
			}
			else state = PER_STOP2;
		}
		break;

	case PER_STOP2:
		if((Act_Btn == BTN_RELEASE) && (sec_Btn == BTN_RELEASE)){
			state = PER_STOP;
		}
		break;
	}

	mbs.writeSingleRegister(MB_STE_PER, state, Aregs);
}

/*========================================================*/
/*		 		  INTERRUPTOR TEMPORIZADO                 */
/*========================================================*/

/*
 * Este sensor será activado mediante una variable de control, igual que el anterior, y aunque la variable de control
 * se vuelva inactiva, el interruptor, permanecerá activo durante un tiempo mínimo. Si durante el tiempo que el interruptor
 * está activo, la variable de control permace, o se activa de nuevo, el tiempo que el interruptor permanece antivo se
 * irá actualizando, de tal forma que el tiempo que el interruptor permanece ativo, siempre se cuenta desde la última vez
 * que la variable de control se volvió inactiva.
 *
 */

void	Interruptor_Temporizado(tActionControl	*acInt){

	if((acInt->MBsensor->Sensor->valor != acInt->MBsensor->Sensor->valor_Df) && acInt->MBsensor->Sensor->Activo){
		acInt->AccionTemporizada->time_lastAct	= millis();

		if(!acInt->AccionTemporizada->estado){
			acInt->AccionTemporizada->estado = true;
			InterruptorV(acInt, true);
		}
	}
	else{


		if((millis() - acInt->AccionTemporizada->time_lastAct) > (*(acInt->AccionTemporizada->time_activo))*(acInt->AccionTemporizada->Cte_Norma)){

			if(acInt->AccionTemporizada->estado){
				acInt->AccionTemporizada->estado = false;
				InterruptorV(acInt, false);
			}
		}
	}
}

/*============================================*/
/*		 		  INTERRUPTOR                 */
/*============================================*/
void	Interruptor(tActionControl	*acInt){
	if(acInt->MBsensor->Sensor->valor == acInt->MBsensor->Sensor->valor_Df){

		//Actualizar actuadores para esta aplicación
		tsActuator actInt = acInt->Actuadores.peek(0);

		//Al ser un interruptor, cambio el valor actual del actuador cero
		int newVal = (actInt.bkMBRegs[actInt.nMBReg] ^ 0x01);

		//El nuevo valor, lo extiendo a todos los actuadores asociados
		for(int i =0; i < acInt->Actuadores.count(); i++){
			actInt = acInt->Actuadores.peek(i);
			mbs.writeRegCoil(actInt.nMBReg, newVal, actInt.bkMBRegs);
		}
	}
}

/*============================================*/
/*			  INTERRUPTOR LUMINOSIDAD         */
/*============================================*/
/*
 * Interruptor por nivel de luminosidad. funcionará con una histeresis, es decir,
 * Si el interruptor está desactivado, se activará cuando alcance el "highlevel".
 *
 * Si el interruptor está ativado, se desactivará cuando alcanze el "lowlevel".
 */
void	interruptor_SRC(tActionControl	*acInt){
	static	int8_t	state = -1;

	//Detectamos situación inicial
	if(state == -1){
		//Seleccionamos el actuador inicial
		tsActuator actInt = acInt->Actuadores.peek(0);

		//Consideramos que el estado del actuador tiene relación directa con
		//el estado del interruptor
		if((bool)(actInt.bkMBRegs[actInt.nMBReg]))
			state = 1;
		else state = 2;
	}

	switch(state){
	case 1:
		if(acInt->MBsensor->Sensor->valor >= (int)A_Regs[MB_SRC_HL]){
			state = 2;
			InterruptorV(acInt, false);
		}
		break;

	case 2:
		if(acInt->MBsensor->Sensor->valor <= (int)A_Regs[MB_SRC_LL]){
			state = 1;
			InterruptorV(acInt, true);
		}
		break;
	}
}

/*============================================*/
/*		 		    PULSADOR                  */
/*============================================*/
void Pulsador(tActionControl	*acInt){
	tsActuator actInt;

	int newVal = !(acInt->MBsensor->Sensor->valor == acInt->MBsensor->Sensor->valor_Df);

	//El nuevo valor, lo extiendo a todos los actuadores asociados
	for(int i =0; i < acInt->Actuadores.count(); i++){
		actInt = acInt->Actuadores.peek(i);
		mbs.writeRegCoil(actInt.nMBReg, newVal, actInt.bkMBRegs);
	}
}

/*============================================*/
/*		 		    CONMUTADOR                  */
/*============================================*/
void Conmutador(tActionControl	*acInt){

	if(acInt->MBsensor->Sensor->valor == acInt->MBsensor->Sensor->valor_Df){

		//Actualizar actuadores para esta aplicación
		tsActuator actInt = ActuadoresConmutador.peek(0);

		//Al ser un interruptor, cambio el valor actual del actuador cero
		int newVal = (actInt.bkMBRegs[actInt.nMBReg] ^ 0x01);

		//El nuevo valor, lo extiendo a todos los actuadores asociados
		for(int i =0; i < ActuadoresConmutador.count(); i++){
			actInt = ActuadoresConmutador.peek(i);
			mbs.writeRegCoil(actInt.nMBReg, newVal, actInt.bkMBRegs);
		}
	}
}


/*****************************************************************************************/
/***********************************  Leer Temperatura ***********************************/
/*****************************************************************************************/
void Calc_Temperatura(tActionControl	*acInt){
	float valTMP;
	int   temp;

//	static float vT = 0;

	valTMP = acInt->MBsensor->Sensor->valor*.004882812;    //Conviere resultado convertidor a voltios

	switch(SENSORTMP){
	case TMP36:
		valTMP = (valTMP - .5)*100;          //Convierte Temperatura de 10 mV por grado con 500 mV de Offset
		break;

	case LM35:
		valTMP = valTMP*100;
		break;
	}

	temp = (valTMP - (int)valTMP)*100;

    if(temp < 25) temp = 0;
    else if ((25 < temp)&&(temp < 75)) temp = 5;
    else if ((75 < temp)&&(temp <= 99)){
    	temp = 0;
    	valTMP = (int)valTMP +1;
    }

    //tempf = (((int)valTMP & 0xff) << 8) | (temp & 0xff);
    /*
    if(Sensor->Coil != NULL)
    	Sensor->Coil->Regs_App[Sensor->Coil->MBReg_App] = (((int)valTMP & 0xff) << 8) | (temp & 0xff);
	*/

    acInt->MBsensor->bkMBRegs[acInt->MBsensor->nMBReg] = (((int)valTMP & 0xff) << 8) | (temp & 0xff);

    /*
    switch(Sensor->aux){
    case MB_TMP:
    	mbDomoBoard->getTMP_MB().bkMBRegs[mbDomoBoard->getTMP_MB().nMBReg] = (((int)valTMP & 0xff) << 8) | (temp & 0xff);
    	//Serial.println(Sensor->name);
    	break;
    }
    */

#ifdef DEBUG_ON
    if(vT != valTMP){
    	vT = valTMP;
    	Serial.print("Temperatura = " );
    	Serial.print((int)valTMP,DEC);
    	Serial.print(".");
    	Serial.println(temp,DEC);

    //	Serial.print("Temperatura F -> ");
    //	Serial.println(Sensor->Regs_App[Sensor->MBReg_App],BIN);
    }
#endif
}

/*=================================================================
 * 		Rutina para testar y ejecutar las acciones temporizadas   =
 *=================================================================*/
void Acciones_Temporizadas(void){

	//Inicia las acciones temporizadas cada milisegundo
	for(int n=0; n<AccionesTemporizadas_Ptr.count(); n++){
		AccionesTemporizadas_Ptr.peek(n)->action(&(AccionesTemporizadas_Ptr.peek(n)->actionControl));
	}

}





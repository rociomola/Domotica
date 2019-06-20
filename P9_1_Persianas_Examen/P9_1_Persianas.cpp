/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include 	<P9_1_Persianas.h>
#include 	"Gest_Modbus.h"
#include	"utils_domoBoard.h"
#include	"AP_Scheduler.h"

/****************************************************************************/
/***        Variables Locales                                             ***/
/****************************************************************************/
// main loop scheduler
static AP_Scheduler scheduler;

/****************************************************************************/
/***          Local Functions                                             ***/
/****************************************************************************/
void leerSensores(void);
void leerPOTs(void);
void leerPhores(void);
void leerPhottor(void);
void leerTemperatura(void);

/*
  scheduler table - all regular tasks apart from the fast_loop()
  should be listed here, along with how often they should be called
  (in 10ms units) and the maximum time they are expected to take (in
  microseconds)
 */
static const AP_Scheduler::Task scheduler_tasks[] PROGMEM = {
		{ leerSensores,     		25,     5000  },
		{ Acciones_Temporizadas,  	1,		500  },
		{ leerPOTs,          		10,     500  },
		{ leerPhores,               10,     500  },
		{ leerPhottor,              10,     500  },
		{ leerTemperatura,			100,    500  }
};

/*
 * Leer Temperatura
 */
void leerTemperatura(void){
	domoboard.leerSensor(&domoboard.sTEMP);
}

/*
 * Leer Photo transistor
 */
void leerPhottor(void){
	//Serial.println("Sensor Foto transistor");
	domoboard.leerSensor(&domoboard.sPHOTTOR);
}

/*
 * Leer PhotoResistor
 */
void leerPhores(void){
	domoboard.leerSensor(&domoboard.sPHOTRES);
}

/*
 * Leer Potenciómetros
 */
void leerPOTs(void){
	domoboard.leerSensor(&domoboard.sPOT1);
	domoboard.leerSensor(&domoboard.sPOT2);
}

void leerSensores(void){
	domoboard.leerAllSensor();
}

//The setup function is called once at startup of the sketch
void setup()
{
	//Iniciamos Modbus en Modo RTU
	Init_RTU_Modbus();

	//Leer Configuración práctica actual
	load_Config();

	// initialise the main loop scheduler
	scheduler.init(&scheduler_tasks[0], sizeof(scheduler_tasks)/sizeof(scheduler_tasks[0]));

	Serial.println(F("P9_1 Persiana"));
}

// The loop function is called in an endless loop
void loop()
{
	static uint32_t timer = 0;

	if((micros() - timer) > 10000){
		timer = micros();
//		domoboard.leerAllSensor();

		RTU_ModBus();

		// tell the scheduler one tick has passed
		scheduler.tick();

		// Ejecuta todas las tareas programadas. Hay que tener en cuenta
		// que se dispone un tiempo limitado para ello. La clase Scheduler
		// secuencia los procesos, usando el tiempo disponible para ello,
		// si no hay tiempo suficiente para todos, en el siguiente paso,
		// procesa los siguientes.

		//Tiempo disponible para ejecutar loop --> 10 ms
		uint32_t time_available = (timer + 10000) - micros();

		//Serial.println(time_available, DEC);

		scheduler.run(time_available);
	}
}

/*
 * app_Configurations.h
 *
 *  Created on: 4/4/2016
 *      Author: JuanCarlos
 */

#ifndef APP_CONFIGURATIONS_H_
#define APP_CONFIGURATIONS_H_

#include	"stdint.h"

/****************************************************************************/
/***        Macro Definitions                                             ***/
/****************************************************************************/
//Defininición de comando de configuración
#define	P6_APARTADO1				0x61
#define	P6_APARTADO2				0x62
#define	P6_APARTADO3				0x63
#define P7_1_InterruptorTemporizado	0x71
#define P7_2_Config_PIR				0x72
#define P8_4_SRC_INTERRUPTOR		0x84
#define P9_1_PERSIANA				0x91

#define MILLISEC					1000	//Multiplicador para pasar segundos a milisegundos

/****************************************************************************/
/***        Exported Functions                                            ***/
/****************************************************************************/
//void Config_interruptor1(void);
void SelectionConfiguration(uint8_t selConf);



#endif /* APP_CONFIGURATIONS_H_ */

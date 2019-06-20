################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
..\Utilidades\AP_Scheduler\AP_Scheduler.cpp 

LINK_OBJ += \
.\Utilidades\AP_Scheduler\AP_Scheduler.cpp.o 

CPP_DEPS += \
.\Utilidades\AP_Scheduler\AP_Scheduler.cpp.d 


# Each subdirectory must supply rules for building sources it contributes
Utilidades\AP_Scheduler\AP_Scheduler.cpp.o: ..\Utilidades\AP_Scheduler\AP_Scheduler.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\domotica\eclipse_cpp\arduinoPlugin\packages\arduino\tools\avr-gcc\5.4.0-atmel3.6.1-arduino2/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10802 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR     -I"C:\domotica\eclipse_cpp\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\cores\arduino" -I"C:\domotica\eclipse_cpp\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\variants\standard" -I"C:\domotica\eclipse_cpp\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\libraries\EEPROM\src" -I"C:\domotica\WorksSpace\Cpp\P9_1_Persianas_Examen" -I"C:\domotica\WorksSpace\Cpp\P9_1_Persianas_Examen\DomoBoard" -I"C:\domotica\WorksSpace\Cpp\P9_1_Persianas_Examen\ModbusSlave" -I"C:\domotica\WorksSpace\Cpp\P9_1_Persianas_Examen\Utilidades\AP_Scheduler" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -D__IN_ECLIPSE__=1 -x c++ "$<"  -o  "$@"
	@echo 'Finished building: $<'
	@echo ' '



################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
..\Gest_Modbus.cpp \
..\P9_1_Persianas.cpp \
..\app_Configurations.cpp \
..\utils_domoBoard.cpp 

LINK_OBJ += \
.\Gest_Modbus.cpp.o \
.\P9_1_Persianas.cpp.o \
.\app_Configurations.cpp.o \
.\utils_domoBoard.cpp.o 

CPP_DEPS += \
.\Gest_Modbus.cpp.d \
.\P9_1_Persianas.cpp.d \
.\app_Configurations.cpp.d \
.\utils_domoBoard.cpp.d 


# Each subdirectory must supply rules for building sources it contributes
Gest_Modbus.cpp.o: ..\Gest_Modbus.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\tools\avr-gcc\5.4.0-atmel3.6.1-arduino2/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10802 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR     -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\cores\arduino" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\variants\standard" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\libraries\EEPROM" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\libraries\EEPROM\src" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\DomoBoard" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\ModbusSlave" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\Utilidades\AP_Scheduler" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -D__IN_ECLIPSE__=1 -x c++ "$<"  -o  "$@"
	@echo 'Finished building: $<'
	@echo ' '

P9_1_Persianas.cpp.o: ..\P9_1_Persianas.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\tools\avr-gcc\5.4.0-atmel3.6.1-arduino2/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10802 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR     -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\cores\arduino" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\variants\standard" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\libraries\EEPROM" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\libraries\EEPROM\src" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\DomoBoard" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\ModbusSlave" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\Utilidades\AP_Scheduler" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -D__IN_ECLIPSE__=1 -x c++ "$<"  -o  "$@"
	@echo 'Finished building: $<'
	@echo ' '

app_Configurations.cpp.o: ..\app_Configurations.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\tools\avr-gcc\5.4.0-atmel3.6.1-arduino2/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10802 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR     -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\cores\arduino" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\variants\standard" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\libraries\EEPROM" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\libraries\EEPROM\src" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\DomoBoard" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\ModbusSlave" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\Utilidades\AP_Scheduler" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -D__IN_ECLIPSE__=1 -x c++ "$<"  -o  "$@"
	@echo 'Finished building: $<'
	@echo ' '

utils_domoBoard.cpp.o: ..\utils_domoBoard.cpp
	@echo 'Building file: $<'
	@echo 'Starting C++ compile'
	"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\tools\avr-gcc\5.4.0-atmel3.6.1-arduino2/bin/avr-g++" -c -g -Os -w -std=gnu++11 -fpermissive -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -Wno-error=narrowing -MMD -flto -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10802 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR     -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\cores\arduino" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\variants\standard" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\libraries\EEPROM" -I"C:\Eclipse\Oxy_cpp_baeyen\arduinoPlugin\packages\arduino\hardware\avr\1.6.23\libraries\EEPROM\src" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\DomoBoard" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\ModbusSlave" -I"D:\Trabajo\Eclipse\Workspaces\Cpp\Practicas_Electronica_para_Domotica\P9_1_Persianas\Utilidades\AP_Scheduler" -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -D__IN_ECLIPSE__=1 -x c++ "$<"  -o  "$@"
	@echo 'Finished building: $<'
	@echo ' '



package Base_COM_Serie;

public class MB_Registers {
	
	//Discrete Output Coils
	public static final int MB_RELE1 	= 0;
	public static final int MB_RELE2 	= 1;
	public static final int MB_ACTPIR   = 2;
	public static final int MB_ACTSRC   = 3;
	public static final int MB_O_COILS	= 4;
	
	//Discrete Input Contacts (Digital Registers)
	public static final int MB_BTN1		= 0;				// Pulsador 1
	public static final int MB_BTN2		= 1;				// Pulsador 2
	public static final int MB_OPT		= 2;				// Entrada Optocoplada	
	public static final int MB_PIR		= 3;				// Sensor de Movimiento PIR
//	public static final int MB_PERUP	= 4;				// Entrada virtual para el control de la persiana UP
//	public static final int MB_PERDOWN	= 5;				// Entrada Virtual para el control de la persiana DOWN				
	public static final int MB_I_REGS	= 4;	 			// total number of registers on slave
	
	//Analog Output Holding Registers
	public static final int MB_PRACT	= 0;				// Registro para indicar la práctica con la que trabajamos
	public static final int MB_TMP_PIR	= 1;				// Registro para controlar el tiempo activo del sensor PIR (Segundos)
	public static final int MB_SRC_HL	= 2;				// Registro para controlar el nivel superior de activación SRC
	public static final int MB_SRC_LL	= 3;				// Registro para controlar el nivel Inferior de activación SRC
	public static final int MB_STE_PER  = 4;				// Estado Persiana
	public static final int MB_AREGS	= 5;
	
	//Analog Input Register
	public static final int MB_POT1			= 0;			// Registro para indicar el estado del potenciómetro 1
	public static final int MB_POT2			= 1;			// Registro para indicar el estado del potenciómetro 1
	public static final int MB_PHOTRES		= 2;			// Foto Resistencia
	public static final int MB_TEMP			= 3;			// Sensor de Temperatura
	public static final int MB_PHOTOTTOR	= 4;			// Photo Transistor
	public static final int MB_I_AREGS		= 5;
	
	
	//Selección configuración Práctica
	public static final int SELPRACT[] = {0x61, 0x62, 0x63, 0x71, 0x72, 0x84, 0x91};
}

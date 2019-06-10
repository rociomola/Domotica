package gui.Visualizers;

import java.awt.Component;
import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import Base_COM_Serie.ConstantesApp;
import Base_COM_Serie.Ctrl_Persianas;
import Base_COM_Serie.MB_Registers;
import Comm.io.CommTransport;
import ModBus.Const_Modbus;
import ModBus.ModBus;
import ModBus.ModBusEvent;
import eu.hansolo.steelseries.extras.LightBulb;

import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import eu.hansolo.steelseries.extras.Led;
import eu.hansolo.steelseries.tools.LedColor;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import eu.hansolo.steelseries.gauges.Radial2Top;
import eu.hansolo.steelseries.tools.PointerType;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.GaugeType;
import eu.hansolo.steelseries.gauges.DigitalRadial;
import eu.hansolo.steelseries.gauges.DisplaySingle;
import javax.swing.plaf.basic.BasicArrowButton;

public class DomoBoardGui extends JPanel implements Visualizer {
	/**
	 * 
	 */
	private static final int			DIGITALINPUT 		= 	1; 	//Refresh each TIMEREFRESH
	private static final int			DIGITALOUTPUT 		= 	2;	//Refresh each four TIMEREFRESH
	private static final int			ANALOGINPUT 		= 	1;	//Refresh each TIMEREFRESH
	private static final int			ANALOGOUTPUT 		= 	2;	//Refresh each TIMEREFRESH
	
	private static final long serialVersionUID = 8619767299083215147L;
	
	//private final 	JPanel 			panel;
	private 		LightBulb 			lightBulb1;
	private 		LightBulb 			lightBulb2;
	private			Led 				ledBtn1;
	private			Led 				ledBtn2;
	private			Led 				ledBtnOpt;
	private			Led 				ledPIR;
	private			Radial2Top 			r2T_Pot1;
	private 		Radial2Top 			r2T_Pot2;
	private			DigitalRadial 		dRSRC;
	private         DigitalRadial 		dRTtor;
	private         DisplaySingle 		dSTemp;
	private 		JCheckBox 			cbActSRC;
	private			JLabel 				lbStadoPersiana;
	
	private			JCheckBox 			cbActPIR;
	private 		MouseAdapter 		ma_lightBulb;
	private final 	String 				category;
	private final 	boolean				isCategory = true;
	private final	CommTransport 		sn_Transport;
	private final 	String				address;
	
	private			int					digitalInput 	= 0;
	private         int					digitalOutput 	= 0;
	private			int					analogInput		= 0;
	
	private			 boolean     		stActualize = true;
	
	//Banco de registros para mantener sincronizada la comunicación Modbus 
	private  		int 				Cregs[];
	private  		int 				Dregs[];
	private			int					Aregs[];
	private         int					Eregs[];
	private JTextField tiempoPIR;
	private JTextField tf_HL_SRC;
	private JTextField tf_LL_SRC;
	
//	public App_Connection serialConnection;

	public DomoBoardGui(String category, String address, CommTransport sn_Transport) {
		
		super();
		
		this.category 		= category;
		this.address		= address;
		this.sn_Transport 	= sn_Transport;
		
		this.setLayout(null);
		
		//Crea Banco de registros para mantener sincronizada la comunicación Modbus 
		Cregs = new int [MB_Registers.MB_O_COILS];
		Dregs = new int [MB_Registers.MB_I_REGS];
		Eregs = new int [MB_Registers.MB_I_AREGS];
		
		ma_lightBulb = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				ONOFF_Bulb(((LightBulb)e.getComponent()));
			}
		};
			
		lightBulb1 = new LightBulb();
		lightBulb1.setOn(true);
		lightBulb1.setGlowColor(Color.YELLOW);
		lightBulb1.setBounds(10, 122, 78, 78);
		//panel.add(lightBulb1);
		this.add(lightBulb1);
		
		lightBulb1.addMouseListener(ma_lightBulb);
		
		lightBulb2 = new LightBulb();
		lightBulb2.setOn(true);
		lightBulb2.setGlowColor(Color.RED);
		lightBulb2.setBounds(98, 122, 78, 78);
		
		this.add(lightBulb2);
		
		JLabel lblNewLabel = new JLabel("REL\u00C9 1");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel.setBounds(20, 203, 63, 24);
		add(lblNewLabel);
		
		JLabel lblRel = new JLabel("REL\u00C9 2");
		lblRel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblRel.setBounds(106, 203, 63, 24);
		add(lblRel);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel.setBounds(10, 11, 166, 100);
		add(panel);
		
		JLabel label = new JLabel("Estado Pulsadores");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.RED);
		label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label.setBounds(0, 75, 166, 14);
		panel.add(label);
		
		ledBtn1 = new Led();
		ledBtn1.setBounds(11, 11, 36, 36);
		panel.add(ledBtn1);
		
		ledBtn2 = new Led();
		ledBtn2.setBounds(67, 11, 36, 36);
		panel.add(ledBtn2);
		
		ledBtnOpt = new Led();
		ledBtnOpt.setBounds(120, 11, 36, 36);
		panel.add(ledBtnOpt);
		
		JLabel label_1 = new JLabel("BTN 1");
		label_1.setForeground(Color.BLUE);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(11, 43, 36, 14);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("BTN 2");
		label_2.setForeground(Color.BLUE);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(67, 43, 36, 14);
		panel.add(label_2);
		
		JLabel label_3 = new JLabel("BTN OPT");
		label_3.setForeground(Color.BLUE);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(110, 43, 56, 14);
		panel.add(label_3);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel_1.setBounds(186, 11, 237, 100);
		add(panel_1);
		
		ledPIR = new Led();
		ledPIR.setLedColor(LedColor.CYAN);
		ledPIR.setBounds(0, 0, 87, 100);
		panel_1.add(ledPIR);
		
		JLabel label_4 = new JLabel("PIR");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_4.setBounds(0, 69, 237, 24);
		panel_1.add(label_4);
		
		cbActPIR = new JCheckBox("Activar PIR");
		cbActPIR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String vSel;
				String vReg = String.valueOf(MB_Registers.MB_ACTPIR);
				
				AbstractButton aButton = (AbstractButton) arg0.getSource();
		        boolean selected = aButton.getModel().isSelected();
		        
		        if(selected) vSel = "1";
				else vSel = "0";

				String[] args = {address, String.valueOf(Const_Modbus.WRITE_COIL), vReg, vSel};
				ModBus.InitComunication(args, sn_Transport, Cregs);
			}			
		});
		cbActPIR.setSelected(true);
		cbActPIR.setBounds(85, 12, 87, 23);
		panel_1.add(cbActPIR);
		
		tiempoPIR = new JTextField();
		tiempoPIR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String vSel = tiempoPIR.getText();
				String vReg = String.valueOf(MB_Registers.MB_TMP_PIR);
								
				String[] args = {address, String.valueOf(Const_Modbus.WRITE_SINGLE_REGISTER), vReg, vSel};
				ModBus.InitComunication(args, sn_Transport, Aregs);
				
				//TODO: Después de escribir, deberíamos leer para comprobar que se ha escrito de forma correcta
				
			}
		});
		//tiempoPIR.setText("3");
		tiempoPIR.setColumns(10);
		tiempoPIR.setBounds(85, 42, 37, 20);
		panel_1.add(tiempoPIR);
		
		JLabel label_5 = new JLabel("Tiempo (Segs.)");
		label_5.setBounds(132, 45, 95, 14);
		panel_1.add(label_5);
		
		r2T_Pot1 = new Radial2Top();
		r2T_Pot1.setLcdBackgroundVisible(false);
		r2T_Pot1.setGaugeType(GaugeType.TYPE5);
		r2T_Pot1.setLcdUnitStringVisible(true);
		r2T_Pot1.setLcdColor(LcdColor.BLUE_LCD);
		r2T_Pot1.setLedColor(LedColor.GREEN);
		r2T_Pot1.setPointerType(PointerType.TYPE5);
		r2T_Pot1.setTrackVisible(true);
		r2T_Pot1.setUserLedVisible(true);
		r2T_Pot1.setUnitString("%");
		r2T_Pot1.setTitle("Pot. 1");
		r2T_Pot1.setLcdScientificFormat(true);
		r2T_Pot1.setLcdInfoString("Test");
		r2T_Pot1.setBounds(10, 257, 197, 193);
		add(r2T_Pot1);
		
		r2T_Pot2 = new Radial2Top();
		r2T_Pot2.setUserLedVisible(true);
		r2T_Pot2.setUnitString("%");
		r2T_Pot2.setTrackVisible(true);
		r2T_Pot2.setTitle("Pot. 2");
		r2T_Pot2.setPointerType(PointerType.TYPE5);
		r2T_Pot2.setLedColor(LedColor.GREEN);
		r2T_Pot2.setLcdUnitStringVisible(true);
		r2T_Pot2.setLcdScientificFormat(true);
		r2T_Pot2.setLcdInfoString("Test");
		r2T_Pot2.setLcdColor(LcdColor.BLUE_LCD);
		r2T_Pot2.setLcdBackgroundVisible(false);
		r2T_Pot2.setGaugeType(GaugeType.TYPE5);
		r2T_Pot2.setBounds(225, 257, 193, 193);
		add(r2T_Pot2);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel_2.setBounds(186, 122, 237, 124);
		add(panel_2);
		
		JLabel label_6 = new JLabel("Photo Resistencia");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_6.setBounds(0, 94, 218, 24);
		panel_2.add(label_6);
		
		cbActSRC = new JCheckBox("Activar SRC");
		cbActSRC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String vSel;
				String vReg = String.valueOf(MB_Registers.MB_ACTSRC);
				
				AbstractButton aButton = (AbstractButton) arg0.getSource();
		        boolean selected = aButton.getModel().isSelected();
		        
		        if(selected) vSel = "1";
				else vSel = "0";

				String[] args = {address, String.valueOf(Const_Modbus.WRITE_COIL), vReg, vSel};
				ModBus.InitComunication(args, sn_Transport, Cregs);
			}
		});
		
		cbActSRC.setSelected(true);
		cbActSRC.setBounds(111, 7, 101, 23);
		panel_2.add(cbActSRC);
		
		tf_HL_SRC = new JTextField();
		tf_HL_SRC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				String vSel = tf_HL_SRC.getText();
				String vReg = String.valueOf(MB_Registers.MB_SRC_HL);
									
				String[] args = {address, String.valueOf(Const_Modbus.WRITE_SINGLE_REGISTER), vReg, vSel};
				ModBus.InitComunication(args, sn_Transport, Aregs);
					
				//TODO: Después de escribir, deberíamos leer para comprobar que se ha escrito de forma correcta
			}
		});
		//tf_HL_SRC.setText("400");
		tf_HL_SRC.setColumns(10);
		tf_HL_SRC.setBounds(112, 37, 37, 20);
		panel_2.add(tf_HL_SRC);
		
		JLabel label_7 = new JLabel("High Level");
		label_7.setBounds(159, 40, 72, 14);
		panel_2.add(label_7);
		
		dRSRC = new DigitalRadial();
		dRSRC.setValue(500.0);
		dRSRC.setMaxValue(1000.0);
		dRSRC.setBounds(10, 7, 86, 86);
		panel_2.add(dRSRC);
		
		tf_LL_SRC = new JTextField();
		tf_LL_SRC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				String vSel = tf_LL_SRC.getText();
				String vReg = String.valueOf(MB_Registers.MB_SRC_LL);
									
				String[] args = {address, String.valueOf(Const_Modbus.WRITE_SINGLE_REGISTER), vReg, vSel};
				ModBus.InitComunication(args, sn_Transport, Aregs);
					
				//TODO: Después de escribir, deberíamos leer para comprobar que se ha escrito de forma correcta
			}
		});
		//tf_LL_SRC.setText("300");
		tf_LL_SRC.setColumns(10);
		tf_LL_SRC.setBounds(111, 68, 37, 20);
		panel_2.add(tf_LL_SRC);
		
		JLabel label_8 = new JLabel("Low Level");
		label_8.setBounds(158, 71, 72, 14);
		panel_2.add(label_8);
		
		JLabel label_9 = new JLabel("Potenci\u00F3metro 1");
		label_9.setHorizontalAlignment(SwingConstants.CENTER);
		label_9.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_9.setBounds(10, 390, 195, 24);
		add(label_9);
		
		JLabel label_10 = new JLabel("Potenci\u00F3metro 2");
		label_10.setHorizontalAlignment(SwingConstants.CENTER);
		label_10.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_10.setBounds(230, 390, 193, 24);
		add(label_10);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel_3.setBounds(433, 122, 166, 124);
		add(panel_3);
		
		JLabel lblPhotoTransistor = new JLabel("Photo Transistor");
		lblPhotoTransistor.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhotoTransistor.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblPhotoTransistor.setBounds(0, 94, 165, 24);
		panel_3.add(lblPhotoTransistor);
		
		dRTtor = new DigitalRadial();
		dRTtor.setValue(500.0);
		dRTtor.setMaxValue(1000.0);
		dRTtor.setBounds(39, 7, 86, 86);
		panel_3.add(dRTtor);
		
		dSTemp = new DisplaySingle();
		dSTemp.setCustomLcdUnitFontEnabled(true);
		dSTemp.setLcdValueFont(new Font("Verdana", Font.PLAIN, 9));
		dSTemp.setFont(new Font("Courier New", Font.PLAIN, 12));
		dSTemp.setCustomLcdUnitFont(new Font("Verdana", Font.PLAIN, 16));
		dSTemp.setLcdUnitFont(new Font("Verdana", Font.PLAIN, 8));
		dSTemp.setLcdValue(24.5);
		dSTemp.setLcdUnitString("\u00BAC");
		dSTemp.setLcdInfoString("Temperatura");
		dSTemp.setLcdInfoFont(new Font("Verdana", Font.PLAIN, 12));
		dSTemp.setBounds(433, 17, 166, 88);
		add(dSTemp);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel_4.setBounds(609, 17, 145, 229);
		add(panel_4);
		
		JLabel label_11 = new JLabel("CTRL. Persiana");
		label_11.setHorizontalAlignment(SwingConstants.CENTER);
		label_11.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_11.setBounds(0, 194, 145, 24);
		panel_4.add(label_11);
		
		BasicArrowButton perUP = new BasicArrowButton(1);
		perUP.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//Cregs[MB_Registers.MB_PERUP] = 1;	//ON
				UPDOWN_Persiana(Ctrl_Persianas.PER_UP);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				//Cregs[MB_Registers.MB_PERUP] = 0;	//OFF
				UPDOWN_Persiana(Ctrl_Persianas.PER_STOP);
			}
		});
		perUP.setBounds(44, 50, 57, 66);
		panel_4.add(perUP);
		
		BasicArrowButton perDOWN = new BasicArrowButton(5);
		perDOWN.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//Cregs[MB_Registers.MB_PERDOWN] = 1;	//ON
				UPDOWN_Persiana(Ctrl_Persianas.PER_DOWN);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				//Cregs[MB_Registers.MB_PERDOWN] = 0;	//OFF
				UPDOWN_Persiana(Ctrl_Persianas.PER_STOP);
			}
		});
		perDOWN.setBounds(44, 117, 57, 66);
		panel_4.add(perDOWN);
		
		lbStadoPersiana = new JLabel("Parada");
		lbStadoPersiana.setHorizontalAlignment(SwingConstants.CENTER);
		lbStadoPersiana.setForeground(Color.RED);
		lbStadoPersiana.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lbStadoPersiana.setBounds(0, 11, 145, 24);
		panel_4.add(lbStadoPersiana);
		
		lightBulb2.addMouseListener(ma_lightBulb);
		
		if(sn_Transport.isConnected())
			leerConfiguracionInicial();
	}
	
	private void UPDOWN_Persiana(Ctrl_Persianas valPer){
		
		switch(valPer){
		case PER_STOP:
			logVisualizer("Persiana Stop"+ConstantesApp.CRLF);
			break;
		case PER_DOWN:
			logVisualizer("Persiana Down"+ConstantesApp.CRLF);
			break;
		case PER_UP:
			logVisualizer("Persiana Up"+ConstantesApp.CRLF);
			break;
		default:
			break;
		}
		
		//String 	vSel 	= Integer.toString(valPer.toInteger(x)));
		String 	vSel 	= Integer.toString(valPer.ordinal());
		String 	vReg 	= String.valueOf(MB_Registers.MB_STE_PER);
		String 	address	= "1";	//BroadCast
		
		int		Aregs[] = new int [MB_Registers.MB_AREGS];
		
		String[] args = {address, String.valueOf(Const_Modbus.WRITE_SINGLE_REGISTER), vReg, vSel};
		ModBus.InitComunication(args, sn_Transport, Aregs);
		
		/*
		String vReg = String.valueOf(MB_Reg);		
		String[] args = {address, String.valueOf(Const_Modbus.WRITE_COIL), vReg, "1"}; 
		ModBus.InitComunication(args, sn_Transport, Cregs);
		*/
		
	}
	
	private void ONOFF_Bulb(LightBulb lightBulb){
		String vBulb;
		String vReg;
		
		lightBulb.setOn(!lightBulb.isOn());
		
		if(lightBulb.isOn()) vBulb = "1";
		else vBulb = "0";
		
		//serialServer.sendToNode("Prueba");
		
	
		if(lightBulb == lightBulb1) vReg = String.valueOf(MB_Registers.MB_RELE1);
		else vReg = String.valueOf(MB_Registers.MB_RELE2);
		
		//String[] args = {"1", "6", vReg, vBulb}; 		
		//SerialModBus.InitComunication(args, serialConnection, regs);
		String[] args = {address, String.valueOf(Const_Modbus.WRITE_COIL), vReg, vBulb};
		ModBus.InitComunication(args, sn_Transport, Cregs);
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getTitle() {
		return "Address : "+address;
	}

	@Override
	public Component getPanel() {
		return this;
	}

	@Override
	public boolean isCategory() {
		return isCategory;
	}
	
	@Override
	public  void		CT_Opened(){
		logVisualizer("DomoBoard Puerto serie Abierto");
		
		leerConfiguracionInicial();
		
	};
	
	private void leerConfiguracionInicial(){
		//******************************************
		//Leer elementos de configuración Analógico
		//******************************************
		String 	vSel 	= "3"; //Leemos un tres registros, tiempo PIR, MAX y MIN valores activación SRC  
		String 	vReg 	= String.valueOf(MB_Registers.MB_TMP_PIR);
		String 	address	= "1";	//BroadCast
				
		int		Aregs[] = new int [MB_Registers.MB_AREGS];
				
		String[] args = {address, String.valueOf(Const_Modbus.READ_MULTIPLE_REGISTERS), vReg, vSel};
		ModBus.InitComunication(args, sn_Transport, Aregs);
				
		ModBusEvent e = new ModBusEvent(Aregs);
		e.set_Args(args);
		//e.setRegs(Dregs);
		UpdateElements(e);
				

		//******************************************
		//Leer elementos de configuración Digitales
		//******************************************
		vReg = String.valueOf(MB_Registers.MB_ACTPIR);
		String nCoils = "1"; //String.valueOf(MB_Registers.MB_O_COILS);
			
		String[] iargs = {address, String.valueOf(Const_Modbus.READ_COILS), vReg, nCoils};
		ModBus.InitComunication(iargs, sn_Transport, Cregs);
			
		//ModBusEvent e = new ModBusEvent(Cregs);
		e.setDigital(true);
		e.set_Args(iargs);
			
		UpdateElements(e);
		
		/*
		*  Lee estado Activado/desactivado photoresistor -- Sensor luminosidad 
		*/
		vReg = String.valueOf(MB_Registers.MB_ACTSRC);
		nCoils = "1"; //String.valueOf(MB_Registers.MB_O_COILS);
		
		iargs = new String[] {address, String.valueOf(Const_Modbus.READ_COILS), vReg, nCoils};
		ModBus.InitComunication(iargs, sn_Transport, Cregs);
					
		//ModBusEvent e = new ModBusEvent(Cregs);
		e.setDigital(true);
		e.set_Args(iargs);
					
		UpdateElements(e);
		
		
	}

	@Override
	public void Actualize() {
		
		if(stActualize){
			// Actualizar dispositivos modbus
			String 		nCoils;
			String 		vReg;
			String[] 	aArgs;
			String 		nRegs;
		
			ModBusEvent e = new ModBusEvent(Cregs);
		
			//Read "Discrete Output Coils"
			if(++digitalOutput == DIGITALOUTPUT){
				vReg = String.valueOf(MB_Registers.MB_RELE1);
				//nCoils = String.valueOf(MB_Registers.MB_O_COILS);
				nCoils = "4";
		
				String[] args = {address, String.valueOf(Const_Modbus.READ_COILS), vReg, nCoils};
				ModBus.InitComunication(args, sn_Transport, Cregs);
		
				//ModBusEvent e = new ModBusEvent(Cregs);
				e.setDigital(true);
				e.set_Args(args);
		
				UpdateElements(e);
			
				digitalOutput = 0;
			}
		
			//Read Discrete Input Contacts
			if(++digitalInput == DIGITALINPUT){
				vReg = String.valueOf(MB_Registers.MB_BTN1);
				nCoils = String.valueOf(MB_Registers.MB_I_REGS);
		
				String[] iargs = {address, String.valueOf(Const_Modbus.READ_INPUT_DISCRETES), vReg, nCoils};
				ModBus.InitComunication(iargs, sn_Transport, Dregs);
		
//		System.out.println("Dregs "+MB_Registers.MB_PIR.getReg()+" : "+ Dregs[MB_Registers.MB_PIR.getReg()]);
				e.set_Args(iargs);
				e.setRegs(Dregs);
				UpdateElements(e);
			
				digitalInput = 0;
			}
		
			//Lectura de registros analógicas de entrada
			if(++analogInput == ANALOGINPUT){
				vReg = String.valueOf(MB_Registers.MB_POT1);
				nRegs = String.valueOf(MB_Registers.MB_I_AREGS);
			
				aArgs = new String[] {address, String.valueOf(Const_Modbus.READ_INPUT_REGISTERS), vReg, nRegs};
				ModBus.InitComunication(aArgs, sn_Transport, Eregs);
			
				e.set_Args(aArgs);
				e.setRegs(Eregs);
				UpdateElements(e);
			
				//analogInput = 0;
			}
			
			//******************************************
			//Lectura de registros analógicos de salida
			//******************************************
			//Lectura de registros analógicas de entrada
			if(++analogInput == ANALOGOUTPUT){
				vReg = String.valueOf(MB_Registers.MB_STE_PER);
				nRegs = "1";
				
				aArgs = new String[] {address, String.valueOf(Const_Modbus.READ_MULTIPLE_REGISTERS), vReg, nRegs};
				ModBus.InitComunication(aArgs, sn_Transport, Eregs);
				
				e.set_Args(aArgs);
				e.setRegs(Eregs);
				UpdateElements(e);
				
				analogInput = 0;
			}
			
			/*
			String 	vSel 	= "3"; //Leemos un tres registros, tiempo PIR, MAX y MIN valores activación SRC  
			String 	vReg 	= String.valueOf(MB_Registers.MB_TMP_PIR);
			String 	address	= "1";	//BroadCast
					
			int		Aregs[] = new int [MB_Registers.MB_AREGS];
					
			String[] args = {address, String.valueOf(Const_Modbus.READ_MULTIPLE_REGISTERS), vReg, vSel};
			ModBus.InitComunication(args, sn_Transport, Aregs);
					
			ModBusEvent e = new ModBusEvent(Aregs);
			e.set_Args(args);
			//e.setRegs(Dregs);
			UpdateElements(e);
			*/
		}
		
//		ledPir.setLedOn(Dregs[MB_Registers.MB_PIR.getReg()] == 1);
	}
	
	public void UpdateElements(final ModBusEvent e){
		/*
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
		*/
		int addr = Integer.parseInt(e.get_Args()[2]);
		int nReg = Integer.parseInt(e.get_Args()[3]);
					
		switch(Integer.parseInt(e.get_Args()[1])){
		case Const_Modbus.READ_MULTIPLE_REGISTERS:
			//Cada registro esta compruesto de dos bytes "little endian"
			for(int i = 0;i<(nReg); i++){
				switch(addr++){
				case MB_Registers.MB_TMP_PIR:
					//tiempoPIR.setText(Integer.toString(((e.getRegs()[i+1]&0xFF)<<8)|((e.getRegs()[i]&0xFF))));
					tiempoPIR.setText(Integer.toString(e.getRegs()[i]));
					break;
					
				case MB_Registers.MB_SRC_HL:					
					tf_HL_SRC.setText(Integer.toString(e.getRegs()[i]));
					break;
					
				case MB_Registers.MB_SRC_LL:
					//int test = ((e.getRegs()[i+1]&0xFF)<<8)|((e.getRegs()[i]&0xFF));
					tf_LL_SRC.setText(Integer.toString(e.getRegs()[i]));
					break;
					
				case MB_Registers.MB_STE_PER:
					Ctrl_Persianas ctrlPersianas = Ctrl_Persianas.fromInteger(e.getRegs()[i]);
					
					switch((ctrlPersianas)){
					case PER_DOWN:
						lbStadoPersiana.setText("Bajando");
						break;
					case PER_STOP:
						lbStadoPersiana.setText("Parada");
						break;
					case PER_STOP2:
						lbStadoPersiana.setText("Parada");
						break;
					case PER_UP:
						lbStadoPersiana.setText("Subiendo");
						break;
					default:
						break;
					
					}
					
					break;
				}
			}
			break;
			
		case Const_Modbus.READ_INPUT_REGISTERS:
			for(int i = addr;i<(addr+nReg); i++){
				switch(i){
				case MB_Registers.MB_POT1:
					r2T_Pot1.setValue(100 - ((e.getRegs()[i]*100)/1024));
					break;
					
				case MB_Registers.MB_POT2:
					r2T_Pot2.setValue((e.getRegs()[i]*100)/1024);
					break;
					
				case MB_Registers.MB_PHOTRES:							
					dRSRC.setValue((e.getRegs()[i]));
					break;	
					
				case MB_Registers.MB_TEMP:							
					//dRSRC.setValue((e.getRegs()[i]));
					String Temp = ((e.getRegs()[i] >> 8)&0xff)+"."+(e.getRegs()[i]&0xff);
					//System.out.println("Temperatura : "+Temp);
					dSTemp.setLcdValue(Double.parseDouble(Temp));
					break;	
					
				case MB_Registers.MB_PHOTOTTOR:							
					dRTtor.setValue((e.getRegs()[i]));
					break;	
				}
			}
			break;
			
		case Const_Modbus.READ_COILS:
			for(int i = 0;i<(nReg); i++){
				switch(addr++){
				case MB_Registers.MB_RELE1:							
					lightBulb1.setOn((e.getRegs()[i] == 1));
					break;
							
				case MB_Registers.MB_RELE2:							
					lightBulb2.setOn((e.getRegs()[i] == 1));
					break;
					
				case MB_Registers.MB_ACTPIR:
					cbActPIR.setSelected((e.getRegs()[i] == 1));
					break;
					
				case MB_Registers.MB_ACTSRC:
					cbActSRC.setSelected((e.getRegs()[i] == 1));
					break;
				
				}
			}
			break;
						
		case Const_Modbus.READ_INPUT_DISCRETES:						
			for(int i = addr;i<(addr+nReg); i++){
				switch(i){
				case MB_Registers.MB_BTN1:
					ledBtn1.setLedOn((e.getRegs()[i] == 0));
					break;
								
				case MB_Registers.MB_BTN2:
					ledBtn2.setLedOn((e.getRegs()[i] == 0));
					break;
							
				case MB_Registers.MB_OPT:
					ledBtnOpt.setLedOn((e.getRegs()[i] == 1));
					break;
				
				case MB_Registers.MB_PIR:
					ledPIR.setLedOn((e.getRegs()[i] == 1));
					break;
				}
			}
			break;
		}				
	}

	@Override
	public void logVisualizer(String Text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActualize(boolean st) {
		stActualize = st;		
	}

	@Override
	public boolean getActualize() {
		
		return stActualize;
	}
}

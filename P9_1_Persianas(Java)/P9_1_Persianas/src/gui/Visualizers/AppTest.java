package gui.Visualizers;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JButton;

import Base_COM_Serie.MB_Registers;
import Comm.io.CommTransport;
import ModBus.Const_Modbus;
import ModBus.ModBus;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class AppTest extends JPanel implements Visualizer {
	private final 	String 				category;
	private final 	boolean				isCategory = false;
	@SuppressWarnings("unused")
	private final	CommTransport 		sn_Transport;
	
	//Banco de registros para mantener sincronizada la comunicación Modbus 
	private  		int 				Cregs[];
	private  		int 				Dregs[];
	
	public AppTest(String category, CommTransport sn_Transport) {
		
		super();
		
		this.category = category;
		this.sn_Transport = sn_Transport;
		
		setLayout(null);
		
		//Crea Banco de registros para mantener sincronizada la comunicación Modbus 
		Cregs = new int [MB_Registers.MB_O_COILS];
		Dregs = new int [MB_Registers.MB_I_REGS];
		
		JButton btnNewButton = new JButton("WRITE_COIL");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String val = "1";		//Valor a escribir en el registro --> ON
				String reg = "0";		//Registro ModBus a escribir
				String[] args = {"1", String.valueOf(Const_Modbus.WRITE_COIL), reg, val};
				
				//Configuramos la comunicación ModBus
				
				//Iniciamos Comunicación
				ModBus.InitComunication(args, sn_Transport, Cregs);
			}
		});
		btnNewButton.setBounds(88, 52, 111, 32);
		add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("READ_COILS");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {				
				String nCoils;
				String vReg;
				
				vReg = String.valueOf(MB_Registers.MB_RELE1);
				nCoils = String.valueOf(MB_Registers.MB_O_COILS);
				
				String[] args = {"1", String.valueOf(Const_Modbus.READ_COILS), vReg, nCoils};
				ModBus.InitComunication(args, sn_Transport, Dregs);		
			}
		});
		btnNewButton_1.setBounds(88, 95, 111, 32);
		add(btnNewButton_1);
		
		JButton btnReadinputdicrete = new JButton("READ_INPUT_DICRETE (0x02)");
		btnReadinputdicrete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String nCoils;
				String vReg;
				
				vReg = String.valueOf(MB_Registers.MB_BTN1);
				nCoils = String.valueOf(MB_Registers.MB_I_REGS);
				
				String[] args = {"1", String.valueOf(Const_Modbus.READ_INPUT_DISCRETES), vReg, nCoils};
				ModBus.InitComunication(args, sn_Transport, Dregs);	
			}
		});
		btnReadinputdicrete.setBounds(37, 138, 212, 32);
		add(btnReadinputdicrete);
		
		JButton btnLeerEstadoSrc = new JButton("LEER ESTADO SRC");
		btnLeerEstadoSrc.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				/*
				*  Lee estado Activado/desactivado photoresistor -- Sensor luminosidad 
				*/
				String 	address	= "1";
				String vReg = String.valueOf(MB_Registers.MB_ACTSRC);
				String nCoils = "1"; //String.valueOf(MB_Registers.MB_O_COILS);
				
				String[] iargs = {address, String.valueOf(Const_Modbus.READ_COILS), vReg, nCoils};
				ModBus.InitComunication(iargs, sn_Transport, Cregs);
							
				//ModBusEvent e = new ModBusEvent(Cregs);
				/*
				e.setDigital(true);
				e.set_Args(iargs);
							
				UpdateElements(e);
				*/
			}
		});
		btnLeerEstadoSrc.setBounds(71, 181, 145, 32);
		add(btnLeerEstadoSrc);
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getTitle() {
		return "** Test **";
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
	public void Actualize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logVisualizer(String Text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void CT_Opened() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActualize(boolean st) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getActualize() {
		// TODO Auto-generated method stub
		return false;
	}
}

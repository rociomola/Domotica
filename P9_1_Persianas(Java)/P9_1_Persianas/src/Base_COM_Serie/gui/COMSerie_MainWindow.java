package Base_COM_Serie.gui;

import gui.Panel.Console;
import gui.Visualizers.AppTest;
import gui.Visualizers.DomoBoardGui;
import gui.Visualizers.Visualizer;
import gui.Visualizers.Msg.VisualizerMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import Utilidades.ConfigUtilities;
import Base_COM_Serie.ConstantesApp;
import Base_COM_Serie.MB_Registers;
import Base_COM_Serie.Messages;
import Comm.io.CommTransport;
import Comm.io.ConnTransportAdaption;
import ModBus.Const_Modbus;
import ModBus.ModBus;
import Comm.Serial.gui.SerialConfig;
import Comm.Serial.io.SerialCommTransport;
//import javax.swing.JPopupMenu;
//import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.JRadioButtonMenuItem;

//import net.wimpi.modbus.Modbus;


public class COMSerie_MainWindow {
	private 	boolean 			doExitOnRequest = true;
	private 	Properties 			configApp = new Properties();
	private 	CommTransport 		sn_Transport;
	private 	JCheckBoxMenuItem 	ConsoleSet;
	private 	Console 			serialConsole;
	private 	boolean 			resized = false;
	private 	JMenu 				mnMenuSerie;
	protected 	JTabbedPane 		mainPanel;
	
	private JFrame window;
	private HashMap<String, JTabbedPane> categoryTable = new HashMap<String, JTabbedPane>();
	
	/* Categories for the tab pane */
	private static final String MAIN = "main";
	
	private ArrayList<Visualizer> 			visualizers;
	private ArrayList<JRadioButtonMenuItem>	practicaSel;
	
	public COMSerie_MainWindow() {
		ConfigUtilities.loadConfig(configApp, ConstantesApp.CONFIG_FILE);
		
		// Make sure we have nice window decorations
		JFrame.setDefaultLookAndFeelDecorated(true);

		Rectangle maxSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
								.getMaximumWindowBounds();
		
		window = new JFrame(ConstantesApp.WINDOW_TITLE+" (not connected)");
		window.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				resized = true;
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(resized){
					
					Rectangle rectangle = window.getContentPane().getBounds();
		        	configApp.setProperty("windowBounds",rectangle.x+", "+rectangle.y+", "+rectangle.width+", "+rectangle.height);
		    		ConfigUtilities.saveConfig(configApp, ConstantesApp.CONFIG_FILE);
		    		resized = false;
				}
			}
		});
		
		window.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {				
				serialConsoleLocation();	
			}
			@Override
			public void componentMoved(ComponentEvent arg0) {
				serialConsoleLocation();
			}			
		});
		
		
		window.setLocationByPlatform(true);
		if (maxSize != null) {
			window.setMaximizedBounds(maxSize);
		}
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}			
		});
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setToolTipText("");
		menuBar.setForeground(Color.WHITE);
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		menuBar.setMargin(new Insets(5, 5, 5, 5));
		menuBar.setBackground(Color.ORANGE);
		window.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu(Messages.ARCHIVO);
		menuBar.add(mnNewMenu);

		JMenuItem ExitItem = new JMenuItem(Messages.SALIR);
		ExitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		
		JMenuItem mntmPruebas = new JMenuItem("Pruebas");
		mntmPruebas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String title = VisualizerMessages.PRUEBAS;
				boolean exisPane = false;
				
				for(int n=0;n<mainPanel.getTabCount();n++)
				{			
					if(title.equals(mainPanel.getTitleAt(n))){
						mainPanel.remove(n);
						categoryTable.remove(title);
						exisPane = true;
					}			
				}
				
				if(!exisPane){
					@SuppressWarnings("serial")
					AppTest appTest = new AppTest(VisualizerMessages.PRUEBAS, sn_Transport){
						//@override
						public void logVisualizer(String Text){
							log(Text);
						}
					};
					addVisualizer(appTest);
				}
			}
		});
			
		mnNewMenu.add(mntmPruebas);
		mnNewMenu.addSeparator();
		mnNewMenu.add(ExitItem);		
		
		JMenu mnDomoboard = new JMenu("DomoBoard");
		mnDomoboard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String title = VisualizerMessages.DOMOBOARD;
				boolean exisPane = false;
				
				for(int n=0;n<mainPanel.getTabCount();n++)
				{			
					if(title.equals(mainPanel.getTitleAt(n))){
						//mainPanel.remove(n);
						//categoryTable.remove(title);
						exisPane = true;
					}			
				}
				
				if(!exisPane){

					@SuppressWarnings("serial")
					DomoBoardGui domoboardGui = new DomoBoardGui(VisualizerMessages.DOMOBOARD, "1", sn_Transport){
						//@override
						public void logVisualizer(String Text){
							log(Text);
						}
					};
					addVisualizer(domoboardGui);
				}
				else{
					for (int m = 0, n = visualizers.size(); m < n; m++) {
	        			if(visualizers.get(m).getCategory() == VisualizerMessages.DOMOBOARD){
	        				visualizers.get(m).setActualize(!visualizers.get(m).getActualize());
	        			}
	        		}
				}
			}
		});
		
		JMenu mnSeleccionarPrctica = new JMenu("Seleccionar Pr\u00E1ctica");
		menuBar.add(mnSeleccionarPrctica);
		
		JMenu mnPrctica = new JMenu("Pr\u00E1ctica 6");
		mnSeleccionarPrctica.add(mnPrctica);
		
		JRadioButtonMenuItem P6_Apartado1 = new JRadioButtonMenuItem("Apartado 1");		
		P6_Apartado1.addActionListener(Select_Practica);
		
		mnPrctica.add(P6_Apartado1);
		
		JRadioButtonMenuItem P6_Apartado2 = new JRadioButtonMenuItem("Apartado 2");
		P6_Apartado2.addActionListener(Select_Practica);
		mnPrctica.add(P6_Apartado2);
		
		JRadioButtonMenuItem P6_Apartado3 = new JRadioButtonMenuItem("Apartado 3");
		P6_Apartado3.addActionListener(Select_Practica);
		mnPrctica.add(P6_Apartado3);
		menuBar.add(mnDomoboard);
		
		//=======================================================
		//             Menú COMUNICACIONES
		//=======================================================
		JMenu mnNewMenu_1 = new JMenu(Messages.COMUNICACIONES);
		mnNewMenu_1.setForeground(Color.BLACK);
		mnNewMenu_1.setBackground(Color.BLACK);
		menuBar.add(mnNewMenu_1);
		
		setMenuSerie(mnNewMenu_1);
				
		ConsoleSet = new JCheckBoxMenuItem(Messages.CONSOLE);
		mnNewMenu_1.add(ConsoleSet);
		ConsoleSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				AbstractButton aButton = (AbstractButton) paramActionEvent.getSource();
		        boolean selected = aButton.getModel().isSelected();
		        
				//SerialConsole serialConsole = new SerialConsole("TEST");
		        if(selected){
		        	serialConsole = new Console();
		        	serialConsole.addActionListener(new ActionListener() {
		    			public void actionPerformed(ActionEvent e) {
		    				ConsoleMessage(e.getActionCommand());
		    			}
		    		});
		        	serialConsole.setVisible(true);
		        	
//		        	serialServer.serialConsole = serialConsole;
		        	
		        	serialConsoleLocation();
		        }
		        else{ 
		        	serialConsole.Close();
//		        	serialServer.serialConsole = null;
		        }
		        
		        configApp.setProperty(ConstantesApp.SERIALCONSOLE,String.valueOf(selected));
	    		ConfigUtilities.saveConfig(configApp, ConstantesApp.CONFIG_FILE);
			}
		});
		
		initSerialTransport(mnMenuSerie);
		window.getContentPane().setLayout(new BorderLayout(0, 0));
		//window.getContentPane().setLayout(null);
		
		mainPanel = new JTabbedPane(JTabbedPane.TOP);
		mainPanel.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		categoryTable.put(MAIN, mainPanel);

		window.getContentPane().add(mainPanel);		
			
		//initSerialTransport(mnMenuSerie);
	
		visualizers = new ArrayList<Visualizer>();
		practicaSel = new ArrayList<JRadioButtonMenuItem>();
		
		practicaSel.add(P6_Apartado1);
		practicaSel.add(P6_Apartado2);
		practicaSel.add(P6_Apartado3);
		
		JMenu mnPrctica_1 = new JMenu("Pr\u00E1ctica 7");
		mnSeleccionarPrctica.add(mnPrctica_1);
		
		JRadioButtonMenuItem rdbtnmntmInterruptorTemporizado = new JRadioButtonMenuItem("Interruptor Temporizado");
		rdbtnmntmInterruptorTemporizado.addActionListener(Select_Practica);
		mnPrctica_1.add(rdbtnmntmInterruptorTemporizado);
		practicaSel.add(rdbtnmntmInterruptorTemporizado);
		
		JRadioButtonMenuItem rdbtnmntmConfiguracinPir = new JRadioButtonMenuItem("Configuraci\u00F3n PIR");
		rdbtnmntmConfiguracinPir.addActionListener(Select_Practica);
		mnPrctica_1.add(rdbtnmntmConfiguracinPir);
		practicaSel.add(rdbtnmntmConfiguracinPir);
		
		JMenu mnMenu_Pract8 = new JMenu("Pr\u00E1ctica 8 - Sensores Anal\u00F3gicos");
		mnSeleccionarPrctica.add(mnMenu_Pract8);
		
		JRadioButtonMenuItem rdbtnmntmNewRadio_SRC = new JRadioButtonMenuItem("Interruptor SRC");
		rdbtnmntmNewRadio_SRC.addActionListener(Select_Practica);
		mnMenu_Pract8.add(rdbtnmntmNewRadio_SRC);
		practicaSel.add(rdbtnmntmNewRadio_SRC);
		
		JMenu mnPrctica_2 = new JMenu("Pr\u00E1ctica 9 - Persiana");
		mnSeleccionarPrctica.add(mnPrctica_2);
		
		JRadioButtonMenuItem NewRadioItem_Persianas_1 = new JRadioButtonMenuItem("1.- Control Persiana (BTN's)");
		NewRadioItem_Persianas_1.addActionListener(Select_Practica);
		mnPrctica_2.add(NewRadioItem_Persianas_1);
		practicaSel.add(NewRadioItem_Persianas_1);
		
		Modbus_Regular_Call();
		
	}
	
	private ActionListener Select_Practica = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			handle_Menu(arg0);
		}};
	
	protected void addVisualizer(Visualizer visualizer){
		String category = visualizer.getCategory();
		
		JTabbedPane pane = categoryTable.get(category);
		if (pane == null) {
			pane = new JTabbedPane();
			categoryTable.put(category, pane);
			if(visualizer.isCategory()){
				mainPanel.add(category, pane);
			}
			else{
				mainPanel.add(category, visualizer.getPanel());
			}
		}
		
		if(visualizer.isCategory())
			pane.add(visualizer.getTitle(), visualizer.getPanel());
		
		visualizers.add(visualizer);	
		
//		visualizer.getPanel().setBounds(pane.getBounds());
	}
	
	protected void setSystemMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				if (message == null) {
					window.setTitle(ConstantesApp.WINDOW_TITLE);
				} else {
					window.setTitle(ConstantesApp.WINDOW_TITLE + " (" + message + ')');
				}
			}
		});
	}
	
	private void log(String Msg){
		if((serialConsole == null)||(!serialConsole.isVisible())){
			System.out.println(Msg);
		}
		else
			serialConsole.log(Msg);
	}
	
	private void exit() {
		if (doExitOnRequest) {
			//stop();
			System.exit(0);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	private void setMenuSerie(JMenu mnMenu){
		mnMenuSerie = new JMenu(Messages.SERIE);
		mnMenuSerie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				log(Messages.LOOKPORTS);
				lookfor_Ports();
			}
		});
		mnMenu.add(mnMenuSerie);
	
		mnMenu.addSeparator();
	}
	
	private void lookfor_Ports(){
		if(((SerialCommTransport)sn_Transport).SetMenu_SerialPorts(mnMenuSerie)>0){
			mnMenuSerie.addSeparator();
			
			JMenuItem mntmNewMenuItem = new JMenuItem(Messages.OPT_SERIE);
			mnMenuSerie.add(mntmNewMenuItem);
			mntmNewMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent paramActionEvent) {
					SerialConfig frame = new SerialConfig(configApp, ConstantesApp.CONFIG_FILE);
			        frame.setVisible(true); //necessary as of 1.3  
				}
			});
		}
	}
	
	private void serialConsoleLocation(){
		if(serialConsole != null){
			if(serialConsole.isVisible()){ 
				Dimension windowSize = window.getSize();
				serialConsole.setSize(serialConsole.getWidth(), windowSize.height);
    	
				Point WinLocation = window.getLocation(); 
    	
				serialConsole.setLocation(WinLocation.x + windowSize.width, WinLocation.y);
			}
		}
	}
	
	private void ConsoleMessage(final String message){
		if(message == "Close"){
//			serialServer.serialConsole = null;
			ConsoleSet.setState(false);
		}
	}
	
	private void Modbus_Regular_Call(){
		//Tiempo en milisegundos
		Timer timer = new Timer (ConstantesApp.TIME_REFRESH_MODBUS, new ActionListener ()
		{
			public void actionPerformed(ActionEvent e)
		    {	
				if(sn_Transport.isConnected()){
					
					//System.out.println("Timer");
					
					for (int i = 0, n = visualizers.size(); i < n; i++) {
	        			visualizers.get(i).Actualize();
	        		}
				}
		    }
		});
		
		timer.start();
	}
	
	private void initSerialTransport(JMenu mnMenu){
		
		sn_Transport = new SerialCommTransport(ConstantesApp.SERIALCONNECTION);
		sn_Transport.loadConfig(configApp);
		//sn_Transport.
		sn_Transport.addTransportListener(new ConnTransportAdaption(){
			@Override
			public void logTransport(String message){
				log(message);
			}
			
			@Override 
			public void SystemMessage(String message){
				setSystemMessage(message);
			}
			
			@Override
			public void CT_Opened(String message){
				setSystemMessage(message);
				
				//Leer Configuración de práctica
				String 	vSel 	= "1"; //Leemos un solo registro //Integer.toString(MB_Registers.SELPRACT[i]);
				String 	vReg 	= String.valueOf(MB_Registers.MB_PRACT);
				String 	address	= "1";	//BroadCast
				
				int		Aregs[] = new int [MB_Registers.MB_AREGS];
				
				String[] args = {address, String.valueOf(Const_Modbus.READ_MULTIPLE_REGISTERS), vReg, vSel};
				ModBus.InitComunication(args, sn_Transport, Aregs);
				
				int i = 0;
				while(i < MB_Registers.SELPRACT.length){
					if(MB_Registers.SELPRACT[i] == Aregs[0]){
						practicaSel.get(i).setSelected(true);
						break;
					}	
					i++;
				}	
				
				for (int m = 0, n = visualizers.size(); m < n; m++) {
        			visualizers.get(m).CT_Opened();
        		}
			}
		});
		
		lookfor_Ports();				
	}
	
	public void start() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window.setVisible(true);
				
				String s = configApp.getProperty("windowBounds");
				if(s != null){
					String[] r = s.split(", ");
					window.setBounds(new Rectangle(
			                        Integer.parseInt(r[0]),
			                        Integer.parseInt(r[1]),
			                        Integer.parseInt(r[2]),
			                        Integer.parseInt(r[3])
			                       )
			        );
				} else window.setSize(new Dimension(799, 535)); 
				
				//Centrar ventana.
				//Centrar ventana.
				int scWidth = 0;
				//int scHeigth = 0;
				if(Boolean.valueOf(configApp.getProperty(ConstantesApp.SERIALCONSOLE))){
					ConsoleSet.doClick();
					scWidth = serialConsole.getWidth();
					//scHeigth = serialConsole.getHeight();
				}
				
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		        int X = (screenSize.width - (window.getWidth()+scWidth))/2;
		        int Y = (screenSize.height - window.getHeight())/2;
		        window.setLocation(X, Y);
			}
		});
	}
	
	/*
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	*/
	
	/*
	 * Maneja menú para seleccionar la práctica con la que trabajamos
	 */
	private void handle_Menu(ActionEvent arg0){
		JRadioButtonMenuItem Source = (JRadioButtonMenuItem)arg0.getSource();
		
		for (int i = 0, n = practicaSel.size(); i < n; i++){
			
			if(!practicaSel.get(i).equals(Source)){
				practicaSel.get(i).setSelected(false);
			}else{
				String 	vSel 	= Integer.toString(MB_Registers.SELPRACT[i]);
				String 	vReg 	= String.valueOf(MB_Registers.MB_PRACT);
				String 	address	= "1";	//BroadCast
				
				int		Aregs[] = new int [MB_Registers.MB_AREGS];
				
				String[] args = {address, String.valueOf(Const_Modbus.WRITE_SINGLE_REGISTER), vReg, vSel};
				ModBus.InitComunication(args, sn_Transport, Aregs);
			}
		}
	}
}

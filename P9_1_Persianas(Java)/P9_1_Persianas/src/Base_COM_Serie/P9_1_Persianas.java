package Base_COM_Serie;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Base_COM_Serie.gui.COMSerie_MainWindow;


//import Utilidades.FileUtilities;

public class P9_1_Persianas
{
	private static void createAndShowGUI() 
	{
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        COMSerie_MainWindow MainWindowGui = new COMSerie_MainWindow();
        
        MainWindowGui.start();
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Auto-generated method stub
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createAndShowGUI();
			}
		});
		
		say(ConstantesApp.WINDOW_TITLE+"Inicializado");
	}	
	
	
	public static void say(String msg) 
	{
		for (int i = 0; i < 1; i++) 
		{
			System.out.println(msg);
		}
	}
}

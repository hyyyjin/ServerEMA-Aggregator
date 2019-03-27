package com.mir.ems.main;

import java.awt.EventQueue;

import com.mir.ems.GUI.Initial;
import com.mir.ems.GUI.MainFrame;
import com.mir.ems.globalVar.global;


public class EmaMainClass {
	public Initial initial;
	MainFrame mainFrame;

	public static void main(String[] args) {
		
		EmaMainClass main = new EmaMainClass();
		main.initial = new Initial();
		main.initial.setMain(main);
		
					
	}

	public void showFrameTest() {
		initial.dispose();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connection connection = new Connection(global.	getProtocol_type_global());
					if (connection.connection_Status) {
						MainFrame frame = new MainFrame();
						frame.setVisible(true);
					} else {	
						EmaMainClass main = new EmaMainClass();	
						main.initial = new Initial();
						main.initial.setMain(main);
					}
				} 
				
				catch (Exception e) {	
					e.printStackTrace();
				}
			}
		});

	}
}
	
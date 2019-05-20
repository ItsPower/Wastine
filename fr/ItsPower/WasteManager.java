package fr.ItsPower;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import fr.ItsPower.Pages.Chargement;
import fr.ItsPower.Pages.Eleve;
import fr.ItsPower.Pages.Principale;
import fr.ItsPower.Types.WasteEleve;

public class WasteManager {
	
	public static JFrame screen;
	
	public void init() {
		screen = new JFrame("Wastine " + Main.getVersion());
		Chargement panel = new Chargement();
		System.out.println("Affichage du screen: Chargement");
		
		Timer timer;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			private int var = 0;
			@Override
			public void run() {
				panel.setX(var);
				panel.repaint();
				var += 5;
				if(var == 100) {
					System.out.println("Chargement des utilitaires.");
					Main.getUtilsInstance().init();
				}
				if(var == 200) {
					Utils.SQL.startsql();
				}
				if(var > 400) {
					System.out.println("Chargement terminé.");
					cancel();
					pagePrincipale();
					try {
						startSmsReceiving();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}, 0, 5);
		
		screen.setUndecorated(true);
		screen.setExtendedState(JFrame.MAXIMIZED_BOTH);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setVisible(true);
		
		screen.getContentPane().add(panel);
	}
	
	public void pagePrincipale() {
		screen.getContentPane().removeAll();
		screen.getContentPane().add(new Principale());
		screen.invalidate();
		screen.revalidate();
	}
	
	public void pageEleve(WasteEleve eleve) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				screen.getContentPane().removeAll();
				screen.getContentPane().add(new Eleve(eleve));
				screen.invalidate();
				screen.revalidate();
			}
		});
	}

	private void startSmsReceiving() throws IOException {
		Timer timer;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			File homedir = new File(System.getProperty("user.home"));
			File fileToRead = new File(homedir, "scripts/msglogs.txt");
			@SuppressWarnings("unused")
			long lines = Files.lines(Paths.get(fileToRead.toURI()), Charset.defaultCharset()).count();
			long fileSize = fileToRead.length();
			@Override
			public void run() {
				if(fileSize != fileToRead.length()) {
					fileSize = fileToRead.length();
					try {
						lines = Files.lines(Paths.get(fileToRead.toURI()), Charset.defaultCharset()).count();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					System.out.println("Nouveau message recu: ");
					try(BufferedReader input = new BufferedReader(new FileReader(fileToRead.getPath()))) {
					    String last = "", line;
	
					    while ((line = input.readLine()) != null) { 
					        last = line;
					    }
					    
					    System.out.println(last);
					    
					    
					    
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}, 5, 1);
	}
}
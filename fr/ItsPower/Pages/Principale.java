package fr.ItsPower.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.ItsPower.Main;
import fr.ItsPower.Utils;
import fr.ItsPower.WasteManager;
import fr.ItsPower.Types.WasteEleve;
import fr.ItsPower.rfid.RaspRC522;

@SuppressWarnings("unused")
public class Principale extends JPanel {
	
	private static final long serialVersionUID = 1L; {
		System.out.println("Affichage du screen: Principal");

		this.setLayout(new BorderLayout());
		JLabel back = new JLabel(new ImageIcon(this.getClass().getResource("/fond.png")));
		this.add(back);
		back.setLayout(new BorderLayout());
		
		JLabel mid = new JLabel("Veuillez passer votre carte   ", JLabel.CENTER);

		mid.setFont(Utils.Fonts.getFont("Quotus.ttf", 90f));
		mid.setForeground(new Color(0,70,0));
		
		back.add(mid, BorderLayout.CENTER);
		
		Timer timer;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			RaspRC522 rasp = new RaspRC522();
			@Override
			public void run() {
				byte tagid[] = new byte[5];
				int back_bits[] = new int[1];
				
				if(rasp.Request(RaspRC522.PICC_REQIDL, back_bits) == RaspRC522.MI_OK) {
					System.out.println("Screen Principal: CarteEvent triggered.");
					if(rasp.AntiColl(tagid) == RaspRC522.MI_OK) {
						rasp.Select_Tag(tagid);
						
						System.arraycopy(tagid, 0, tagid, 0, 5);
						String uid = tagid[0]+","+tagid[1]+","+tagid[2]+","+tagid[3]+","+ tagid[4];
						
						mid.setFont(new Font("serif", Font.BOLD, 50));
						mid.setForeground(new Color(0,200,0));
						mid.setText(uid);
						
						System.out.println("Screen Principal: Carte -> "+uid);
						
						if(Utils.SQL.isOnDB(uid)) {
							WasteEleve eleve = Utils.SQL.getEleve(uid);
							
							if(eleve != null) {
								cancel();
								Main.getWastInstance().pageEleve(eleve);
							}
							
						} else {
							mid.setText("Erreur lors de l'authentification.");
							mid.setForeground(Color.RED);
						}
						
					} else {
						mid.setText("Veuillez repasser votre carte.  ");
						mid.setForeground(Color.RED);
					}
					
				} else {
					mid.setFont(Utils.Fonts.getFont("Quotus.ttf", 90f));
					mid.setForeground(new Color(0,70,0));
					
					if(mid.getText().equals("Veuillez passer votre carte   ")) {
						mid.setText("Veuillez passer votre carte.  ");
						
					} else if(mid.getText().equals("Veuillez passer votre carte.  ")) {
						mid.setText("Veuillez passer votre carte.. ");
						
					} else if(mid.getText().equals("Veuillez passer votre carte.. ")) {
						mid.setText("Veuillez passer votre carte...");
						
					} else {
						mid.setText("Veuillez passer votre carte   ");
					}
					
				}
			}
		}, 0, 500);
	}
}

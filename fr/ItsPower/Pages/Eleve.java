package fr.ItsPower.Pages;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pi4j.io.gpio.PinState;

import fr.ItsPower.Main;
import fr.ItsPower.Utils;
import fr.ItsPower.Types.WasteEleve;

public class Eleve extends JPanel {

	private String Nom = "t";
	private String Prenom;
	private String Classe;
	private String UID;
	private int Solde;
	private int Fidelite;
	private int selected = 0;
	
	public Eleve(WasteEleve e) {
		System.out.println("NOM="+e.getNom()+" PRENOM="+e.getPrenom()+" SOLDE="+e.getSolde()+" CLASSE="+e.getClasse()+" FIDE="+e.getFid());
		Nom = e.getNom();
		Prenom = e.getPrenom();
		Classe = e.getClasse();
		Solde = e.getSolde();
		Fidelite = e.getFid();
		UID = e.getUID();
	}

	private static final long serialVersionUID = 2L; {
		System.out.println("Affichage du screen: Eleve");
		setLayout(new BorderLayout());
		JLabel back = new JLabel(new ImageIcon(this.getClass().getResource("/fond.png")));
		add(back);
		Timer timer;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(Main.getUtilsInstance().getExitButton().getState().equals(PinState.LOW)) {
					System.out.println("Screen Eleve: QuitButton triggered.");
					selected = 4;
					repaint();
					cancel();
					Main.getWastInstance().pagePrincipale();
				}
				
				if(selected == 0) {
					if(Main.getUtilsInstance().getFroidButton().getState().equals(PinState.LOW)) {
						System.out.println("Screen Eleve: FroidButton triggered.");
						selected = 3;
						repaint();
						try {
							Utils.SQL.updateRepas(UID, new SimpleDateFormat("dd-MM-yyyy").format(new Date()), 3);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						new Timer().schedule( 
								new TimerTask() {
								@Override
								public void run() {
									selected = 0;
									repaint();
							 	}
							}, 1500);
					}
					
					if(Main.getUtilsInstance().getChaud2Button().getState().equals(PinState.LOW)) {
						System.out.println("Screen Eleve: Chaud2Button triggered.");
						selected = 2;
						repaint();
						try {
							Utils.SQL.updateRepas(UID, new SimpleDateFormat("dd-MM-yyyy").format(new Date()), 2);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						new Timer().schedule( 
								new TimerTask() {
								@Override
								public void run() {
									selected = 0;
									repaint();
							 	}
							}, 1500);
					}
					
					if(Main.getUtilsInstance().getChaud1Button().getState().equals(PinState.LOW)) {
						System.out.println("Screen Eleve: Chaud1Button triggered.");
						selected = 1;
						repaint();
						try {
							Utils.SQL.updateRepas(UID, new SimpleDateFormat("dd-MM-yyyy").format(new Date()), 1);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						new Timer().schedule( 
								new TimerTask() {
								@Override
								public void run() {
									selected = 0;
									repaint();
							 	}
							}, 1500);
					}
				}
				
			}
		}, 0, 50);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		 
		g2.setPaint(new GradientPaint((this.getWidth()/2)-(800/2), 50, new Color(152,251,152), (this.getWidth()/2)-(800/2)+800, 250, new Color(60,179,113)));
		g2.fillRect((this.getWidth()/2)-(800/2), 50, 800, 200);
		
		g2.setStroke(new BasicStroke(10));
		g2.setColor(new Color(34,139,34));
		 
		g2.drawRect((this.getWidth()/2)-(800/2), 50, 800, 200);
		g2.setStroke(new BasicStroke(14));
	
		g2.drawRect(-10, 350, 650, 200);
		g2.drawRect(-10, 650, 650, 200);
	
		g2.drawRect(1275, 350, 650, 200);
		g2.drawRect(1275, 650, 650, 200);

		if(selected == 1) {
			g2.setColor(new Color(50,255,80));
			g2.fillRect(-10, 350, 650, 200);
		} else {
			g2.setColor(new Color(60,179,110));
			g2.fillRect(-10, 350, 650, 200);
		}
		
		if(selected == 2) {
			g2.setColor(new Color(50,255,80));
			g2.fillRect(-10, 650, 650, 200);
			
		} else {
			g2.setColor(new Color(60,179,110));
			g2.fillRect(-10, 650, 650, 200);
		}
		
		if(selected == 3) {
			g2.setColor(new Color(50,255,80));
			g2.fillRect(1275, 350, 650, 200);
			
		} else {
			g2.setColor(new Color(60,179,110));
			g2.fillRect(1275, 350, 650, 200);
		}
		
		if(selected == 4) {
			g2.setColor(new Color(50,255,80));
			g2.fillRect(1275, 650, 650, 200);
			
		} else {
			g2.setColor(new Color(60,179,110));
			g2.fillRect(1275, 650, 650, 200);
		}
	
		 
		g2.setFont(Utils.Fonts.getFont("Roboto.ttf", 52f));
		g2.setColor(new Color(0,70,0));
		
		g2.drawString(Nom, 580, 125);
		g2.drawString(Prenom, 580, 210);
		 
		g2.drawString(Classe, 950, 130);
		g2.drawString("Solde: " + Solde + " Fidélité: " + Fidelite, 875, 210);
	
		g2.setFont(Utils.Fonts.getFont("Roboto.ttf", 80f));
	
		g2.drawString("Cordon bleu / Frites", 50, 475);
		g2.drawString("Nuggets de poisson / Pates", 50, 775);
		
		g2.drawString("Sandwich Poulet", 1400, 475);
		g2.drawString("Déconnexion", 1400, 775);
	}
}

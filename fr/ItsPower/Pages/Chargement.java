package fr.ItsPower.Pages;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.ItsPower.Utils;

public class Chargement extends JPanel {
	
	private int x;
	 
	public void setX(int nb) {
		this.x = nb;
	}
		
	private static final long serialVersionUID = 4L; {
		setLayout(new BorderLayout());
		JLabel back = new JLabel(new ImageIcon(this.getClass().getResource("/fond.png")));
		add(back);
		back.setLayout(new BorderLayout());
		
		JLabel credits = new JLabel("Réalisé par: BARBIER Benoit, DELPLACE loic, POTIER Quentin et VDV Lucas"+System.lineSeparator(), JLabel.CENTER);
		
		credits.setFont(Utils.Fonts.getFont("Quotus.ttf", 25f));
		credits.setForeground(new Color(0,70,0));
		
		back.add(credits, BorderLayout.SOUTH);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		 
		ImageIcon icone = new ImageIcon(this.getClass().getResource("/logo.png"));
		g.drawImage(icone.getImage(), (this.getWidth()/2)-(400/2), (int) ((int) this.getHeight()*0.1), 400, 400, null);
		
		g2.setStroke(new BasicStroke(3));
		g2.drawRect((this.getWidth()/2)-(400/2), 600, 400, 100);
		 
		g.setColor(Color.GREEN);
		g.fillRect((this.getWidth()/2)-(400/2), 600, 400, 100);
		 
		g.setColor(new Color(0,70,0));
		g.fillRect((this.getWidth()/2)-(400/2), 600, this.x, 100);
	}
}

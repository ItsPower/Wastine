package fr.ItsPower;

import java.awt.Font;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

import fr.ItsPower.Types.WasteEleve;

public class Utils {
	
	private GpioController gpio = null;

	//private GpioPinDigitalOutput ledVerte;
	private GpioPinDigitalInput boutton22;
	private GpioPinDigitalInput boutton23;
	private GpioPinDigitalInput boutton24;
	private GpioPinDigitalInput boutton25;
	
	public void init() {
		gpio = GpioFactory.getInstance();
		//ledVerte = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, PinState.LOW);
		boutton25 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, PinPullResistance.PULL_UP);
		boutton24 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_24, PinPullResistance.PULL_UP);
		boutton23 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_23, PinPullResistance.PULL_UP);
		boutton22 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, PinPullResistance.PULL_UP);
	}

	/*public GpioPinDigitalOutput getGreenLed() {
		return ledVerte;
	}*/

	public GpioPinDigitalInput getExitButton() {
		return boutton25;
	}

	public GpioPinDigitalInput getChaud1Button() {
		return boutton24;
	}

	public GpioPinDigitalInput getChaud2Button() {
		return boutton23;
	}

	public GpioPinDigitalInput getFroidButton() {
		return boutton22;
	}
	
	public GpioController getControl() {
		return gpio;
	}
	
	public static class Fonts {

		  private static String[] names = { "Quotus.ttf", "Roboto.ttf" };

		  private static Map<String, Font> cache = new ConcurrentHashMap<String, Font>(names.length);
		  static {
			for (String name : names) {
			  cache.put(name, getFont(name, 40));
			}
		  }

		  public static Font getFont(String name, float size) {
			Font font = null;
			if (cache != null) {
			  if ((font = cache.get(name)) != null) {
				return font;
			  }
			}
			String fName = "/fonts/" + name;
			try {
			  InputStream is = Fonts.class.getResourceAsStream(fName);
			  font = Font.createFont(Font.TRUETYPE_FONT, is);
			  font = font.deriveFont(size);
			} catch (Exception ex) {
			  ex.printStackTrace();
			  font = new Font("serif", Font.PLAIN, 80);
			}
			return font;
		  }
	}
	
	public static class SQL {
		private static String HOST	  = "localhost";
		private static String PORT	  = "3306";
		private static String DATABASE  = "Wastine";
		private static String USER	  = "WastineAdmin";
		private static String PASS	  = "AqWzSxEdC";
		
		public static Connection CONN;
		
		public static boolean startsql() {
			if (!isConnected()) {
				System.out.println("Connexion à la MySQL...");
				try {
					CONN = DriverManager.getConnection("jdbc:mariadb://"+HOST+":"+PORT+"/"+DATABASE, USER, PASS);
					System.out.println("Connexion à la MySQL réussie.");
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Connexion à la MySQL non réussie.");
				}
			}
			return false;
		}
		
		public static void stopsql() {
			if (isConnected()) {
				try {
					CONN.close();
					System.out.println("Connexion à la MySQL terminée.");
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Connexion à la MySQL à terminer buggée.");
				}
			}
		}
		
		public static boolean isConnected() {
			return CONN != null;
		}
		
		public static PreparedStatement getStatement(String sql) {
			if (isConnected()) {
			  try {
				return CONN.prepareStatement(sql);
			  } catch (SQLException e) {
				e.printStackTrace();
			  }
			}
			return null;
		  }

		public static boolean isOnDB(String uid) {
			try {
			  PreparedStatement ps = getStatement("SELECT * FROM Eleves WHERE UID= ?");
			  ps.setString(1, uid);
			  ResultSet rs = ps.executeQuery();
			  boolean user = rs.next();
			  rs.close();
			  return user;
			  
			} catch (Exception ex) {
			  ex.printStackTrace();
			}
			return false;
		  }
		
		private static boolean repasCheck(String uid, String date) {
			try {
			  PreparedStatement ps = getStatement("SELECT * FROM Choix WHERE UID = ? AND DATE = ?");
			  ps.setString(1, uid);
			  ps.setString(2, date);
			  ResultSet rs = ps.executeQuery();
			  boolean user = rs.next();
			  rs.close();
			  return user;
			  
			} catch (Exception ex) {
			  ex.printStackTrace();
			}
			return false;
		  }
		
		public static void updateRepas(String uid, String date, int idrepas) throws SQLException {
			if(repasCheck(uid, date)) {
				  PreparedStatement ps = getStatement("UPDATE Choix SET DATE = ?, CHOIX = ? WHERE UID = ?");
				  ps.setString(1, date);
				  ps.setInt(2, idrepas);
				  ps.setString(3, uid);
				  ps.executeUpdate();
			  } else {
				  PreparedStatement ps = getStatement("INSERT INTO Choix (DATE, UID, CHOIX) VALUES (?,?,?)");
				  ps.setString(1, date);
				  ps.setString(2, uid);
				  ps.setInt(3, idrepas);
				  ps.executeUpdate();
			  }
				
		}
		
		public static WasteEleve getEleve(String uid) {
			if(isOnDB(uid)) {
				try {
				  PreparedStatement ps = getStatement("SELECT * FROM Eleves WHERE UID = ?");
				  ps.setString(1, uid);
				  ResultSet rs = ps.executeQuery();
				  if(rs.next()) {
					  return new WasteEleve(uid, rs.getInt("SOLDE"), rs.getString("NOM"), rs.getString("PRENOM"), rs.getString("CLASSE"), rs.getInt("FID"));
				  
				  }
				  ps.close();
				} catch (Exception ex) {
				  ex.printStackTrace();
				}
			}
			return null;
		  }
	}
	
}

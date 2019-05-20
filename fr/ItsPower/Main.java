package fr.ItsPower;

public class Main {
	
	private static WasteManager  Wasteinstance = null;
	private static Utils		 UtilsInstance = null;
	private static String		 version  = "v0.1";
	
	public static void main(String[] args) {
		if (args.length == 1) {
			  if(args[0].equalsIgnoreCase("borne")) {
					System.out.println("Wastine started!");
					Wasteinstance = new WasteManager();
					Wasteinstance.init();
			  } else if(args[0].equalsIgnoreCase("cuisine")) {
				  
			  } else {
				  System.out.println("Votre argument de démarrage est incorrect. [ Borne / Cuisine ]");
			  }
		  } else {
			  System.out.println("Veuillez ajouter un argument spécifique à votre périphérique. [ Borne / Cuisine ]");
		  }
	}
	
	public static WasteManager getWastInstance() {
		if (Wasteinstance == null) {
			Wasteinstance = new WasteManager(); 
		}
		return Wasteinstance; 
	}
	
	public static Utils getUtilsInstance() {
		if (UtilsInstance == null) { 
			UtilsInstance = new Utils(); 
		}
		return UtilsInstance; 
	}
	
	public static String getVersion() {
		return version;
	}
}
package fr.ItsPower.Types;

public class WasteEleve {

	private String UID;
	private int SOLDE;
	private String NOM;
	private String PRENOM;
	private String CLASSE;
	private int FID;
	
	public WasteEleve(String var1, int var2, String var3, String var4, String var5, int var6) {
		UID = var1;
		SOLDE = var2;
		NOM = var3;
		PRENOM = var4;
		CLASSE = var5;
		FID = var6;
	}

	public String getUID() {
		return UID;
	}
	public int getSolde() {
		return SOLDE;
	}
	public String getNom() {
		return NOM;
	}
	public String getPrenom() {
		return PRENOM;
	}
	public String getClasse() {
		return CLASSE;
	}
	public int getFid() {
		return FID;
	}
}

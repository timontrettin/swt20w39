package pharmacy.catalog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Entity
public class Medicine extends Product {

	public static enum PrescriptionType {
		PRESONLY, WITHOUTPRES;
	}
	public static enum IngredientType {
		LABOR, SHOP, BOTH, MIXTURE;
	}
	public static enum MedicineType {
		CAPSULE, TABLET, OINTMENT, LIQUID, POWDER;
	}

	private String id, image, usage; // Identifikationsnummer, Name des Medikaments, Bild zum Medikament, Benutzt für ...
	private int size; // wie viele vorrätig sein sollen, Packungsgröße 
	//private Money price;
	private LocalDate bbd; // Mindesthaltbarkeitsdatum und Listenlänge sagt wie viele Medikamente da sind
	private ArrayList<Medicine> ingredients; // Falls es eine Mixtur unseres Labors ist muss etwas in der Liste stehen
	private PrescriptionType presType; // Brauch man ein Rezept oder nicht
	private IngredientType ingType; // wofür wird es verwendet
	private MedicineType medType; // definiert welche Form das Medikament hat und was die 'size' bedeutet (Anzahl, ml, g)

	@SuppressWarnings({ "unused", "deprecation" })
	private Medicine() {}

	public Medicine(String id, String name, String image, String usage, int size, Money price, LocalDate bbd,
			ArrayList<Medicine> ingredients, PrescriptionType presType, IngredientType ingType, MedicineType medType) {
		
		super(name, price);
		
		this.id = id;
		//this.name = name;
		this.image = image;
		this.usage = usage;
		this.size = size;
		this.bbd = bbd;
		//this.price = price;
		this.ingredients = ingredients;
		this.presType = presType;
		this.ingType = ingType;
		this.medType = medType;
	}
	// getter Funktionen für alle Variablen
	public String getID() {
		return id;
	}
	public String getImage() {
		return image;
	}
	public String getUsage() {
		return usage;
	}
	public int getSize() {
		return size;
	}
	public String getPackageSize() {
		if (medType.equals(MedicineType.CAPSULE) || medType.equals(MedicineType.TABLET))
			return size + " Stück";
		else if(medType.equals(MedicineType.OINTMENT) || medType.equals(MedicineType.LIQUID))
			return size + " ml";
		else return size + " g";
	}
	public LocalDate getBBD() {
		return bbd;
	}
	public ArrayList<Medicine> getIngredients() {
		return ingredients;
	}
	public String getPresType() {
		if (presType.equals(PrescriptionType.PRESONLY))
			return "Verschreibungspflichtig";
		else return "Frei Verkäuflich";
		
	}
	public IngredientType getIngType() {
		return ingType;
	}
	public String getMedType() {
		if (medType.equals(MedicineType.CAPSULE))
			return "Kapsel";
		else if(medType.equals(MedicineType.TABLET))
			return "Tablette";
		else if(medType.equals(MedicineType.OINTMENT))
			return "Salbe";
		else if(medType.equals(MedicineType.LIQUID))
			return "Flüssigkeit";
		else return "Pulver";
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
}
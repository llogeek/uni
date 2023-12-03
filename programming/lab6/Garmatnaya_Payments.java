package garmatnaya_z6v1;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

public class Garmatnaya_Payments implements Serializable {
	public static class ArgException extends Exception {
		private static final long serialVersionUID = 1L;
		ArgException(String arg) {
			super(arg); 
		}
	}
	private static final long serialVersionUID = 1L;
protected String name;
public String GetName() {
	return this.name;
}
public String displayCurrency(String string){
	@SuppressWarnings("removal")
	Double currency = new Double(string);
    NumberFormat currencyFormatter;
    String currencyOut;

    currencyFormatter = NumberFormat.getCurrencyInstance(Garmatnaya_AppLocale.get());
    currencyOut = currencyFormatter.format(currency);
	return currencyOut;
 }
	public final Date creationDate = new Date();
	public String getCreationDate() {
		DateFormat dateFormatter = DateFormat.getDateTimeInstance(
				DateFormat.DEFAULT, DateFormat.DEFAULT, Garmatnaya_AppLocale.get());
	    String dateOut = dateFormatter.format(creationDate);
		return dateOut;
	}
public enum type {Client, CreditCard, BankAccount, Order, Administrator}
private type typeOf;
public type getType() {
	return this.typeOf;
}

protected Garmatnaya_Payments(String name, type t) throws ArgException{
	 if (name == null || name.trim().length() == 0) throw new ArgException("NULL string");
	this.name = name;
	this.typeOf = t;
}
public Garmatnaya_Payments() {}
public String toString() {
	if (this.typeOf == Garmatnaya_Payments.type.Administrator) {
		return new String("Name: " + this.GetName() + "|| Type: " + this.getType().toString() + "|| " + Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.creation ) + ": " + getCreationDate());
	}
	else if (this.typeOf == Garmatnaya_Payments.type.BankAccount) {
		return new String("Name: " + this.GetName()+ "|| Type: " + this.getType().toString() + "|| Amount of money: " + displayCurrency(String.valueOf(((Garmatnaya_BankAccount)this).GetAmount())));
	}
	else if (this.typeOf == Garmatnaya_Payments.type.Client) {
		return new String("Name: " + this.GetName() + "|| Type: " + this.getType().toString() + "|| Credit Card: " + ((Garmatnaya_Client)this).GetCreditCard().toString()+"|| " + Garmatnaya_AppLocale.getString( Garmatnaya_AppLocale.creation ) + ": " + getCreationDate());
	}
	else if (this.typeOf == Garmatnaya_Payments.type.CreditCard) {
		return new String("Name: " + this.GetName()+ "|| Type: " + this.getType().toString() + "|| Max Credit: "  + displayCurrency(((Garmatnaya_CreditCard)this).GetMaxCredit()) + "|| Status " + ((Garmatnaya_CreditCard)this).GetStatusCard() + "|| Bank account " + ((Garmatnaya_CreditCard)this).GetAccount().toString());
	}
	else {
		return new String("Name: " + this.GetName() + "|| Type: " + this.getType().toString() + "|| Price: " + displayCurrency(((Garmatnaya_Order)this).GetPrice()) + "|| " + Garmatnaya_AppLocale.getString( Garmatnaya_AppLocale.creation ) + ": " + getCreationDate());
	}
}

}

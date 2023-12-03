package garmatnaya_z6v1;

import java.io.Serializable;
public class Garmatnaya_Administrator extends Garmatnaya_Payments implements Serializable {
	private static final long serialVersionUID = 1L;
	public static class ArgException extends Exception {
		private static final long serialVersionUID = 1L;
		ArgException(String arg) {
			super(arg); 
		}
	}
public Garmatnaya_Administrator(String name) throws garmatnaya_z6v1.Garmatnaya_Payments.ArgException {
	super(name, Garmatnaya_Payments.type.Administrator);
}
public Garmatnaya_Administrator() {}
public void BlockCreditCard(Garmatnaya_CreditCard card) throws ArgException {
	if (card.account.amount < card.MaxCredit*(-1)) {
		if (card.statusCard.equals("Blocked")) {
			throw new ArgException("It is impossible to perfom the action! The card is blocked!");
		}
		else if (card.statusCard.equals("Valid")) {
			card.statusCard = "Blocked";
			System.out.println("Card of " + card.GetName() + " is blocked by administrator.");
		}
	}
}
}

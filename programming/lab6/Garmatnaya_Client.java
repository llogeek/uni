package garmatnaya_z6v1;
import java.io.Serializable;

public class Garmatnaya_Client extends Garmatnaya_Payments implements Serializable {
	private static final long serialVersionUID = 1L;
	public static class ArgException extends Exception {
		private static final long serialVersionUID = 1L;
		ArgException(String arg) {
			super(arg); 
		}
	}
public Garmatnaya_CreditCard card;
public Garmatnaya_CreditCard GetCreditCard() {
	return this.card;
}
public Garmatnaya_Client(String name) throws garmatnaya_z6v1.Garmatnaya_Payments.ArgException {
	super(name, Garmatnaya_Payments.type.Client);
	this.card = new Garmatnaya_CreditCard(name);
}
public Garmatnaya_Client(String name, Garmatnaya_CreditCard card) throws garmatnaya_z6v1.Garmatnaya_Payments.ArgException {
    super(name, Garmatnaya_Payments.type.Client);
    this.card = card;
}
public Garmatnaya_Client(String name, double amount, double MaxCredit) throws garmatnaya_z6v1.Garmatnaya_Payments.ArgException {
	super(name, Garmatnaya_Payments.type.Client);
	this.card = new Garmatnaya_CreditCard(name, amount, MaxCredit);
}
public Garmatnaya_Client(String name, double MaxCredit) throws garmatnaya_z6v1.Garmatnaya_Payments.ArgException {
	super(name, Garmatnaya_Payments.type.Client);
	this.card = new Garmatnaya_CreditCard(name, MaxCredit);
}
public Garmatnaya_Client() {}
public void PayOrder(Garmatnaya_Order order) throws ArgException {
	if (!order.statusOrder.equals("In stock")) {
		System.out.println("The order status is sold out!");
		return;
	}
	if (this.card.statusCard.equals("Blocked")) {
		throw new ArgException("invalid operation. Card is blocked!");
	}
	else {
		if (this.card.account.amount - order.price < this.card.MaxCredit*(-1)) {
			throw new ArgException("Invalid operation! Not enough  money.");
		}
		else {
			this.card.account.amount -= order.price;
			order.statusOrder = "Sold out";
			System.out.println("The order "+ order.GetName() + " is paid!");
		}
	}
}
public void TransferMoney(Garmatnaya_Client client, double transfer) throws ArgException{
	if (this.card.statusCard.equals("Blocked") || client.card.statusCard.equals("Blocked")) {
		throw new ArgException("Invalid operation. Account is blocked!");
	}
	else {
		if (this.card.account.amount - transfer < this.card.MaxCredit*(-1)) {
			throw new ArgException("Invalid operation! Not enough  money.");
		}
		else {
			this.card.account.amount -= transfer;
			client.card.account.amount += transfer;
			System.out.println("Sum of size in "+ transfer +" was transferred from " + this.GetName() + " to " + client.GetName());
		}
	}
}
public void Block_CardAndAccount() throws ArgException {
	if (this.card.statusCard.equals("Blocked")) {
		throw new ArgException("Invalid operation. Card and account are blocked!");
	}
	else {
		this.card.statusCard = "Blocked";
		System.out.println("Card and Bank account of " + this.GetName() + " is blocked.");
	}
}
}

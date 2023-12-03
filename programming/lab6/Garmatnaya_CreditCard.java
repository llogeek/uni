package garmatnaya_z6v1;

import java.io.Serializable;

public class Garmatnaya_CreditCard extends Garmatnaya_Payments implements Serializable{
	private static final long serialVersionUID = 1L;
	protected double MaxCredit;
	public String statusCard;
	public Garmatnaya_BankAccount account;
	public String GetMaxCredit() {
		return Double.toString(MaxCredit);
	}
	public String GetStatusCard() {
		return this.statusCard;
	}
	public Garmatnaya_BankAccount GetAccount() {
		return this.account;
	}
	public Garmatnaya_CreditCard(String name) throws ArgException {
		super(name, Garmatnaya_Payments.type.CreditCard);
		this.MaxCredit = 0;
		this.account = new Garmatnaya_BankAccount(name);
		statusCard = "Valid";
	}
	public Garmatnaya_CreditCard(String name, double MaxCredit) throws ArgException {
		super(name, Garmatnaya_Payments.type.CreditCard);
		this.MaxCredit = MaxCredit;
		this.account = new Garmatnaya_BankAccount(name, 0);
		statusCard = "Valid";
	}
	public Garmatnaya_CreditCard(String name, double amount, double MaxCredit) throws ArgException {
		super(name, Garmatnaya_Payments.type.CreditCard);
		this.MaxCredit = MaxCredit;
		this.account = new Garmatnaya_BankAccount(name, amount);
		statusCard = "Valid";
	}
	public Garmatnaya_CreditCard() {};

}

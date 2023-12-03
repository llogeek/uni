package garmatnaya_z6v1;

import java.io.Serializable;
public class Garmatnaya_BankAccount extends Garmatnaya_Payments implements Serializable {
	protected double amount;
	public double GetAmount() {
		return this.amount;
	}
	private static final long serialVersionUID = 1L;
	public Garmatnaya_BankAccount(String name) throws ArgException {
		super(name, Garmatnaya_Payments.type.BankAccount);
		this.amount = 0;
	}
	public Garmatnaya_BankAccount(String name, double amount) throws ArgException {
		super(name, Garmatnaya_Payments.type.BankAccount);
		this.amount = amount;
	}
	public Garmatnaya_BankAccount() {}
}

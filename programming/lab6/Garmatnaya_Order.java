package garmatnaya_z6v1;
import java.io.Serializable;
public class Garmatnaya_Order extends Garmatnaya_Payments implements Serializable{
	private static final long serialVersionUID = 1L;
	public String statusOrder;
	public double price;
	public String GetPrice() {
		return Double.toString(price);
	}
	public Garmatnaya_Order(String name, double price) throws ArgException {
		super(name, Garmatnaya_Payments.type.Order);
		this.price = price;
		this.statusOrder = "In stock";
	}
	public Garmatnaya_Order(String name) throws ArgException {
		super(name, Garmatnaya_Payments.type.Order);
		this.price = 0;
		this.statusOrder = "In stock";
	}
	public Garmatnaya_Order() {}
}

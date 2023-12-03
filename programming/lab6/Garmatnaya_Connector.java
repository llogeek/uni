package garmatnaya_z6v1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class Garmatnaya_Connector {
	
	private String filename;
	
	public Garmatnaya_Connector( String filename ) {
		this.filename = filename;
	}
	
	public void write( Garmatnaya_Payments[] system) throws IOException {
		FileOutputStream fos = new FileOutputStream (filename);
		try ( ObjectOutputStream oos = new ObjectOutputStream( fos )) {
			oos.writeInt( system.length );
			for ( int i = 0; i < system.length; i++) {
				oos.writeObject( system[i] );		
			}
			oos.flush();
		}
	}
	
	public Garmatnaya_Payments[] read() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(filename);
		try ( ObjectInputStream oin = new ObjectInputStream(fis)) {
			int length = oin.readInt();
			Garmatnaya_Payments[] result = new Garmatnaya_Payments[length];
			for ( int i = 0; i < length; i++ ) {
				result[i] = (Garmatnaya_Payments) oin.readObject();
			}
			return result;	
		}
	}
	
}

package garmatnaya_z6v1;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;

import garmatnaya_z6v1.Garmatnaya_Payments.ArgException;


public class Garmatnaya_z6v1_test {
	public static Garmatnaya_Payments[] createBand() throws ArgException {
		Garmatnaya_Payments[] system = new Garmatnaya_Payments[5];
		system[0] = new Garmatnaya_Client(Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.viktoria));
		system[1] = new Garmatnaya_Administrator(Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.ekaterina));
		system[2] = new Garmatnaya_Order (Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.dress), Integer.valueOf(Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.price1)));
		system[3] = new Garmatnaya_Client(Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.vasilisa), Integer.valueOf(Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.amount1)), Integer.valueOf(Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.MaxCredit1)));
		system[4] = new Garmatnaya_Client(Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.lisa), Integer.valueOf(Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.amount2)), Integer.valueOf(Garmatnaya_AppLocale.getString(Garmatnaya_AppLocale.MaxCredit2)));
		return system;
	}
	
	static Locale createLocale( String[] args )	{
		if ( args.length == 2 ) {
			return new Locale( args[0], args[1] );
		} else if( args.length == 4 ) {
			return new Locale( args[2], args[3] );
		}
		return null;
	}
	
	static void setupConsole(String[] args) {
		if ( args.length >= 2 ) {
			if ( args[0].compareTo("-encoding")== 0 ) {
				try {
					System.setOut( new PrintStream( System.out, true, args[1] ));
				} catch ( UnsupportedEncodingException ex ) {
					System.err.println( "Unsupported encoding: " + args[1] );
					System.exit(1);
				}				
			}
		}
	}
	public static void main(String[] args) {
		/*Payments[] system = new Payments[5];
		system[0] = new Client("Viktoria");
		system[1] = new Administrator("Ekaterina");
		system[2] = new Order ("Dress", 40);
		system[3] = new Client("Vasilisa", 100, 200);
		system[4] = new Client("Lisa", -120, 100);*/
		
		try {
			setupConsole( args );
			Locale loc = createLocale( args );
			if ( loc == null ) {
				System.err.println( 
						"Invalid argument(s)\n" +
				        "Syntax: [-encoding ENCODING_ID] language country\n" +
						"Example: -encoding Cp1251 be BY" );
				System.exit(1);
			}
			Garmatnaya_AppLocale.set( loc );
			Garmatnaya_Connector con = new Garmatnaya_Connector("Garmatnaya_z6v1.dat");	
			con.write( createBand());
			if (createBand().length <= 0) throw new Exception("Memory exception");
			Garmatnaya_Payments[] band = con.read();
			System.out.println( 
					Garmatnaya_AppLocale.getString( Garmatnaya_AppLocale.payments ) + ":" );
			for (Garmatnaya_Payments n : band ) {
				System.out.println( n );
			}
			Garmatnaya_Payments[] system = Arrays.copyOf(createBand(), createBand().length);
			((Garmatnaya_Administrator)system[1]).BlockCreditCard(((Garmatnaya_Client)system[4]).GetCreditCard());
		((Garmatnaya_Client)system[3]).PayOrder((Garmatnaya_Order)system[2]);
		((Garmatnaya_Client)system[3]).TransferMoney((Garmatnaya_Client)system[0], 30);
		((Garmatnaya_Client)system[3]).Block_CardAndAccount();
		System.out.println(system[3].toString());
		}
		catch ( Exception e ) {
			System.err.println(e);
		}
	}

}

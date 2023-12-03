package garmatnaya_z6v1;


import java.util.*;
public class Garmatnaya_AppLocale {
	private static final String strMsg = "Msg";
	private static Locale loc = Locale.getDefault();
	private static ResourceBundle res = 
			ResourceBundle.getBundle( Garmatnaya_AppLocale.strMsg, Garmatnaya_AppLocale.loc );
	
	static Locale get() {
		return Garmatnaya_AppLocale.loc;
	}
	
	static void set( Locale loc ) {
		Garmatnaya_AppLocale.loc = loc;
		res = ResourceBundle.getBundle( Garmatnaya_AppLocale.strMsg, Garmatnaya_AppLocale.loc );
	}
	
	static ResourceBundle getBundle() {
		return Garmatnaya_AppLocale.res;
	}
	
	static String getString( String key ) {
		return Garmatnaya_AppLocale.res.getString(key);
	}
	
	public static final String payments="payments";
	public static final String type="type";
	public static final String info="info";
	public static final String creation="creation";
	public static final String viktoria="viktoria";
	public static final String ekaterina="ekaterina";
	public static final String dress="dress";
	public static final String price1="price1";
	public static final String vasilisa="vasilisa";
	public static final String amount1="amount1";
	public static final String MaxCredit1="MaxCredit1";
	public static final String lisa="lisa";
	public static final String amount2="amount2";
	public static final String MaxCredit2="MaxCredit2";
}

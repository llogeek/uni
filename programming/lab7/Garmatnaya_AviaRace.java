package garmatnaya_z7v22;

import java.io.Serializable;
import java.util.Scanner;
public class Garmatnaya_AviaRace implements Serializable {
	private static final long serialVersionUID = 1L;
	 public static final String authorDel = ",";
	  public static final String areaDel = "\n";
		String departure;
	    String destination;
	    String number;
	    String type;
	    String time;
	    String days;
		public static boolean checkTime(String str) {
			String regex = "[.:0-9]+";
			return str.matches(regex);
		}
		public static boolean checkDays(String str) {
			String regex = "[-0-9]+";
			boolean result = str.matches(regex);
			return result;
		}
	    public static Garmatnaya_AviaRace read( Scanner fin ) throws Exception {
	        Garmatnaya_AviaRace aviaRace = new Garmatnaya_AviaRace();
	        if ( ! fin.hasNextLine()) return null;
	        aviaRace.departure = fin.nextLine();
	        if ( ! fin.hasNextLine()) return null;
	        aviaRace.destination = fin.nextLine();
	        if ( ! fin.hasNextLine()) return null;
	        aviaRace.number = fin.nextLine();
	        if ( ! fin.hasNextLine()) return null;
	        aviaRace.type = fin.nextLine();
	        if ( ! fin.hasNextLine()) return null;
	        aviaRace.time = fin.nextLine();
	        if (checkTime(aviaRace.time) == false) {
	        	throw new Exception("Incorrect time entry!");
	        }
	        if ( ! fin.hasNextLine()) return null;
	        aviaRace.days = fin.nextLine();  
	        return aviaRace;
	    }
		
	    public Garmatnaya_AviaRace() {}

	    public String toString() {
	        return new String (
	            this.departure + areaDel +
	            this.destination + areaDel +
	            this.number + areaDel +
	            this.type + areaDel +
	            this.time + areaDel +
	            this.days + areaDel			
	        );
	    }
}

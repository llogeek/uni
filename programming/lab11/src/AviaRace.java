import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class AviaRace implements Serializable {
    // class release version:
    private static final long serialVersionUID = 1L;
    // areas with prompts:
    String departure;
    static final String P_departure = "Departure";
    String destination;
    static final String P_destination = "Destination";
    String number;
    static final String P_number = "Number";
    String type;
    static final String P_type = "type";
    String time;
    static final String P_time = "Time";
    String days;
    static final String P_days = "Days";

    private static GregorianCalendar curCalendar = new GregorianCalendar();
    static Boolean validYear( int year ) {
        return year > 0 && year <= curCalendar.get( Calendar.YEAR );
    }
	
    public static Boolean nextRead( Scanner fin, PrintStream out ) {
        return nextRead( P_departure, fin, out );
    }
	
    static Boolean nextRead( final String prompt, Scanner fin, PrintStream out ) {
        out.print( prompt );
        out.print( ": " );
        return fin.hasNextLine();
    }
    public static boolean checkTime(String str) {
        String DATE_FORMAT = "HH:mm";
        String temp = new String(str);
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(temp);
            return true;
        } catch (ParseException | java.text.ParseException e) {
            return false;
        }
    }
    public static boolean checkDays(String str) {
        String temp = new String(str);
        int totalSpace = str.split("-[1-7]+").length - 1;
        str = temp;
        return totalSpace <= 8 && str.matches("-[1-7]+");
    }
    public static final String authorDel = ",";

    public  AviaRace readRace(Scanner fin, PrintStream out )
            throws IOException {
        String str;
        AviaRace book = new AviaRace();
        book.departure = fin.nextLine();
        if (book.departure.equals("exit")) return null;
        if ( ! nextRead( P_destination, fin, out ))           return null;
        book.destination = fin.nextLine();
        if ( ! nextRead( P_number, fin, out ))             return null;
        book.number = fin.nextLine();
        if ( ! nextRead( P_type, fin, out ))             return null;
        book.type = fin.nextLine();
        if ( ! nextRead( P_time, fin, out ))        return null;
        book.time = fin.nextLine();
        if (checkTime(book.time) == false) throw new IOException("Invalid time! Input example: 13.15");
        if ( ! nextRead( P_days, fin, out ))       return null;
        book.days = fin.nextLine();
        if (checkDays(book.days) == false) throw new IOException("Invalid days format! Input example: 1-2-3--67!");
        return book;
    }
		
    public AviaRace() {
    }
	public AviaRace(String departure, String destination, String number, String type, String time, String days) throws IOException {
        this.departure = departure;
        this.destination = destination;
        this.number = number;
        this.type = type;
        this.time = time;
        this.days = days;
    }
    public static final String areaDel = "\n";

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("  Departure: ").append(departure);
        sb.append(", Destination: ").append(destination);
        sb.append(", Number: ").append(number);
        sb.append(", Type: ").append(type);
        sb.append(", Time: ").append(time);
        sb.append(", Days: ").append(days);
        return sb.toString();
    }
}

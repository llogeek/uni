package garmatnaya_z7v22;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
public class Garmatnaya_Aviation {
	 public static void main(String[] args) {
	        try {
	            if ( args.length >= 1 ) {
	                if ( args[0].compareTo( "-a" )== 0 ) {
	                    // Append file with new object from System.in
	                    append_file();
	                }
	                else if ( args[0].compareTo( "-p" )== 0 ) {
	                    // Prints data file
	                    print_file();
	                }
	                else if ( args[0].compareTo( "-d" )== 0 ) {
	                    // Delete data file
	                	try {
	                    delete_file();
	                	}
	                	catch(FileNotFoundException e) {
	                		System.out.println("File not found!");
	                	}
	                }
	                else {
	                    System.err.println( "Option is not realised: " + args[0] );
	                    System.exit(1);
	                }
	            }
	            else {
	                System.err.println( "Avia Races: Nothing to do!" );
	            }
	        }
	        catch ( Exception e ) {
	            System.err.println( "Run/time error: " + e );
	            System.exit(1);
	        }
	        System.out.println( "Races finished..." );	
		System.exit(0);
	    }

	    static final String filename = "Avia.dat";
		
	    private static Scanner fin = new Scanner( System.in );

	    static Garmatnaya_AviaRace read_races() throws Exception {
	        if ( fin.hasNextLine()) {
	            return Garmatnaya_AviaRace.read(fin);
	        }
	        return null;
	    }
		
	    static void delete_file() throws IOException {
	        File f = new File( filename );
			f.delete();
	    }
		
	    static void append_file() throws FileNotFoundException, IOException, Exception {
	        Garmatnaya_AviaRace aviaRace;
	        System.out.println( "Enter race data\nFORMAT:\nPoint of departure\n"
	        		                            + "destination\n" + "flight number\n" 
	        		                            +  "aircraft type\n" + "departure time (example 13:05 or 13.05)\n"
	        		                            + "days of the week");
	        try ( RandomAccessFile raf = new RandomAccessFile( filename, "rw" )) {
	            while (( aviaRace = read_races())!= null ) {
	                Garmatnaya_Buffer.writeObject( raf, aviaRace );
		    }
	        }
	    }

	    static void print_file() 
	            throws FileNotFoundException, IOException, ClassNotFoundException {
	        try ( RandomAccessFile raf = new RandomAccessFile( filename, "rw" )) {
	            long pos;
	            while (( pos = raf.getFilePointer()) < raf.length() ) {
	                Garmatnaya_AviaRace race = (Garmatnaya_AviaRace) Garmatnaya_Buffer.readObject( raf, pos );
	                System.out.println( pos + ": " + race);
	            }
	        }	
	    }
}
// ввод 
// Baku Minsk B2 0740 E95 04.15 -2-4567
// Dubai Istanbul B2* 0728/0184 737/321 13.00 -2-4--7
// Tel-Aviv Moscow B2 0748/0975 737 23:50 ---4--7

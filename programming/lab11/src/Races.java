import java.io.*;
import java.util.*;

public class Races {
    static Frame frame;
    public static void main(String[] args){

            Races r = new Races();
            r.frame.setSize(1000, 500);
            r.frame.setVisible(true);

    }
    public Races(){
       frame = new Frame(this);
    }
    private static void deleteBackup(String filenameBak, String idxnameBak) {
        new File( filenameBak ).delete();
        new File( idxnameBak ).delete();
    }
	
    static void deleteFile(String filename, String idxname) {
        deleteBackup(filename, idxname);
        new File( filename ).delete();
        new File( idxname ).delete();
    }

    private static void backup(String filename, String idxname,String filenameBak, String idxnameBak) {
        deleteBackup(filenameBak,idxnameBak);
        new File( filename ).renameTo( new File( filename ));
        new File( idxname ).renameTo( new File( idxname ));
    }

    static boolean deleteFile(String filename, String idxname,String filenameBak, String idxnameBak, int index, String key )
            throws ClassNotFoundException, IOException, KeyNotUniqueException {
        long[] poss = null;
        try ( Index idx = Index.load( idxname )) {
            IndexBase pidx = indexByArg(index, idx);
            if ( pidx == null ) {
                return false;
            }
            if ( pidx.contains(key)== false ) {
                System.err.println( "Key not found: " + key );
                return false;
            }
            poss = pidx.get(key);
        }
        backup(filename, idxname,filenameBak,idxnameBak);
        Arrays.sort( poss );
        try (Index idx = Index.load( idxname );
             RandomAccessFile fileBak= new RandomAccessFile(filenameBak, "rw");
             RandomAccessFile file = new RandomAccessFile( filename, "rw")) {
            boolean[] wasZipped = new boolean[] {false};
            long pos;
            while (( pos = fileBak.getFilePointer()) < fileBak.length() ) {
                AviaRace race = (AviaRace)
                        Buffer.readObject( fileBak, pos, wasZipped );
                if ( Arrays.binarySearch(poss, pos) < 0 ) { // if not found in deleted
                    long ptr = Buffer.writeObject( file, race, wasZipped[0] );
                    idx.put( race, ptr );
                }
            }
        }
        return true;
    }
    static void appendFile(String filename, String idxname, boolean zipped, String departure, String destination, String number, String type, String time, String days) throws FileNotFoundException, IOException, ClassNotFoundException,
            KeyNotUniqueException {
        try (Index idx = Index.load(idxname);
             RandomAccessFile raf = new RandomAccessFile(filename, "rw")) {
            AviaRace race = new AviaRace(departure, destination,number, type, time, days);
            idx.test( race );
            long pos = Buffer.writeObject( raf, race, zipped );
            idx.put( race, pos );
        }
    }

    private static AviaRace printRecord( RandomAccessFile raf, long pos )
            throws ClassNotFoundException, IOException {
        boolean[] wasZipped = new boolean[] {false};
        AviaRace race = (AviaRace) Buffer.readObject( raf, pos, wasZipped );
        if ( wasZipped[0] == true ) {
            //System.out.print( " compressed" );
        }
        //System.out.println( " record at position "+ pos + ": \n" + book );
        return race;
    }
	
    private static List<AviaRace> printRecord( RandomAccessFile raf, String key,
            IndexBase pidx ) throws ClassNotFoundException, IOException {
        List<AviaRace> temp = new ArrayList<>();
        long[] poss = pidx.get( key );
        for ( long pos : poss ) {
          //  System.out.print( "*** Key: " +  key + " points to" );
            temp.add(printRecord( raf, pos ));
        }
        return temp;
    }
	
    static void printFile(String file)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        long pos;
        int rec = 0;
        List<AviaRace> temp = new ArrayList<>();
        try ( RandomAccessFile raf = new RandomAccessFile( file, "rw" )) {
            while (( pos = raf.getFilePointer()) < raf.length() ) {
              //  System.out.print( "#" + (++rec ));
                temp.add(printRecord( raf, pos ));
            }
          //  System.out.flush();
        }
        frame.showData(temp);
    }
	
    private static IndexBase indexByArg(int arg, Index idx ) {
        IndexBase pidx = null;
        if ( arg == 0) {
            pidx = idx.departures;
        } 
        else if ( arg == 1) {
            pidx = idx.destinations;
        } 
        else if ( arg == 2) {
            pidx = idx.numbers;
        } 
        else {
            frame.showError( "Invalid index specified: " + arg );
        }
        return pidx;
    }
    static boolean printFile(String filenam, String idxnam, int index, boolean reverse )
            throws ClassNotFoundException, IOException {
        try (Index idx = Index.load( idxnam );
             RandomAccessFile raf = new RandomAccessFile( filenam, "rw" )) {
            IndexBase pidx = indexByArg( index, idx );
            List<AviaRace> temp = new ArrayList<>();
            if ( pidx == null ) {
                return false;
            }
            String[] keys = 
                pidx.getKeys( reverse ? new KeyCompReverse() : new KeyComp() );
            for ( String key : keys ) {
               temp.addAll( printRecord( raf, key, pidx ));
            }
            frame.showData(temp);
        }

        return true;
    }
    void delete(String filename, String indexname, String filenameBak, String idxnameBak, int number, String key) throws FileNotFoundException, IOException, ClassNotFoundException,
            KeyNotUniqueException {
        long[] poss;
        try (Index idx = Index.load(indexname)) {
            IndexBase pidx = indexByArg(number, idx);
            if (!pidx.contains(key)) {
                throw new IllegalArgumentException("Key not found: " + key);
            }
            poss = pidx.get(key);
        }
        Arrays.sort( poss );
        File data = new File(filename), index = new File(indexname), dataBak = new File(filenameBak), indexBak = new File(idxnameBak);
        if ((!dataBak.exists() || dataBak.delete()) && (!indexBak.exists() || indexBak.delete()) && data.renameTo(dataBak) && index.renameTo(indexBak)) {
            try (Index idx = Index.load(indexname);
                 RandomAccessFile fileBak = new RandomAccessFile(filenameBak, "rw");
                 RandomAccessFile file = new RandomAccessFile(filename, "rw")) {
                boolean[] wasZipped = new boolean[]{false};
                long pos;
                while ((pos = fileBak.getFilePointer()) < fileBak.length()) {
                    AviaRace employee = (AviaRace)
                            Buffer.readObject(fileBak, pos, wasZipped);
                    if (Arrays.binarySearch(poss, pos) < 0) { // if not found in deleted
                        long ptr = Buffer.writeObject(file, employee, wasZipped[0]);
                        idx.put(employee, ptr);
                    }
                }
            }
        }
    }
    static boolean findByKey( String filenam, String idxnam, int index, String key  )
            throws ClassNotFoundException, IOException {
        try (Index idx = Index.load( idxnam );
             RandomAccessFile raf = new RandomAccessFile( filenam, "rw" )) {
           List<AviaRace> temp = new ArrayList<>();
            IndexBase pidx = indexByArg( index, idx );
            if ( pidx.contains(key)== false ) {
                frame.showError( "Key not found: " +key );
                return false;				
            }
           temp.addAll( printRecord( raf, key, pidx ));
            if (temp.size() == 0) frame.showError("No information");
            else frame.showData(temp);
        }

        return true;	
    }

    static boolean findByKey(String filenam, String idxnam, int index, String key, Comparator<String> comp )
    throws ClassNotFoundException, IOException {
        try (Index idx = Index.load( idxnam );
             RandomAccessFile raf = new RandomAccessFile( filenam, "rw" )) {
            IndexBase pidx = indexByArg( index, idx );
            if ( pidx.contains(key)== false ) {
                frame.showError( "Key not found: " + key);
                return false;				
            }
            List<AviaRace> temp = new ArrayList<>();
            String[] keys = pidx.getKeys( comp );
            for ( int i = 0; i < keys.length; i++ ) {
                String keyy = keys[i];
                if ( keyy.equals( key )) {
                    break;
                }
                temp.addAll(printRecord( raf, keyy, pidx ));
                if (temp.size() == 0) frame.showError("No information");
                else frame.showData(temp);
            }
        }
        return true;
    }

}

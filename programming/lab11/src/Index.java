import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

class KeyComp implements Comparator<String> {
    public int compare(String o1, String o2) {
        // right order:	
        return o1.compareTo(o2);
    }
}

class KeyCompReverse implements Comparator<String> {
    public int compare(String o1, String o2) {
        // reverse order:
        return o2.compareTo(o1);
    }
}

interface IndexBase {
    String[] getKeys( Comparator<String> comp );
    void put( String key, long value );
    boolean contains( String key );
    long[] get( String key );
}

class IndexOne2One implements Serializable, IndexBase {
    // Unique keys
    // class release version:
    private static final long serialVersionUID = 1L;
    
    private TreeMap<String,Long> map;
    
    public IndexOne2One() {
        map = new TreeMap<String,Long> ();
    }
	
    public String[] getKeys( Comparator<String> comp ) {
        String[] result = map.keySet().toArray( new String[0] );
        Arrays.sort( result, comp );
        return result;
    }
	
    public void put( String key, long value ) {
        map.put(key, new Long( value ));
    }

    public boolean contains( String key ) {
        return map.containsKey(key);
    }
	
    public long[] get( String key ) {
        long pos = map.get( key ).longValue();
        return new long[] {pos};
    }
}

class IndexOne2N implements Serializable, IndexBase {
    // Not unique keys
    // class release version:
    private static final long serialVersionUID = 1L;

    private TreeMap<String,long[]> map;
	
    public IndexOne2N() {
        map = new TreeMap<String,long[]> ();
    }
	
    public String[] getKeys( Comparator<String> comp ) {
        String[] result = map.keySet().toArray( new String[0] );
        Arrays.sort( result, comp );
        return result;
    }
	
    public void put( String key, long value ) {
        long[] arr = map.get(key);
        arr = ( arr != null ) ?
            Index.InsertValue( arr, value ) :
            new long[] {value};		
	map.put(key, arr);
    }
	
    public void put( String keys,   // few keys in one string
                     String keyDel, // key delimiter
                     long value ) {
        StringTokenizer st = new StringTokenizer( keys, keyDel );
        int num = st.countTokens();
        for ( int i= 0; i < num; i++ ) {
            String key = st.nextToken();
            key = key.trim();
            put( key, value );
        }
    }

    public boolean contains( String key ) {
        return map.containsKey(key);
    }
	
    public long[] get( String key ) {
        return map.get( key );
    }
}

class KeyNotUniqueException extends Exception {
    // class release version:
    private static final long serialVersionUID = 1L;
    
    public KeyNotUniqueException( String key ) {
        super( new String( "Key is not unique: " + key ));
    }
}

public class Index implements Serializable, Closeable {
    // class release version:
    private static final long serialVersionUID = 1L;

    public static long[] InsertValue( long[] arr, long value ) {
        int length = ( arr == null ) ? 0 : arr.length;
        long [] result = new long[length + 1];
        for( int i = 0; i < length; i++ )
            result[i] = arr[i];
        result[length] = value;
        return result;
    }
	
    IndexOne2N         departures;
    IndexOne2N         destinations;
    IndexOne2One        numbers;
	
    public void test( AviaRace book ) throws KeyNotUniqueException {
        assert( book != null );
        if ( numbers.contains( book.number )) {
            throw new KeyNotUniqueException( book.number );
        }		
    }
	
    public void put(AviaRace book, long value ) throws KeyNotUniqueException {
        test( book );
        departures.put( book.departure, AviaRace.authorDel, value );
        destinations.put( book.destination, AviaRace.authorDel, value);
        numbers.put( book.number, value);
    }
	
    public Index()  {
        departures   = new IndexOne2N();
        destinations = new IndexOne2N();
        numbers 	= new IndexOne2One();
    }
	
    public static Index load( String name ) 
            throws IOException, ClassNotFoundException {
        Index obj = null;
        try {
            FileInputStream file = new FileInputStream( name );
            try( ZipInputStream zis = new ZipInputStream( file )) {
                ZipEntry zen = zis.getNextEntry();
                if ( zen.getName().equals( Buffer.zipEntryName )== false ) {
                    throw new IOException("Invalid block format");
                }
                try ( ObjectInputStream ois = new ObjectInputStream( zis )) {
                    obj = (Index) ois.readObject();
                }
            }
        } catch ( FileNotFoundException e ) {
            obj = new Index();
        }
        if ( obj != null ) {
            obj.save( name );
        }
        return obj;
    }
	
    private transient String filename = null; 
	
    public void save( String name ) {

        filename = name;
    }
	
    public void saveAs( String name ) throws IOException {
        FileOutputStream file = new FileOutputStream( name );
        try ( ZipOutputStream zos = new ZipOutputStream( file )) {
            zos.putNextEntry( new ZipEntry( Buffer.zipEntryName ));
            zos.setLevel( ZipOutputStream.DEFLATED );
            try ( ObjectOutputStream oos = new ObjectOutputStream( zos )) {
                oos.writeObject( this );
                oos.flush();
                zos.closeEntry();
                zos.flush();
            }
        }
    }
	
    public void close() throws IOException {
        saveAs( filename );
    }
}

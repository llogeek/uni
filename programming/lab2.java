package garmatnaya_z2v1;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.lang.String;
public class Garmatnaya_z2v1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Scanner in = new Scanner( System.in );
	        while ( in.hasNextLine() ) {
	            String s = in.nextLine();
	    		int open = 0, close = 0;
	    		int lastposition = 0, firstposition = 0;
	    		for (int i = 0; i < s.length(); i++) {
	    			if (s.charAt(i) == '(') {
	    				open++;
	    			}
	    			else if (s.charAt(i) == ')') {
	    					close++;
	    			}
	    		}
	    		//System.out.printf(" %d , %d", open, close);
	    		//System.out.println();
	    		if (open == close) {
	    			int i = s.length() - 1;
	    		for (; i > 0; i--) {
	    			if (s.charAt(i) == ')') {
	    				if (lastposition == -1) break;
	    				lastposition = s.lastIndexOf(')', i - 1);
	    				firstposition = s.lastIndexOf('(', i - 1);
	    				if (firstposition > lastposition) {
	    					s = s.substring(0, firstposition) + s.substring(i);
	    					i = lastposition + 1;
	    					continue;
	    				}
	    				else {
	    			       if (lastposition == -1) break;
	    				   else i = lastposition + 1;
	    					continue;
	    				}
	    			}
	    		}	
	    		//s = s.replaceAll("[\\(\\)\\[\\]\\{\\}]","");
	    		 StringTokenizer st = new StringTokenizer(s, "()");
	    		 while(st.hasMoreTokens()) {
	    			    String key = st.nextToken();
	    		 	    System.out.print(key);
	    		        }
	    		}
	    		else if ( open < close || open > close){
	    			System.err.println("Amounts of opening and ending brackets are not equal!");
	    		}
	        }
	            in.close();
	}

}
                   // tests 
//outside (a text (in another) parenthesis) outside (again)
//lo(li)ta
//I (have (to (go (to) the) university) now) but (I (will come) back) as (soon as possible)
//It’s raining
//I don’t like (this (p)a(rt))
//((We) don’t (nee)d (no) edu(cat)ion), we (d(o))n’t n(e)ed no (thought control)
//Who (i(s())) there?
//(I)( )want( )to( )sleep(ing)
//To( )b(e) (o())r not (to) b(e?)
//to live( a) healty( life)
//^*iILK(KJ*&*())*^&
//Lolita*23?(GARMATNAYA**__)??\\/(/)
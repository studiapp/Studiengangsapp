package de.hfu.mos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

public class QuickSortCalendar {
	
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'hhmmss", Locale.GERMANY);
	
	   public static void sortiere(Vector<Component> x) throws ParseException {
	      qSort(x, 0, x.size()-1);
	   }
	    
	   public static void qSort(Vector<Component> x, int links, int rechts) throws ParseException {
	      if (links < rechts) {
	         int i = partition(x,links,rechts);
	         qSort(x,links,i-1);
	         qSort(x,i+1,rechts);
	      }
	   }
	    
	   public static int partition(Vector<Component> x, int links, int rechts) throws ParseException {
	      int i, j;
	      Component pivot, help;
	      pivot = x.get(rechts);               
	      i     = links;
	      j     = rechts-1;
	      while(i<=j) {
	         if (simpleDateFormat.parse(x.get(i).getProperty(Property.DTSTART).getValue()).after(simpleDateFormat.parse(pivot.getProperty(Property.DTSTART).getValue())) ) {     
	            // tausche x[i] und x[j]
	            help = x.get(i); 
	            x.set(i, x.get(j)); 
	            x.set(j, help);                             
	            j--;
	         } else i++;            
	      }
	      // tausche x[i] und x[rechts]
	      help      = x.get(i);
	      x.set(i, x.get(rechts));
	      x.set(rechts, help);
	        
	      return i;
	   }
	       
	}

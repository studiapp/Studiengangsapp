package de.hfu.mos.home.budget;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import de.hfu.mos.R;


//implements OnItemClickListener 
public class BudgetRechner extends Fragment implements View.OnClickListener  {

	// for button Eigene Berechnung
	public int eigeneb = 0;
	public int  eigenem = 0;
	public String bufferbs, bufferbm;
	public float bufferb, bufferm;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	View rootView = inflater.inflate(R.layout.fragment_graphauswahl, container, false);
	
    	final EditText tvb = (EditText) rootView.findViewById(R.id.etb);
    	final EditText tvm = (EditText) rootView.findViewById(R.id.etm);
    	
        Button ai = (Button) rootView.findViewById(R.id.ai);
        Button cn = (Button) rootView.findViewById(R.id.cn);
        Button spb = (Button) rootView.findViewById(R.id.spb);
        Button verglbachelor = (Button) rootView.findViewById(R.id.verglbachelor);
        Button mos  = (Button) rootView.findViewById(R.id.mos);   
        final Button eigene = (Button) rootView.findViewById(R.id.indi);
		       
        // Eigene Berechnung gets enabled if EditText were used
		eigene.setEnabled(false);		
		tvb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		      @Override
		      public void onFocusChange(View v, boolean hasFocus) {
					eigene.setEnabled(true);
		      }
		    });
		tvm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		      @Override
		      public void onFocusChange(View v, boolean hasFocus) {
						eigene.setEnabled(true);
		      }
		    });

        ai.setOnClickListener(this);
        cn.setOnClickListener(this);
        spb.setOnClickListener(this);
        verglbachelor.setOnClickListener(this);
        mos.setOnClickListener(this);     
        
        
        eigene.setOnClickListener ( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Fragment fragment = null;
            	// try to convert values for Eigene Berechnung
	    		String textb = tvb.getText().toString();
	        	try {
	        	   bufferbs = textb.replace(',', '.');
	        	   bufferb = Float.parseFloat(bufferbs);
	        	   eigeneb = (int) bufferb;
	        	   Log.i("",eigeneb+" is a number");
	        	} catch (NumberFormatException e) {
	        	   Log.i("",eigeneb+"is not a number");
	        	}
	        	String textm = tvm.getText().toString();
	        	try {
	        	   bufferbm = textm.replace(',', '.');
	        	   bufferm = Float.parseFloat(bufferbm);
	        	   eigenem = (int) bufferm;
	        	   Log.i("",eigenem+" is a number");
	        	} catch (NumberFormatException e) {
	        	   Log.i("",eigenem+"is not a number");
	        	} 
	        	fragment = new LineGraph("Eigene Berechnung","Eigene Berechnung",1, eigeneb, eigenem);
	        	if (fragment != null) {
		            FragmentManager fragmentManager = getFragmentManager();
		            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	        	}
            }
        });
        return rootView;
    }	
	
		public void onClick(View v){
			Fragment fragment = null;
	    	switch(v.getId()){   	
	    	// IT-Consulting
	    	case R.id.cn:	    		
	    		fragment = new LineGraph("IT-Consulting","Bacheloreinstiegsgehalt: 40.494,00 €",0,-12360,-22008,-7379,-22008,218,-10731,13245,8474,26765,28244,33655,42195,40043,49472,47096,57506,54883,66376,63480,76170);
	    		break;
	        // Anwender Support
	    	case R.id.ai:
	    		fragment = new LineGraph("Anwender Support","Bacheloreinstiegsgehalt:  40.193,00 €",0,-12667,-22008,-7698,-22008,-120,-10731,12870,8474,26352,28244,33199,42195,39539,49472,46540,57506,54269,66376,62802,76170);
	    		break; 
	        // Netzwerk
	    	case R.id.spb:
	    		fragment = new LineGraph("Netzwerke","Bacheloreinstiegsgehalt: 40.299,00 €",0,-12559,-22009,-7586,-22008,0,-10731,13002,8474,26497,28244,33359,42195,39717,49472,46735,57506,54485,66376,63041,76170);
	    		break;
	    	// Vertrieb-Au�endienst	
	    	case R.id.verglbachelor:
	    		fragment = new LineGraph("Vertrieb-Außendienst","Bacheloreinstiegsgehalt: 40.689,00 €",0,-12161,-22008,-7172,-22008,438,-10731,13487,8474,27033,28244,33950,42195,40369,49472,47456,57506,55280,66376,63919,76170);
	    		break;
	    	// Softwareentwickler	
	    	case R.id.mos:
	    		fragment = new LineGraph("Softwareentwickler","Bacheloreinstiegsgehalt: 40.441,00 €",0,-12414,-22008,-7436,-22008,159,-10731,13179,8474,26692,28244,33575,42195,39954,49472,46998,57506,54775,66376,63361,76170);
	    		break;  
	    	//case R.id.indi:

	    		
	    		//break;
	    	default:
	    		break;
	    		}  	
	    	//openFragment(fragment, position);
	        if (fragment != null) {
	            FragmentManager fragmentManager = getFragmentManager();
	            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	            }
       }
    }
package de.hfu.mos.campus;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.hfu.mos.R;

public class CampusFragment extends Fragment {

	
	public CampusFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_campus, container, false);
	    
		resetFragments();
		
		return rootView;
	}
	
	// fragments have to be reset or app will crash if they are called a second time and still referenced
	private void resetFragments(){
		
	    Fragment f = (Fragment) getFragmentManager().findFragmentById(R.id.campus_buttonFragment);
	    if (f != null){ 
	        getFragmentManager().beginTransaction().remove(f).commit();
	    }
	    
	    f = (Fragment) getFragmentManager().findFragmentById(R.id.campus_mapFragment);
	    if (f != null){ 
	        getFragmentManager().beginTransaction().remove(f).commit();
	    }
	}
	
}

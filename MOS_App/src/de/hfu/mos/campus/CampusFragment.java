package de.hfu.mos.campus;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.hfu.mos.R;
import de.hfu.mos.campus.TouchImageView;

public class CampusFragment extends Fragment {

	
	public CampusFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_campus, container, false);

        TouchImageView img = new TouchImageView(getActivity());
        img.setImageResource(R.drawable.ic_campus_lageplan);
        img.setMaxZoom(4f);

        // Wenn nur Bild OHNE Map Button, damit Bild zoomable ist
        // return img
		
		return rootView;
	}

}

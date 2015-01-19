package de.hfu.mos.campus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.hfu.mos.R;
import de.hfu.mos.campus.TouchImageView;

public class CampusMap extends Fragment {

	
	public CampusMap() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TouchImageView img = new TouchImageView(getActivity());
        img.setImageResource(R.drawable.ic_campus_lageplan);
        img.setMaxZoom(4f);

        return img;

	}

}

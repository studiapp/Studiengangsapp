package de.hfu.mos.home;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import de.hfu.mos.R;

public class HomeFragment extends Fragment {

    ImageView _mosVideo;

    // Video

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Video

        _mosVideo = (ImageView) rootView.findViewById(R.id.video_button);

        _mosVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), VideoViewActivity.class);
                startActivity(myIntent);
            }
        });

        return rootView;
    }

}

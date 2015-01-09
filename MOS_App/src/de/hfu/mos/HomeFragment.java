package de.hfu.mos;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    Button buttonTest;
    ImageView _mosVideo;

    // Video

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        buttonTest = (Button) rootView.findViewById(R.id.buttonFelix);

        OnClickListener oni = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                test(v);

            }
        };


        //buttonTest.setOnClickListener(oni);

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


    public void test(View v) {

        Toast.makeText(getActivity(), "Funtzt!", Toast.LENGTH_SHORT).show();
    }
}

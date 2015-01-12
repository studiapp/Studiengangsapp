package de.hfu.mos.fakultaet;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import de.hfu.mos.R;

public class InformatikFragment extends Fragment {

    private Button buttonAIB, buttonCNB, buttonSPB, buttonINM, buttonMOS;

    private DownloadManager _DownloadManager;

    private String url;

    public InformatikFragment(DownloadManager dm) {

        _DownloadManager = dm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_informatik, container, false);
        ;

        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {

                doOnClick(v);

            }
        };

        buttonAIB = (Button) rootView.findViewById(R.id.ButtonAIB);
        buttonCNB = (Button) rootView.findViewById(R.id.ButtonCNB);
        buttonSPB = (Button) rootView.findViewById(R.id.ButtonSPB);
        buttonINM = (Button) rootView.findViewById(R.id.ButtonINM);
        buttonMOS = (Button) rootView.findViewById(R.id.ButtonMOS);


        buttonAIB.setOnClickListener(clickListener);
        buttonCNB.setOnClickListener(clickListener);
        buttonSPB.setOnClickListener(clickListener);
        buttonINM.setOnClickListener(clickListener);
        buttonMOS.setOnClickListener(clickListener);


        return rootView;
    }


    private void doOnClick(View v) {

        switch (v.getId()) {

            case R.id.ButtonAIB:
                url = "https://marke.hs-furtwangen.de/fileadmin/user_upload/Print/Studiengangsflyer/Online_Version/2014-06_Flyer_AIB-web.pdf";
                downloadModul(url);
                break;
            case R.id.ButtonCNB:
                url = "https://marke.hs-furtwangen.de/fileadmin/user_upload/Print/Studiengangsflyer/Online_Version/Flyer-CNB-web.pdf";
                downloadModul(url);
                break;
            case R.id.ButtonSPB:
                url = "https://marke.hs-furtwangen.de/fileadmin/user_upload/Print/Studiengangsflyer/Online_Version/2014-06_Flyer_SPB-web.pdf";
                downloadModul(url);
                break;
            case R.id.ButtonINM:
                url = "https://marke.hs-furtwangen.de/fileadmin/user_upload/Print/Studiengangsflyer/Online_Version/Flyer-INM-web.pdf";
                downloadModul(url);
                break;
            case R.id.ButtonMOS:
                url = "https://marke.hs-furtwangen.de/fileadmin/user_upload/Print/Studiengangsflyer/Online_Version/Flyer_MOS-web.pdf";
                downloadModul(url);
                break;
            default:
                Toast.makeText(getActivity(), "Irgendwas ging schief", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadModul(String linkModul) {

        new File(Environment.DIRECTORY_DOWNLOADS + "/IN_Studiengaenge").mkdirs();

        Request request = new Request(Uri.parse(linkModul));
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS + "/IN_Studiengaenge", linkModul.substring(linkModul.lastIndexOf("/", linkModul.length())));
        _DownloadManager.enqueue(request);

    }
}

package de.hfu.mos;

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

public class StudiengangFragment extends Fragment {
	
	private Button buttonS3M1_5, buttonS2M1, buttonS2M2, buttonS2M3, buttonS2M4, buttonS2M5,
					buttonS1M1, buttonS1M2, buttonS1M3, buttonS1M4, buttonS1M5;
	
	private DownloadManager _DownloadManager;
	
	private String url;
	
	public StudiengangFragment(DownloadManager dm) {
		
		_DownloadManager = dm;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_studiengang, container, false);;
		
		OnClickListener clickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				doOnClick(v);
				
			}
		};
			
		buttonS1M5 = (Button) rootView.findViewById(R.id.ButtonS1M5);
		buttonS1M4 = (Button) rootView.findViewById(R.id.ButtonS1M4);
		buttonS1M3 = (Button) rootView.findViewById(R.id.ButtonS1M3);
		buttonS1M2 = (Button) rootView.findViewById(R.id.ButtonS1M2);
		buttonS1M1 = (Button) rootView.findViewById(R.id.ButtonS1M1);
		buttonS2M5 = (Button) rootView.findViewById(R.id.ButtonS2M5);
		buttonS2M4 = (Button) rootView.findViewById(R.id.ButtonS2M4);
		buttonS2M3 = (Button) rootView.findViewById(R.id.ButtonS2M3);
		buttonS2M2 = (Button) rootView.findViewById(R.id.ButtonS2M2);
		buttonS2M1 = (Button) rootView.findViewById(R.id.ButtonS2M1);
		buttonS3M1_5 = (Button) rootView.findViewById(R.id.ButtonS3M1_5);
		
		buttonS1M5.setOnClickListener(clickListener);
		buttonS1M4.setOnClickListener(clickListener);
		buttonS1M3.setOnClickListener(clickListener);
		buttonS1M2.setOnClickListener(clickListener);
		buttonS1M1.setOnClickListener(clickListener);
		buttonS2M5.setOnClickListener(clickListener);
		buttonS2M4.setOnClickListener(clickListener);
		buttonS2M3.setOnClickListener(clickListener);
		buttonS2M2.setOnClickListener(clickListener);
		buttonS2M1.setOnClickListener(clickListener);
		buttonS3M1_5.setOnClickListener(clickListener);
		
		return rootView;
	}
	

	private void doOnClick(View v){
		
		switch(v.getId()){
		
		case R.id.ButtonS3M1_5:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem3_Abschlussarbeit_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS2M1:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem2_InfrastrukturenDerKommunikation_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS2M2:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem2_SichereCloudDienste_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS2M3:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem2_SoftwareFuerMobileSysteme_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS2M4:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem2_Projekt_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS2M5:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem1_WPM_Beispiel_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS1M1:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem1_AssistiveSysteme_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS1M2:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem1_Ergonomie_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS1M3:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem1_SEmobilerSysteme_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS1M4:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/MOS/Sem1_MobilitaetUndInnovation_MOS.pdf";
			downloadModul(url);
			break;
		case R.id.ButtonS1M5:
			url = "http://www.hs-furtwangen.de/fileadmin/user_upload/Fakultaet_IN/Dokumente/Modulbeschreibungen/WPVs-WS2014-15/Digitale_Geschaeftsmodelle_und_Prozesse_MOS.pdf";
			downloadModul(url);
			break;
		default:
			Toast.makeText(getActivity(), "Irgendwas ging schief", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void downloadModul(String linkModul){
		
		new File(Environment.DIRECTORY_DOWNLOADS + "/MOS_Module").mkdirs();
		
		Request request = new Request(Uri.parse(linkModul));
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS+ "/MOS_Module", linkModul.substring(linkModul.lastIndexOf("/", linkModul.length())));
		_DownloadManager.enqueue(request);
		
	}
}

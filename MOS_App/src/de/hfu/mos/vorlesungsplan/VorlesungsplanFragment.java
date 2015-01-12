package de.hfu.mos.vorlesungsplan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.String;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.hfu.mos.ConnectionDetector;
import de.hfu.mos.R;
import de.hfu.mos.R.array;
import de.hfu.mos.R.color;
import de.hfu.mos.R.id;
import de.hfu.mos.R.layout;
import de.hfu.mos.R.string;
import de.hfu.mos.vorlesungsplan.quicksort.QuickSortCalendar;

public class VorlesungsplanFragment extends Fragment implements OnItemSelectedListener {
	
	private CalendarBuilder _CalendarBuilder;
	private DownloadManager _DownloadManager;
	private Button _buttonUpdate, _buttonShow;
	private LinearLayout _Layout, emptyLayout;
	private Spinner _SpinnerStudiengang, _SpinnerSemester;
	private TextView _AngezeigterPlan;
	
	private Vorlesung asynkTaskVorl;
	private DownloadFileAndShow asynkTaskHTML;
	
	private ArrayAdapter<CharSequence> adapterStudiengang, adapterSemster;
	
	private String url, fileName, html="", link="";
	private File file; 
	
	private java.util.Calendar today;
		
	private final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd'T'hhmmss", Locale.GERMANY);
		
	//we need the Downloadmanager to download ics File from the HFU website.
	//Downloadmanager has to be initialized in the mainActivity. Otherwise the application will crash
	public VorlesungsplanFragment(DownloadManager dm) {
		
		_DownloadManager = dm;
	}

	//For this function we use the .ics File provided by the HFU website/schedule.
	//to be able to read and work with the entries of the ics File, we use the opensource libary iCal4j
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_vorlesungsplan, container, false);

		today = java.util.Calendar.getInstance();
		
		_CalendarBuilder = new CalendarBuilder();
	
		_AngezeigterPlan = (TextView) rootView.findViewById(R.id.textView_angezeigterPlan);
		
		_Layout = (LinearLayout) rootView.findViewById(R.id.linearLayout_Vorlesung);
				
		_buttonUpdate = (Button) rootView.findViewById(R.id.button_updateVorlesungsplan);
		
		_buttonShow = (Button) rootView.findViewById(R.id.button_showVorlesungsplan);
		
		_SpinnerStudiengang = (Spinner) rootView.findViewById(R.id.spinnerStudiengang_Vorlesung);
		
		_SpinnerSemester = (Spinner) rootView.findViewById(R.id.spinnerSemester_Vorlesung);
		
		adapterStudiengang = ArrayAdapter.createFromResource(getActivity(),
		        R.array.studiengang, android.R.layout.simple_spinner_dropdown_item);
		
		adapterSemster = ArrayAdapter.createFromResource(getActivity(),
		        R.array.Semester_AIB, android.R.layout.simple_spinner_dropdown_item);
		
		_SpinnerStudiengang.setAdapter(adapterStudiengang);
		_SpinnerSemester.setAdapter(adapterSemster);
		
		_SpinnerStudiengang.setOnItemSelectedListener(this);

		OnClickListener clickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(asynkTaskVorl != null)
					asynkTaskVorl.cancel(true);
				if(asynkTaskHTML != null)
					asynkTaskHTML.cancel(true);
				
				try{
				switch(v.getId()){
				
				case R.id.button_updateVorlesungsplan:
					
					if(ConnectionDetector.isOnline(getActivity())){
						
						setFileName();
						getFileLinkAndShowFile();
						
					}
					else 
						Toast.makeText(getActivity(), "Sorry, no Internet available", Toast.LENGTH_LONG).show();
					
					break;
					
				case R.id.button_showVorlesungsplan:
					
					setFileName();
					loadPlan();
					break;
				}
				}catch(Exception e){}
			}
		};
		_buttonUpdate.setOnClickListener(clickListener);
		_buttonShow.setOnClickListener(clickListener);
		
		new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/MOS_vorlesungsplan/").mkdirs();
		
		if( new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/MOS_vorlesungsplan/").listFiles().length > 0){
			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/MOS_vorlesungsplan/").listFiles()[0];
			fileName = file.getName();
		}
		loadPlan();
		
		return rootView;
	}
	
	class DownloadFileAndShow extends AsyncTask<String, Void, Void>{

	@Override
	protected Void doInBackground(String... url) {

		
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url[0]);
				HttpResponse response = client.execute(request);

				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				StringBuilder str = new StringBuilder();
				String line = null;
				 while ((line = reader.readLine()) != null) {
				 str.append(line);
				 }
				 in.close();
				 html = str.toString();
			} catch (Exception e) {

			}
			
		return null;
	}
	
	@Override
		protected void onPostExecute(Void result) {
			Pattern p = Pattern.compile("/splan/ical[^\"]*");
			Matcher m = p.matcher(html);
			if (m.find()){
				link = m.group();
				link = link.replaceAll("amp;", "");
				url = getActivity().getString(R.string.HFUdomain) + link;
			}
			
			if(file != null)
				file.delete();
			
			downloadVorlesungsplan();
			
			loadPlan();
	}
}
	
	private void loadPlan(){

		if(file != null && file.exists() ){
			asynkTaskVorl = new Vorlesung();
			asynkTaskVorl.execute();
		}
		else
			Toast.makeText(getActivity(), "Nothing to display. Please update.", Toast.LENGTH_SHORT).show();
		
	}
    
	@Override
	public void onStop() {
		super.onStop();
		if(asynkTaskVorl != null)
			asynkTaskVorl.cancel(true);
		if(asynkTaskHTML != null)
			asynkTaskHTML.cancel(true);
	}
	//The entries in the ics file are sorted by the Event and not by date
	//here we sort the content by date
	private Vector<Component> sortData(Calendar cal) throws ParseException{
		
		Vector<Component> tempList = new Vector<Component>();
		
		tempList.addAll(cal.getComponents(Component.VEVENT));
		
		QuickSortCalendar.sortiere(tempList);
		
		return tempList;
		
	}
	
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
		if(pos == adapterStudiengang.getPosition("AIB")){
			adapterSemster = ArrayAdapter.createFromResource(getActivity(),
			        R.array.Semester_AIB, android.R.layout.simple_spinner_dropdown_item);
			_SpinnerSemester.setAdapter(adapterSemster);
			return;
		}
		
		if(pos == adapterStudiengang.getPosition("CNB")){
			adapterSemster = ArrayAdapter.createFromResource(getActivity(),
			        R.array.Semester_CNB, android.R.layout.simple_spinner_dropdown_item);
			_SpinnerSemester.setAdapter(adapterSemster);
			return;
		}
		
		if(pos == adapterStudiengang.getPosition("SPB")){
			adapterSemster = ArrayAdapter.createFromResource(getActivity(),
			        R.array.Semester_SPB, android.R.layout.simple_spinner_dropdown_item);
			_SpinnerSemester.setAdapter(adapterSemster);
			return;
		}
		
		if(pos == adapterStudiengang.getPosition("MOS")){
			adapterSemster = ArrayAdapter.createFromResource(getActivity(),
			        R.array.Semester_MOS, android.R.layout.simple_spinner_dropdown_item);
			_SpinnerSemester.setAdapter(adapterSemster);
			return;
		}

		
	}

	
	
	private String getDay(int day){

		switch (day) {
		case 1:
			return "Montag";
		case 2:
			return "Dienstag";
		case 3:
			return "Mittwoch";
		case 4:
			return "Donnerstag";
		case 5:
			return "Freitag";
		case 6:
			return "Samstag";
		case 7:
			return "Sonntag";
		default:
			return "Wrong value";
		}
	}
	
	private void downloadVorlesungsplan(){
		
		Uri path;
		
		Request request = new Request(Uri.parse(url));
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS +"/MOS_vorlesungsplan" , fileName);
		long id = _DownloadManager.enqueue(request);

		while ((path = _DownloadManager.getUriForDownloadedFile(id)) == null) {

		}
		
		file = new File(path.getPath());
	}
	
	private class Vorlesung extends AsyncTask<Void, Void, LinearLayout>{
		

		@Override
		protected void onPostExecute(LinearLayout result) {
			try {
				_Layout.removeAllViews();
				_Layout.addView(this.get());
				Toast.makeText(getActivity(), "fertig", Toast.LENGTH_SHORT).show();
				file = null;
				_AngezeigterPlan.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				_AngezeigterPlan.setText(fileName);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		protected void onPreExecute() {
			
			Toast.makeText(getActivity(), "loading...", Toast.LENGTH_SHORT).show();
			
		}
		
		@Override
		protected LinearLayout doInBackground(Void... params) {
			
			LayoutInflater inflater = getActivity().getLayoutInflater();
			
			View view, view2;
			
			//it's possible to have more events at the same date
			//actuallDate is used to look for this cases
			java.util.Calendar actuallDate = java.util.Calendar.getInstance();
			
			//holds the StartDate and time of the event we are looking at 
			java.util.Calendar tempDateStart = java.util.Calendar.getInstance();
			//holds the EndDate and time of the event we are looking at
			java.util.Calendar tempDateEnd = java.util.Calendar.getInstance();
				        
			view2 = inflater.inflate(R.layout.empty_layout,_Layout, false);
			emptyLayout = (LinearLayout) view2.findViewById(R.id.empty_Layout);
			
			emptyLayout.removeAllViews();
			try {
				
				//needs to be set somewhere in the (far) past to make the following code work (see: switch/case 0)
				actuallDate.setTime(SDF.parse("20000101T123456"));
				
				FileInputStream fin= new FileInputStream(file);

				Calendar calendar = _CalendarBuilder.build(fin);

				Vector<Component> daten = sortData(calendar);

				for(int i = 0; i < daten.size(); i++){

					//parses the entry of the time into somthing we can work with
					//little bug here: Hour_of_day is 0 where it should be 12
					tempDateStart.setTime(SDF.parse(daten.get(i).getProperty(Property.DTSTART).getValue()));
					if(tempDateStart.get(java.util.Calendar.HOUR_OF_DAY) == 0)
						tempDateStart.set(java.util.Calendar.HOUR_OF_DAY, 12);
					tempDateEnd.setTime(SDF.parse(daten.get(i).getProperty(Property.DTEND).getValue()));
					if(tempDateEnd.get(java.util.Calendar.HOUR_OF_DAY) == 0)
						tempDateEnd.set(java.util.Calendar.HOUR_OF_DAY, 12);

					//tests if date is already in the past and not ended yet. If so the event is not used and we jump to the next entry
					if(tempDateEnd.before(today)){
						continue;
					}
					
					for(int j = 0; j<5; j++){
						
						//to dynamically add Views to the application, we created a simple textLayout with a LinearLayout and two child views
						//this textLayout is created for every iteration and inserted into our _Layout of this Fragment (see end of switch/case)
						view = inflater.inflate(R.layout.vorlesung_textlayout,emptyLayout, false);

						LinearLayout layoutToDisplay = (LinearLayout) view.findViewById(R.id.vorlesung_Textlayout);

						switch(j){
						//Note: TextSize should always be set in SP, to scale with different screen sizes
						
						//prints the first line of the schedule -> Montag: 12.12.1900
							case 0:
								//tests if the date is already shown. If not this date is used for the header.
								//also saves date in actualDate for next iteration to check
								//actualDate is first set to somewhere in the past (see above)
								if (tempDateStart.get(java.util.Calendar.DAY_OF_YEAR) > actuallDate.get(java.util.Calendar.DAY_OF_YEAR)
										|| tempDateStart.get(java.util.Calendar.YEAR) > actuallDate.get(java.util.Calendar.YEAR)) {
									actuallDate.setTime(tempDateStart.getTime());
									((TextView) (layoutToDisplay.getChildAt(0))).setTypeface(Typeface.DEFAULT_BOLD);
									((TextView) (layoutToDisplay.getChildAt(0))).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
									((TextView) (layoutToDisplay.getChildAt(0))).setText(getDay(tempDateStart.get(java.util.Calendar.DAY_OF_WEEK) - 1)
												+ ": "
												+ tempDateStart.get(java.util.Calendar.DATE)
												+ "."
												+ (tempDateStart.get(java.util.Calendar.MONTH) + 1)
												+ "."
												+ tempDateStart.get(java.util.Calendar.YEAR));
									//childAt(1) is View to show a line under the TextView. We set it HFULight = green to underline the header.
									((View) (layoutToDisplay.getChildAt(1))).setBackgroundResource(R.color.HFULight);
								}
								else{
									//if date was already printed before, we end up here
									//we set Visibility of the ChildAt(0) (the TextView) to GONE, because it has no Text set
									//and would otherwise take some space that would result in a empty row
					        		layoutToDisplay.getChildAt(0).setVisibility(View.GONE);
					        		//CildAt(1) is a View to display a line
					        		//we set it GrayLight because ifwe end up here there is a second event at the same date
					        		//the gray line is displayed between the Events to make it more appealing
					        		((View) (layoutToDisplay.getChildAt(1))).setBackgroundResource(R.color.HFUGrayLight);
								}
								break;
							case 1:
								//prints the second line, shows the course name
								((TextView) (layoutToDisplay.getChildAt(0))).setTypeface(Typeface.DEFAULT_BOLD);
								//the description is not only the course name, but also, in some cases some additional info, the name of the professor
								//but the course name is always in the first part and its parted by an '\n'
								((TextView) (layoutToDisplay.getChildAt(0))).setText(daten.get(i).getProperty(Property.DESCRIPTION).getValue().split("\n")[0]);
								((TextView) (layoutToDisplay.getChildAt(0))).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
								//we set ChildAt(1) ( View to show line under textView) to White so that we don't see it. So we also get a little space between entries
								((View) (layoutToDisplay.getChildAt(1))).setBackgroundResource(R.color.White);						
								break;
							case 2:	
								//prints the third line, shows the remaining description after the course name
								
								String tempString = "";
								((TextView) (layoutToDisplay.getChildAt(0))).setTypeface(Typeface.DEFAULT);
								((TextView) (layoutToDisplay.getChildAt(0))).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
								//we iterate through all remaining description entries and save them in one string to be able to print it in one line
								for(int part = 1; part < daten.get(i).getProperty(Property.DESCRIPTION).getValue().split("\n").length; part++)
									if( part < 2 || daten.get(i).getProperty(Property.DESCRIPTION).getValue().split("\n").length-1 > part)
										tempString += daten.get(i).getProperty(Property.DESCRIPTION).getValue().split("\n")[part] + " ";
									else
										tempString += "\n"+daten.get(i).getProperty(Property.DESCRIPTION).getValue().split("\n")[part] + " ";
								
								((TextView) (layoutToDisplay.getChildAt(0))).setText(tempString);
								((View) (layoutToDisplay.getChildAt(1))).setBackgroundResource(R.color.White);
								break;
								
							case 3:
								//prints the fourth line, shows the start and end time of this event
								((TextView) (layoutToDisplay.getChildAt(0))).setTypeface(Typeface.DEFAULT);
								((TextView) (layoutToDisplay.getChildAt(0))).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
								//libary was bugged here, had to do some workaround, casts are needed to make it work
								((TextView) (layoutToDisplay.getChildAt(0))).setText(
										//hour_of_day showed "0" when hour was actually "12". so we look if it's 0 and then print 12, otherwise we print the hour
										((CharSequence) (tempDateStart.get(java.util.Calendar.HOUR_OF_DAY) == 0 ? "12" : Integer.toString(tempDateStart.get(java.util.Calendar.HOUR_OF_DAY))))+
										":" +
										//Minute showed "0" when minute was "00". we simply print 00 or otherwise the actual minute
										((CharSequence) (tempDateStart.get(java.util.Calendar.MINUTE) == 0 ? "00" : Integer.toString(tempDateStart.get(java.util.Calendar.MINUTE)))) +
										" - " +
										((CharSequence) (tempDateEnd.get(java.util.Calendar.HOUR_OF_DAY) == 0 ? "12" : Integer.toString(tempDateEnd.get(java.util.Calendar.HOUR_OF_DAY)))) +
										":" +
										((CharSequence) (tempDateEnd.get(java.util.Calendar.MINUTE) == 0 ? "00" : Integer.toString(tempDateEnd.get(java.util.Calendar.MINUTE)))) +
										" Uhr"
										);
								((View) (layoutToDisplay.getChildAt(1))).setBackgroundResource(R.color.White);
								break;
								
							case 4:
								//prints the fifth line, shows the location
								((TextView) (layoutToDisplay.getChildAt(0))).setTypeface(Typeface.DEFAULT);
								((TextView) (layoutToDisplay.getChildAt(0))).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
								((TextView) (layoutToDisplay.getChildAt(0))).setText("Raum: "+daten.get(i).getProperty(Property.LOCATION).getValue());
								((View) (layoutToDisplay.getChildAt(1))).setBackgroundResource(R.color.White);
								break;
								
						}
						emptyLayout.addView(layoutToDisplay);
					}
				}
									
				} catch (Exception e) {
					Toast.makeText(getActivity(), "ERROR: "+ e.toString(), Toast.LENGTH_LONG).show();
				}		

			return emptyLayout;
		}
		
	}
	
private void setFileName() throws InterruptedException, ExecutionException{
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB1")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.AIB1)+".ics";
			createFile();				
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB2")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.AIB2)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB3")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.AIB3)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB4")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.AIB4)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB6")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.AIB6)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB1")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.CNB1)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB2")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.CNB2)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB3")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.CNB3)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB4")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.CNB4)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB6")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.CNB6)+".ics";
			createFile();
			return;
		}
	
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB1")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.SPB1)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB2")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.SPB2)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB3")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.SPB3)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB4")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.SPB4)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB6")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.SPB6)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("MOS1")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.MOS1)+".ics";
			createFile();
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("MOS2")){
			fileName = "Vorlesungsplan_"+ getActivity().getString(R.string.MOS2)+".ics";
			createFile();
			return;
		}
		

	}
	
	
	private void createFile() {

		file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
				"/MOS_vorlesungsplan/" + fileName);

	}
	
private void getFileLinkAndShowFile(){
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB1")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.AIB1_vorlesung_link));
				
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB2")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.AIB2_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB3")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.AIB3_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB4")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.AIB4_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("AIB6")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.AIB6_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB1")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.CNB1_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB2")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.CNB2_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB3")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.CNB3_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB4")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.CNB4_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("CNB6")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.CNB6_vorlesung_link));
			return;
		}
	
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB1")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.SPB1_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB2")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.SPB2_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB3")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.SPB3_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB4")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.SPB4_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("SPB6")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.SPB6_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("MOS1")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.MOS1_vorlesung_link));
			return;
		}
		
		if(_SpinnerSemester.getSelectedItemPosition() == adapterSemster.getPosition("MOS2")){
			asynkTaskHTML = new DownloadFileAndShow();
			asynkTaskHTML.execute(getActivity().getString(R.string.MOS2_vorlesung_link));
			return;
		}
		

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}

}

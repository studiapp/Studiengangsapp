package de.hfu.mos.webmail;

import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import de.hfu.mos.R;

public class WebmailFragment extends Fragment {
	
	private WebView _WebView;
	private DownloadListener test;
//	private DownloadManager _DownloadManager;
		
	//String to display when no internet available:
    private String noInternet = "<html><body>No internet available! Try again later.</body></html>";
	
	public WebmailFragment(/*DownloadManager dm*/) {
//		_DownloadManager = dm;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_webmail, container, false);

		_WebView = (WebView) rootView.findViewById(R.id.webView_WebMail);
		
		
		//Download of attachment is not working, because website uses TSL to verify the user and session
		//siply giving the cookie to the request woun't work
		//maybe you have to use HttpsUrlConnection to make this work
		//below you find a class test that at least can download for starters
		
//		test = new DownloadListener() {
//
//		    @Override
//		    public void onDownloadStart(String url, String userAgent,
//		            String contentDisposition, String mimetype, long contentLength) {
//  
////				test testtest = new test();
////
////		    	testtest.execute(url);
//		    	
//		    	CookieManager cookieMngr = CookieManager.getInstance();
//		    	String cookie = cookieMngr.getCookie(url);
//	
//		    	Log.d("Cookies: ", cookie);
//		    	Log.d("Cookie1: ", cookie.split("; ")[0]);
//		    	Log.d("Cookie2: ", cookie.split("; ")[1]);
//		    	Log.d("Cookie3: ", cookie.split("; ")[2]);
//		        Request request = new Request(Uri.parse(url));
//		        request.allowScanningByMediaScanner();
//		        request.setMimeType(mimetype);
//		        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//		        request.setDestinationInExternalPublicDir(
//		                    Environment.DIRECTORY_DOWNLOADS,
//		                    contentDisposition.substring(
//		                        contentDisposition.lastIndexOf("filename=\"")+10,
//		                        contentDisposition.length()-1)); //getting the filename
//		        request.addRequestHeader("Cookie", cookie);
//		        _DownloadManager.enqueue(request);
//
//		    }
//
//		};
		
		webLogin();
	
		return rootView;
	}

	
    @Override
    public void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        _WebView.saveState(outState);
    }
	
    //Handles webView:
	public void webLogin() {

//		_WebView.setDownloadListener(test);
		
		_WebView.getSettings().setJavaScriptEnabled(true);

		_WebView.getSettings().setUseWideViewPort(true);

		_WebView.getSettings().setLoadWithOverviewMode(true);

		_WebView.getSettings().setBuiltInZoomControls(true);

		_WebView.getSettings().setDisplayZoomControls(false);

		_WebView.setWebViewClient(new WebViewClient());

		_WebView.setWebChromeClient(new WebChromeClient());

		if (isOnline())
			_WebView.loadUrl("https://webmail.hs-furtwangen.de/ox.html");
		else
			_WebView.loadData(noInternet, "text/html", null);

	}
	

	//looks for onlinestate //Redundanz WebMail <-> FelixLogin <-> Website
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
 
    /*
    private class test extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
	        
			String url = params[0];
	    	URL blubb = null;
	    	HttpsURLConnection urlConnection;
			try {
				blubb = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
	    	
			try {
				urlConnection = (HttpsURLConnection)blubb.openConnection();
				
		        urlConnection.connect();
		        
				BufferedInputStream br = 
							new BufferedInputStream(urlConnection.getInputStream());
				
				new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/").mkdirs();
				
				File writeTo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/test.pdf");
				writeTo.createNewFile();
				FileOutputStream output = new FileOutputStream(writeTo);
					
				byte[] buffer = new byte[64000];
				
				int sizeToWrite = 0;
				while ( ( sizeToWrite = br.read(buffer)) != -1){
										
					output.write(buffer, 0, sizeToWrite);
			   }
			   br.close();
			   output.flush();
			   output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}
    	
    }
*/
}

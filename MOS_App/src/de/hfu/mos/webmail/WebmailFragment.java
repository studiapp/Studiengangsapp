package de.hfu.mos.webmail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import de.hfu.mos.ConnectionDetector;
import de.hfu.mos.R;

public class WebmailFragment extends Fragment {
	
	private WebView _WebView;
//	private DownloadListener test;
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

		/*
		test = new DownloadListener() {

		    @Override
		    public void onDownloadStart(String url, String userAgent,
		            String contentDisposition, String mimetype, long contentLength) {
 
//				test testtest = new test();
//
//		    	testtest.execute(url);
		    	
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

		    }

		};
		*/
		webLogin();
	
		return rootView;
	}

	public static void copyInputStreamToOutputStream(final InputStream in,
	        final OutputStream out) throws IOException
	{
	    try
	    {
	        try
	        {
	            final byte[] buffer = new byte[1024];
	            int n;
	            while ((n = in.read(buffer)) != -1)
	                out.write(buffer, 0, n);
	        }
	        finally
	        {
	            out.close();
	        }
	    }
	    finally
	    {
	        in.close();
	    }
	}
	
    @Override
    public void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        _WebView.saveState(outState);
    }
	
    //Handles webView:
	@SuppressLint("SetJavaScriptEnabled")
	//javascript needed for login
	public void webLogin() {

//		_WebView.setDownloadListener(test);
		
		WebSettings settings = _WebView.getSettings();
		settings.setJavaScriptEnabled(true);

		settings.setUseWideViewPort(true);

		settings.setLoadWithOverviewMode(true);

		settings.setBuiltInZoomControls(true);

		settings.setDisplayZoomControls(false);

		_WebView.setWebViewClient(new WebViewClient());

		_WebView.setWebChromeClient(new WebChromeClient());

		if (ConnectionDetector.isOnline(getActivity()))
			_WebView.loadUrl("https://webmail.hs-furtwangen.de/ox.html");
		else
			_WebView.loadData(noInternet, "text/html", null);

	}
	
 
 /*   
    private class test extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
	        
		    try{
		    	new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/").mkdirs();
		    	FileOutputStream f = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/test.pdf");
		    	
		    	// Load CAs from an InputStream
		    	// (could be from a resource or ByteArrayInputStream or ...)
		    	CertificateFactory cf = CertificateFactory.getInstance("X.509");
		    	// From https://www.washington.edu/itconnect/security/ca/load-der.crt
		    	InputStream caInput = new BufferedInputStream(new FileInputStream("https://www.washington.edu/itconnect/security/ca/load-der.crt"));
		    	Certificate ca;
		    	try {
		    	    ca = cf.generateCertificate(caInput);
		    	    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
		    	} finally {
		    	    caInput.close();
		    	}

		    	// Create a KeyStore containing our trusted CAs
		    	String keyStoreType = KeyStore.getDefaultType();
		    	KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		    	keyStore.load(null, null);
		    	keyStore.setCertificateEntry("ca", ca);

		    	// Create a TrustManager that trusts the CAs in our KeyStore
		    	String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		    	TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		    	tmf.init(keyStore);

		    	// Create an SSLContext that uses our TrustManager
		    	SSLContext context = SSLContext.getInstance("TLS");
		    	context.init(null, tmf.getTrustManagers(), null);

		    	// Tell the URLConnection to use a SocketFactory from our SSLContext
		    	URL url = new URL(params[0]);
		    	HttpsURLConnection urlConnection =
		    	    (HttpsURLConnection)url.openConnection();
		    	urlConnection.setSSLSocketFactory(context.getSocketFactory());
		    	InputStream in = urlConnection.getInputStream();
		    	
		    	byte[] buffer = new byte[1024];
		        int len1 = 0; 
		        while ((len1 = in.read(buffer)) > 0) {
		            f.write(buffer, 0, len1);
		            Log.d("downloader","downloading");
		        }

		        f.close();
		        
		    } catch (Exception e){
		    	Log.d("Exception: ", e+"");
		    }
		    
			return null;
//			String url = params[0];
//	    	URL blubb = null;
//	    	HttpsURLConnection urlConnection;
//			try {
//				blubb = new URL(url);
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//	    	
//			try {
//				urlConnection = (HttpsURLConnection)blubb.openConnection();
//				
//		        urlConnection.connect();
//		        
//				BufferedInputStream br = 
//							new BufferedInputStream(urlConnection.getInputStream());
//				
//				new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/").mkdirs();
//				
//				File writeTo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/test.pdf");
//				writeTo.createNewFile();
//				FileOutputStream output = new FileOutputStream(writeTo);
//					
//				byte[] buffer = new byte[64000];
//				
//				int sizeToWrite = 0;
//				while ( ( sizeToWrite = br.read(buffer)) != -1){
//										
//					output.write(buffer, 0, sizeToWrite);
//			   }
//			   br.close();
//			   output.flush();
//			   output.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			return null;
		}
    	
    }
*/
}

package de.hfu.mos.home;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import de.hfu.mos.ConnectionDetector;
import de.hfu.mos.R;

public class FelixFragment extends Fragment {
	
	private WebView _WebView;
	private DownloadManager _DownloadManager;
		
	//String to display when no internet available:
    private String noInternet = "<html><body>No internet available! Try again later.</body></html>";
	
	public FelixFragment(DownloadManager dm) {

		_DownloadManager = dm;
	}
	
	public FelixFragment() {
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_felix, container, false);

		_WebView = (WebView) rootView.findViewById(R.id.webView_Felix);
		
		webLogin();
	
		return rootView;
	}
	
	//Enables WebView to go page back via device back button
	//also look at MainActivity onBackPressed() method that uses this method
	public boolean webGoBack() {
	    if (_WebView.canGoBack()) {
	        _WebView.goBack();
	        return true;
	    } else {
	        return false;
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
	// javascript needed to make navigation work
	public void webLogin() {

		WebSettings settings = _WebView.getSettings();
		
		settings.setJavaScriptEnabled(true);

		settings.setUseWideViewPort(true);

		settings.setLoadWithOverviewMode(true);

		settings.setBuiltInZoomControls(true);

		settings.setDisplayZoomControls(false);

		_WebView.setWebViewClient(new WebViewClient() {

			// forces page to open in webView instead of Browser
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
								
				if (url.contains(".pdf") ||
						url.contains(".doc") ||
						url.contains(".txt") ||
						url.contains(".zip") ||
						url.contains(".rar") ||
						url.contains(".png")) {
				
					String cookie = CookieManager.getInstance().getCookie(url);
					Request request = new Request(Uri.parse(url));
					request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
					request.setDestinationInExternalPublicDir(
							Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf("/", url.length())));
					request.addRequestHeader("Cookie", cookie);
					_DownloadManager.enqueue(request);

				} else
				view.loadUrl(url);

				return true;
			}
		});

		_WebView.setWebChromeClient(new WebChromeClient());

		if (ConnectionDetector.isOnline(getActivity()))
			_WebView.loadUrl("https://felix.hs-furtwangen.de/dmz/");
		else
			_WebView.loadData(noInternet, "text/html", null);

	}

}

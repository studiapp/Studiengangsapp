package de.hfu.mos.website;

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

public class WebsiteFragment extends Fragment {
	
	private String url;
	private WebView _WebView;
	
	//This string shows when internet is not available
	private String _noInternet = "<html><body>No internet available! Try again later.</body></html>";
	
	public WebsiteFragment() {

        url = "http://www.hs-furtwangen.de/willkommen.html";
	}

    public WebsiteFragment(String search) {

        url = "https://www.google.de/m/search?q=test&ie=utf-8&oe=utf-8&gws_rd=cr&ei=b5eNVNfpGY33aomMgbgD#q=" + search +" +site:www.hs-furtwangen.de";
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_website, container, false);

		_WebView = (WebView) rootView.findViewById(R.id.webView_Website);
		
		loadWebsite();
		
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
	//javascript enables better navigation of the website
	public void loadWebsite() {

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
				view.loadUrl(url);

				return true;
			}
		});

		_WebView.setWebChromeClient(new WebChromeClient());

		if (ConnectionDetector.isOnline(getActivity()))
			_WebView.loadUrl(url);
		else
			_WebView.loadData(_noInternet, "text/html", null);

	}

}

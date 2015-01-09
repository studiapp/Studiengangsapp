package de.hfu.mos;

import de.hfu.mos.R;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
	public void loadWebsite() {

		_WebView.getSettings().setJavaScriptEnabled(true);

		_WebView.getSettings().setUseWideViewPort(true);

		_WebView.getSettings().setLoadWithOverviewMode(true);

		_WebView.getSettings().setBuiltInZoomControls(true);

		_WebView.getSettings().setDisplayZoomControls(false);

		_WebView.setWebViewClient(new WebViewClient() {

			// forces page to open in webView instead of Browser
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);

				return true;
			}
		});

		_WebView.setWebChromeClient(new WebChromeClient());

		if (isOnline())
			_WebView.loadUrl(url);
		else
			_WebView.loadData(_noInternet, "text/html", null);

	}
	

	//looks for onlinestate //Redundanz WebMail <-> FelixLogin <-> Website
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

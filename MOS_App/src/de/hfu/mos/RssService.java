package de.hfu.mos;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import de.hfu.mos.data.RssItem;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.widget.Toast;

public class RssService extends IntentService {
	 
    private static final String RSS_LINK = "http://www.hs-furtwangen.de/willkommen.html?type=100";
    public static final String ITEMS = "items";
    public static final String RECEIVER = "receiver";
 
    public RssService() {
        super("RssService");
    }
 
    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.d(Constants.TAG, "Service started");
        List<RssItem> rssItems = null;{
        try {
            PcWorldRssParser parser = new PcWorldRssParser();
            rssItems = parser.parse(getInputStream(RSS_LINK));
        } catch (XmlPullParserException e) {
            Log.w(e.getMessage(), e);
        } catch (IOException e) {
            Log.w(e.getMessage(), e);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEMS, (Serializable) rssItems);
        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
        receiver.send(0, bundle);
        }
        }
    
 
    public InputStream getInputStream(String link) {
        try {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            //Log.w(Constants.TAG, "Exception while retrieving the input stream", e);
            return null;
        }
    }
    

    
}
package de.hfu.mos.home.news;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.hfu.mos.ConnectionDetector;
import de.hfu.mos.R;
import de.hfu.mos.home.news.data.RssItem;
import de.hfu.mos.home.news.listeners.ListListener;

/**
 * Created by Michael on 29.10.2014.
 */
public class RSSReaderFragment extends Activity{// A reference to the local object
    private RSSReaderFragment local;

    /**
     * This method creates main application view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set view
        setContentView(R.layout.layout_rssreader);

        // Set reference to this activity
        local = this;

        GetRSSDataTask task = new GetRSSDataTask();

        // Start download RSS task
        if(ConnectionDetector.isOnline(this))
        	task.execute("http://www.hs-furtwangen.de/willkommen.html?type=100");
        else{
        	Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
        // Debug the thread name
        Log.d("hfureader", Thread.currentThread().getName());
    }  
    
    public class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > {
        @Override
        protected List<RssItem> doInBackground(String... urls) {

            // Debug the task thread name
            Log.d("hfureader", Thread.currentThread().getName());

            try {
                // Create RSS reader
                RssReader rssReader = new RssReader(urls[0]);

                // Parse RSS, get items
                return rssReader.getItems();

            } catch (Exception e) {
                Log.e("hfureader", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RssItem> result) {

            // Get a ListView from main view
            ListView itcItems = (ListView) findViewById(R.id.listRSS);

            // Create a list adapter
            ArrayAdapter<RssItem> adapter = new ArrayAdapter<RssItem>(local,android.R.layout.simple_list_item_1, result);
            // Set list adapter for the ListView
            itcItems.setAdapter(adapter);

            // Set list view item click listener
            itcItems.setOnItemClickListener(new ListListener(result, local));
        }

    }


}

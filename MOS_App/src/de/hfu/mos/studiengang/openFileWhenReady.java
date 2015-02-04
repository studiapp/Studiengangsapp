package de.hfu.mos.studiengang;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This class opens the downloaded file with an installed application.
 * <br>
 * Needs three parameter in execute()
 * <p>
 * openFileWhenReady.execute(<b>request</b>, <b>downloadmanager</b>, <b>context</b>);
 * @param request : the request used by downloadmanager.
 * @param downloadmanager : the downloadmanager itself
 * @param context : the activity that we are on ( usually getActivity() )
 */

public class openFileWhenReady extends AsyncTask<Object, Void, Void> {

	private long id;

	private Request request;
	private DownloadManager _DownloadManager;
	private Activity context;
	
	protected void onPostExecute(Void result) {

		Intent LaunchIntent = new Intent(Intent.ACTION_VIEW);
		Uri uri = _DownloadManager.getUriForDownloadedFile(id);
		LaunchIntent.setDataAndType(uri, "application/pdf");
		context.startActivity(LaunchIntent);

	};

	@Override
	protected Void doInBackground(Object... params) {

		if(params.length != 3){
			Log.d("ERROR", "openFileWhenReady needs 3 parameter");
			return null;
		}
		request = (Request) params[0];
		_DownloadManager = (DownloadManager) params[1];
		context = (Activity) params[2];
		
		id = _DownloadManager.enqueue(request);
		
		//wait while file is still loading
		while (_DownloadManager.getUriForDownloadedFile(id) == null) {

		}

		return null;
	}

}

package de.hfu.mos;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hfu.mos.kontakte.adapter.CustomListAdapter;
import de.hfu.mos.kontakte.app.AppController;
import de.hfu.mos.kontakte.model.Kontakt;

public class KontaktFragment extends Fragment {

    private static final String TAG = KontaktFragment.class.getSimpleName();

    // kontakt json url
    private static final String url = "http://141.28.100.57/kontakt.json";
    private ProgressDialog pDialog;
    private List<Kontakt> kontaktList = new ArrayList<Kontakt>();
    private ListView listView;
    private CustomListAdapter adapter;
    String tag_json_arry = "json_array_req";

    public KontaktFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_kontakte, container, false);

        listView = (ListView) rootView.findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), kontaktList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        JsonArrayRequest kontaktReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Kontakt kontakt = new Kontakt();
                                kontakt.setName(obj.getString("name"));
                                kontakt.setThumbnailUrl(obj.getString("image"));
                                kontakt.setFunction(obj.getString("function"));
                                kontakt.setTel(obj.getString("tel"));
                                kontakt.setMail(obj.getString("mail"));
                                kontakt.setRoom(obj.getString("room"));

                                // adding kontakt to kontakt array
                                kontaktList.add(kontakt);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(kontaktReq, tag_json_arry);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}

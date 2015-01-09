package de.hfu.mos.kontakte.adapter;

/**
 * Created by gera on 21.12.14.
 */
import de.hfu.mos.R;
import de.hfu.mos.kontakte.app.AppController;
import de.hfu.mos.kontakte.model.Kontakt;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Kontakt> kontaktItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Kontakt> kontaktItems) {
        this.activity = activity;
        this.kontaktItems = kontaktItems;
    }

    @Override
    public int getCount() {
        return kontaktItems.size();
    }

    @Override
    public Object getItem(int location) {
        return kontaktItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView function = (TextView) convertView.findViewById(R.id.function);
        TextView tel = (TextView) convertView.findViewById(R.id.tel);
        TextView mail = (TextView) convertView.findViewById(R.id.mail);
        TextView room = (TextView) convertView.findViewById(R.id.room);

        // getting movie data for the row
        Kontakt m = kontaktItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // name
        name.setText(m.getName());

        // function
        function.setText(m.getFunction());

        // tel

        tel.setText(m.getTel());

        // mail
        mail.setText(m.getMail());

        // room
        room.setText(m.getRoom());

        return convertView;
    }

}

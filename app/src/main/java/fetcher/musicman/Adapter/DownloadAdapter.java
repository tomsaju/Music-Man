package fetcher.musicman.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fetcher.musicman.Models.Song;
import fetcher.musicman.R;

/**
 * Created by Dreams on 12-Aug-17.
 */
public class DownloadAdapter extends BaseAdapter {
    ArrayList<Song> songs;
    Context context;

    public DownloadAdapter(ArrayList<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.downloadlistitem,null);
        TextView title =(TextView) view.findViewById(R.id.title_dlist);
        TextView location = (TextView) view.findViewById(R.id.location_dlist);
        TextView status = (TextView) view.findViewById(R.id.status_dlist);

        title.setText(songs.get(i).getTitle());
        location.setText(songs.get(i).getDownloadLocation());

        String downloadStatus = songs.get(i).getStatus();
        if(downloadStatus.equals("0")){
            status.setText("Failed");
            status.setTextColor(Color.RED);
        }else if(downloadStatus.equals("1")){
            status.setText("Completed");
            status.setTextColor(Color.GREEN);
        }else if(downloadStatus.equals("2")){
            status.setText("Downloading");
            status.setTextColor(Color.GREEN);
        }


        return view;
    }
}

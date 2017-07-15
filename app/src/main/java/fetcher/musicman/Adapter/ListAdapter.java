package fetcher.musicman.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fetcher.musicman.Activity.DetailPage;
import fetcher.musicman.Models.Song;
import fetcher.musicman.R;

/**
 * Created by tom.saju on 7/10/2017.
 */

public class ListAdapter extends BaseAdapter {

    ArrayList<Song> songList;
    Context context;

    public ListAdapter(Context context, ArrayList<Song> songList) {
       this.songList = songList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_layout,null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView singer = (TextView) convertView.findViewById(R.id.singer);
        LinearLayout mainLayoutListItem = (LinearLayout) convertView.findViewById(R.id.main_layout_list_item);

        title.setText(songList.get(position).getTitle());
        singer.setText(songList.get(position).getSinger());
        mainLayoutListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailPage.class);
                i.putExtra("title",songList.get(position).getTitle());
                i.putExtra("singer",songList.get(position).getSinger());
                context.startActivity(i);
            }
        });
        return convertView;
    }
}

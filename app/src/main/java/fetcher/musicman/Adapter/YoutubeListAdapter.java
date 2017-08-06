package fetcher.musicman.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import fetcher.musicman.Models.Song;
import fetcher.musicman.R;
import fetcher.musicman.controller.Detail.IDetailController;
import fetcher.musicman.controller.Detail.IDetailView;

/**
 * Created by Dreams on 15-Jul-17.
 */
public class YoutubeListAdapter extends BaseAdapter {
    IDetailView iDetailView;
    ArrayList<Song> songList;
    Context context;

    public YoutubeListAdapter(IDetailView iDetailView, ArrayList<Song> songList, Context context) {
        this.iDetailView = iDetailView;
        this.songList = songList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
       LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       view = inflater.inflate(R.layout.youtube_list_item,null);
        RelativeLayout layoutParent = (RelativeLayout) view.findViewById(R.id.layout_parent);
        TextView title = (TextView)view.findViewById(R.id.title_text);
        title.setText(songList.get(i).getTitle());
        layoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iDetailView.onListItemClicked(songList.get(i));
            }
        });

        return view;
    }
}

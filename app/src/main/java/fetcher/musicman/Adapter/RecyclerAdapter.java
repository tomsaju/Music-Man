package fetcher.musicman.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.ArrayList;

import fetcher.musicman.Fragment.RecyclerListener;
import fetcher.musicman.Models.Song;
import fetcher.musicman.R;

/**
 * Created by Dreams on 05-Aug-17.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.SongViewHolder> implements View.OnClickListener {

    private ArrayList<Song> songArrayList;
    private Context mContext;
    RecyclerListener recyclerListener;


    public class SongViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title,artist;
        public ImageView albumArt;
        public RelativeLayout cardParent;

        public SongViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textview_album_title);
            artist = (TextView) itemView.findViewById(R.id.textview_artist_name);
            albumArt = (ImageView) itemView.findViewById(R.id.imageview_album_art);
            cardParent = (RelativeLayout) itemView.findViewById(R.id.text_card_background);
        }
    }


    public RecyclerAdapter(ArrayList<Song> songArrayList,RecyclerListener recyclerListener){
        this.songArrayList = songArrayList;
        this.recyclerListener=recyclerListener;
    }

    @Override
    public RecyclerAdapter.SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_grid_item_layout,parent,false);
        itemView.setOnClickListener(this);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.SongViewHolder holder, int position) {

        Song song = songArrayList.get(position);
        holder.artist.setText(song.getSinger());
        holder.title.setText(song.getTitle());
        Glide.with(mContext).load(song.getAlbumArtUrl())
                .listener(GlidePalette.with(song.getAlbumArtUrl())
                                .use(GlidePalette.Profile.VIBRANT)
                                .intoBackground(holder.cardParent, GlidePalette.Swatch.RGB)
                                .crossfade(true)
                )
        .into((holder.albumArt));


    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }


    @Override
    public void onClick(View view) {
        recyclerListener.onClick(view);
    }
}

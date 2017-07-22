package fetcher.musicman.controller.Main;

import java.util.ArrayList;

import fetcher.musicman.Models.Song;

/**
 * Created by tom.saju on 7/12/2017.
 */

public interface IMainListener {
    void onDownloadComplete(String filename);
    void progressUpdate(String... progress);
    void onSongsFetched(ArrayList<Song> songsList);
}

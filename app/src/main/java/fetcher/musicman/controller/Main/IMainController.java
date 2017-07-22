package fetcher.musicman.controller.Main;

import java.util.ArrayList;

import fetcher.musicman.Models.Song;

/**
 * Created by tom.saju on 7/12/2017.
 */

public interface IMainController  {
    void saveSong(String url);
    ArrayList<Song> retrieveAlbumArt(ArrayList<Song> songList);
    void getSongs(String url);
}

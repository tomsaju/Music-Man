package fetcher.musicman.controller.Main;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import fetcher.musicman.Application.VolleyApplication;
import fetcher.musicman.Models.Song;

/**
 * Created by tom.saju on 7/12/2017.
 */

public class MainController implements IMainController {

    private static final String TAG = "MainController";
    Context context;
    IMainListener iMainListener;
    public String CoverDownloadBaseURL = "http://covers.slothradio.com/?adv=";
    public String artist;
    public String album;
    public MainController(Context context,IMainListener iMainListener) {
        this.context = context;
        this.iMainListener = iMainListener;
    }

    @Override
    public void saveSong(String url) {

    }

    @Override
    public ArrayList<Song> retrieveAlbumArt(ArrayList<Song> songList) {
        ArrayList<Song> newSongList=new ArrayList<>();
        for(int i=0;i<songList.size();i++){
            Song song = getAlbumArt(songList.get(i));
        }
        return newSongList;
    }

    private Song getAlbumArt(Song song) {
        if(song.getTitle()!=null&&song.getSinger()!=null) {
            artist = "&artist=" + song.getSinger().trim().replace(" ", "+");
            album = "&album=" + song.getTitle().trim().replace(" ", "+");
            String url = CoverDownloadBaseURL+artist+album;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Document doc = Jsoup.parse(s);
                    Log.d(TAG, "onResponse ");
                    System.out.println(doc.select("div[name=album0]"));

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("Error");
                }
            });

            VolleyApplication.getInstance().getRequestQueue().add(stringRequest);
        }
    return song;
    }
}

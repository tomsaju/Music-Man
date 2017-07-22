package fetcher.musicman.controller.Main;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
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
    Song nSong;
    ArrayList<Song> songList;
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
        Song song = getAlbumArt(songList.get(0));
        newSongList.add(song);
        return newSongList;
    }


    @Override
    public void getSongs(String url) {
         songList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject feed = response.getJSONObject("feed");
                            JSONArray results = feed.getJSONArray("results");

                            //String videoid = get video id
                            if (results != null) {

                                for (int n = 0; n < results.length(); n++) {

                                    JSONObject object = results.getJSONObject(n);
                                    String artist = object.getString("artistName");
                                    String title = object.getString("name");
                                    String albumArtCoverUrl = object.getString("artworkUrl100");
                                    Song song = new Song();
                                    song.setTitle(title);
                                    song.setAlbumArtUrl(albumArtCoverUrl);
                                    song.setSinger(artist);
                                    songList.add(song);
                                }

                            }
                            iMainListener.onSongsFetched(songList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                    }
                }
        );

        VolleyApplication.getInstance().getRequestQueue().add(request);

    }

    private Song getAlbumArt(final Song song) {

        if(song.getTitle()!=null&&song.getSinger()!=null) {
            artist = "&artist=" + song.getSinger().trim().replace(" ", "+");
            album = "&album=" + song.getTitle().trim().replace(" ", "+");
            String url = "itunes.apple.com/search?term="+artist+album+"&entity=song";
            JsonObjectRequest request = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int resultCount = Integer.parseInt(response.getString("resultCount"));
                                if(resultCount>0) {

                                    JSONArray items = response.getJSONArray("results");
                                    //String videoid = get video id
                                    if (items != null) {

                                        for (int n = 0; n < items.length(); n++) {

                                            JSONObject object = items.getJSONObject(n);
                                            String artist = object.getString("artistName");
                                            String title = object.getString("trackName");
                                            String albumArtCoverUrl = object.getString("artworkUrl100");
                                            if(albumArtCoverUrl==null||albumArtCoverUrl.isEmpty()){
                                                albumArtCoverUrl = object.getString("artworkUrl60");
                                            }

                                           if(songMatchesWithFetchedItem(artist,title,song)){
                                               nSong = song;
                                               nSong.setAlbumArtUrl(albumArtCoverUrl);
                                               break;
                                           }else{
                                               if(n==items.length()-1){
                                                   nSong = song;
                                               }
                                           }
                                        }
                                        Log.d(TAG, "onResponse: " + response.toString());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {

                            }
                        }
                    },

                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.toString());
                        }
                    }
            );

            VolleyApplication.getInstance().getRequestQueue().add(request);
        }
    return nSong;
    }

    private boolean songMatchesWithFetchedItem(String artist, String title, Song song) {
        //TO DO needs improvement
        boolean match = false;
        String songTitle = song.getTitle();
        String songArtist = song.getSinger();
        if(songTitle!=null&&songArtist!=null){
            if(songTitle.trim().equalsIgnoreCase(title)&&songArtist.trim().equalsIgnoreCase(artist)){
                match = true;
            }else if(songTitle.trim().toLowerCase().contains(title)&&songArtist.trim().toLowerCase().contains(artist)){
                match = true;
            }else{
                match = false;
            }
        }
        return match;
    }
}

package fetcher.musicman.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.florent37.glidepalette.GlidePalette;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import fetcher.musicman.Adapter.YoutubeListAdapter;
import fetcher.musicman.Application.VolleyApplication;
import fetcher.musicman.Models.Song;
import fetcher.musicman.R;
import fetcher.musicman.controller.Detail.DetailController;
import fetcher.musicman.controller.Detail.IDetailController;
import fetcher.musicman.controller.Detail.IDetailView;
import fetcher.musicman.controller.Main.IMainListener;
import fetcher.musicman.helper.DownloadFileAsync;

public class DetailPage extends AppCompatActivity implements IDetailView,IMainListener {
    private static final String TAG = "DetailPage";
    IDetailController mDetailController;
    String title;
    String singer;
    String imageUrl;
    ListView youtubeList;
    TextView titletxt,singertxt,downloadProgress;
    ImageView mainImageView;
    RelativeLayout topPortion;
    ArrayList<Song> youtubeSongList;
    Song selectedSong;
    String youtubeURL;
    String youtubeTitle;
    String MAX_RESULTS = "5";
    String YOUTUBE_API_KEY = "AIzaSyAnoU5N78yVwk2er4dQcvq7bdXCMc6lfJE";
    String REQUEST_BASE_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=";
    String REQUEST_FINAL_PART = "&maxResults="+MAX_RESULTS+"&key="+YOUTUBE_API_KEY;
  //  https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=eminem%20lost&maxResults=2&key=AIzaSyBqXp0Uo2ktJcMRpL_ZwF5inLTWZfsCYqY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        mDetailController = new DetailController(this,this);
        titletxt = (TextView) findViewById(R.id.textView_title);
        singertxt = (TextView) findViewById(R.id.textView_singer);
        downloadProgress = (TextView) findViewById(R.id.download_progress);
        downloadProgress.setVisibility(View.INVISIBLE);
        topPortion = (RelativeLayout) findViewById(R.id.top_portion);
        youtubeList = (ListView) findViewById(R.id.youtube_listview);
        mainImageView = (ImageView) findViewById(R.id.imageview_main);
        this.registerForContextMenu(youtubeList);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            youtubeURL = extras.getString(Intent.EXTRA_TEXT);
            youtubeTitle = extras.getString(Intent.EXTRA_SUBJECT);
            title = getIntent().getStringExtra("title");
            singer = getIntent().getStringExtra("singer");
            imageUrl = getIntent().getStringExtra("imageurl");
        }
        if(youtubeURL!=null&&!youtubeURL.isEmpty()){
            if(youtubeTitle!=null&&!youtubeTitle.isEmpty()){
                String tempTitle = youtubeTitle.substring(youtubeTitle.indexOf("\""),youtubeTitle.lastIndexOf("\""));
                if(tempTitle!=null&&!tempTitle.isEmpty()){
                    titletxt.setText(tempTitle);
                    Song song = new Song();
                    song.setTitle(tempTitle);
                    selectedSong = song;
                }else{
                    long currentTimeStamp = System.currentTimeMillis();
                    Song song = new Song();
                    song.setTitle(String.valueOf(currentTimeStamp));
                    selectedSong = song;
                }
            }else{
                long currentTimeStamp = System.currentTimeMillis();
                Song song = new Song();
                song.setTitle(String.valueOf(currentTimeStamp));
                selectedSong = song;
            }

            String videoId = "";
            if(youtubeURL.contains("watch")){
                videoId = youtubeURL.substring(youtubeURL.lastIndexOf("v=")+1);
            }else{
                videoId = youtubeURL.substring(youtubeURL.lastIndexOf("/")+1);
            }
            mDetailController.getDownloadUrl(videoId);
            downloadSong(youtubeURL);
        }else {
            int colonIndex = title.indexOf(":");
            titletxt.setText(title.substring(colonIndex + 1));
            singertxt.setText(singer);

            String REQUEST_URL = REQUEST_BASE_URL + title + " " + singer + REQUEST_FINAL_PART;
            String finalURL = REQUEST_URL.replaceAll(" ", "%20");

            JsonObjectRequest request = new JsonObjectRequest(finalURL, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray items = response.getJSONArray("items");
                                //String videoid = get video id
                                if (items != null) {
                                    youtubeSongList = new ArrayList<>();
                                    for (int n = 0; n < items.length(); n++) {
                                        Song song = new Song();
                                        JSONObject object = items.getJSONObject(n);
                                        String videoId = object.getJSONObject("id").getString("videoId");
                                        String title = object.getJSONObject("snippet").getString("title");
                                        String albumArtCoverUrl = object.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                                        song.setTitle(title);
                                        song.setVideoId(videoId);
                                        song.setAlbumArtUrl(albumArtCoverUrl);
                                        if(itemPassCheck(song)){
                                            youtubeSongList.add(song);
                                        }
                                        Log.d(TAG, "onResponse videoId " + videoId);
                                    }
                                    Log.d(TAG, "onResponse: " + response.toString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                loadSongList(youtubeSongList);
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
    }

    private boolean itemPassCheck(Song song) {
        boolean pass = true;
        ArrayList<String> filterStringList = new ArrayList<>();
        filterStringList.add("reaction");
        filterStringList.add("things you didn't notice");
        filterStringList.add("react");
        //checks id video contains any reaction videos
        for (int i = 0; i <filterStringList.size() ; i++) {
            String filterString = filterStringList.get(i);
            if(!title.toLowerCase().contains(filterString)&&song.getTitle().toLowerCase().contains(filterString)){
                pass = false;
                break;
            }
        }
        return pass;
    }

    private void loadSongList(ArrayList<Song> youtubeSongList) {
        Glide.with(getBaseContext()).load(imageUrl)
                .listener(GlidePalette.with(imageUrl)
                                .use(GlidePalette.Profile.VIBRANT)
                                .intoBackground(topPortion, GlidePalette.Swatch.RGB)
                                .crossfade(true)
                )
                .into((mainImageView));

    if(youtubeList!=null){
        YoutubeListAdapter ytAdapter = new YoutubeListAdapter(this,youtubeSongList,getBaseContext());
        youtubeList.setAdapter(ytAdapter);
    }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select an action");
        menu.add(0,v.getId(),0,"View in Youtube");
        menu.add(1,v.getId(),0,"Download");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="View in Youtube"){
            openYoutube();
        }else{
           mDetailController.getDownloadUrl(selectedSong.getVideoId());

        }
        return true;

    }

    private void downloadSong(String url) {
        //To download via async task
        DownloadFileAsync downloader = new DownloadFileAsync(this,this);
        downloader.setSongName(selectedSong.getTitle());
        downloader.execute(url);

        //To download via download manager
      //  Uri downloadURI = Uri.parse(url);

    }

    private void openYoutube() {
        String videoId = selectedSong.getVideoId();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+videoId)));
    }

    @Override
    public void onListItemClicked(Song song) {
        selectedSong = song;
        this.openContextMenu(youtubeList);
    }

    @Override
    public void onDownloadURLObtain(String URL) {
        if(isStoragePermissionGranted()) {
           // downloadSong(URL);
            mDetailController.DownloadData(Uri.parse(URL),selectedSong.getTitle());
        }else{
            Toast.makeText(DetailPage.this, "No storage permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDownloadComplete(String filename) {
        Toast.makeText(DetailPage.this, "Download Complete", Toast.LENGTH_SHORT).show();
        vibrate();
        downloadProgress.setVisibility(View.INVISIBLE);


    }

    private void vibrate() {
        Vibrator v = (Vibrator)getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200);
    }

    @Override
    public void progressUpdate(String... progress) {
        downloadProgress.setVisibility(View.VISIBLE);
        downloadProgress.setText(progress[0]+" %");
    }

    @Override
    public void onSongsFetched(ArrayList<Song> songsList) {

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}

package fetcher.musicman.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

import fetcher.musicman.Adapter.ListAdapter;
import fetcher.musicman.Adapter.YoutubeListAdapter;
import fetcher.musicman.Models.Song;
import fetcher.musicman.R;
import fetcher.musicman.controller.Main.IMainController;
import fetcher.musicman.controller.Main.IMainListener;
import fetcher.musicman.controller.Main.MainController;
import fetcher.musicman.helper.Utility;

public class MainActivity extends AppCompatActivity implements IMainListener {
    private static final String TAG = "MainActivity";
    ArrayList<Song> mainSongList;
    ListView topList;
    String MAX_RESULT = "20";
    String urlString = "https://rss.itunes.apple.com/api/v1/in/apple-music/top-songs/"+MAX_RESULT+"/explicit/json";
    IMainController mainController;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topList = (ListView) findViewById(R.id.listview_main);
        mainController = new MainController(this, this);
        Utility util = new Utility();
        if(util.isNetworkAvailable(this)) {
            showLoader();
            mainController.getSongs(urlString);
        }else{
            //Show no network message
            Toast.makeText(MainActivity.this, "No Network", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDownloadComplete(String filename) {
        Toast.makeText(MainActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void progressUpdate(String... progress) {
        //progress of download
    }

    @Override
    public void onSongsFetched(ArrayList<Song> songsList) {
        hideLoader();
        //Called when rss parsing is complete
        if(!songsList.isEmpty()){
            ListAdapter adapter = new ListAdapter(MainActivity.this,songsList);
            topList.setAdapter(adapter);
        }
    }

    private void showLoader(){
        if(progressDialog==null) {
            progressDialog = new ProgressDialog(MainActivity.this);
        }
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    private void hideLoader(){
        if(progressDialog!=null) {
           progressDialog.dismiss();
        }

    }
}

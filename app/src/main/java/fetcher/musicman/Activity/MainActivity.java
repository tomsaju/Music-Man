package fetcher.musicman.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

import fetcher.musicman.Adapter.ListAdapter;
import fetcher.musicman.Models.Song;
import fetcher.musicman.R;
import fetcher.musicman.controller.Main.IMainListener;

public class MainActivity extends AppCompatActivity implements IMainListener {
    private static final String TAG = "MainActivity";
    ArrayList<Song> billboardList;
    ListView topList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topList = (ListView) findViewById(R.id.listview_main);

        String urlString = "http://www.billboard.com/rss/charts/hot-100";
        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                //what to do when the parsing is done
                //the Array List contains all article's data. For example you can use it for your adapter.
                Log.d(TAG, "onTaskCompleted: ");
                billboardList = new ArrayList<Song>();

                for(int i=0;i<10;i++){
                    Song song = new Song();
                    song.setTitle(list.get(i).getTitle());
                    int firstindex = list.get(i).getDescription().indexOf("by");
                    int lastindex = list.get(i).getDescription().indexOf("ranks");
                    song.setSinger(list.get(i).getDescription().substring(firstindex+2,lastindex));
                    billboardList.add(song);
                }
                if(!billboardList.isEmpty()){

                    ListAdapter adapter = new ListAdapter(MainActivity.this,billboardList);
                    topList.setAdapter(adapter);
                }
            }

            @Override
            public void onError() {
                //what to do in case of error
            }
        });
    }

    @Override
    public void onDownloadComplete(String filename) {
        Toast.makeText(MainActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void progressUpdate(String... progress) {

    }
}

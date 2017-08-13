package fetcher.musicman.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import fetcher.musicman.Adapter.PagerAdapter;
import fetcher.musicman.Fragment.DownloadFragment;
import fetcher.musicman.Fragment.TopItemFragment;
import fetcher.musicman.R;
import fetcher.musicman.helper.Utility;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,TopItemFragment.TopItemFragmentListener,DownloadFragment.OnFragmentInteractionListener,ViewPager.OnPageChangeListener{
    private static final String TAG = "MainActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ProgressDialog progressDialog;
    public static String MAX_RESULT_TOP_SONG = "50";
    public static String MAX_RESULT_NEW_RELEASE = "50";
    public static String MAX_RESULT_WORLD_HITS = "50";
    public static String urlStringTopSongs = "https://rss.itunes.apple.com/api/v1/in/apple-music/top-songs/"+MAX_RESULT_TOP_SONG+"/explicit.json";
    public static String urlStringNewReleaseSongs = "https://rss.itunes.apple.com/api/v1/in/apple-music/new-music/"+MAX_RESULT_NEW_RELEASE+"/explicit.json";
    public static String urlStringWorldHits = "https://rss.itunes.apple.com/api/v1/us/apple-music/new-music/"+MAX_RESULT_WORLD_HITS+"/explicit.json";
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        viewPager = (ViewPager) findViewById(R.id.pager);
         adapter = new PagerAdapter(getSupportFragmentManager(), 3);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(this);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

        if(Utility.isNetworkAvailable(this)) {

        }else{
            //Show no network message
            Toast.makeText(MainActivity.this, "No Network", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        Fragment fragment = adapter.getFragment(position);
        if (fragment != null) {
            fragment.onResume();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

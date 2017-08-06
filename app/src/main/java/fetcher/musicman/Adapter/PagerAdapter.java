package fetcher.musicman.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fetcher.musicman.Activity.MainActivity;
import fetcher.musicman.Fragment.DownloadFragment;
import fetcher.musicman.Fragment.TopItemFragment;

/**
 * Created by Dreams on 05-Aug-17.
 */
public class PagerAdapter extends FragmentStatePagerAdapter  {

    int tabCount;
    // tab titles
    private String[] tabTitles = new String[]{"Top 100", "New Release", "Downloads"};
    TopItemFragment topItemFragment;
    public PagerAdapter(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount = tabCount;
       topItemFragment = new TopItemFragment();
    }

    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               topItemFragment = new TopItemFragment();
               topItemFragment.setUrlString(MainActivity.urlStringTopSongs);
               return topItemFragment;

           case 1:
               topItemFragment = new TopItemFragment();
               topItemFragment.setUrlString(MainActivity.urlStringNewReleaseSongs);
               return topItemFragment;
              // TopItemFragment topItemFragment = new TopItemFragment();
              // topItemFragment = new TopItemFragment();
              // return topItemFragment;

           case 2:
               DownloadFragment df = new DownloadFragment();
               return df;
              // TopItemFragment topItemFragment = new TopItemFragment();
              // topItemFragment = new TopItemFragment();
              // return topItemFragment;

           default:

               return null;
       }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

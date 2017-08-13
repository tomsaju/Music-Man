package fetcher.musicman.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import fetcher.musicman.Activity.MainActivity;
import fetcher.musicman.Fragment.DownloadFragment;
import fetcher.musicman.Fragment.TopItemFragment;

/**
 * Created by Dreams on 05-Aug-17.
 */
public class PagerAdapter extends FragmentStatePagerAdapter  {

    int tabCount;
    // tab titles
    private String[] tabTitles = new String[]{"Top 100", "New Release", "Global Hits"};
    TopItemFragment topItemFragment;
    FragmentManager fragmentManager;
    private Map<Integer, String> mFragmentTags;
    private DownloadFragment df;

    public PagerAdapter(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        this.fragmentManager = fm;
       topItemFragment = new TopItemFragment();
        mFragmentTags = new HashMap<Integer, String>();
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
               topItemFragment = new TopItemFragment();
               topItemFragment.setUrlString(MainActivity.urlStringWorldHits);
               return topItemFragment;
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
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            String tag = fragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        String tag = mFragmentTags.get(position);
        if (tag != null) {
            fragment = fragmentManager.findFragmentByTag(tag);
        }
        if(position==2){
            if(df!=null){
                df.onResume();
            }
        }
        return fragment;
    }
}

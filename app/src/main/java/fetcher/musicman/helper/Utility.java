package fetcher.musicman.helper;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Dreams on 23-Jul-17.
 */
public  class Utility {

    public boolean isNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}

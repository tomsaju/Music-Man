package fetcher.musicman.controller.Detail;

import android.net.Uri;

/**
 * Created by Dreams on 15-Jul-17.
 */
public interface IDetailController {
     String getDownloadUrl(String videoId);
      long DownloadData (Uri uri,String name);
}

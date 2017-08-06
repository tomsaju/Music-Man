package fetcher.musicman.controller.Detail;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fetcher.musicman.Application.VolleyApplication;
import fetcher.musicman.Models.Song;

/**
 * Created by Dreams on 15-Jul-17.
 */
public class DetailController implements IDetailController {
    private static final String TAG = "DetailController";
    Context context;
    IDetailView iDetailView;
    String youtubeinmp3BaseUrl="http://www.youtubeinmp3.com/fetch/?format=JSON&video=https://www.youtube.com/watch?v=";
    private DownloadManager downloadManager;
    private long downloadReference;


    public DetailController(Context context, IDetailView iDetailView) {
        this.context = context;
        this.iDetailView = iDetailView;
    }

    @Override
    public String getDownloadUrl(String videoId) {
        String finalURL = youtubeinmp3BaseUrl+videoId;
        JsonObjectRequest request = new JsonObjectRequest(finalURL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String downloadUrl = response.getString("link");
                            iDetailView.onDownloadURLObtain(downloadUrl);
                        }catch(Exception e){
                            e.printStackTrace();
                        }finally{

                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+error.toString());
                    }
                }
        );

        VolleyApplication.getInstance().getRequestQueue().add(request);
    return finalURL;
    }

    public long DownloadData (Uri uri,String name) {

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri Download_Uri = uri;
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Music Man");
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Android Data download using DownloadManager.");
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, name+".mp3");

        //Enqueue a new download and same the referenceId
        downloadReference = downloadManager.enqueue(request);
        return downloadReference;
    }
}

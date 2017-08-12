package fetcher.musicman.controller.Detail;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fetcher.musicman.Application.VolleyApplication;
import fetcher.musicman.Models.Song;
import fetcher.musicman.helper.DBHelper;

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
    private static int STATUS_COMPLETE = 1;
    private static int STATUS_FAILED = 0;
    private static int STATUS_DOWNLOADING = 2;


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
        request.setDescription("Downloading "+name);
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, name+".mp3");

        //Enqueue a new download and same the referenceId
        downloadReference = downloadManager.enqueue(request);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver,filter);
        Song song = new Song();
        song.setId(String.valueOf(downloadReference));
        song.setTitle(name);
        song.setDownloadDate(getDate());
        song.setDownloadLocation(Environment.DIRECTORY_DOWNLOADS+name+".mp3");
        song.setStatus(String.valueOf(STATUS_DOWNLOADING));
        DBHelper dbHelper = new DBHelper(context,DBHelper.DB_NAME,null,DBHelper.DB_VERSION);
        dbHelper.insertIntoDownloadTable(song);
        return downloadReference;
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }


    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,0);
            DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
            //set the query filter to our previously Enqueued download
            myDownloadQuery.setFilterById(referenceId);
            Cursor cursor = downloadManager.query(myDownloadQuery);
            if(cursor.moveToFirst()){
                checkStatus(cursor,referenceId);
            }



        }
    };


    private void checkStatus(Cursor cursor,long referenceId) {

        //column for status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
        String filename = cursor.getString(filenameIndex);

        String statusText = "";
        String reasonText = "";
        DBHelper dbHelper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                dbHelper.updateDownloadStatus(String.valueOf(referenceId), STATUS_FAILED);
                switch (reason) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch (reason) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                reasonText = "Filename:\n" + filename;
                dbHelper.updateDownloadStatus(String.valueOf(referenceId), STATUS_COMPLETE);
                break;
        }
    }

}

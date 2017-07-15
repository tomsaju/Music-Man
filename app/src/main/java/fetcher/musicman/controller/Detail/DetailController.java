package fetcher.musicman.controller.Detail;

import android.content.Context;
import android.util.Log;

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
}

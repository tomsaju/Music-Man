package fetcher.musicman.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import fetcher.musicman.controller.Main.IMainListener;

/**
 * Created by tom.saju on 7/12/2017.
 */

public class DownloadFileAsync extends AsyncTask<String, String, String> {
    IMainListener iMainListener;
    Context context;
    public  String songName;
    public DownloadFileAsync(Context context, IMainListener iMainListener) {
        this.context = context;
        this.iMainListener = iMainListener;



    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;
        try {
            URL url = new URL(aurl[0]);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
            File outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            outputPath.mkdirs();
            File outputFile = new File(outputPath, songName+".mp3");
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(outputFile);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress(""+(int)((total*100)/lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC",progress[0]);
        iMainListener.progressUpdate(progress[0]);

    }

    @Override
    protected void onPostExecute(String unused) {
        iMainListener.onDownloadComplete("filename");
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}

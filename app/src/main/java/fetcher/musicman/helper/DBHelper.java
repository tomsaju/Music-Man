package fetcher.musicman.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import fetcher.musicman.Models.Song;

/**
 * Created by Dreams on 12-Aug-17.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "MusicManDB";
    public static int DB_VERSION = 1;

    //Table names
    public String TABLE_DOWNLOADS = "downloads_table";

    //field names
    private static String SONG_ID = "id";
    private static String SONG_NAME = "title";
    private static String ARTIST_NAME = "artist";
    private static String DOWNLOAD_DATE = "download_date";
    private static String DOWNLOAD_LOCATION = "location";
    private static String DOWNLOAD_STATUS = "status";


    //field values
    private static int STATUS_COMPLETE = 1;
    private static int STATUS_FAILED = 0;
    private static int STATUS_DOWNLOADING = 2;

    //Table create statements
    public String TABLE_DOWNLOADS_CREATE = "CREATE TABLE "+TABLE_DOWNLOADS+" ( "
            + SONG_ID+" TEXT, "+SONG_NAME+" TEXT, "+ARTIST_NAME+" TEXT, "+DOWNLOAD_DATE+" TEXT, "+DOWNLOAD_LOCATION+" TEXT, "+DOWNLOAD_STATUS+" TEXT )";



    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_DOWNLOADS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Song> getAllDownloads(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        ArrayList<Song> downloadList = new ArrayList<>();
        try {
            cursor = db.query(TABLE_DOWNLOADS, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(SONG_NAME)));
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_NAME)));
                song.setId(cursor.getString(cursor.getColumnIndexOrThrow(SONG_ID)));
                song.setDownloadDate(cursor.getString(cursor.getColumnIndexOrThrow(DOWNLOAD_DATE)));
                song.setDownloadLocation(cursor.getString(cursor.getColumnIndexOrThrow(DOWNLOAD_LOCATION)));
                song.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DOWNLOAD_STATUS)));
                downloadList.add(song);
            }
        }finally {
                db.close();
                cursor.close();
        }
        return downloadList;
    }

    public void insertIntoDownloadTable(Song song){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SONG_ID,song.getId());
        cv.put(SONG_NAME,song.getTitle());
        cv.put(ARTIST_NAME,song.getSinger());
        cv.put(DOWNLOAD_DATE,song.getDownloadDate());
        cv.put(DOWNLOAD_LOCATION,song.getDownloadLocation());
        cv.put(DOWNLOAD_STATUS,song.getStatus());

        try {
            db.insert(TABLE_DOWNLOADS, null, cv);
        }finally {
            db.close();
        }

    }

    public void updateDownloadStatus(String ID,int status){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DOWNLOAD_STATUS,status);

        try{
            db.update(TABLE_DOWNLOADS,cv,ID+" = ID",null);
        }finally {
            db.close();
        }

    }
}

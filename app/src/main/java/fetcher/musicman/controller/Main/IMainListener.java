package fetcher.musicman.controller.Main;

/**
 * Created by tom.saju on 7/12/2017.
 */

public interface IMainListener {
    void onDownloadComplete(String filename);
    void progressUpdate(String... progress);
}

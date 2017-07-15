package fetcher.musicman.controller.Detail;

import fetcher.musicman.Models.Song;

/**
 * Created by Dreams on 15-Jul-17.
 */
public interface IDetailView {
    void onListItemClicked(Song song);
    void onDownloadURLObtain(String URL);
}

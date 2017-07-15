package fetcher.musicman.controller.Main;

import android.content.Context;

/**
 * Created by tom.saju on 7/12/2017.
 */

public class MainController implements IMainController {
    Context context;
    IMainListener iMainListener;

    public MainController(Context context,IMainListener iMainListener) {
        this.context = context;
        this.iMainListener = iMainListener;
    }

    @Override
    public void saveSong(String url) {

    }
}

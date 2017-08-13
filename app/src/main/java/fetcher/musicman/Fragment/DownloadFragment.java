package fetcher.musicman.Fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import fetcher.musicman.Adapter.DownloadAdapter;
import fetcher.musicman.Adapter.YoutubeListAdapter;
import fetcher.musicman.Models.Song;
import fetcher.musicman.R;
import fetcher.musicman.controller.Main.IMainController;
import fetcher.musicman.controller.Main.IMainListener;
import fetcher.musicman.controller.Main.MainController;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DownloadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DownloadFragment extends Fragment implements IMainListener {

    private OnFragmentInteractionListener mListener;
    ListView downloadsList;
    ArrayList<Song> songArrayList;
    IMainController mainController;
    public DownloadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainController = new MainController(getContext(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_download, container, false);
        downloadsList = (ListView) view.findViewById(R.id.downloads_list);
        songArrayList =mainController.getAlldownloads();
        if(songArrayList!=null&&!songArrayList.isEmpty()){
            DownloadAdapter adapter = new DownloadAdapter(songArrayList,getContext());
            downloadsList.setAdapter(adapter);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDownloadComplete(String filename) {

    }

    @Override
    public void progressUpdate(String... progress) {

    }

    @Override
    public void onSongsFetched(ArrayList<Song> songsList) {

    }

    @Override
    public void onDownloadStatusReceive() {
        songArrayList =mainController.getAlldownloads();
        ((DownloadAdapter)downloadsList.getAdapter()).notifyDataSetChanged();
        /*if(songArrayList!=null&&!songArrayList.isEmpty()){
            DownloadAdapter adapter = new DownloadAdapter(songArrayList,getContext());
            downloadsList.setAdapter(adapter);
        }*/
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        songArrayList =mainController.getAlldownloads();
        if(songArrayList!=null&&!songArrayList.isEmpty()){
            DownloadAdapter adapter = new DownloadAdapter(songArrayList,getContext());
            downloadsList.setAdapter(adapter);
        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null ) {
                String str= intent.getStringExtra("key");
                //Get all your data from intent and do what you want
                onResume();
            }
        }
    };

}

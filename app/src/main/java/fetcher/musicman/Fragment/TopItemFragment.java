package fetcher.musicman.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fetcher.musicman.Activity.DetailPage;
import fetcher.musicman.Adapter.ListAdapter;
import fetcher.musicman.Adapter.RecyclerAdapter;
import fetcher.musicman.Models.Song;
import fetcher.musicman.R;
import fetcher.musicman.controller.Main.IMainController;
import fetcher.musicman.controller.Main.IMainListener;
import fetcher.musicman.controller.Main.MainController;
import fetcher.musicman.helper.ItemOffsetDecoration;
import fetcher.musicman.helper.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fetcher.musicman.Fragment.TopItemFragment.TopItemFragmentListener} interface
 * to handle interaction events.
 */
public class TopItemFragment extends Fragment implements IMainListener,RecyclerListener {

    //added new fragment
    private static final String TAG = "MainActivity";
    ArrayList<Song> mainSongList;
    String MAX_RESULT = "50";
    String urlString = "https://rss.itunes.apple.com/api/v1/in/apple-music/top-songs/"+MAX_RESULT+"/explicit/json";
    IMainController mainController;
    ProgressDialog progressDialog;
    private TopItemFragmentListener mListener;
    RecyclerView songRecyclerView;
    RecyclerAdapter mAdapter;
    public TopItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_top_item, container, false);
        songRecyclerView =(RecyclerView) mainView.findViewById(R.id.recyclerview_top_items);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        songRecyclerView.addItemDecoration(itemDecoration);
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TopItemFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        mainController = new MainController(getContext(), this);
        if(Utility.isNetworkAvailable(getContext())) {
            showLoader();
            mainController.getSongs(urlString);
        }else{
            //Show no network message
            Toast.makeText(getContext(), "No Network", Toast.LENGTH_SHORT).show();
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
        hideLoader();
        //Called when rss parsing is complete
        if(!songsList.isEmpty()){
            mainSongList = songsList;
            mAdapter = new RecyclerAdapter(songsList,this);
            RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
            songRecyclerView.setLayoutManager(gridLayoutManager);
            songRecyclerView.setItemAnimator(new DefaultItemAnimator());
            songRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        int itemPosition = songRecyclerView.getChildLayoutPosition(v);
        Song item = mainSongList.get(itemPosition);
        Intent i = new Intent(getContext(), DetailPage.class);
        i.putExtra("title",item.getTitle());
        i.putExtra("singer",item.getSinger());
        i.putExtra("imageurl",item.getAlbumArtUrl());
        getContext().startActivity(i);

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
    public interface TopItemFragmentListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void showLoader(){
        if(progressDialog==null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    private void hideLoader(){
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }

    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }
}

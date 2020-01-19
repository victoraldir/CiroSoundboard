package com.quartzodev.cirosoundboard.sound;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quartzodev.cirosoundboard.R;
import com.quartzodev.cirosoundboard.data.Audio;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by victoraldir on 17/12/2017.
 */

public class SoundFragment extends Fragment implements Observer<List<Audio>> {

    private SoundViewModel mSoundViewModel;
    private SoundFragmentListener mSoundFragmentListener;
    private RecyclerView mGridRecyclerView;
    private SoundAdapter mAdapter;
    private Long mSectionId;
    private ProgressBar mProgressBar;
    private TextView mMessage;
    private TextView mSubMessage;

    private static final String ARG_SECTION_ID = "section_id";

    public SoundFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SoundFragment newInstance(long sectionNumberId) {
        SoundFragment fragment = new SoundFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SECTION_ID, sectionNumberId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSoundViewModel = SoundActivity.obtainViewModel(getActivity());

        if(getArguments().containsKey(ARG_SECTION_ID)){
            mSectionId = getArguments().getLong(ARG_SECTION_ID);
        }

        if(mSectionId == 0){
            mSoundViewModel.getFavoriteList().observe(this, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;

        if(mSectionId == 0){
            rootView = inflater.inflate(R.layout.sound_fav_frag, container, false);
        }else{
            rootView = inflater.inflate(R.layout.sound_frag, container, false);
        }

        mProgressBar = rootView.findViewById(R.id.load_progress);

        mMessage = rootView.findViewById(R.id.message);

        mSubMessage = rootView.findViewById(R.id.sub_message);

        mGridRecyclerView = rootView.findViewById(R.id.grid_audio);

        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.list_column_count));
        mGridRecyclerView.setLayoutManager(lm);
        mAdapter = new SoundAdapter(new ArrayList<Audio>(), mSoundFragmentListener);

        mGridRecyclerView.setAdapter(mAdapter);

        loading();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mSectionId != 0)
            listAudios();
    }

    public void listAudios() {
        if(mSectionId != 0){
            mSoundViewModel.loadAudios(mSectionId).observe(this, this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SoundFragmentListener) {
            mSoundFragmentListener = (SoundFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SoundFragmentListener");
        }
    }

    @Override
    public void onChanged(@Nullable List<Audio> audioList) {

        if(audioList.isEmpty()){

            if(mSectionId == 0){
                loaded(getString(R.string.favorite_empty_message), getString(R.string.favorite_empty_sub_message));
            }else{
                loaded(getString(R.string.list_empty),"");
            }
            return;
        }else{
            mAdapter.swap(audioList);
        }

        loaded(null,null);
    }

    private void loaded(String message, String subMessage){

        mProgressBar.setVisibility(View.GONE);

        if(message != null){
            mGridRecyclerView.setVisibility(View.GONE);
            mMessage.setVisibility(View.VISIBLE);
            mMessage.setText(message);
        }else{
            mGridRecyclerView.setVisibility(View.VISIBLE);
            mMessage.setVisibility(View.GONE);
        }

        if(subMessage != null && mSubMessage != null){
            mSubMessage.setVisibility(View.VISIBLE);
            mSubMessage.setText(subMessage);
        }
    }

    private void loading(){
        mProgressBar.setVisibility(View.VISIBLE);
        mGridRecyclerView.setVisibility(View.GONE);
    }

    public interface SoundFragmentListener{
        void onClick(Audio audio, FancyButton button, View container);
        void onLongClick(Audio audio, FancyButton button, View container);
        void onFavoriteClick(Audio audio, boolean flag, View container);
        void onShareClick(Audio audio, View container);
    }
}

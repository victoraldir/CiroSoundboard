package com.quartzodev.cirosoundboard.sound;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quartzodev.cirosoundboard.R;
import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.source.GenericDataSource;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by victoraldir on 17/12/2017.
 */

public class SoundFragment extends Fragment implements GenericDataSource.LoadListCallback {

    private SoundViewModel mSoundViewModel;
    private SoundFragmentListener mSoundFragmentListener;
    private RecyclerView mGridRecyclerView;
    private SoundAdapter mAdapter;
    private Long mSectionId;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sound_frag, container, false);

        mGridRecyclerView = rootView.findViewById(R.id.grid_audio);

        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 3);
        mGridRecyclerView.setLayoutManager(lm);
        mAdapter = new SoundAdapter(new ArrayList<Audio>(),(SoundActivity) getActivity());

        mGridRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listAudios();
    }

    public void listAudios() {
        mSoundViewModel.loadAudios(mSectionId,this);
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
    public void onListLoaded(List list) {
        mAdapter.swap(list);
    }

    @Override
    public void onDataNotAvailable() {

    }

    public interface SoundFragmentListener{
        void onClick(Audio audio, FancyButton button, View container);
        void onLongClick(Audio audio, FancyButton button, View container);
    }
}

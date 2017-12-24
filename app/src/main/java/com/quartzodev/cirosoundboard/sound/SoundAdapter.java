package com.quartzodev.cirosoundboard.sound;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quartzodev.cirosoundboard.R;
import com.quartzodev.cirosoundboard.data.Audio;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by victoraldir on 22/12/2017.
 */

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.ViewHolder> {

    private List<Audio> mAudioList;
    private SoundFragment.SoundFragmentListener mListener;


    public SoundAdapter(List<Audio> audioList, SoundFragment.SoundFragmentListener listener){
        mAudioList = audioList;
        mListener = listener;
    }

    public void swap(List<Audio> audios){
        setList(audios);
    }

    private void setList(List<Audio> audios) {
        mAudioList = audios;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Audio audio = mAudioList.get(position);

        holder.labelAudio.setText(audio.getLabel());

        final FrameLayout container = holder.container;
        final FancyButton button = holder.btnAudio;

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(audio, button, container);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(audio, button, container);
            }
        });

        container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onLongClick(audio, button, container);
                return true;
            }
        });

        holder.btnAudio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onLongClick(audio, button, container);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAudioList != null ? mAudioList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView labelAudio;
        FancyButton btnAudio;
        FrameLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_btn);
            btnAudio = itemView.findViewById(R.id.btn_audio);
            labelAudio = itemView.findViewById(R.id.label_audio);
        }
    }
}

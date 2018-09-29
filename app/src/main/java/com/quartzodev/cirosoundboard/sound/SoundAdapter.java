package com.quartzodev.cirosoundboard.sound;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.quartzodev.cirosoundboard.R;
import com.quartzodev.cirosoundboard.data.Audio;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by victoraldir on 22/12/2017.
 */

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.ViewHolder> {

    private List<Audio> mAudioList;
    private SoundFragment.SoundFragmentListener mListener;

    public SoundAdapter(List<Audio> audioList, SoundFragment.SoundFragmentListener listener) {
        mAudioList = audioList;
        mListener = listener;
    }

    public void swap(List<Audio> audios) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Audio audio = mAudioList.get(position);

        holder.mLabelAudio.setText(audio.getLabel());

        final LinearLayout container = holder.mContainer;
        final FancyButton button = holder.mBtnAudio;

        if (audio.isFavorite()) {
            holder.mImageView.setLiked(true);
            holder.mImageView.setTag(true);
        } else {
            holder.mImageView.setLiked(false);
            holder.mImageView.setTag(false);
        }

        holder.mImageView.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                mListener.onFavoriteClick(audio, true, holder.mContainer);
            }
        });

        holder.mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onShareClick(audio,holder.mContainer);
            }
        });

        holder.mImageView.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                mListener.onFavoriteClick(audio, false, holder.mContainer);
            }
        });

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

        holder.mBtnAudio.setOnLongClickListener(new View.OnLongClickListener() {
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mLabelAudio;
        FancyButton mBtnAudio;
        LinearLayout mContainer;
        LikeButton mImageView;
        ImageView mImageNew;
        ImageView mShareBtn;

        public ViewHolder(View itemView) {
            super(itemView);
//            mImageNew = itemView.findViewById(R.id.icon_new);
            mContainer = itemView.findViewById(R.id.container_btn);
            mBtnAudio = itemView.findViewById(R.id.btn_audio);
            mLabelAudio = itemView.findViewById(R.id.label_audio);
            mImageView = itemView.findViewById(R.id.fav_icon);
            mShareBtn = itemView.findViewById(R.id.share_btn);
        }
    }
}

package com.quartzodev.cirosoundboard.utils;

import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.quartzodev.cirosoundboard.data.Audio;

/**
 * Created by victoraldir on 04/01/2018.
 */

public class AnswersUtil {

    private static final String METRIC_PLAY_AUDIO = "Play Audio";
    private static final String METRIC_SHARE_AUDIO = "Share Audio";
    private static final String METRIC_FAVORITE_AUDIO = "Favorite Audio";

    public static void onPlayAudioMetric(Audio audio) {
        Answers.getInstance().logCustom(new CustomEvent(METRIC_PLAY_AUDIO)
                .putCustomAttribute("Label", audio.getLabel())
                .putCustomAttribute("Audio Path", audio.getAudioPath()));
    }

    public static void onShareAudioMetric(Audio audio) {
        Answers.getInstance().logCustom(new CustomEvent(METRIC_SHARE_AUDIO)
                .putCustomAttribute("Label", audio.getLabel())
                .putCustomAttribute("Audio Path", audio.getAudioPath()));
    }

    public static void onFavoriteAudioMetric(Audio audio) {
        Answers.getInstance().logCustom(new CustomEvent(METRIC_FAVORITE_AUDIO)
                .putCustomAttribute("Label", audio.getLabel())
                .putCustomAttribute("Audio Path", audio.getAudioPath()));
    }

}

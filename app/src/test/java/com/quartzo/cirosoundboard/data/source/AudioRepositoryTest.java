package com.quartzo.cirosoundboard.data.source;

import com.quartzo.cirosoundboard.data.source.local.audio.AudioDataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by victoraldir on 20/12/2017.
 */
@Ignore
public class AudioRepositoryTest {

    private AudioRepository mAudioRepository;

    @Mock
    private AudioDataSource mAudioDataSource;

    @Mock
    private AudioDataSource.LoadListCallback mLoadTasksCallback;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);
        mAudioRepository = AudioRepository.getInstance(mAudioDataSource);
    }

    @Test
    public void getTasks_requestsAllTasksFromLocalDataSource() {
        // When tasks are requested from the tasks repository
        mAudioRepository.getAudios(mLoadTasksCallback);

        // Then tasks are loaded from the local data source
        verify(mAudioDataSource).getAudios(any(AudioDataSource.LoadListCallback.class));
    }

}
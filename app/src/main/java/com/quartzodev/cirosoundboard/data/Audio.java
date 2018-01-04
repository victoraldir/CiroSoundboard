package com.quartzodev.cirosoundboard.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by victoraldir on 16/12/2017.
 */

@Entity(tableName = "audio", foreignKeys = @ForeignKey(entity = Section.class,
        parentColumns = "id",
        childColumns = "section_id",
        onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "section_id"))
public class Audio {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private final Long id;

    @NonNull
    private final String label;

    @NonNull
    @ColumnInfo(name = "audio_path")
    private final String audioPath;

    @NonNull
    @ColumnInfo(name = "section_id")
    private final Integer sectionId;

    @NonNull
    private final Integer order;

    @NonNull()
    @ColumnInfo(name = "flag_new")
    private boolean isNew;

    @NonNull()
    @ColumnInfo(name = "flag_favorite")
    private boolean isFavorite;

    public Audio(@NonNull Long id, @NonNull String label, @NonNull String audioPath,
                 @NonNull Integer sectionId, @NonNull Integer order, @NonNull boolean isNew,
                 @NonNull boolean isFavorite) {
        this.id = id;
        this.label = label;
        this.audioPath = audioPath;
        this.sectionId = sectionId;
        this.order = order;
        this.isNew = isNew;
        this.isFavorite = isFavorite;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public Integer getOrder() {
        return order;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    @NonNull
    public boolean isNew() {
        return isNew;
    }

    @NonNull
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @NonNull
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
    }
}

package com.quartzodev.cirosoundboard.data.source.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.local.audio.AudioDao;
import com.quartzodev.cirosoundboard.data.source.local.section.SectionDao;

/**
 * Created by victoraldir on 17/12/2017.
 */

@Database(entities = {Audio.class, Section.class}, version = 1)
public abstract class CiroSoundBoardDatabase extends RoomDatabase {

    private static CiroSoundBoardDatabase INSTANCE;

    public abstract AudioDao audioDao();

    public abstract SectionDao sectionDao();

    private static final Object sLock = new Object();

    public static CiroSoundBoardDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        CiroSoundBoardDatabase.class, "CiroSoundBoard.db")
                        .addCallback(callback)
                        .build();
            }
            return INSTANCE;
        }
    }

    /**
     * Migrations
     *
     */

    private static final RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (1,'Section 1',0)");

            database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (2,'Section 2',1)");

            database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (3,'Section 3',2)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('Bilhao',0, 'bilhao',1, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('campeao_demissao',0, 'campeao_demissao',2, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('Conspirador Filho da Puta',0, 'conspirador_filho_puta',1, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('More presidential',0, 'more_presidential',1, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('onde_achar_bilhao',0, 'onde_achar_bilhao',3, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('Presidente parecido povo',0, 'presidente_povo',1, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('Retirar filho da puta',0, 'retirar_filho_da_puta',1, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('Temer Safado',0, 'temer_safado',1, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('Conta Bolsonaro',0, 'conta_bolsonaro',1, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('JBS Conta Bolsonaro',0, 'jbs_conta_bolsonaro',1, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('Lavagem dinheiro',0, 'lavagem_dinheiro',1, 0, 0)");

            database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                    "VALUES ('Madar pastar',0, 'mando_pastar',1, 0, 0)");

            super.onCreate(database);
        }
    };
}

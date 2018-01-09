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

@Database(entities = {Audio.class, Section.class}, version = 3)
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
                        .addMigrations(MIGRATION_1_2)
                        .addMigrations(MIGRATION_2_3)
                        .build();
            }
            return INSTANCE;
        }
    }

    /**
     * Migrations
     */

    private static final RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase database) {
            super.onCreate(database);
        }
    };


    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            cleanUpDatabase(database);
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            cleanUpDatabase(database);
            initialScript(database);
        }
    };

    private static void cleanUpDatabase(SupportSQLiteDatabase database){
        database.execSQL("DELETE FROM section;");
        database.execSQL("delete from sqlite_sequence where name='section';");

        database.execSQL("DELETE FROM audio;");
        database.execSQL("delete from sqlite_sequence where name='audio';");
    }

    private static void initialScript(SupportSQLiteDatabase database){

        //Sections
        database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (1,'Famosas',0)");
        database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (2,'Gestão',1)");
        database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (7,'Lula',2)");
        database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (4,'João Doria',3)");
        database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (6,'Temer',5)");
        database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (3,'Bolsonaro',6)");
        database.execSQL("INSERT INTO section ('id', 'label', 'order') VALUES (9,'Saúde',8)");

        /**
         * Audios
         */
        //Famosos
        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Saber onde achar 1 bilhäo',1, 'famosas_onde_achar_bilhao',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Bilhao',2, 'famosas_bilhao',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Temer Safado',3, 'famosas_temer_safado',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Conspirador filho da puta',4, 'famosas_conspirador_filho_puta',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Presidente não fala filho da puta',5, 'famosas_retirar_filho_da_puta',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('More presidential',6, 'famosas_more_presidential',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Presidente parecido povo',7, 'famosas_presidente_povo',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Vai frescar?',8, 'famosas_vai_frescar',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Depósito na conta de Bolsonaro',9, 'famosas_conta_bolsonaro',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Depósito legal Bolsonaro',10, 'famosas_jbs_conta_bolsonaro',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Mandar pastar',11, 'famosas_mando_pastar',1, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Tudo canalha!',12, 'famosas_tudo_canalha',1, 0, 0)");


        //Gestão
        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Demissão de servidor ocioso',1, 'gestao_campeao_demissao',2, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Meus filhos estão proibidos',2, 'gestao_meus_filhos_proibidos',2, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Nunca aceitei aposentadoria de governo',3, 'gestao_aposentadoria_governo',2, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Mais barato que lata de coca-cola',4, 'gestao_barril_mais_barato_coca_cola',2, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Não há razão para internacionalizar petróleo',5, 'gestao_nao_ha_razao_internacionalizar',2, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Isso é perverso',6, 'gestao_perverso',2, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Vou sofrer o pão que o diabo amassou',7, 'gestao_sofrer',2, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Prometo tomar petróleo de volta',8, 'gestao_tomo_de_volta_petroleo',2, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Reforma profunda',9, 'gestao_reforma',2, 0, 0)");

        //Bolsonaro
        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Lavagem dinheiro',1, 'bolsonaro_lavagem_dinheiro',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Não sirvo alguém que não respeite',2, 'bolsonaro_alguem_que_eu_nao_respeite',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Nunca administrou uma budega',3, 'bolsonaro_budega',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Como é que resolve défict primário?',4, 'bolsonaro_como_resolve_deficit_primario',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Avisa ao Bolsonaro que ele tem que saber!',5, 'bolsonaro_deficit_nao_some',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Eu sou honesto - diz ele - Eu também sou - digo eu',6, 'bolsonaro_eu_sou_honesto',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Me botar para ser ministro de uma tragédia?',7, 'bolsonaro_ministro_tragedia',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Quer ser presidente sem nenhuma experiência',8, 'bolsonaro_presidente_sem_experiencia',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Onde é que ele tava quando o Rio estava apodrecendo?',9, 'bolsonaro_rio_corrupcao',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Qual é a sua opinião sobre o tripé macroeconomico',10, 'bolsonaro_tripe_macroeconomico',3, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Valentão que quer resolver problema do Brasil',11, 'bolsonaro_valentao_golpe_frase_feita',3, 0, 0)");

        //Doria
        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Ele é um não político',1, 'doria_anti_pt',4, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Foi corrido de lá por roubalheira',2, 'doria_corrido_roubalheira',4, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Picareta completo!',3, 'doria_farsante_picareta',4, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Não é normal internar violentamente',4, 'doria_internar_dependente',4, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('O cara anda num Jato pra cima e pra baixo',5, 'doria_jato',4, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('O cara é podre de rico',6, 'doria_lobby',4, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Político que não tem nada na cabeça',7, 'doria_nada_na_cabeca',4, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Político não político',8, 'doria_politico_nao_gestor',4, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('E rico pra cacete!',9, 'doria_rico_p_cacete',4, 0, 0)");

        //Lula
        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Candidatura ruim para ele',1, 'lula_candidatura_ruim_para_ele',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Polarizará o eleitorado',2, 'lula_polariza_elitorado',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Pôs temer na linha de sucessão',3, 'lula_temer_linha_sucessao',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('O que resta da autoridade de Dilma?',4, 'lula_autoridade_dilma',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Fortuna na conta de Lula',5, 'lula_fortuna_conta',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Misturar política com dinheiro',6, 'lula_misturar_politica_dinheiro',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Lula não gosta do Mercadante',7, 'lula_nao_gosta_mercadante',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Lula não gosta do Zé Eduardo Cardoso',8, 'lula_nao_gosta_ze_eduardo',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Ninguém é idiota',9, 'lula_ninguem_e_idiota',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Tutela pública do Lula',10, 'lula_tutela_publica',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Quer ser político? Não da para ganhar dinheiro',11, 'lula_quer_ser_politico',7, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Pacto com o Cunha',12, 'lula_pactos_eduardo_cunha',7, 0, 0)");

        //Temer
        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Sem voto corrupto!',1, 'temer_corrupto',6, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Elite brasileira sabia que ele era um marginal',2, 'temer_marginal',6, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Ele me processou',3, 'temer_processou',6, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Lula e eu já sabíamos',4, 'temer_sem_voto',6, 0, 0)");


        //Saúde
        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Consórcio saúde',1, 'saude_consorcio_saude',9, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Ninguém vai pra fila',2, 'saude_ninguem_vai_fila',9, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Não sai verba para município',3, 'saude_estudar_verba_municipio',9, 0, 0)");

        database.execSQL("INSERT INTO audio ('label', 'order', 'audio_path', 'section_id', 'flag_new', 'flag_favorite') " +
                "VALUES ('Pessoas referidas pelos postos',4, 'saude_pessoas_referidas_posto',9, 0, 0)");
    }
}

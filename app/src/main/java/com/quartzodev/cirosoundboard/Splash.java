package com.quartzodev.cirosoundboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.quartzodev.cirosoundboard.sound.SoundActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, SoundActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.quartzodev.cirosoundboard;

import android.content.Intent;
import android.os.Bundle;
import com.quartzodev.cirosoundboard.sound.SoundActivity;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, SoundActivity.class);
        startActivity(intent);
        finish();
    }
}

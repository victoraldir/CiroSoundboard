package com.quartzodev.cirosoundboard.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.quartzodev.cirosoundboard.R;
import com.quartzodev.cirosoundboard.ViewModelFactory;
import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.GenericDataSource;
import com.quartzodev.cirosoundboard.utils.AppExecutors;
import com.quartzodev.cirosoundboard.utils.AppMediaPlayer;
import com.quartzodev.cirosoundboard.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class SoundActivity extends AppCompatActivity implements SoundFragment.SoundFragmentListener,
        GenericDataSource.LoadListCallback<Section>, GenericDataSource.GetObjectCallback<Audio>,
        View.OnClickListener, android.view.ActionMode.Callback,
        NavigationView.OnNavigationItemSelectedListener {

    private AppMediaPlayer mAppMediaPlayer;
    private SoundSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SoundViewModel mSoundViewModel;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private ActionMode mMode;
    private AppExecutors mAppExecutors;

    private boolean mMultiSelect = false;
    private Map<Audio, View> mSelectedItems = new HashMap<Audio, View>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_act);

        mCoordinatorLayout = findViewById(R.id.main_content);
        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.container);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        mAppMediaPlayer = AppMediaPlayer.newInstance(this, this);
        mSoundViewModel = obtainViewModel(this);
        mSoundViewModel.loadSections(this);

        mAppExecutors = new AppExecutors();

        setupFloatButton();
        setupViewPager();
        setupDrawer();
    }

    public void setupFloatButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    public void setupViewPager() {

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SoundSectionsPagerAdapter(getSupportFragmentManager(), new ArrayList<Section>());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setupDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public static SoundViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SoundViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sound_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Audio audio, FancyButton fancyButton, View container) {

        if (!mMultiSelect) {
            mAppMediaPlayer.setButton(fancyButton);
            mAppMediaPlayer.playAudio(audio.getAudioPath());
        } else {
            selectItem(audio, container);
        }
    }

    @Override
    public void onLongClick(Audio audio, FancyButton fancyButton, View container) {
        mMode = mToolbar.startActionMode(this);
        selectItem(audio, container);
    }

    @Override
    public void onFavoriteClick(Audio audio, boolean flag) {
        mSoundViewModel.updateFavoriteFlag(audio.getId(), flag);

        Snackbar.make(mCoordinatorLayout, "Audio " + audio.getLabel() + " flagged as " + (flag ? "favorite" : "not favorite"), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onListLoaded(List<Section> list) {
        mSectionsPagerAdapter.swap(list);
    }

    @Override
    public void onObjectLoaded(Audio audio) {

        mAppMediaPlayer.playAudio(audio.getAudioPath());
        Snackbar.make(mCoordinatorLayout, "Playing " + audio.getLabel() + " audio", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void onClick(View view) {
        mSoundViewModel.loadRandomAudio(this);
    }

    @Override
    public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
        mMultiSelect = true;
        menu.add("Compartilhar");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {

        if (menuItem.getTitle().equals("Compartilhar")) {

            Runnable taskWriteSong = new Runnable() {
                @Override
                public void run() {
                    shareAudio(new ArrayList<>(mSelectedItems.keySet()));
                }
            };

            mAppExecutors.mainThread().execute(taskWriteSong);
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode actionMode) {
        mMultiSelect = false;

        for (View view : mSelectedItems.values()) {
            view.setBackgroundColor(Color.WHITE);
        }

        mSelectedItems.clear();

    }

    void selectItem(Audio audio, View container) {
        if (mMultiSelect) {
            if (mSelectedItems.containsKey(audio)) {
                mSelectedItems.remove(audio);
                container.setBackgroundColor(Color.WHITE);

                if (mSelectedItems.isEmpty()) {
                    mMode.finish();
                }

            } else {
                mSelectedItems.put(audio, container);
                container.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    private synchronized void shareAudio(List<Audio> audioList) {

        ArrayList<Uri> uriList = new ArrayList<>();

        for (Audio audioToShare : audioList) {

            Uri fileToShareUri = FileUtils.getExistingFile(this, audioToShare.getLabel());

            if (fileToShareUri == null) {
                fileToShareUri = FileUtils.writeStreamToFile(this, audioToShare.getLabel(), audioToShare.getAudioPath());
            }

            uriList.add(fileToShareUri);
        }

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE).setType("audio/x-mpeg3");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        startActivity(Intent.createChooser(intent, "Share to"));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}

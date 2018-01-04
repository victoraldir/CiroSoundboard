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

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
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
        mViewPager = (ViewPager) findViewById(R.id.container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        setupToolbar();
        setupFloatButton();
        setupViewPager();
        setupDrawer();

        mAppMediaPlayer = AppMediaPlayer.newInstance(this, this);
        mSoundViewModel = obtainViewModel(this);
        mSoundViewModel.loadSections(this);

        mAppExecutors = new AppExecutors();

    }

    public void setupToolbar() {
        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setSubtitle(getString(R.string.app_sub_name));
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
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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
        if (id == R.id.action_about) {
            aboutIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Audio audio, FancyButton fancyButton, View container) {

        if (!mMultiSelect) {
            mAppMediaPlayer.setButton(fancyButton);
            mAppMediaPlayer.playAudio(audio.getAudioPath());

            if(audio.isNew()) {
                audio.setNew(false);
                mSoundViewModel.updateAudio(audio);
            }

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
        audio.setFavorite(flag);
        mSoundViewModel.updateAudio(audio);
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
        menu.add(getString(R.string.share_audio));
        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {

        if (menuItem.getTitle().equals(getString(R.string.share_audio))) {

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

    private void shareApp(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    public void facebookIntent() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/2004974256454670"));
            startActivity(intent);
        } catch(Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/Falas-Ciro-Gomes-2004974256454670")));
        }
    }

    public void twitterIntent(){
        Intent intent = null;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=948635988892569601"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/FalasCiroGomes"));
        }
        this.startActivity(intent);
    }

    private void emailIntent(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","falasdocirogomes@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact App Falas do Ciro Gomes");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void aboutIntent(){
        new LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .start(this);
    }

    public void surveyIntent(){
        String url = "https://goo.gl/forms/1QhmbIDT1Cc4sXam1";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int menuId = item.getItemId();

        switch (menuId){
            case R.id.nav_send_suggestion:
                surveyIntent();
                return false;
            case R.id.nav_share:
                shareApp();
                return false;
            case R.id.nav_facebook:
                facebookIntent();
                return false;
            case R.id.nav_twitter:
                twitterIntent();
                return false;
            case R.id.nav_email:
                emailIntent();
                return false;
            case R.id.nav_about:
                aboutIntent();
                return false;
            default:
                break;
        }

        return false;
    }
}

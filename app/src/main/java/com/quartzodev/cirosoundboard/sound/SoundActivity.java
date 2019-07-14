package com.quartzodev.cirosoundboard.sound;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.quartzodev.cirosoundboard.R;
import com.quartzodev.cirosoundboard.ViewModelFactory;
import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.GenericDataSource;
import com.quartzodev.cirosoundboard.utils.AnswersUtil;
import com.quartzodev.cirosoundboard.utils.AppExecutors;
import com.quartzodev.cirosoundboard.utils.AppMediaPlayer;
import com.quartzodev.cirosoundboard.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import io.fabric.sdk.android.Fabric;
import mehdi.sakout.fancybuttons.FancyButton;

public class SoundActivity extends AppCompatActivity implements SoundFragment.SoundFragmentListener,
        GenericDataSource.LoadListCallback<Section>,
        View.OnClickListener, android.view.ActionMode.Callback,
        NavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener{

    private AppMediaPlayer mAppMediaPlayer;
    private SoundSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SoundViewModel mSoundViewModel;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private ActionMode mMode;
    private AppExecutors mAppExecutors;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private AdManager adManager;

    private boolean mMultiSelect = false;
    private Map<Audio, View> mSelectedItems = new HashMap<Audio, View>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new Answers());
        setContentView(R.layout.sound_act);
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, getString(R.string.app_id));

        mCoordinatorLayout = findViewById(R.id.main_content);
        mViewPager = findViewById(R.id.container);
        mToolbar = findViewById(R.id.toolbar);
        mAdView = findViewById(R.id.adView);

        setSupportActionBar(mToolbar);

        loadAdView();
        loadmInterstitialAd();
        setupToolbar();
        setupFloatButton();
        setupViewPager();
        setupDrawer();

        mAppMediaPlayer = AppMediaPlayer.newInstance(this, this);
        mSoundViewModel = obtainViewModel(this);
        mSoundViewModel.loadSections(this);

        mAppExecutors = new AppExecutors();

    }

    private void loadAdRequest() {
        if (!mInterstitialAd.isLoading()) {
            AdRequest request = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(request);
        }
    }

    private void showInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            adManager.flagAdIsOpen = true;
        }
    }

    public void loadmInterstitialAd(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ad_inter_main_activity));
        adManager = new AdManager();
        mInterstitialAd.setAdListener(adManager);

    }

    public void loadAdView(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void setupToolbar() {
        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setSubtitle(getString(R.string.app_sub_name));
    }

    public void setupFloatButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdRequest();
    }

    public void setupViewPager() {

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SoundSectionsPagerAdapter(getSupportFragmentManager(), new ArrayList<Section>(), getString(R.string.tab_favorite));
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(this);

    }

    public void setupDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
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

            AnswersUtil.onPlayAudioMetric(audio);

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
    public void onFavoriteClick(final Audio audio, final boolean flag, View container) {

        if(!mMultiSelect) {

            if (flag)
                AnswersUtil.onFavoriteAudioMetric(audio);

            audio.setFavorite(flag);
            mSoundViewModel.updateAudio(audio);

            String msg;

            if (flag) {
                msg = String.format(getString(R.string.favorite_audio), audio.getLabel());
            } else {
                msg = String.format(getString(R.string.not_favorite_audio), audio.getLabel());
            }

            Snackbar.make(mCoordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
        }else{
            selectItem(audio, container);
        }

    }

    @Override
    public void onShareClick(Audio audio, View container) {
        if (!mMultiSelect) {
            mMode = mToolbar.startActionMode(this);
            selectItem(audio, container);
        }else {
            selectItem(audio, container);
        }
    }

    @Override
    public void onListLoaded(List<Section> list) {
        mSectionsPagerAdapter.swap(list);
    }

    @Override
    public void onDataNotAvailable() {
    }

    @Override
    public void onClick(View view) {
        mSoundViewModel.loadRandomAudio(new GenericDataSource.GetObjectCallback<Audio>() {
            @Override
            public void onObjectLoaded(Audio audio) {
                mAppMediaPlayer.playRandomAudio(audio.getAudioPath());

                Snackbar.make(mCoordinatorLayout, String.format(getString(R.string.playing_audio),audio.getLabel()), Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
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

            AnswersUtil.onShareAudioMetric(audioToShare);

            Uri fileToShareUri = FileUtils.getExistingFile(this, audioToShare.getLabel());

            if (fileToShareUri == null) {
                fileToShareUri = FileUtils.writeStreamToFile(this, audioToShare.getLabel(), audioToShare.getAudioPath());
            }

            uriList.add(fileToShareUri);
        }

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE).setType("audio/x-mpeg3");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        startActivity(Intent.createChooser(intent, getString(R.string.send_to)));

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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(mSoundViewModel.isShowInterstitialAd()){
            showInterstitialAd();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class AdManager extends AdListener {

        public boolean flagAdIsOpen;

        public AdManager() {
            flagAdIsOpen = false;
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            flagAdIsOpen = false;
            loadAdRequest();
        }
    }
}

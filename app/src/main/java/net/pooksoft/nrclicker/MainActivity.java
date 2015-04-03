package net.pooksoft.nrclicker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;

import net.pooksoft.nrclicker.model.DataEntry;
import net.pooksoft.nrclicker.ui.LabeledNumberPicker;
import net.pooksoft.nrclicker.ui.TurnClicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements
        ActionBar.TabListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        ValueChangeListener, GameStateListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    ActionBar mActionBar;
    ArrayList<Integer> labeledNumberPickers;

    GameState gs;

    public String getApplicationName() {
        int stringId = getApplicationContext().getApplicationInfo().labelRes;
        return getApplicationContext().getString(stringId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gs = new GameState(this);
        updateAppTitle();

        // Set up the action bar.
        mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            mActionBar.addTab(
                    mActionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        // Keep the screen on in this app
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        labeledNumberPickers = new ArrayList<>();
        labeledNumberPickers.add(R.id.lnpAgendasCorp);
        labeledNumberPickers.add(R.id.lnpAgendasRunner);
        labeledNumberPickers.add(R.id.lnpBadPublicity);
        labeledNumberPickers.add(R.id.lnpBrainDamage);
        labeledNumberPickers.add(R.id.lnpCreditsCorp);
        labeledNumberPickers.add(R.id.lnpCreditsRunner);
        labeledNumberPickers.add(R.id.lnpLinkStrength);
        labeledNumberPickers.add(R.id.lnpTags);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, 1);

            return true;
        }

        if (id == R.id.action_statistics) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, StatisticsActivity.class);
            startActivityForResult(intent, 1);

            return true;
        }

        if (id == R.id.action_newgame) {
            clearAndReset();

            return true;
        }

        if (id == R.id.action_endgame) {
            saveStats();

            clearAndReset();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveStats() {
        gs.saveData(getData(labeledNumberPickers));
    }

    private List<DataEntry> getData(ArrayList<Integer> labeledNumberPickers) {
        List<DataEntry> data = new ArrayList<>();
        for (int lnpId : labeledNumberPickers) {
            int value = ((LabeledNumberPicker) this.findViewById(lnpId)).getValue();
            data.add(new DataEntry(lnpId, value, getResources().getResourceEntryName(lnpId)));
        }
        return data;
    }

    private void clearAndReset() {
        TurnClicker tc = (TurnClicker) findViewById(R.id.turnClickerCorp);
        tc.clearButtons();
        tc = (TurnClicker) findViewById(R.id.turnClickerRunner);
        tc.clearButtons();

        for (int lnp : labeledNumberPickers) {
            ((LabeledNumberPicker) this.findViewById(lnp)).reset();
        }

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /**
         * Init the app
         */
        mViewPager.setCurrentItem(GameState.CORP);
        onValueUpdated(R.id.lnpCreditsCorp, 5);
        onValueUpdated(R.id.lnpCreditsRunner, 5);

        gs = new GameState(this);
        updateAppTitle();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("screen_sleep_disabled")) {

            boolean sleep_off = sharedPreferences.getBoolean("screen_sleep_disabled", true);
            Log.d("test", "sleep_off: " + Boolean.toString(sleep_off));
            if (sleep_off) {
                // Don't keep the screen on in this app
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            else {
                // Keep the screen on in this app
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
    }

    @Override
    public void onValueUpdated(int id, int value) {
        switch (id) {
            case R.id.lnpCreditsCorp:
                mSectionsPagerAdapter.setCredits(GameState.CORP, value);
                mActionBar.getTabAt(GameState.CORP).setText(mSectionsPagerAdapter.getPageTitle(GameState.CORP));
                break;
            case R.id.lnpCreditsRunner:
                mSectionsPagerAdapter.setCredits(GameState.RUNNER, value);
                mActionBar.getTabAt(GameState.RUNNER).setText(mSectionsPagerAdapter.getPageTitle(GameState.RUNNER));
                break;
            default:
                break;
        }

    }

    @Override
    public void onNextTurn() {
        // update app title
        updateAppTitle();
    }

    private void updateAppTitle() {
        this.setTitle(this.getApplicationName() + " - turn " + gs.getNumRoundsAsString());
    }

    public void startNextTurn() {
        TurnClicker tc = (TurnClicker) findViewById(R.id.turnClickerCorp);
        tc.clearButtons();
        tc = (TurnClicker) findViewById(R.id.turnClickerRunner);
        tc.clearButtons();

        gs.switchPlayer();
        mViewPager.setCurrentItem(gs.getActivePlayer());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean rotate = prefs.getBoolean("rotation_enabled", true);
        if (rotate) {
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == Surface.ROTATION_0) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        gs.startNextTurn();
    }

    public void dontStartNextTurn() {
        int i = mViewPager.getCurrentItem();
        if (i == GameState.CORP) {
            TurnClicker tc = (TurnClicker) findViewById(R.id.turnClickerCorp);
            tc.clearLastButton();
        } else {
            TurnClicker tc = (TurnClicker) findViewById(R.id.turnClickerRunner);
            tc.clearLastButton();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int creditsCorp = 5;
        private int creditsRunner = 5;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case GameState.CORP:
                    return CorpFragment.newInstance();
                case GameState.RUNNER:
                    return RunnerFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case GameState.CORP:
                    return getString(R.string.title_section1).toUpperCase(l) + " (" + Integer.toString(creditsCorp) + " C)";
                case GameState.RUNNER:
                    return getString(R.string.title_section2).toUpperCase(l) + " (" + Integer.toString(creditsRunner) + " C)";
            }
            return null;
        }

        public void setCredits(int position, int value) {
            switch (position) {
                case GameState.CORP:
                    creditsCorp = value;
                case GameState.RUNNER:
                    creditsRunner = value;
            }

            this.notifyDataSetChanged();
        }
    }
}

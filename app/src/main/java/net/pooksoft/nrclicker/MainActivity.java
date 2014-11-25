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

import net.pooksoft.nrclicker.ui.LabeledNumberPicker;
import net.pooksoft.nrclicker.ui.TurnClicker;

import java.util.Locale;


public class MainActivity extends Activity implements ActionBar.TabListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int CORP = 0;
    private static final int RUNNER = 1;

    private int numRounds = 0;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        // Keep the screen on in this app
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

            /*getFragmentManager().beginTransaction()
                    .add(R.id.main_container, SettingsFragment.newInstance())
                    .addToBackStack(null)
                    .commit();*/
            return true;
        }

        if (id == R.id.action_newgame) {
            TurnClicker tc = (TurnClicker)findViewById(R.id.turnClickerCorp);
            tc.clearButtons();
            tc = (TurnClicker)findViewById(R.id.turnClickerRunner);
            tc.clearButtons();

            /*((CorpFragment)mSectionsPagerAdapter.getItem(CORP)).clearValues();
            ((RunnerFragment)mSectionsPagerAdapter.getItem(RUNNER)).clearValues();
*/
            ((LabeledNumberPicker)this.findViewById(R.id.lnpBadPublicity)).reset();
            ((LabeledNumberPicker)this.findViewById(R.id.lnpBrainDamage)).reset();
            ((LabeledNumberPicker)this.findViewById(R.id.lnpLinkStrength)).reset();
            ((LabeledNumberPicker)this.findViewById(R.id.lnpTags)).reset();
            ((LabeledNumberPicker)this.findViewById(R.id.lnpAgendasCorp)).reset();
            ((LabeledNumberPicker)this.findViewById(R.id.lnpCreditsCorp)).reset();
            ((LabeledNumberPicker)this.findViewById(R.id.lnpAgendasRunner)).reset();
            ((LabeledNumberPicker)this.findViewById(R.id.lnpCreditsRunner)).reset();
            mViewPager.setCurrentItem(CORP);

            numRounds = 0;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case CORP:
                    return CorpFragment.newInstance();
                case RUNNER:
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
                case CORP:
                    return getString(R.string.title_section1).toUpperCase(l);
                case RUNNER:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    public void doPositiveClick() {
        TurnClicker tc = (TurnClicker)findViewById(R.id.turnClickerCorp);
        tc.clearButtons();
        tc = (TurnClicker)findViewById(R.id.turnClickerRunner);
        tc.clearButtons();

        int i = mViewPager.getCurrentItem();
        i ^= 1; // XOR with 1, obviously stops working if more than two tabs.
        mViewPager.setCurrentItem(i);

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
    }

    public void doNegativeClick() {
        int i = mViewPager.getCurrentItem();
        if (i == CORP) {
            TurnClicker tc = (TurnClicker)findViewById(R.id.turnClickerCorp);
            tc.clearLastButton();
        }
        else {
            TurnClicker tc = (TurnClicker)findViewById(R.id.turnClickerRunner);
            tc.clearLastButton();
        }
    }
}

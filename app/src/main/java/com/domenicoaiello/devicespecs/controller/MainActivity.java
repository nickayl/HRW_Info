package com.domenicoaiello.devicespecs.controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.domenicoaiello.devicespecs.R;
import com.domenicoaiello.devicespecs.model.DataProvider;
import com.domenicoaiello.devicespecs.model.valuechangelistener.ListenerPoolExecutor;
import com.domenicoaiello.devicespecs.model.TextTabWithTvp;
import com.domenicoaiello.devicespecs.model.adapters.fragment.DeviceSpecPagerAdapter;
import com.domenicoaiello.devicespecs.model.Battery;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.Utils;
import com.domenicoaiello.devicespecs.model.sensor.Sensors;
import com.domenicoaiello.simpletab.OnTabClickListener;
import com.domenicoaiello.simpletab.Tab;
import com.domenicoaiello.simpletab.TabContainer;
import com.domenicoaiello.simpletab.style.DefaultTabStyle;
import com.domenicoaiello.simpletab.style.TabStyle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private Battery battery;
    private Sensors sensor;
    private static final String ADMOB_APP_ID_REAL = "ca-app-pub-1737562881577853~7319498424";
    private static final String ADMOB_INTERSTITIAL_UNIT_ID_TEST = "ca-app-pub-3940256099942544/1033173712";
    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ViewPager mViewPager;
    private TabContainer tabController;

    // App per la prenotazione nei ristoranti
    // Sistema di prenotazione centralizzato

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(createBundleNoFragmentRestore(savedInstanceState));
        Log.d("ACTIVITY-LIFECYCLE", "OnCreate triggered");

        setContentView(R.layout.activity_main2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, ADMOB_APP_ID_REAL);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(ADMOB_INTERSTITIAL_UNIT_ID_TEST);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // ---------
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(Utils.getGlobalTypeface(this));

        Toolbar actionBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(actionBar);

        ImageView about = (ImageView) findViewById(R.id.about);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        about.setOnClickListener((view) -> {
            Intent i = new Intent(context, AboutActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "info-app");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Clicked info button");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Info about me");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            startActivity(i);
        });

        try {
            DataProvider dataProvider = new DataProvider(this);

            battery = new Battery(this);
            sensor = new Sensors(this);

            List<TextTitleValuePair> tab1Pairs = dataProvider.getProcessorData();
            tab1Pairs.addAll(dataProvider.getGpuData());

            List<TextTitleValuePair> tab2Pairs = dataProvider.getDeviceData();
            tab2Pairs.addAll(dataProvider.getScreenData());

            tabController = (TabContainer) findViewById(R.id.tabContainer2);

            TabStyle style = new DefaultTabStyle(this);

            TextTabWithTvp tab1 = new TextTabWithTvp(this, 0, style);
            tab1.setName("Soc");
            tab1.setTitleValuePairs(tab1Pairs);
        //    tab1.setImageValuePairs(processor.getImageValuePairProvider());
            tab1.setSelected();

            TextTabWithTvp tab2 = new TextTabWithTvp(this, 1, style);
            tab2.setName("Device");
            tab2.setTitleValuePairs(tab2Pairs);

            TextTabWithTvp tab3 = new TextTabWithTvp(this, 2, style);
            tab3.setName("System");
            tab3.setTitleValuePairs(dataProvider.getSystemData());

            TextTabWithTvp tab4 = new TextTabWithTvp(this, 3, style);
            tab4.setName("Battery");
            tab4.setTitleValuePairs(battery.getTitleValuePair());

            TextTabWithTvp tab5 = new TextTabWithTvp(this, 4, style);
            tab5.setName("Sensors");
            tab5.setTitleValuePairs(sensor.getTitleValuePair());


            tabController.addTabs(tab1,tab2,tab3,tab4,tab5);
            tabController.setSelectedTab(0);
            tabController.setBackgroundColor(style.getBackgroundColor());

            tabController.setOnTabClickListener(new OnTabClickListener() {
                @Override
                public void onClick(Tab tab, int position) {
                    mViewPager.setCurrentItem(position, true);
                }
            });

            DeviceSpecPagerAdapter mCollectionPagerAdapter = new DeviceSpecPagerAdapter(getSupportFragmentManager(), tabController, this);
            mViewPager.setAdapter(mCollectionPagerAdapter);

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Log.i("INFO", "PAGE SCROLLED");
                }

                @Override
                public void onPageSelected(int position) {
                    tabController.setSelectedTab(position);

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "switch-tab");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Switched to tab " + tabController.getTab(position).getName());
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Switch tab to "+ tabController.getTab(position).getName());
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    Log.i("INFO", "-------------------- PAGE SELECTED -----------------");
                    /*
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    } */
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    Log.i("INFO", "PAGE SCROLL --STATE-- CHANGED");
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }

        Log.d("debug-info","num cores: "+Runtime.getRuntime().availableProcessors());
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ACTIVITY-LIFECYCLE", "OnStart triggered");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ACTIVITY-LIFECYCLE", "OnPause triggered");

        unregisterReceiver(battery);
        sensor.unregisterListeners();

        ListenerPoolExecutor
                .getInstance()
                .suspendAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ACTIVITY-LIFECYCLE", "onResume triggered");

        registerReceiver(battery, battery.getIntentFilter());
        sensor.registerListeners();

        ListenerPoolExecutor
                .getInstance()
                .executeAll();

        System.gc();
        Runtime.getRuntime().gc();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("ACTIVITY-LIFECYCLE", "OnRestart triggered");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ACTIVITY-LIFECYCLE", "OnDestroy triggered");

        ListenerPoolExecutor
                .getInstance()
                .removeAll();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //here you can handle orientation change
    }


    /**
     * Improve bundle to prevent restoring of fragments.
     * @param bundle bundle container
     * @return improved bundle with removed "fragments parcelable"
     */
    private static Bundle createBundleNoFragmentRestore(Bundle bundle) {
        if (bundle != null) {
            bundle.remove("android:support:fragments");
        }
        return bundle;
    }




    // app admob id: 1737562881577853~7319498424

        /*
        #cat "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"
#cat "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"
#cat "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"

// if the cpu is in use or stopped
cat /sys/devices/system/cpu/cpu0/online

// --
cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor

// get current frequency for the cpu[i]
cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq

// (ONLY QUALCOMM GPUS) get the max gpu clock
cat sys/class/kgsl/kgsl-3d0/max_gpuclk

// (ONLY QUALCOMM GPUS) get the current usage of gpu as percentage (first_number/second_number)*100
cat sys/class/kgsl/kgsl-3d0/gpubusy

// SYSTEM UPTIME
cat proc/uptime

// GPU Mali
/sys/class/misc/mali0/device/clock

// cat sys/devices/platform/HardwareInfo/hq_hardwareinfo

// system uptime
// cat proc/uptime
*/

    // ---------

      /*  TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String getSimSerialNumber = telemamanger.getSimSerialNumber();
        String getSimNumber = telemamanger.getLine1Number();

        telemamanger */

      /*
        try {
            Build b = Build;
            InputStream is = getAssets().open("text/devices_list.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(br).getAsJsonArray();
            for(JsonElement e : array) {
                JsonObject obj = e.getAsJsonObject();
                String deviceName = obj.get("DeviceName").getAsString();
                deviceName = deviceName.trim().toLowerCase().replaceAll("(4g|lte|[ ]|wifi)","");
                deviceName.trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
*/
}

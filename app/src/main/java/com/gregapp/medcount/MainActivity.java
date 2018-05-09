package com.gregapp.medcount;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.gregapp.medcount.Fagment.BarcodeFragment;
import com.gregapp.medcount.Fagment.HomeFragment;
import com.gregapp.medcount.Fagment.PhotoFragment;
import com.gregapp.medcount.Fagment.SettingFragment;
import com.gregapp.medcount.Helper.MyHelper;
import com.gregapp.medcount.Model.BarcodeItem;
import com.gregapp.medcount.Model.PhotoItem;
import com.gregapp.medcount.dummy.DummyContent;
import com.gregapp.medcount.receiver.AlarmReceiver;

public class MainActivity extends AppCompatActivity
        implements BarcodeFragment.OnListFragmentInteractionListener,
                   PhotoFragment.OnListFragmentInteractionListener
{
    private BottomNavigationView navigation;
    private MenuItem currentItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            return true;
        }
    };

    private void selectFragment(MenuItem item){
        if(currentItem != item) {
            MyHelper.disableShiftMode(navigation);
            currentItem = item;
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = HomeFragment.newInstance();
                    break;
                case R.id.navigation_barcode:
                    selectedFragment = BarcodeFragment.newInstance(1);
                    break;
                case R.id.navigation_photo:
                    selectedFragment = PhotoFragment.newInstance(1);
                    break;
                case R.id.navigation_setting:
                    selectedFragment = (Fragment) SettingFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        selectFragment(menu.getItem(0));

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

    }

    @Override
    public void onListFragmentInteraction(BarcodeItem item) {

    }

    @Override
    public void onListFragmentInteraction(PhotoItem item) {

    }
}

package cn.edu.china.ntu.bluetoothchat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity {

    private ListView deviceListView;

    private BluetoothAdapter btAdapter;

    private BottomNavigationView navigationView;

    private static final ConcurrentHashMap<String, BluetoothDevice> deviceMap = new ConcurrentHashMap<>();


    private BluetoothReceiver receiver;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            try {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        deviceListView.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.navigation_dashboard:
                        navigationView.setVisibility(View.GONE);
                        deviceListView.setVisibility(View.INVISIBLE);
                        return true;
                    case R.id.navigation_notifications:
                        deviceListView.setVisibility(View.INVISIBLE);
                        return true;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceListView = (ListView) findViewById(R.id.devicelist_view);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        init();
        initBluetooth();
        LayoutInflater inflater = getLayoutInflater();
        DeviceAdapter deviceAdapter = new DeviceAdapter(Arrays.asList("xiaofeiyu","sdf"),inflater);
        this.deviceListView.setAdapter(deviceAdapter);
        deviceListView.setVisibility(View.INVISIBLE);
    }

    private void init() {
        this.receiver = new BluetoothReceiver();
    }

    private void initBluetooth() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }

        btAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        receiver = new BluetoothReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(this.receiver);
        super.onDestroy();
    }


    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (CommonUtils.isStringNullOrEmpty(device.getAddress())) {
                        return;
                    }
                    deviceMap.put(device.getAddress(), device);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("error", "BluetoothReceiver.onReceive", ex);
            }
        }
    }

}

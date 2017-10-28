package cn.edu.china.ntu.bluetoothchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by panfei on 2017/10/28.
 */

public class DeviceAdapter extends BaseAdapter {

    /**
     * 设备数据
     */
    private List<String> devices;

    /**
     * 定义Inflater,加载我们自定义的布局
     */
    private LayoutInflater mInflater;

    public DeviceAdapter(List<String> devices, LayoutInflater inflater){
        this.devices = devices;
        this.mInflater = inflater;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View deviceView = mInflater.inflate(R.layout.devicerow, null);
        String device = devices.get(position);
        TextView textView = (TextView) deviceView.findViewById(R.id.devicerow_txt);
        textView.setText(device);
        return deviceView;
    }
}

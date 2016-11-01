package com.timestudio.mynews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timestudio.mynews.R;

/**
 * Created by hasee on 2016/11/1.
 */

public class MyCenterAdapter extends MyBaseAdapter<String[]> {

    public MyCenterAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.mycenter_log_item, null);
            holder.time = (TextView) convertView.findViewById(R.id.tv_mycenter_item_time);
            holder.address = (TextView) convertView.findViewById(R.id.tv_mycenter_item_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.time.setText(myList.get(position)[0]);
        holder.address.setText(myList.get(position)[1]);
        return convertView;
    }

    class ViewHolder {
        TextView time;
        TextView address;
    }
}

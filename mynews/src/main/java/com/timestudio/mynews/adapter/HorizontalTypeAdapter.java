package com.timestudio.mynews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timestudio.mynews.R;
import com.timestudio.mynews.entity.NewsTitleHorizontal;

/**
 * Created by thinkpad on 2016/10/18.
 */

public class HorizontalTypeAdapter extends MyBaseAdapter {

    private int positionIndex;
    private Context mContext;
    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public HorizontalTypeAdapter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.horizontal_item,null);
        TextView tv_type_title = (TextView) convertView.findViewById(R.id.tv_type_title);
        tv_type_title.setText(((NewsTitleHorizontal)myList.get(position)).getSubgroup());
        if (positionIndex == position) {
            tv_type_title.setTextColor(mContext.getResources().getColor(R.color.sel_btn_login_false));
        }
        return convertView;
    }
}

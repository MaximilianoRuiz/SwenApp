package com.example.maxi.swenapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.VOs.FanPageVO;
import com.example.maxi.swenapp.data.DataBaseHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FanPageListAdapter extends ArrayAdapter<FanPageVO> {

    Context context;
    public List<FanPageVO> fanPageVOs;
    public ViewHolder viewHolder;
    DataBaseHandler dataBaseHandler;

    public FanPageListAdapter(Context c, List<FanPageVO> fanPageVOs) {
        super(c, R.layout.post_list_adapter_row, fanPageVOs);
        this.context = c;
        this.fanPageVOs = fanPageVOs;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null) {

            dataBaseHandler = new DataBaseHandler(context);
            dataBaseHandler.open();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fan_page_selector_adapter_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.fanPageName = (TextView) convertView.findViewById(R.id.tvFanPageName);
            viewHolder.fanPageCategori = (TextView) convertView.findViewById(R.id.tvFanPageCategori);
            viewHolder.cheched = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.ivImageFP);

            viewHolder.cheched.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    dataBaseHandler.upDate(fanPageVOs.get(position).getId(),
                            fanPageVOs.get(position).getName(), fanPageVOs.get(position).getCategori(),
                            fanPageVOs.get(position).getPicture(), b, fanPageVOs.get(position).getUrl());
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FanPageVO fanPageVO = fanPageVOs.get(position);
        if(fanPageVO != null) {
            viewHolder.fanPageName.setText(fanPageVO.getName());
            viewHolder.fanPageCategori.setText(fanPageVO.getCategori());
            viewHolder.cheched.setChecked(fanPageVO.isChecked());
            try{
                Picasso.with(context).load(fanPageVO.getPicture()).into(viewHolder.picture);
            } catch (Exception e){
                String s = e.getMessage();
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView fanPageName, fanPageCategori;
        ImageView picture;
        CheckBox cheched;
    }
}

package com.example.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class listViewAdapter extends ArrayAdapter<BikeData> {

    private List<BikeData> bikeList;
    private Context context;

    public listViewAdapter(Context context, List<BikeData> bikes ){
        super(context, R.layout.listview_row_layout, bikes);
        this.context = context;
        this.bikeList = bikes;
    }

    private static class ViewHolder {
        TextView price;
        TextView description;
        TextView model;
        ImageView pic;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        LayoutInflater inflater = LayoutInflater.from(context);

        if(convertView == null){

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_row_layout,null);
            viewHolder.price = (TextView) convertView.findViewById(R.id.Price);
            viewHolder.model = (TextView) convertView.findViewById(R.id.Model);
            viewHolder.description = (TextView) convertView.findViewById(R.id.Description);


            convertView.setTag(viewHolder);
        }

        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BikeData current = bikeList.get(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
        DownloadImageTask dlImage = new DownloadImageTask("http://www.tetonsoftware.com/bikes", imageView);
        dlImage.execute("http://www.tetonsoftware.com/bikes/" + current.getPicture());



        viewHolder.price.setText(current.getPrice() + "");
        viewHolder.model.setText(current.getModel());
        viewHolder.description.setText(current.getDescription());

        return convertView;
    }
}

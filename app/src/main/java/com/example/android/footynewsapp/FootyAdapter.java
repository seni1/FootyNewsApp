package com.example.android.footynewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FootyAdapter extends ArrayAdapter<Footy> {

    public FootyAdapter(Context context, ArrayList<Footy> allFooties) {
        super(context, 0, allFooties);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.footy_item, parent, false);
        }

        Footy currentFooty = getItem(position);


        TextView typeTextView = listItemView.findViewById(R.id.type_tv);
        TextView titleTextView = listItemView.findViewById(R.id.title_tv);
        TextView sectionNameTextView = listItemView.findViewById(R.id.section_tv);
        TextView dateTextView = listItemView.findViewById(R.id.date_tv);

        String type = String.valueOf(currentFooty.getType());
        String title = String.valueOf(currentFooty.getWebTitle());
        String sectionName = String.valueOf(currentFooty.getWebSectionName());
        String date = String.valueOf(currentFooty.getWebPublicationDate());

        typeTextView.setText(type);
        titleTextView.setText(title);
        sectionNameTextView.setText(sectionName);
        dateTextView.setText(date);

        return listItemView;
    }

}

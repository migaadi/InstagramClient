package com.gaadi.instagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    // What data do we need from the activity
    // Context, Data Source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
    // What our items look like
    // Use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this view
        InstagramPhoto photo = getItem(position);
        // Check if we are using a recycled view, if not we need to inflate
        if (convertView == null) {
            // create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Lookup the views for populating the data (image, caption)
        // Insert the model data into each of the view items
        // Caption
        //TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        //tvCaption.setText(photo.caption);
        // Likes count
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
        String likesCount_formatted = String.valueOf(photo.likesCount) + " likes";
        tvLikesCount.setText(likesCount_formatted);
        // Username & Caption in same TextView
        TextView tvUsernameCaption = (TextView) convertView.findViewById(R.id.tvUsernameCaption);
        String formatted_username = "<b>" + photo.username + "</b>";
        String username_caption = Html.fromHtml(formatted_username) + " -- " + photo.caption;
        tvUsernameCaption.setText(username_caption);
        // Image - Graphic
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        // Clear out the ImageView if it is recycled (right away)
        ivPhoto.setImageResource(0);
        // Insert the image using Picasso (send out async request)
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        // Returned the created item as a view
        return convertView;
    }
}

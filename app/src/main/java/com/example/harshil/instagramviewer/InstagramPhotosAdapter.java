package com.example.harshil.instagramviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harshil on 1/25/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    // view lookup cache, to improve the performance use ViewHolder pattern
    private class ViewHolder {
        TextView userName;
        TextView caption;
        TextView timestamp;
        ImageView photo;
        ImageView profilePhoto;
    }
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> instagramPhotos) {
        super(context, R.layout.item_photos, instagramPhotos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the item for the postion
        InstagramPhoto photo = getItem(position);

        ViewHolder viewHolder;  // view lookup cache stored in tag for better scrolling performance
        // Use recycle view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photos, parent, false);
            viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.profilePhoto = (ImageView) convertView.findViewById(R.id.ivUserPhoto);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate the data into template view
        viewHolder.caption.setText(photo.caption);
        viewHolder.userName.setText(photo.userName);
        viewHolder.timestamp.setText(photo.postTimestamp);
        viewHolder.photo.getLayoutParams().height = photo.imageHeight;
        // reset the image from recycle view
        viewHolder.photo.setImageResource(0);
        viewHolder.profilePhoto.setImageResource(0);
        viewHolder.profilePhoto.setAdjustViewBounds(true);  // to scale image more like profile picture
        // Ask for the photo to be added based on the image usr.
        // send a network request to the url, download the image bytes, convert into bitmap/ may be resize, insert bitmap into the imageview
        Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.photo);
        Picasso.with(getContext()).load(photo.profileImageUrl).into(viewHolder.profilePhoto);

        // return the completed view to render on screen
        return convertView;
    }
}

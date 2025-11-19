package com.example.restandroam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class GenericListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<?> items;
    private int layoutResource;
    private LayoutInflater inflater;

    public GenericListAdapter(Context context, ArrayList<?> items, int layoutResource) {
        this.context = context;
        this.items = items;
        this.layoutResource = layoutResource;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layoutResource, parent, false);
        }

        Object item = items.get(position);

        if (item instanceof Bungalow) {
            Bungalow bungalow = (Bungalow) item;

            ImageView bungalowImage = convertView.findViewById(R.id.bungalowImage);
            TextView bungalowName = convertView.findViewById(R.id.bungalowName);
            TextView bungalowDescription = convertView.findViewById(R.id.bungalowDescription);
            TextView bungalowLocation = convertView.findViewById(R.id.bungalowLocation);
            TextView bungalowPrice = convertView.findViewById(R.id.bungalowPrice);
            TextView bungalowAvailability = convertView.findViewById(R.id.bungalowAvailability);

            bungalowImage.setImageResource(bungalow.getImgID());

            bungalowName.setText(bungalow.getName());
            bungalowLocation.setText(bungalow.getLocation());
            bungalowPrice.setText("Price: $" + bungalow.getPrice());

            bungalowDescription.setText("Capacity: " + bungalow.getCapacity());

            if (bungalow.isReserved()) {
                bungalowAvailability.setText("Reserved");
                bungalowAvailability.setTextColor(Color.RED);
            } else {
                bungalowAvailability.setText("Available Now");
                bungalowAvailability.setTextColor(Color.GREEN);
            }

            Button bookButton = convertView.findViewById(R.id.bookBungalowButton);
            bookButton.setOnClickListener(e -> {
                ((BungalowsActivity) context).showBungalowBookingDialog(bungalow);
            });
        } else if (item instanceof Activity) {
            Activity activity = (Activity) item;

            TextView activityName = convertView.findViewById(R.id.activityName);
            TextView activityPrice = convertView.findViewById(R.id.activityPrice);
            TextView activityLocation = convertView.findViewById(R.id.activityLocation);

            activityName.setText(activity.getName());
            activityPrice.setText("Price: $" + activity.getPrice());
            activityLocation.setText(activity.getLocation());

        } else if (item instanceof Restaurant) {
            Restaurant restaurant = (Restaurant) item;

            TextView restaurantName = convertView.findViewById(R.id.restaurantName);
            TextView restaurantLocation = convertView.findViewById(R.id.restaurantLocation);
            TextView restaurantOpeningHours = convertView.findViewById(R.id.restaurantOpeningHours);

            restaurantName.setText(restaurant.getName());
            restaurantLocation.setText(restaurant.getLocation());
            restaurantOpeningHours.setText("Hours: " + restaurant.getOpeningHours());
        }
        return convertView;
    }
}


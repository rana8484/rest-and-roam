package com.example.restandroam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BungalowsActivity extends AppCompatActivity {

    private ListView bungalowsListView;
    private GenericListAdapter genericAdapter;
    private DBHandler dbHandler;
    private Button book;

    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bungalows);

        book = new Button(this);
        book.findViewById(R.id.bookBungalowButton);
        bungalowsListView = new ListView(this);
        bungalowsListView = findViewById(R.id.bungalowsListView);
        dbHandler = new DBHandler(this);

        Bungalow b1 = new Bungalow("Merchak Guest House L","Batroun",120,false,null,8,R.drawable.imgb1);
        Bungalow b2 = new Bungalow("Merchak Guest House M","Batroun",100,false,null,5,R.drawable.imgb2);
        Bungalow b3 = new Bungalow("Merchak Guest House S","Batroun",80,false,null,3,R.drawable.imgb3);
        Bungalow b4 = new Bungalow("Orizon","Jbeil",110,false,null,6,R.drawable.imgb4);
        Bungalow b5 = new Bungalow("Le Blanc Bleu", "Jounieh", 150, false, null, 4,R.drawable.imgb5);
        dbHandler.addBungalow(b1);
        dbHandler.addBungalow(b2);
        dbHandler.addBungalow(b3);
        dbHandler.addBungalow(b4);
        dbHandler.addBungalow(b5);

        ArrayList<Bungalow> bungalowsList = dbHandler.getAllBungalows();
        genericAdapter = new GenericListAdapter(this, bungalowsList, R.layout.bungalow_item);
        bungalowsListView.setAdapter(genericAdapter);
    }

    @SuppressLint("SetTextI18n")
    public void showBungalowBookingDialog(Bungalow bungalow) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_booking, null);
        builder.setView(dialogView);

        TextView bookingTitle = dialogView.findViewById(R.id.bookingTitle);
        TextView itemDetails = dialogView.findViewById(R.id.itemDetails);
        TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
        DatePicker datePicker = dialogView.findViewById(R.id.bookingDatePicker);
        Button confirmButton = dialogView.findViewById(R.id.confirmBookingButton);

        bookingTitle.setText("Book Bungalow");
        itemDetails.setText("Name: " + bungalow.getName() + "\nLocation: " + bungalow.getLocation());
        priceDetails.setText("Price: $" + bungalow.getPrice());

        disablePastDates(datePicker);

        confirmButton.setOnClickListener(e -> {
            String username = getIntent().getStringExtra("username");

            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int dayOfMonth = datePicker.getDayOfMonth();

            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

            if (isDateInThePast(selectedDate)) {
                Toast.makeText(BungalowsActivity.this, "You cannot choose a date in the past.", Toast.LENGTH_SHORT).show();
            } else {
                boolean done = confirmBungalowBooking(bungalow, datePicker, username);
                if (done) {
                    Intent intent = new Intent(BungalowsActivity.this, MainActivity.class);
                    intent.putExtra("selectedBungalow", bungalow.getName());
                    startActivity(intent);
                }
            }
        });

        builder.create().show();
    }


    public boolean confirmBungalowBooking(Bungalow bungalow, DatePicker datePicker, String username) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date selectedDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String bookingDate = dateFormat.format(selectedDate);

        if (bungalow.isReserved()) {
            Toast.makeText(this, "Sorry, this bungalow is already booked.", Toast.LENGTH_SHORT).show();
            return false;
        }

        DBHandler dbHandler = new DBHandler(this);
        User user = dbHandler.getUserByUsername(username);

        if (!user.canAfford(bungalow.getPrice())) {
            Toast.makeText(this, "Insufficient funds to book this bungalow.", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean success = dbHandler.bookBungalow(username, bungalow.getId(), bungalow.getPrice(), bookingDate);

        if (success) {
            Toast.makeText(this, "Bungalow successfully booked for " + bookingDate, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void disablePastDates(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());
    }

    private boolean isDateInThePast(String selectedDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        try {
            Date selected = sdf.parse(selectedDate);
            return selected.before(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}





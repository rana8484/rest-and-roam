package com.example.restandroam;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "myappdb";
    private static final int DB_VERSION = 4;

    private static final String USERS_TABLE = "users";
    private static final String USER_ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String BUDGET = "budget";

    private static final String BUNGALOWS_TABLE = "bungalows";
    private static final String BUNGALOW_ID = "id";
    private static final String BUNGALOW_NAME = "name";
    private static final String BUNGALOW_LOCATION = "location";
    private static final String BUNGALOW_RESERVED = "reserved";
    private static final String BUNGALOW_DATE = "reserved_date";
    private static final String BUNGALOW_PRICE = "price";
    private static final String BUNGALOW_CAPACITY = "capacity";
    private static final String BUNGALOW_IMG = "imgID";

    private static final String ACTIVITIES_TABLE = "activities";
    private static final String ACTIVITY_ID = "id";
    private static final String ACTIVITY_NAME = "name";
    private static final String ACTIVITY_LOCATION = "location";
    private static final String ACTIVITY_PRICE = "price";

    private static final String RESTAURANTS_TABLE = "restaurants";
    private static final String RESTAURANT_ID = "id";
    private static final String RESTAURANT_NAME = "name";
    private static final String RESTAURANT_LOCATION = "location";
    private static final String RESTAURANT_OPENING_HOURS = "opening_hours";

    private static final String BOOKINGS_TABLE = "bookings";
    private static final String BOOKING_DATE = "booking_date";


    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + USERS_TABLE + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT, " +
                PASSWORD + " TEXT, " +
                EMAIL + " TEXT, " +
                BUDGET + " DOUBLE)";

        String createBungalowsTable = "CREATE TABLE " + BUNGALOWS_TABLE + " (" +
                BUNGALOW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BUNGALOW_NAME + " TEXT, " +
                BUNGALOW_LOCATION + " TEXT, " +
                BUNGALOW_RESERVED + " BOOLEAN, " +
                BUNGALOW_DATE + " TEXT, " +
                BUNGALOW_PRICE + " DOUBLE, " +
                BUNGALOW_CAPACITY + " INTEGER," +
                BUNGALOW_IMG + " INTEGER)";

        String createActivitiesTable = "CREATE TABLE " + ACTIVITIES_TABLE + " (" +
                ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ACTIVITY_NAME + " TEXT, " +
                ACTIVITY_LOCATION + " TEXT, " +
                ACTIVITY_PRICE + " DOUBLE) ";

        String createRestaurantsTable = "CREATE TABLE " + RESTAURANTS_TABLE + " (" +
                RESTAURANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RESTAURANT_NAME + " TEXT, " +
                RESTAURANT_LOCATION + " TEXT, " +
                RESTAURANT_OPENING_HOURS + " TEXT)";

        db.execSQL(createUsersTable);
        db.execSQL(createBungalowsTable);
        db.execSQL(createActivitiesTable);
        db.execSQL(createRestaurantsTable);

        String CREATE_BOOKINGS_TABLE = "CREATE TABLE bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "bungalow_id INTEGER NOT NULL, " +
                "booking_date TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES users(id), " +
                "FOREIGN KEY(bungalow_id) REFERENCES bungalows(id))";
        db.execSQL(CREATE_BOOKINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BUNGALOWS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RESTAURANTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BOOKINGS_TABLE);
        onCreate(db);
    }

    public boolean addUser(String username, String password, String email, double budget) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(USERS_TABLE, new String[]{USER_ID}, USERNAME + "=?",
                new String[]{username}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(PASSWORD, password);
        values.put(EMAIL, email);
        values.put(BUDGET, budget);

        long result = db.insert(USERS_TABLE, null, values);
        db.close();

        return result != -1;
    }

    public User authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "users",
                new String[]{"id", "username", "password", "email", "budget"},
                "username = ? AND password = ?",
                new String[]{username, password},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") User user = new User(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getDouble(cursor.getColumnIndex("budget"))
            );
            cursor.close();
            return user;
        }

        cursor.close();
        return null; // User not found
    }

    @SuppressLint("Range")
    public double getUserBudget(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERS_TABLE, new String[]{BUDGET}, USERNAME + "=?", new String[]{username}, null, null, null);

        double budget = 0;
        if (cursor.moveToFirst()) {
            budget = cursor.getDouble(cursor.getColumnIndex(BUDGET));
        }
        cursor.close();
        return budget;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "users",
                new String[]{"id", "username", "password", "email", "budget"},
                "username = ?",
                new String[]{username},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") User user = new User(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getDouble(cursor.getColumnIndex("budget"))
            );
            cursor.close();
            return user;
        }

        cursor.close();
        return null;
    }

    public void updateUserBudget(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("budget", user.getBudget());

        db.update("users", values, "id = ?", new String[]{String.valueOf(user.getId())});
    }

    @SuppressLint("Range")
    public String getUserBungalow(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor userCursor = db.query(USERS_TABLE, new String[]{USER_ID}, USERNAME + "=?", new String[]{username}, null, null, null);
        int userId = -1;
        if (userCursor.moveToFirst()) {
            userId = userCursor.getInt(userCursor.getColumnIndex(USER_ID));
        }
        userCursor.close();

        if (userId == -1) {
            Log.e("getUserBungalow", "No user found for username: " + username);
            return "No user found";
        }

        Cursor bookingCursor = db.query(BOOKINGS_TABLE, new String[]{BUNGALOW_ID}, USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (bookingCursor.moveToFirst()) {
            int bungalowId = bookingCursor.getInt(bookingCursor.getColumnIndex(BUNGALOW_ID));
            bookingCursor.close();

            Cursor bungalowCursor = db.query(BUNGALOWS_TABLE, new String[]{BUNGALOW_NAME}, BUNGALOW_ID + "=?", new String[]{String.valueOf(bungalowId)}, null, null, null);
            if (bungalowCursor.moveToFirst()) {
                String bungalowName = bungalowCursor.getString(bungalowCursor.getColumnIndex(BUNGALOW_NAME));
                bungalowCursor.close();
                return bungalowName;
            } else {
                Log.e("getUserBungalow", "No bungalow found with ID: " + bungalowId);
            }
            bungalowCursor.close();
        } else {
            Log.e("getUserBungalow", "No bookings found for user ID: " + userId);
        }
        bookingCursor.close();

        return "No bungalow reserved";
    }


    public boolean addBungalow(Bungalow bungalow) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(
                BUNGALOWS_TABLE,
                new String[]{BUNGALOW_ID},
                BUNGALOW_NAME + "=? AND " + BUNGALOW_LOCATION + "=?",
                new String[]{bungalow.getName(), bungalow.getLocation()},
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(BUNGALOW_NAME, bungalow.getName());
        values.put(BUNGALOW_LOCATION, bungalow.getLocation());
        values.put(BUNGALOW_PRICE, bungalow.getPrice());
        values.put(BUNGALOW_DATE, bungalow.getReservedDate());
        values.put(BUNGALOW_RESERVED, bungalow.isReserved() ? 1 : 0);
        values.put(BUNGALOW_CAPACITY, bungalow.getCapacity());
        values.put(BUNGALOW_IMG, bungalow.getImgID());

        long result = db.insert(BUNGALOWS_TABLE, null, values);
        cursor.close();
        db.close();

        return result != -1;
    }

    public ArrayList<Bungalow> getAllBungalows() {
        ArrayList<Bungalow> bungalows = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(BUNGALOWS_TABLE, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint({"Range", "ResourceType"}) Bungalow bungalow = new Bungalow(
                    cursor.getInt(cursor.getColumnIndex(BUNGALOW_ID)),
                    cursor.getString(cursor.getColumnIndex(BUNGALOW_NAME)),
                    cursor.getString(cursor.getColumnIndex(BUNGALOW_LOCATION)),
                    cursor.getDouble(cursor.getColumnIndex(BUNGALOW_PRICE)),
                    cursor.getInt(cursor.getColumnIndex(BUNGALOW_RESERVED)) == 1,
                    cursor.getString(cursor.getColumnIndex(BUNGALOW_DATE)),
                    cursor.getInt(cursor.getColumnIndex(BUNGALOW_CAPACITY)),
                    cursor.getInt(cursor.getColumnIndex(BUNGALOW_IMG))
            );
            bungalows.add(bungalow);
        }
        cursor.close();
        return bungalows;
    }

    @SuppressLint("Range")
    public boolean bookBungalow(String username, int bungalowId, double price, String bookingDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            User user = getUserByUsername(username);
            if (user == null) {
                Log.e("BungalowBooking", "User not found.");
                return false;
            }

            Cursor bookingCursor = db.query(BOOKINGS_TABLE, new String[]{"bungalow_id"}, USER_ID + "=?",
                    new String[]{String.valueOf(user.getId())}, null, null, null);

            boolean hasBungalowReservation = false;
            while (bookingCursor.moveToNext()) {
                int cursorUserId = bookingCursor.getInt(bookingCursor.getColumnIndex(USER_ID));
                if (cursorUserId == user.getId()) {
                    hasBungalowReservation = true;
                    break;
                }
            }
            bookingCursor.close();

            if (hasBungalowReservation) {
                return false;
            }

            if (!user.canAfford(price)) {
                Log.w("BungalowBooking", "User cannot afford the bungalow. Required: " + price + ", Available: " + user.getBudget());
                return false;
            }

            user.deductBudget(price);
            updateUserBudget(user);

            ContentValues bookingValues = new ContentValues();
            bookingValues.put("user_id", user.getId());
            bookingValues.put("bungalow_id", bungalowId);
            bookingValues.put(BOOKING_DATE, bookingDate);
            db.insert(BOOKINGS_TABLE, null, bookingValues);

            reserveBungalow(bungalowId, bookingDate);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("BungalowBooking", "Error booking bungalow for user: " + username, e);
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public void reserveBungalow(int bungalowId, String reservedDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(BUNGALOWS_TABLE,
                new String[]{BUNGALOW_RESERVED},
                BUNGALOW_ID + "=?",
                new String[]{String.valueOf(bungalowId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int reserved = cursor.getInt(cursor.getColumnIndex(BUNGALOW_RESERVED));
            if (reserved == 1) {
                cursor.close();
                throw new IllegalStateException("This bungalow is already reserved.");
            }
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(BUNGALOW_RESERVED, 1);
        values.put(BUNGALOW_DATE, reservedDate);
        db.update(BUNGALOWS_TABLE, values, BUNGALOW_ID + "=?", new String[]{String.valueOf(bungalowId)});
    }

    public void updateBungalowAvailability() {
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = DateFormat.format("yyyy-MM-dd", new Date()).toString();

        Cursor cursor = db.query(BUNGALOWS_TABLE, new String[]{BUNGALOW_ID, BUNGALOW_DATE}, BUNGALOW_RESERVED + "=?",
                new String[]{"1"}, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String reservedDate = cursor.getString(cursor.getColumnIndex(BUNGALOW_DATE));

            if (reservedDate != null && reservedDate.compareTo(currentDate) < 0) {
                @SuppressLint("Range") int bungalowId = cursor.getInt(cursor.getColumnIndex(BUNGALOW_ID));

                ContentValues values = new ContentValues();
                values.put(BUNGALOW_RESERVED, 0);
                values.put(BUNGALOW_DATE, "");

                db.update(BUNGALOWS_TABLE, values, BUNGALOW_ID + "=?", new String[]{String.valueOf(bungalowId)});
            }
        }

        cursor.close();
    }

    public boolean addActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + ACTIVITIES_TABLE +
                " WHERE " + ACTIVITY_NAME + " = ? AND " + ACTIVITY_LOCATION + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{activity.getName(), activity.getLocation()});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(ACTIVITY_NAME, activity.getName());
        values.put(ACTIVITY_LOCATION, activity.getLocation());
        values.put(ACTIVITY_PRICE, activity.getPrice());

        long result = db.insert(ACTIVITIES_TABLE, null, values);

        cursor.close();

        return result != -1;
    }

    @SuppressLint("Range")
    public ArrayList<Activity> getActivitiesNearby(String location) {
        ArrayList<Activity> activities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor activityCursor = db.query(
                ACTIVITIES_TABLE,
                null,
                ACTIVITY_LOCATION + "=?",
                new String[]{location},
                null, null, null
        );

        while (activityCursor.moveToNext()) {
            @SuppressLint("Range")
            Activity activity = new Activity(
                    activityCursor.getInt(activityCursor.getColumnIndex(ACTIVITY_ID)),
                    activityCursor.getString(activityCursor.getColumnIndex(ACTIVITY_NAME)),
                    activityCursor.getString(activityCursor.getColumnIndex(ACTIVITY_LOCATION)),
                    activityCursor.getDouble(activityCursor.getColumnIndex(ACTIVITY_PRICE))
            );
            activities.add(activity);
        }

        activityCursor.close();
        return activities;
    }

    public boolean addRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(RESTAURANTS_TABLE, new String[]{RESTAURANT_ID},
                RESTAURANT_NAME + "=? AND " + RESTAURANT_LOCATION + "=?",
                new String[]{restaurant.getName(), restaurant.getLocation()}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RESTAURANT_NAME, restaurant.getName());
        values.put(RESTAURANT_LOCATION, restaurant.getLocation());
        values.put(RESTAURANT_OPENING_HOURS, restaurant.getOpeningHours());

        long result = db.insert(RESTAURANTS_TABLE, null, values);
        cursor.close();
        db.close();

        return result != -1;
    }

    public ArrayList<Restaurant> getRestaurantsNearby(String location) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(RESTAURANTS_TABLE, null, RESTAURANT_LOCATION + "=?", new String[]{location}, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") Restaurant restaurant = new Restaurant(
                    cursor.getInt(cursor.getColumnIndex(RESTAURANT_ID)),
                    cursor.getString(cursor.getColumnIndex(RESTAURANT_NAME)),
                    cursor.getString(cursor.getColumnIndex(RESTAURANT_LOCATION)),
                    cursor.getString(cursor.getColumnIndex(RESTAURANT_OPENING_HOURS))
            );
            restaurants.add(restaurant);
            Log.d("DBHandler", "Fetched restaurant: " + restaurant.getName());
        }
        cursor.close();
        return restaurants;
    }

}

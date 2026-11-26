
package com.example.apex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ApexCinema.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // USER TABLE
        db.execSQL("CREATE TABLE User (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "phone TEXT)");

        // MOVIE TABLE
        db.execSQL("CREATE TABLE Movie (" +
                "movieId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "duration INTEGER, " +
                "genre TEXT, " +
                "rating REAL, " +
                "posterImage TEXT, " +
                "description TEXT)");

        // THEATER TABLE
        db.execSQL("CREATE TABLE Theater (" +
                "theaterId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "location TEXT, " +
                "address TEXT, " +
                "totalSeats INTEGER)");

        // SHOWTIME TABLE
        db.execSQL("CREATE TABLE Showtime (" +
                "showtimeId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movieId INTEGER, " +
                "theaterId INTEGER, " +
                "date TEXT, " +
                "time TEXT, " +
                "availableSeats INTEGER, " +
                "FOREIGN KEY(movieId) REFERENCES Movie(movieId), " +
                "FOREIGN KEY(theaterId) REFERENCES Theater(theaterId))");

        // SEAT TABLE
        db.execSQL("CREATE TABLE Seat (" +
                "seatId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "theaterId INTEGER, " +
                "seatNumber TEXT, " +
                "\"row\" TEXT, " +
                "\"column\" TEXT, " +
                "seatType TEXT, " +
                "status TEXT, " +
                "UNIQUE(theaterId, seatNumber), " +
                "FOREIGN KEY(theaterId) REFERENCES Theater(theaterId))");

        // BOOKING TABLE
        db.execSQL("CREATE TABLE Booking (" +
                "bookingId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "showtimeId INTEGER, " +
                "bookingDate TEXT, " +
                "totalPrice REAL, " +
                "status TEXT, " +
                "FOREIGN KEY(userId) REFERENCES User(userId), " +
                "FOREIGN KEY(showtimeId) REFERENCES Showtime(showtimeId))");

        // TICKET TABLE
        db.execSQL("CREATE TABLE Ticket (" +
                "ticketId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "bookingId INTEGER, " +
                "seatId INTEGER, " +
                "price REAL, " +
                "qrCode TEXT, " +
                "FOREIGN KEY(bookingId) REFERENCES Booking(bookingId), " +
                "FOREIGN KEY(seatId) REFERENCES Seat(seatId))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Ticket");
        db.execSQL("DROP TABLE IF EXISTS Booking");
        db.execSQL("DROP TABLE IF EXISTS Seat");
        db.execSQL("DROP TABLE IF EXISTS Showtime");
        db.execSQL("DROP TABLE IF EXISTS Theater");
        db.execSQL("DROP TABLE IF EXISTS Movie");
        db.execSQL("DROP TABLE IF EXISTS User");

        onCreate(db);
    }

    // REGISTER USER

    // REGISTER USER
    public boolean registerUser(String name, String email,
                                String password, String phone) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name.trim());
        values.put("email", email.trim().toLowerCase());
        values.put("password", password);
        values.put("phone", phone.trim());

        long result = db.insert("User", null, values);

        return result != -1;
    }


    // LOGIN USER

    // LOGIN USER
    public boolean loginUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT userId FROM User " +
                        "WHERE LOWER(TRIM(email)) = LOWER(?) " +
                        "AND password = ?",
                new String[]{
                        email.trim(),
                        password
                }
        );

        boolean result = cursor.moveToFirst();

        cursor.close();

        return result;
    }


}

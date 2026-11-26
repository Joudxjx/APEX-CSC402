package com.example.apex.bookings;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apex.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class BookingsActivity extends AppCompatActivity {

    private ImageView ivQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookings);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                });

        // Initialize QR Code ImageView
        ivQRCode = findViewById(R.id.ivQRCode);

        // Generate QR Code
        generateQRCode();
    }

    private void generateQRCode() {

        Log.d("BookingsQR", "QR generation started");

        String ticketData =
                "APEX CINEMA TICKET\n" +
                        "Ticket ID: 101\n" +
                        "Booking ID: 40437775990056\n" +
                        "Movie: Cosmic Horizon\n" +
                        "Hall: 4\n" +
                        "Seats: P6, P9\n" +
                        "Status: CONFIRMED";

        try {

            // Generate QR Code
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    ticketData,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            // Create Bitmap
            Bitmap bitmap = Bitmap.createBitmap(
                    width,
                    height,
                    Bitmap.Config.ARGB_8888
            );

            // Draw QR Code pixels
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {

                    bitmap.setPixel(
                            x,
                            y,
                            bitMatrix.get(x, y)
                                    ? Color.BLACK
                                    : Color.WHITE
                    );
                }
            }

            // Display QR Code
            ivQRCode.setImageBitmap(bitmap);
            ivQRCode.invalidate();

            Log.d("BookingsQR", "QR Code displayed successfully");

        } catch (WriterException e) {

            Log.e("BookingsQR", "QR Code generation failed", e);

        }
    }
}
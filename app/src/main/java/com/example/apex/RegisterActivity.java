
package com.example.apex;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPhone;
    EditText etPassword, etConfirmPassword;

    Button btnCreateAccount;
    TextView tvLogin;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvLogin = findViewById(R.id.tvLogin);

        tvLogin.setOnClickListener(v -> finish());

        btnCreateAccount.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {


        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword =
                etConfirmPassword.getText().toString();

        // Check empty fields
        if (name.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty()) {

            showError("Please fill in all fields");
            return;
        }
        boolean validEmail =
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        && email.substring(email.indexOf("@") + 1).contains(".");

        if (!validEmail) {
            showError(
                    "Invalid Email Address\n\n" +
                            "Please enter a valid email.\n" +
                            "Example: name@gmail.com"
            );
            return;
        }
        // Password length validation
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        // Confirm password validation
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Register user in database
        boolean inserted = databaseHelper.registerUser(
                name, email, password, phone
        );

        if (inserted) {

            new AlertDialog.Builder(this)
                    .setTitle("Success!")
                    .setMessage("Your account has been created successfully.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        finish();
                    })
                    .show();

        } else {
            showError("Email already exists or registration failed");
        }
    }

    // Error dialog
    private void showError(String message) {

        new AlertDialog.Builder(this)
                .setTitle("Registration Error")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Try Again", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }
}

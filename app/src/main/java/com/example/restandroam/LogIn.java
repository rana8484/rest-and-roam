package com.example.restandroam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {

    private EditText usernameEdt, passwordEdt, emailEdt, budgetEdt;
    private Button actionBtn, toggleBtn;
    private DBHandler dbHandler;
    private boolean isSignUp = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        usernameEdt = new EditText(this);
        usernameEdt = findViewById(R.id.usernameEditText);
        passwordEdt = new EditText(this);
        passwordEdt = findViewById(R.id.passwordEditText);
        emailEdt = new EditText(this);
        emailEdt = findViewById(R.id.emailEditText);
        budgetEdt = new EditText(this);
        budgetEdt = findViewById(R.id.budgetEditText);

        actionBtn = new Button(this);
        actionBtn = findViewById(R.id.actionButton);
        toggleBtn = new Button(this);
        toggleBtn = findViewById(R.id.toggleButton);

        dbHandler = new DBHandler(LogIn.this);

        toggleBtn.setOnClickListener(e-> {
                if (isSignUp) {
                    isSignUp = false;
                    toggleBtn.setText("Go to Sign Up");
                    actionBtn.setText("Login");
                    emailEdt.setVisibility(View.GONE);
                    budgetEdt.setVisibility(View.GONE);
                } else {
                    isSignUp = true;
                    toggleBtn.setText("Go to Login");
                    actionBtn.setText("Sign Up");
                    emailEdt.setVisibility(View.VISIBLE);
                    budgetEdt.setVisibility(View.VISIBLE);
                }

        });

        actionBtn.setOnClickListener(e->{
            String username = usernameEdt.getText().toString();
            String password = passwordEdt.getText().toString();

            if (isSignUp) {
                String email = emailEdt.getText().toString();
                String budgetStr = budgetEdt.getText().toString();
                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || budgetStr.isEmpty()) {
                    Toast.makeText(LogIn.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                double budget = Double.parseDouble(budgetStr);
                boolean isSuccess = dbHandler.addUser(username, password, email, budget);

                if (isSuccess) {
                    Toast.makeText(LogIn.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogIn.this, "Username already exists.", Toast.LENGTH_SHORT).show();
                }
            } else {
                User authenticatedUser = dbHandler.authenticateUser(username, password);

                if (authenticatedUser != null) {
                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                    intent.putExtra("username", authenticatedUser.getUsername());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogIn.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

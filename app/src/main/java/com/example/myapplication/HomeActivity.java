package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button expensesListButton, addExpenseButton, logoutButton, resetDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Fixed savedInstanceState typo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // Initialize buttons
        expensesListButton = findViewById(R.id.expenseList);
        addExpenseButton = findViewById(R.id.addExpense);
        logoutButton = findViewById(R.id.logout);
        resetDataButton = findViewById(R.id.resetData); // Ensure the ID matches your layout file

        expensesListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExpensesListActivity.class);
                startActivity(intent);
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddExpenses.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        resetDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler dbHandler = new DBHandler(HomeActivity.this);
                dbHandler.deleteAllExpenses();
                Toast.makeText(HomeActivity.this, "Database reset successfully", Toast.LENGTH_SHORT).show(); // Corrected context
            }
        });
    }
}

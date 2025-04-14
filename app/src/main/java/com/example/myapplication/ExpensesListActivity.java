package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.view.MenuItem;


public class ExpensesListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewExpenses;
    private ExpensesAdapter expensesAdapter;
    private List<Expense> expenseList;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_list);

        // Initialize the toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());



        // Initialize RecyclerView
        recyclerViewExpenses = findViewById(R.id.recyclerView_trips);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));

        // Initialize DBHandler
        dbHandler = new DBHandler(this);

        // Initialize Expense List
        expenseList = new ArrayList<>();

        // Fetch all expenses from the database
        Cursor cursor = dbHandler.getAllExpenses();
        if (cursor.moveToFirst()) {
            do {
                // Extract data from cursor
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String destination = cursor.getString(cursor.getColumnIndexOrThrow("destination"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String riskAssessment = cursor.getString(cursor.getColumnIndexOrThrow("risk_assessment"));

                // Create Expense object and add to the list
                expenseList.add(new Expense(id, title, destination, amount, date, description, riskAssessment));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Set up Adapter
        expensesAdapter = new ExpensesAdapter(this, expenseList);
        recyclerViewExpenses.setAdapter(expensesAdapter);
    }

    // Handle toolbar actions, including back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button press
            onBackPressed(); // Navigate back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

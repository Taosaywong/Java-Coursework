package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExpenses extends AppCompatActivity {

    private EditText tripNameEditText, amountEditText, dateEditText, descriptionEditText, destinationEditText;
    private RadioGroup riskAssessmentRadioGroup;
    private Button saveButton, backButton;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expenses_trip);

        // Initialize DBHandler
        dbHandler = new DBHandler(this);

        // Initialize UI elements
        tripNameEditText = findViewById(R.id.expense_title);
        destinationEditText = findViewById(R.id.destination);
        amountEditText = findViewById(R.id.amount);
        dateEditText = findViewById(R.id.date);
        descriptionEditText = findViewById(R.id.description);
        riskAssessmentRadioGroup = findViewById(R.id.risk_assessment_group);
        saveButton = findViewById(R.id.button_save_trip);
        backButton = findViewById(R.id.back_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddExpenses.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // ✅ Save new expense
    private void saveExpense() {
        String trip_name = tripNameEditText.getText().toString().trim();
        String destination = destinationEditText.getText().toString().trim();
        String amountStr = amountEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        int selectedRiskId = riskAssessmentRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRiskButton = findViewById(selectedRiskId);
        String riskAssessment = selectedRiskButton != null ? selectedRiskButton.getText().toString() : "";

        // Validate inputs
        if (TextUtils.isEmpty(trip_name) || TextUtils.isEmpty(destination) || TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(date)) {
            Toast.makeText(AddExpenses.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // ✅ Insert new expense
        long newExpenseId = dbHandler.addExpense(trip_name, destination, amount, date, description, riskAssessment);

        if (newExpenseId == -1) {
            Toast.makeText(AddExpenses.this, "Failed to add expense", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddExpenses.this, "Expense added successfully", Toast.LENGTH_SHORT).show();
        }

        // Return to HomeActivity
        Intent intent = new Intent(AddExpenses.this, HomeActivity.class);
        startActivity(intent);
    }

}

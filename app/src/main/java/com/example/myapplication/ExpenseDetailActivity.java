package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ExpenseDetailActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextAmount, editTextDate, editTextDescription, editTextDestination;
    private RadioGroup radioGroupRisk;
    private RadioButton radioButtonYes, radioButtonNo;
    private Button buttonSaveExpense, buttonBack;
    private DBHandler dbHandler;
    private int expenseId = -1; // Default value for a new expense

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_detail);

        // Initialize UI Elements
        editTextTitle = findViewById(R.id.title);
        editTextDestination = findViewById(R.id.destination);
        editTextAmount = findViewById(R.id.amount);
        editTextDate = findViewById(R.id.date);
        editTextDescription = findViewById(R.id.description);
        radioGroupRisk = findViewById(R.id.radioGroup_risk_assessment);
        radioButtonYes = findViewById(R.id.radio_yes);
        radioButtonNo = findViewById(R.id.radio_no);
        buttonSaveExpense = findViewById(R.id.button_update_expense);
        buttonBack = findViewById(R.id.back_button);

        dbHandler = new DBHandler(this);

        // Retrieve expenseId from Intent (for editing an existing expense)
        expenseId = getIntent().getIntExtra("expenseId", -1);
        if (expenseId != -1) {
            loadExpenseDetails(expenseId);
        }

        // Save button logic
        buttonSaveExpense.setOnClickListener(view -> saveExpense());

        // Back button action
        buttonBack.setOnClickListener(view -> {
            Intent intent = new Intent(ExpenseDetailActivity.this, ExpensesListActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadExpenseDetails(int expenseId) {
        Expense expense = dbHandler.getExpenseById(expenseId);
        if (expense != null) {
            editTextTitle.setText(expense.getExpenseTitle());
            editTextDestination.setText(expense.getDestination());
            editTextAmount.setText(String.valueOf(expense.getAmount()));
            editTextDate.setText(expense.getDate());
            editTextDescription.setText(expense.getDescription());

            if (expense.getRiskAssessment().equalsIgnoreCase("Yes")) {
                radioButtonYes.setChecked(true);
            } else {
                radioButtonNo.setChecked(true);
            }
        }
    }

    private void saveExpense() {
        String title = editTextTitle.getText().toString().trim();
        String destination = editTextDestination.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String riskAssessment = radioButtonYes.isChecked() ? "Yes" : "No";

        if (title.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

       if (expenseId != -1) {
            // Update existing expense
            dbHandler.updateExpense(expenseId, title, destination, amount, date, description, riskAssessment);
            Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
        }

        // Return to Expenses List
        Intent intent = new Intent(ExpenseDetailActivity.this, ExpensesListActivity.class);
        startActivity(intent);
    }
}

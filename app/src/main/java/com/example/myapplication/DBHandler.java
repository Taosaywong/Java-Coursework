package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "expensesApp";
    private static final int DB_VERSION = 2;

    // Expenses Table
    private static final String EXPENSES_TABLE_NAME = "expenses";
    private static final String EXPENSE_ID_COL = "id";
    private static final String TITLE_COL = "title";
    private static final String DESTINATION_COL = "destination";
    private static final String AMOUNT_COL = "amount";
    private static final String DATE_COL = "date";
    private static final String DESCRIPTION_COL = "description";
    private static final String RISK_ASSESSMENT_COL = "risk_assessment";

    // Users Table
    private static final String USERS_TABLE_NAME = "users";
    private static final String USER_ID_COL = "id";
    private static final String EMAIL_COL = "email";
    private static final String PASSWORD_COL = "password";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Expenses Table
        String createExpensesTableQuery = "CREATE TABLE " + EXPENSES_TABLE_NAME + " ("
                + EXPENSE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT NOT NULL, "
                + DESTINATION_COL + " TEXT NOT NULL, "
                + AMOUNT_COL + " REAL NOT NULL, "
                + DATE_COL + " TEXT NOT NULL, "
                + DESCRIPTION_COL + " TEXT, "
                + RISK_ASSESSMENT_COL + " TEXT NOT NULL)";
        db.execSQL(createExpensesTableQuery);

        // Create Users Table
        String createUsersTableQuery = "CREATE TABLE " + USERS_TABLE_NAME + " ("
                + USER_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL_COL + " TEXT UNIQUE NOT NULL, "
                + PASSWORD_COL + " TEXT NOT NULL)";
        db.execSQL(createUsersTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        onCreate(db);
    }

    // ==========================
    // Expense Table Methods
    // ==========================

    public long addExpense(String title, String destination, double amount, String date, String description, String riskAssessment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_COL, title);
        values.put(DESTINATION_COL, destination);
        values.put(AMOUNT_COL, amount);
        values.put(DATE_COL, date);
        values.put(DESCRIPTION_COL, description);
        values.put(RISK_ASSESSMENT_COL, riskAssessment);

        long result = db.insert(EXPENSES_TABLE_NAME, null, values);
        db.close();

        return result; // ✅ Returns the inserted row ID (or -1 if failed)
    }


    public Expense getExpenseById(int expenseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Expense expense = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + EXPENSES_TABLE_NAME + " WHERE " + EXPENSE_ID_COL + " = ?",
                new String[]{String.valueOf(expenseId)});

        if (cursor != null && cursor.moveToFirst()) {
            expense = new Expense(
                    cursor.getInt(cursor.getColumnIndexOrThrow(EXPENSE_ID_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESTINATION_COL)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(AMOUNT_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DATE_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RISK_ASSESSMENT_COL))
            );
            cursor.close();
        }

        db.close();
        return expense;
    }

    public List<Expense> getAllExpensesAsList() {
        List<Expense> expenseList = new ArrayList<>();
        Cursor cursor = getAllExpenses();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String destination = cursor.getString(cursor.getColumnIndexOrThrow("destination"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String riskAssessment = cursor.getString(cursor.getColumnIndexOrThrow("risk_assessment"));


                Expense expense = new Expense(id, title, destination, amount, date, description, riskAssessment);
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenseList;
    }


    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + EXPENSES_TABLE_NAME, null);
    }

    public void updateExpense(int id, String title, String destination, double amount, String date, String description, String riskAssessment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_COL, title);
        values.put(DESTINATION_COL, destination);
        values.put(AMOUNT_COL, amount);
        values.put(DATE_COL, date);
        values.put(DESCRIPTION_COL, description);
        values.put(RISK_ASSESSMENT_COL, riskAssessment);
        db.update(EXPENSES_TABLE_NAME, values, EXPENSE_ID_COL + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXPENSES_TABLE_NAME, EXPENSE_ID_COL + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllExpenses() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + EXPENSES_TABLE_NAME);
        db.close();
    }

    // ==========================
    // User Table Methods
    // ==========================

    public boolean addNewUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Hash the password before storing
        String hashedPassword = hashPassword(password);
        values.put(EMAIL_COL, email);
        values.put(PASSWORD_COL, hashedPassword);

        long result = db.insert(USERS_TABLE_NAME, null, values);
        db.close();

        return result != -1;  // Return true if insertion was successful
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Hash the entered password to match the stored hash
        String hashedPassword = hashPassword(password);

        String query = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + EMAIL_COL + " = ? AND " + PASSWORD_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, hashedPassword});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return isValid;
    }


    public void deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USERS_TABLE_NAME, EMAIL_COL + " = ?", new String[]{email});
        db.close();
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + USERS_TABLE_NAME);
        db.close();
    }

    // ==========================
    // Secure Password Hashing
    // ==========================

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback (not secure)
        }
    }
}
